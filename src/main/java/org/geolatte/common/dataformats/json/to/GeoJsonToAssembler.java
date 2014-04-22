/*
 * This file is part of the GeoLatte project.
 *
 *     GeoLatte is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     GeoLatte is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with GeoLatte.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2010 - 2011 and Ownership of code is shared by:
 * Qmino bvba - Esperantolaan 4 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.common.dataformats.json.to;

import org.geolatte.geom.*;
import org.geolatte.geom.crs.CrsId;

/**
 * Factory which allows conversion between GeoJsonTo objects on the one hand, and Geolatte-geometries or JTS
 * geometries on the other hand.
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 */
public class GeoJsonToAssembler {


    //region fromTransferObject methods

    /**
     * Creates the correct TO starting from any geolatte geometry.
     *
     * @param geometry the geometry to convert
     * @return a TO that, once serialized, results in a valid geoJSON representation of the geometry
     */
    public GeoJsonTo toTransferObject(Geometry geometry) {

        if (geometry instanceof Point) {
            return toTransferObject((Point) geometry);
        } else if (geometry instanceof LineString) {
            return toTransferObject((LineString) geometry);
        } else if (geometry instanceof MultiPoint) {
            return toTransferObject((MultiPoint) geometry);
        } else if (geometry instanceof MultiLineString) {
            return toTransferObject((MultiLineString) geometry);
        } else if (geometry instanceof Polygon) {
            return toTransferObject((Polygon) geometry);
        } else if (geometry instanceof MultiPolygon) {
            return toTransferObject((MultiPolygon) geometry);
        } else if (geometry instanceof GeometryCollection) {
            return toTransferObject((GeometryCollection) geometry);
        }
        return null;
    }

    /**
     * Converts a polygon to its corresponding Transfer Object
     *
     * @param input a polygon object
     * @return a transfer object for the polygon
     */
    public PolygonTo toTransferObject(Polygon input) {
        PolygonTo result = new PolygonTo();
        result.setCrs(GeoJsonTo.createCrsTo("EPSG:" + input.getSRID()));
        double[][][] rings = new double[input.getNumInteriorRing() + 1][][];
        // Exterior ring:
        rings[0] = getPoints(input.getExteriorRing());
        // Interior rings!
        for (int i = 0; i < input.getNumInteriorRing(); i++) {
            rings[i + 1] = getPoints(input.getInteriorRingN(i));
        }
        result.setCoordinates(rings);
        return result;
    }

    /**
     * Converts a multilinestring to its corresponding Transfer Object
     *
     * @param input the multilinestring
     * @return a transfer object for the multilinestring
     */
    public MultiLineStringTo toTransferObject(MultiLineString input) {
        MultiLineStringTo result = new MultiLineStringTo();
        double[][][] resultCoordinates = new double[input.getNumGeometries()][][];
        for (int i = 0; i < input.getNumGeometries(); i++) {
            resultCoordinates[i] = getPoints(input.getGeometryN(i));
        }
        result.setCoordinates(resultCoordinates);
        result.setCrs(GeoJsonTo.createCrsTo("EPSG:" + input.getSRID()));
        return result;
    }

    /**
     * Converts a multipoint to its corresponding Transfer Object
     *
     * @param input the multipoint
     * @return a transfer object for the multipoint
     */
    public MultiPointTo toTransferObject(MultiPoint input) {
        MultiPointTo result = new MultiPointTo();
        result.setCrs(GeoJsonTo.createCrsTo("EPSG:" + input.getSRID()));
        result.setCoordinates(getPoints(input));
        return result;
    }

    /**
     * Converts a point to its corresponding Transfer Object
     *
     * @param input the point object
     * @return the transfer object for the point
     */
    public PointTo toTransferObject(Point input) {
        PointTo result = new PointTo();
        result.setCrs(GeoJsonTo.createCrsTo("EPSG:" + input.getSRID()));
        result.setCoordinates(getPoints(input)[0]);
        return result;
    }

    /**
     * Converts a linestring to its corresponding Transfer Object
     *
     * @param input the linestring object to convert
     * @return the transfer object for the linestring
     */
    public LineStringTo toTransferObject(LineString input) {
        LineStringTo result = new LineStringTo();
        result.setCrs(GeoJsonTo.createCrsTo("EPSG:" + input.getSRID()));
        result.setCoordinates(getPoints(input));
        return result;
    }

