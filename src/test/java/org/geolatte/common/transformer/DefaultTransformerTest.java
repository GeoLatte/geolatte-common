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
import org.geolatte.testobjects.CharacterCountTransformation;
import org.geolatte.testobjects.ExceptionThrowingTransformation;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * <p>
 * Unit test for the DefaultTransformer class.
 * </p>
 * <i>Creation-Date</i>: 18-Mar-2010<br>
 * <i>Creation-Time</i>:  16:27:20<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class DefaultTransformerTest {

    /**
     * Tests the constructor of Transformer
     */
    @SuppressWarnings({"CatchGenericClass"})
    @Test
    public void test_Constructor() {

        try {

            DefaultTransformer<Object, Object> transformer = new DefaultTransformer<Object, Object>(null);
        }
        catch (Throwable t) {

            Assert.assertSame(IllegalArgumentException.class, t.getClass());
        }

        try {

            new DefaultTransformer<String, Integer>(new CharacterCountTransformation());
        }
        catch (Throwable t) {

            Assert.fail("Should not throw an exception");
        }
    }

    /**
     * Tests basic execution of a Transformer by feeding it input data and checking whether the output is as expected.
     * Also tests whether invoking output() again yields no results.
     */
    @Test
    public void test_Execute() {

        ArrayList<String> input = new ArrayList<String>(Arrays.asList(new String[]{"Scarlett", "Natalie", "Rachel", "Cameron"}));
        ArrayList<Integer> expectedOutput = new ArrayList<Integer>(Arrays.asList(new Integer[]{8, 7, 6, 7}));

        DefaultTransformer<String, Integer> transformer = new DefaultTransformer<String, Integer>(new CharacterCountTransformation());

        transformer.setInput(input);

        int transformationStep = 0;
        for (int count : transformer.output()) {

            Assert.assertEquals(expectedOutput.get(transformationStep), count);
            transformationStep++;
        }

        Assert.assertEquals(expectedOutput.size(), transformationStep);

        // Should not recurse.. the transformation has already provided all of its output values in the previous loop
        for (int count : transformer.output()) {

            Assert.fail("Transformer should not continue to provide output when the output() method is invoked multiple times.");
        }
    }

    /**
     * Tests execution behavior when the input of a transformer is changed or when a transformer is reused to recurse over different sets of inputs.
     */
    @Test
    public void test_ReuseExecute()
    {
        ArrayList<String> input = new ArrayList<String>(Arrays.asList(new String[]{"Scarlett", "Natalie", "Rachel", "Cameron"}));
        ArrayList<Integer> expectedOutput = new ArrayList<Integer>(Arrays.asList(new Integer[]{8, 7, 6, 7}));

        DefaultTransformer<String, Integer> transformer = new DefaultTransformer<String, Integer>(new CharacterCountTransformation());

        transformer.setInput(input);

        int transformationStep = 0;
        for (int element : transformer.output()) {

            // set input to something else and verify that the previous execution isn't interrupted
            if (transformationStep == 2)
            {
                transformer.setInput(input);
                int innerTransformationStep = 0;
                for (int innerElement : transformer.output()) {

                    Assert.assertEquals(expectedOutput.get(innerTransformationStep), innerElement);
                    innerTransformationStep++;
                }
            }

            Assert.assertEquals(expectedOutput.get(transformationStep), element);
            transformationStep++;
        }
    }

    /**
     * Tests whether removing an element from the ouput iterable is impossible.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void test_RemoveFromOutput() {

        ArrayList<String> input = new ArrayList<String>(Arrays.asList(new String[]{"Scarlett", "Natalie", "Rachel", "Cameron"}));

        DefaultTransformer<String, Integer> transformer = new DefaultTransformer<String, Integer>(new CharacterCountTransformation());

        transformer.setInput(input);

        Iterator iterator = transformer.output().iterator();
        iterator.next();
        iterator.remove();
    }

    @Test
    public void test_NoInput() {

        DefaultTransformer<String, Integer> transformer = new DefaultTransformer<String, Integer>(new CharacterCountTransformation());

        Assert.assertEquals("Retrieving the ouput from a transformation that has no input should yield null", null, transformer.output());
    }

    @Test
    public void test_ExceptionHandling() {

        ArrayList<String> input = new ArrayList<String>(Arrays.asList(new String[]{"Scarlett", "Natalie", "Rachel", "Cameron"}));
        ArrayList<String> expectedOutput = new ArrayList<String>(Arrays.asList(new String[]{"Scarlett", "Natalie", "Cameron"})); // exception will be thrown for the third element -> don't expect this in the output

        ExceptionThrowingTransformation<String> tf = new ExceptionThrowingTransformation<String>(3);

        DefaultTransformer<String, String> transformer = new DefaultTransformer<String, String>(tf);
        LoggingTransformerEventListener loggingTransformerEventListener = new LoggingTransformerEventListener();
        transformer.addTransformerEventListener(loggingTransformerEventListener);
        transformer.setInput(input);

        ArrayList<String> actualOutput = new ArrayList<String>();
        for (String out : transformer.output()) {

            actualOutput.add(out);
        }

        // Verify that failed inputs are skipped
        Assert.assertEquals(expectedOutput, actualOutput);

        // Verify that each failed input is reported
        Assert.assertEquals(loggingTransformerEventListener.errorsReported, 1);

        // Verify that the ErrorEvent reports the expected correct object and exception type
        Assert.assertEquals(input.get(2), loggingTransformerEventListener.eventsOccurred.get(0).getFailedObject());
        Assert.assertTrue(loggingTransformerEventListener.eventsOccurred.get(0).getException() instanceof TransformationException);
    }
}