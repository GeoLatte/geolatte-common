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

import org.geolatte.common.transformer.testutil.LoggingTransformerEventListener;
import org.geolatte.common.transformer.testutil.LoggingTransformerSourceEventListener;
import org.geolatte.testobjects.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>
 * Tests the TransformerChainFactory.
 * <p>
 * <i>Creation-Date</i>: 19-Apr-2010<br>
 * <i>Creation-Time</i>:  11:17:20<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class TransformerChainFactoryTest {

    private Transformation<Double, String> numberTransformation;
    private Transformation<String, String> capitalizeTransformation;
    private Transformation<String, Integer> countTransformation;

    private Transformer<Double, String> numberTransformer;
    private Transformer<String, String> capitalizeTransformer;
    private Transformer<String, Integer> countTransformer;

    @Before
    public void setUp() throws Exception {

        numberTransformation = new CharacterNumberTransformation();
        capitalizeTransformation = new CapitalizeTransformation();
        countTransformation = new CharacterCountTransformation();

        numberTransformer = new DefaultTransformer<Double, String>(numberTransformation);
        capitalizeTransformer = new DefaultTransformer<String, String>(capitalizeTransformation);
        countTransformer = new DefaultTransformer<String, Integer>(countTransformation);
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Verifies whether a transformer chain cannot be constructed with 'null' transformers.
     */
    @Test
    public void test_CompositionErrors() {

        // Try to add a null transformer at several places in the chain

        try {
            TransformerChainFactory.<Double, Integer>newChain().add((Transformer<Double, Object>)null);
            Assert.fail();
        }
        catch (IllegalArgumentException e) {
            Assert.assertTrue(true); // this should happen
        }

        try {
            TransformerChainFactory.<Double, Integer>newChain().add(numberTransformer)
                                                                   .add((Transformer<String,String>)null);
            Assert.fail();
        }
        catch (IllegalArgumentException e) {
            Assert.assertTrue(true); // this should happen
        }

        try {
            TransformerChainFactory.<Double, Integer>newChain().add(numberTransformer)
                                                               .last((Transformer<String, Integer>)null);
            Assert.fail();
        }
        catch (IllegalArgumentException e) {
            Assert.assertTrue(true); // this should happen
        }

        try {
            TransformerChainFactory.<Double, Integer>newChain().add(numberTransformer)
                                                               .last((Transformation<String, Integer>)null);
            Assert.fail();
        }
        catch (IllegalArgumentException e) {
            Assert.assertTrue(true); // this should happen
        }


        try {
            TransformerChainFactory.<Double, Integer>newChain().add((TransformerSource<Double>)null);
            Assert.fail();
        }
        catch (IllegalArgumentException e) {
            Assert.assertTrue(true); // this should happen
        }

        try {
            TransformerChainFactory.<Double, Integer>newChain().add(new DummyTransformerSource<Double>(new ArrayList<Double>(Arrays.asList(2.2))))
                                                 .add((Transformer<Double, Integer>)null);
            Assert.fail();
        }
        catch (IllegalArgumentException e) {
            Assert.assertTrue(true); // this should happen
        }

        try {
            TransformerChainFactory.<Double, Integer>newChain().add(new DummyTransformerSource<Double>(new ArrayList<Double>(Arrays.asList(2.2))))
                                                 .add((Transformation<Double, Integer>)null);
            Assert.fail();
        }
        catch (IllegalArgumentException e) {
            Assert.assertTrue(true); // this should happen
        }

        try {
            TransformerChainFactory.<Double, Integer>newChain().add(new DummyTransformerSource<Double>(new ArrayList<Double>(Arrays.asList(2.2))))
                                                                   .last((Transformer<? super Double,Integer>) null);
            Assert.fail();
        }
        catch (IllegalArgumentException e) {
            Assert.assertTrue(true); // this should happen
        }

        try {
            TransformerChainFactory.<Double, Integer>newChain().add(new DummyTransformerSource<Double>(new ArrayList<Double>(Arrays.asList(2.2))))
                                                                   .last((TransformerSink<Integer>) null);
            Assert.fail();
        }
        catch (IllegalArgumentException e) {
            Assert.assertTrue(true); // this should happen
        }
    }

    /**
     * Checks whether a transformer chain can be constructed and executed without a source.
     */
    @Test
    public void test_Composition()
    {
        ArrayList<Double> input = new ArrayList<Double>(Arrays.asList(88.0, 754.8, 0.0, 123456.2));
        ArrayList<Integer> expectedResults = new ArrayList<Integer>(Arrays.asList(4, 5, 3, 8));

        Transformer<Double, Integer> compositeTransformer =
                TransformerChainFactory.<Double, Integer>newChain().add(numberTransformer)
                                                     .add(capitalizeTransformer)
                                                     .last(countTransformer);

        compositeTransformer.setInput(input);

        int count = 0;
        for (Integer i : compositeTransformer.output()) {

            Assert.assertEquals(expectedResults.get(count), i);
            count++;
        }
    }

    /**
     * Checks whether a transformer chain can be constructed using Transformations (who should be wrapped into Transformers automatically).
     */
    @Test
    public void test_TransformationComposition()
    {
        ArrayList<Double> input = new ArrayList<Double>(Arrays.asList(88.0, 754.8, 0.0, 123456.2));
        ArrayList<Integer> expectedResults = new ArrayList<Integer>(Arrays.asList(4, 5, 3, 8));

        Transformer<Double, Integer> compositeTransformer =
                TransformerChainFactory.<Double, Integer>newChain().add(numberTransformation)
                                                     .add(capitalizeTransformation)
                                                     .last(countTransformation);

        compositeTransformer.setInput(input);

        int count = 0;
        for (Integer i : compositeTransformer.output()) {

            Assert.assertEquals(expectedResults.get(count), i);
            count++;
        }
    }

    /**
     * Checks whether a transformer chain can be constructed and executed without a source and with filters.
     */
    @Test
    public void test_CompositionWithFilters()
    {                // invocation count (as seen by the first stage)    1      2    3         4      5       6
                    // invocation count (as seen by the second stage)    1           2                3       4
        ArrayList<Double> input = new ArrayList<Double>(Arrays.asList(88.0, 754.8, 0.0, 123456.2, 321.3, 3210.5));
        Integer[] filteredOutFirstStage = new Integer[] {2, 4};
        Integer[] filteredOutSecondStage = new Integer[] {4};
        ArrayList<Integer> expectedResults = new ArrayList<Integer>(Arrays.asList(4, 3, 5));

        ProgrammableFilter<Double> firstFilterTransformation = new ProgrammableFilter<Double>(filteredOutFirstStage);
        DefaultFilter<Double> firstFilter = new DefaultFilter<Double>(firstFilterTransformation);

        ProgrammableFilter<String> secondFilterTransformation = new ProgrammableFilter<String>(filteredOutSecondStage);
        DefaultFilter<String> secondFilter = new DefaultFilter<String>(secondFilterTransformation);

        Transformer<Double, Integer> compositeTransformer =
                TransformerChainFactory.<Double, Integer>newChain().add(firstFilter)
                                                                   .add(numberTransformer)
                                                                   .add(secondFilter)
                                                                   .add(capitalizeTransformer)
                                                                   .last(countTransformer);

        compositeTransformer.setInput(input);

        int count = 0;
        for (Integer i : compositeTransformer.output()) {

            Assert.assertEquals(expectedResults.get(count), i);
            count++;
        }
    }


    /**
     * Checks whether a transformer chain can be constructed using Transformations and Filters (who should be wrapped into Transformers automatically).
     */
    @Test
    public void test_TransformationCompositionWithFilters()
    {
        // invocation count (as seen by the first stage)            1      2    3         4      5       6
        // invocation count (as seen by the second stage)           1           2                3       4
        ArrayList<Double> input = new ArrayList<Double>(Arrays.asList(88.0, 754.8, 0.0, 123456.2, 321.3, 3210.5));
        Integer[] filteredOutFirstStage = new Integer[] {2, 4};
        Integer[] filteredOutSecondStage = new Integer[] {4};
        ArrayList<Integer> expectedResults = new ArrayList<Integer>(Arrays.asList(4, 3, 5));

        ProgrammableFilter<Double> firstFilterTransformation = new ProgrammableFilter<Double>(filteredOutFirstStage);
        ProgrammableFilter<String> secondFilterTransformation = new ProgrammableFilter<String>(filteredOutSecondStage);

        Transformer<Double, Integer> compositeTransformer =
                TransformerChainFactory.<Double, Integer>newChain().addFilter(firstFilterTransformation)
                                                                   .add(numberTransformer)
                                                                   .addFilter(secondFilterTransformation)
                                                                   .add(capitalizeTransformer)
                                                                   .last(countTransformer);

        compositeTransformer.setInput(input);

        int count = 0;
        for (Integer i : compositeTransformer.output()) {

            Assert.assertEquals(expectedResults.get(count), i);
            count++;
        }
    }

    /**
     * Checks whether a transformer chain can be constructed and executed with an initial TransformationSource.
     */
    @Test
    public void test_CompositionWithSource() {

        ArrayList<Double> input = new ArrayList<Double>(Arrays.asList(88.0, 754.8, 0.0, 123456.2));
        ArrayList<Integer> expectedResults = new ArrayList<Integer>(Arrays.asList(4, 5, 3, 8));



        // With transformer source
        TransformerSource<Double> initialSource = new DummyTransformerSource<Double>(input);
        OpenTransformerChain<Integer> chain =
                TransformerChainFactory.<Double, Integer>newChain().add(initialSource)
                                       .add(numberTransformer)
                                       .add(capitalizeTransformer)
                                       .last(countTransformer);

        int count = 0;
        for (Integer i : chain) {

            Assert.assertEquals(expectedResults.get(count), i);
            count++;
        }
    }

    /**
     * Checks whether a closed transformer chain can be constructed and executed with a TransformationSource, a Sink and
     * no transformers in between.
     */
    @Test
    public void test_CompositionWithSourceAndTargetNoTransformers() {

        ArrayList<Double> input = new ArrayList<Double>(Arrays.asList(88.0, 754.8, 0.0, 123456.2));

        // With transformer source
        TransformerSource<Double> initialSource = new DummyTransformerSource<Double>(input);
        ArrayList<Double> results = new ArrayList<Double>();
        DummyTransformerSink<Double> sink = new DummyTransformerSink<Double>(results);
        ClosedTransformerChain chain =
                TransformerChainFactory.<Double, Double>newChain()
                                       .add(initialSource)
                                       .last(sink);

        chain.run();

        int count = 0;
        for (Double i : results) {

            Assert.assertEquals(input.get(count), i);
            count++;
        }
    }

    /**
     * Tests whether error events are correctly fired for an open transformer chain.
     */
    @Test
    public void test_OpenTransformerChainErrorReporting() {

        //                                                    Invocation number :   1     2      3       4        5         6          7           8            9            10
        ArrayList<Double> input = new ArrayList<Double>(            Arrays.asList(1.2, 1.23, 1.234, 1.2345, 1.23456, 1.234567, 1.2345678, 1.23456789, 1.234567891, 1.2345678912));
        ArrayList<Integer> expectedResults = new ArrayList<Integer>(Arrays.asList(  3,           5,      6,                 8,         9,                      11));
        //                                                    Output element num    0            1       2                  3          4                        5

        // Transformer error on 5th and 8th invocation (source fails on 2, so the transformation never gets the 2nd element, therefore 5->4 and 8->7
        ExceptionThrowingTransformation<String> tf = new ExceptionThrowingTransformation<String>(4, 7);
        DefaultTransformer<String, String> errorTransformer = new DefaultTransformer<String, String>(tf);
        // Transformer source error on 2nd and 9th invocation
        TransformerSource<Double> initialSource = new DummyTransformerSource<Double>(input, 2, 10);

        // Create an OpenTransformerChain
        OpenTransformerChain<Integer> chain =
                TransformerChainFactory.<Double, Integer>newChain().add(initialSource)
                                                              .add(numberTransformer)
                                                              .add(capitalizeTransformer)
                                                              .add(errorTransformer)
                                                              .last(countTransformer);

        // Listen to Transformer Error events
        LoggingTransformerEventListener loggingTransformerEventListener = new LoggingTransformerEventListener();
        chain.addTransformerEventListener(loggingTransformerEventListener);

        // Listen to Transformer Source Error events
        LoggingTransformerSourceEventListener loggingTransformerSourceEventListener = new LoggingTransformerSourceEventListener();
        chain.addSourceEventListener(loggingTransformerSourceEventListener);

        ArrayList<Integer> actualOutput = new ArrayList<Integer>();
        for (Integer i : chain) {

            actualOutput.add(i);
        }

        // Verify that failed inputs are skipped
        Assert.assertEquals(expectedResults, actualOutput);

        // Verify that each failed transformations are reported
        Assert.assertEquals(2, loggingTransformerEventListener.errorsReported);

        // Verify that each failed source is reported
        Assert.assertEquals(2, loggingTransformerSourceEventListener.errorsReported);

        // Verify that the ErrorEvent reports the expected correct object and exception type

        // Verify whether the source of the error is the correct transformer
        Assert.assertEquals(errorTransformer, loggingTransformerEventListener.eventsOccurred.get(0).getSource());
        Assert.assertEquals(errorTransformer, loggingTransformerEventListener.eventsOccurred.get(1).getSource());

        Assert.assertTrue(loggingTransformerEventListener.eventsOccurred.get(0).getException() instanceof TransformationException);
        Assert.assertTrue(loggingTransformerEventListener.eventsOccurred.get(1).getException() instanceof TransformationException);

        // TODO : Error should contain the first
        //Assert.assertEquals(input.get(4), loggingTransformerEventListener.eventsOccurred.get(0).getFailedObject());
        //Assert.assertEquals(input.get(7), loggingTransformerEventListener.eventsOccurred.get(1).getFailedObject());

        Assert.assertNotNull(loggingTransformerSourceEventListener.eventsOccurred.get(0));
        Assert.assertNotNull(loggingTransformerSourceEventListener.eventsOccurred.get(1));
    }

    /**
     * Tests whether error events are correctly fired for a closed transformer chain.
     */
    @Test
    public void test_ClosedTransformerChainErrorReporting() {

        //                                                    Invocation number :   1     2      3       4        5         6          7           8            9            10
        ArrayList<Double> input = new ArrayList<Double>(            Arrays.asList(1.2, 1.23, 1.234, 1.2345, 1.23456, 1.234567, 1.2345678, 1.23456789, 1.234567891, 1.2345678912));
        ArrayList<Integer> expectedResults = new ArrayList<Integer>(Arrays.asList(  3,           5,      6,                 8,         9,                      11));
        //                                                    Output element num    0            1       2                  3          4                        5
        ArrayList<Integer> actualResults = new ArrayList<Integer>();

        // Create an OpenTransformerChain
        TransformerSource<Double> initialSource = new DummyTransformerSource<Double>(input);
        TransformerSink<Integer> sink = new DummyTransformerSink<Integer>(actualResults, 2, 5, 8, 10);
        ClosedTransformerChain chain =
                TransformerChainFactory.<Double, Integer>newChain().add(initialSource)
                                                              .add(numberTransformer)
                                                              .add(capitalizeTransformer)
                                                              .add(countTransformer)
                                                              .last(sink);

        // Listen to Transformer Error events
        LoggingTransformerEventListener loggingTransformerEventListener = new LoggingTransformerEventListener();
        chain.addTransformerEventListener(loggingTransformerEventListener);

        // Listen to Transformer Source Error events
        LoggingTransformerSourceEventListener loggingTransformerSourceEventListener = new LoggingTransformerSourceEventListener();
        chain.addSourceEventListener(loggingTransformerSourceEventListener);

        // execute the closed chain
        chain.run();

        // Verify that failed inputs are skipped
        Assert.assertEquals(expectedResults, actualResults);

        // Verify that no failed transformations are reported
        Assert.assertEquals(0, loggingTransformerEventListener.errorsReported);

        // Verify that no failed source is reported
        Assert.assertEquals(0, loggingTransformerSourceEventListener.errorsReported);

        // Verify that the ErrorEvent reports the expected correct object and exception type
    }
}
