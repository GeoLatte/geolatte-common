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
 * Abstract base class for all Number expressions. Offers further concatenation operations such as {@link NumberExpression#isEqual(ComparableExpression)} and {@link NumberExpression#add(Expression)}.
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
public abstract class NumberExpression implements ComparableExpression<Number> {

    /**
     * Creates an IsEqual expression from this expression (as left expression) and the given expression (as
     * the right expression).
     *
     * @param right The right expression.
     * @return A new IsEqual binary expression.
     */
    public IsEqual isEqual(ComparableExpression<Number> right) {
        return new IsEqual(this, right);
    }

    /**
     * Creates an IsGreaterThan expression from this expression (as left expression) and the given expression (as
     * the right expression).
     *
     * @param right The right expression.
     * @return A new IsGreaterThan binary expression.
     */
    public IsGreaterThan isGreaterThan(ComparableExpression<Number> right) {
        return new IsGreaterThan(this, right);
    }

    /**
     * Creates an IsGreaterThanOrEqual expression from this expression (as left expression) and the given expression (as
     * the right expression).
     *
     * @param right The right expression.
     * @return A new IsGreaterThanOrEqual binary expression.
     */
    public IsGreaterThanOrEqual isGreaterThanOrEqual(ComparableExpression<Number> right) {
        return new IsGreaterThanOrEqual(this, right);
    }

    /**
     * Creates an is less than expression from this expression (as left expression) and the given expression (as
     * the right expression).
     *
     * @param right The right expression.
     * @return A new is less than binary expression.
     */
    public IsLessThan isLessThan(ComparableExpression<Number> right) {
        return new IsLessThan(this, right);
    }

    /**
     * Creates an IsLessThanOrEqual expression from this expression (as left expression) and the given expression (as
     * the right expression).
     *
     * @param right The right expression.
     * @return A new IsLessThanOrEqual binary expression.
     */
    public IsLessThanOrEqual isLessThanOrEqual(ComparableExpression<Number> right) {
        return new IsLessThanOrEqual(this, right);
    }

    /**
     * Creates an IsNotEqual expression from this expression (as left expression) and the given expression (as
     * the right expression).
     *
     * @param right The right expression.
     * @return A new IsNotEqual binary expression.
     */
    public IsNotEqual isNotEqual(ComparableExpression<Number> right) {
        return new IsNotEqual(this, right);
    }

    /**
     * Creates an Add expression form this expression (as left expression) and the given right expression.
     *
     * @param right The right expression.
     * @return A new binary expression that contains two number expressions and evaluates to a number expression.
     */
    public Add add(Expression<Number> right) {
        return new Add(this, right);
    }

    public int compareTo(Object evaluateObject, Number value) {

        Number thisNumber = evaluate(evaluateObject);

        double difference = thisNumber.doubleValue() - value.doubleValue();

        return (int)Math.signum(difference);
    }

    public void switchOn(BasicTypeSwitch switcher) {

        switcher.caseNumber(this);
    }
}