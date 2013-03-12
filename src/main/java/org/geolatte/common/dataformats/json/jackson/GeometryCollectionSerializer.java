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


import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.geolatte.geom.Envelope;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.GeometryCollection;

import java.io.IOException;

/**
 * Implements serialization of an entire GeometryCollection according to the spec
 * <p>
 * <i>Creation-Date</i>: 22-apr-2010<br>
 * <i>Creation-Time</i>: 15:19:21<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class GeometryCollectionSerializer extends GeometrySerializer<GeometryCollection> {

    /**
     * @param containingTransformation The containing serializationtransformation.
     */
    public GeometryCollectionSerializer(JsonMapper containingTransformation) {
        super(containingTransformation);
    }

    @Override
    protected void writeShapeSpecificSerialization(GeometryCollection value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
		jgen.writeStringField("type", "GeometryCollection");
        jgen.writeFieldName("geometries");
        jgen.writeStartArray();
        for (int i=0; i< value.getNumGeometries(); i++)
        {
            Geometry current = value.getGeometryN(i);
            JsonSerializer ser = provider.findValueSerializer(current.getClass());
            if (ser != null && ser instanceof GeometrySerializer)
            {
                getParent().increaseDepth();
                getParent().moveInsideGeometryCollection();
                ser.serialize(current, jgen, provider);
                getParent().moveOutsideGeometryCollection();
                getParent().decreaseDepth();                
            }
        }
        jgen.writeEndArray();
    }

}