    /**
     * Converts a multipolygon to its corresponding Transfer Object
     *
     * @param input the multipolygon
     * @return the transfer object for the multipolygon
     */
    public MultiPolygonTo toTransferObject(MultiPolygon input) {
        MultiPolygonTo result = new MultiPolygonTo();
        double[][][][] coordinates = new double[input.getNumGeometries()][][][];
        for (int i = 0; i < input.getNumGeometries(); i++) {
            coordinates[i] = toTransferObject(input.getGeometryN(i)).getCoordinates();
        }
        result.setCoordinates(coordinates);
        result.setCrs(GeoJsonTo.createCrsTo("EPSG:" + input.getSRID()));
        return result;
    }

    /**
     * Converts a multipolygon to its corresponding Transfer Object
     *
     * @param input the multipolygon
     * @return the transfer object for the geometrycollection
     */
    public GeometryCollectionTo toTransferObject(GeometryCollection input) {
        GeometryCollectionTo result = new GeometryCollectionTo();
        GeoJsonTo[] tos = new GeoJsonTo[input.getNumGeometries()];
        for (int i = 0; i < input.getNumGeometries(); i++) {
            tos[i] = toTransferObject(input.getGeometryN(i));
            tos[i].setCrs(null); // Crs may not be repeated
        }
        result.setGeometries(tos);
        result.setCrs(GeoJsonTo.createCrsTo("EPSG:" + input.getSRID()));
        return result;
    }

    //endregion

    //region fromTransferObject methods

    /**
     * Creates a geolatte geometry object starting from a geojsonto.
     *
     * @param input the geojson to to start from
     * @return the corresponding geometry
     * @throws IllegalArgumentException If the geometry could not be constructed due to an invalid
     *                                  GeoJsonTo
     */
    public Geometry fromTransferObject(GeoJsonTo input) throws IllegalArgumentException {

        return fromTransferObject(input, null);
    }

    /**
     * Creates a geolatte geometry object starting from a GeoJsonTo.
     *
     * @param input the geojson to to start from
     * @param crsId the crs id to use (ignores the crs of the input). If null, uses the crs of the input.
     * @return the corresponding geometry
     * @throws IllegalArgumentException If the geometry could not be constructed due to an invalid
     *                                  geojsonto
     */
    public Geometry fromTransferObject(GeoJsonTo input, CrsId crsId) throws IllegalArgumentException {

        if (input instanceof PointTo) {
            return fromTransferObject((PointTo) input, crsId);
        } else if (input instanceof MultiPointTo) {
            return fromTransferObject((MultiPointTo) input, crsId);
        } else if (input instanceof LineStringTo) {
            return fromTransferObject((LineStringTo) input, crsId);
        } else if (input instanceof MultiLineStringTo) {
            return fromTransferObject((MultiLineStringTo) input, crsId);
        } else if (input instanceof PolygonTo) {
            return fromTransferObject((PolygonTo) input, crsId);
        } else if (input instanceof MultiPolygonTo) {
            return fromTransferObject((MultiPolygonTo) input, crsId);
        } else if (input instanceof GeometryCollectionTo) {
            return fromTransferObject((GeometryCollectionTo) input, crsId);
        }
        return null;
    }

    /**
     * Creates a polygon starting from a transfer object.
     *
     * @param input the transfer object
     * @return a polygon corresponding to the transfer object
     * @throws IllegalArgumentException If the geometry could not be constructed due to an invalid transfer object
     */
    public Polygon fromTransferObject(PolygonTo input) {

        return fromTransferObject(input, null);
    }

    /**
     * Creates a polygon object starting from a transfer object.
     *
     * @param input the polygon transfer object
     * @param crsId the crs id to use (ignores the crs of the input). If null, uses the crs of the input.
     * @return the corresponding geometry
     * @throws IllegalArgumentException If the geometry could not be constructed due to an invalid transfer object
     */
    public Polygon fromTransferObject(PolygonTo input, CrsId crsId) {

        if (input == null) { return null; }
        crsId = getCrsId(input, crsId);
        isValid(input);

        return createPolygon(input.getCoordinates(), crsId);
    }

    /**
     * Creates a geometrycollection starting from a transfer object.
     *
     * @param input the transfer object
     * @return a geometrycollection corresponding to the transfer object
     * @throws IllegalArgumentException If the geometry could not be constructed due to an invalid transfer object
     */
    public GeometryCollection fromTransferObject(GeometryCollectionTo input) {

        return fromTransferObject(input, null);
    }

