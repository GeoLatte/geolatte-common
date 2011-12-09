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

import org.geolatte.common.expressions.Expression;
import org.geolatte.geom.Geometry;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 31-Aug-2010<br>
 * <i>Creation-Time</i>:  18:48:22<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class GeoEquals extends BooleanBinaryGeometryExpression {

    /**
     * Constructor of a binary geometry expression.
     *
     * @param left  The left hand side expression.
     * @param right The right hand side expression.
     */
    public GeoEquals(Expression<Geometry> left, Expression<Geometry> right) {
        super(left, right);
    }

    /**
     * Evaluates the given object against this expression.
     *
     *
     * @param o The object to evaluate.
     * @return True if the given object matches this expression, false otherwise.
     */
    public Boolean evaluate(Object o) {
        return getLeft().evaluate(o).equals(getRight().evaluate(o));
    }
}
