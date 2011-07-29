/*
 * This file is part of the GeoLatte project. This code is licenced under
 * the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 *
 * Copyright (C) 2010 - 2010 and Ownership of code is shared by:
 * Qmino bvba - Romeinsestraat 18 - 3001 Heverlee  (http://www.Qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.core.dataformats.json;

import org.codehaus.jackson.JsonParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 6-sep-2010<br>
 * <i>Creation-Time</i>: 16:44:42<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class AbstractJsonDeserializerTest {

    private static final double ACCURACY = 0.0000005;
    private TestDeser test;

    /**
     * Empty deserializer to test the parentmethods.
     */
    class TestDeser extends AbstractJsonDeserializer<Object>
    {
        public TestDeser(JsonMapper owner) {
            super(owner);
        }

        @Override
        protected Object deserialize(JsonParser jsonParser) throws IOException {
            return null;
        }
    }

    @Before
    public void setup()
    {
        test = new TestDeser(null);
    }

    @Test
    public void testParseMethods()
    {
        Double emptyDouble = null;
        Integer emptyInt = null;
        Double d = test.parseDefault("5.5", emptyDouble);
        Assert.assertEquals(5.5, d, ACCURACY);
        d = test.parseDefault("a", emptyDouble);
        Assert.assertNull(d);
        d = test.parseDefault("a", 3.3);
        Assert.assertEquals(3.3, d, ACCURACY);
    }


}
