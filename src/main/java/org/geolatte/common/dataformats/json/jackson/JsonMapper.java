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


import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.*;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.deser.CustomDeserializerFactory;
import org.codehaus.jackson.map.deser.StdDeserializerProvider;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;
import org.codehaus.jackson.type.TypeReference;
import org.geolatte.common.Feature;
import org.geolatte.common.FeatureCollection;
import org.geolatte.geom.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * The JsonMapper is a class that can convert jsonstrings to objects and vice versa, and that can be extended on
 * demand of the user by providing additional serializers or deserializers.
 * <i>Creation-Date</i>: 20-apr-2010<br>
 * <i>Creation-Time</i>: 8:09:55<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class JsonMapper {

    public static final int MAXIMUMDEPTH = 10;
    private ObjectMapper mapper;
    private CustomSerializerFactory serializerFactory;
    private CustomDeserializerFactory deserializerFactory;
    private int depth;
    private boolean insideGeometryCollection;
    private boolean serializeNullValues;
    private boolean ignoreUnknownProperties;

    /**
     * Constructor of the jsonmapper
     *
     * @param serializeNullValues     if set to true, null values are serialized as usual. If set to false, they are not.
     * @param ignoreUnknownProperties if set to true, unknown properties in a json object will be ignored when they can not
     *                                be mapped to a field instead of an exception being thrown.
     */
    public JsonMapper(boolean serializeNullValues, boolean ignoreUnknownProperties) {
        this.serializeNullValues = serializeNullValues;
        this.ignoreUnknownProperties = ignoreUnknownProperties;
        setNewObjectMapper();
        serializerFactory = new CustomSerializerFactory();

        serializerFactory.addGenericMapping(MultiLineString.class, new MultiLineStringSerializer(this));
        serializerFactory.addGenericMapping(LineString.class, new LineStringSerializer(this));
        serializerFactory.addGenericMapping(Point.class, new PointSerializer(this));
        serializerFactory.addGenericMapping(MultiPoint.class, new MultiPointSerializer(this));
        serializerFactory.addGenericMapping(Polygon.class, new PolygonSerializer(this));
        serializerFactory.addGenericMapping(Feature.class, new FeatureSerializer(this));
        serializerFactory.addGenericMapping(MultiPolygon.class, new MultiPolygonSerializer(this));
        serializerFactory.addGenericMapping(Geometry.class, new AnyGeometrySerializer(this));
        serializerFactory.addGenericMapping(GeometryCollection.class, new GeometryCollectionSerializer(this));

        mapper.setSerializerFactory(serializerFactory);
        deserializerFactory = new CustomDeserializerFactory();

        deserializerFactory.addSpecificMapping(Geometry.class, new GeometryDeserializer<Geometry>(this, Geometry.class));
        deserializerFactory.addSpecificMapping(Point.class, new GeometryDeserializer<Point>(this, Point.class));
        deserializerFactory.addSpecificMapping(LineString.class, new GeometryDeserializer<LineString>(this, LineString.class));
        deserializerFactory.addSpecificMapping(MultiPoint.class, new GeometryDeserializer<MultiPoint>(this, MultiPoint.class));
        deserializerFactory.addSpecificMapping(MultiLineString.class, new GeometryDeserializer<MultiLineString>(this, MultiLineString.class));
        deserializerFactory.addSpecificMapping(Polygon.class, new GeometryDeserializer<Polygon>(this, Polygon.class));
        deserializerFactory.addSpecificMapping(MultiPolygon.class, new GeometryDeserializer<MultiPolygon>(this, MultiPolygon.class));
        deserializerFactory.addSpecificMapping(GeometryCollection.class, new GeometryDeserializer<GeometryCollection>(this, GeometryCollection.class));
        deserializerFactory.addSpecificMapping(Feature.class, new FeatureDeserializer(this));
        deserializerFactory.addSpecificMapping(FeatureCollection.class, new FeatureCollectionDeserializer(this));

        StdDeserializerProvider deserProvider = new StdDeserializerProvider(deserializerFactory);
        mapper.setDeserializerProvider(deserProvider);

    }

    /**
     * Default constructor. Maps all values, including null values and will throw exceptions on unknown properties
     */
    public JsonMapper() {
        this(true, false);
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
     * @param classToMap      the class for which the given serializer is meant
     * @param classSerializer the serializer
     * @param <T>             the type of objects that will be serialized by the given serializer
     */
    public <T> void addClassSerializer(Class<? extends T> classToMap, JsonSerializer<T> classSerializer) {
        setNewObjectMapper();
        serializerFactory.addGenericMapping(classToMap, classSerializer);
        mapper.setDeserializerProvider(new StdDeserializerProvider(deserializerFactory));
        mapper.setSerializerFactory(serializerFactory);
    }

    /**
     * Adds a deserializer to this mapper. Allows a user to alter the deserialization behavior for a certain type.
     *
     * @param classToMap      the class for which the given serializer is meant
     * @param classSerializer the serializer
     * @param <T>             the type of objects that will be serialized by the given serializer
     */
    public <T> void addClassDeserializer(Class<T> classToMap, JsonDeserializer<? extends T> classSerializer) {
        setNewObjectMapper();
        deserializerFactory.addSpecificMapping(classToMap, classSerializer);
        mapper.setDeserializerProvider(new StdDeserializerProvider(deserializerFactory));
        mapper.setSerializerFactory(serializerFactory);
    }

    /**
     * We allow retrieval of the mapper for serializers (in this package)
     *
     * @return the objectmapper used by this transformation
     */
    ObjectMapper getObjectMapper() {
        return mapper;
    }

    void increaseDepth() {
        depth++;
    }

    void decreaseDepth() {
        depth--;
    }

    /**
     * Indicates whether a subgeometry is being mapped (used to know whether the CRS property needs to be generated)
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
            mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
            if (ignoreUnknownProperties) {
                mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            }
        }
    }

}
