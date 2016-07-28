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

import java.io.IOException;
import java.util.Map;

/**
 * Abstract deserialization class that implements the parsing of crs (srids) and is common to a number of deserializers
 * that together implement the geojson spec, such as GeometryDeserizalir, FeatureDeserializer
 * and FeaturecollectionDeserializer
 * <p>
 * <i>Creation-Date</i>: 1-sep-2010<br>
 * <i>Creation-Time</i>: 18:13:55<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public abstract class GeoJsonDeserializer<T> extends AbstractJsonDeserializer<T> {

    private Class<T> deserializerClass;

    public GeoJsonDeserializer(JsonMapper owner, Class<T> clazz) {
        super(owner);
        deserializerClass = clazz;
    }

    /**
     * @return the class of the object that this deserializer can deserialize
     */
    protected Class<T> getDeserializerClass()
    {
        return deserializerClass;
    }

    /**
     * If an srid (crs) is specified in the json object, it is returned. If no srid is found in the current parameter-map
     * null is returned. This is a simplified version from the actual GeoJSON specification, since the GeoJSON specification
     * allows for relative links to either URLS or local files in which the crs should be defined. This implementation
     * only supports named crs's: namely:
     * <pre>
     *  "crs": {
     *       "type": "name",
     *       "properties": {
     *             "name": "...yourcrsname..."
     *       }
     * }
     * </pre>
     * Besides the fact that only named crs is permitted for deserialization, the given name must either be of the form:
     * <pre>
     *  urn:ogc:def:EPSG:x.y:4326
     * </pre>
     * (with x.y the version of the EPSG) or of the form:
     * <pre>
     * EPSG:4326
     * </pre>
     * @return the SRID value of the crs system in the json if it is present, null otherwise.
     * @throws java.io.IOException If a crs object is present, but deserialization is not possible
     */
    protected Integer getSrid() throws IOException {
        Map<String, Object> crsContent = getTypedParam("crs", null, Map.class);
        if (crsContent != null) {
            if (crsContent.get("type") == null || !"name".equals(crsContent.get("type"))) {
                throw new IOException("If the crs is specified the type must be specified. Currently, only named crses are supported.");
            }
            Object properties = crsContent.get("properties");
            if (properties == null || !(properties instanceof Map) || (!((Map) properties).containsKey("name"))) {
                throw new IOException("A crs specification requires a properties value containing a name value.");
            }
            String sridString = ((Map) properties).get("name").toString();
            if (sridString.startsWith("EPSG:")) {
                Integer srid = parseDefault(sridString.substring(5), (Integer) null);
                if (srid == null) {
                    throw new IOException("Unable to derive SRID from crs name");
                } else {
                    return srid;
                }
            } else if (sridString.startsWith("urn:ogc:def:crs:EPSG:")) {
                String[] splits = sridString.split(":");
                if (splits.length != 7) {
                    throw new IOException("Unable to derive SRID from crs name");
                } else {
                    Integer srid = parseDefault(splits[6], (Integer) null);
                    if (srid == null) {
                        throw new IOException("Unable to derive SRID from crs name");
                    }
                    return srid;
                }
            } else {
                throw new IOException("Unable to derive SRID from crs name");
            }
        }
        return null;
    }
}
