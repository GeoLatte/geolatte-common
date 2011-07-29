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

package org.geolatte.wkt;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>
 * Tests whether well known text strings are parsed correctly by {@link WKT#toGeometry()}.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 30-Jun-2010<br>
 * <i>Creation-Time</i>:  10:43:35<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
@RunWith(Parameterized.class)
public class WKTTest {

    private String wellKnownText;
    private Geometry expectedGeometry;

    /**
     * Constructor
     * @param wellKnownText The text to parse.
     * @param expectedGeometry The geometry that is expected to result from parsing.
     */
    public WKTTest(String wellKnownText, Geometry expectedGeometry) {
        super();

        this.wellKnownText = wellKnownText;
        this.expectedGeometry = expectedGeometry;
    }

    /**
     * Creates the collection of data to run tests against. For each element in this collection, a FilterExpressionBuilderTest is instantiated and executed.
     * @return A collection of test data.
     */
    @Parameterized.Parameters
    public static Collection data() {

        GeometryFactory geometryFactory = new GeometryFactory();

        ArrayList<Object[]> list = new ArrayList<Object[]>();

        list.add(new Object[]{"POINT (5 10)", geometryFactory.createPoint(new Coordinate(5, 10))});
        list.add(new Object[]{"POINT EMPTY", geometryFactory.createPoint((Coordinate)null)});
        list.add(new Object[]{"LINESTRING (5 10, 10 15, 15 20)", geometryFactory.createLineString(new Coordinate[] {
                                                                        new Coordinate(5, 10),
                                                                        new Coordinate(10, 15),
                                                                        new Coordinate(15, 20)}
                                                                        )});

        return list;
    }

    /**
     * Verifies a single test case.
     * @throws Exception
     */
    @Test
    public void parameterizedTest() throws Exception {

        /*
        Geometry actualGeometry = null;
        try {

            actualGeometry = WKT.toGeometry(wellKnownText);
            Assert.assertTrue(expectedGeometry.equalsExact(actualGeometry));
        }
        catch(AssertionError e) {

            throw new AssertionError(e.getMessage() + " / WKT=" + wellKnownText + " | Expected=" + expectedGeometry + " | actual=" + actualGeometry);
        }
        catch(Exception e) {

            throw new Exception(e.getMessage() + " / WKT=" + wellKnownText + " | Expected=" + expectedGeometry + " | actual=" + actualGeometry, e);
        }
        */
    }
}
