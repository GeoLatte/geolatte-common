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

import org.geolatte.common.util.WildcardMatch;

/**
 * <p>
 * "like" expression for Strings. This expression implements wildcard comparisons of strings.
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
public class Like extends StringLikeComparisonExpression {

    /**
     * Constructor of a binary expression.
     *
     * @param left  The left expression.
     * @param right The right expression.
     */
    public Like(Expression<String> left, Expression<String> right) {
        super(left, right);
    }

    /**
     * Constructor of a binary expression.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @param caseInsensitive Indicates whether string comparison should ignore case.
     */
    public Like(Expression<String> left, Expression<String> right, boolean caseInsensitive) {
        super(left, right, caseInsensitive);
    }

    /**
     * Constructor of a binary expression.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @param wildcardChar the character to use as a wildcard
     */
    public Like(Expression<String> left, Expression<String> right, char wildcardChar) {
        super(left, right, wildcardChar);
    }

    /**
     * Constructor of a binary expression.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @param wildcardChar the character to use as a wildcard
     * @param caseInsensitive Indicates whether string comparision should ignore case.
     */
    public Like(Expression<String> left, Expression<String> right, char wildcardChar, boolean caseInsensitive) {
        super(left, right, wildcardChar, caseInsensitive);
    }

    public Boolean evaluate(Object o) {

//        WildcardMatch wc = new WildcardMatch('_', '%', '\\');
        WildcardMatch wc = new WildcardMatch('_', getWildcardChar(), '\\');
        wc.setCaseSensitive(!isCaseInsensitive());
        return wc.match(getLeft().evaluate(o), getRight().evaluate(o));
    }
}