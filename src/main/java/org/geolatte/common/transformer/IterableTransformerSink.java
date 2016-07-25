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

import java.util.Iterator;

/**
 * This sink offers its collected input directly to a client through the {@code Iterable<T>} interface. The input objects are
 * not cached in an internal list first, but are available immediately.
 * Use this sink if you want to process the output of a transformer operation manually (not encapsulated in a dedicated sink).
 *
 * @param <T> The type of data processed by this sink.
 *
 * <p>
 * <i>Creation-Date</i>: 13-Apr-2010<br>
 * <i>Creation-Time</i>:  12:32:40 <br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class IterableTransformerSink<T> extends TransformerSink<T> implements Iterable<T> {

    private Iterable<? extends T> input;
    private Iterator<T> outputIterator;

    /**
     * Sets the input for this sink.
     * @param input The data source.
     */
    public void setInput(Iterable<? extends T> input) {

        this.input = input;
        outputIterator = null; // invalidate output if new input is set
    }

    /**
     * Starts iterating over its input.
     */
    public void start() {
        // nothing to do
    }

    /**
     * Provides immdediate access to the input elements collected by this sink. Each next() call will pull an new element from the input as long as there are elements.
     *
     * @return A read-only iterator over the input elements received by this sink.
     */
    public Iterator<T> iterator() {

        if (input == null)
            return null;

        if (outputIterator == null)
            outputIterator = new SinkIterator<T>(input.iterator());

        return outputIterator;
    }

    /**
     * This iterator wraps the input and provides its elements back to the client.
     * @param <T>
     */
    private class SinkIterator<T> implements Iterator<T> {

        private Iterator<? extends T> input;

        SinkIterator(Iterator<? extends T> input) {

            this.input = input;
        }

        /**
         * Determines whether there are more input elements available.
         *
         * @return true is there are more elements, false otherwise.
         */
        public boolean hasNext() {

            return input.hasNext();
        }

        /**
         * Gets the next input element.
         *
         * @return The next input element.
         */
        public T next() {

            return input.next();
        }

        /**
         * Not supported
         */
        public void remove() {
            
            throw new UnsupportedOperationException();
        }
    }
}
