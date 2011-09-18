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

package org.geolatte.common.expressions.geo;

import com.vividsolutions.jts.geom.Geometry;
import org.geolatte.common.expressions.BasicTypeSwitch;
import org.geolatte.common.expressions.Expression;

/**
 * <p>
 * Abstract base class for all {@link Geometry} expressions. Offers methods to create other expressions from this expression.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 31-Aug-2010<br>
 * <i>Creation-Time</i>:  20:40:23<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public abstract class GeometryExpression implements Expression<Geometry> {

    /**
     * Creates a GeoEquals expression from this expression (as left expression) and the given expression (as right
     * expression).
     *
     * @param right The right expression.
     * @return A GeoEquals expression.
     */
    public GeoEquals equals(Expression<Geometry> right) {
        return new GeoEquals(this, right);
    }

    /**
     * Implements the visitor pattern as a switch on the type of this expression.
     * For example, on an Expression<Integer>, this method calls the {@link org.geolatte.common.expressions.BasicTypeSwitch#caseInteger(org.geolatte.common.expressions.Expression)} method on the given switcher.
     *
     * @param switcher The class on which a 'case' corresponding to type of this expression will be called.
     */
    public void switchOn(BasicTypeSwitch switcher) {

        switcher.caseGeometry(this);
    }
}
