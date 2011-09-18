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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

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
    protected void writeShapeSpecificSerialization(MultiPolygon value, JsonGenerator jgen, SerializerProvider provider) throws IOException {


        jgen.writeFieldName("type");
        jgen.writeString("MultiPolygon");
        jgen.writeArrayFieldStart("coordinates");
        JsonSerializer<Object> ser = provider.findValueSerializer(Double.class);

        for (int i = 0; i < value.getNumGeometries(); i++) {
            jgen.writeStartArray();
            Polygon currentPolygon = (Polygon) value.getGeometryN(i);
            // Exterior ring
            LineString exterior = currentPolygon.getExteriorRing();
            jgen.writeStartArray();
            for (int j = 0; j < exterior.getNumPoints(); j++) {
                Coordinate coordinate = exterior.getPointN(j).getCoordinate();
                jgen.writeStartArray();
                ser.serialize(coordinate.x, jgen, provider);
                ser.serialize(coordinate.y, jgen, provider);
                jgen.writeEndArray();
            }
            jgen.writeEndArray();
            // Interior rings
            for (int k = 0; k < currentPolygon.getNumInteriorRing(); k++) {
                LineString ml = currentPolygon.getInteriorRingN(k);
                jgen.writeStartArray();
                for (int j = 0; j < ml.getNumPoints(); j++) {
                    Coordinate coordinate = ml.getPointN(j).getCoordinate();
                    jgen.writeStartArray();
                    ser.serialize(coordinate.x, jgen, provider);
                    ser.serialize(coordinate.y, jgen, provider);
                    jgen.writeEndArray();
                }
                jgen.writeEndArray();
            }
            jgen.writeEndArray();
        }
        jgen.writeEndArray();
    }

    @Override
    protected double[] getBboxCoordinates(JsonGenerator jgen, MultiPolygon shape, SerializerProvider provider) {
        // We only check the exterior rings!
        // minX, minY, maxX, maxY
        double[] result = new double[]{Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_VALUE};

        for (int i = 0; i < shape.getNumGeometries(); i++) {
            LineString exterior = ((Polygon) shape.getGeometryN(i)).getExteriorRing();

            for (int j = 0; j < exterior.getNumPoints(); j++) {
                Coordinate coordinate = exterior.getPointN(j).getCoordinate();
                result[0] = Math.min(coordinate.x, result[0]);
                result[1] = Math.min(coordinate.y, result[1]);
                result[2] = Math.max(coordinate.x, result[2]);
                result[3] = Math.max(coordinate.y, result[3]);
            }
        }
        return result;
    }
}
