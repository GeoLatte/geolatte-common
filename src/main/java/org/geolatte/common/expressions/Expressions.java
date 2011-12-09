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

package org.geolatte.common.expressions;

import org.geolatte.common.expressions.geo.GeoEquals;
import org.geolatte.common.expressions.geo.GeometryConstant;
import org.geolatte.common.expressions.geo.GeometryProperty;
import org.geolatte.geom.Geometry;

import java.util.Date;

/**
 * <p>
 * A convenient factory class to construct all kinds of expressions. 
 * </p>
 * <p>
 * <i>Creation-Date</i>: 23-Jul-2010<br>
 * <i>Creation-Time</i>:  14:16:22<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class Expressions {

    /**
     * Creates a NumberConstant expression from the given number.
     *
     * @param number The number.
     * @return A NumberConstant expression.
     */
    public static NumberConstant constant(Number number) {
        return new NumberConstant(number);
    }

    /**
     * Creates a StringConstant expression from the given string.
     *
     * @param string The string.
     * @return A StringConstant expression.
     */
    public static StringConstant constant(String string) {
        return new StringConstant(string);
    }

    /**
     * Creates a BooleanConstant expression from the given value.
     *
     * @param bool The boolean value.
     * @return A BooleanConstant expression.
     */
    public static BooleanConstant constant(Boolean bool) {
        return new BooleanConstant(bool);
    }

    /**
     * Creates a DateConstant expression from the given number.
     *
     * @param date The date.
     * @return A DateConstant expression.
     */
    public static DateConstant constant(Date date) {
        return new DateConstant(date);
    }

    public static GeometryConstant constant(Geometry geometry) {

        return new GeometryConstant(geometry);
    }

    /**
     * Creates a NumberProperty based on the given property name.
     *
     * @param propertyName The property name.
     * @return A new NumberProperty.
     */
    public static NumberProperty numberProperty(String propertyName) {
        return new NumberProperty(propertyName);
    }

    /**
     * Creates a BooleanProperty based on the given property name.
     *
     * @param propertyName The property name.
     * @return A new NumberProperty.
     */
    public static BooleanProperty booleanProperty(String propertyName) {
        return new BooleanProperty(propertyName);
    }

    /**
     * Creates a StringProperty based on the given property name.
     *
     * @param propertyName The property name.
     * @return A StringProperty.
     */
    public static StringProperty stringProperty(String propertyName) {
        return new StringProperty(propertyName);
    }

    /**
     * Creates a StringProperty based on the given property name.
     * @param propertyName The property name.
     * @return A DateProperty
     */
    public static DateProperty dateProperty(String propertyName) {
        return new DateProperty(propertyName);
    }

    /**
     * Creates an IsEqual expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return A new IsEqual binary expression.
     */
    public static IsEqual isEqual(ComparableExpression<Number> left, ComparableExpression<Number> right) {
        return new IsEqual(left, right);
    }

    /**
     * Creates an IsEqual expression from the given expression and constant.
     *
     * @param left  The left expression.
     * @param constant The constant to compare to.
     * @return A new IsEqual binary expression.
     */
    public static IsEqual isEqual(ComparableExpression<Number> left, Number constant) {
        return new IsEqual(left, constant(constant));
    }

    /**
     * Creates an IsEqual expression from the given expression and constant.
     *
     * @param left  The left expression.
     * @param constant The constant to compare to (must be a Number).
     * @throws IllegalArgumentException If constant is not a Number.
     * @return A new IsEqual binary expression.
     */
    public static IsEqual isEqual(NumberExpression left, Object constant) {

        if (!(constant instanceof Number))
            throw new IllegalArgumentException("constant is not a Number");

        return new IsEqual(left, constant((Number)constant));
    }

    /**
     * Creates a BooleanIsEqual expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return A new BooleanIsEqual binary expression.
     */
    public static BooleanIsEqual isEqual(ComparableExpression<Boolean> left, ComparableExpression<Boolean> right) {
        return new BooleanIsEqual(left, right);
    }

    /**
     * Creates a BooleanIsEqual expression from the given expression and constant.
     *
     * @param left  The left expression.
     * @param constant The constant to compare to.
     * @return A new BooleanIsEqual binary expression.
     */
    public static BooleanIsEqual isEqual(ComparableExpression<Boolean> left, Boolean constant) {
        return new BooleanIsEqual(left, constant(constant));
    }

    /**
     * Creates an IsEqual expression from the given expression and constant.
     *
     * @param left  The left expression.
     * @param constant The constant to compare to (must be a Boolean).
     * @throws IllegalArgumentException If constant is not a Boolean.
     * @return A new IsEqual binary expression.
     */
    public static BooleanIsEqual isEqual(BooleanExpression left, Object constant) {

        if (!(constant instanceof Boolean))
            throw new IllegalArgumentException("constant is not a Boolean");

        return new BooleanIsEqual(left, constant((Boolean)constant));
    }

    /**
     * Creates an StringIsEqual expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return A new StringIsEqual binary expression.
     */
    public static StringIsEqual isEqual(ComparableExpression<String> left, ComparableExpression<String> right) {

        return new StringIsEqual(left, right);
    }

    /**
     * Creates an IsEqual expression from the given expression and constant.
     *
     * @param left  The left expression.
     * @param constant The constant to compare to (must be a String).
     * @throws IllegalArgumentException If constant is not a String.
     * @return A new IsEqual binary expression.
     */
    public static StringIsEqual isEqual(StringExpression left, Object constant) {

        if (!(constant instanceof String))
            throw new IllegalArgumentException("constant is not a String");

        return new StringIsEqual(left, constant((String)constant));
    }

    /**
     * Creates an IsGreaterThan expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return A new IsGreaterThan binary expression.
     */
    public static IsGreaterThan isGreaterThan(ComparableExpression<Number> left, ComparableExpression<Number> right) {
        return new IsGreaterThan(left, right);
    }

    /**
     * Creates a BooleanIsGreaterThan expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return A new IsGreaterThan binary expression.
     */
    public static BooleanIsGreaterThan isGreaterThan(ComparableExpression<Boolean> left, ComparableExpression<Boolean> right) {
        return new BooleanIsGreaterThan(left, right);
    }

    /**
     * Creates a StringIsGreaterThan expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return A new IsGreaterThan binary expression.
     */
    public static StringIsGreaterThan isGreaterThan(ComparableExpression<String> left, ComparableExpression<String> right) {
        return new StringIsGreaterThan(left, right);
    }

    /**
     * Creates an IsGreaterThanOrEqual expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return A new IsGreaterThanOrEqual binary expression.
     */
    public static IsGreaterThanOrEqual isGreaterThanOrEqual(ComparableExpression<Number> left, ComparableExpression<Number> right) {
        return new IsGreaterThanOrEqual(left, right);
    }

    /**
     * Creates an BooleanIsGreaterThanOrEqual expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return A new BooleanIsGreaterThanOrEqual binary expression.
     */
    public static BooleanIsGreaterThanOrEqual isGreaterThanOrEqual(ComparableExpression<Boolean> left, ComparableExpression<Boolean> right) {
        return new BooleanIsGreaterThanOrEqual(left, right);
    }

    /**
     * Creates an BooleanIsGreaterThanOrEqual expression from the given expression and constant.
     *
     * @param left  The left expression.
     * @param constant The constant to compare to (must be a Boolean).
     * @throws IllegalArgumentException If the constant is not a Boolean or a {@link org.geolatte.common.expressions.ComparableExpression}
     * @return A new is less than binary expression.
     */
    public static BooleanIsLessThanOrEqual isLessThanOrEqual(ComparableExpression<Boolean> left, Object constant) {

        if (!(constant instanceof Boolean))
            throw new IllegalArgumentException("constant is not a Boolean");

        return new BooleanIsLessThanOrEqual(left, constant((Boolean)constant));
    }

    /**
     * Creates an StringIsGreaterThanOrEqual expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return A new StringIsGreaterThanOrEqual binary expression.
     */
    public static StringIsGreaterThanOrEqual isGreaterThanOrEqual(ComparableExpression<String> left, ComparableExpression<String> right) {
        return new StringIsGreaterThanOrEqual(left, right);
    }

    /**
     * Creates an IsLessThan expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return A new is less than binary expression.
     */
    public static IsLessThan isLessThan(ComparableExpression<Number> left, ComparableExpression<Number> right) {
        return new IsLessThan(left, right);
    }

    /**
     * Creates an IsLessThan expression from the given expression and constant.
     *
     * @param left  The left expression.
     * @param constant The constant to compare to (must be a Number).
     * @throws IllegalArgumentException If the constant is not a Number
     * @return A new is less than binary expression.
     */
    public static IsLessThan isLessThan(ComparableExpression<Number> left, Object constant) {

        if (!(constant instanceof Number))
            throw new IllegalArgumentException("constant is not a Number");

        return new IsLessThan(left, constant((Number)constant));
    }

    /**
     * Creates an BooleanIsLessThan expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return A new is less than binary expression.
     */
    public static BooleanIsLessThan isLessThan(ComparableExpression<Boolean> left, ComparableExpression<Boolean> right) {
        return new BooleanIsLessThan(left, right);
    }

    /**
     * Creates an BooleanIsLessThan expression from the given expression and constant.
     *
     * @param left  The left expression.
     * @param constant The constant to compare to (must be a Number).
     * @throws IllegalArgumentException If the constant is not a Number
     * @return A new is less than binary expression.
     */
    public static BooleanIsLessThan isLessThan(ComparableExpression<Boolean> left, Object constant) {

        if (!(constant instanceof Boolean))
            throw new IllegalArgumentException("constant is not a Boolean");

        return new BooleanIsLessThan(left, constant((Boolean)constant));
    }

    /**
     * Creates an StringIsLessThan expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return A new is less than binary expression.
     */
    public static StringIsLessThan isLessThan(ComparableExpression<String> left, ComparableExpression<String> right) {
        return new StringIsLessThan(left, right);
    }

    /**
     * Creates an StringIsLessThan expression from the given expression and constant.
     *
     * @param left  The left expression.
     * @param constant The constant to compare to (must be a String).
     * @throws IllegalArgumentException If the constant is not a String
     * @return A new is less than binary expression.
     */
    public static StringIsLessThan isLessThan(ComparableExpression<String> left, Object constant) {

        if (!(constant instanceof String))
            throw new IllegalArgumentException("constant is not a String");

        return new StringIsLessThan(left, constant((String)constant));
    }

    /**
     * Creates an IsLessThanOrEqual expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return A new IsLessThanOrEqual binary expression.
     */
    public static IsLessThanOrEqual isLessThanOrEqual(ComparableExpression<Number> left, ComparableExpression<Number> right) {
        return new IsLessThanOrEqual(left, right);
    }

    /**
     * Creates an IsLessThanOrEqual expression from the given expression and constant.
     *
     * @param left  The left expression.
     * @param constant The constant to compare to (must be a Number).
     * @throws IllegalArgumentException If the constant is not a Number
     * @return A new is less than binary expression.
     */
    public static IsLessThanOrEqual isLessThanOrEqual(ComparableExpression<Number> left, Object constant) {

        if (!(constant instanceof Number))
            throw new IllegalArgumentException("constant is not a Number");

        return new IsLessThanOrEqual(left, constant((Number)constant));
    }



    /**
     * Creates an StringIsLessThanOrEqual expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return A new StringIsLessThanOrEqual binary expression.
     */
    public static StringIsLessThanOrEqual isLessThanOrEqual(ComparableExpression<String> left, ComparableExpression<String> right) {
        return new StringIsLessThanOrEqual(left, right);
    }

    /**
     * Creates an StringIsLessThanOrEqual expression from the given expression and constant.
     *
     * @param left  The left expression.
     * @param constant The constant to compare to (must be a String).
     * @throws IllegalArgumentException If the constant is not a String
     * @return A new is less than binary expression.
     */
    public static StringIsLessThanOrEqual isLessThanOrEqual(ComparableExpression<String> left, Object constant) {

        if (!(constant instanceof String))
            throw new IllegalArgumentException("constant is not a String");

        return new StringIsLessThanOrEqual(left, constant((String)constant));
    }

    /**
     * Creates an IsNotEqual expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return A new IsNotEqual binary expression.
     */
    public static IsNotEqual isNotEqual(ComparableExpression<Number> left, ComparableExpression<Number> right) {
        return new IsNotEqual(left, right);
    }

    /**
     * Creates an IsNotEqual expression from the given expression and constant.
     *
     * @param left  The left expression.
     * @param constant The constant to compare to (must be a Number).
     * @throws IllegalArgumentException If the constant is not a Number
     * @return A new is less than binary expression.
     */
    public static IsNotEqual isNotEqual(ComparableExpression<Number> left, Object constant) {

        if (!(constant instanceof Number))
            throw new IllegalArgumentException("constant is not a Number");

        return new IsNotEqual(left, constant((Number)constant));
    }

    /**
     * Creates an BooleanIsNotEqual expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return A new BooleanIsNotEqual binary expression.
     */
    public static BooleanIsNotEqual isNotEqual(ComparableExpression<Boolean> left, ComparableExpression<Boolean> right) {
        return new BooleanIsNotEqual(left, right);
    }

    /**
     * Creates an StringIsNotEqual expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return A new StringIsNotEqual binary expression.
     */
    public static StringIsNotEqual isNotEqual(ComparableExpression<String> left, ComparableExpression<String> right) {
        return new StringIsNotEqual(left, right);
    }

    /**
     * Creates an Add expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return An Add expression.
     */
    public static Add add(Expression<Number> left, Expression<Number> right) {
        return new Add(left, right);
    }

    /**
     * Creates a Like expression from the given expressions with % as the wildcard character
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return A Like expression.
     */
    public static Like like(Expression<String> left, Expression<String> right) {
        return new Like(left, right);
    }

    /**
     * Creates a Like expression from the given expressions with % as the wildcard character
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @param caseInsensitive Indicates whether comparison should be case insensitive.
     * @return A Like expression.
     */
    public static Like like(Expression<String> left, Expression<String> right, boolean caseInsensitive) {
        return new Like(left, right, caseInsensitive);
    }

    /**
     * Creates a Like expression from the given expressions with % as the wildcard character
     *
     * @param left  The left expression.
     * @param constant The constant.
     * @return A Like expression.
     */
    public static Like like(Expression<String> left, String constant) {
        return new Like(left, constant(constant));
    }

    /**
     * Creates a Like expression from the given expressions with % as the wildcard character
     *
     * @param left  The left expression.
     * @param constant The constant.
     * @param caseInsensitive Indicates whether comparison should be case insensitive.
     * @return A Like expression.
     */
    public static Like like(Expression<String> left, String constant, boolean caseInsensitive) {
        return new Like(left, constant(constant), caseInsensitive);
    }

    /**
     * Creates a NotLike expression from the given expressions with % as the wildcard character
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return A NotLike expression.
     */
    public static NotLike notLike(Expression<String> left, Expression<String> right) {
        return new NotLike(left, right);
    }

    /**
     * Creates a NotLike expression from the given expressions with % as the wildcard character
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @param caseInsensitive Indicates whether comparison should be case insensitive.
     * @return A NotLike expression.
     */
    public static NotLike notLike(Expression<String> left, Expression<String> right, boolean caseInsensitive) {
        return new NotLike(left, right, caseInsensitive);
    }

    /**
     * Creates a NotLike expression from the given expressions with % as the wildcard character
     *
     * @param left  The left expression.
     * @param constant The constant.
     * @return A NotLike expression.
     */
    public static NotLike notLike(Expression<String> left, String constant) {
        return new NotLike(left, constant(constant));
    }

    /**
     * Creates a NotLike expression from the given expressions with % as the wildcard character
     *
     * @param left  The left expression.
     * @param constant The constant.
     * @param caseInsensitive Indicates whether comparison should be case insensitive.
     * @return A NotLike expression.
     */
    public static NotLike notLike(Expression<String> left, String constant, boolean caseInsensitive) {
        return new NotLike(left, constant(constant), caseInsensitive);
    }

    /**
     * Creates a Like expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @param wildCardCharacter The character to use as a wildcardcharacter
     * @return A Like expression.
     */
    public static Like like(Expression<String> left, Expression<String> right, char wildCardCharacter) {
        return new Like(left, right, wildCardCharacter);
    }

    /**
     * Creates a Like expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @param wildCardCharacter The character to use as a wildcardcharacter
     * @param caseInsensitive Indicates whether comparison should be case insensitive.
     * @return A Like expression.
     */
    public static Like like(Expression<String> left, Expression<String> right, char wildCardCharacter, boolean caseInsensitive) {
        return new Like(left, right, wildCardCharacter, caseInsensitive);
    }

    /**
     * Creates a Like expression from the given expressions.
     *
     * @param left  The left expression.
     * @param constant The constant.
     * @param wildCardCharacter The character to use as a wildcardcharacter
     * @return A Like expression.
     */
    public static Like like(Expression<String> left, String constant, char wildCardCharacter) {
        return new Like(left, constant(constant), wildCardCharacter);
    }

    /**
     * Creates a Like expression from the given expressions.
     *
     * @param left  The left expression.
     * @param constant The constant.
     * @param wildCardCharacter The character to use as a wildcardcharacter
     * @param caseInsensitive Indicates whether comparison should be case insensitive.
     * @return A Like expression.
     */
    public static Like like(Expression<String> left, String constant, char wildCardCharacter, boolean caseInsensitive) {
        return new Like(left, constant(constant), wildCardCharacter, caseInsensitive);
    }

    /**
     * Creates a NotLike expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @param wildCardCharacter The character to use as a wildcardCharacter
     * @return A NotLike expression.
     */
    public static NotLike notLike(Expression<String> left, Expression<String> right, char wildCardCharacter) {
        return new NotLike(left, right, wildCardCharacter);
    }

    /**
     * Creates a NotLike expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @param wildCardCharacter The character to use as a wildcardCharacter
     * @param caseInsensitive Indicates whether comparison should be case insensitive.
     * @return A NotLike expression.
     */
    public static NotLike notLike(Expression<String> left, Expression<String> right, char wildCardCharacter, boolean caseInsensitive) {
        return new NotLike(left, right, wildCardCharacter, caseInsensitive);
    }

    /**
     * Creates a NotLike expression from the given expressions.
     *
     * @param left  The left expression.
     * @param constant The constant.
     * @param wildCardCharacter The character to use as a wildcardCharacter
     * @return A NotLike expression.
     */
    public static NotLike notLike(Expression<String> left, String constant, char wildCardCharacter) {
        return new NotLike(left, constant(constant), wildCardCharacter);
    }

    /**
     * Creates a NotLike expression from the given expressions.
     *
     * @param left  The left expression.
     * @param constant The constant.
     * @param wildCardCharacter The character to use as a wildcardCharacter
     * @param caseInsensitive Indicates whether comparison should be case insensitive.
     * @return A NotLike expression.
     */
    public static NotLike notLike(Expression<String> left, String constant, char wildCardCharacter, boolean caseInsensitive) {
        return new NotLike(left, constant(constant), wildCardCharacter, caseInsensitive);
    }

    /**
     * Creates an And expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return An And expression.
     */
    public static And and(Expression<Boolean> left, Expression<Boolean> right) {
        return new And(left, right);
    }

    /**
     * Creates an Or expression from the given expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @return An Or expression.
     */
    public static Or or(Expression<Boolean> left, Expression<Boolean> right) {
        return new Or(left, right);
    }

    /**
     * Creates a Not expression from the given expressions.
     *
     * @param expression The expression.
     * @return An Not expression.
     */
    public static Not not(Expression<Boolean> expression) {
        return new Not(expression);
    }

    /**
     * Creates an IsBefore expression from the given expressions.
     *
     * @param left The left hand side of the comparison
     * @param right The right hand side of the comparison.
     * @return An IsBefore expression.
     */
    public static IsBefore isBefore(Expression<Date> left, Expression<Date> right) {
        return new IsBefore(left, right);
    }

    /**
     * Creates an IsAfter expression from the given expressions.
     *
     * @param left The left hand side of the comparison
     * @param right The right hand side of the comparison.
     * @return An IsBefore expression.
     */
    public static IsAfter isAfter(Expression<Date> left, Expression<Date> right) {
        return new IsAfter(left, right);
    }

    /**
     * Creates an IsBetween expression from the given expressions.
     *
     * @param date The date to compare.
     * @param lowDate The low date to compare to.
     * @param highDate The high date to compare to
     * @return A DateIsBetween expression.
     */
    public static DateIsBetween isBetween(ComparableExpression<Date> date, ComparableExpression<Date> lowDate, ComparableExpression<Date> highDate) {
        return new DateIsBetween(date, lowDate, highDate);
    }

    /**
     * Creates a PropertyDoesNotExist expression from the given property name.
     * @param propertyName The property name.
     * @return A PropertyDoesNotExist expression.
     */
    public static PropertyDoesNotExist doesNotExist(String propertyName) {
        return new PropertyDoesNotExist(propertyName);
    }

    /**
     * Creates a PropertyExists expression from the given property name.
     * @param propertyName The property name.
     * @return A PropertyExists expression.
     */
    public static PropertyExists exists(String propertyName) {
        return new PropertyExists(propertyName);
    }

    /**
     * Creates a GeoEquals expression from the left and right expressions.
     * @param left The left expression.
     * @param right The right expression.
     * @return A GeoEquals expression.
     */
    public static GeoEquals geoEquals(Expression<Geometry> left, Expression<Geometry> right) {
        return new GeoEquals(left, right);
    }

    public static GeometryProperty geometryProperty(String attributeName) {

        return new GeometryProperty(attributeName);
    }
}
