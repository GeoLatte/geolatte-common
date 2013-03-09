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


import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.geolatte.geom.LineString;
import org.geolatte.geom.MultiLineString;
import org.geolatte.geom.Point;

/**
 * Serializationclass for multilinestrings: creates a geojson representation of a
 * multilinestring-geometry.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 20-apr-2010<br>
 * <i>Creation-Time</i>: 9:01:31<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class MultiLineStringSerializer extends GeometrySerializer<MultiLineString> {
    /**
     * @param containingTransformation The containing serializationtransformation.
     */
    public MultiLineStringSerializer(JsonMapper containingTransformation) {
        super(containingTransformation);
    }

    /**
     * Method that can be called to ask implementation to serialize values of type this serializer handles.
     * @param value Value to serialize; can not be null.
     * @param jgen Generator used to output resulting Json content
     * @param provider Provider that can be used to get serializers for serializing Objects value contains, if any.
     * @throws java.io.IOException If serialization failed.
     */
    @Override
	public void writeShapeSpecificSerialization(MultiLineString value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		jgen.writeFieldName( "type");
		jgen.writeString( "MultiLineString");
		jgen.writeArrayFieldStart( "coordinates");
		JsonSerializer<Object> ser = provider.findValueSerializer( Double.class);
        for (int i = 0; i < value.getNumGeometries(); i++) {
            LineString ml = (LineString) value.getGeometryN(i);
            jgen.writeStartArray();
	        for (int j = 0; j < ml.getNumPoints(); j++) {
	            Point point = ml.getPointN(j);
	            jgen.writeStartArray();
				ser.serialize(  point.getX(), jgen, provider);
				ser.serialize(  point.getY(), jgen, provider);
				jgen.writeEndArray();
	        }
			jgen.writeEndArray();
        }
		jgen.writeEndArray();
	}

}
