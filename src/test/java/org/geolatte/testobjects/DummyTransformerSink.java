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

import org.geolatte.common.transformer.AbstractObservableTransformerSink;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 05-May-2010<br>
 * <i>Creation-Time</i>:  19:29:39<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class DummyTransformerSink<T> extends AbstractObservableTransformerSink<T> {

    private ArrayList<Integer> throwOnInvocations;
    private int currentInvocation = 0;
    private Iterable<? extends T> inputIterable;
    private ArrayList<T> results = new ArrayList<T>();

    /**
     * Creates a transformer sink that stores its received input in the given resultStore. This resultStore will be fully populated when the sink has finished processing its input (this is usually after the start() method of a containing ClosedTransformationChain returns).
     * Furthermore, this sink will fire an error event on the iteration(s) specified.
     *
     * @param resultStore A list where consumed input will be stored.
     * @param throwOnInvocations The iterations on which an exception is thrown.
     */
    public DummyTransformerSink(ArrayList<T> resultStore, Integer ... throwOnInvocations) {

        this.results = resultStore;
        this.throwOnInvocations = new ArrayList<Integer>(Arrays.asList(throwOnInvocations));
    }

    /**
     * Sets the data input source for this sink.
     *
     * @param input The data source.
     */
    @Override
    protected void setInput(Iterable<? extends T> input) {

        if (inputIterable != null)
            throw new RuntimeException("Should not set input more than once.");

        inputIterable = input;
    }

    @Override
    protected void start() {

        for (T element : inputIterable) {

            currentInvocation++;

            if (throwOnInvocations.contains(currentInvocation))
                onTransformationSinkErrorOccurred(new Exception("Generated exception for testing purposes"));
            else
                results.add(element);
        }
    }
}