    /**
     * Creates a geometrycollection object starting from a transfer object.
     *
     * @param input the geometry collection transfer object
     * @param crsId the crs id to use (ignores the crs of the input). If null, uses the crs of the input.
     * @return the corresponding geometry
     * @throws IllegalArgumentException If the geometry could not be constructed due to an invalid transfer object
     */
    public GeometryCollection fromTransferObject(GeometryCollectionTo input, CrsId crsId) {

        if (input == null) { return null; }
        crsId = getCrsId(input, crsId);
        isValid(input);

        Geometry[] geoms = new Geometry[input.getGeometries().length];
        for (int i = 0; i < geoms.length; i++) {
            geoms[i] = fromTransferObject(input.getGeometries()[i], crsId);
        }
        return new GeometryCollection(geoms);
    }

    /**
     * Creates a multipolygon starting from a transfer object.
     *
     * @param input the transfer object
     * @return a multipolygon corresponding to the transfer object
     * @throws IllegalArgumentException If the geometry could not be constructed due to an invalid transfer object
     */
    public MultiPolygon fromTransferObject(MultiPolygonTo input) {

        return fromTransferObject(input, null);
    }

    /**
     * Creates a multipolygon object starting from a transfer object.
     *
     * @param input the multipolygon transfer object
     * @param crsId the crs id to use (ignores the crs of the input). If null, uses the crs of the input.
     * @return the corresponding geometry
     * @throws IllegalArgumentException If the geometry could not be constructed due to an invalid transfer object
     */
    public MultiPolygon fromTransferObject(MultiPolygonTo input, CrsId crsId) {

        if (input == null) { return null; }
        crsId = getCrsId(input, crsId);
        isValid(input);

        Polygon[] polygons = new Polygon[input.getCoordinates().length];
        for (int i = 0; i < polygons.length; i++) {
            polygons[i] = createPolygon(input.getCoordinates()[i], crsId);
        }
        return new MultiPolygon(polygons);
    }

    /**
     * Creates a multilinestring starting from a transfer object.
     *
     * @param input the transfer object
     * @return a multilinestring corresponding to the transfer object
     * @throws IllegalArgumentException If the geometry could not be constructed due to an invalid transfer object
     */
    public MultiLineString fromTransferObject(MultiLineStringTo input) {
        return fromTransferObject(input, null);
    }

    /**
     * Creates a multilinestring object starting from a transfer object.
     *
     * @param input the multilinestring transfer object
     * @param crsId the crs id to use (ignores the crs of the input). If null, uses the crs of the input.
     * @return the corresponding geometry
     * @throws IllegalArgumentException If the geometry could not be constructed due to an invalid transfer object
     */
    public MultiLineString fromTransferObject(MultiLineStringTo input, CrsId crsId) {

        if (input == null) { return null; }
        crsId = getCrsId(input, crsId);
        isValid(input);

        LineString[] lineStrings = new LineString[input.getCoordinates().length];
        for (int i = 0; i < lineStrings.length; i++) {
            lineStrings[i] = new LineString(createPointSequence(input.getCoordinates()[i], crsId));
        }
        return new MultiLineString(lineStrings);
    }

    /**
     * Creates a linestring starting from a transfer object.
     *
     * @param input the transfer object
     * @return a linestring corresponding to the transfer object
     * @throws IllegalArgumentException If the geometry could not be constructed due to an invalid transfer object
     */
    public LineString fromTransferObject(LineStringTo input) {
        return fromTransferObject(input, null);
    }

    /**
     * Creates a linestring object starting from a transfer object.
     *
     * @param input the linestring transfer object
     * @param crsId the crs id to use (ignores the crs of the input). If null, uses the crs of the input.
     * @return the corresponding geometry
     * @throws IllegalArgumentException If the geometry could not be constructed due to an invalid transfer object
     */
    public LineString fromTransferObject(LineStringTo input, CrsId crsId) {

        if (input == null) { return null; }
        crsId = getCrsId(input, crsId);
        isValid(input);

        return new LineString(createPointSequence(input.getCoordinates(), crsId));
    }

    /**
     * Creates a multipoint starting from a transfer object.
     *
     * @param input the transfer object
     * @return a multipoint corresponding to the transfer object
     * @throws IllegalArgumentException If the geometry could not be constructed due to an invalid transfer object
     */
    public MultiPoint fromTransferObject(MultiPointTo input) {
        return fromTransferObject(input, null);
    }

