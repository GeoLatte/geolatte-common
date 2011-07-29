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

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 25-May-2010<br>
 * <i>Creation-Time</i>:  17:38:52<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public abstract class AbstractBooleanOperatorTest {

    // Set Imposteriser to enable mocking classes (otherwise, only interfaces can be mocked)
    protected Mockery context = new JUnit4Mockery() {
        {setImposteriser(ClassImposteriser.INSTANCE);}
    };

    @SuppressWarnings("unchecked")
    protected final Expression<Boolean> falseExpression = (Expression<Boolean>)context.mock(Expression.class, "False");

    @SuppressWarnings("unchecked")
    protected final Expression<Boolean> trueExpression = (Expression<Boolean>)context.mock(Expression.class, "True");

    protected Object theObjectToEvaluate = new Object();

    @Before
    public void setUp() throws Exception {

        context.checking(new Expectations() {
            {allowing(falseExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(false));}
        });

        context.checking(new Expectations() {
            {allowing(trueExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(true));}
        });

    }

    @After
    public void tearDown() throws Exception {
    }
}
