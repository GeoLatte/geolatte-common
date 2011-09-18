package org.geolatte.common.dataformats.json.to;

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
public class GeometryCollectionTo extends GeoJsonTo{

    private GeoJsonTo[] geometries;

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
}
