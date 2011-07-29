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

package org.geolatte.core.expressions.geo;

import com.vividsolutions.jts.geom.*;
import org.geolatte.core.expressions.Expression;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * Tests the {@link GeoEquals} class.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 31-Aug-2010<br>
 * <i>Creation-Time</i>:  19:50:43<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class GeoEqualsTest {

    private GeometryFactory geometryFactory = new GeometryFactory();

    // Set Imposteriser to enable mocking classes (otherwise, only interfaces can be mocked)
    protected Mockery context = new JUnit4Mockery() {
        {setImposteriser(ClassImposteriser.INSTANCE);}
    };

    protected Point geometry;
    protected Point anEqualGeometry;
    protected Point anUnEqualGeometry;
    protected LineString aDifferentTypeOfGeometry;

    @SuppressWarnings("unchecked")
    protected Expression<Geometry> geometryExpression;

    @SuppressWarnings("unchecked")
    protected Expression<Geometry> anEqualGeometryExpression;

    @SuppressWarnings("unchecked")
    protected Expression<Geometry> anUnEqualGeometryExpression;

    @SuppressWarnings("unchecked")
    protected Expression<Geometry> aDifferentTypeOfGeometryExpression;

    protected Object theObjectToEvaluate = new Object();

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {

        geometry = geometryFactory.createPoint(new Coordinate( 1, 1 ));
        anEqualGeometry = geometryFactory.createPoint(new Coordinate( 1, 1 ));
        anUnEqualGeometry = geometryFactory.createPoint(new Coordinate( 1, 2));
        aDifferentTypeOfGeometry = geometryFactory.createLineString(new Coordinate[]{new Coordinate(1,1), new Coordinate(2,2)});


        geometryExpression = (Expression<Geometry>)context.mock(Expression.class, "geometry");
        anEqualGeometryExpression = (Expression<Geometry>)context.mock(Expression.class, "anEqualGeometry");
        anUnEqualGeometryExpression = (Expression<Geometry>)context.mock(Expression.class, "anUnEqualGeometry");
        aDifferentTypeOfGeometryExpression = (Expression<Geometry>)context.mock(Expression.class, "aDifferentTypeOfGeometry");

        context.checking(new Expectations() {
            {allowing(geometryExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(geometry));}               // Always evaluate to geometry;
        });
        context.checking(new Expectations() {
            {allowing(anEqualGeometryExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(anEqualGeometry));} // Always evaluate to anEqualGeometry;
        });
        context.checking(new Expectations() {
            {allowing(anUnEqualGeometryExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(anUnEqualGeometry));} // Always evaluate to anUnEqualGeometry;
        });
        context.checking(new Expectations() {
            {allowing(aDifferentTypeOfGeometryExpression).evaluate(with(theObjectToEvaluate)); will(returnValue(aDifferentTypeOfGeometry));} // Always evaluate to anUnEqualGeometry;
        });
    }

    @Test
    public void testEvaluate() throws Exception {

        GeoEquals exp;

        exp = new GeoEquals(geometryExpression, anEqualGeometryExpression);
        Assert.assertEquals("Two equal geometry expressions were evaluated as not equal", true, exp.evaluate(theObjectToEvaluate));

        exp = new GeoEquals(geometryExpression, anUnEqualGeometryExpression);
        Assert.assertEquals("Two different geometry expressions were evaluated as equal", false, exp.evaluate(theObjectToEvaluate));

        exp = new GeoEquals(geometryExpression, aDifferentTypeOfGeometryExpression);
        Assert.assertEquals("Two different geometry expressions types were evaluated as equal", false, exp.evaluate(theObjectToEvaluate));
    }
}
