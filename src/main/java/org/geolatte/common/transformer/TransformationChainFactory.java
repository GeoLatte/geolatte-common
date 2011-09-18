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
 * Allows the client to concatenate multiple Transformations and retrieve a single Transformation object that executes these in a chain.
 * This class offers a type-safe fluent interface to concatenate transformations.
 * Usage:
 * <pre>
 * {@code
 * CompositeTransformationFactory<Integer, String> factory = new CompositeTransformationFactory<Integer, String>();
 * Transformation tf = factory.first(subtf)
 *                            .add(subtf)
 *                            .add(subtf)
 *                            .last(subtf);
 * }
 * </pre>
 * An object of this class can be reused to create different chains of the same type.
 *
 * @param <Source> The input type of the transformation.
 * @param <Target> The output type of the transformation.
 *
 * <p>
 * <i>Creation-Date</i>: 18-Mar-2010<br>
 * <i>Creation-Time</i>:  18:35:34<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class TransformationChainFactory<Source, Target> {

    private TransformationChain<Source, Target> transformationChain;

    /**
     * Adds the first sub-transformation to the chain. Subsequent transformations can be added by invoking methods on the TransformationConcatenator returned by this method.
     *
     * @param firstTransformation The first transformation in the chain.
     * @param <Intermediate> The type of the output produced by the (intermediate) transformation added.
     *
     * @return A TransformationConcatenator that allows to add additional transformations to the chain or close the chain.
     */
    public <Intermediate> TransformationConcatenator<Intermediate, Target> first(Transformation<Source, Intermediate> firstTransformation) {

        transformationChain = new TransformationChain<Source, Target>();
        transformationChain.chain.add(firstTransformation);

        return new TransformationConcatenator<Intermediate, Target>();
    }

    /**
     * Internal helper class to facilitate the fluent interface for adding sub-transformations to CompositeTransformationFactory.
     *
     * @param <IntermediateSource> The the source/target of the current stage in the transformation chain (for the first add(), this is the Source type of the CompositeTransformationFactory).
     * @param <Target> The final target of the CompositeTransformationFactory
     */
    class TransformationConcatenator<IntermediateSource, Target>
    {
        /**
         * Adds a transformation to the chain.
         *
         * @param transformation The transformation to add
         *
         * @return A TransformationConcatenator for the next transformation.
         */
        public <IntermediateTarget> TransformationConcatenator<IntermediateTarget, Target> add(Transformation<IntermediateSource, IntermediateTarget> transformation) {
            transformationChain.chain.add(transformation);
            return new TransformationConcatenator<IntermediateTarget, Target>();
        }

        /**
         * Closes the transformation chain by adding the final transformation.
         *
         * @param transformation The last transformation in the composition.
         *
         * @return The constructed composite Transformation.
         */
        @SuppressWarnings("unchecked")
        public Transformation<Source,Target> last(Transformation<IntermediateSource, Target> transformation)
        {
            transformationChain.chain.add(transformation);
            return (Transformation<Source,Target>) transformationChain;
        }
    }

    /**
     * Contains the logic to recurse a transformation chain as created by CompositeTransformationFactory.
     *
     * @param <Source> The input type of the transformation
     * @param <Target> The output type of the transformation
     */
    private class TransformationChain<Source, Target> implements Transformation<Source, Target> {

        // The transformation chain is stored as an ordered sequence of transformations. Type safety is ensured by the CompositeTransformationFactory.
        private final ArrayList<Transformation<?, ?>> chain = new ArrayList<Transformation<?, ?>>();

        /**
         * Transforms a single input to a single output by invoking the specified transformation chain.
         * @param input The given input?
         * @return The transformed input.
         */
        @SuppressWarnings("unchecked") // We know at construction time of the TransformationChain that the subsequent types are compatible
        public Target transform(Source input) throws TransformationException {

            Object currentInput = input;
            for (Transformation<?, ?> subTransformation : chain) {

                currentInput = new GenericTransformationWrapper(subTransformation).transform(currentInput);
            }

            return (Target)currentInput;
        }
    }
}
