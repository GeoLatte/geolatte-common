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
import org.junit.Before;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 10-Aug-2010<br>
 * <i>Creation-Time</i>:  18:07:31<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class AbstractStringComparisonTest {

    private String baseString = "def";
    private String lowerString = "abc";
    private String greaterString = "ghi";

    // Set Imposteriser to enable mocking classes (otherwise, only interfaces can be mocked)
    protected Mockery context = new JUnit4Mockery() {
        {setImposteriser(ClassImposteriser.INSTANCE);}
    };

    @SuppressWarnings("unchecked")
    protected final ComparableExpression<String> baseStringExpression = (ComparableExpression<String>)context.mock(ComparableExpression.class, "BaseString");

    @SuppressWarnings("unchecked")
    protected final ComparableExpression<String> lowerStringExpression = (ComparableExpression<String>)context.mock(ComparableExpression.class, "LowerString");

    @SuppressWarnings("unchecked")
    protected final ComparableExpression<String> greaterStringExpression = (ComparableExpression<String>)context.mock(ComparableExpression.class, "GreaterString");

    protected Object theObjectToEvaluate = new Object();

    @Before
    public void setUp() throws Exception {

        // Set return values for mocked baseStringExpression
        context.checking(new Expectations() {
            {allowing(baseStringExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(baseString));                     // Evaluate always returns baseString
             allowing(baseStringExpression).compareTo(with(theObjectToEvaluate), with(baseString)); will(returnValue(0));           // Compare to itself (equal) returns 0
             allowing(baseStringExpression).compareTo(with(theObjectToEvaluate), with(lowerString)); will(returnValue(1));          // Compare to a lower string returns 1
             allowing(baseStringExpression).compareTo(with(theObjectToEvaluate), with(greaterString)); will(returnValue(-1));}      // Compare to a greater string returns -1
        });

        // Set return values for mocked greaterStringExpression
        context.checking(new Expectations() {
            {allowing(greaterStringExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(greaterString));               // Evaluate always returns greaterString
             allowing(greaterStringExpression).compareTo(with(theObjectToEvaluate), with(greaterString)); will(returnValue(0));     // Compare to itself (equal) returns 0
             allowing(greaterStringExpression).compareTo(with(theObjectToEvaluate), with(lowerString)); will(returnValue(1));       // Compare to a lower string returns 1
             allowing(greaterStringExpression).compareTo(with(theObjectToEvaluate), with(baseString)); will(returnValue(1));}       // Compare to a lower string returns 1
        });

        // Set return values for mocked lowerStringExpression
        context.checking(new Expectations() {
            {allowing(lowerStringExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(lowerString));                   // Evaluate always returns lowerString
             allowing(lowerStringExpression).compareTo(with(theObjectToEvaluate), with(lowerString)); will(returnValue(0));         // Compare to itself (equal) returns 0
             allowing(lowerStringExpression).compareTo(with(theObjectToEvaluate), with(baseString)); will(returnValue(-1));         // Compare to a lower string returns -1
             allowing(lowerStringExpression).compareTo(with(theObjectToEvaluate), with(greaterString)); will(returnValue(-1));}     // Compare to a lower string returns -1
        });
    }
}
