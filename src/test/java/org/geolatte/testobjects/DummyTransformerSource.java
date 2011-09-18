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

package org.geolatte.testobjects;

import org.geolatte.common.transformer.AbstractObservableTransformerSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 19-Apr-2010<br>
 * <i>Creation-Time</i>:  15:13:52<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class DummyTransformerSource<T> extends AbstractObservableTransformerSource<T> {

    private DummyIterator<T> dataSource;

    private ArrayList<Integer> throwOnInvocations;
    private boolean terminate = false;

    /**
     * Creates a transformer source that uses the given list as a datasource to server on its output.
     * Furthermore, this datasource will fire an error event (possibly terminating) on the iteration(s) specified.
     *
     * @param dataSource The data to serve through the output() method.
     * @param terminate Indicates whether the source should completely shutdown after the first error (if true, only the first argument of throwOnInvocations will be used)
     * @param throwOnInvocations The iterations on which an exception is thrown.
     */
    public DummyTransformerSource(ArrayList<T> dataSource, boolean terminate, Integer ... throwOnInvocations) {

        this.throwOnInvocations = new ArrayList<Integer>(Arrays.asList(throwOnInvocations));
        this.dataSource = new DummyIterator<T>(dataSource);
    }

    /**
     * Creates a transformer source that uses the given list as a datasource to server on its output.
     * Furthermore, this datasource will fire an error event on the iteration(s) specified.
     *
     * @param dataSource The data to serve through the output() method.
     * @param throwOnInvocations The iterations on which an exception is thrown.
     */
    public DummyTransformerSource(ArrayList<T> dataSource, Integer ... throwOnInvocations) {
        this(dataSource, false, throwOnInvocations);
    }

    /**
     * Creates a transformer source that uses the given list as a datasource to server on its output
     *
     * @param dataSource The data to serve through the output() method.
     */
    public DummyTransformerSource(ArrayList<T> dataSource) {
        this(dataSource, false);
    }

    public Iterable<T> output() {

        return new Iterable<T>() {
            public Iterator<T> iterator() {
                return dataSource;
            }
        };
    }

    private class DummyIterator<T> implements Iterator<T> {

        private ArrayList<T> dataSource;
        private int invocationCounter = 0;
        private T nextElement;
        private boolean isTerminated = false;

        private DummyIterator(ArrayList<T> dataSource) {

            this.dataSource = dataSource;
        }

        /**
         * Returns <tt>true</tt> if the iteration has more elements. (In other
         * words, returns <tt>true</tt> if <tt>next</tt> would return an element
         * rather than throwing an exception.)
         *
         * @return <tt>true</tt> if the iterator has more elements.
         */
        public boolean hasNext() {

            if (isTerminated)
                return false;

            if (invocationCounter >= dataSource.size())
                return false;

            nextElement = dataSource.get(invocationCounter);

            invocationCounter++;
            if (DummyTransformerSource.this.throwOnInvocations.contains(invocationCounter)) {
                onSourceErrorOccurred(DummyTransformerSource.this.terminate, new Exception("Dummy exception on " + invocationCounter + "th invocation."));
                if (DummyTransformerSource.this.terminate) {
                    isTerminated = true;
                    return false;
                }

                return hasNext(); // check whether the following element is available
            }

            return true;

        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration.
         * @throws java.util.NoSuchElementException
         *          iteration has no more elements.
         */
        public T next() {

            if (isTerminated)
                throw new NoSuchElementException();

            if (nextElement != null || hasNext())
            {
                return nextElement;
            }

            throw new NoSuchElementException();

        }

        /**
         * Removes from the underlying collection the last element returned by the
         * iterator (optional operation).  This method can be called only once per
         * call to <tt>next</tt>.  The behavior of an iterator is unspecified if
         * the underlying collection is modified while the iteration is in
         * progress in any way other than by calling this method.
         *
         * @throws UnsupportedOperationException if the <tt>remove</tt>
         *                                       operation is not supported by this Iterator.
         * @throws IllegalStateException         if the <tt>next</tt> method has not
         *                                       yet been called, or the <tt>remove</tt> method has already
         *                                       been called after the last call to the <tt>next</tt>
         *                                       method.
         */
        public void remove() {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
