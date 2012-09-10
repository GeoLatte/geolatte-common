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

import java.util.Arrays;

/**
 * This class represents a transfer object which, if serialized by a standard JSON serializer, leads to a valid
 * GeoJSON representation of a Polygon
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 */
public final class MultiPolygonTo extends GeoJsonTo {

    private double[][][][] coordinates;

    public MultiPolygonTo() {
    }

    public MultiPolygonTo(CrsTo crs, double[][][][] coordinates) {
        super(crs, createBoundingBox(coordinates));
        this.coordinates = coordinates;
    }

    @Override
    public boolean isValid() {
        if (coordinates == null || coordinates.length == 0) {
            return false;
        }
        for (double[][][] polygon : coordinates) {
            if (polygon == null || polygon.length < 1) {
                return false;
            }
            for (double[][] linearRing : polygon) {
                if (linearRing == null || linearRing.length < 2) {
                    return false;
                }
                int length = linearRing[0].length;
                if (length != 2 && length != 3) {
                    return false;
                }
                for (double[] points : linearRing) {
                    if (points.length != length) {
                        return false;
                    }
                }
                for (int i = 0; i < linearRing[0].length; i++) {
                    if (linearRing[0][i] - linearRing[linearRing.length - 1][i] > 0.00000001) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public double[][][][] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[][][][] coordinates) {
        this.coordinates = coordinates;
        if (isValid()) {
            setBbox(createBoundingBox(coordinates));
        }
    }

    @Override
    public boolean equals(Object o) {

        if (!super.equals(o)) {
            return false;
        }

        MultiPolygonTo multiPolygonTo = (MultiPolygonTo)o;

        if (!Arrays.deepEquals(coordinates, multiPolygonTo.coordinates)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {

        int result = super.hashCode();
        result = 31 * result + (coordinates != null ? Arrays.deepHashCode(coordinates) : 0);
        return result;
    }
}