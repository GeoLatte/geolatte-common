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
 * Copyright (C) 2010 - 2010 and Ownership of code is shared by:
 * Qmino bvba - Romeinsestraat 18 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.testobjects;

import org.geolatte.geom.*;
import org.geolatte.geom.crs.CrsId;

/**
 * Base feature
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 20-apr-2010<br>
 * <i>Creation-Time</i>: 11:15:50<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class TestFeature extends TestFeatureNoShape {

    private Geometry shape;

    private SubObject subObject;

    /**
     * Dummy constructor
     */
    public TestFeature() {
        super("Antwerpen", 50, new String[]{"Belgium", "Flanders"}, 125, "name", "subName");
        PointSequence pnts = PointSequenceFactory.create(new double[]{5,6,6,7,7,8}, DimensionalFlag.XY);
        shape = new LineString(pnts, CrsId.valueOf(900913));
    }

    public TestFeature(String name, int length, String[] ownerNames, Geometry shape, int idValue, String subObjectName, String subSubObjectName) {
        super(name, length, ownerNames, idValue, subObjectName, subSubObjectName);
        this.shape = shape;
    }

    public TestFeature(String name, int length, String[] ownerNames, Geometry shape, int idValue, String subObjectName) {
        this(name, length, ownerNames, shape, idValue, subObjectName, null);
    }

    public Geometry getShape() {
        return shape;
    }
}

