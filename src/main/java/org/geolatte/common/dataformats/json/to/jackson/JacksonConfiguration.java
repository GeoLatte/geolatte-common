package org.geolatte.common.dataformats.json.to.jackson;

import org.codehaus.jackson.map.ObjectMapper;
import org.geolatte.common.dataformats.json.to.GeoJsonTo;

/**
 * Applies GeoJson-TO configuration options to a Jackson {@link ObjectMapper}.
 *
 * @author Bert Vanhooff [<a href="http://www.qmino.com">Qmino bvba</a>]
 */
public class JacksonConfiguration {

    public static void applyMixin(ObjectMapper objectMapper) {
        objectMapper.getSerializationConfig().addMixInAnnotations(GeoJsonTo.class, GeoJsonToMixin.class);
        objectMapper.getDeserializationConfig().addMixInAnnotations(GeoJsonTo.class, GeoJsonToMixin.class);
    }
}