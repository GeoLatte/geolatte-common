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
 * "is between" expression: checks whether a base date falls in a specified period.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 09-Aug-2010<br>
 * <i>Creation-Time</i>:  17:29:56<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class DateIsBetween extends DateComparisonExpression {

    /**
     * Constructs a DateIsBetween object.
     * @param baseDate The date that is checked being between lowDate and highDate.
     * @param lowDate The low date.
     * @param highDate The high date.
     */
    protected DateIsBetween(ComparableExpression<Date> baseDate, ComparableExpression<Date> lowDate, ComparableExpression<Date> highDate) {
        super(baseDate, lowDate, highDate);
    }

    public Boolean evaluate(Object o) {

        return getBaseDate().compareTo(o, getLowDate().evaluate(o)) > 0 && getBaseDate().compareTo(o, getHighDate().evaluate(o)) < 0;
    }
}
