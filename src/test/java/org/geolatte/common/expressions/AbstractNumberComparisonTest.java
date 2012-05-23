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

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;

/**
 * <p>
 * This is the parent class for number comparison tests. It defines an number of common mocks.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 25-May-2010<br>
 * <i>Creation-Time</i>:  16:53:19<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public abstract class AbstractNumberComparisonTest {

    private final int HIGHNUMBER = 5;
    private final int LOWNUMBER = 1;

    // Set Imposteriser to enable mocking classes (otherwise, only interfaces can be mocked)
    protected Mockery context = new JUnit4Mockery() {
        {setImposteriser(ClassImposteriser.INSTANCE);}
    };

    @SuppressWarnings("unchecked")
    protected final NumberExpression highNumberExpression = (NumberExpression)context.mock(NumberExpression.class, "HighNumber");

    @SuppressWarnings("unchecked")
    protected final NumberExpression lowNumberExpression = (NumberExpression)context.mock(NumberExpression.class, "LowNumber");

    protected Object theObjectToEvaluate = new Object();

    @Before
    public void setUp() throws Exception {

        // Set return values for mocked highNumberExpression
        context.checking(new Expectations() {
            {allowing(highNumberExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(HIGHNUMBER));                     // Evaluate always returns HIGHNUMBER
             allowing(highNumberExpression).compareTo(with(theObjectToEvaluate), with(HIGHNUMBER)); will(returnValue(0));           // Compare to itself (equal) returns 0
            allowing(highNumberExpression).compareTo(with(theObjectToEvaluate), with(LOWNUMBER)); will(returnValue(1));}            // Compare to a lower number returns 1
        });

        // Set return values for mocked lowNumberExpression
        context.checking(new Expectations() {
            {allowing(lowNumberExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(LOWNUMBER));                       // Evaluate always returns LOWNUMBER
             allowing(lowNumberExpression).compareTo(with(theObjectToEvaluate), with(LOWNUMBER)); will(returnValue(0));             // Compare to itself (equal) returns 0
            allowing(lowNumberExpression).compareTo(with(theObjectToEvaluate), with(HIGHNUMBER)); will(returnValue(-1));}           // Compare to a higher number returns -1
        });

    }

    @After
    public void tearDown() throws Exception {
    }
}
