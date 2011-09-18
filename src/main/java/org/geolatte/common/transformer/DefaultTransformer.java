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
 * Transforms a collection of input elements to a collection of output elements, using a {@link Transformation} to process each element.
 *
 * Once the input is set (setInput()), output() may be called to get an iterator to set the transformation process in motion. output() will return the same iterable/iterator as long as the input remains the same. This means that one cannot transform the input element multiple times in parallel.
 * <p/>
 * e.g.,
 * <pre>
 * {@code
 * setInput(input);
 * iter = output().iterator();
 * iter.next();                // -> object#1
 * output().iterator().next(); // -> object#2 (the iterator is the same)
 * setInput(otherInput);
 * output().iterator().next(); // -> object#a
 * iter.next();                // -> object#3 (the original iterator/transformation can still be used)
 * }
 * </pre>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @param <Source> The type of the elements that will be transformed.
 * @param <Target> The type of the transformed elements.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 18-Mar-2010<br>
 * <i>Creation-Time</i>:  16:30:04 <br>
 * </p>
 * @since SDK1.5
 */
public class DefaultTransformer<Source, Target> extends AbstractObservableTransformer<Source, Target> {

    private Transformation<? super Source, ? super Target> transformation;

    private Iterable<? extends Source> currentInput; // The currentInput Iterable set by the client
    private Iterable<Target> currentOutput;

    /**
     * Constructs a DefaultTransformer which calls the given transformation.
     *
     * @param transformation Cannot be null
     * @throws IllegalArgumentException When transformation is null.
     */
    public DefaultTransformer(Transformation<? super Source, ? super Target> transformation) {

        if (transformation == null)
            throw new IllegalArgumentException("Argument transformation cannot be null");

        this.transformation = transformation;
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
    protected Iterable<Target> output() {

        if (currentInput == null && currentOutput == null)
            return null;

        // In case a transformation is already in progress, return the existing output iterable
        if (currentOutput != null)
            return currentOutput;

        // No transformation is in progress -> create new output and forget the input
        currentOutput = new DefaultTransformerIterable<Target>(currentInput.iterator());
        currentInput = null;

        return currentOutput;
    }

    /*
     * Implementation of the actual transformer process is encapsulated in an internal Iterator.
     */

    /**
     * The Iterable implementation for DefaultTransformer. Returns the DefaultTransformerIterator.
     *
     * @param <Target> Target type that must correspond to the Target type of the corresponding transformer.
     */
    private class DefaultTransformerIterable<Target> implements Iterable<Target> {
        private Iterator<? extends Source> inputIterator;

        private DefaultTransformerIterable(Iterator<? extends Source> inputIterator) {

            this.inputIterator = inputIterator;
        }

        /**
         * Returns an iterator for the Transformer.
         *
         * @return An iterator for the Transformer.
         */
        public Iterator<Target> iterator() {

            return new DefaultTransformerIterator<Target>(inputIterator);
        }
    }

    /**
     * The Iterator implementation that delegates to DefaultTransformer to get its next value.
     *
     * @param <Target> Target type that must correspond to the Target type of the corresponding transformer.
     */
    private class DefaultTransformerIterator<Target> implements Iterator<Target> {


        private Iterator<? extends Source> inputIterator;
        private Target cachedElement = null;
        private boolean isCachedElementValid = false;

        private DefaultTransformerIterator(Iterator<? extends Source> inputIterator) {

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

                Source nextElement = null;
                try {

                    nextElement = inputIterator.next();
                    // Don't know why this cast is actually necassary
                    cachedElement = (Target) transformation.transform(nextElement);
                    isCachedElementValid = true;
                    return true;
                }
                catch (TransformationException e) {

                    onTransformerErrorOccurred(nextElement, e);
                }
            }

            return false;
        }

        /**
         * Returns the next transformed element.
         *
         * @return The next transformed element.
         */
        public Target next() {

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
