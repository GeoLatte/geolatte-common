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
import org.geolatte.common.transformer.TransformationException;
import org.geolatte.testobjects.TestFeature;
import org.junit.Assert;
import org.junit.Test;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 10-mei-2010<br>
 * <i>Creation-Time</i>: 11:34:58<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class ObjectToFeatureTransformationTest {

    @Test
    public void testTransform()
    {
        ObjectToFeatureTransformation trans = new ObjectToFeatureTransformation();
        Feature f;
        try {
            f = trans.transform(new TestFeature());
            Assert.assertEquals(f.getProperty("name"), "Antwerpen");
            Assert.assertNull(trans.transform(null));
        } catch (TransformationException e) {
            Assert.fail("No exception should have been thrown.");
        }
    }
}
