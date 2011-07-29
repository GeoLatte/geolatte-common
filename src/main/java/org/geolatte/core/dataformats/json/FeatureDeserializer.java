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

package org.geolatte.core.dataformats.json;

import com.vividsolutions.jts.geom.Geometry;
import org.codehaus.jackson.JsonParser;
import org.geolatte.core.Feature;

import java.io.IOException;
import java.util.Map;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 30-aug-2010<br>
 * <i>Creation-Time</i>: 18:17:52<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class FeatureDeserializer extends GeoJsonDeserializer<Feature> {

    public FeatureDeserializer(JsonMapper owner) {
        super(owner, Feature.class);
    }

    @Override
    protected Feature deserialize(JsonParser jsonParser) throws IOException {
        String type = getStringParam("type", "Invalid Feature, type property required.");
        Integer srid = getSrid();
        // Default srd = WGS84 according to the GeoJSON specification        
        int sridToUse = srid == null ? 5 : srid;

        if ("Feature".equals(type)) {
            Geometry theGeom;
            try {
                DefaultFeature f = new DefaultFeature();
                String subJson = getSubJson("geometry", "A geometry field is required for a Feature");
                String noSpaces = subJson.replaceAll(" ", "");
                if (noSpaces.contains("\"crs\":{"))
                {
                    throw new IOException("Specification of the crs information is forbidden in child elements. Either leave it out, or specify it at the toplevel object.");
                }

                theGeom = parent.fromJson(subJson, Geometry.class);
                if (srid != null) {
                    theGeom.setSRID(sridToUse);
                }
                f.setGeometry("geometry", theGeom);

                Object idValue = getTypedParam("id", null, Object.class);
                if (idValue != null) {
                    f.setId("id", idValue);
                }

                Map<String, Object> properties = parent.fromJson(getSubJson("properties", "A feature requires a properties list"), Map.class);
                for (String m : properties.keySet()) {
                    f.addProperty(m, properties.get(m));
                }
                return f;
            } catch (JsonException e) {
                throw new IOException("Deserialization of geometry for the given feature failed", e);
            }
        } else {
            throw new IOException("The type parameter of a feature must have 'Feature' as its value");
        }
    }
}
