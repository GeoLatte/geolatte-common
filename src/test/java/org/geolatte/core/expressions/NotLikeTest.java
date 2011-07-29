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

import org.junit.Assert;
import org.junit.Test;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 26-May-2010<br>
 * <i>Creation-Time</i>:  19:57:58<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class NotLikeTest extends AbstractStringLikeComparisonTest {

    @Test
    public void testEvaluate() throws Exception {

        NotLike expr = null;

        expr = new NotLike(aStringExpression, singleWildcardMatchesStringExpression);
        Assert.assertEquals(false, expr.evaluate(theObjectToEvaluate));

        expr = new NotLike(aStringExpression, anotherSingleWildcardMatchesStringExpression);
        Assert.assertEquals(false, expr.evaluate(theObjectToEvaluate));

        expr = new NotLike(aStringExpression, doubleWildcardMatchesStringExpression);
        Assert.assertEquals(false, expr.evaluate(theObjectToEvaluate));

        expr = new NotLike(aStringExpression, anotherDoubleWildcardMatchesStringExpression);
        Assert.assertEquals(false, expr.evaluate(theObjectToEvaluate));

        expr = new NotLike(aStringExpression, singleWildcardDoesNotMatchStringExpression);
        Assert.assertEquals(true, expr.evaluate(theObjectToEvaluate));

        expr = new NotLike(aStringExpression, anotherSingleWildcardDoesNotMatchStringExpression);
        Assert.assertEquals(true, expr.evaluate(theObjectToEvaluate));

        expr = new NotLike(aStringExpression, doubleWildcardDoesNotMatchStringExpression);
        Assert.assertEquals(true, expr.evaluate(theObjectToEvaluate));

        expr = new NotLike(aStringExpression, anotherDoubleWildcardDoesNotMatchStringExpression);
        Assert.assertEquals(true, expr.evaluate(theObjectToEvaluate));
    }
}
