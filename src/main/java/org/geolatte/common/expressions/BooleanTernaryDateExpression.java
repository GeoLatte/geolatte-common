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

import java.util.Date;

/**
 * <p>
 * Represents expressions that operates on three Date expressions and returns a boolean value.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 09-Aug-2010<br>
 * <i>Creation-Time</i>:  17:34:42<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public abstract class BooleanTernaryDateExpression extends BooleanExpression {

    private Expression<Date> first;
    private Expression<Date> second;
    private Expression<Date> third;

    /**
     * Constructs a BooleanTernaryDateExpression.
     * @param first The first operand.
     * @param second The second operand.
     * @param third The third operand.
     */
    protected BooleanTernaryDateExpression(Expression<Date> first, Expression<Date> second, Expression<Date> third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    /**
     * Gets the first operand.
     * @return The first operand.
     */
    public Expression<Date> getFirst() {
        return first;
    }

    /**
     * Gets the second operand.
     * @return The second operand.
     */
    public Expression<Date> getSecond() {
        return second;
    }

    /**
     * Gets the third operand.
     * @return The third operand.
     */
    public Expression<Date> getThird() {
        return third;
    }
}
