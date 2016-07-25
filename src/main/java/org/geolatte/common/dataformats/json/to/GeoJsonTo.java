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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Arrays;

/**
 * Abstract parentclass for all geojson transfer objects.
 *
 * Not to be subclassed.
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.PROPERTY, property="type")
@JsonIgnoreProperties({"valid"})
@JsonSubTypes({
        @JsonSubTypes.Type(value=PointTo.class, name="Point"),
        @JsonSubTypes.Type(value=MultiPointTo.class, name="MultiPoint"),
        @JsonSubTypes.Type(value=LineStringTo.class, name="LineString"),
        @JsonSubTypes.Type(value=MultiLineStringTo.class, name="MultiLineString"),
        @JsonSubTypes.Type(value=PolygonTo.class, name="Polygon"),
        @JsonSubTypes.Type(value=MultiPolygonTo.class, name="MultiPolygon"),
        @JsonSubTypes.Type(value=GeometryCollectionTo.class, name="GeometryCollection")
})
public abstract class GeoJsonTo {

    private CrsTo crs;
    private double[] bbox;

    GeoJsonTo() {
    }

    GeoJsonTo(CrsTo crs, double[] bbox) {
        this.crs = crs;
        this.bbox = bbox;
    }

    /**
     * @return whether the transfer object corresponds with a valid GeoJson entity
     */
    public abstract boolean isValid();

    /**
     * @return the crs which is specified in this to.
     */
    public CrsTo getCrs() {
        return crs;
    }

    /**
     * @param crs the crs to set for this geometry
     */
    public void setCrs(CrsTo crs) {
        this.crs = crs;
    }

    /**
     * @return the boundingbox of this geometry. The value of the bbox member must be a 2*n array where n is
     *         the number of dimensions represented in the contained geometries, with the lowest values for all axes
     *         followed by the highest values. The axes order of a bbox follows the axes order of geometries. In
     *         addition,
     *         the coordinate reference system for the bbox is assumed to match the coordinate reference system of the
     *         GeoJSON object of which it is a member.
     */
    public double[] getBbox() {
        return bbox;
    }

    /**
     * Sets the boundingbox value for this geometry
     *
     * @param bbox the given value to set (it is not checked for validity)
     */
    protected void setBbox(double[] bbox) {
        this.bbox = bbox;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        // Geometries can only be equals if they are of the same type
        if (!o.getClass().equals(this.getClass())) {
            return false;
        }

        GeoJsonTo geoJsonTo = (GeoJsonTo) o;

        if (!Arrays.equals(bbox, geoJsonTo.bbox)) {
            return false;
        }

        if (crs != null ? !crs.equals(geoJsonTo.crs) : geoJsonTo.crs != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = crs != null ? crs.hashCode() : 0;
        result = 31 * result + (bbox != null ? Arrays.hashCode(bbox) : 0);
        return result;
    }

    /**
     * Convenience method to create a named crs to.
     *
     * @param crsName the name of the crs to use, if null, the default crs for geojson is used.
     * @return the transfer object that corresponds with the default Crs. According to the GeoJson spec
     *         The default CRS is a geographic coordinate reference system, using the WGS84 datum, and with longitude
     *         and latitude units of decimal degrees.
     */
    public static CrsTo createCrsTo(String crsName) {
        String nameToUse = crsName == null ? "EPSG:4326" : crsName;
        CrsTo result = new CrsTo();
        NamedCrsPropertyTo property = new NamedCrsPropertyTo();
        property.setName(nameToUse);
        result.setProperties(property);
        return result;
    }


    /**
     * Creates a boundingbox for a point. In this case, both the lower and higher edges of the bbox are the point
     * itself
     *
     * @param coordinates the coordinates of the point
     * @return the boundingbox, a list with doubles. the result is a 2*n array where n is the number of dimensions
     *         represented in the input, with the lowest values for all axes followed by the highest values.
     */
    public static double[] createBoundingBox(double[] coordinates) {
        int maxActualCoords = Math.min(coordinates.length,3);// ignore potential M values, so 4 dimension (X Y Z M) becomes X Y Z
        double[] result = new double[maxActualCoords * 2];
        for (int i = 0; i < maxActualCoords; i++) {
            result[i] = coordinates[i];
            result[maxActualCoords + i] = coordinates[i];
        }
        return result;
    }

    /**
     * This method computes the boundingbox of a list of points (such as the coordinates of a multipoint or linestring)
     *
     * @param input a list of lists that each contain between two and for doubles (x, y and optional z and m coordinates)
     * @return a list with doubles. the result is a 2*n array where n is the number of dimensions represented
     *         in the input (m doesn't count as a dimension), with the lowest values for all axes followed by the highest values.
     */
    public static double[] createBoundingBox(double[][] input) {
        int maxActualCoords = Math.min(input[0].length, 3);// max 3, ignoring potential m values
        double[] result = new double[maxActualCoords * 2];

        for (int i = 0; i < maxActualCoords; i++) {
            result[i] = Double.MAX_VALUE;
        }
        for (int i = maxActualCoords; i < result.length; i++) {
            result[i] = - Double.MAX_VALUE;  // use negative Double.MAX_VALUE, Double.MIN_VALUE is NOT what you'd expect it to be!
        }
        for (double[] point : input) {
            for (int i = 0; i <  maxActualCoords; i++) {
                result[i] = Math.min(point[i], result[i]);
                result[i + maxActualCoords] = Math.max(point[i], result[i + maxActualCoords]);
            }
        }
        return result;
    }

    /**
     * This method computes the boundingbox of a threedimensional list of doubles, which are to be interpreted as
     * a list of lists of coordinates (such as the coordinates of a multilinestring or polygon)
     *
     * @param input a list of lists that each contain two or three doubles (x, y and optional z coordinates)
     * @return a list with doubles. the result is a 2*n array where n is the number of dimensions represented
     *         in the input, with the lowest values for all axes followed by the highest values.
     */
    public static double[] createBoundingBox(double[][][] input) {
        double[] bbox = createBoundingBox(input[0]);
        for (int i = 1; i < input.length; i++) {
            double[] current = createBoundingBox(input[i]);
            mergeInto(bbox, current);
        }
        return bbox;
    }

    /**
     * This method computes the boundingbox of a fourdimensional list of doubles, which are to be interpreted as
     * a list of items that each represent list of lists of coordinates (such as the coordinates of a multilipolygon,
     * which contains lists of polygons, each containing lists of linearrings that contain coordinates)
     *
     * @param input a list of lists that each contain two or three doubles (x, y and optional z coordinates)
     * @return a list with doubles. the result is a 2*n array where n is the number of dimensions represented
     *         in the input, with the lowest values for all axes followed by the highest values.
     */
    public static double[] createBoundingBox(double[][][][] input) {
        double[] bbox = createBoundingBox(input[0]);
        for (int i = 1; i < input.length; i++) {
            double[] current = createBoundingBox(input[i]);
            mergeInto(bbox, current);
        }
        return bbox;
    }

    /**
     * Merges the second boundingbox into the first. Basically, this extends the first boundingbox to also
     * encapsulate the second
     *
     * @param first  the first boundingbox
     * @param second the second boundingbox
     */
    private static void mergeInto(double[] first, double[] second) {
        for (int j = 0; j < first.length / 2; j++) {
            first[j] = Math.min(first[j], second[j]);
            first[j + first.length / 2] = Math.max(first[j + first.length / 2], second[j + first.length / 2]);
        }
    }
}
