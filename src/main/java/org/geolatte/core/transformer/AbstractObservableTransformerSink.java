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

import java.util.ArrayList;

/**
 * <p>
 * Implements the event handling part of {@link ObservableTransformerSink}.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 05-May-2010<br>
 * <i>Creation-Time</i>:  19:30:30<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public abstract class AbstractObservableTransformerSink<T> extends ObservableTransformerSink<T> {

    private ArrayList<TransformerSinkEventListener> transformerSinkEventListeners = new ArrayList<TransformerSinkEventListener>();

    /**
     * Adds a listener for TransformerSink events. Null listeners are ignored.
     * @param listener The listener to add
     */
    @Override
    public void addTransformerSinkEventListener(TransformerSinkEventListener listener) {

        if (listener != null)
            transformerSinkEventListeners.add(listener);
    }

    /**
     * Removes the given listener.
     * @param listener The listener to remove.
     */
    @Override
    public void removeTransformerSinkEventListener(TransformerSinkEventListener listener) {

        transformerSinkEventListeners.remove(listener);
    }

    /**
     * Called when an exception occurred. Fires the ErrorOccured event.
     * @param exception The exception that was thrown by the TransformationSink.
     */
    protected void onTransformationSinkErrorOccurred(Exception exception) {

        for (TransformerSinkEventListener listener : transformerSinkEventListeners)
            fireErrorEvent(listener, new TransformerSinkErrorEvent(this, exception));
    }


    /**
     * (Re)-fires a preconstructed event.
     * @param event The event to fire
     */
    protected void onTransformationSinkErrorOccurred(TransformerSinkErrorEvent event) {

        for (TransformerSinkEventListener listener : transformerSinkEventListeners)
            fireErrorEvent(listener, event);
    }

    /**
     * Fires the given event on the given listener.
     * @param listener The listener to fire the event to.
     * @param event The event to fire.
     */
    private void fireErrorEvent(TransformerSinkEventListener listener, TransformerSinkErrorEvent event) {

        try {
            listener.ErrorOccurred(event);
        }
        catch (RuntimeException e) {
            // Log this somehow
            System.err.println("Exception thrown while trying to invoke event listener, removing bed behaved listener.");
            e.printStackTrace();
            removeTransformerSinkEventListener(listener);
        }
    }
}
