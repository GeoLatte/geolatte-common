package org.geolatte.common.dataformats.json.to;

import java.util.Arrays;

/**
 * Hier dient nog commentaar te worden voorzien.
 * <p/>
 * <p>
 * Bestand aangemaakt op: </i>: 16/09/11-15:27<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since Kabelnet 0.1
 */
public final class GeometryCollectionTo extends GeoJsonTo {

    private GeoJsonTo[] geometries;

    public GeometryCollectionTo() {

    }

    public GeometryCollectionTo(CrsTo crsTo, GeoJsonTo[] geometries) {
        this.setCrs(crsTo);
        this.geometries = geometries;
    }

    public GeoJsonTo[] getGeometries() {
        return geometries;
    }

    public void setGeometries(GeoJsonTo[] geometries) {
        this.geometries = geometries;
    }

    @Override
    public boolean isValid() {
        if (geometries == null) {
            return false;
        }
        for (GeoJsonTo geom: geometries) {
            // It is not allowed to specify crs at the children level!
            if (!geom.isValid() || geom.getCrs() != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {

        if (!super.equals(o)) {
            return false;
        }

        GeometryCollectionTo that = (GeometryCollectionTo) o;

        if (!Arrays.equals(geometries, that.geometries)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {

        int result = super.hashCode();
        result = 31 * result + (geometries != null ? Arrays.hashCode(geometries) : 0);
        return result;
    }
}
