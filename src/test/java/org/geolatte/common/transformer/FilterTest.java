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

import org.geolatte.testobjects.ProgrammableFilter;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>
 * Unit test for the DefaultFilter class.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 03-Jun-2010<br>
 * <i>Creation-Time</i>:  10:09:22<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class FilterTest {

    /**
     * Tests the constructor of DefaultFilter
     */
    @SuppressWarnings({"CatchGenericClass"})
    @Test
    public void test_Constructor() {

        try {

            DefaultFilter<Object> transformer = new DefaultFilter<Object>(null);
        }
        catch (Throwable t) {

            Assert.assertSame(IllegalArgumentException.class, t.getClass());
        }

        try {

            new DefaultFilter<String>(new ProgrammableFilter<String>());
        }
        catch (Throwable t) {

            Assert.fail("Should not throw an exception");
        }
    }

    /**
     * Tests basic execution of a DefaultFilter by feeding it input data and checking whether the correct elements are filtered out.
     * Also tests whether invoking output() again yields no results.
     */
    @Test
    public void test_Execute() {

        ArrayList<String> input = new ArrayList<String>(Arrays.asList("Scarlett", "Natalie", "Rachel", "Cameron", "McAdams", "Dunst"));
        Integer[] filteredElements = new Integer[] {2, 4, 6};
        ArrayList<String> expectedOutput = new ArrayList<String>(Arrays.asList("Scarlett", "Rachel", "McAdams"));

        DefaultFilter<String> transformer = new DefaultFilter<String>(new ProgrammableFilter<String>(filteredElements));

        transformer.setInput(input);

        int transformationStep = 0;
        for (String outputElement : transformer.output()) {

            Assert.assertEquals(expectedOutput.get(transformationStep), outputElement);
            transformationStep++;
        }

        Assert.assertEquals(expectedOutput.size(), transformationStep);

        // Should not recurse.. the transformation has already provided all of its output values in the previous loop
        for (String outputElement : transformer.output()) {

            Assert.fail("DefaultFilter should not continue to provide output when the output() method is invoked multiple times.");
        }
    }

}
