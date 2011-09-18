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

package org.geolatte.common.dataformats.json.jackson;

import com.vividsolutions.jts.geom.Geometry;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.BeanSerializer;
import org.geolatte.common.Feature;
import org.geolatte.common.reflection.EntityClassReader;
import org.geolatte.common.reflection.InvalidObjectReaderException;

import java.io.IOException;
import java.util.HashMap;

/**
 * Generic serializer for a feature. This serializer will serialize all properties of the feature and serialize them
 * seperately according to the serializers present in the system. If for some reason, a part can not be serialized
 * it is skipped from the feature (ie: the serializer will attempt to deliver a json representation of a part of the
 * feature if it can)
 * <p>
 * <i>Creation-Date</i>: 20-apr-2010<br>
 * <i>Creation-Time</i>: 9:17:47<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class FeatureSerializer extends JsonSerializer<Feature> {

    JsonMapper parent;

    public FeatureSerializer(JsonMapper jsonTransformation) {
        parent = jsonTransformation;
    }

    /**
     * Method that can be called to ask implementation to serialize values of type this serializer handles.
     *
     * @param value    Value to serialize; can not be null.
     * @param jgen     Generator used to output resulting Json content
     * @param provider Provider that can be used to get serializers for serializing Objects value contains, if any.
     * @throws java.io.IOException If serialization failed.
     */
    public void serialize(Feature value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("type", "Feature");
        if (value != null) {
            Geometry geom = value.getGeometry();
            if (geom == null) {
                jgen.writeNullField("geometry");
            } else {
                JsonSerializer ser = provider.findValueSerializer(geom.getClass());
                if (ser != null && ser instanceof GeometrySerializer) {
                    parent.increaseDepth();
                    jgen.writeFieldName("geometry");
                    ser.serialize(geom, jgen, provider);
                    parent.decreaseDepth();
                }
            }
            Object id = value.getId();
            if (id != null) {
                // Shouldn't happen, but what can you do?
                jgen.writeObjectField("id", id);
            }

            jgen.writeFieldName("properties");
            jgen.writeStartObject();
            for (String propertyName : value.getProperties()) {
                Object propertyContents = value.getProperty(propertyName);
                if (propertyContents != null) {
                    try {
                        JsonSerializer<Object> ser = provider.findValueSerializer(propertyContents.getClass());
                        if (ser != null) {
                            // Beanserializers have the nasty habit to crash if they don't know how to proceed, leaving
                            // the resulting json in inconsistent state from which it's hard to recover inside a
                            // serialization. We therefore reroute it through the transformer: if it crashes, we just
                            // keep the empty serialization for that specific object instead of invalidating the entire
                            // surrounding json. In addition, they crash with circular references...
                            if (ser instanceof BeanSerializer) {
                                String subPart = parent.recurse(
                                        EntityClassReader.getClassReaderFor(propertyContents.getClass()).asFeature(propertyContents));
                                try {
                                    HashMap subPartMap = parent.getObjectMapper().readValue(subPart, HashMap.class);
                                    jgen.writeFieldName(propertyName);
                                    jgen.writeObject(subPartMap);
                                }
                                catch (IOException ignored) {
                                }
                            } else {
                                jgen.writeFieldName(propertyName);
                                ser.serialize(propertyContents, jgen, provider);
                            }
                        }
                    } catch (JsonMappingException exc) {
                        jgen.writeStartObject();
                        jgen.writeEndObject();
                    } catch (InvalidObjectReaderException e) {
                        // Will not happen!
                    }
                }
            }
            jgen.writeEndObject();
        }
        jgen.writeEndObject();
    }
}
