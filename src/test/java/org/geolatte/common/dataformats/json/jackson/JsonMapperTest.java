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

import junit.framework.Assert;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.geolatte.common.dataformats.json.jackson.JsonMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

/**
 * Tests the JsonMapper.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 9-apr-2010<br>
 * <i>Creation-Time</i>:  11:48:54<br>
 * </p>
 *
 * @author Peter Rigole
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class JsonMapperTest {

    @Test
    public void testConfigurationAfterAddingSerializerDeserializer() {
        JsonMapper mapper = new JsonMapper(false, true);
        // Add a dummy serializer and deserializer.
        mapper.addClassSerializer(Date.class, new JsonSerializer<Date>() {

            @Override
            public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException {
                return;
            }
        });
        mapper.addClassDeserializer(Date.class, new JsonDeserializer<Date>() {

            @Override
            public Date deserialize(JsonParser jp, DeserializationContext ctxt)
                    throws IOException {
                return null;
            }
        });
        // Now, check if the configuration parameters on the JsonMapper still apply.
        Assert.assertEquals("Non-null inclusion is expected.", JsonSerialize.Inclusion.NON_NULL,
                mapper.getObjectMapper().getSerializationConfig().getSerializationInclusion());
        Assert.assertFalse("FAIL_ON_UNKNOWN_PROPERTIES is expected to be false.", mapper.getObjectMapper()
                .getDeserializationConfig().isEnabled(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES));
    }

}