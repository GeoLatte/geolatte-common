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
import org.geolatte.geom.Geometry;

import java.io.IOException;

/**
 * Abstract parentclass for multiple geojson serializers that implements a number of common
 * properties for a Geometry to GeoJSON conversion. See <a href="http://geojson.org/geojson-spec.html">
 * the GeoJSON specification</a> for more information.
 * <p>
 * <i>Creation-Date</i>: 20-apr-2010<br>
 * <i>Creation-Time</i>: 14:02:52<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public abstract class GeometrySerializer<T extends Geometry> extends JsonSerializer<T> {

    private JsonMapper parent;

    /**
     * @param containingTransformation The containing serializationtransformation.
     */
    public GeometrySerializer(JsonMapper containingTransformation)
    {
        parent = containingTransformation;
    }

    /**
     * @return a reference to the containing transformation
     */
    protected JsonMapper getParent() {
        return parent;
    }


    /**
     * Method that can be called to ask implementation to serialize values of type this serializer handles.
     *
     * @param value    Value to serialize; can not be null.
     * @param jgen     Generator used to output resulting Json content
     * @param provider Provider that can be used to get serializers for serializing Objects value contains, if any.
     * @throws java.io.IOException If serialization failed.
     */
    @Override
    public void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        // Crs or bbox-information must be placed on the toplevel object, but may not be repeated in contained objects!
        if (!parent.insideGeometryCollection())
        {
            writeCrs(jgen, value);
            writeBbox(jgen, value, provider);
        }
        writeShapeSpecificSerialization(value, jgen, provider);
        jgen.writeEndObject();
    }

    /**
     * Method that in which the shapespecific geojson serialization is implemented.
     *
     * @param value    Value to serialize; can not be null.
     * @param jgen     Generator used to output resulting Json content
     * @param provider Provider that can be used to get serializers for serializing Objects value contains, if any.
     * @throws java.io.IOException If serialization fails.
     */
    protected abstract void writeShapeSpecificSerialization(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException;

    /**
     * This method will return, for each of its dimension, the lowest value followed by its highest value.
     * Suppose the dimension is 3 (eg xyz) , the method will return [xmin, xmax, ymin, ymax, zmin, zmax] in that order.
     * <p/>
     * Since the presence of boundingbox coordinates is not required by the specification, the subclass may decide whether
     * or not the information is to be included. If this method returns null, the bbox information will not be part of the
     * final geojson representation.
     *
     * @param jgen  the jsongenerator used for the geojson construction
     * @param shape the geometry for which the bbox coordinates must be retrieved
     * @param provider provider to retrieve other serializers (for recursion)
     * @return boundingbox coordinates of this geojson or null if none are to be specified
     * @throws org.codehaus.jackson.map.JsonMappingException If for some reason a provider could not be found (recursion)
     */
    protected abstract double[] getBboxCoordinates(JsonGenerator jgen, T shape, SerializerProvider provider) throws JsonMappingException;

    /**
     * Writes out the crs information in the GeoJSON string
     *
     * @param jgen  the jsongenerator used for the geojson construction
     * @param shape the geometry for which the contents is to be retrieved
     * @throws java.io.IOException If the underlying jsongenerator fails writing the contents
     */
    protected void writeCrs(JsonGenerator jgen, Geometry shape)
            throws IOException {
        /*
            "crs": {
            "type": "name",
            "properties": {
                "name": "EPSG:xxxx"
                }
            }
        */
        if (shape.getSRID() > 0) {
            jgen.writeFieldName("crs");
            jgen.writeStartObject();
            jgen.writeStringField("type", "name");
            jgen.writeFieldName("properties");
            jgen.writeStartObject();
            jgen.writeStringField("name", "EPSG:" + shape.getSRID());
            jgen.writeEndObject();
            jgen.writeEndObject();
        }
    }

    /**
     * Adds the bbox parameter to the geojson string, as defined by the <a href="http://geojson.org/geojson-spec.html">
     * GeoJSON specification</a>
     *
     * @param jgen  the jsongenerator used for the geojson construction
     * @param shape the geometry for which the contents is to be retrieved
     * @param provider provider to retrieve other serializers (for recursion)
     * @throws java.io.IOException If the underlying jsongenerator fails writing the contents
     */
    protected void writeBbox(JsonGenerator jgen, T shape, SerializerProvider provider)
            throws IOException {
        double[] coordinates = getBboxCoordinates(jgen, shape, provider);
        if (coordinates != null) {
            jgen.writeFieldName("bbox");
            jgen.writeObject(coordinates);
        }
    }
}
