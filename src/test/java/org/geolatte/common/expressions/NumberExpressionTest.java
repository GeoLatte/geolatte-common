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

import org.geolatte.common.expressions.stubs.NumberExpressionStub;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 11-Aug-2010<br>
 * <i>Creation-Time</i>:  09:47:10<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class NumberExpressionTest {

    private Number baseNumber;
    private Number greaterNumber;
    private Number lowerNumber;

    private NumberExpression baseExpression;

    private Object objectToEvaluate;

    private Mockery context = new Mockery();

    @Before
    public void setUp() throws Exception {

        baseNumber = 5;
        greaterNumber = 10;
        lowerNumber = 1;

        baseExpression = new NumberExpressionStub(baseNumber);

        objectToEvaluate = new Object();
    }

    @Test
    public void testCompareTo() throws Exception {

        Assert.assertTrue(baseExpression.compareTo(objectToEvaluate, lowerNumber) > 0);
        Assert.assertTrue(baseExpression.compareTo(objectToEvaluate, greaterNumber) < 0);
        Assert.assertEquals(0, baseExpression.compareTo(objectToEvaluate, baseNumber));
    }

    @Test
    public void testSwitchOn() throws Exception {

        final BasicTypeSwitch basicSwitch = context.mock(BasicTypeSwitch.class);

        context.checking(new Expectations() {
            {exactly(1).of(basicSwitch).caseNumber(baseExpression);}
        });

        baseExpression.switchOn(basicSwitch);

        context.assertIsSatisfied();
    }
}
