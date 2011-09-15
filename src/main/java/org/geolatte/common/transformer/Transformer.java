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

/**
 * A Transformer is basically both a (Transformer)Sink for input data and a (Transformer)Source of transformed output data.
 * A Transformer executes some sort of transformation over a collection of input elements to yield a collection of output elements.
 * Implementers can use the Transformation interface to recurse the transformations of individual elements.
 *
 * Remark: A Transformer can be stateful and should never be reused!
 *
 * @param <Source> The type of the elements that will be transformed.
 * @param <Target> The type of the transformed elements.
 *
 * <p>
 * <i>Creation-Date</i>: 18-Mar-2010<br>
 * <i>Creation-Time</i>:  18:22:29<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public abstract class Transformer<Source, Target> {

    /**
     * Sets the data input source for this sink.
     *
     * This method should never be called directly by clients when the Transformer is intended to be used in a transformer chain.
     *
     * @param input The data source.
     */
    protected abstract void setInput(Iterable<? extends Source> input);

    /**
     * Gets the output for the transformation given the input set by setInput().
     * On each "next()", the transformer pulls an element from the currentInput and pulls it through the transformation.
     * Mind that the remove() method on the iterator has no effect.
     *
     * As long as the input does not change, output() returns the same Iterable. If it has changed, output() returns an Iterable for the new input.
     *
     * This method should never be called directly by clients when the TransformerSource is intended to be used in a transformer chain.
     *
     * @return An Iterable which contains the result of the transformation (constructed on the fly) or null if no input has been set.
     */
    protected abstract Iterable<? extends Target> output();
}
