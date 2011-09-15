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
 * Simple wrapper for boolean {@link Expression}.
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
public class Filter {

    private final Expression<Boolean> filterExpression;

    /**
     * Constructor.
     *
     * @param filterExpression The filter expression.
     * @throws IllegalArgumentException If the given filterExpression is null.
     */
    public Filter(Expression<Boolean> filterExpression) throws IllegalArgumentException {
        if(filterExpression == null){
            throw new IllegalArgumentException("The given expression is null.");
        }
        this.filterExpression = filterExpression;
    }

    /**
     * Evaluates the given object against this filter.
     *
     * @param o The object to evaluate.
     * @return True if the object is accepted by the filter, false otherwise.
     */
    public Boolean evaluate(Object o) {
        return filterExpression.evaluate(o);
    }
}