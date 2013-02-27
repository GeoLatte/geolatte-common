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
 * Copyright (C) 2010 - 2011 and Ownership of code is shared by:
 * Qmino bvba - Esperantolaan 4 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.common.dataformats.json;

import com.vividsolutions.jts.geom.*;
import junit.framework.Assert;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.geolatte.common.dataformats.json.to.*;
import org.geolatte.geom.crs.CrsId;
import org.geolatte.geom.jts.JTS;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests the entire sequence from Json string -> To -> Geolatte Geom -> JTS
 * Although the last part is not strictly necessary in this testsuite, i have included it in the testscope since
 * the geojson -> jts sequence is often used by users of the library
 * The actual deserialization from the json string to a geojsonto is done by a standard json serializer (jackson)
 * The test sequences are similar to those of the original json deserializer (in the jackson project)
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 */
public class GeoJsonToDeserializationTest {

    private static GeoJsonToAssembler assembler;
    private static ObjectMapper mapper;
    private static final double ACCURACY = 0.0000005;
    private static final CrsId LAMBERT72 = new CrsId("EPSG", 31370);

    @BeforeClass
    public static void setupSuite() {
        assembler = new GeoJsonToAssembler();
        mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Test deserialization of a point, with and without crs specification. Both with Point and Geometry as target
     *
     * @throws java.io.IOException if an unexpected exception is thrown. This will fail the test.
     */
    @Test
    public void testvalidPointDeserialization() throws IOException {
        String testString = "{ \"type\": \"Point\", \"coordinates\": [100.0, 0.0] }";
        // Integers instead of double
        String testString1b = "{ \"type\": \"Point\", \"coordinates\": [100, 0] }";
        String testString2 = "{ \"crs\": {\n" +
                "  \"type\": \"name\",\n" +
                "  \"properties\": {\n" +
                "    \"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"\n" +
                "    }\n" +
                "  }, \"type\": \"Point\", \"coordinates\": [100.0, 0.0] }";
        // Only for a point we test the alternative crs as well...
        String testString3 = "{ \"crs\": {\n" +
                "  \"type\": \"name\",\n" +
                "  \"properties\": {\n" +
                "    \"name\": \"EPSG:31370\"\n" +
                "    }\n" +
                "  }, \"type\": \"Point\", \"coordinates\": [100.0, 0.0] }";
        // Let's also test a 3d point.
        String testString4 = "{ \"crs\": {\n" +
                "  \"type\": \"name\",\n" +
                "  \"properties\": {\n" +
                "    \"name\": \"EPSG:31370\"\n" +
                "    }\n" +
                "  }, \"type\": \"Point\", \"coordinates\": [100.0, 0.0, 50.0] }";

        Point test = (Point) JTS.to(assembler.fromTransferObject(mapper.readValue(testString, PointTo.class)));
        Coordinate c = test.getCoordinate();
        Assert.assertEquals(100.0, c.x, ACCURACY);
        Assert.assertEquals(0.0, c.y, ACCURACY);
        Assert.assertEquals(CrsId.UNDEFINED, CrsId.valueOf(test.getSRID()));
        Assert.assertTrue(Double.isNaN(c.z));

        Point test1b = (Point) JTS.to(assembler.fromTransferObject(mapper.readValue(testString1b, PointTo.class)));
        Coordinate c1b = test1b.getCoordinate();
        Assert.assertEquals(100.0, c1b.x, ACCURACY);
        Assert.assertEquals(0.0, c1b.y, ACCURACY);
        Assert.assertEquals(CrsId.UNDEFINED, CrsId.valueOf(test1b.getSRID()));
        Assert.assertTrue(Double.isNaN(c1b.z));

        // We deserialize to a geojsonto instead of a
        com.vividsolutions.jts.geom.Geometry test2 = JTS.to(assembler.fromTransferObject(mapper.readValue(testString2, GeoJsonTo.class)));
        Assert.assertTrue(test2 instanceof Point);
        Coordinate c2 = test2.getCoordinate();
        Assert.assertEquals(100.0, c2.x, ACCURACY);
        Assert.assertEquals(0.0, c2.y, ACCURACY);
        Assert.assertEquals(LAMBERT72.getCode(), test2.getSRID());
        Assert.assertTrue(Double.isNaN(c2.z));

        Point test3 = (Point) JTS.to(assembler.fromTransferObject(mapper.readValue(testString3, PointTo.class)));
        Coordinate c3 = test3.getCoordinate();
        Assert.assertEquals(100.0, c3.x, ACCURACY);
        Assert.assertEquals(0.0, c3.y, ACCURACY);
        Assert.assertEquals(LAMBERT72.getCode(), test3.getSRID());
        Assert.assertTrue(Double.isNaN(c3.z));

        Point test4 = (Point) JTS.to(assembler.fromTransferObject(mapper.readValue(testString4, PointTo.class)));
        Coordinate c4 = test4.getCoordinate();
        Assert.assertEquals(100.0, c4.x, ACCURACY);
        Assert.assertEquals(0.0, c4.y, ACCURACY);
        Assert.assertEquals(50.0, c4.z, ACCURACY);
        Assert.assertEquals(LAMBERT72.getCode(), test4.getSRID());
    }

    /**
     * Test invalid variations of a json representing a point
     */
    @Test
    public void testInvalidPointDeserialization() {
        List<String> invalidTestStrings = new ArrayList<String>();
        // Unexisting type
        invalidTestStrings.add("{ \"type\": \"Poit\", \"coordinates\": [100.0, 0.0] }");
        // Wrong type
        invalidTestStrings.add("{ \"type\": \"LineString\", \"coordinates\": [100.0, 0.0] }");
        // No type
        invalidTestStrings.add("{ \"coordinates\": [100.0, 0.0] }");
        // Invalid crs name
        invalidTestStrings.add("{ \"crs\": {\n" +
                "  \"type\": \"name\",\n" +
                "  \"properties\": {\n" +
                "    \"name\": \"urn:EPSG:7.6:31370\"\n" +
                "    }\n" +
                "  }, \"type\": \"Point\", \"coordinates\": [100.0, 0.0] }");
        // Empty crs name
        invalidTestStrings.add("{ \"crs\": {}\n" +
                "  }, \"type\": \"Point\", \"coordinates\": [100.0, 0.0] }");
        // Invalid coordinate array
        invalidTestStrings.add("{ \"type\": \"Point\", \"coordinates\": [[100.0, 0.0]] }");
        // Only one value as a coordinate
        invalidTestStrings.add("{\"type\": \"Point\", \"coordinates\": [100.0] }");
        // No coordinate array
        invalidTestStrings.add("{\"type\": \"Point\"}");
        // Just an invalid jsonstring :) Is this useful? We're just testing jackson here
        //invalidTestStrings.add("some weird stuff");
        // Strings instead of coordinates
        invalidTestStrings.add("\"type\": \"Point\", \"coordinates\": [\"a\", \"b\"] }");

        // Each of the above should result in an Exception being thrown, nothing else!
        for (String s : invalidTestStrings) {
            try {
                assembler.fromTransferObject(mapper.readValue(s, PointTo.class));
                Assert.fail("Following json is invalid for a point and should not parse: " + s);
            } catch (JsonMappingException e) {
                // Ok! Json string cannot be parsed
            } catch (IllegalArgumentException e) {
                // Ok! Json string can be parsed but not converted to a geometry because the geojson TO is not valid
            } catch (Exception other) {
                Assert.fail("No other exception expected!");
            }
        }
        try {
            // Valid point. It is not a linestring however, so even if we tell the parser to deserialize as a
            // linestring, a point should be returned
            org.geolatte.geom.Geometry t = assembler.fromTransferObject((GeoJsonTo) mapper.readValue("{ \"type\": \"Point\", \"coordinates\": [100.0, 0.0] }", LineStringTo.class));
            Assert.assertTrue(t instanceof org.geolatte.geom.Point);
        } catch (Exception e) {
            Assert.fail("No exception expected");
        }
    }

    /**
     * Test valid linestring json and their deserialization
     *
     * @throws java.io.IOException if an unexpected exception is thrown (which would fail the test)
     */
    @Test
    public void testValidLineStringDeserialization() throws IOException {
        String validLinestring = "{ \"type\": \"LineString\",\n" +
                "  \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]\n" +
                "  }";
        String validLineStringLambert = "{ \"crs\": {\n" +
                "  \"type\": \"name\",\n" +
                "  \"properties\": {\n" +
                "    \"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"\n" +
                "    }\n" +
                "  }, \"type\": \"LineString\",\n" +
                "  \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]\n" +
                "  }";
        LineString ls = (LineString) JTS.to(assembler.fromTransferObject(mapper.readValue(validLinestring, LineStringTo.class)));
        Assert.assertEquals(CrsId.UNDEFINED, CrsId.valueOf(ls.getSRID()));
        Coordinate[] c = ls.getCoordinates();
        Assert.assertEquals(100.0, c[0].x, ACCURACY);
        Assert.assertEquals(0.0, c[0].y, ACCURACY);
        Assert.assertEquals(101.0, c[1].x, ACCURACY);
        Assert.assertEquals(1.0, c[1].y, ACCURACY);

        ls = (LineString) JTS.to(assembler.fromTransferObject(mapper.readValue(validLineStringLambert, LineStringTo.class)));
        Assert.assertEquals(LAMBERT72.getCode(), ls.getSRID());
        c = ls.getCoordinates();
        Assert.assertEquals(100.0, c[0].x, ACCURACY);
        Assert.assertEquals(0.0, c[0].y, ACCURACY);
        Assert.assertEquals(101.0, c[1].x, ACCURACY);
        Assert.assertEquals(1.0, c[1].y, ACCURACY);
    }

    /**
     * Test invalid variations of a json representing a linestring
     *
     * @throws java.io.IOException If an unexpected exception is thrown in the tests (this causes the test to fail)
     */
    @Test
    public void testInvalidLineStringDeserialization() throws IOException {
        List<String> invalidTestStrings = new ArrayList<String>();
        // Unexisting type
        invalidTestStrings.add("{ \"type\": \"LinString\", \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]  }");
        // Wrong type
        invalidTestStrings.add("{ \"type\": \"Point\", \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]  }");
        // No type
        invalidTestStrings.add("{ \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]  }");
        // Invalid crs name
        invalidTestStrings.add("{ \"crs\": {\n" +
                "  \"type\": \"name\",\n" +
                "  \"properties\": {\n" +
                "    \"name\": \"urn:EPSG:7.6:31370\"\n" +
                "    }\n" +
                "  },  \"type\": \"LineString\", \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]  }");
        // Empty crs name
        invalidTestStrings.add("{ \"crs\": {}\n" +
                "  }, \"type\": \"LineString\", \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]  }");
        // Invalid coordinate array
        invalidTestStrings.add("{ \"type\": \"LineString\", \"coordinates\": [[ [100.0, 0.0], [101.0, 1.0] ]]  }");
        invalidTestStrings.add("{ \"type\": \"LineString\", \"coordinates\": [100.0, 0.0]  }");
        invalidTestStrings.add("{ \"type\": \"LineString\", \"coordinates\": [ [100.0, 0.0]]  }");
        // Same point
        invalidTestStrings.add("{ \"type\": \"LinString\", \"coordinates\": [ [100.0, 0.0], [100.0, 0.0] ]  }");
        invalidTestStrings.add("{ \"type\": \"LinString\", \"coordinates\": [ [100.0, 0.0], [100.0, 1.0], [100.0, 1.0] ]  }");
        // Only one value as a coordinate
        invalidTestStrings.add("{ \"type\": \"LineString\", \"coordinates\": [[ [100.0], [101.0, 1.0] ]]  }");
        // No coordinate array
        invalidTestStrings.add("{ \"type\": \"LineString\", \"coordinates\": []  }");
        invalidTestStrings.add("{ \"type\": \"LineString\" }");
        // Just an invalid jsonstring :)
        invalidTestStrings.add("some weird stuff");
        // Strings instead of coordinates
        invalidTestStrings.add("\"type\": \"Point\", \"coordinates\": [[\"a\", \"b\"],[\"a\", \"b\"]] }");

        // Each of the above should result in a JsonException being thrown, nothing else!
        for (String s : invalidTestStrings) {
            try {
                JTS.to(assembler.fromTransferObject(mapper.readValue(s, LineStringTo.class)));
                Assert.fail("Following json is invalid for a point and should not parse: " + s);
            } catch (JsonProcessingException e) {
                // OK!
            } catch (IllegalArgumentException e) {
                // OK!
            }
            catch (Exception e) {
                Assert.fail("Wrong exception type thrown.");
            }
        }
        // Valid linestring. Should ignore the given deserialize target
        String valid = "{ \"type\": \"LineString\", \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]  }";
        org.geolatte.geom.Geometry t = assembler.fromTransferObject((GeoJsonTo) mapper.readValue(valid, PointTo.class));
        Assert.assertTrue(t instanceof org.geolatte.geom.LineString);
    }


    /**
     * Tests valid polygon jsons
     *
     * @throws java.io.IOException If an unexptected exception was thrown (this fails the test)
     */
    @Test
    public void testValidPolygonDeserialization() throws IOException {
        String noHoles = "{ \"type\": \"Polygon\", \"coordinates\": [[ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ]]}";
        String withHolesInLambert = "{ \"crs\": {\n" +
                "  \"type\": \"name\",\n" +
                "  \"properties\": {\n" +
                "    \"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"\n" +
                "    }\n" +
                "  }, \"type\": \"Polygon\",  \"coordinates\": [[ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ],[ [100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2] ]    ] }";
        Polygon p = (Polygon) JTS.to(assembler.fromTransferObject(mapper.readValue(noHoles, PolygonTo.class)));
        Assert.assertEquals(CrsId.UNDEFINED, CrsId.valueOf(p.getSRID()));
        Assert.assertEquals(0, p.getNumInteriorRing());
        LineString ls = p.getExteriorRing();
        Assert.assertEquals(CrsId.UNDEFINED, CrsId.valueOf(ls.getSRID()));
        Coordinate[] coords = ls.getCoordinates();
        Assert.assertEquals(5, coords.length);
        Assert.assertEquals(coords[0], coords[4]);

        p = (Polygon) JTS.to(assembler.fromTransferObject(mapper.readValue(withHolesInLambert, PolygonTo.class)));
        Assert.assertEquals(LAMBERT72.getCode(), p.getSRID());
        Assert.assertEquals(1, p.getNumInteriorRing());
        ls = p.getExteriorRing();
        Assert.assertEquals(LAMBERT72.getCode(), ls.getSRID());
        coords = ls.getCoordinates();
        Assert.assertEquals(5, coords.length);
        Assert.assertEquals(coords[0], coords[4]);
        ls = p.getInteriorRingN(0);
        Assert.assertEquals(LAMBERT72.getCode(), ls.getSRID());
        coords = ls.getCoordinates();
        Assert.assertEquals(5, coords.length);
        Assert.assertEquals(coords[0], coords[4]);
    }

    /**
     * All kinds of invalid json variations regarding a polygon
     *
     * @throws java.io.IOException If an unexpected exception is thrown (this will fail the test)
     */
    @Test
    public void testInvalidPolygonDeserialization() throws IOException {
        List<String> invalidTestStrings = new ArrayList<String>();
        // Unexisting type
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Polgon\",  \"coordinates\": [[ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ], [ [100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2] ]    ] }");
        // Wrong type
        invalidTestStrings.add("{ \"type\": \"Point\", \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]  }");
        // No type
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"coordinates\": [[ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ], [ [100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2] ]    ] }");
        // Invalid crs name
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:EPSG:7.6:31370\"}}, \"type\": \"Polygon\",  \"coordinates\": [[ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ], [ [100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2] ]    ] }");
        // Empty crs name
        invalidTestStrings.add("{ \"crs\": {}, \"type\": \"Polygon\",  \"coordinates\": [[ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ], [ [100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2] ]    ] }");
        // Invalid coordinate array
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Polygon\",  \"coordinates\": [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ,  [100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]     ] }");
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Polygon\",  \"coordinates\": [[], [ [100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2] ]    ] }");
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Polygon\",  \"coordinates\": [[[ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ], [ [100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2] ]    ]]] }");
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Polygon\",  \"coordinates\": [[ [100.0, 0.0, 101.0, 0.0, 101.0, 1.0, 100.0, 1.0, 100.0, 0.0 ], [ [100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2] ]    ] }");
        // Only one value as a coordinate
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Polygon\",  \"coordinates\": [[ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0] ], [ [100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2] ]    ] }");
        // No coordinate array
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Polygon\",  \"coordinates\": [] }");
        invalidTestStrings.add("{ \"type\": \"Polygon\" }");

        // Each of the above should result in
        // - JsonException if the json is wrong
        // - IllegalargumentException if the json is valid but does not comply to the geoJson standard (the to is notvalid)
        for (String s : invalidTestStrings) {
            try {
                JTS.to(assembler.fromTransferObject(mapper.readValue(s, PolygonTo.class)));
                Assert.fail("Following json is invalid for a point and should not parse: " + s);
            } catch (JsonMappingException e) {
                // Ok! Json string cannot be parsed
            } catch (IllegalArgumentException e) {
                // Ok! Json string can be parsed but not converted to a geometry because the geojson TO is not valid
            }
            catch (Exception e) {
                Assert.fail("Wrong exception thrown.");
            }
        }
        // Valid point but as linestring should fail!
        String s = "{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Polygon\",  \"coordinates\": [[ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ], [ [100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2] ]    ] }";
        org.geolatte.geom.Geometry t = assembler.fromTransferObject((GeoJsonTo) mapper.readValue(s, PointTo.class));
        Assert.assertTrue(t instanceof org.geolatte.geom.Polygon);
    }


    /**
     * Parsing of a valid multipoint
     *
     * @throws java.io.IOException If an unexpected exception is thrown (this will fail the test)
     */
    @Test
    public void validMultiPointDeserialization() throws IOException {
        String testString = "{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},\"type\": \"MultiPoint\",  \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]  }";
        MultiPoint mp = (MultiPoint) JTS.to(assembler.fromTransferObject(mapper.readValue(testString, MultiPointTo.class)));
        Assert.assertEquals(LAMBERT72.getCode(), mp.getSRID());
        Assert.assertEquals(2, mp.getNumPoints());
        Assert.assertEquals(LAMBERT72.getCode(), mp.getGeometryN(0).getSRID());
        Assert.assertEquals(LAMBERT72.getCode(), mp.getGeometryN(1).getSRID());
        Assert.assertTrue(mp.getGeometryN(0) instanceof Point);
        Assert.assertTrue(mp.getGeometryN(1) instanceof Point);
        Assert.assertEquals(100.0, mp.getGeometryN(0).getCoordinate().x, ACCURACY);
        Assert.assertEquals(101.0, mp.getGeometryN(1).getCoordinate().x, ACCURACY);
        Assert.assertEquals(0.0, mp.getGeometryN(0).getCoordinate().y, ACCURACY);
        Assert.assertEquals(1.0, mp.getGeometryN(1).getCoordinate().y, ACCURACY);
    }

    /**
     * Test invalid variations of a json representing a point
     *
     * @throws java.io.IOException If an unexpected exception is thrown (this will fail the test)
     */
    @Test
    public void testInvalidMultiPointDeserialization() throws IOException {
        List<String> invalidTestStrings = new ArrayList<String>();
        // Unexisting type
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},\"type\": \"MuliPoint\",  \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]  }");
        // Wrong type
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},\"type\": \"LineString\",  \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]  }");
        // No type
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]  }");
        // Invalid crs name
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ors:EPSG:7.6:31370\"}},\"type\": \"MultiPoint\",  \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]  }");
        // Empty crs name
        invalidTestStrings.add("{ \"crs\": {},\"type\": \"MultiPoint\",  \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]  }");
        // Invalid coordinate array
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},\"type\": \"MultiPoint\",  \"coordinates\": [ [100.0, 0.0,101.0, 1.0] ]  }");
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},\"type\": \"MultiPoint\",  \"coordinates\": [[ [100.0, 0.0],[101.0, 1.0]] ]  }");
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},\"type\": \"MultiPoint\",  \"coordinates\": [[],[]] }");
        // Only one value as a coordinate
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},\"type\": \"MultiPoint\",  \"coordinates\": [ [100.0], [101.0, 1.0] ]  }");
        // No coordinate array
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},\"type\": \"MultiPoint\" }");
        // Just an invalid jsonstring :)
        invalidTestStrings.add("some weird stuff");
        // Strings instead of coordinates
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},\"type\": \"MultiPoint\",  \"coordinates\": [ [\"a\", 0.0], [101.0, 1.0] ]  }");
        // No points
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},\"type\": \"MultiPoint\",  \"coordinates\": [ ]  }");

        // Each of the above should result in a JsonException being thrown, nothing else!
        int count = 0;
        for (String s : invalidTestStrings) {
            try {
                JTS.to(assembler.fromTransferObject(mapper.readValue(s, MultiPointTo.class)));
                Assert.fail("Following json is invalid for a point and should not parse: " + s);
            } catch (JsonProcessingException e) {
                // Ok!
            } catch(IllegalArgumentException e) {
                // Ok when json is valid but not valid geoJson
            } catch (ClassCastException e) {
                // Ok, for case 2 since there is no difference between a multipoint and a linestring on geojson level except for the type, so
                // if you misspecify the type, all deserialization will be ok except for the case at the end
                count++;
            }
        }
        Assert.assertEquals(count, 1);
        // Valid multipoint but as linestring should fail!
        String s = "{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},\"type\": \"MultiPoint\",  \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]  }";
        org.geolatte.geom.Geometry t = assembler.fromTransferObject((GeoJsonTo) mapper.readValue(s, LineStringTo.class));
        Assert.assertTrue(t instanceof org.geolatte.geom.MultiPoint);
    }


    /**
     * Test valid multilinestring deserialization
     *
     * @throws java.io.IOException If an unexpected exception would be thrown (this will fail the tests)
     */
    @Test
    public void testValidMultiLineString() throws IOException {
        String multiLineString = "{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"MultiLineString\", \"coordinates\": [[ [100.0, 0.0], [101.0, 1.0] ], [ [102.0, 2.0], [103.0, 3.0] ] ]}";
        MultiLineString result = (MultiLineString) JTS.to(assembler.fromTransferObject(mapper.readValue(multiLineString, MultiLineStringTo.class)));
        Assert.assertEquals(LAMBERT72.getCode(), result.getSRID());
        Assert.assertEquals(2, result.getNumGeometries());
        com.vividsolutions.jts.geom.Geometry first = result.getGeometryN(0);
        com.vividsolutions.jts.geom.Geometry second = result.getGeometryN(1);
        Assert.assertTrue(first instanceof LineString);
        Assert.assertTrue(second instanceof LineString);
        Coordinate[] firstCoords = first.getCoordinates();
        Coordinate[] secondCoords = second.getCoordinates();
        Assert.assertEquals(2, firstCoords.length);
        Assert.assertEquals(100.0, firstCoords[0].x, ACCURACY);
        Assert.assertEquals(101.0, firstCoords[1].x, ACCURACY);
        Assert.assertEquals(0.0, firstCoords[0].y, ACCURACY);
        Assert.assertEquals(1.0, firstCoords[1].y, ACCURACY);
        Assert.assertEquals(2, secondCoords.length);
        Assert.assertEquals(102.0, secondCoords[0].x, ACCURACY);
        Assert.assertEquals(103.0, secondCoords[1].x, ACCURACY);
        Assert.assertEquals(2.0, secondCoords[0].y, ACCURACY);
        Assert.assertEquals(3.0, secondCoords[1].y, ACCURACY);
    }

    /**
     * Test invalid variations of a json representing a multilinestring
     *
     * @throws java.io.IOException If an unexpected exception is thrown (fails the test)
     */
    @Test
    public void testInvalidMultiLineStringDeserialization() throws IOException {
        List<String> invalidTestStrings = new ArrayList<String>();
        // Unexisting type
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},  \"type\": \"MuliLineString\", \"coordinates\": [[ [100.0, 0.0], [101.0, 1.0] ], [ [102.0, 2.0], [103.0, 3.0] ] ]}");
        // Wrong type
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},  \"type\": \"LineString\", \"coordinates\": [[ [100.0, 0.0], [101.0, 1.0] ], [ [102.0, 2.0], [103.0, 3.0] ] ]}");
        // No type
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, {\"coordinates\": [[ [100.0, 0.0], [101.0, 1.0] ], [ [102.0, 2.0], [103.0, 3.0] ] ]}");
        // Invalid crs name
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogcEPSG:7.6:31370\"}},  \"type\": \"MultiLineString\", \"coordinates\": [[ [100.0, 0.0], [101.0, 1.0] ], [ [102.0, 2.0], [103.0, 3.0] ] ]}");
        // Empty crs name
        invalidTestStrings.add("{ \"crs\": {}, { \"type\": \"MultiLineString\", \"coordinates\": [[ [100.0, 0.0], [101.0, 1.0] ], [ [102.0, 2.0], [103.0, 3.0] ] ]}");
        // Invalid coordinate array
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},  \"type\": \"MultiLineString\", \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ,  [102.0, 2.0], [103.0, 3.0]  ]}");
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},  \"type\": \"MultiLineString\", \"coordinates\": [[[ [100.0, 0.0], [101.0, 1.0] ], [ [102.0, 2.0], [103.0, 3.0] ] ]]}");
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},  \"type\": \"MultiLineString\", \"coordinates\": [[],[]]}");
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},  \"type\": \"MultiLineString\", \"coordinates\": []}");
        // Only one value as a coordinate
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},  \"type\": \"MultiLineString\", \"coordinates\": [[ [100.0], [101.0, 1.0,3.0] ], [ [102.0, 2.0], [103.0, 3.0] ] ]}");
        // No coordinate array
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},  \"type\": \"MultiLineString\"}");
        // Just an invalid jsonstring :)
        invalidTestStrings.add("some weird stuff");
        // Strings instead of coordinates
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},  \"type\": \"MultiLineString\", \"coordinates\": [[ [100.0, 0.0], [101.0, 1.0,3.0] ], [ [\"a\", 2.0], [103.0, 3.0] ] ]}");

        // Each of the above should result in a JsonException being thrown, nothing else!
        for (String s : invalidTestStrings) {
            try {
                JTS.to(assembler.fromTransferObject(mapper.readValue(s, MultiLineStringTo.class)));
                Assert.fail("Following json is invalid for a MultiLineString and should not parse: " + s);
            } catch (JsonProcessingException e) {
                // Ok
            } catch (IllegalArgumentException e) {
                // Ok if valid json but not geoJson
            }
        }
        // Valid multilinestring, must remain multilinestring even if linestringto is specified
        String s = "{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"MultiLineString\", \"coordinates\": [[ [100.0, 0.0], [101.0, 1.0] ], [ [102.0, 2.0], [103.0, 3.0] ] ]}";
        org.geolatte.geom.Geometry t = assembler.fromTransferObject((GeoJsonTo) mapper.readValue(s, LineStringTo.class));
        Assert.assertTrue(t instanceof org.geolatte.geom.MultiLineString);
    }


    /**
     * Tests a valid multipolygon deserialization
     *
     * @throws java.io.IOException When an unexpected exception is thrown (fails the test)
     */
    @Test
    public void testMultiPolygon() throws IOException {
        String testString = "{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"MultiPolygon\", \"coordinates\": [    [[[102.0, 2.0], [103.0, 2.0], [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]]],    [[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]],     [[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]    ]}";
        MultiPolygon result = (MultiPolygon) JTS.to(assembler.fromTransferObject(mapper.readValue(testString, MultiPolygonTo.class)));
        Assert.assertEquals(LAMBERT72.getCode(), result.getSRID());
        Assert.assertEquals(2, result.getNumGeometries());
        com.vividsolutions.jts.geom.Geometry first = result.getGeometryN(0);
        com.vividsolutions.jts.geom.Geometry second = result.getGeometryN(1);
        Assert.assertTrue(first instanceof Polygon);
        Assert.assertTrue(second instanceof Polygon);

        Polygon polygon = (Polygon) first;
        Assert.assertEquals(LAMBERT72.getCode(), polygon.getSRID());
        Assert.assertEquals(0, polygon.getNumInteriorRing());
        LineString ls = polygon.getExteriorRing();
        Assert.assertEquals(LAMBERT72.getCode(), ls.getSRID());
        Coordinate[] coords = ls.getCoordinates();
        Assert.assertEquals(5, coords.length);
        Assert.assertEquals(coords[0], coords[4]);

        polygon = (Polygon) second;
        Assert.assertEquals(LAMBERT72.getCode(), polygon.getSRID());
        Assert.assertEquals(1, polygon.getNumInteriorRing());
        ls = polygon.getExteriorRing();
        Assert.assertEquals(LAMBERT72.getCode(), ls.getSRID());
        coords = ls.getCoordinates();
        Assert.assertEquals(5, coords.length);
        Assert.assertEquals(coords[0], coords[4]);
        ls = polygon.getInteriorRingN(0);
        Assert.assertEquals(LAMBERT72.getCode(), ls.getSRID());
        coords = ls.getCoordinates();
        Assert.assertEquals(5, coords.length);
        Assert.assertEquals(coords[0], coords[4]);
    }

    /**
     * Tests variations of invalid multipolygons
     */
    @Test
    public void testInvalidMultiPolygon() throws IOException {
        List<String> invalidTestStrings = new ArrayList<String>();
        // Unexisting type
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Multiolygon\", \"coordinates\": [    [[[102.0, 2.0], [103.0, 2.0], [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]]],    [[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]],     [[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]    ]}");
        // Wrong type
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"LineString\", \"coordinates\": [    [[[102.0, 2.0], [103.0, 2.0], [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]]],    [[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]],     [[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]    ]}");
        // No type
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},  \"coordinates\": [    [[[102.0, 2.0], [103.0, 2.0], [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]]],    [[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]],     [[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]    ]}");
        // Invalid crs name
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:rs:EPSG:7.6:31370\"}}, \"type\": \"MultiPolygon\", \"coordinates\": [    [[[102.0, 2.0], [103.0, 2.0], [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]]],    [[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]],     [[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]    ]}");
        // Empty crs name
        invalidTestStrings.add("{ \"crs\": {}, \"type\": \"MultiPolygon\", \"coordinates\": [    [[[102.0, 2.0], [103.0, 2.0], [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]]],    [[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]],     [[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]    ]}");
        // Invalid coordinate array
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"MultiPolygon\", \"coordinates\": [    [],    [[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]],     [[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]    ]}");
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"MultiPolygon\", \"coordinates\": [    ]}");
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"MultiPolygon\", \"coordinates\": [    [[[102.0, 2.0]]],    [[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]],     [[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]    ]}");

        // Only one value as a coordinate
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"MultiPolygon\", \"coordinates\": [    [[[102.0, 2.0], [103.0, 2.0], [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]]],    [[[100.0, 0.0], [101.0, 0.0], [101.0], [100.0, 1.0], [100.0, 0.0]],     [[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]    ]}");
        // No coordinate array
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"MultiPolygon\"}");
        // Just an invalid jsonstring :)
        invalidTestStrings.add("some weird stuff");
        // Strings instead of coordinates
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"MultiPolygon\", \"coordinates\": [    [[[102.0, 2.0], [103.0, 2.0], [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]]],    [[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [\"a\", 0.0]],     [[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]    ]}");

        // Each of the above should result in a JsonException being thrown, nothing else!
        for (String s : invalidTestStrings) {
            try {
                JTS.to(assembler.fromTransferObject(mapper.readValue(s, MultiPolygonTo.class)));
                Assert.fail("Following json is invalid for a MultiPolygon and should not parse: " + s);
            } catch (JsonProcessingException e) {
                // Ok
            } catch (IllegalArgumentException e) {
                // Ok if valid json but not geojson
            }
        }
        // Valid multipolygon, must remain multipolygon even if linestringto is specified
        String s = "{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"MultiPolygon\", \"coordinates\": [    [[[102.0, 2.0], [103.0, 2.0], [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]]],    [[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]],     [[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]    ]}";
        org.geolatte.geom.Geometry t = assembler.fromTransferObject((GeoJsonTo) mapper.readValue(s, LineStringTo.class));
        Assert.assertTrue(t instanceof org.geolatte.geom.MultiPolygon);
    }

    /**
     * Tests a valid geometrycollection deserialization
     *
     * @throws java.io.IOException If an unexpected exception were thrown (this will fail the test)
     */
    @Test
    public void testGeometryCollection() throws IOException {
        String testGeomCollection = "{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"GeometryCollection\",   \"geometries\": [    { \"type\": \"Point\",      \"coordinates\": [100.0, 0.0]      },    { \"type\": \"LineString\",      \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ]      }  ]}";
        GeometryCollection geomCol = (GeometryCollection) JTS.to(assembler.fromTransferObject(mapper.readValue(testGeomCollection, GeometryCollectionTo.class)));
        Assert.assertEquals(LAMBERT72.getCode(), geomCol.getSRID());
        Assert.assertEquals(2, geomCol.getNumGeometries());
        Assert.assertTrue(geomCol.getGeometryN(0) instanceof Point);
        Assert.assertTrue(geomCol.getGeometryN(1) instanceof LineString);

        // Let's nest soem geometrycollections :)
        String nestedGeomCol = "{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},  \"type\": \"GeometryCollection\",   \"geometries\": [    { \"type\": \"GeometryCollection\",   \"geometries\": [    { \"type\": \"Point\",      \"coordinates\": [100.0, 0.0]      },    { \"type\": \"LineString\",      \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ]      }  ]},    { \"type\": \"LineString\",      \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ]      }  ]}";
        geomCol = (GeometryCollection) JTS.to(assembler.fromTransferObject(mapper.readValue(nestedGeomCol, GeometryCollectionTo.class)));
        Assert.assertEquals(LAMBERT72.getCode(), geomCol.getSRID());
        Assert.assertEquals(2, geomCol.getNumGeometries());
        Assert.assertTrue(geomCol.getGeometryN(0) instanceof GeometryCollection);
        Assert.assertEquals(2, geomCol.getGeometryN(0).getNumGeometries());
        Assert.assertTrue(geomCol.getGeometryN(1) instanceof LineString);
    }

    /**
     * Tests various invalid configurations of geometrycollection
     */
    @Test
    public void testInvalidGeometryCollection() throws IOException {
        List<String> invalidTestStrings = new ArrayList<String>();
        // Unexisting type
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"GeometryCllection\",   \"geometries\": [    { \"type\": \"Point\",      \"coordinates\": [100.0, 0.0]      },    { \"type\": \"LineString\",      \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ]      }  ]}");
        // Wrong type
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"LineString\",   \"geometries\": [    { \"type\": \"Point\",      \"coordinates\": [100.0, 0.0]      },    { \"type\": \"LineString\",      \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ]      }  ]}");
        // CRS Overridden in child is not allowed.
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"GeometryCollection\",   \"geometries\": [    { \"type\": \"Point\",   \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},   \"coordinates\": [100.0, 0.0]      },    { \"type\": \"LineString\",      \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ]      }  ]}");
        // Not even if it was not specified in the parent!
        invalidTestStrings.add("{  \"type\": \"GeometryCollection\",   \"geometries\": [    { \"type\": \"Point\",  \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},   \"coordinates\": [100.0, 0.0]      },    { \"type\": \"LineString\",      \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ]      }  ]}");

        // No type
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},   \"geometries\": [    { \"type\": \"Point\",      \"coordinates\": [100.0, 0.0]      },    { \"type\": \"LineString\",      \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ]      }  ]}");
        // Invalid crs name
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def.6:31370\"}}, \"type\": \"GeometryCollection\",   \"geometries\": [    { \"type\": \"Point\",      \"coordinates\": [100.0, 0.0]      },    { \"type\": \"LineString\",      \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ]      }  ]}");
        // Empty crs name
        invalidTestStrings.add("{ \"crs\": {}, \"type\": \"GeometryCollection\",   \"geometries\": [    { \"type\": \"Point\",      \"coordinates\": [100.0, 0.0]      },    { \"type\": \"LineString\",      \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ]      }  ]}");
        // Missing geometries parameter
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"GeometryCollection\"}");
        // Invalid geometries array structure
        invalidTestStrings.add("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"GeometryCollection\",   \"geometries\": [[    { \"type\": \"Point\",      \"coordinates\": [100.0, 0.0]      },    { \"type\": \"LineString\",      \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ]      }  ]]}");
        // Just an invalid jsonstring :) Is this useful?
        // invalidTestStrings.add("some weird stuff");

        // Each of the above should result in a JsonException being thrown, nothing else!
        for (String s : invalidTestStrings) {
            try {
                JTS.to(assembler.fromTransferObject(mapper.readValue(s, GeometryCollectionTo.class)));
                Assert.fail("Following json is invalid for a GeometryCollection and should not parse: " + s);
            } catch (JsonProcessingException e) {
                // Ok!
            } catch (IllegalArgumentException e) {
                // Ok if valid json but not geojson
            } catch (ClassCastException e) {
                // Ok for case 2 where type is linestring
            }
        }

        // Valid geometrycollection, must remain geometrycollection even if linestringto is specified
        String s = "{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"GeometryCollection\",   \"geometries\": [    { \"type\": \"Point\",      \"coordinates\": [100.0, 0.0]      },    { \"type\": \"LineString\",      \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ]      }  ]}";
        org.geolatte.geom.Geometry t = assembler.fromTransferObject((GeoJsonTo) mapper.readValue(s, LineStringTo.class));
        Assert.assertTrue(t instanceof org.geolatte.geom.GeometryCollection);
    }
}
