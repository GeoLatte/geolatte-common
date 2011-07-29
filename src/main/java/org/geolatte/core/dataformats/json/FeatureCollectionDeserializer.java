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
import org.codehaus.jackson.JsonParser;
import org.geolatte.core.Feature;
import org.geolatte.core.FeatureCollection;

import java.io.IOException;
import java.util.List;

/**
 * Deserializes a featurecollection into a list of features.
 * <p>
 * <i>Creation-Date</i>: 1-sep-2010<br>
 * <i>Creation-Time</i>: 11:32:15<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class FeatureCollectionDeserializer extends GeoJsonDeserializer<FeatureCollection> {

    public FeatureCollectionDeserializer(JsonMapper owner) {
        super(owner, FeatureCollection.class);
    }

    @Override
    protected FeatureCollection deserialize(JsonParser jsonParser) throws IOException {
        Integer srid = getSrid();
        String type = getStringParam("type", "Invalid Feature, type property required.");
        // Default srd = WGS84 according to the GeoJSON specification
        int sridToUse = srid == null ? 5 : srid;

        if ("FeatureCollection".equals(type)) {
            try {
                List<Feature> features =  parent.collectionFromJson(getSubJson("features", "A geometry field is required for a Feature"), Feature.class);
                if (srid !=null)
                {
                    for (Feature f: features)
                    {
                        f.getGeometry().setSRID(sridToUse);
                    }
                }
                return new DefaultFeatureCollection(features);
            } catch (JsonException e) {
                throw new IOException("Problem deserializing individual features in collection", e);
            }
        }
        else
        {
            throw new IOException("For a featurecollection, the type property must equal 'FeatureCollection'");
        }
    }
}
