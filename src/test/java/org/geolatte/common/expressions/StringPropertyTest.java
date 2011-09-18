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
 * Unit test for the StringProperty class.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 23-Jul-2010<br>
 * <i>Creation-Time</i>:  14:01:45<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class StringPropertyTest {

    FilterableObject filterableObject = new FilterableObject();

    @Test
    public void testGetPropertyName() throws Exception {

        StringProperty prop = new StringProperty("aString");

        Assert.assertEquals("aString", prop.getPropertyName());
    }

    @Test
    public void testEvaluate() throws Exception {

        StringProperty prop = new StringProperty(FilterableObject.Properties.aString.toString());

        filterableObject.setaString("somethingsomething");

        Assert.assertEquals(filterableObject.getaString(), prop.evaluate(filterableObject));
    }

    @Test
    public void compareToNormalCaseTest() throws Exception {

        filterableObject.setaString("def");

        StringProperty property = new StringProperty(FilterableObject.Properties.aString.toString());

        // GT
        int comparisonResult = property.compareTo(filterableObject, "ghi");
        Assert.assertTrue(comparisonResult < 0);

        // LT
        comparisonResult = property.compareTo(filterableObject, "abc");
        Assert.assertTrue(comparisonResult > 0);

        // EQ
        comparisonResult = property.compareTo(filterableObject, "def");
        Assert.assertTrue(comparisonResult == 0);
    }
}
