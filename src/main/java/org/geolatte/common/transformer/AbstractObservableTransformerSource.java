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

import java.util.ArrayList;

/**
 * <p>
 * Implements the event handling part of {@link ObservableTransformerSource}.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 04-May-2010<br>
 * <i>Creation-Time</i>:  11:36:26<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public abstract class AbstractObservableTransformerSource<T> extends ObservableTransformerSource<T> {

    private ArrayList<TransformerSourceEventListener> transformerSourceEventListeners = new ArrayList<TransformerSourceEventListener>();

    /**
     * Adds a listener for TransformerSource events. Null listeners are ignored.
     * @param listener The listener to add
     */
    public void addTransformerSourceEventListener(TransformerSourceEventListener listener) {

        if (listener != null)
            transformerSourceEventListeners.add(listener);
    }

    /**
     * Removes the given listener.
     * @param listener The listener to remove.
     */
    public void removeTransformerSourceEventListener(TransformerSourceEventListener listener) {

        transformerSourceEventListeners.remove(listener);
    }

    /**
     * Called when an exception occurred. Fires the ErrorOccured event.
     * @param isTerminating Indicates whether this TransformerSource can still provide further values (false) or will terminate completely (true).
     * @param exception The exception that was thrown by the Transformation.
     */
    protected void onSourceErrorOccurred(boolean isTerminating, Exception exception) {

        for (TransformerSourceEventListener listener : transformerSourceEventListeners) {

            fireErrorEvent(listener, new TransformerSourceErrorEvent(this, exception, isTerminating));
        }
    }

    /**
     * (Re)-fires a preconstructed event.
     * @param event The event to fire
     */
    protected void onSourceErrorOccurred(TransformerSourceErrorEvent event) {

        for (TransformerSourceEventListener listener : transformerSourceEventListeners)
            fireErrorEvent(listener, event);
    }

    /**
     * Fires the given event on the given listener.
     * @param listener The listener to fire the event to.
     * @param event The event to fire.
     */
    private void fireErrorEvent(TransformerSourceEventListener listener, TransformerSourceErrorEvent event) {

        try {
            listener.ErrorOccurred(event);
        }
        catch (RuntimeException e) {
            // Log this somehow
            System.err.println("Exception thrown while trying to invoke event listener, removing bed behaved listener.");
            e.printStackTrace();
            removeTransformerSourceEventListener(listener);
        }
    }
}
