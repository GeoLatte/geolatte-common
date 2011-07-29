/*
 * This file is part of the GeoLatte project.
 *
 * GeoLatte is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GeoLatte is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GeoLatte.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2010 - 2010 and Ownership of code is shared by:
 * Qmino bvba - Romeinsestraat 18 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.wkt;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.geolatte.wkt.analysis.DepthFirstAdapter;
import org.geolatte.wkt.node.AD2Point;
import org.geolatte.wkt.node.AEmptyPointGeometry;
import org.geolatte.wkt.node.APointGeometry;
import org.geolatte.wkt.node.Start;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 29-Jun-2010<br>
 * <i>Creation-Time</i>:  16:15:00<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class GeometryBuilder {

    private Geometry geometry;
    private GeometryFactory geometryFactory = new GeometryFactory();
    private GeometryAdaptor geometryAdaptor = new GeometryAdaptor();

    public void setTree(Start tree) {

        tree.apply(geometryAdaptor);
    }

    public Geometry getGeometry() {
        return geometry;
    }

    private class GeometryAdaptor extends DepthFirstAdapter {

        private Point point;

        @Override
        public void outAPointGeometry(APointGeometry node) {

            geometry = point;
        }

        @Override
        public void outAEmptyPointGeometry(AEmptyPointGeometry node) {

            geometry = geometryFactory.createPoint((Coordinate)null);
        }

        public void caseAD2Point(AD2Point node) {

            point = geometryFactory.createPoint(new Coordinate(Double.parseDouble(node.getX().getText()),
                                                               Double.parseDouble(node.getY().getText())));
        }
    }

}
