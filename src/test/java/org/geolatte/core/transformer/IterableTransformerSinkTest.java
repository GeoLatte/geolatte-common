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

package org.geolatte.core.transformer;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * <p>
 * <i>Creation-Date</i>: 13-Apr-2010<br>
 * <i>Creation-Time</i>:  13:27:37<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class IterableTransformerSinkTest {

    /**
     * Tests the construction of an IterableTransformerSink as well setting the input.
     */
    @Test
    public void test_Construction() {

        IterableTransformerSink<String> sink = new IterableTransformerSink<String>();

        Assert.assertEquals(null, sink.iterator());

        sink.setInput(new ArrayList<String>());
        Assert.assertNotNull(sink.iterator());
    }

    /**
     * Check whether the given input is passed to the output 1-to-1.
     * Also checks whether re-setting the input results in a new, independent output iterator.
     */
    @Test
    public void test_Iterator()
    {
        ArrayList<String> input = new ArrayList<String>(Arrays.asList(new String[]{"Scarlett", "Natalie", "Rachel", "Cameron"}));
        ArrayList<String> input2 = new ArrayList<String>(Arrays.asList(new String[]{"Johanssen", "Portman", "McAdams", "Diaz"}));

        IterableTransformerSink<String> sink = new IterableTransformerSink<String>();
        sink.setInput(input);

        int index = 0;
        for (String s : sink) {

            Assert.assertEquals(input.get(index++), s);
        }

        sink.setInput(input2);
        index = 0;
        for (String s : sink) {

            Assert.assertEquals(input2.get(index++), s);
        }
    }

    /**
     * Verifies that the remove() method of the iterator does nothing.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void check_IteratorRemoveDisabled() {

        ArrayList<String> input = new ArrayList<String>(Arrays.asList(new String[]{"Scarlett", "Natalie", "Rachel", "Cameron"}));

        IterableTransformerSink<String> sink = new IterableTransformerSink<String>();
        sink.setInput(input);

        Iterator iterator = sink.iterator();
        iterator.next();
        iterator.remove();

    }
}
