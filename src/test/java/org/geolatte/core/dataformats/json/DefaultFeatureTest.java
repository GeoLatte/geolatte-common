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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 6-sep-2010<br>
 * <i>Creation-Time</i>: 16:05:04<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class DefaultFeatureTest {

    private DefaultFeature f;
    private static GeometryFactory geomFact;

    @BeforeClass
    public static void setupOnce()
    {
        geomFact = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 31370);
    }

    @Before
    public void setup()
    {
        f = new DefaultFeature();
    }

    @Test
    public void geometryTest()
    {
        Assert.assertFalse(f.hasGeometry());
        Assert.assertNull(f.getGeometry());
        Assert.assertFalse(f.hasId());
        Assert.assertNull(f.getProperty("geometry"));
        Assert.assertEquals(0, f.getProperties().size());

        Point p = geomFact.createPoint(new Coordinate(3.0, 2.0));
        f.setGeometry("geometry", p);

        Assert.assertTrue(f.hasGeometry());
        Assert.assertEquals(p, f.getGeometry());
        Assert.assertFalse(f.hasId());
        Assert.assertNull(f.getProperty("geometry"));
        Assert.assertTrue(f.hasProperty("geometry", true));
        Assert.assertFalse(f.hasProperty("geometry", false));        
        Assert.assertEquals(0, f.getProperties().size());

        f.setGeometry(null, p);
        Assert.assertFalse(f.hasGeometry());
        Assert.assertEquals(null, f.getGeometry());
        Assert.assertFalse(f.hasId());
        Assert.assertNull(f.getProperty("geometry"));
        Assert.assertEquals(0, f.getProperties().size());

        f.setGeometry("geometry", null);
        Assert.assertTrue(f.hasGeometry());
        Assert.assertEquals(null, f.getGeometry());
        Assert.assertFalse(f.hasId());
        Assert.assertNull(f.getProperty("geometry"));
        Assert.assertEquals(0, f.getProperties().size());
    }

    @Test
    public void idTest()
    {
        Assert.assertFalse(f.hasGeometry());
        Assert.assertFalse(f.hasId());
        Assert.assertNull(f.getId());
        Assert.assertNull(f.getProperty("id"));
        Assert.assertEquals(0, f.getProperties().size());

        f.setId("id", "hey");

        Assert.assertTrue(f.hasId());
        Assert.assertFalse(f.hasGeometry());
        Assert.assertEquals("hey", f.getId());
        Assert.assertNull(f.getProperty("id"));
        Assert.assertEquals(0, f.getProperties().size());

        f.setId("geometry", "hey");
        Assert.assertTrue(f.hasId());
        Assert.assertFalse(f.hasGeometry());
        Assert.assertEquals("hey", f.getId());
        Assert.assertNull(f.getProperty("geometry"));
        Assert.assertEquals(0, f.getProperties().size());

        f.setId("geometry", null);
        Assert.assertTrue(f.hasId());
        Assert.assertFalse(f.hasGeometry());
        Assert.assertEquals(null, f.getId());
        Assert.assertNull(f.getProperty("geometry"));
        Assert.assertEquals(0, f.getProperties().size());

        f.setId(null, null);
        Assert.assertFalse(f.hasGeometry());
        Assert.assertEquals(null, f.getId());
        Assert.assertFalse(f.hasId());
        Assert.assertNull(f.getProperty("geometry"));
        Assert.assertEquals(0, f.getProperties().size());
    }

    @Test
    public void propertyTest()
    {
        Assert.assertFalse(f.hasProperty("hallo", false));
        Assert.assertFalse(f.hasProperty("hallo", true));
        Assert.assertEquals(null, f.getProperty("hallo"));
        Assert.assertEquals(0, f.getProperties().size());
        f.addProperty("hallo", "hey");
        Assert.assertTrue(f.hasProperty("hallo", false));
        Assert.assertTrue(f.hasProperty("hallo", true));
        Assert.assertEquals("hey", f.getProperty("hallo"));
        Assert.assertEquals(1, f.getProperties().size());
        f.addProperty("hallo", null);
        Assert.assertTrue(f.hasProperty("hallo", false));
        Assert.assertTrue(f.hasProperty("hallo", true));
        Assert.assertEquals(null, f.getProperty("hallo"));
        Assert.assertEquals(1, f.getProperties().size());
        f.addProperty("hallo2", "hihi");
        Assert.assertTrue(f.hasProperty("hallo", false));
        Assert.assertTrue(f.hasProperty("hallo", true));
        Assert.assertEquals(null, f.getProperty("hallo"));
        Assert.assertTrue(f.hasProperty("hallo2", false));
        Assert.assertTrue(f.hasProperty("hallo2", true));
        Assert.assertEquals("hihi", f.getProperty("hallo2"));
        Assert.assertEquals(2, f.getProperties().size());
        f.wipeProperty("hallo2");
        Assert.assertTrue(f.hasProperty("hallo", false));
        Assert.assertTrue(f.hasProperty("hallo", true));
        Assert.assertEquals(null, f.getProperty("hallo"));
        Assert.assertFalse(f.hasProperty("hallo2", false));
        Assert.assertFalse(f.hasProperty("hallo2", true));
        Assert.assertEquals(null, f.getProperty("hallo2"));
        Assert.assertEquals(1, f.getProperties().size());
    }
}
