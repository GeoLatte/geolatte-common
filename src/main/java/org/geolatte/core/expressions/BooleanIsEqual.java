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

package org.geolatte.core.expressions;

/**
 * <p>
 * "is equal" expression for Boolean valued operands.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 14-Jul-2010<br>
 * <i>Creation-Time</i>:  19:02:02<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class BooleanIsEqual extends BooleanBinaryBooleanExpression {

    /**
     * Constructor of a binary expression.
     *
     * @param left  The left hand side expression.
     * @param right The right hand side expression.
     */
    public BooleanIsEqual(Expression<Boolean> left, Expression<Boolean> right) {
        super(left, right);
    }

    public Boolean evaluate(Object o) {

        Boolean left = getLeft().evaluate(o);
        Boolean right = getRight().evaluate(o);

        return left == right;
    }
}
