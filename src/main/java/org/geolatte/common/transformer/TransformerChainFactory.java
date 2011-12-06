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
import java.util.Iterator;

/**
 * <p>
 * Provides a type safe API to create chains of Transformers.
 *
 * Usage Example:
 * <pre>
 * {@code
 *
 * // create a chain without a source
 * Transformer<Double, Integer> compositeTransformer =
 *               TransformerFactory.<Double, Integer>newChain().add(numberTransformer)
 *                                                                      .add(capitalizeTransformer)
 *                                                                      .last(countTransformer);
 *
 * // create a chain with a source
 * TransformerSource<Double> initialSource = new DummyTransformerSource<Double>(input);
 *       TransformerSource<Integer> transformedSource =
 *               TransformerFactory.<Double, Integer>newChain().add(initialSource)
 *                                                                      .add(numberTransformer)
 *                                                                      .add(capitalizeTransformer)
 *                                                                      .last(countTransformer);
 *
 * }
 * </pre>
 * </p>
 *
 * <p>
 * Both <tt>Transformers</tt> and <TT>Filters</tt> can be added to a chain directly; <tt>Transformations</tt> and filter <tt>Transformation</tt> can also be added. The latter two are wrapped in <tt>DefaultTransformers</tt> and <tt>DefaultFilters</tt> automatically.
 *
 * </p>
 *
 * <p>
 * <i>Creation-Date</i>: 19-Apr-2010<br>
 * <i>Creation-Time</i>:  11:16:37<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class TransformerChainFactory {

    /**
     * Creates a new transformer chain of the given type. Use the add() method of the returned object to add new transformers to the chain. Use last() after calling add() at least one time to close the chain and use it.
     *
     * @param <Source> The input type of the chain. If a TransformerSource is used, this is the type of that source.
     * @param <Target> The output type of the chain.
     * @return A helper object that allows to concatenate new transformers in a type safe way.
     */
    public static <Source, Target> TransformerConcatenatorInitial<Source, Target> newChain() {

        return new TransformerConcatenatorInitial<Source, Target>();
    }

    /**
     * Part of the type-safe transformer concatenation API. This class cooperates with TransformerConcatenator and TransformerConcatenatorWithSource to create a type-checked transformer chain. Has no other meaning to the client.
     *
     * @param <Source> The initial input type of the transformer chain under construction.
     * @param <Target> The final output type of the transformer chain under construction.
     */
    public static class TransformerConcatenatorInitial<Source, Target> {

        /**
         * Package only visible constructor.
         */
        TransformerConcatenatorInitial() {
        }

        public TransformerConcatenatorWithSource<Source, Source, Target> add(TransformerSource<Source> transformerSource)
        {
            if (transformerSource == null)
                throw new IllegalArgumentException("transformerSource cannot be null");

            return new TransformerConcatenatorWithSource<Source, Source, Target>(transformerSource, new ArrayList<Transformer<?,?>>());
        }

        public <IntermediateTarget> TransformerConcatenator<Source, IntermediateTarget, Target> add(Transformer<? super Source, IntermediateTarget> transformer)
        {
            if (transformer == null)
                throw new IllegalArgumentException("transformer cannot be null");

            ArrayList<Transformer<?,?>> chain = new ArrayList<Transformer<?,?>>();
            chain.add(transformer);
            return new TransformerConcatenator<Source, IntermediateTarget, Target>(chain);
        }

        public <IntermediateTarget> TransformerConcatenator<Source, IntermediateTarget, Target> add(Transformation<Source, IntermediateTarget> transformation)
        {
            if (transformation == null)
                throw new IllegalArgumentException("transformation cannot be null");

            return add(new DefaultTransformer<Source, IntermediateTarget>(transformation));
        }

        public TransformerConcatenator<Source, Source, Target> addFilter(Transformation<Source, Boolean> filter)
        {
            if (filter == null)
                throw new IllegalArgumentException("filter cannot be null");

            ArrayList<Transformer<?,?>> chain = new ArrayList<Transformer<?,?>>();
            chain.add(new DefaultFilter<Source>(filter));
            return new TransformerConcatenator<Source, Source, Target>(chain);
        }
    }

    /**
     * Part of the type-safe transformer concatenation API. This class cooperates with TransformerConcatenatorInitial and TransformerConcatenator to create a type-checked transformer chain. Has no other meaning to the client.
     *
     * @param <Source> The initial input type of the transformer chain under construction.
     * @param <IntermediateSource> The type of the current stage of the transformer chain under construction.
     * @param <Target> The final output type of the transformer chain under construction.
     */
    public static class TransformerConcatenatorWithSource<Source, IntermediateSource, Target> {

        private TransformerSource<? extends Source> source;
        private ArrayList<Transformer<?, ?>> chain = new ArrayList<Transformer<?, ?>>();

        TransformerConcatenatorWithSource(TransformerSource<? extends Source> source, ArrayList<Transformer<?, ?>> chain) {

            this.source = source;
            this.chain = chain;
        }

        public <IntermediateTarget> TransformerConcatenatorWithSource<Source, IntermediateTarget, Target> add(Transformer<? super IntermediateSource, IntermediateTarget> transformer)
        {
            if (transformer == null)
                throw new IllegalArgumentException("transformer cannot be null");

            chain.add(transformer);
            return new TransformerConcatenatorWithSource<Source, IntermediateTarget, Target>(source, chain);
        }

        public OpenTransformerChain<Target> last(Transformer<? super IntermediateSource, Target> transformer) {

            if (transformer == null)
                throw new IllegalArgumentException("transformer cannot be null");

            chain.add(transformer);
            OpenTransformerChainInternal<Source, Target> openChain = new OpenTransformerChainInternal<Source, Target>();
            openChain.transformerSource = source;
            openChain.chain = chain;
            return openChain;
        }

        public <IntermediateTarget> TransformerConcatenatorWithSource<Source, IntermediateTarget, Target> add(Transformation<IntermediateSource, IntermediateTarget> transformation)
        {
            if (transformation == null)
                throw new IllegalArgumentException("transformation cannot be null");

            return add(new DefaultTransformer<IntermediateSource, IntermediateTarget>(transformation));
        }

        public TransformerConcatenatorWithSource<Source, IntermediateSource, Target> addFilter(Transformation<IntermediateSource, Boolean> filter)
        {
            if (filter == null)
                throw new IllegalArgumentException("filter cannot be null");

            return add(new DefaultFilter<IntermediateSource>(filter));
        }

        public ClosedTransformerChain last(TransformerSink<Target> transformerSink) {

            if (transformerSink == null)
                throw new IllegalArgumentException("transformerSink cannot be null");

            ClosedTransformerChainInternal<Source, Target> closedChain = new ClosedTransformerChainInternal<Source, Target>();
            closedChain.transformerSource = source;
            closedChain.chain = chain;
            closedChain.transformerSink = transformerSink;
            return closedChain;
        }
    }

    /**
     * Part of the type-safe transformer concatenation API. This class cooperates with TransformerConcatenatorInitial and TransformerConcatenatorWithSource to create a type-checked transformer chain. Has no other meaning to the client.
     *
     * @param <Source> The initial input type of the transformer chain under construction.
     * @param <IntermediateSource> The type of the current stage of the transformer chain under construction.
     * @param <Target> The final output type of the transformer chain under construction.
     */
    public static class TransformerConcatenator<Source, IntermediateSource, Target> {

        private ArrayList<Transformer<?,?>> chain;

        TransformerConcatenator(ArrayList<Transformer<?,?>> chain) {

            this.chain = chain;
        }

        public <IntermediateTarget> TransformerConcatenator<Source, IntermediateTarget, Target> add(Transformer<? super IntermediateSource, IntermediateTarget> transformer)
        {
            if (transformer == null)
                throw new IllegalArgumentException("transformer cannot be null");

            chain.add(transformer);
            return new TransformerConcatenator<Source, IntermediateTarget, Target>(chain);
        }

        public TransformerChain<Source, Target> last(Transformer<? super IntermediateSource, Target> transformer) {

            if (transformer == null)
                throw new IllegalArgumentException("transformer cannot be null");

            chain.add(transformer);
            return new TransformerChain<Source, Target>(chain);
        }

        public <IntermediateTarget> TransformerConcatenator<Source, IntermediateTarget, Target> add(Transformation<IntermediateSource, IntermediateTarget> transformation)
        {
            if (transformation == null)
                throw new IllegalArgumentException("transformation cannot be null");

            return add(new DefaultTransformer<IntermediateSource, IntermediateTarget>(transformation));
        }

        public TransformerChain<Source, Target> last(Transformation<IntermediateSource, Target> transformation) {

            if (transformation == null)
                throw new IllegalArgumentException("transformation cannot be null");

            return last(new DefaultTransformer<IntermediateSource, Target>(transformation));
        }

        public TransformerConcatenator<Source, IntermediateSource, Target> addFilter(Transformation<IntermediateSource, Boolean> filter)
        {
            if (filter == null)
                throw new IllegalArgumentException("filter cannot be null");

            return add(new DefaultFilter<IntermediateSource>(filter));
        }
    }

    /**
     * Stores and executes a transformation chain.
     *
     * The list of transformations and initial source is created by the TransformerConcatenators and gauranteed to be type-safe (at compilation time at least).
     *
     * @param <Source> The type of the input parameters of the transformer.
     * @param <Target> The type of the output parameters of the transformer.
     */
    private static class TransformerChain<Source, Target> extends AbstractObservableTransformer<Source, Target> implements TransformerEventListener {

        // The transformer chain is stored as an ordered sequence of transformers. Type safety is ensured by the TransformerFactory.
        private ArrayList<Transformer<?, ?>> chain = new ArrayList<Transformer<?, ?>>();
        private boolean isConfigured = false;

        TransformerChain(ArrayList<Transformer<?, ?>> chain) {

            if (chain.size() > 0) {
                this.chain = chain;
            } else {
                ArrayList<Transformer<?, ?>> noOpChain = new ArrayList<Transformer<?, ?>>(1);
                noOpChain.add(new NoOpTransformer());
                this.chain = noOpChain;
            }
        }

        /**
         * Should only be called after the input is set
         */
        @SuppressWarnings("unchecked")
        private void configure() {

            if (isConfigured)
                return;

            // create the rest of the chain
            for (int i = 1; i < chain.size(); i++) {

                Transformer current = chain.get(i);
                current.setInput(chain.get(i-1).output());

                if (current instanceof ObservableTransformer)
                    ((ObservableTransformer)current).addTransformerEventListener(this);
            }

            isConfigured = true;
        }

        public void setInput(Iterable<? extends Source> input) {

            // input cannot be set when source is set because then this instance is returned as a TransformerSource to the client (by the concatenator).
            if (input == null)
                isConfigured = false;
            chain.get(0).setInput((Iterable)input);
            configure();
        }

        @SuppressWarnings("unchecked")
        public Iterable<? extends Target> output() {

            configure();
            return (Iterable<Target>)chain.get(chain.size()-1).output();
        }

        @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
        public void ErrorOccurred(TransformerErrorEvent transformerErrorEvent) {

            // Re-fire events of one of the child transformers
            onTransformerErrorOccurred(transformerErrorEvent);
        }
    }

    /**
     * This implementation of OpenTransformerChain has as purpose to keep track of the Source type of the chain, which is not exposed by the public OpenTransformerChain.
     * @param <Source> The Source type of the transformer chain (type of the TransformerSource)
     * @param <Target> The Target type of the transformer chain (type of the output of the last transformer, and the internal sink)
     */
    private static class OpenTransformerChainInternal<Source, Target> extends OpenTransformerChain<Target> implements TransformerEventListener, TransformerSourceEventListener {

        private TransformerSource<? extends Source> transformerSource;
        private ArrayList<Transformer<?, ?>> chain = new ArrayList<Transformer<?, ?>>();
        private IterableTransformerSink<Target> transformerSink;

        private Iterator<Target> iterator = null;

        private void configure() {

            TransformerChain<Source, Target> transformerChain = new TransformerChain<Source, Target>(chain);

            Iterable<? extends Source> output = transformerSource.output();
            if (transformerSource instanceof ObservableTransformerSource) {
                ((ObservableTransformerSource)transformerSource).addTransformerSourceEventListener(this);
            }
            transformerChain.setInput(output);

            transformerChain.configure();
            transformerChain.addTransformerEventListener(this);

            transformerSink = new IterableTransformerSink<Target>();
            transformerSink.setInput(transformerChain.output());
        }

        /**
         * Returns an iterator over a set of elements of type T.
         *
         * @return an Iterator.
         */
        public Iterator<Target> iterator() {

            if (iterator == null) {
                configure();
                transformerSink.start();
                iterator = transformerSink.iterator();
            }

            return iterator;
        }

        /**
         * Called when a transformation error occurs.
         *
         * @param event An object containing detailed information about the error.
         */
        public void ErrorOccurred(TransformerErrorEvent event) {

            // Re-fire events of one of the child transformers
            onTransformerErrorOccurred(event);
        }

        /**
         * Called when a TransformationSource encounters an error.
         *
         * @param event An object containing detailed information about the error.
         */
        public void ErrorOccurred(TransformerSourceErrorEvent event) {

            // Re-fire events of the transformer source
            onTransformerSourceErrorOccurred(event);
        }
    }

    private static class ClosedTransformerChainInternal<Source, Target> extends ClosedTransformerChain implements TransformerEventListener, TransformerSourceEventListener, TransformerSinkEventListener {

        private TransformerSource<? extends Source> transformerSource;
        private ArrayList<Transformer<?, ?>> chain = new ArrayList<Transformer<?, ?>>();
        private TransformerSink<Target> transformerSink;
        private boolean isConfigured = false;
        private boolean isStarted = false;

        private void configure() {

            if (isConfigured)
                return;

            TransformerChain<Source, Target> transformerChain = new TransformerChain<Source, Target>(chain);

            Iterable<? extends Source> output = transformerSource.output();
            if (transformerSource instanceof ObservableTransformerSource) {
                ((ObservableTransformerSource)transformerSource).addTransformerSourceEventListener(this);
            }

            transformerChain.setInput(output);
            transformerChain.addTransformerEventListener(this);

            if (transformerSink instanceof ObservableTransformerSink) {
                ((ObservableTransformerSink)transformerSink).addTransformerSinkEventListener(this);
            }
            transformerSink.setInput(transformerChain.output());

            isConfigured = true;
        }

        @Override
        public void run() {

            if (isStarted)
                return;

            isStarted = true;
            configure();
            transformerSink.start();
        }

        /**
         * Called when a transformation error occurs.
         *
         * @param event An object containing detailed information about the error.
         */
        public void ErrorOccurred(TransformerErrorEvent event) {

            // Re-fire events of the transformer source
            onTransformerErrorOccurred(event);
        }

        /**
         * Called when a TransformationSink encounters an error.
         *
         * @param event An object containing detailed information about the error.
         */
        public void ErrorOccurred(TransformerSinkErrorEvent event) {

            // Re-fire events of the transformer sink
            onTransformerSinkErrorOccurred(event);
        }

        /**
         * Called when a TransformationSource encounters an error.
         *
         * @param event An object containing detailed information about the error.
         */
        public void ErrorOccurred(TransformerSourceErrorEvent event) {

            // Re-fire events of the transformer source
            onTransformerSourceErrorOccurred(event);
        }
    }

    /**
     * Helper transformer that directly connects its input to its output (does nothing).
     */
    private static class NoOpTransformer extends Transformer {

        private Iterable input;

        @Override
        protected void setInput(Iterable input) {

            this.input = input;
        }

        @Override
        protected Iterable output() {
            return input;
        }
    }
}
