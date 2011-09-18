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

import org.codehaus.jackson.map.JsonSerializer;
import org.geolatte.common.transformer.Transformation;
import org.geolatte.common.transformer.TransformationException;

/**
 * A transformation that serializes an object into a jsonstring. A Json
 * serializer intially knows how to serialize all primitive types,
 * different geometries and Features. In addition, a JsonSerializationTransformation allows
 * the user to configure it and add additional objectserializers to deal
 * with a certain type through the addClassSerializer method.
 *
 * This class uses the Jackson JSON Serialisation framework underneath.
 *
 * <p>
 * <i>Creation-Date</i>: 20-apr-2010<br>
 * <i>Creation-Time</i>: 8:09:55<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class JsonSerializationTransformation implements Transformation<Object, String> {

    JsonMapper mapper;

    public JsonSerializationTransformation()
    {
        mapper = new JsonMapper();
    }

    public synchronized String transform(Object input) throws TransformationException {
        try {
            return mapper.toJson(input);
        } catch (JsonException e) {
            throw new TransformationException(e);
        }
    }

    public <T> void addClassSerializer(Class<? extends T> classToMap, JsonSerializer<T> classSerializer)
    {
        mapper.addClassSerializer(classToMap, classSerializer);
    }
}
