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

package org.geolatte.common.expressions;

import org.geolatte.testobjects.FilterableObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 * Unit test for the BooleanProperty class.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 23-Jul-2010<br>
 * <i>Creation-Time</i>:  13:55:54<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class BooleanPropertyTest {

    FilterableObject filterableObject = new FilterableObject();

    @Test
    public void testGetPropertyName() throws Exception {

        BooleanProperty prop = new BooleanProperty("aBoolean");

        Assert.assertEquals("aBoolean", prop.getPropertyName());
    }

    @Test
    public void testEvaluate() throws Exception {

        BooleanProperty prop = new BooleanProperty(FilterableObject.Properties.aBoolean.toString());

        filterableObject.setaBoolean(true);
        Assert.assertEquals(true, prop.evaluate(filterableObject));

        filterableObject.setaBoolean(false);
        Assert.assertEquals(false, prop.evaluate(filterableObject));

        filterableObject.setaBoolean(null);
        Assert.assertEquals(null, prop.evaluate(filterableObject));
    }


    @Test
    public void compareToNormalCaseTest() throws Exception {

        BooleanProperty property = new BooleanProperty(FilterableObject.Properties.aBoolean.toString());

        filterableObject.setaBoolean(true);

        // GT
        int comparisonResult = property.compareTo(filterableObject, false);
        Assert.assertTrue(comparisonResult > 0);

        // EQ
        comparisonResult = property.compareTo(filterableObject, true);
        Assert.assertTrue(comparisonResult == 0);

        filterableObject.setaBoolean(false);

        // LT
        comparisonResult = property.compareTo(filterableObject, true);
        Assert.assertTrue(comparisonResult < 0);

        // EQ
        comparisonResult = property.compareTo(filterableObject, false);
        Assert.assertTrue(comparisonResult == 0);
    }
}
