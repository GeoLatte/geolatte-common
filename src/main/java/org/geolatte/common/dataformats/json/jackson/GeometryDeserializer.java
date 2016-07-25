/*
 * This file is part of the GeoLatte project. This code is licenced under
 * the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 *
 * Copyright (C) 2010 - 2010 and Ownership of code is shared by:
 * Qmino bvba - Romeinsestraat 18 - 3001 Heverlee  (http://www.Qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.common.dataformats.json.jackson;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import org.geolatte.geom.DimensionalFlag;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.GeometryCollection;
import org.geolatte.geom.LineString;
import org.geolatte.geom.LinearRing;
import org.geolatte.geom.MultiLineString;
import org.geolatte.geom.MultiPoint;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.geolatte.geom.PointCollectionFactory;
import org.geolatte.geom.PointSequence;
import org.geolatte.geom.PointSequenceBuilder;
import org.geolatte.geom.PointSequenceBuilders;
import org.geolatte.geom.Polygon;
import org.geolatte.geom.crs.CrsId;

/**
 * General deserializer responsable for the deserialization of all geometries.
 * <p>
 * <i>Creation-Date</i>: 30-aug-2010<br>
 * <i>Creation-Time</i>: 18:17:52<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class GeometryDeserializer<T extends Geometry> extends GeoJsonDeserializer<T> {

    public GeometryDeserializer(JsonMapper owner, Class<T> clazz) {
        super(owner, clazz);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    protected T deserialize(JsonParser jsonParser) throws IOException {
        String type = getStringParam("type", "Invalid GeoJSON, type property required.");
        //TODO -- spec also states that if CRS element is null, no CRS should be assumed.
        // Default srd = WGS84 according to the GeoJSON specification
        Integer srid = getSrid();
        CrsId crsId =  srid == null ? parent.getDefaultCrsId() : CrsId.valueOf(srid);
        if ("GeometryCollection".equals(type)) {
            GeometryCollection result = asGeomCollection(crsId);
            if (getDeserializerClass().isAssignableFrom(GeometryCollection.class)) {
                return (T) result;
            } else {
                throw new IOException("Json is a valid GeometryCollection serialization, but this does not correspond with " +
                        "the expected outputtype of the deserializer (" + getDeserializerClass().getSimpleName() + ")");
            }
        } else {
            List coordinates = getTypedParam("coordinates", "Invalid or missing coordinates property", ArrayList.class);
            // We risk a classcast exception for each of these calls, since every call specifically states which list
            // he needs.
            try {
                if ("Point".equals(type)) {
                    Point p = asPoint(coordinates, crsId);
                    if (getDeserializerClass().isAssignableFrom(Point.class)) {
                        return (T) p;
                    } else {
                        throw new IOException("Json is a valid Point serialization, but this does not correspond with " +
                                "the expected outputtype of the deserializer (" + getDeserializerClass().getSimpleName() + ")");
                    }
                } else if ("MultiPoint".equals(type)) {
                    MultiPoint result = asMultiPoint(coordinates, crsId);
                    if (getDeserializerClass().isAssignableFrom(MultiPoint.class)) {
                        return (T) result;
                    } else {
                        throw new IOException("Json is a valid MultiPoint serialization, but this does not correspond with " +
                                "the expected outputtype of the deserializer (" + getDeserializerClass().getSimpleName() + ")");
                    }
                } else if ("LineString".equals(type)) {
                    LineString result = asLineString(coordinates, crsId);
                    if (getDeserializerClass().isAssignableFrom(LineString.class)) {
                        return (T) result;
                    } else {
                        throw new IOException("Json is a valid LineString serialization, but this does not correspond with " +
                                "the expected outputtype of the deserializer (" + getDeserializerClass().getSimpleName() + ")");

                    }
                } else if ("MultiLineString".equals(type)) {
                    MultiLineString result = asMultiLineString(coordinates, crsId);
                    if (getDeserializerClass().isAssignableFrom(MultiLineString.class)) {
                        return (T) result;
                    } else {
                        throw new IOException("Json is a valid MultiLineString serialization, but this does not correspond with " +
                                "the expected outputtype of the deserializer (" + getDeserializerClass().getSimpleName() + ")");

                    }
                } else if ("Polygon".equals(type)) {
                    Polygon result = asPolygon(coordinates, crsId);
                    if (getDeserializerClass().isAssignableFrom(Polygon.class)) {
                        return (T) result;
                    } else {
                        throw new IOException("Json is a valid Polygon serialization, but this does not correspond with " +
                                "the expected outputtype of the deserializer (" + getDeserializerClass().getSimpleName() + ")");

                    }
                } else if ("MultiPolygon".equals(type)) {
                    MultiPolygon result = asMultiPolygon(coordinates, crsId);
                    if (getDeserializerClass().isAssignableFrom(MultiPolygon.class)) {
                        return (T) result;
                    } else {
                        throw new IOException("Json is a valid MultiPolygon serialization, but this does not correspond with " +
                                "the expected outputtype of the deserializer (" + getDeserializerClass().getSimpleName() + ")");

                    }
                } else {
                    throw new IOException("Unknown type for a geometry deserialization");
                }
            }
            catch (ClassCastException e) {
                // this classcast can be thrown since coordinates is passed on as a List but the different methods
                // expect a specific list, so an implicit cast is performed there.
                throw new IOException("Coordinate array is not of expected type with respect to given type parameter.");
            }
        }
    }


    /**
     * Parses the JSON as a GeometryCollection.
     *
     * @param crsId the crsId of this collection.
     * @throws IOException if the given json does not correspond to a geometrycollection or can be parsed as such
     * @return an instance of a geometrycollection
     */
    private GeometryCollection asGeomCollection(CrsId crsId) throws IOException {
        try {
            String subJson = getSubJson("geometries", "A geometrycollection requires a geometries parameter")
                    .replaceAll(" ", "");
            String noSpaces = subJson.replace(" ", "");
            if (noSpaces.contains("\"crs\":{"))
            {
                throw new IOException("Specification of the crs information is forbidden in child elements. Either " +
                                      "leave it out, or specify it at the toplevel object.");
            }

            // add crs to each of the json geometries, otherwise they are deserialized with an undefined crs and the
            // collection will then also have an undefined crs.
            subJson = setCrsIds(subJson, crsId);

            List<Geometry> geometries = parent.collectionFromJson(subJson, Geometry.class);

            return new GeometryCollection(geometries.toArray(new Geometry[geometries.size()]));

        } catch (JsonException e) {
            throw new IOException(e);
        }
    }

    /**
     * Adds the given crs to all json objects. Used in {@link #asGeomCollection(org.geolatte.geom.crs.CrsId)}.
     *
     * @param json  the json string representing an array of geometry objects without crs property.
     * @param crsId the crsId
     * @return the same json string with the crs property filled in for each of the geometries.
     */
    private String setCrsIds(String json, CrsId crsId) throws IOException, JsonException {

        /* Prepare a geojson crs structure
        "crs": {
            "type": "name",
                    "properties": {
                "name": "EPSG:xxxx"
            }
        }
        */
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("name", crsId.getAuthority() + ":" + crsId.getCode());
        HashMap<String, Object> type = new HashMap<String, Object>();
        type.put("type", "name");
        type.put("properties", properties);

        List<HashMap> result = parent.collectionFromJson(json, HashMap.class);
        
        for (HashMap geometryJson : result) {
            geometryJson.put("crs", type);
        }

        return parent.toJson(result);
    }


    /**
     * Parses the JSON as a MultiPolygon geometry
     *
     *
     * @param coords the coordinates of a multipolygon which is just a list of coordinates of polygons.
     * @param crsId
     * @return an instance of multipolygon
     * @throws IOException if the given json does not correspond to a multipolygon or can be parsed as such
     */
    private MultiPolygon asMultiPolygon(List<List<List<List>>> coords, CrsId crsId) throws IOException {
        if (coords == null || coords.isEmpty()) {
            throw new IOException("A multipolygon should have at least one polyon.");
        }
        Polygon[] polygons = new Polygon[coords.size()];
        for (int i = 0; i < coords.size(); i++) {
            polygons[i] = asPolygon(coords.get(i), crsId);
        }
        return new MultiPolygon(polygons);
    }

    /**
     * Parses the JSON as a MultiLineString geometry
     *
     *
     * @param coords the coordinates of a multlinestring (which is a list of coordinates of linestrings)
     * @param crsId
     * @return an instance of multilinestring
     * @throws IOException if the given json does not correspond to a multilinestring or can be parsed as such
     */
    private MultiLineString asMultiLineString(List<List<List>> coords, CrsId crsId) throws IOException {
        if (coords == null || coords.isEmpty()) {
            throw new IOException("A multilinestring requires at least one line string");
        }
        LineString[] lineStrings = new LineString[coords.size()];
        for (int i = 0; i < lineStrings.length; i++) {
            lineStrings[i] = asLineString(coords.get(i), crsId);
        }
        return new MultiLineString(lineStrings);
    }

    /**
     * Parses the JSON as a polygon geometry
     *
     *
     * @param coords the coordinate array corresponding with the polygon (a list containing rings, each of which
     *               contains a list of coordinates (which in turn are lists of numbers)).
     * @param crsId
     * @return An instance of polygon
     * @throws IOException if the given json does not correspond to a polygon or can be parsed as such.
     */
    private Polygon asPolygon(List<List<List>> coords, CrsId crsId) throws IOException {
        if (coords == null || coords.isEmpty()) {
            throw new IOException("A polygon requires the specification of its outer ring");
        }
        List<LinearRing> rings = new ArrayList<LinearRing>();
        try { 
        for (List<List> ring : coords) {
            PointSequence ringCoords = getPointSequence(ring, crsId);
            rings.add(new LinearRing(ringCoords));
        }
            return new Polygon(rings.toArray(new LinearRing[]{}));
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid Polygon: " + e.getMessage(), e);
        }

    }

    /**
     * Parses the JSON as a point geometry.
     *
     *
     * @param coords the coordinates (a list with an x and y value)
     * @param crsId
     * @return An instance of point
     * @throws IOException if the given json does not correspond to a point or can not be parsed to a point.
     */
    private Point asPoint(List coords, CrsId crsId) throws IOException {
        if (coords != null && coords.size() >= 2) {
            ArrayList<List> coordinates = new ArrayList<List>();
            coordinates.add(coords);
            return new Point(getPointSequence(coordinates, crsId));
        } else {
            throw new IOException("A point must has exactly one coordinate (an x, a y and possibly a z value). Additional numbers in the coordinate are permitted but ignored.");
        }
    }

    /**
     * Parses the JSON as a linestring geometry
     *
     *
     * @param coords The coordinates for the linestring, which is a list of coordinates (which in turn are lists of
     *               two values, x and y)
     * @param crsId
     * @return An instance of linestring
     * @throws IOException if the given json does not correspond to a linestring or can be parsed as such.
     */
    private LineString asLineString(List<List> coords, CrsId crsId) throws IOException {
        if (coords == null || coords.size() < 2) {
            throw new IOException("A linestring requires a valid series of coordinates (at least two coordinates)");
        }
        PointSequence coordinates = getPointSequence(coords, crsId);
        return new LineString(coordinates);
    }

    /**
     * Parses the JSON as a linestring geometry
     *
     *
     * @param coords A list of coordinates of points.
     * @param crsId
     * @return An instance of linestring
     * @throws IOException if the given json does not correspond to a linestring or can be parsed as such.
     */
    private MultiPoint asMultiPoint(List<List> coords, CrsId crsId) throws IOException {
        if (coords == null || coords.isEmpty()) {
            throw new IOException("A multipoint contains at least one point");
        }
        Point[] points = new Point[coords.size()];
        for (int i = 0; i < coords.size(); i++) {
            points[i] = asPoint(coords.get(i), crsId);
        }
        return new MultiPoint(points);
    }

    /**
     * This method takes in a list of lists and returns a <code>PointSequence</code> that correspond with that list.  The elements
     * in the outer list are lists that contain numbers (either integers or doubles). Each of those lists must have
     * at least two values, which are interpreted as x and y. If a third value is present, it is interpreted as the z value.
     * If more than three values are present, they are ignored. This is consistent with the geojson specification that states:
     * <i>
     * A position is represented by an array of numbers. There must be at least two elements, and may be more.
     * The order of elements must follow x, y, z order (easting, northing, altitude for coordinates in a projected
     * coordinate reference system, or longitude, latitude, altitude for coordinates in a geographic coordinate
     * reference system). Any number of additional elements are allowed -- interpretation and meaning of additional
     *  elements is beyond the scope of this specification
     * </i>
     *
     * @param entry a list of lists of numbers representing a list of points.
     * @return an <code>PointSequence</code> containing the list of points.
     * @throws IOException if the conversion can not be executed (eg because one of the innerlists contains more or
     *                     less than two doubles.
     */
    private PointSequence getPointSequence(List<List> entry, CrsId crsId) throws IOException {
        {
            double[] coordinates2d = new double[entry.size()*2];
            double[] zValues = new double[entry.size()];
            boolean haszValues = false;
            for (int i = 0; i < entry.size(); i++) {
                List current = entry.get(i);
                if (current.size() < 2) {
                    throw new IOException("A coordinate must always contain at least two numbers");
                }
                for (Object value : current) {
                    if (!(value instanceof Integer || value instanceof Double)) {
                        throw new IOException("A coordiante only permits numbers.");
                    }
                }
                Double type = null;
                Double x = parseDefault(String.valueOf(current.get(0)), type);
                Double y = parseDefault(String.valueOf(current.get(1)), type);
                Double z = null;
                if (current.size() == 3) {
                    z = parseDefault(String.valueOf(current.get(2)), type);
                }
                if (x == null || y == null) {
                    // I don't see how this is possible....So you won't be able to unittest this case I think.
                    throw new IOException("Unexpected number format for coordinate?");
                }
                coordinates2d[2*i] = x;
                coordinates2d[2*i+1] = y;
                if (z != null) {
                    zValues[i] = z;
                    haszValues = true;
                } else {
                    zValues[i] = Double.NaN;
                }
            }
            if (haszValues) {
                PointSequenceBuilder builder = PointSequenceBuilders.fixedSized(entry.size(), DimensionalFlag.d3D, crsId);
                for (int i = 0; i < entry.size(); i++){
                    builder.add(coordinates2d[2*i], coordinates2d[2*i+1], zValues[i]);
                }
                return builder.toPointSequence();
            }  else {
                return PointCollectionFactory.create(coordinates2d, DimensionalFlag.d2D, crsId);
            }
        }
    }
}
