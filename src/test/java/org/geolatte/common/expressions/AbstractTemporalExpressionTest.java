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

import java.util.Calendar;
import java.util.Date;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 01-Jun-2010<br>
 * <i>Creation-Time</i>:  19:07:31<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public abstract class AbstractTemporalExpressionTest {

    protected Date now = new Date();
    protected Date earlier;
    protected Date later;

    // Set Imposteriser to enable mocking classes (otherwise, only interfaces can be mocked)
    protected Mockery context = new JUnit4Mockery() {
        {setImposteriser(ClassImposteriser.INSTANCE);}
    };

    @SuppressWarnings("unchecked")
    protected final ComparableExpression<Date> nowDateExpression = (ComparableExpression<Date>)context.mock(ComparableExpression.class, "NowDate");

    @SuppressWarnings("unchecked")
    protected final ComparableExpression<Date> laterDateExpression = (ComparableExpression<Date>)context.mock(ComparableExpression.class, "LaterDate");

    @SuppressWarnings("unchecked")
    protected final ComparableExpression<Date> earlierDateExpression = (ComparableExpression<Date>)context.mock(ComparableExpression.class, "EarlierDate");

    protected Object theObjectToEvaluate = new Object();

    @Before
    public void setUp() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, 5);
        later = cal.getTime();

        cal.setTime(now);
        cal.add(Calendar.DATE, -5);
        earlier = cal.getTime();

        context.checking(new Expectations() {
            {allowing(nowDateExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(now));
             allowing(nowDateExpression).compareTo(with(theObjectToEvaluate), with(now)); will(returnValue(0));
             allowing(nowDateExpression).compareTo(with(theObjectToEvaluate), with(earlier)); will(returnValue(1));
             allowing(nowDateExpression).compareTo(with(theObjectToEvaluate), with(later)); will(returnValue(-1));}
        });

        context.checking(new Expectations() {
            {allowing(laterDateExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(later));
             allowing(laterDateExpression).compareTo(with(theObjectToEvaluate), with(later)); will(returnValue(0));
             allowing(laterDateExpression).compareTo(with(theObjectToEvaluate), with(now)); will(returnValue(1));
             allowing(laterDateExpression).compareTo(with(theObjectToEvaluate), with(earlier)); will(returnValue(1));}
        });

        context.checking(new Expectations() {
            {allowing(earlierDateExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(earlier));
             allowing(earlierDateExpression).compareTo(with(theObjectToEvaluate), with(earlier)); will(returnValue(0));
             allowing(earlierDateExpression).compareTo(with(theObjectToEvaluate), with(now)); will(returnValue(-1));
             allowing(earlierDateExpression).compareTo(with(theObjectToEvaluate), with(later)); will(returnValue(-1));}
        });

    }

    @After
    public void tearDown() throws Exception {
    }
}
