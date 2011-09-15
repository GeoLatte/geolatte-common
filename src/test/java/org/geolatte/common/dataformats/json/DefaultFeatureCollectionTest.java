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

package org.geolatte.common.dataformats.json;

import org.geolatte.common.Feature;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
public class DefaultFeatureCollectionTest {


    @Test
    public void testConstructors()
    {
        DefaultFeatureCollection fc = new DefaultFeatureCollection();
        Assert.assertEquals(0, fc.getFeatures().size());
        List<Feature> test = new ArrayList<Feature>();
        test.add(new DefaultFeature());
        fc = new DefaultFeatureCollection(test);
        Assert.assertEquals(1, fc.getFeatures().size());
    }

    @Test
    public void addRemoveFeatureTest()
    {
        DefaultFeatureCollection fc = new DefaultFeatureCollection();
        Assert.assertEquals(0, fc.getFeatures().size());
        Feature f = new DefaultFeature();
        fc.addFeature(f);
        Assert.assertEquals(1, fc.getFeatures().size());
        Assert.assertTrue(fc.getFeatures().contains(f));
        fc.addFeature(f);
        Assert.assertEquals(2, fc.getFeatures().size());
        Assert.assertTrue(fc.getFeatures().contains(f));
        fc.removeFeature(f);
        Assert.assertEquals(1, fc.getFeatures().size());
        Assert.assertTrue(fc.getFeatures().contains(f));
        fc.removeFeature(f);
        Assert.assertEquals(0, fc.getFeatures().size());
        Assert.assertFalse(fc.getFeatures().contains(f));
    }
}
