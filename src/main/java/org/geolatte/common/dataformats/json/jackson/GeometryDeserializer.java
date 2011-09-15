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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import org.codehaus.jackson.JsonParser;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private GeometryFactory geomFact;

    public GeometryDeserializer(JsonMapper owner, Class<T> clazz) {
        super(owner, clazz);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    protected T deserialize(JsonParser jsonParser) throws IOException {
        String type = getStringParam("type", "Invalid GeoJSON, type property required.");
        // Default srd = WGS84 according to the GeoJSON specification
        Integer srid = getSrid();
        int sridValue = srid == null ? DEFAULT_SRID : srid;

        geomFact = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), sridValue);
        if ("GeometryCollection".equals(type)) {
            GeometryCollection result = asGeomCollection();
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
                    Point p = asPoint(coordinates);
                    if (getDeserializerClass().isAssignableFrom(Point.class)) {
                        return (T) p;
                    } else {
                        throw new IOException("Json is a valid Point serialization, but this does not correspond with " +
                                "the expected outputtype of the deserializer (" + getDeserializerClass().getSimpleName() + ")");
                    }
                } else if ("MultiPoint".equals(type)) {
                    MultiPoint result = asMultiPoint(coordinates);
                    if (getDeserializerClass().isAssignableFrom(MultiPoint.class)) {
                        return (T) result;
                    } else {
                        throw new IOException("Json is a valid MultiPoint serialization, but this does not correspond with " +
                                "the expected outputtype of the deserializer (" + getDeserializerClass().getSimpleName() + ")");
                    }
                } else if ("LineString".equals(type)) {
                    LineString result = asLineString(coordinates);
                    if (getDeserializerClass().isAssignableFrom(LineString.class)) {
                        return (T) result;
                    } else {
                        throw new IOException("Json is a valid LineString serialization, but this does not correspond with " +
                                "the expected outputtype of the deserializer (" + getDeserializerClass().getSimpleName() + ")");

                    }
                } else if ("MultiLineString".equals(type)) {
                    MultiLineString result = asMultiLineString(coordinates);
                    if (getDeserializerClass().isAssignableFrom(MultiLineString.class)) {
                        return (T) result;
                    } else {
                        throw new IOException("Json is a valid MultiLineString serialization, but this does not correspond with " +
                                "the expected outputtype of the deserializer (" + getDeserializerClass().getSimpleName() + ")");

                    }
                } else if ("Polygon".equals(type)) {
                    Polygon result = asPolygon(coordinates);
                    if (getDeserializerClass().isAssignableFrom(Polygon.class)) {
                        return (T) result;
                    } else {
                        throw new IOException("Json is a valid Polygon serialization, but this does not correspond with " +
                                "the expected outputtype of the deserializer (" + getDeserializerClass().getSimpleName() + ")");

                    }
                } else if ("MultiPolygon".equals(type)) {
                    MultiPolygon result = asMultiPolygon(coordinates);
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
     * Parses the JSON as a GeometryCollection
     *
     * @return an instance of a geometrycollection
     * @throws IOException if the given json does not correspond to a geometrycollection or can be parsed as such
     */
    private GeometryCollection asGeomCollection() throws IOException {
        try {
            String subJson = getSubJson("geometries", "A geometrycollection requires a geometries parameter").replaceAll(" ", "");
            String noSpaces = subJson.replace(" ", "");
            if (noSpaces.contains("\"crs\":{"))
            {
                throw new IOException("Specification of the crs information is forbidden in child elements. Either leave it out, or specify it at the toplevel object.");
            }
            List<Geometry> geometries = parent.collectionFromJson(subJson, Geometry.class);
            return new GeometryCollection(geometries.toArray(new Geometry[geometries.size()]), geomFact);

        } catch (JsonException e) {
            throw new IOException(e);
        }
    }


    /**
     * Parses the JSON as a MultiPolygon geometry
     *
     * @param coords the coordinates of a multipolygon which is just a list of coordinates of polygons.
     * @return an instance of multipolygon
     * @throws IOException if the given json does not correspond to a multipolygon or can be parsed as such
     */
    private MultiPolygon asMultiPolygon(List<List<List<List>>> coords) throws IOException {
        if (coords == null || coords.isEmpty()) {
            throw new IOException("A multipolygon should have at least one polyon.");
        }
        Polygon[] polygons = new Polygon[coords.size()];
        for (int i = 0; i < coords.size(); i++) {
            polygons[i] = asPolygon(coords.get(i));
        }
        return new MultiPolygon(polygons, geomFact);
    }

    /**
     * Parses the JSON as a MultiLineString geometry
     *
     * @param coords the coordinates of a multlinestring (which is a list of coordinates of linestrings)
     * @return an instance of multilinestring
     * @throws IOException if the given json does not correspond to a multilinestring or can be parsed as such
     */
    private MultiLineString asMultiLineString(List<List<List>> coords) throws IOException {
        if (coords == null || coords.isEmpty()) {
            throw new IOException("A multilinestring requires at least one line string");
        }
        LineString[] lineStrings = new LineString[coords.size()];
        for (int i = 0; i < lineStrings.length; i++) {
            lineStrings[i] = asLineString(coords.get(i));
        }
        return new MultiLineString(lineStrings, geomFact);
    }

    /**
     * Parses the JSON as a polygon geometry
     *
     * @param coords the coordinate array corresponding with the polygon (a list containing rings, each of which
     *               contains a list of coordinates (which in turn are lists of numbers)).
     * @return An instance of polygon
     * @throws IOException if the given json does not correspond to a polygon or can be parsed as such.
     */
    private Polygon asPolygon(List<List<List>> coords) throws IOException {
        if (coords == null || coords.isEmpty()) {
            throw new IOException("A polygon requires the specification of its outer ring");
        }
        List<LinearRing> rings = new ArrayList<LinearRing>();
        try { 
        for (List<List> ring : coords) {
            Coordinate[] ringCoords = getCoordArray(ring);
            rings.add(new LinearRing(new CoordinateArraySequence(ringCoords), geomFact));
        }
        LinearRing[] holes = rings.size() > 1 ? rings.subList(1, rings.size()).toArray(new LinearRing[rings.size() - 1]) :
                new LinearRing[0];

            return new Polygon(rings.get(0), holes, geomFact);
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid Polygon: " + e.getMessage(), e);
        }

    }

    /**
     * Parses the JSON as a point geometry.
     *
     * @param coords the coordinates (a list with an x and y value)
     * @return An instance of point
     * @throws IOException if the given json does not correspond to a point or can not be parsed to a point.
     */
    private Point asPoint(List coords) throws IOException {
        if (coords != null && coords.size() >= 2) {
            ArrayList<List> coordinates = new ArrayList<List>();
            coordinates.add(coords);
            return new Point(new CoordinateArraySequence(getCoordArray(coordinates)), geomFact);
        } else {
            throw new IOException("A point must has exactly one coordinate (an x, a y and possibly a z value). Additional numbers in the coordinate are permitted but ignored.");
        }
    }

    /**
     * Parses the JSON as a linestring geometry
     *
     * @param coords The coordinates for the linestring, which is a list of coordinates (which in turn are lists of
     *               two values, x and y)
     * @return An instance of linestring
     * @throws IOException if the given json does not correspond to a linestring or can be parsed as such.
     */
    private LineString asLineString(List<List> coords) throws IOException {
        if (coords == null || coords.size() < 2) {
            throw new IOException("A linestring requires a valid series of coordinates (at least two coordinates)");
        }
        CoordinateArraySequence coordinates = new CoordinateArraySequence(getCoordArray(coords));
        return new LineString(coordinates, geomFact);
    }

    /**
     * Parses the JSON as a linestring geometry
     *
     * @param coords A list of coordinates of points.
     * @return An instance of linestring
     * @throws IOException if the given json does not correspond to a linestring or can be parsed as such.
     */
    private MultiPoint asMultiPoint(List<List> coords) throws IOException {
        if (coords == null || coords.isEmpty()) {
            throw new IOException("A multipoint contains at least one point");
        }
        Point[] points = new Point[coords.size()];
        for (int i = 0; i < coords.size(); i++) {
            points[i] = asPoint(coords.get(i));
        }
        return new MultiPoint(points, geomFact);
    }

    /**
     * This method takes in a list of lists and returns a coordinate array that correspond with that list.  The elements
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
     * @param entry a list of lists of numbers
     * @return an array of coordinates
     * @throws IOException if the conversion can not be executed (eg because one of the innerlists contains more or
     *                     less than two doubles.
     */
    private Coordinate[] getCoordArray(List<List> entry) throws IOException {
        {
            Coordinate[] result = new Coordinate[entry.size()];
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
                if (z == null) {
                    result[i] = new Coordinate(x, y);
                } else {
                    result[i] = new Coordinate(x, y, z);
                }

            }
            return result;
        }
    }
}
