package org.geolatte.common.dataformats.json.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.geolatte.common.Feature;
import org.geolatte.common.FeatureCollection;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.GeometryCollection;
import org.geolatte.geom.LineString;
import org.geolatte.geom.MultiLineString;
import org.geolatte.geom.MultiPoint;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.geolatte.geom.Polygon;
import org.geolatte.geom.crs.CrsId;

/**
 * A Jackson module for Geolatte.
 */
public class GeolatteCommonModule extends SimpleModule {

    /**
     * Constructor of the jsonmapper
     *
     * @param defaultCrsId            default Coordinate Referency System to use for {@code Geometry Geometries}.
     */
    public GeolatteCommonModule(CrsId defaultCrsId) {
        this(new JsonMapper(defaultCrsId, true, false));
    }

    /**
     * Default constructor. Maps all values, including null values and will throw exceptions on unknown properties;
     * default coordinate reference system will be WGS84.
     */
    public GeolatteCommonModule() {
        this(new JsonMapper());
    }

    GeolatteCommonModule(JsonMapper jsonMapper) {
        addSerializer(MultiLineString.class, new MultiLineStringSerializer(jsonMapper));
        addSerializer(LineString.class, new LineStringSerializer(jsonMapper));
        addSerializer(Point.class, new PointSerializer(jsonMapper));
        addSerializer(MultiPoint.class, new MultiPointSerializer(jsonMapper));
        addSerializer(Polygon.class, new PolygonSerializer(jsonMapper));
        addSerializer(Feature.class, new FeatureSerializer(jsonMapper));
        addSerializer(MultiPolygon.class, new MultiPolygonSerializer(jsonMapper));
        addSerializer(Geometry.class, new AnyGeometrySerializer());
        addSerializer(GeometryCollection.class, new GeometryCollectionSerializer(jsonMapper));

        addDeserializer(Geometry.class, new GeometryDeserializer<Geometry>(jsonMapper, Geometry.class));
        addDeserializer(Point.class, new GeometryDeserializer<Point>(jsonMapper, Point.class));
        addDeserializer(LineString.class, new GeometryDeserializer<LineString>(jsonMapper, LineString.class));
        addDeserializer(MultiPoint.class, new GeometryDeserializer<MultiPoint>(jsonMapper, MultiPoint.class));
        addDeserializer(MultiLineString.class, new GeometryDeserializer<MultiLineString>(jsonMapper, MultiLineString.class));
        addDeserializer(Polygon.class, new GeometryDeserializer<Polygon>(jsonMapper, Polygon.class));
        addDeserializer(MultiPolygon.class, new GeometryDeserializer<MultiPolygon>(jsonMapper, MultiPolygon.class));
        addDeserializer(GeometryCollection.class, new GeometryDeserializer<GeometryCollection>(jsonMapper, GeometryCollection.class));
        addDeserializer(Feature.class, new FeatureDeserializer(jsonMapper));
        addDeserializer(FeatureCollection.class, new FeatureCollectionDeserializer(jsonMapper));
    }

}
