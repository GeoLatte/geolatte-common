package org.geolatte.common.dataformats.json.to.jackson;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.geolatte.common.dataformats.json.to.*;

/**
 * Provides Jackson-specific annotations for the GeoJson TOs.
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.PROPERTY, property="type")
@JsonIgnoreProperties({"valid"})
@JsonSubTypes({
        @JsonSubTypes.Type(value=PointTo.class, name="Point"),
        @JsonSubTypes.Type(value=MultiPointTo.class, name="MultiPoint"),
        @JsonSubTypes.Type(value=LineStringTo.class, name="LineString"),
        @JsonSubTypes.Type(value=MultiLineStringTo.class, name="MultiLineString"),
        @JsonSubTypes.Type(value=PolygonTo.class, name="Polygon"),
        @JsonSubTypes.Type(value=MultiPolygonTo.class, name="MultiPolygon"),
        @JsonSubTypes.Type(value=GeometryCollectionTo.class, name="GeometryCollection")

})
public interface GeoJsonToMixin {
}
