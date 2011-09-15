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
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

/**
 * Serializer for MultiPoints. Creates a geojson representation of a multipoint
 * geometry.
 * <p>
 * <i>Creation-Date</i>: 20-apr-2010<br>
 * <i>Creation-Time</i>: 9:11:37<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class MultiPointSerializer extends GeometrySerializer<MultiPoint> {
    /**
     * @param containingTransformation The containing serializationtransformation.
     */
    public MultiPointSerializer(JsonMapper containingTransformation) {
        super(containingTransformation);
    }

    /**
     *  Method that can be called to ask implementation to serialize values of type this serializer handles.
     * @param value Value to serialize; can not be null.
     * @param jgen Generator used to output resulting Json content
     * @param provider Provider that can be used to get serializers for serializing Objects value contains, if any.
     * @throws java.io.IOException If serialization failed.
     */
	@Override
	public void writeShapeSpecificSerialization(MultiPoint value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		jgen.writeFieldName( "type");
		jgen.writeString( "MultiPoint");
		jgen.writeArrayFieldStart( "coordinates");
		JsonSerializer<Object> ser = provider.findValueSerializer( Float.class);
        for (int i = 0; i < value.getNumGeometries(); i++) {
            Point ml = (Point) value.getGeometryN(i);
	        for (int j = 0; j < ml.getNumPoints(); j++) {
	            Coordinate coordinate = ml.getCoordinate();
	            jgen.writeStartArray();
				ser.serialize( (float) coordinate.x, jgen, provider);
				ser.serialize( (float) coordinate.y, jgen, provider);
				jgen.writeEndArray();
	        }
        }
		jgen.writeEndArray();
	}

    @Override
    protected double[] getBboxCoordinates(JsonGenerator jgen, MultiPoint shape, SerializerProvider provider) {
        // minX, minY, maxX, maxY
        double[] result = new double[]{Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_VALUE};
        for (int i = 0; i < shape.getNumGeometries(); i++) {
            Point ml = (Point) shape.getGeometryN(i);
            for (int j = 0; j < ml.getNumPoints(); j++) {
                Coordinate coordinate = ml.getCoordinate();
                result[0] = Math.min(coordinate.x, result[0]);
                result[1] = Math.min(coordinate.y, result[1]);
                result[2] = Math.max(coordinate.x, result[2]);
                result[3] = Math.max(coordinate.y, result[3]);
            }
        }
        return result;
    }
}
