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

package org.geolatte.core.transformer;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 06-May-2010<br>
 * <i>Creation-Time</i>:  14:42:28<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class AbstractObservableTransformerSinkTest {
    Mockery context = new JUnit4Mockery();

    AbstractObservableTransformerSink transformerSink;

    @Before
    public void setUp() throws Exception {

        transformerSink = new ConcreteObservableTransformerSink();
    }

    /**
    * Checks whether the TransformerErrorEvent is fired correctly once.
    */
    @Test
    public void test_TransformerSinkEventListenerSubscribeAndFire() {

        final TransformerSinkEventListener subscriber = context.mock(TransformerSinkEventListener.class);
        transformerSink.addTransformerSinkEventListener(subscriber);

        // Expectations
        final TransformerSinkErrorEvent expectedEvent = new TransformerSinkErrorEvent(this,null);
        context.checking(new Expectations() {
            {oneOf (subscriber).ErrorOccurred(expectedEvent);}
            });

        // Fire the event
        transformerSink.onTransformationSinkErrorOccurred(expectedEvent);

        // Check whether expectations have are satisfied
        context.assertIsSatisfied();
    }

    /**
    * Checks whether unsubscribing the TransformerErrorEvent works correctly.
    */
    @Test
    public void test_TransformerSinkEventListenerUnsubscribe() {

        final TransformerSinkEventListener subscriber = context.mock(TransformerSinkEventListener.class);

        // subscribe and unscubscribe
        transformerSink.addTransformerSinkEventListener(subscriber);
        transformerSink.removeTransformerSinkEventListener(subscriber);

        // Expectations
        final TransformerSinkErrorEvent expectedEvent = new TransformerSinkErrorEvent(this,null);
        context.checking(new Expectations() {
            {never (subscriber).ErrorOccurred(with(any(TransformerSinkErrorEvent.class)));}
            });

        // Fire the event
        transformerSink.onTransformationSinkErrorOccurred(expectedEvent);

        // Check whether expectations have are satisfied
        context.assertIsSatisfied();
    }

    /**
    * Most basic implementation of AbstractObservableTransformer, just to test whether the event system works correctly.
    */
    private class ConcreteObservableTransformerSink extends AbstractObservableTransformerSink {

        @Override
        protected void setInput(Iterable input) {

        }

        /**
        * Starts iterating over its input.
        */
        @Override
        protected void start() {

        }
    }
}


