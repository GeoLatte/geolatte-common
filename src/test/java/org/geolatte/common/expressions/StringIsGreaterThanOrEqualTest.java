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

import org.junit.Assert;
import org.junit.Test;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 11-Aug-2010<br>
 * <i>Creation-Time</i>:  13:25:01<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class StringIsGreaterThanOrEqualTest extends AbstractStringComparisonTest {

    @Test
    public void testEvaluate() throws Exception {

        StringIsGreaterThanOrEqual exp = null;

        // Test the true case
        exp = new StringIsGreaterThanOrEqual(greaterStringExpression, baseStringExpression);
        Assert.assertTrue(exp.evaluate(theObjectToEvaluate));
        exp = new StringIsGreaterThanOrEqual(greaterStringExpression, lowerStringExpression);
        Assert.assertTrue(exp.evaluate(theObjectToEvaluate));
        exp = new StringIsGreaterThanOrEqual(baseStringExpression, baseStringExpression);
        Assert.assertTrue(exp.evaluate(theObjectToEvaluate));

        // Test the false case
        exp = new StringIsGreaterThanOrEqual(baseStringExpression, greaterStringExpression);
        Assert.assertFalse(exp.evaluate(theObjectToEvaluate));
    }
}