    /**
     * Creates a multipoint object starting from a transfer object.
     *
     * @param input the multipoint transfer object
     * @param crsId the crs id to use (ignores the crs of the input). If null, uses the crs of the input.
     * @return the corresponding geometry
     * @throws IllegalArgumentException If the geometry could not be constructed due to an invalid transfer object
     */
    public MultiPoint fromTransferObject(MultiPointTo input, CrsId crsId) {

        if (input == null) { return null; }
        crsId = getCrsId(input, crsId);
        isValid(input);

        Point[] points = new Point[input.getCoordinates().length];
        for (int i = 0; i < points.length; i++) {
            points[i] = createPoint(input.getCoordinates()[i], crsId);
        }
        return new MultiPoint(points);
    }

    /**
     * Creates a point starting from a transfer object.
     *
     * @param input the transfer object
     * @return a point corresponding to the transfer object
     * @throws IllegalArgumentException If the geometry could not be constructed due to an invalid transfer object
     */
    public Point fromTransferObject(PointTo input) {
        return fromTransferObject(input, null);
    }

    /**
     * Creates a point object starting from a transfer object.
     *
     * @param input the point transfer object
     * @param crsId the crs id to use (ignores the crs of the input). If null, uses the crs of the input.
     * @return the corresponding geometry
     * @throws IllegalArgumentException If the geometry could not be constructed due to an invalid transfer object
     */
    public Point fromTransferObject(PointTo input, CrsId crsId) {

        if (input == null) { return null; }
        crsId = getCrsId(input, crsId);
        isValid(input);

        return createPoint(input.getCoordinates(), crsId);
    }

    //endregion

    /**
     * Creates a polygon starting from its geojson coordinate array
     *
     * @param coordinates the geojson coordinate array
     * @param crsId       the srid of the crs to use
     * @return a geolatte polygon instance
     */
    private Polygon createPolygon(double[][][] coordinates, CrsId crsId) {
        LinearRing[] rings = new LinearRing[coordinates.length];
        for (int i = 0; i < coordinates.length; i++) {
            rings[i] = new LinearRing(createPointSequence(coordinates[i], crsId));
        }
        return new Polygon(rings);
    }

    /**
     * Helpermethod that creates a geolatte pointsequence starting from an array containing coordinate arrays
     *
     * @param coordinates an array containing coordinate arrays
     * @return a geolatte pointsequence or null if the coordinatesequence was null
     */
    private PointSequence createPointSequence(double[][] coordinates, CrsId crsId) {
        if (coordinates == null) {
            return null;
        } else if (coordinates.length == 0) {
            return PointCollectionFactory.createEmpty();
        }

        DimensionalFlag df = coordinates[0].length == 4 ? DimensionalFlag.d3DM  : coordinates[0].length == 3 ? DimensionalFlag.d2DM : DimensionalFlag.d2D;
        PointSequenceBuilder psb = PointSequenceBuilders.variableSized(df, crsId);
        for (double[] point : coordinates) {
            psb.add(point);
        }
        return psb.toPointSequence();
    }

    /**
     * Helpermethod that creates a point starting from its geojsonto coordinate array
     *
     * @param input      the coordinate array to convert to a point
     * @param crsIdValue the sridvalue of the crs in which the point is defined
     * @return an instance of a geolatte point corresponding to the given to or null if the given array is null
     */
    private Point createPoint(double[] input, CrsId crsIdValue) {
        if (input == null) {
            return null;
        }
        if (input.length == 2) {
            return Points.create2D(input[0], input[1], crsIdValue);
        } else if(input.length == 3){
            return Points.create3D(input[0], input[1], input[2], crsIdValue);
        } else {
            double z = input[2];
            double m = input[3];
            if(Double.isNaN(z)) {
                return Points.create2DM(input[0],input[1],m,crsIdValue);
            } else {
                return Points.create3DM(input[0], input[1], z, m, crsIdValue);
            }
        }
    }

    /**
     * Serializes all points of the input into a list of their coordinates
     *
     * @param input a geometry whose points are to be converted to a list of coordinates
     * @return an array containing arrays with x,y and optionally z and m values.
     */
    private double[][] getPoints(Geometry input) {
        double[][] result = new double[input.getNumPoints()][];
        for (int i = 0; i < input.getPoints().size(); i++) {
            Point p = input.getPointN(i);
            if(p.isMeasured() && p.is3D()) {
                result[i] = new double[]{p.getX(), p.getY(), p.getZ(), p.getM()};
            } else if(p.isMeasured()) {
                // ideally we'd use something like Double.Nan, but JSON doesn't support that.
                result[i] = new double[]{p.getX(), p.getY(), 0, p.getM()};
            } else if(p.is3D()) {
                result[i] = new double[]{p.getX(), p.getY(), p.getZ()};
            } else {
                result[i] = new double[]{p.getX(), p.getY()};
            }
        }
        return result;
    }

