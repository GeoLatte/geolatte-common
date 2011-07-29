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
 * <i>Creation-Time</i>:  19:48:42<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public abstract class AbstractStringLikeComparisonTest {

    // Set Imposteriser to enable mocking classes (otherwise, only interfaces can be mocked)
    protected Mockery context = new JUnit4Mockery() {
        {setImposteriser(ClassImposteriser.INSTANCE);}
    };

    @SuppressWarnings("unchecked")
    protected final Expression<String> aStringExpression = (Expression<String>)context.mock(Expression.class, "AString");
    protected final String aString = "Scarlett Johansson";

    @SuppressWarnings("unchecked")
    protected final Expression<String> singleWildcardMatchesStringExpression = (Expression<String>)context.mock(Expression.class, "SingleWildcardMatchesString");
    protected final String singleWildcardMatchesString = "%hansson";

    @SuppressWarnings("unchecked")
    protected final Expression<String> anotherSingleWildcardMatchesStringExpression = (Expression<String>)context.mock(Expression.class, "AnotherSingleWildcardMatchesString");
    protected final String anotherSingleWildcardMatchesString = "Scarle%";

    @SuppressWarnings("unchecked")
    protected final Expression<String> doubleWildcardMatchesStringExpression = (Expression<String>)context.mock(Expression.class, "DoubleWildcardMatchesString");
    protected final String doubleWildcardMatchesString = "Scar% Joha%on";

    @SuppressWarnings("unchecked")
    protected final Expression<String> anotherDoubleWildcardMatchesStringExpression = (Expression<String>)context.mock(Expression.class, "AnotherDoubleWildcardMatchesString");
    protected final String anotherDoubleWildcardMatchesString = "%Scarlett Joh%son";


    @SuppressWarnings("unchecked")
    protected final Expression<String> singleWildcardDoesNotMatchStringExpression = (Expression<String>)context.mock(Expression.class, "SingleWildcardDoesNotMatchString");
    protected final String singleWildcardDoesNotMatchString = "%hansso";

    @SuppressWarnings("unchecked")
    protected final Expression<String> anotherSingleWildcardDoesNotMatchStringExpression = (Expression<String>)context.mock(Expression.class, "AnotherSingleWildcardDoesNotMatchString");
    protected final String anotherSingleWildcardDoesNotMatchString = "Scarlett %man";

    @SuppressWarnings("unchecked")
    protected final Expression<String> doubleWildcardDoesNotMatchStringExpression = (Expression<String>)context.mock(Expression.class, "DoubleWildcardDoesNotMatchString");
    protected final String doubleWildcardDoesNotMatchString = "Scart% Joha%on";

    @SuppressWarnings("unchecked")
    protected final Expression<String> anotherDoubleWildcardDoesNotMatchStringExpression = (Expression<String>)context.mock(Expression.class, "AnotherDoubleWildcardDoesNotMatchString");
    protected final String anotherDoubleWildcardDoesNotMatchString = "%Scarlett Joh%so";

    @SuppressWarnings("unchecked")
    protected final Expression<String> doubleUserSpecifiedWildcardMatchesStringExpression = (Expression<String>)context.mock(Expression.class, "DoubleUserSpecifiedWildcardMatchesString");
    protected final String doubleUserSpecifiedWildcardMatchesString = "Scarl* Joha*on";


    @SuppressWarnings("unchecked")
    protected final Expression<String> doubleUserSpecifiedWildcardDoesNotMatchStringExpression = (Expression<String>)context.mock(Expression.class, "DoubleUserSpecifiedWildcardDoesNotMatchString");
    protected final String doubleUserSpecifiedWildcardDoesNotMatchString = "*Scarlett Joh*so";



    protected Object theObjectToEvaluate = new Object();
    protected char userSpecifiedWildCard = '*';

    @Before
    public void setUp() throws Exception {

        context.checking(new Expectations() {
            {allowing(aStringExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(aString));}
        });

        context.checking(new Expectations() {
            {allowing(singleWildcardMatchesStringExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(singleWildcardMatchesString));}
        });
        context.checking(new Expectations() {
            {allowing(anotherSingleWildcardMatchesStringExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(anotherSingleWildcardMatchesString));}
        });
        context.checking(new Expectations() {
            {allowing(doubleWildcardMatchesStringExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(doubleWildcardMatchesString));}
        });
        context.checking(new Expectations() {
            {allowing(anotherDoubleWildcardMatchesStringExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(anotherDoubleWildcardMatchesString));}
        });

        context.checking(new Expectations() {
            {allowing(singleWildcardDoesNotMatchStringExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(singleWildcardDoesNotMatchString));}
        });
        context.checking(new Expectations() {
            {allowing(anotherSingleWildcardDoesNotMatchStringExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(anotherSingleWildcardDoesNotMatchString));}
        });
        context.checking(new Expectations() {
            {allowing(doubleWildcardDoesNotMatchStringExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(doubleWildcardDoesNotMatchString));}
        });
        context.checking(new Expectations() {
            {allowing(anotherDoubleWildcardDoesNotMatchStringExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(anotherDoubleWildcardDoesNotMatchString));}
        });
        context.checking(new Expectations() {
            {allowing(doubleUserSpecifiedWildcardMatchesStringExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(doubleUserSpecifiedWildcardMatchesString));}
        });
        context.checking(new Expectations() {
                {allowing(doubleUserSpecifiedWildcardDoesNotMatchStringExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(doubleUserSpecifiedWildcardDoesNotMatchString));}
            });


    }

    @After
    public void tearDown() throws Exception {
    }
}
