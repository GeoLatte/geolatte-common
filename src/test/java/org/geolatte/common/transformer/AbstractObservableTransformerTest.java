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

package org.geolatte.common.transformer;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * Tests the event implementation of AbstractObservableTransformer.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 06-May-2010<br>
 * <i>Creation-Time</i>:  13:53:45<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */

public class AbstractObservableTransformerTest {

    Mockery context = new JUnit4Mockery();

    AbstractObservableTransformer transformer;

    @Before
    public void setUp() throws Exception {

        transformer = new ConcreteObservableTransformer();
    }

    /**
     * Checks whether the TransformerErrorEvent is fired correctly once.
     */
    @Test
    public void test_TransformerEventListenerSubscribeAndFire() {

        final TransformerEventListener subscriber = context.mock(TransformerEventListener.class);
        transformer.addTransformerEventListener(subscriber);

        // Expectations
        final TransformerErrorEvent expectedEvent = new TransformerErrorEvent(this,null);
        context.checking(new Expectations() {
            {oneOf (subscriber).ErrorOccurred(expectedEvent);}
        });

        // Fire the event
        transformer.onTransformerErrorOccurred(expectedEvent);

        // Check whether expectations have are satisfied
        context.assertIsSatisfied();
    }

    /**
     * Checks whether unsubscribing the TransformerErrorEvent works correctly.
     */
    @Test
    public void test_TransformerEventListenerUnsubscribe() {

        final TransformerEventListener subscriber = context.mock(TransformerEventListener.class);

        // subscribe and unscubscribe
        transformer.addTransformerEventListener(subscriber);
        transformer.removeTransformerEventListener(subscriber);

        // Expectations
        final TransformerErrorEvent expectedEvent = new TransformerErrorEvent(this,null);
        context.checking(new Expectations() {
            {never (subscriber).ErrorOccurred(with(any(TransformerErrorEvent.class)));}
        });

        // Fire the event
        transformer.onTransformerErrorOccurred(expectedEvent);

        // Check whether expectations have are satisfied
        context.assertIsSatisfied();
    }

    /**
     * Most basic implementation of AbstractObservableTransformer, just to test whether the event system works correctly.
     */
    private class ConcreteObservableTransformer extends AbstractObservableTransformer {

        @Override
        protected void setInput(Iterable input) {
        }

        @Override
        protected Iterable output() {
            return null;
        }
    }
}
