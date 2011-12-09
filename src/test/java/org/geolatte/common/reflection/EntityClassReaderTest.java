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

package org.geolatte.common.reflection;

import org.geolatte.common.Feature;
import org.geolatte.geom.*;
import org.geolatte.geom.crs.CrsId;
import org.geolatte.testobjects.TestFeature;
import org.geolatte.testobjects.TestFeatureDoubleShape;
import org.geolatte.testobjects.TestFeatureNoShape;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * <p>
 * Testclass for the entityclassreader
 * </p>
 * <p>
 * <i>Creation-Date</i>: 9-apr-2010<br>
 * <i>Creation-Time</i>:  11:48:54<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class EntityClassReaderTest {

    private Object testFeature;
    private EntityClassReader reader;

    @Before
    public void setUp() {
        PointSequence pnts = PointSequenceFactory.create(new double[]{5,6,6,7,7,8}, DimensionalFlag.XY);
        LineString shape = LineString.create(pnts, CrsId.UNDEFINED);
        testFeature = new TestFeature("Antwerpen", 128, new String[]{"Belgium", "Flanders"}, shape, 5, "sub", "subsub");
        reader = EntityClassReader.getClassReaderFor(testFeature.getClass());
    }

    @Test
    public void PropertyGettersTest() {
        try {
            Assert.assertEquals(5, reader.getId(testFeature));
            Assert.assertEquals("Antwerpen", reader.getPropertyValue(testFeature, "name"));
            Assert.assertNull(reader.getPropertyValue(testFeature, "doesNotExist"));
            Assert.assertEquals(((TestFeature) testFeature).getShape(), reader.getGeometry(testFeature));

            // Compound properties
            Assert.assertSame("sub", reader.getPropertyValue(testFeature, "subObject.name"));
            Assert.assertSame("subsub", reader.getPropertyValue(testFeature, "subObject.subSubObject.name"));
            Assert.assertNull(reader.getPropertyValue(testFeature, "subObject.doesNotExist"));

        } catch (InvalidObjectReaderException e) {
            Assert.fail("Should not throw an exception here!");
        }
    }

    @Test
    public void getPropertyTypeTest() {

        Assert.assertSame("Getting simple property type failed", String.class, reader.getPropertyType("name"));
        Assert.assertNull(reader.getPropertyType("doesNotExist"));

        // Sub property
        Assert.assertSame("Getting sub property type failed", String.class, reader.getPropertyType("subObject.name"));
        Assert.assertNull(reader.getPropertyType("subObject.doesNotExist"));
    }

    @Test
    public void GeometryNameGetterTest() {
        Assert.assertEquals("shape", reader.getGeometryName());
        Assert.assertNull(EntityClassReader.getClassReaderFor(TestFeatureNoShape.class).getGeometryName());
    }


    @Test
    public void PropertyListTest() {
        Assert.assertTrue(reader.exists("length", false));
        Assert.assertFalse(reader.exists("someRandom", false));
        Assert.assertEquals(4, reader.getProperties().size());
    }

    @Test
    public void InvalidReaderCallTest() {
        try {
            reader.getPropertyValue(new ArrayList<String>(), "name");
            Assert.fail("Reader should have thrown an exception!");
        } catch (InvalidObjectReaderException e) {
            // Ok!
        }
        try {
            reader.getId(new ArrayList<String>());
            Assert.fail("Reader should have thrown an exception!");
        } catch (InvalidObjectReaderException e) {
            // Ok!
        }
        try {
            reader.getGeometry(new ArrayList<String>());
            Assert.fail("Reader should have thrown an exception!");
        } catch (InvalidObjectReaderException e) {
            // Ok!
        }
        try {
            reader.asFeature(new ArrayList<String>());
            Assert.fail("Reader should have thrown an exception!");
        } catch (InvalidObjectReaderException e) {
            // Ok!
        }
        try {
            reader.asFeature(null);
            Assert.fail("IllegalArgumentException should be thrown");
        } catch (InvalidObjectReaderException e) {
            Assert.fail("IllegalArgumentException should be thrown instead");
        } catch (IllegalArgumentException e) {
            // Ok!
        }
        try {
            reader.getGeometry(null);
            Assert.fail("IllegalArgumentException should be thrown");
        } catch (InvalidObjectReaderException e) {
            Assert.fail("IllegalArgumentException should be thrown instead");
        } catch (IllegalArgumentException e) {
            // Ok!
        }
        try {
            reader.getId(null);
            Assert.fail("IllegalArgumentException should be thrown");
        } catch (InvalidObjectReaderException e) {
            Assert.fail("IllegalArgumentException should be thrown instead");
        } catch (IllegalArgumentException e) {
            // Ok!
        }
        try {
            reader.getPropertyValue(testFeature, null);
            Assert.fail("IllegalArgumentException should be thrown");
        } catch (InvalidObjectReaderException e) {
            Assert.fail("IllegalArgumentException should be thrown instead");
        } catch (IllegalArgumentException e) {
            // Ok!
        }
        try {
            reader.getPropertyValue(null, "name");
            Assert.fail("IllegalArgumentException should be thrown");
        } catch (InvalidObjectReaderException e) {
            Assert.fail("IllegalArgumentException should be thrown instead");
        } catch (IllegalArgumentException e) {
            // Ok!
        }
        try {
            reader.exists(null, true);
            Assert.fail("IllegalArgumentException should be thrown");
        }
        catch (IllegalArgumentException e) {
        }
        try {
            reader = EntityClassReader.getClassReaderFor(null);
        }
        catch (IllegalArgumentException e) {
            Assert.fail("Null should have been returned!");
        }

    }

    @Test
    public void AsFeatureTest() {
        try {
            Feature asFeature = reader.asFeature(testFeature);
            Feature asFeature2 = reader.asFeature(testFeature);

            Assert.assertEquals(5, asFeature.getId());
            Assert.assertEquals("Antwerpen", asFeature.getProperty("name"));
            Assert.assertEquals(((TestFeature) testFeature).getShape(), asFeature.getGeometry());
            Assert.assertEquals("shape", asFeature.getGeometryName());
            Assert.assertEquals(4, asFeature.getProperties().size());
            Assert.assertTrue(asFeature.hasProperty("length", false));
            Assert.assertFalse(asFeature.hasProperty("somethingelse", false));
            try {
                asFeature.getProperty(null);
                Assert.fail("Impossible to retrieve a null property");
            } catch (IllegalArgumentException e) {
                // Ok!
            }
            try {
                asFeature.hasProperty(null, false);
                Assert.fail("Impossible to check existence of a null property");
            } catch (IllegalArgumentException e) {
                // OK!
            }
        } catch (InvalidObjectReaderException e) {
            Assert.fail("Should not thrown an exception!");
        }

    }


    @Test
    public void alternativeConstructorTest()
    {
        reader = EntityClassReader.getClassReaderFor(testFeature.getClass(), "shape", "length");
        Assert.assertEquals(4, reader.getProperties().size());
        try {
            Assert.assertEquals(128, reader.getId(testFeature));
        } catch (InvalidObjectReaderException e) {
            Assert.fail("No exception should be thrown");
        }
        reader = EntityClassReader.getClassReaderFor(testFeature.getClass(), null, null);
        Assert.assertEquals(6, reader.getProperties().size());
        try {
            Assert.assertNull(reader.getId(testFeature));
            Assert.assertNull(reader.getGeometry(testFeature));            
        } catch (InvalidObjectReaderException e) {
            Assert.fail("No exception should be thrown");
        }

    }

    @Test
    public void NoShapeDoubleShapeTest() {
        PointSequence points = PointSequenceFactory.create(new double[]{8,9,9,10,10,11} , DimensionalFlag.XY);
        LineString shape2 = LineString.create(points, CrsId.UNDEFINED);
        points = PointSequenceFactory.create(new double[]{5,6,6,7,7,8}, DimensionalFlag.XY);
        LineString shape = LineString.create(points, CrsId.UNDEFINED);
        Object noShape = new TestFeatureNoShape("Antwerpen", 128, new String[]{"Belgium", "Flanders"}, 5, "sub", "subsub");
        Object doubleShape = new TestFeatureDoubleShape("Antwerpen", 128, new String[]{"Belgium", "Flanders"}, shape, 5, shape2, "sub");
        reader = EntityClassReader.getClassReaderFor(noShape.getClass());
        try {
            Geometry test = reader.getGeometry(noShape);
            Assert.assertNull(test);
        } catch (InvalidObjectReaderException e) {
            // No Exception should be thrown!
            Assert.fail("No Exception should be thrown when no one geometry is present");
        }
        reader = EntityClassReader.getClassReaderFor(doubleShape.getClass());
        try {
            Geometry geom = reader.getGeometry(doubleShape);
            Assert.assertTrue(geom == shape2 || geom == shape);
        } catch (InvalidObjectReaderException e) {
            // No exception may be thrown
            Assert.fail("No Exception should be thrown when more than one geometry is present");
        }
    }


    
}
