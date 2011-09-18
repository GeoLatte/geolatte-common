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

import com.vividsolutions.jts.geom.*;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

/**
 * This is just a dumb class which is simply added so that users of the geolatte serializer/deserializer for geojson
 * can do something as: ... provider.findValueSerializer(Geometry.class) inside their own serializers without needing to
 * know what kind of geometry they have.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 23-sep-2010<br>
 * <i>Creation-Time</i>: 16:40:24<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class AnyGeometrySerializer extends JsonSerializer<Geometry> {
    public AnyGeometrySerializer(JsonMapper jsonMapper) {
    }

    @Override
    public void serialize(Geometry value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        if (value instanceof Point) {
            JsonSerializer<Object> jser = provider.findValueSerializer(Point.class);
            jser.serialize(value, jgen, provider);
        } else if (value instanceof MultiPoint) {
            JsonSerializer<Object> jser = provider.findValueSerializer(MultiPoint.class);
            jser.serialize(value, jgen, provider);
        } else if (value instanceof LineString) {
            JsonSerializer<Object> jser = provider.findValueSerializer(LineString.class);
            jser.serialize(value, jgen, provider);
        } else if (value instanceof MultiLineString) {
            JsonSerializer<Object> jser = provider.findValueSerializer(MultiLineString.class);
            jser.serialize(value, jgen, provider);
        } else if (value instanceof Polygon) {
            JsonSerializer<Object> jser = provider.findValueSerializer(Polygon.class);
            jser.serialize(value, jgen, provider);
        } else if (value instanceof MultiPolygon) {
            JsonSerializer<Object> jser = provider.findValueSerializer(MultiPolygon.class);
            jser.serialize(value, jgen, provider);
        } else if (value instanceof GeometryCollection) {
            JsonSerializer<Object> jser = provider.findValueSerializer(GeometryCollection.class);
            jser.serialize(value, jgen, provider);
        }
    }
}
