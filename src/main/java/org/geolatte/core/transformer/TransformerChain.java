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
 * Base class for transformer chains. A transformer chain can include a {@link TransformerSource}, a {@link Transformer} and {@link TransformerSink}, all of them optional.
 * This abstract class implements nothing more than the basic event mechanism for firing error events that might originate from a source, sink of a transformer.
 * </p>
 * <i>Creation-Date</i>: 04-May-2010<br>
 * <i>Creation-Time</i>:  14:54:50<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public abstract class TransformerChain<Source, Target> {

    private ArrayList<TransformerEventListener> transformerEventListeners = new ArrayList<TransformerEventListener>();
    private ArrayList<TransformerSourceEventListener> transformerSourceEventListeners = new ArrayList<TransformerSourceEventListener>();
    private ArrayList<TransformerSinkEventListener> transformerSinkEventListeners = new ArrayList<TransformerSinkEventListener>();

    /**
     * Adds a listener for transformer events. Null listeners are ignored.
     * @param listener The listener to add
     */
    public void addTransformerEventListener(TransformerEventListener listener) {

        if (listener != null)
            transformerEventListeners.add(listener);
    }

    /**
     * Removes the given listener.
     * @param listener The listener to remove.
     */
    public void removeTransformerEventListener(TransformerEventListener listener) {

        transformerEventListeners.remove(listener);
    }

    /**
     * (Re)-fires the given event
     * @param event A pre-constructed event.
     */
    protected void onTransformerErrorOccurred(TransformerErrorEvent event) {

        for (TransformerEventListener listener : transformerEventListeners)
            fireTransformerErrorEvent(listener, event);
    }

    /**
     * Helper method that fires the given event on the given listener.
     * @param listener The listener to fire the event on.
     * @param event The event to fire.
     */
    private void fireTransformerErrorEvent(TransformerEventListener listener, TransformerErrorEvent event) {

        try {
            listener.ErrorOccurred(event);
        }
        catch (RuntimeException e) {
            // Log this somehow
            System.err.println("Exception thrown while trying to invoke event listener, removing bed behaved listener.");
            e.printStackTrace();
            removeTransformerEventListener(listener);
        }
    }

    public void addSourceEventListener(TransformerSourceEventListener listener) {

        if (listener != null)
            transformerSourceEventListeners.add(listener);
    }

    public void removeSourceEventListener(TransformerSourceEventListener listener) {

        transformerSourceEventListeners.remove(listener);
    }

    /**
     * (Re)-fires the given event
     * @param event The event to fire.
     */
    protected void onTransformerSourceErrorOccurred(TransformerSourceErrorEvent event) {

        for (TransformerSourceEventListener listener : transformerSourceEventListeners)
            fireTransformerSourceErrorEvent(listener, event);
    }

    /**
     * Helper method that fires the given event on the given listener.
     * @param listener The listener to fire the event on.
     * @param event The event to fire.
     */
    private void fireTransformerSourceErrorEvent(TransformerSourceEventListener listener, TransformerSourceErrorEvent event) {

        try {
            listener.ErrorOccurred(event);
        }
        catch (RuntimeException e) {
            // Log this somehow
            System.err.println("Exception thrown while trying to invoke event listener, removing bed behaved listener.");
            e.printStackTrace();
            removeSourceEventListener(listener);
        }
    }

    public void addSinkEventListener(TransformerSinkEventListener listener) {

        if (listener != null)
            transformerSinkEventListeners.add(listener);
    }

    public void removeSinkEventListener(TransformerSinkEventListener listener) {

        transformerSinkEventListeners.remove(listener);
    }

    /**
     * (Re)-fires the given event
     * @param event The event to fire.
     */
    protected void onTransformerSinkErrorOccurred(TransformerSinkErrorEvent event) {

        for (TransformerSinkEventListener listener : transformerSinkEventListeners)
            fireTransformerSinkErrorEvent(listener, event);
    }

    private void fireTransformerSinkErrorEvent(TransformerSinkEventListener listener, TransformerSinkErrorEvent event) {

        try {
            listener.ErrorOccurred(event);
        }
        catch (RuntimeException e) {
            // Log this somehow
            System.err.println("Exception thrown while trying to invoke event listener, removing bad behaved listener.");
            e.printStackTrace();
            removeSinkEventListener(listener);
        }
    }
}
