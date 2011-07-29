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

package org.geolatte.core.expressions;

import org.geolatte.testobjects.FilterableObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 * Unit test for the NumberProperty class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 25-May-2010<br>
 * <i>Creation-Time</i>:  14:49:08<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class NumberPropertyTest {

    FilterableObject filterableObject = new FilterableObject();

    @Test
    public void testGetPropertyName() throws Exception {

        NumberProperty prop = new NumberProperty("anInteger");

        Assert.assertEquals("anInteger", prop.getPropertyName());
    }

    @Test
    public void testEvaluate() throws Exception {

        NumberProperty prop = new NumberProperty(FilterableObject.Properties.anInteger.toString());

        filterableObject.setAnInteger(5);

        Assert.assertEquals(5, prop.evaluate(filterableObject));
    }


    @Test
    public void compareToNormalCaseTest() throws Exception {

        filterableObject.setaDouble(10);

        NumberProperty property = new NumberProperty(FilterableObject.Properties.aDouble.toString());

        // GT
        int comparisonResult = property.compareTo(filterableObject, 5);
        Assert.assertTrue(comparisonResult > 0);

        // LT
        comparisonResult = property.compareTo(filterableObject, 15);
        Assert.assertTrue(comparisonResult < 0);

        // EQ
        comparisonResult = property.compareTo(filterableObject, 10);
        Assert.assertTrue(comparisonResult == 0);
    }
}
