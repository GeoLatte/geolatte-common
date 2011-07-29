/*
 * This file is part of the GeoLatte project. This code is licenced under
 * the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 *
 * Copyright (C) 2010 - 2010 and Ownership of code is shared by:
 * Qmino bvba - Romeinsestraat 18 - 3001 Heverlee  (http://www.Qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.core.expressions;

import org.junit.Assert;
import org.junit.Test;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 10-Aug-2010<br>
 * <i>Creation-Time</i>:  19:12:36<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class BooleanIsEqualTest extends AbstractBooleanComparisonTest {

    @Test
    public void testEvaluate() throws Exception {

        BooleanIsEqual exp = null;

        // Test the true case
        exp = new BooleanIsEqual(falseExpression, falseExpression);
        Assert.assertEquals(true, exp.evaluate(theObjectToEvaluate));
        exp = new BooleanIsEqual(trueExpression, trueExpression);
        Assert.assertEquals(true, exp.evaluate(theObjectToEvaluate));

        // Test the false case
        exp = new BooleanIsEqual(trueExpression, falseExpression);
        Assert.assertEquals(false, exp.evaluate(theObjectToEvaluate));

        // Test another false case
        exp = new BooleanIsEqual(falseExpression, trueExpression);
        Assert.assertEquals(false, exp.evaluate(theObjectToEvaluate));
    }
}
