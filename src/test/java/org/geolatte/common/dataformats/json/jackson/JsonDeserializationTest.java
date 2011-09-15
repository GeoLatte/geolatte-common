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

package org.geolatte.common.dataformats.json.jackson;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import junit.framework.Assert;
import org.geolatte.common.Feature;
import org.geolatte.common.FeatureCollection;
import org.geolatte.common.dataformats.json.jackson.JsonException;
import org.geolatte.common.dataformats.json.jackson.JsonMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Tests the GeoJSON deserialization routines.
 * <p>
 * <i>Creation-Date</i>: 1-sep-2010<br>
 * <i>Creation-Time</i>: 15:36:42<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class JsonDeserializationTest {

    private static final double ACCURACY = 0.0000005;
    private static final int WGS84 = 4326;
    private static final int LAMBERT72 = 31370;
    private JsonMapper mapper;

    @Before
    public void setup() {
        mapper = new JsonMapper();
    }

    /**
     * Test deserialization of a point, with and without crs specification. Both with Point and Geometry as target
     */
    @Test
    public void testvalidPointDeserialization() {
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
        try {
            Point test = mapper.fromJson(testString, Point.class);
            Coordinate c = test.getCoordinate();
            Assert.assertEquals(100.0, c.x, ACCURACY);
            Assert.assertEquals(0.0, c.y, ACCURACY);
            Assert.assertEquals(WGS84, test.getSRID());
            Assert.assertTrue(Double.isNaN(c.z));            

            Point test1b = mapper.fromJson(testString1b, Point.class);
            Coordinate c1b = test1b.getCoordinate();
            Assert.assertEquals(100.0, c1b.x, ACCURACY);
            Assert.assertEquals(0.0, c1b.y, ACCURACY);
            Assert.assertEquals(WGS84, test.getSRID());
            Assert.assertTrue(Double.isNaN(c1b.z));

            Geometry test2 = mapper.fromJson(testString2, Geometry.class);
            Assert.assertTrue(test2 instanceof Point);
            Assert.assertEquals(LAMBERT72, test2.getSRID());
            Coordinate c2 = test2.getCoordinate();
            Assert.assertEquals(100.0, c2.x, ACCURACY);
            Assert.assertEquals(0.0, c2.y, ACCURACY);
            Assert.assertTrue(Double.isNaN(c2.z));

            Geometry test3 = mapper.fromJson(testString3, Point.class);
            Assert.assertEquals(LAMBERT72, test2.getSRID());
            Coordinate c3 = test3.getCoordinate();
            Assert.assertEquals(100.0, c3.x, ACCURACY);
            Assert.assertEquals(0.0, c3.y, ACCURACY);
            Assert.assertTrue(Double.isNaN(c3.z));

            Geometry test4 = mapper.fromJson(testString4, Point.class);
            Assert.assertEquals(LAMBERT72, test4.getSRID());
            Coordinate c4 = test4.getCoordinate();
            Assert.assertEquals(100.0, c4.x, ACCURACY);
            Assert.assertEquals(0.0, c4.y, ACCURACY);
            Assert.assertEquals(50.0, c4.z, ACCURACY);             

        } catch (JsonException e) {
            Assert.fail("No exception should be thrown");
        }

    }

    /**
     * Test invalid variations of a json representing a point
     */
    @Test
    public void testInvalidPointDeserialization(){
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
        // Just an invalid jsonstring :)
        invalidTestStrings.add("some weird stuff");
        // Strings instead of coordiantes
        invalidTestStrings.add("\"type\": \"Point\", \"coordinates\": [\"a\", \"b\"] }");

        // Each of the above should result in a JsonException being thrown, nothing else!
        int count = 0;
        for (String s : invalidTestStrings) {
            try {
                Point test = mapper.fromJson(s, Point.class);
                Assert.fail("Following json is invalid for a point and should not parse: " + s);
            } catch (JsonException e) {
                count++;
            } catch (Exception e) {
                Assert.fail("Only json exceptions should be thrown");
            }
        }
        Assert.assertEquals(invalidTestStrings.size(), count);
        try
        {
            // Valid point but as linestring should fail!
            Geometry t = mapper.fromJson("{ \"type\": \"Point\", \"coordinates\": [100.0, 0.0] }", LineString.class);
            Assert.fail("The json is valid, but is not a linestring.");
        } catch (JsonException e) {
            // OK!
        }
    }

    /**
     * Test valid linestring json and their deserialization
     */
    @Test
    public void testValidLineStringDeserialization() {
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
        try
        {
            LineString ls = mapper.fromJson(validLinestring, LineString.class);
            Assert.assertEquals(WGS84, ls.getSRID());
            Coordinate[] c = ls.getCoordinates();
            Assert.assertEquals(100.0, c[0].x, ACCURACY);
            Assert.assertEquals(0.0, c[0].y, ACCURACY);
            Assert.assertEquals(101.0, c[1].x, ACCURACY);
            Assert.assertEquals(1.0, c[1].y, ACCURACY);

            ls = mapper.fromJson(validLineStringLambert, LineString.class);
            Assert.assertEquals(LAMBERT72, ls.getSRID());
            c = ls.getCoordinates();
            Assert.assertEquals(100.0, c[0].x, ACCURACY);
            Assert.assertEquals(0.0, c[0].y, ACCURACY);
            Assert.assertEquals(101.0, c[1].x, ACCURACY);
            Assert.assertEquals(1.0, c[1].y, ACCURACY);
        } catch (JsonException ex)
        {
            Assert.fail("No exception expected for a valid LineString json.");
        }
    }

    /**
     * Test invalid variations of a json representing a linestring
     */
    @Test
    public void testInvalidLineStringDeserialization(){
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
        // Strings instead of coordiantes
        invalidTestStrings.add("\"type\": \"Point\", \"coordinates\": [[\"a\", \"b\"],[\"a\", \"b\"]] }");

        // Each of the above should result in a JsonException being thrown, nothing else!
        int count = 0;
        for (String s : invalidTestStrings) {
            try {
                LineString test = mapper.fromJson(s, LineString.class);
                Assert.fail("Following json is invalid for a point and should not parse: " + s);
            } catch (JsonException e) {
                count++;
            } catch (Exception e) {
                Assert.fail("Only json exceptions should be thrown");
            }
        }
        Assert.assertEquals(invalidTestStrings.size(), count);
        try
        {
            // Valid point but as linestring should fail!
            Geometry t = mapper.fromJson("{ \"type\": \"LineString\", \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]  }"
                    , Point.class);
            Assert.fail("The json is valid, but is not a Point.");
        } catch (JsonException e) {
            // OK!
        }
    }

    /**
     * Tests valid polygon jsons
     */
    @Test
    public void testValidPolygonDeserialization()
    {
        String noHoles = "{ \"type\": \"Polygon\", \"coordinates\": [[ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ]]}";
        String withHolesInLambert = "{ \"crs\": {\n" +
                "  \"type\": \"name\",\n" +
                "  \"properties\": {\n" +
                "    \"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"\n" +
                "    }\n" +
                "  }, \"type\": \"Polygon\",  \"coordinates\": [[ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ],[ [100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2] ]    ] }";

        try{
            Polygon p = mapper.fromJson(noHoles, Polygon.class);
            Assert.assertEquals(WGS84, p.getSRID());
            Assert.assertEquals(0, p.getNumInteriorRing());
            LineString ls = p.getExteriorRing();
            Assert.assertEquals(WGS84, ls.getSRID());
            Coordinate[] coords = ls.getCoordinates();
            Assert.assertEquals(5, coords.length);
            Assert.assertEquals(coords[0], coords[4]);

            p = mapper.fromJson(withHolesInLambert, Polygon.class);
            Assert.assertEquals(LAMBERT72, p.getSRID());
            Assert.assertEquals(1, p.getNumInteriorRing());
            ls = p.getExteriorRing();
            Assert.assertEquals(LAMBERT72, ls.getSRID());
            coords = ls.getCoordinates();
            Assert.assertEquals(5, coords.length);
            Assert.assertEquals(coords[0], coords[4]);
            ls = p.getInteriorRingN(0);
            Assert.assertEquals(LAMBERT72, ls.getSRID());
            coords = ls.getCoordinates();
            Assert.assertEquals(5, coords.length);
            Assert.assertEquals(coords[0], coords[4]);

        }catch(JsonException je)
        {
            Assert.fail("No exception expected");
        }
    }

    /**
     * All kinds of invalid json variations regarding a polygon
     */
    @Test
    public void testInvalidPolygonDeserialization(){
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
        // Just an invalid jsonstring :)
        invalidTestStrings.add("some weird stuff");

        // Each of the above should result in a JsonException being thrown, nothing else!
        int count = 0;
        for (String s : invalidTestStrings) {
            try {
                Polygon test = mapper.fromJson(s, Polygon.class);
                Assert.fail("Following json is invalid for a point and should not parse: " + s);
            } catch (JsonException e) {
                count++;
            } catch (Exception e) {
                Assert.fail("Only json exceptions should be thrown");
            }
        }
        Assert.assertEquals(invalidTestStrings.size(), count);
        try
        {
            // Valid point but as linestring should fail!
            Geometry t = mapper.fromJson("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Polygon\",  \"coordinates\": [[ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ], [ [100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2] ]    ] }"
                    , Point.class);
            Assert.fail("The json is a valid polygon, but is not a Point.");
        } catch (JsonException e) {
            // OK!
        }
    }


    /**
     * Parsing of a valid multipoint
     */
    @Test
    public void validMultiPointDeserialization()
    {
        String testString = "{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},\"type\": \"MultiPoint\",  \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]  }";
        try
        {
            MultiPoint mp = mapper.fromJson(testString, MultiPoint.class);
            Assert.assertEquals(LAMBERT72, mp.getSRID());
            Assert.assertEquals(2, mp.getNumPoints());
            Assert.assertEquals(LAMBERT72, mp.getGeometryN(0).getSRID());
            Assert.assertEquals(LAMBERT72, mp.getGeometryN(1).getSRID());
            Assert.assertTrue(mp.getGeometryN(0) instanceof Point);
            Assert.assertTrue(mp.getGeometryN(1) instanceof Point);
            Assert.assertEquals(100.0, mp.getGeometryN(0).getCoordinate().x, ACCURACY);
            Assert.assertEquals(101.0, mp.getGeometryN(1).getCoordinate().x, ACCURACY);
            Assert.assertEquals(0.0, mp.getGeometryN(0).getCoordinate().y, ACCURACY);
            Assert.assertEquals(1.0, mp.getGeometryN(1).getCoordinate().y, ACCURACY);             
        }
        catch (JsonException ex)
        {
            Assert.fail("No exception expected");
        }
    }

    /**
     * Test invalid variations of a json representing a point
     */
    @Test
    public void testInvalidMultiPointDeserialization(){
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
                Point test = mapper.fromJson(s, Point.class);
                Assert.fail("Following json is invalid for a point and should not parse: " + s);
            } catch (JsonException e) {
                count++;
            } catch (Exception e) {
                Assert.fail("Only json exceptions should be thrown");
            }
        }
        Assert.assertEquals(invalidTestStrings.size(), count);
        try
        {
            // Valid point but as linestring should fail!
            Geometry t = mapper.fromJson("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},\"type\": \"MultiPoint\",  \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]  }", LineString.class);
            Assert.fail("The json is a valid multipoint but not a linestring.");
        } catch (JsonException e) {
            // OK!
        }
    }


    /**
     * Test valid multilinestring deserialization
     */
    @Test
    public void testMultiLineString()
    {
        String multiLineString = "{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"MultiLineString\", \"coordinates\": [[ [100.0, 0.0], [101.0, 1.0] ], [ [102.0, 2.0], [103.0, 3.0] ] ]}";
        try {
            MultiLineString result= mapper.fromJson(multiLineString, MultiLineString.class);
            Assert.assertEquals(LAMBERT72, result.getSRID());
            Assert.assertEquals(2, result.getNumGeometries());
            Geometry first = result.getGeometryN(0);
            Geometry second = result.getGeometryN(1);
            Assert.assertTrue(first instanceof LineString);
            Assert.assertTrue(second instanceof LineString);
            Coordinate[] firstCoords = first.getCoordinates();
            Coordinate[] secondCoords = second.getCoordinates();
            Assert.assertEquals(2, firstCoords.length);
            Assert.assertEquals(100.0,firstCoords[0].x, ACCURACY);
            Assert.assertEquals(101.0,firstCoords[1].x, ACCURACY);
            Assert.assertEquals(0.0,firstCoords[0].y, ACCURACY);
            Assert.assertEquals(1.0,firstCoords[1].y, ACCURACY);             
            Assert.assertEquals(2, secondCoords.length);
            Assert.assertEquals(102.0,secondCoords[0].x, ACCURACY);
            Assert.assertEquals(103.0,secondCoords[1].x, ACCURACY);
            Assert.assertEquals(2.0,secondCoords[0].y, ACCURACY);
            Assert.assertEquals(3.0,secondCoords[1].y, ACCURACY);
        } catch (JsonException e) {
            Assert.fail("No exception should be thrown deserializing a valid multilinestring");
        }
    }

    /**
     * Test invalid variations of a json representing a multilinestring
     */
    @Test
    public void testInvalidMultiLineStringDeserialization(){
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
        int count = 0;
        for (String s : invalidTestStrings) {
            try {
                MultiLineString test = mapper.fromJson(s, MultiLineString.class);
                Assert.fail("Following json is invalid for a MultiLineString and should not parse: " + s);
            } catch (JsonException e) {
                count++;
            } catch (Exception e) {
                Assert.fail("Only json exceptions should be thrown");
            }
        }
        Assert.assertEquals(invalidTestStrings.size(), count);
        try
        {
            // Valid multilinestring but as linestring should fail!
            Geometry t = mapper.fromJson("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"MultiLineString\", \"coordinates\": [[ [100.0, 0.0], [101.0, 1.0] ], [ [102.0, 2.0], [103.0, 3.0] ] ]}", LineString.class);
            Assert.fail("The json is a valid multilinestring but not a linestring.");
        } catch (JsonException e) {
            // OK!
        }
    }


    /**
     * Tests a valid multipolygon deserialization
     */
    @Test
    public void testMultiPolygon()
    {
        String testString = "{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"MultiPolygon\", \"coordinates\": [    [[[102.0, 2.0], [103.0, 2.0], [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]]],    [[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]],     [[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]    ]}"; 
        try {
            MultiPolygon result = mapper.fromJson(testString, MultiPolygon.class);
            Assert.assertEquals(LAMBERT72, result.getSRID());
            Assert.assertEquals(2, result.getNumGeometries());
            Geometry first = result.getGeometryN(0);
            Geometry second = result.getGeometryN(1);
            Assert.assertTrue(first instanceof Polygon);
            Assert.assertTrue(second instanceof Polygon);

            Polygon polygon = (Polygon) first;
            Assert.assertEquals(LAMBERT72, polygon.getSRID());
            Assert.assertEquals(0, polygon.getNumInteriorRing());
            LineString ls = polygon.getExteriorRing();
            Assert.assertEquals(LAMBERT72, ls.getSRID());
            Coordinate[] coords = ls.getCoordinates();
            Assert.assertEquals(5, coords.length);
            Assert.assertEquals(coords[0], coords[4]);

            polygon = (Polygon) second;
            Assert.assertEquals(LAMBERT72, polygon.getSRID());
            Assert.assertEquals(1, polygon.getNumInteriorRing());
            ls = polygon.getExteriorRing();
            Assert.assertEquals(LAMBERT72, ls.getSRID());
            coords = ls.getCoordinates();
            Assert.assertEquals(5, coords.length);
            Assert.assertEquals(coords[0], coords[4]);
            ls = polygon.getInteriorRingN(0);
            Assert.assertEquals(LAMBERT72, ls.getSRID());
            coords = ls.getCoordinates();
            Assert.assertEquals(5, coords.length);
            Assert.assertEquals(coords[0], coords[4]);

        } catch (JsonException e) {
            Assert.fail("No exception should be thrown for a valid multipolygon.");
        }

    }

    /**
     * Tests variations of invalid multipolygons
     */
    @Test
    public void testInvalidMultiPolygon()
    {
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
        int count = 0;
        for (String s : invalidTestStrings) {
            try {
                MultiPolygon test = mapper.fromJson(s, MultiPolygon.class);
                Assert.fail("Following json is invalid for a MultiPolygon and should not parse: " + s);
            } catch (JsonException e) {
                count++;
            } catch (Exception e) {
                Assert.fail("Only json exceptions should be thrown");
            }
        }
        Assert.assertEquals(invalidTestStrings.size(), count);
        try
        {
            // Valid multilinestring but as linestring should fail!
            Geometry t = mapper.fromJson("{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"MultiPolygon\", \"coordinates\": [    [[[102.0, 2.0], [103.0, 2.0], [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]]],    [[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]],     [[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]    ]}", LineString.class);
            Assert.fail("The json is a valid multipolygon but not a linestring.");
        } catch (JsonException e) {
            // OK!
        }
    }

    /**
     * Tests a valid geometrycollection deserialization
     */
    @Test
    public void testGeometryCollection()
    {
        String testGeomCollection = "{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"GeometryCollection\",   \"geometries\": [    { \"type\": \"Point\",      \"coordinates\": [100.0, 0.0]      },    { \"type\": \"LineString\",      \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ]      }  ]}";
        try {
            GeometryCollection geomCol = mapper.fromJson(testGeomCollection, GeometryCollection.class);
            Assert.assertEquals(LAMBERT72, geomCol.getSRID());
            Assert.assertEquals(2, geomCol.getNumGeometries());
            Assert.assertTrue(geomCol.getGeometryN(0) instanceof Point);
            Assert.assertTrue(geomCol.getGeometryN(1) instanceof LineString);            
        } catch (JsonException e) {
            Assert.fail("No exception expected for a valid geometrycollection specification.");
        }
        // Let's nest soem geometrycollections :)
        String nestedGeomCol = "{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},  \"type\": \"GeometryCollection\",   \"geometries\": [    { \"type\": \"GeometryCollection\",   \"geometries\": [    { \"type\": \"Point\",      \"coordinates\": [100.0, 0.0]      },    { \"type\": \"LineString\",      \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ]      }  ]},    { \"type\": \"LineString\",      \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ]      }  ]}";
        try {
            GeometryCollection geomCol = mapper.fromJson(nestedGeomCol, GeometryCollection.class);
            Assert.assertEquals(LAMBERT72, geomCol.getSRID());
            Assert.assertEquals(2, geomCol.getNumGeometries());
            Assert.assertTrue(geomCol.getGeometryN(0) instanceof GeometryCollection);
            Assert.assertEquals(2, geomCol.getGeometryN(0).getNumGeometries());            
            Assert.assertTrue(geomCol.getGeometryN(1) instanceof LineString);
        } catch (JsonException e) {
            Assert.fail("No exception expected for a valid geometrycollection specification.");
        }


    }

    /**
     * Tests various invalid configurations of geometrycollection
     */
    @Test
    public void testInvalidGeometryCollection()
    {
        List<String> invalidTestStrings = new ArrayList<String>();
        // Unexisting type
        invalidTestStrings.add( "{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"GeometryCllection\",   \"geometries\": [    { \"type\": \"Point\",      \"coordinates\": [100.0, 0.0]      },    { \"type\": \"LineString\",      \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ]      }  ]}");
        // Wrong type
        invalidTestStrings.add( "{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"LineString\",   \"geometries\": [    { \"type\": \"Point\",      \"coordinates\": [100.0, 0.0]      },    { \"type\": \"LineString\",      \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ]      }  ]}");
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
        // Just an invalid jsonstring :)
        invalidTestStrings.add("some weird stuff");

        // Each of the above should result in a JsonException being thrown, nothing else!
        int count = 0;
        for (String s : invalidTestStrings) {
            try {
                GeometryCollection test = mapper.fromJson(s, GeometryCollection.class);
                Assert.fail("Following json is invalid for a GeometryCollection and should not parse: " + s);
            } catch (JsonException e) {
                count++;
            } catch (Exception e) {
                Assert.fail("Only json exceptions should be thrown");
            }
        }
        Assert.assertEquals(invalidTestStrings.size(), count);
        try
        {
            // Valid multilinestring but as linestring should fail!
            Geometry t = mapper.fromJson( "{ \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"GeometryCollection\",   \"geometries\": [    { \"type\": \"Point\",      \"coordinates\": [100.0, 0.0]      },    { \"type\": \"LineString\",      \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ]      }  ]}", LineString.class);
            Assert.fail("The json is a valid geometrycollection but not a linestring.");
        } catch (JsonException e) {
            // OK!
        }
    }


    /**
     * Test a valid feature deserialization
     */
    @Test
    public void testFeatureDeserialization()
    {
        try {
            String testFeature = "{\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Feature\", \"geometry\": {\"type\": \"LineString\", \"coordinates\": [[102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]]}, \"properties\": {\"prop0\": \"value0\",\"prop1\": 0.0 }}";
            Feature f = mapper.fromJson(testFeature, Feature.class);
            Assert.assertNotNull(f.getGeometry());
            Assert.assertNull(f.getId());
            Assert.assertFalse(f.hasId());
            Assert.assertTrue(f.getGeometry() instanceof LineString);
            Assert.assertTrue(f.hasGeometry());
            Assert.assertEquals("value0", f.getProperty("prop0"));
            Assert.assertEquals(0.0, (Double) f.getProperty("prop1"), ACCURACY);
            // With an integer id
            String testFeature2 = "{ \"id\": 125, \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Feature\", \"geometry\": {\"type\": \"LineString\", \"coordinates\": [[102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]]}, \"properties\": {\"prop0\": \"value0\",\"prop1\": 0.0 }}";
            f = mapper.fromJson(testFeature2, Feature.class);
            Assert.assertNotNull(f.getGeometry());
            Assert.assertTrue(f.hasGeometry());
            Assert.assertNotNull(f.getId());
            Assert.assertTrue(f.hasId());
            Assert.assertEquals(125, f.getId());            
            Assert.assertTrue(f.getGeometry() instanceof LineString);
            Assert.assertEquals("value0", f.getProperty("prop0"));
            Assert.assertEquals(0.0, (Double) f.getProperty("prop1"), ACCURACY);
            // With a string id
            String testFeature3 = "{ \"id\": \"125\", \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Feature\", \"geometry\": {\"type\": \"LineString\", \"coordinates\": [[102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]]}, \"properties\": {\"prop0\": \"value0\",\"prop1\": 0.0 }}";
            f = mapper.fromJson(testFeature3, Feature.class);
            Assert.assertNotNull(f.getGeometry());
            Assert.assertNotNull(f.getId());
            Assert.assertTrue(f.hasGeometry());
            Assert.assertTrue(f.hasId());
            Assert.assertEquals("125", f.getId());
            Assert.assertTrue(f.getGeometry() instanceof LineString);
            Assert.assertEquals("value0", f.getProperty("prop0"));
            Assert.assertEquals(0.0, (Double) f.getProperty("prop1"), ACCURACY);
            // With a complex id
            String testFeature4 = "{ \"id\": {\"some\": 10 }, \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Feature\", \"geometry\": {\"type\": \"LineString\", \"coordinates\": [[102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]]}, \"properties\": {\"prop0\": \"value0\",\"prop1\": 0.0 }}";            
            f = mapper.fromJson(testFeature4, Feature.class);
            Assert.assertNotNull(f.getGeometry());
            Assert.assertNotNull(f.getId());
            Assert.assertTrue(f.hasGeometry());
            Assert.assertTrue(f.hasId());
            Assert.assertTrue(f.getId() instanceof Map);
            Assert.assertTrue(((Map) f.getId()).containsKey("some"));
            Assert.assertTrue(f.getGeometry() instanceof LineString);
            Assert.assertEquals("value0", f.getProperty("prop0"));
            Assert.assertEquals(0.0, (Double) f.getProperty("prop1"), ACCURACY);
        } catch (JsonException e) {
            Assert.fail("No exception should be thrown when parsing a valid feature.");
        }
    }

    /**
     * Test all kinds of invalid variants of feature deserializations.
     */
    @Test
    public void testInvalidFeatureDeserializations()
    {
        List<String> invalidTestStrings = new ArrayList<String>();
        // Unexisting type
        invalidTestStrings.add("{\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Feture\", \"geometry\": {\"type\": \"LineString\", \"coordinates\": [[102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]]}, \"properties\": {\"prop0\": \"value0\",\"prop1\": 0.0 }}");
        // Wrong type
        invalidTestStrings.add("{\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"LineString\", \"geometry\": {\"type\": \"LineString\", \"coordinates\": [[102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]]}, \"properties\": {\"prop0\": \"value0\",\"prop1\": 0.0 }}");
        // CRS Overridden in child is not allowed.
        invalidTestStrings.add("{\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Feature\", \"geometry\": {\"type\": \"LineString\", \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"coordinates\": [[102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]]}, \"properties\": {\"prop0\": \"value0\",\"prop1\": 0.0 }}");
        // Not even if it was not specified in the parent!
        invalidTestStrings.add("{\"type\": \"Feature\", \"geometry\": {\"type\": \"LineString\",\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},  \"coordinates\": [[102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]]}, \"properties\": {\"prop0\": \"value0\",\"prop1\": 0.0 }}");

        // No type
        invalidTestStrings.add("{\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"geometry\": {\"type\": \"LineString\", \"coordinates\": [[102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]]}, \"properties\": {\"prop0\": \"value0\",\"prop1\": 0.0 }}");
        // Invalid crs name
        invalidTestStrings.add("{\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:os:EPSG:7.6:31370\"}}, \"type\": \"Feature\", \"geometry\": {\"type\": \"LineString\", \"coordinates\": [[102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]]}, \"properties\": {\"prop0\": \"value0\",\"prop1\": 0.0 }}");
        // Empty crs name
        invalidTestStrings.add("{\"crs\": {}, \"type\": \"Feature\", \"geometry\": {\"type\": \"LineString\", \"coordinates\": [[102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]]}, \"properties\": {\"prop0\": \"value0\",\"prop1\": 0.0 }}");
        // Missing geometries parameter
        invalidTestStrings.add("{\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Feature\", \"properties\": {\"prop0\": \"value0\",\"prop1\": 0.0 }}");
        // Invalid properties parameter
        invalidTestStrings.add("{\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Feature\", \"geometry\": {\"type\": \"LineString\", \"coordinates\": [[102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]]}}");
        // Invalid geometry.
        invalidTestStrings.add("{\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Feature\", \"geometry\": {\"type\": \"LineString\", \"coordinates\": [[102], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]]}}");

        // Just an invalid jsonstring :)
        invalidTestStrings.add("some weird stuff");

        // Each of the above should result in a JsonException being thrown, nothing else!
        int count = 0;
        for (String s : invalidTestStrings) {
            try {
                Feature test = mapper.fromJson(s, Feature.class);
                Assert.fail("Following json is invalid for a Feature and should not parse: " + s);
            } catch (JsonException e) {
                count++;
            } catch (Exception e) {
                Assert.fail("Only json exceptions should be thrown");
            }
        }
        Assert.assertEquals(invalidTestStrings.size(), count);
        try
        {
            // Valid multilinestring but as linestring should fail!
            Geometry t = mapper.fromJson("{\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Feture\", \"geometry\": {\"type\": \"LineString\", \"coordinates\": [[102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]]}, \"properties\": {\"prop0\": \"value0\",\"prop1\": 0.0 }}", LineString.class);
            Assert.fail("The json is a valid Feature but not a linestring.");
        } catch (JsonException e) {
            // OK!
        }
    }


    /**
     * Tests a valid deserialization of a featurecollection
     */
    @Test
    public void testFeatureCollectionDeserialization()
    {
        // testcase taken from the geojson spec site
        String validFeatureCollection = "{\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \t\"type\": \"FeatureCollection\", \"features\": [ { \"type\": \"Feature\", \"geometry\": {\"type\": \"Point\", \"coordinates\": [102.0, 0.5]}, \"properties\": {\"prop0\": \"value0\"}}, { \"type\": \"Feature\", \"geometry\": { \"type\": \"LineString\", \"coordinates\": [ [102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0] ]}, \"properties\": { \"prop0\": \"value0\", \"prop1\": 0.0}}, { \"type\": \"Feature\", \"geometry\": { \"type\": \"Polygon\", \"coordinates\": [ [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ] ]}, \"properties\": { \"prop0\": \"value0\", \"prop1\": {\"this\": \"that\"}}} ]} ";
        try {
            FeatureCollection collection = mapper.fromJson(validFeatureCollection, FeatureCollection.class);
            Assert.assertEquals(3, collection.getFeatures().size());
            Assert.assertTrue(collection.getFeatures().get(0).getGeometry() instanceof Point);
            Assert.assertTrue(collection.getFeatures().get(1).getGeometry() instanceof LineString);
            Assert.assertTrue(collection.getFeatures().get(2).getGeometry() instanceof Polygon);        
        } catch (JsonException e) {
            Assert.fail("No exception should be thrown during the deserialization of a valid featurecollection");
        }
    }

    /**
     * Tests a number of invalid deserializations of featurecollections  
     */
    @Test
    public void invalidFeatureCollectionDeserialization()
    {
        List<String> invalidTestStrings = new ArrayList<String>();
        // Unexisting type
        invalidTestStrings.add("{\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"FeatureCllection\", \"features\": [ { \"type\": \"Feature\", \"geometry\": {\"type\": \"Point\", \"coordinates\": [102.0, 0.5]}, \"properties\": {\"prop0\": \"value0\"}}, { \"type\": \"Feature\", \"geometry\": { \"type\": \"LineString\", \"coordinates\": [ [102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0] ]}, \"properties\": { \"prop0\": \"value0\", \"prop1\": 0.0}}, { \"type\": \"Feature\", \"geometry\": { \"type\": \"Polygon\", \"coordinates\": [ [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ] ]}, \"properties\": { \"prop0\": \"value0\", \"prop1\": {\"this\": \"that\"}}} ]} ");
        // Wrong type
        invalidTestStrings.add("{\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Feature\", \"features\": [ { \"type\": \"Feature\", \"geometry\": {\"type\": \"Point\", \"coordinates\": [102.0, 0.5]}, \"properties\": {\"prop0\": \"value0\"}}, { \"type\": \"Feature\", \"geometry\": { \"type\": \"LineString\", \"coordinates\": [ [102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0] ]}, \"properties\": { \"prop0\": \"value0\", \"prop1\": 0.0}}, { \"type\": \"Feature\", \"geometry\": { \"type\": \"Polygon\", \"coordinates\": [ [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ] ]}, \"properties\": { \"prop0\": \"value0\", \"prop1\": {\"this\": \"that\"}}} ]} ");
        // Featurecollection contains a non-feature :)
        invalidTestStrings.add("{\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"Feature\", \"features\": [ { \"type\": \"Feature\", \"geometry\": {\"type\": \"Point\", \"coordinates\": [102.0, 0.5]}, \"properties\": {\"prop0\": \"value0\"}}, { \"type\": \"Feature\", \"geometry\": { \"type\": \"LineString\", \"coordinates\": [ [102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0] ]}, \"properties\": { \"prop0\": \"value0\", \"prop1\": 0.0}}, { \"type\": \"Polygon\", \"coordinates\": [ [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ] ]} ]} ");
        // CRS Overridden in child is not allowed.
        invalidTestStrings.add("{\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"FeatureCollection\", \"features\": [ { \"type\": \"Feature\", \"geometry\": {\"type\": \"Point\", \"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}},\"coordinates\": [102.0, 0.5]}, \"properties\": {\"prop0\": \"value0\"}}, { \"type\": \"Feature\", \"geometry\": { \"type\": \"LineString\", \"coordinates\": [ [102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0] ]}, \"properties\": { \"prop0\": \"value0\", \"prop1\": 0.0}}, { \"type\": \"Feature\", \"geometry\": { \"type\": \"Polygon\", \"coordinates\": [ [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ] ]}, \"properties\": { \"prop0\": \"value0\", \"prop1\": {\"this\": \"that\"}}} ]} ");
        // Not even if it was not specified in the parent!
        invalidTestStrings.add("{\t\"type\": \"FeatureCollection\", \"features\": [ { \"type\": \"Feature\", \"geometry\": {\"type\": \"Point\",\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"coordinates\": [102.0, 0.5]}, \"properties\": {\"prop0\": \"value0\"}}, { \"type\": \"Feature\", \"geometry\": { \"type\": \"LineString\", \"coordinates\": [ [102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0] ]}, \"properties\": { \"prop0\": \"value0\", \"prop1\": 0.0}}, { \"type\": \"Feature\", \"geometry\": { \"type\": \"Polygon\", \"coordinates\": [ [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ] ]}, \"properties\": { \"prop0\": \"value0\", \"prop1\": {\"this\": \"that\"}}} ]} ");

        // No type
        invalidTestStrings.add("{\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"features\": [ { \"type\": \"Feature\", \"geometry\": {\"type\": \"Point\", \"coordinates\": [102.0, 0.5]}, \"properties\": {\"prop0\": \"value0\"}}, { \"type\": \"Feature\", \"geometry\": { \"type\": \"LineString\", \"coordinates\": [ [102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0] ]}, \"properties\": { \"prop0\": \"value0\", \"prop1\": 0.0}}, { \"type\": \"Feature\", \"geometry\": { \"type\": \"Polygon\", \"coordinates\": [ [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ] ]}, \"properties\": { \"prop0\": \"value0\", \"prop1\": {\"this\": \"that\"}}} ]} ");
        // Invalid crs name
        invalidTestStrings.add("{\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn::EPSG:7.6:31370\"}}, \"type\": \"FeatureCollection\", \"features\": [ { \"type\": \"Feature\", \"geometry\": {\"type\": \"Point\", \"coordinates\": [102.0, 0.5]}, \"properties\": {\"prop0\": \"value0\"}}, { \"type\": \"Feature\", \"geometry\": { \"type\": \"LineString\", \"coordinates\": [ [102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0] ]}, \"properties\": { \"prop0\": \"value0\", \"prop1\": 0.0}}, { \"type\": \"Feature\", \"geometry\": { \"type\": \"Polygon\", \"coordinates\": [ [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ] ]}, \"properties\": { \"prop0\": \"value0\", \"prop1\": {\"this\": \"that\"}}} ]} ");
        // Empty crs name
        invalidTestStrings.add("{\"crs\": {}, \"type\": \"FeatureCollection\", \"features\": [ { \"type\": \"Feature\", \"geometry\": {\"type\": \"Point\", \"coordinates\": [102.0, 0.5]}, \"properties\": {\"prop0\": \"value0\"}}, { \"type\": \"Feature\", \"geometry\": { \"type\": \"LineString\", \"coordinates\": [ [102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0] ]}, \"properties\": { \"prop0\": \"value0\", \"prop1\": 0.0}}, { \"type\": \"Feature\", \"geometry\": { \"type\": \"Polygon\", \"coordinates\": [ [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ] ]}, \"properties\": { \"prop0\": \"value0\", \"prop1\": {\"this\": \"that\"}}} ]} ");
        // Missing features parameter
        invalidTestStrings.add("{\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"FeatureCollection\"} ");
        // Invalid feature in collection
        invalidTestStrings.add("{\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \"type\": \"FeatureCollection\", \"features\": [ { \"type\": \"Feature\", \"geometry\": {\"type\": \"Point\", \"coordinates\": [102.0, 0.5]}, \"properties\": {\"prop0\": \"value0\"}}, { \"type\": \"Feature\", \"geometry\": { \"type\": \"Lining\", \"coordinates\": [ [102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0] ]}, \"properties\": { \"prop0\": \"value0\", \"prop1\": 0.0}}, { \"type\": \"Feature\", \"geometry\": { \"type\": \"Polygon\", \"coordinates\": [ [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ] ]}, \"properties\": { \"prop0\": \"value0\", \"prop1\": {\"this\": \"that\"}}} ]} ");

        // Just an invalid jsonstring :)
        invalidTestStrings.add("some weird stuff");

        // Each of the above should result in a JsonException being thrown, nothing else!
        int count = 0;
        for (String s : invalidTestStrings) {
            try {
                FeatureCollection test = mapper.fromJson(s, FeatureCollection.class);
                Assert.fail("Following json is invalid for a Feature and should not parse: " + s);
            } catch (JsonException e) {
                count++;
            } catch (Exception e) {
                Assert.fail("Only json exceptions should be thrown");
            }
        }
        Assert.assertEquals(invalidTestStrings.size(), count);
        try
        {
            // Valid multilinestring but as linestring should fail!
            Geometry t = mapper.fromJson("{\"crs\": {\"type\": \"name\", \"properties\": {\"name\": \"urn:ogc:def:crs:EPSG:7.6:31370\"}}, \t\"type\": \"FeatureCollection\", \"features\": [ { \"type\": \"Feature\", \"geometry\": {\"type\": \"Point\", \"coordinates\": [102.0, 0.5]}, \"properties\": {\"prop0\": \"value0\"}}, { \"type\": \"Feature\", \"geometry\": { \"type\": \"LineString\", \"coordinates\": [ [102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0] ]}, \"properties\": { \"prop0\": \"value0\", \"prop1\": 0.0}}, { \"type\": \"Feature\", \"geometry\": { \"type\": \"Polygon\", \"coordinates\": [ [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ] ]}, \"properties\": { \"prop0\": \"value0\", \"prop1\": {\"this\": \"that\"}}} ]} ", LineString.class);
            Assert.fail("The json is a valid FeatureCollection but not a linestring.");
        } catch (JsonException e) {
            // OK!
        }
    }


}
