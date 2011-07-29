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

package org.geolatte.core.geo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 10-mei-2010<br>
 * <i>Creation-Time</i>: 11:11:14<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class EnvelopeConverterTest {

    private EnvelopeConverter ec;

    @Before
    public void setUp()
    {
        ec = new EnvelopeConverter();
    }

    @Test
    public void testConvert()
    {
        try {
            Envelope en = ec.convert("10.0,11.0,15.3,18");
            Assert.assertEquals(10, en.getMinX(), 0.001);
            Assert.assertEquals(11, en.getMinY(), 0.001);
            Assert.assertEquals(15.3, en.getMaxX(), 0.001);
            Assert.assertEquals(18, en.getMaxY(), 0.001);
        } catch (TypeConversionException e) {
            Assert.fail("No exception should be thrown here!");
        }
        try {
            Envelope en = ec.convert("10.0,11.0,15.3,18,25,25");
            Assert.assertEquals(10, en.getMinX(), 0.001);
            Assert.assertEquals(11, en.getMinY(), 0.001);
            Assert.assertEquals(15.3, en.getMaxX(), 0.001);
            Assert.assertEquals(18, en.getMaxY(), 0.001);
        } catch (TypeConversionException e) {
            Assert.fail("No exception should be thrown here!");
        }
        try {
            Envelope en = ec.convert("10.0,11.0,15.3");
            Assert.fail("Exception should have been thrown");
        } catch (TypeConversionException e) {
            // Ok!
        }
        try {
            Envelope en = ec.convert("Abc,11.0,15.3");
            Assert.fail("Exception should have been thrown");
        } catch (TypeConversionException e) {
            // Ok!
        }
    }

    @Test
    public void testCreateEnvelope()
    {
        Coordinate first = new Coordinate(10.0,11.0,12.0);
        Coordinate second = new Coordinate(5.0,28.0,42.0);
        Coordinate third = new Coordinate(30,30,30);
        try {
            Envelope en = ec.createEnvelope(new Coordinate[]{first, second});
            Assert.assertEquals(5, en.getMinX(), 0.001);
            Assert.assertEquals(11, en.getMinY(), 0.001);
            Assert.assertEquals(10, en.getMaxX(), 0.001);
            Assert.assertEquals(28, en.getMaxY(), 0.001);
        } catch (TypeConversionException e) {
            Assert.fail("No exception expected");
        }
        try {
            Envelope en = ec.createEnvelope(new Coordinate[]{first, second,third});
            Assert.assertEquals(5, en.getMinX(), 0.001);
            Assert.assertEquals(11, en.getMinY(), 0.001);
            Assert.assertEquals(10, en.getMaxX(), 0.001);
            Assert.assertEquals(28, en.getMaxY(), 0.001);
        } catch (TypeConversionException e) {
            Assert.fail("No exception expected");
        }
        try {
            Envelope en = ec.createEnvelope(new Coordinate[]{first});
            Assert.fail("Exception should have been thrown");
        } catch (TypeConversionException e) {
            // Ok!
        }
    }

    @Test
    public void testGetCoordinate()
    {
        try {
            Coordinate[] list = ec.getCoordinates("10.0,11.0,15.3,18,25,25,13.3");
            Assert.assertEquals(3, list.length);
            Assert.assertEquals(10.0,list[0].x,0.001);
            Assert.assertEquals(11.0,list[0].y,0.001);
            Assert.assertEquals(15.3,list[1].x,0.001);
            Assert.assertEquals(18,list[1].y,0.001);
            Assert.assertEquals(25,list[2].x,0.001);
            Assert.assertEquals(25,list[2].y,0.001);
        } catch (TypeConversionException e) {
            Assert.fail("No exception expected");
        }
        try {
            Envelope en = ec.convert("abc,11.0,15.3");
            Assert.fail("Exception should have been thrown");
        } catch (TypeConversionException e) {
            // Ok!
        }
    }

}
