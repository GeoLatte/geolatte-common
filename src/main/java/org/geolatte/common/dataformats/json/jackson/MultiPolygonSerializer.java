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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.geolatte.geom.LineString;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.geolatte.geom.Polygon;

import java.io.IOException;

/**
 * Serializer for MultiPolygons according to the geojson specification
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 21-apr-2010<br>
 * <i>Creation-Time</i>: 16:14:51<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class MultiPolygonSerializer extends GeometrySerializer<MultiPolygon> {
    /**
     * @param containingTransformation The containing serializationtransformation.
     */
    public MultiPolygonSerializer(JsonMapper containingTransformation) {
        super(containingTransformation);
    }

    @Override
    protected void writeShapeSpecificSerialization(MultiPolygon value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {


        jgen.writeFieldName("type");
        jgen.writeString("MultiPolygon");
        jgen.writeArrayFieldStart("coordinates");
        // set beanproperty to null since we are not serializing a real property
        JsonSerializer<Object> ser = provider.findValueSerializer(Double.class, null);

        for (int i = 0; i < value.getNumGeometries(); i++) {
            jgen.writeStartArray();
            Polygon currentPolygon = value.getGeometryN(i);
            // Exterior ring
            LineString exterior = currentPolygon.getExteriorRing();
            jgen.writeStartArray();
            for (int j = 0; j < exterior.getNumPoints(); j++) {
                Point point = exterior.getPointN(j);
                jgen.writeStartArray();
                ser.serialize(point.getX(), jgen, provider);
                ser.serialize(point.getY(), jgen, provider);
                jgen.writeEndArray();
            }
            jgen.writeEndArray();
            // Interior rings
            for (int k = 0; k < currentPolygon.getNumInteriorRing(); k++) {
                LineString ml = currentPolygon.getInteriorRingN(k);
                jgen.writeStartArray();
                for (int j = 0; j < ml.getNumPoints(); j++) {
                    Point point = ml.getPointN(j);
                    jgen.writeStartArray();
                    ser.serialize(point.getX(), jgen, provider);
                    ser.serialize(point.getY(), jgen, provider);
                    jgen.writeEndArray();
                }
                jgen.writeEndArray();
            }
            jgen.writeEndArray();
        }
        jgen.writeEndArray();
    }
}
