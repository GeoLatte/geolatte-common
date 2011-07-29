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

package org.geolatte.core.expressions.geo;

import com.vividsolutions.jts.geom.Geometry;
import org.geolatte.core.expressions.BooleanExpression;
import org.geolatte.core.expressions.Expression;

/**
 * <p>
 * Abstract base class for all binary expressions that that operate on two {@link Geometry} expressions and evaluate
 * to a boolean.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 31-Aug-2010<br>
 * <i>Creation-Time</i>:  18:56:39<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public abstract class BooleanBinaryGeometryExpression extends BooleanExpression implements BinaryGeometryExpression<Boolean> {

    private Expression<Geometry> left;
    private Expression<Geometry> right;

    /**
     * Constructor of a binary geometry expression.
     *
     * @param left  The left hand side expression.
     * @param right The right hand side expression.
     */
    protected BooleanBinaryGeometryExpression(Expression<Geometry> left, Expression<Geometry> right) {

        this.left = left;
        this.right = right;
    }

    /**
     * Gets the left hand side expression.
     * @return The left hand side expression.
     */
    @Override
    public Expression<Geometry> getLeft() {

        return left;
    }

    /**
     * Gets the right hand side expression.
     * @return The right hand side expresion
     */
    @Override
    public Expression<Geometry> getRight() {

        return right;
    }
}
