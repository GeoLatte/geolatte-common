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
import java.util.NoSuchElementException;

/**
 * <p>
 * Performs a filtering operation on its source elements. When the given filter {@link Transformation} (<_, Boolean>) evaluates to false, the source element is not passed, when it evaluates to true, it is passed unmodified to the output.
 *
 * <pre>
 *  input | tf    | output
 * --------------------
 *   obj1 | false |  -
 *   obj2 | true  | obj2
 *   obj3 | false | -
 *   obj4 | true  | obj4
 *   obj5 |*excep*| -     (+ error event)
 * </pre>
 *
 * </p>
 * <p>
 * <i>Creation-Date</i>: 02-Jun-2010<br>
 * <i>Creation-Time</i>:  17:08:21<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class DefaultFilter<Source> extends AbstractObservableTransformer<Source, Source> {

    private Transformation<Source, Boolean> filter;

    private Iterable<? extends Source> currentInput; // The currentInput Iterable set by the client
    private Iterable<Source> currentOutput;

    /**
     * Constructs a DefaultFilterTransformer which calls the given transformation.
     *
     * @param transformation Cannot be null
     * @throws IllegalArgumentException When transformation is null.
     */
    public DefaultFilter(Transformation<Source, Boolean> transformation) {

        if (transformation == null)
            throw new IllegalArgumentException("Argument transformation cannot be null");

        this.filter = transformation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setInput(Iterable<? extends Source> input) {

        currentInput = input;
        currentOutput = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Iterable<Source> output() {

        if (currentInput == null && currentOutput == null)
            return null;

        // In case a transformation is already in progress, return the existing output iterable
        if (currentOutput != null)
            return currentOutput;

        // No transformation is in progress -> create new output and forget the input
        currentOutput = new FilterIterable<Source>(currentInput.iterator());
        currentInput = null;

        return currentOutput;
    }

    /*
     * Implementation of the actual transformer process is encapsulated in an internal Iterator.
     */

    /**
     * The Iterable implementation for DefaultFilter. Returns the FilterIterator.
     *
     * @param <Source> Source type that must correspond to the Source type of the corresponding transformer.
     */
    private class FilterIterable<Source> implements Iterable<Source> {
        private Iterator<? extends Source> inputIterator;

        private FilterIterable(Iterator<? extends Source> inputIterator) {

            this.inputIterator = inputIterator;
        }

        /**
         * Returns an iterator for the Transformer.
         *
         * @return An iterator for the Transformer.
         */
        public Iterator<Source> iterator() {

            return new FilterIterator<Source>(inputIterator);
        }
    }

    /**
     * The Iterator implementation that delegates to DefaultFilter to get its next value.
     *
     * @param <T> Target type that must correspond to the Source type of the corresponding transformer.
     */
    private class FilterIterator<T> implements Iterator<T> {


        private Iterator<? extends T> inputIterator;
        private T cachedElement = null;
        private boolean isCachedElementValid = false;

        private FilterIterator(Iterator<? extends T> inputIterator) {

            this.inputIterator = inputIterator;
        }

        /**
         * Returns <tt>true</tt> if the transformation can produce more elements. (In other
         * words, returns <tt>true</tt> if <tt>next</tt> would return an element
         * rather than throwing an exception.)
         *
         * @return <tt>true</tt> if the transformation can produce more elements.
         */
        @SuppressWarnings("unchecked")
        public boolean hasNext() {

            if (isCachedElementValid)
                return true;

            while (inputIterator.hasNext()) {

                T nextElement = null;
                try {

                    nextElement = inputIterator.next();
                    // Don't know why this cast is actually necassary
                    if (filter.transform((Source)nextElement))  { // passed through filter
                        cachedElement = nextElement;
                        isCachedElementValid = true;
                    }
                    else { // Not passed through filter -> try the next element
                        cachedElement = null;
                        isCachedElementValid = false;
                        continue;
                    }
                    return true;
                }
                catch (TransformationException e) {

                    onTransformerErrorOccurred((Source)nextElement, e);
                }
            }

            return false;
        }

        /**
         * Returns the next transformed element.
         *
         * @return The next transformed element.
         */
        public T next() {

            if (isCachedElementValid || hasNext())
            {
                isCachedElementValid = false;
                return cachedElement;
            }

            throw new NoSuchElementException();
        }

        /**
         * Not supported.
         */
        public void remove() {

            throw new UnsupportedOperationException();
        }
    }
}
