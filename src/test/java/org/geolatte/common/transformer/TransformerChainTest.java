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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 21-Jun-2010<br>
 * <i>Creation-Time</i>:  17:32:17<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class TransformerChainTest {

    Mockery context = new JUnit4Mockery();

    private TransformerChain testSubject;

    @Before
    public void setUp() throws Exception {

        testSubject = new TransformerChainBasicImpl();
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Tests whether Transformer error events are fired correctly.
     * @throws Exception
     */
    @Test
    public void testTransformerEventListener() throws Exception {

        // mock a subscriber
        final TransformerEventListener subscriber = context.mock(TransformerEventListener.class);

        // subscribe to the event
        testSubject.addTransformerEventListener(subscriber);

        // Expect exactly one invocation
        final TransformerErrorEvent expectedEvent = new TransformerErrorEvent(this,null);
        context.checking(new Expectations() {
            {one (subscriber).ErrorOccurred(expectedEvent);}
        });

        // fire the event
        testSubject.onTransformerErrorOccurred(expectedEvent);

        // unsubscribe and fire again -- should not be fired
        testSubject.removeTransformerEventListener(subscriber);
        testSubject.onTransformerErrorOccurred(expectedEvent);

        context.assertIsSatisfied();
    }

    /**
     * Tests whether Transformer source error events are fired correctly.
     * @throws Exception
     */
    @Test
    public void testSourceEventListener() throws Exception {

        // mock a subscriber
        final TransformerSourceEventListener subscriber = context.mock(TransformerSourceEventListener.class);

        // subscribe to the event
        testSubject.addSourceEventListener(subscriber);

        // Expect exactly one invocation
        final TransformerSourceErrorEvent expectedEvent = new TransformerSourceErrorEvent(this,null);
        context.checking(new Expectations() {
            {one (subscriber).ErrorOccurred(expectedEvent);}
        });

        // fire the event
        testSubject.onTransformerSourceErrorOccurred(expectedEvent);

        // unsubscribe and fire again -- should not be fired
        testSubject.removeSourceEventListener(subscriber);
        testSubject.onTransformerSourceErrorOccurred(expectedEvent);

        context.assertIsSatisfied();
    }

    /**
     * Tests whether Transformer sink error events are fired correctly.
     * @throws Exception
     */
    @Test
    public void testSinkEventListener() throws Exception {

        // mock a subscriber
        final TransformerSinkEventListener subscriber = context.mock(TransformerSinkEventListener.class);

        // subscribe to the event
        testSubject.addSinkEventListener(subscriber);

        // Expect exactly one invocation
        final TransformerSinkErrorEvent expectedEvent = new TransformerSinkErrorEvent(this,null);
        context.checking(new Expectations() {
            {one (subscriber).ErrorOccurred(expectedEvent);}
        });

        // fire the event
        testSubject.onTransformerSinkErrorOccurred(expectedEvent);

        // unsubscribe and fire again -- should not be fired
        testSubject.removeSinkEventListener(subscriber);
        testSubject.onTransformerSinkErrorOccurred(expectedEvent);

        context.assertIsSatisfied();
    }

    /**
     * Implementation of TransformerChain that exposed the on*Event*Occurred methods publicly so they can be tested.
     */
    private class TransformerChainBasicImpl extends TransformerChain {

        public void onTransformerErrorOccurred(TransformerErrorEvent nestedEvent) {

            super.onTransformerErrorOccurred(nestedEvent);
        }

        public void onTransformerSourceErrorOccurred(TransformerSourceErrorEvent nestedEvent) {

            super.onTransformerSourceErrorOccurred(nestedEvent);
        }

        public void onTransformerSinkErrorOccurred(TransformerSinkErrorEvent nestedEvent) {

            super.onTransformerSinkErrorOccurred(nestedEvent);
        }
    }
}
