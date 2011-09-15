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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import junit.framework.Assert;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.geolatte.common.dataformats.json.to.*;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.jts.JTS;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Tests the entire sequence from Json string -> To -> Geolatte Geom -> JTS
 * Although the last part is not strictly necessary in this testsuite, i have included it in the testscope since
 * the geojson -> jts sequence is often used by users of the library
 * The actual deserialization from the json string to a geojsonto is done by a standard json serializer (jackson)
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 */
public class JsonDeserializationTest {

    private static GeoJsonToFactory factory;
    private static ObjectMapper mapper;
    private static final double ACCURACY = 0.0000005;
    private static final int WGS84 = 4326;
    private static final int LAMBERT72 = 31370;

    @BeforeClass
    public static void setupSuite() {
        factory = new GeoJsonToFactory();
        mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Test deserialization of a point, with and without crs specification. Both with Point and Geometry as target
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

            Point test = (Point) JTS.to(factory.fromTo(mapper.readValue(testString, PointTo.class)));
            Coordinate c = test.getCoordinate();
            Assert.assertEquals(100.0, c.x, ACCURACY);
            Assert.assertEquals(0.0, c.y, ACCURACY);
            Assert.assertEquals(WGS84, test.getSRID());
            Assert.assertTrue(Double.isNaN(c.z));

            Point test1b = (Point) JTS.to(factory.fromTo(mapper.readValue(testString1b, PointTo.class)));
            Coordinate c1b = test1b.getCoordinate();
            Assert.assertEquals(100.0, c1b.x, ACCURACY);
            Assert.assertEquals(0.0, c1b.y, ACCURACY);
            Assert.assertEquals(WGS84, test1b.getSRID());
            Assert.assertTrue(Double.isNaN(c1b.z));

            Point test2 = (Point) JTS.to(factory.fromTo(mapper.readValue(testString2, PointTo.class)));
            Coordinate c2 = test2.getCoordinate();
            Assert.assertEquals(100.0, c2.x, ACCURACY);
            Assert.assertEquals(0.0, c2.y, ACCURACY);
            Assert.assertEquals(LAMBERT72, test2.getSRID());
            Assert.assertTrue(Double.isNaN(c2.z));

            Point test3 = (Point) JTS.to(factory.fromTo(mapper.readValue(testString3, PointTo.class)));
            Coordinate c3 = test3.getCoordinate();
            Assert.assertEquals(100.0, c3.x, ACCURACY);
            Assert.assertEquals(0.0, c3.y, ACCURACY);
            Assert.assertEquals(LAMBERT72, test3.getSRID());
            Assert.assertTrue(Double.isNaN(c3.z));

            Point test4 = (Point) JTS.to(factory.fromTo(mapper.readValue(testString4, PointTo.class)));
            Coordinate c4 = test4.getCoordinate();
            Assert.assertEquals(100.0, c4.x, ACCURACY);
            Assert.assertEquals(0.0, c4.y, ACCURACY);
            Assert.assertEquals(50.0, c4.z, ACCURACY);
            Assert.assertEquals(LAMBERT72, test4.getSRID());
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
  //      invalidTestStrings.add("{ \"coordinates\": [100.0, 0.0] }");
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
        // Strings instead of coordinates
        invalidTestStrings.add("\"type\": \"Point\", \"coordinates\": [\"a\", \"b\"] }");

        // Each of the above should result in an Exception being thrown, nothing else!
        int count = 0;
        for (String s : invalidTestStrings) {
            try {
                org.geolatte.geom.Geometry test = factory.fromTo(mapper.readValue(s, PointTo.class));
                Assert.fail("Following json is invalid for a point and should not parse: " + s);
            } catch (Exception e) {
                count++;
                System.out.println(e.getClass());// OK!
            }
        }
        Assert.assertEquals(invalidTestStrings.size(), count);
        try
        {
            // Valid point but as linestring should fail!
            Geometry t = factory.fromTo(mapper.readValue("{ \"type\": \"Point\", \"coordinates\": [100.0, 0.0] }", LineStringTo.class));
            Assert.fail("The json is valid, but is not a linestring.");
        } catch (Exception e) {
            // OK!
        }
    }



    /**
     * Retrieves the content from a hierarchical map using a path defined as a string denoting the different accesskeys
     * seperated by a dot. Eg, if called on a map with key a.b.c, the method will retrieve the contents of
     * map.get(a).get(b).get(c). If any of the intermediate structures is not a map, or if any other error prevents
     * the method from returning the requested value, an IOException is thrown.
     *
     * @param path       path to use to retrieve the value
     * @param properties the map of properties to retrieve the value from.
     * @return the value that corresponds with the element situated in a hierarchical map as described above.
     * @throws java.io.IOException if the value could not be retrieved
     */
    private Object getFromMap(String path, Map properties)
            throws IOException {
        if (properties == null || path == null) {
            throw new IOException("Map or path invalid");
        }
        String[] parts = path.split("\\.");
        Map current = properties;
        for (int i = 0; i < parts.length - 1; i++) {
            current = (Map) current.get(parts[i]);
            if (current == null) {
                throw new IOException("Value does not exist!");
            }
        }
        return current.get(parts[parts.length - 1]);
    }


}
