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


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.geolatte.common.Feature;
import org.geolatte.common.FeatureCollection;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.GeometryCollection;
import org.geolatte.geom.LineString;
import org.geolatte.geom.MultiLineString;
import org.geolatte.geom.MultiPoint;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.geolatte.geom.Polygon;
import org.geolatte.geom.crs.CrsId;

/**
 * The JsonMapper is a class that can convert jsonstrings to objects and vice versa, and that can be extended on
 * demand of the user by providing additional serializers or deserializers.
 * <i>Creation-Date</i>: 20-apr-2010<br>
 * <i>Creation-Time</i>: 8:09:55<br>
 * <br>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class JsonMapper {


    private static final CrsId WGS84 = CrsId.valueOf(4326);
    public static final int MAXIMUMDEPTH = 10;
    private ObjectMapper mapper;
    private int depth;
    private CrsId defaultCrsId;
    private boolean insideGeometryCollection;
    private boolean serializeNullValues;
    private boolean ignoreUnknownProperties;

    /**
     * Constructor of the jsonmapper
     *
     * @param defaultCrsId            default Coordinate Referency System to use for {@code Geometry}s.
     * @param serializeNullValues     if set to true, null values are serialized as usual. If set to false, they are not.
     * @param ignoreUnknownProperties if set to true, unknown properties in a json object will be ignored when they can not
     *                                be mapped to a field instead of an exception being thrown.
     */
    public JsonMapper(CrsId defaultCrsId, boolean serializeNullValues, boolean ignoreUnknownProperties) {
        this.defaultCrsId = defaultCrsId;
        this.serializeNullValues = serializeNullValues;
        this.ignoreUnknownProperties = ignoreUnknownProperties;
        setNewObjectMapper();
        mapper.registerModule(new GeolatteCommonModule(this));
    }

    /**
     * Constructor of the jsonmapper using as default Coordinate System WGS84 (EPSG:4326)
     *
     * @param serializeNullValues     if set to true, null values are serialized as usual. If set to false, they are not.
     * @param ignoreUnknownProperties if set to true, unknown properties in a json object will be ignored when they can not
     *                                be mapped to a field instead of an exception being thrown.
     */
    public JsonMapper(boolean serializeNullValues, boolean ignoreUnknownProperties) {
        this(WGS84, serializeNullValues, ignoreUnknownProperties);
    }

    /**
     * Default constructor. Maps all values, including null values and will throw exceptions on unknown properties;
     * default coordinate reference system will be WGS84.
     */
    public JsonMapper() {
        this(WGS84, true, false);
    }

    /**
     * Converts a given object into a JSON String
     *
     * @param input the object to convert into json
     * @return the JSON string corresponding to the given object.
     * @throws JsonException If the object can not be converted to a JSON string.
     */
    public synchronized String toJson(Object input)
            throws JsonException {
        try {
            //   depth = 0;
            return recurse(input);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * Converts a given string into an object of the given class.
     *
     * @param clazz      The class to which the returned object should belong
     * @param jsonString the jsonstring representing the object to be parsed
     * @param <T>        the type of the returned object
     * @return an instantiated object of class T corresponding to the given jsonstring
     * @throws JsonException If deserialization failed or if the object of class T could for some reason not be
     *                       constructed.
     */
    public synchronized <T> T fromJson(String jsonString, Class<T> clazz)
            throws JsonException {
        if (jsonString == null) {
            return null;
        }
        Reader reader = new StringReader(jsonString);
        try {
            return mapper.readValue(reader, clazz);
        } catch (JsonParseException e) {
            throw new JsonException(e.getMessage(), e);
        } catch (JsonMappingException e) {
            throw new JsonException(e.getMessage(), e);
        } catch (IOException e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    /**
     * Converts a JsonString into a corresponding javaobject of the requested type.
     *
     * @param json  the inputstring to be converted to a javaobject
     * @param clazz the class to which the resulting object should belong to
     * @param <T>   the type of the result elements
     * @return An object of type T that corresponds with the given json string.
     * @throws JsonException If something went wrong converting the jsonstring into an object
     */
    public <T> List<T> collectionFromJson(String json, Class<T> clazz) throws JsonException {
        if (json == null) {
            return null;
        }
        try {
            List<T> result = new ArrayList<T>();
            List tempParseResult = (List) mapper.readValue(json, new TypeReference<List<T>>() {
            });
            for (Object temp : tempParseResult) {
                result.add(fromJson(toJson(temp), clazz));
            }
            return result;

        } catch (JsonParseException e) {
            throw new JsonException(e.getMessage(), e);
        } catch (JsonMappingException e) {
            throw new JsonException(e.getMessage(), e);
        } catch (IOException e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    /**
     * @return Internal method that returns the current (recursion)-depth of this in the context of recursion/serialization
     */
    int getDepth() {
        return depth;
    }

    /**
     * This method should only be used by serializers who need recursive calls. It prevents overflow due to circular
     * references and ensures that the maximum depth of different sub-parts is limited
     *
     * @param input the object to serialize
     * @return the jsonserialization of the given object
     * @throws java.io.IOException If serialisation of the object failed.
     */
    protected String recurse(Object input)
            throws IOException {
        increaseDepth();
        if (depth > MAXIMUMDEPTH) {
            decreaseDepth();
            return "{ \"error\": \"maximum serialization-depth reached.\" }";
        }
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, input);
        writer.close();
        decreaseDepth();
        return writer.getBuffer().toString();
    }

    /**
     * Adds a serializer to this mapper. Allows a user to alter the serialization behavior for a certain type.
     *
     * @param classToMap the class to map
     * @param classSerializer the serializer
     * @param <T>             the type of objects that will be serialized by the given serializer
     */
    public <T> void addClassSerializer(Class<? extends T> classToMap, JsonSerializer<T> classSerializer) {
        setNewObjectMapper(); // Is this right, setting a new object mapper on each add operation?
        SimpleModule mod = new SimpleModule("GeolatteCommonModule-" + classSerializer.getClass().getSimpleName());
        mod.addSerializer(classToMap, classSerializer);
        mapper.registerModule(mod);
    }

    /**
     * Adds a deserializer to this mapper. Allows a user to alter the deserialization behavior for a certain type.
     *
     * @param classToMap      the class for which the given serializer is meant
     * @param classDeserializer the serializer
     * @param <T>             the type of objects that will be serialized by the given serializer
     */
    public <T> void addClassDeserializer(Class<T> classToMap, JsonDeserializer<? extends T> classDeserializer) {
        setNewObjectMapper(); // Is this right, setting a new object mapper on each add operation?
        SimpleModule mod = new SimpleModule("GeolatteCommonModule-" + classDeserializer.getClass().getSimpleName());
        mod.addDeserializer(classToMap, classDeserializer);
        mapper.registerModule(mod);
    }

    /**
     * We allow retrieval of the mapper for serializers
     *
     * @return the objectmapper used by this transformation
     */
    public ObjectMapper getObjectMapper() {
        return mapper;
    }

    /**
     * Returns the default {@code CrsId} for this instance
     *
     * @return the {@code CrsId}
     */
    public CrsId getDefaultCrsId() {
        return defaultCrsId;
    }

    void increaseDepth() {
        depth++;
    }

    void decreaseDepth() {
        depth--;
    }

    /**
     * Indicates whether a subgeometry is being mapped (used to know whether the CRS property needs to be generated)
     *
     * @return
     */
    boolean insideGeometryCollection() {
        return insideGeometryCollection;
    }

    void moveInsideGeometryCollection() {
        this.insideGeometryCollection = true;
    }

    void moveOutsideGeometryCollection() {
        this.insideGeometryCollection = false;
    }

    /**
     * Sets a new object mapper, taking into account the original configuration parameters.
     */
    private void setNewObjectMapper() {
        mapper = new ObjectMapper();
        if (!serializeNullValues) {
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            if (ignoreUnknownProperties) {
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            }
        }
    }

}
