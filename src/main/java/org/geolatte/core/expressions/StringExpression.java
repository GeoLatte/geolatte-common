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
 * Abstract base class for all Number expressions. Offers further concatenation operations such as {@link StringExpression#isGreaterThan(ComparableExpression)} and String specific {@link StringExpression#like(Expression)} and {@link StringExpression#notLike(Expression)}.
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
public abstract class StringExpression implements Expression<String>, ComparableExpression<String> {

    /**
     * Creates a StringIsEqual expression from this expression (as left expression) and the given expression (as
     * the right expression).
     *
     * @param right The right expression.
     * @return A new StringIsEqual expression.
     */
    public StringIsEqual isEqual(ComparableExpression<String> right) {
        return new StringIsEqual(this, right);
    }

    /**
     * Creates an StringIsGreaterThan expression from this expression (as left expression) and the given expression (as
     * the right expression).
     *
     * @param right The right expression.
     * @return A new StringIsGreaterThan binary expression.
     */
    public StringIsGreaterThan isGreaterThan(ComparableExpression<String> right) {
        return new StringIsGreaterThan(this, right);
    }

    /**
     * Creates an StringIsGreaterThanOrEqual expression from this expression (as left expression) and the given expression (as
     * the right expression).
     *
     * @param right The right expression.
     * @return A new StringIsGreaterThanOrEqual binary expression.
     */
    public StringIsGreaterThanOrEqual isGreaterThanOrEqual(ComparableExpression<String> right) {
        return new StringIsGreaterThanOrEqual(this, right);
    }

    /**
     * Creates an is less than expression from this expression (as left expression) and the given expression (as
     * the right expression).
     *
     * @param right The right expression.
     * @return A new StringIsLessThan binary expression.
     */
    public StringIsLessThan lessThan(ComparableExpression<String> right) {
        return new StringIsLessThan(this, right);
    }

    /**
     * Creates an StringIsLessThanOrEqual expression from this expression (as left expression) and the given expression (as
     * the right expression).
     *
     * @param right The right expression.
     * @return A new StringIsLessThanOrEqual binary expression.
     */
    public StringIsLessThanOrEqual isLessThanOrEqual(ComparableExpression<String> right) {
        return new StringIsLessThanOrEqual(this, right);
    }

    /**
     * Creates an StringIsNotEqual expression from this expression (as left expression) and the given expression (as
     * the right expression).
     *
     * @param right The right expression.
     * @return A new StringIsNotEqual binary expression.
     */
    public StringIsNotEqual isNotEqual(ComparableExpression<String> right) {
        return new StringIsNotEqual(this, right);
    }

    /**
     * Creates a new Like expression based on this expression (on the left side) and the given expression (on the
     * right side).
     *
     * @param right The right side of the expression.
     * @return A new Like expression.
     */
    public Like like(Expression<String> right) {
        return new Like(this, right);
    }

    /**
     * Creates a new NotLike expression based on this expression (on the left side) and the given expression (on the
     * right side).
     *
     * @param right The right side of the expression.
     * @return A new NotLike expression.
     */
    public NotLike notLike(Expression<String> right) {
        return new NotLike(this, right);
    }

    public int compareTo(Object evaluateObject, String value) {

        String thisString = evaluate(evaluateObject);
        return thisString.compareTo(value);
        
    }

    public void switchOn(BasicTypeSwitch switcher) {

        switcher.caseString(this);
    }
}