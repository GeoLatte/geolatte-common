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

package org.geolatte.common.expressions;

/**
 * <p>
 * Abstract base class for all boolean expressions. Offers boolean concatenation operations such as And and Or.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 9-apr-2010<br>
 * <i>Creation-Time</i>:  11:48:54<br>
 * </p>
 *
 * @author Peter Rigole
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public abstract class BooleanExpression implements Expression<Boolean>, ComparableExpression<Boolean> {

    /**
     * Creates an And expression from this expression (as left expression) and the given expression (as right
     * expression).
     *
     * @param right The right expression.
     * @return An And expression.
     */
    public And and(Expression<Boolean> right) {
        return new And(this, right);
    }

    /**
     * Creates an Or expression from the given expressions.
     *
     * @param right The right expression.
     * @return An Or expression.
     */
    public Or or(Expression<Boolean> right) {
        return new Or(this, right);
    }

    public int compareTo(Object evaluateObject, Boolean value) throws ClassCastException, NullPointerException {

        if (value == null)
            throw new NullPointerException("Cannot compare boolean to null");

        Boolean thisBoolean = evaluate(evaluateObject);

        if (thisBoolean == null)
            throw new NullPointerException("BooleanExpression evaluates to null, cannot compare this.");

        if (thisBoolean)
            return value ? 0 : 1;
        else
            return value ? -1 : 0;
    }

    public void switchOn(BasicTypeSwitch switcher) {

        switcher.caseBoolean(this);
    }
}