    /**
     * If an CRS (srid) is specified in the json object, it is returned. If no CRS is found in the current
     * parameter-map
     * null is returned. This is a simplified version from the actual GeoJSON specification, since the GeoJSON
     * specification
     * allows for relative links to either URLS or local files in which the crs should be defined. This implementation
     * only supports named crs's: namely:
     * <pre>
     *  "crs": {
     *       "type": "name",
     *       "properties": {
     *             "name": "<yourcrsname>"
     *       }
     * }
     * </pre>
     * Besides the fact that only named crs is permitted for deserialization, the given name must either be of the
     * form:
     * <pre>
     *  urn:ogc:def:EPSG:x.y:4326
     * </pre>
     * (with x.y the version of the EPSG) or of the form:
     * <pre>
     * EPSG:4326
     * </pre>
     *
     * @return the CrsId of the crs system in the json if it is present. {@link CrsId#UNDEFINED} otherwise.
     * @throws IllegalArgumentException If a crs object is present, but deserialization is not possible
     */
    protected CrsId getCrsId(GeoJsonTo to) throws IllegalArgumentException {
        if (to.getCrs() == null) {
            return CrsId.UNDEFINED;
        } else {
            if (to.getCrs().getType() == null || !"name".equals(to.getCrs().getType())) {
                throw new IllegalArgumentException("If the crs is specified the type must be specified. Currently, only named crses are supported.");
            }
            if (to.getCrs().getProperties() == null || to.getCrs().getProperties().getName() == null) {
                throw new IllegalArgumentException("A crs specification requires a properties value containing a name value.");
            }
            String sridString = to.getCrs().getProperties().getName();
            if (sridString.startsWith("EPSG:")) {
                Integer srid = parseDefault(sridString.substring(5), null);
                if (srid == null) {
                    throw new IllegalArgumentException("Unable to derive SRID from crs name");
                } else {
                    return new CrsId("EPSG", srid);
                }
            } else if (sridString.startsWith("urn:ogc:def:crs:EPSG:")) {
                String[] splits = sridString.split(":");
                if (splits.length != 7) {
                    throw new IllegalArgumentException("Unable to derive SRID from crs name");
                } else {
                    Integer srid = parseDefault(splits[6], null);
                    if (srid == null) {
                        throw new IllegalArgumentException("Unable to derive SRID from crs name");
                    }
                    return new CrsId("EPSG", srid);
                }
            } else {
                throw new IllegalArgumentException("Unable to derive SRID from crs name");
            }
        }
    }

    private CrsId getCrsId(GeoJsonTo input, CrsId defaultCrsId) {
        defaultCrsId = defaultCrsId == null ? getCrsId(input) : defaultCrsId;
        if (defaultCrsId == null) {
            throw new IllegalArgumentException("Input has not CRS and no default CRS provided.");
        }
        return defaultCrsId;
    }

    /**
     * Convenience method. Parses a string into a double. If it can not be converted to a double, the
     * defaultvalue is returned. Depending on your choice, you can allow null as output or assign it some value
     * and have very convenient syntax such as: double d = parseDefault(myString, 0.0); which is a lot shorter
     * than dealing with all kinds of numberformatexceptions.
     *
     * @param input        The inputstring
     * @param defaultValue The value to assign in case of error
     * @return A double corresponding with the input, or defaultValue if no double can be extracted
     */
    protected Integer parseDefault(String input, Integer defaultValue) {
        if (input == null) {
            return defaultValue;
        }
        Integer answer = defaultValue;
        try {
            answer = Integer.parseInt(input);
        } catch (NumberFormatException ignored) {
        }
        return answer;
    }

    /**
     * Checks whether the given to is a valid TO and throws an exception if not.
     *
     * @param to The TO to check
     * @throws IllegalArgumentException When the to is invalid.
     */
    protected static void isValid(GeoJsonTo to) throws IllegalArgumentException {
        if (!to.isValid()) {
            throw new IllegalArgumentException("TO is not valid.");
        }
    }
}
