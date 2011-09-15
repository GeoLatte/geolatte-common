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
 * Represents an expression that can be compared/ordered by (evaluated) value.
 * This interface is similar to the {@link Comparable} interface. For more documentation, we refer to the {@link Comparable} docs.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 23-Jul-2010<br>
 * <i>Creation-Time</i>:  09:58:21<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 * @param <T> The type the expression evaluates to.
 */
public interface ComparableExpression<T> extends Expression<T> {

    /**
     * Evaluates the given evaluateObject and compares the result of its evaluation to the given value. Returns a
     * positive, negative or zero value if the evaluation result is smaller, greater of equal to the given value.
     *
     * This method behaves almost the same as {@link Comparable#compareTo(Object)}. For more info we refer to the docs
     * of that method.
     * @param evaluateObject The object against which the expression is evaluated.
     * @param value The object to which the evaluation result is compared.
     * @return +, - or 0 when the expression's result is greater than, lower than or equal to 0.
     *
     * @throws ClassCastException If the provided value is not of the same type as the property.
     * @throws NullPointerException If one of the values involved in the comparison is or evaluates to null.
     */
    public int compareTo(Object evaluateObject, T value) throws ClassCastException, NullPointerException;
}
