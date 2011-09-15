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

package org.geolatte.common.dataformats.csv;

import org.geolatte.common.reflection.EntityClassReader;
import org.geolatte.common.transformer.TransformationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 3-dec-2010<br>
 * <i>Creation-Time</i>: 14:45:03<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since Kabelnet 0.1
 */
public class CsvSerializationTransformationTest {

    private List<TestClass> testFeatures;
    private static EntityClassReader reader;

    @Before
    public void setUp() {
            TestClass testFeature = new TestClass(15,10,"Hehe", false);
            TestClass testFeature2 = new TestClass(8,0,null, true);
            testFeatures = new ArrayList<TestClass>();
        testFeatures.add(testFeature);
        testFeatures.add(testFeature2);
    }

    @Test
    public void BaseSerializerTest() {
        CsvSerializationTransformation<Object> serializer = new CsvSerializationTransformation<Object>(
                TestClass.class, '|', null);
        Assert.assertEquals(7, serializer.getHeader().length());
        Assert.assertEquals('|', serializer.getHeader().charAt(1));
        Assert.assertEquals('|', serializer.getHeader().charAt(3));
        Assert.assertEquals('|', serializer.getHeader().charAt(5));
        Assert.assertTrue(serializer.getHeader().contains("A"));
        Assert.assertTrue(serializer.getHeader().contains("B"));
        Assert.assertTrue(serializer.getHeader().contains("C"));
        Assert.assertTrue(serializer.getHeader().contains("D"));
        try {
            String result = serializer.transform(testFeatures.get(0));
            String[] splitResult = result.split("\\|", -2);
            Assert.assertEquals(4, splitResult.length);
            // chars are at 0,2,4,6
            Assert.assertEquals("15", splitResult[serializer.getHeader().indexOf("A")/2]);
            Assert.assertEquals("10", splitResult[serializer.getHeader().indexOf("B")/2]);
            Assert.assertEquals("Hehe", splitResult[serializer.getHeader().indexOf("C")/2]);
            Assert.assertEquals("false", splitResult[serializer.getHeader().indexOf("D")/2]);

            String[] splitResult2 = serializer.transform(testFeatures.get(1)).split("\\|", -2);
            Assert.assertEquals(4, splitResult2.length);
            // chars are at 0,2,4,6
            Assert.assertEquals("8", splitResult2[serializer.getHeader().indexOf("A")/2]);
            Assert.assertEquals("0", splitResult2[serializer.getHeader().indexOf("B")/2]);
            Assert.assertEquals("", splitResult2[serializer.getHeader().indexOf("C")/2]);
            Assert.assertEquals("true", splitResult2[serializer.getHeader().indexOf("D")/2]);

        } catch (TransformationException e) {
            Assert.fail("No exception expected");
        }
        try {
            serializer.transform("SomeString");
            Assert.fail("Exception should be given when transforming wrong class objects");
        } catch (TransformationException e) {
            //
        } catch (Exception e) {
            Assert.fail("Only a transformationexception please!");
        }
    }

    @Test
    public void VisibilitySerializerTest() {
        CsvSerializationTransformation<Object> serializer = new CsvSerializationTransformation<Object>(
                TestClass.class, '+', Arrays.asList("D", "B"));
        Assert.assertEquals("D+B", serializer.getHeader());
        try {
            Assert.assertEquals("false+10", serializer.transform(testFeatures.get(0)));
            Assert.assertEquals("true+0", serializer.transform(testFeatures.get(1)));
        } catch (TransformationException e) {
            Assert.fail("No exception expected");
        }
        try {
            serializer.transform("SomeString");
            Assert.fail("Exception should be given when transforming wrong class objects");
        } catch (TransformationException e) {
            //
        } catch (Exception e) {
            Assert.fail("Only a transformationexception please!");
        }
    }

    public class TestClass
    {
        private int a;
        private int b;
        private String c;
        private boolean d;

        public TestClass(int a, int b, String c, boolean d)
        {
            setA(a);
            setB(b);
            setC(c);
            setD(d);
        }

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public boolean getD() {
            return d;
        }

        public void setD(boolean d) {
            this.d = d;
        }
    }
}


