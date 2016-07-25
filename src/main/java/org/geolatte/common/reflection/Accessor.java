package org.geolatte.common.reflection;

import org.geolatte.geom.Geometry;
import org.geolatte.geom.jts.JTS;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A <code>Accessor</code> retrieves a property value from an object by means of reflection.
 * <br>
 * <p>This abstraction allows us to massage the return value of a Method.invoke() before using it,
 * e.g. so that we can first transform if from JTS Geometry to Geolatte Geometry.</p>
 *
 * @author Karel Maesen, Geovise BVBA, 2012
 */
abstract class Accessor {

    static Accessor newInstance(Method accessorMethod, String propertyName) {
        if (com.vividsolutions.jts.geom.Geometry.class.isAssignableFrom(
                accessorMethod.getReturnType())) {
            return new JTSGeometryAccessor(accessorMethod, propertyName);
        } else {
            return new BasicAccessor(accessorMethod, propertyName);
        }
    }

    abstract Object getValueFrom(Object object);

    abstract String getPropertyName();

    abstract Class getReturnType();

    /**
     * A <code>Accessor</code> returns the value of a property from a targetObject
     */
    private static class BasicAccessor extends Accessor {
        final Method accessorMethod;
        final String propertyName;

        BasicAccessor(Method method, String name) {
            accessorMethod = method;
            propertyName = name;
        }

        Object getValueFrom(Object object) {
            try {
                return accessorMethod.invoke(object);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Failed to fetch value", e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException("Failed to fetch value", e);
            }
        }

        String getPropertyName() {
            return propertyName;
        }

        Class getReturnType(){
            return accessorMethod.getReturnType();
        }

    }


    private static class JTSGeometryAccessor extends BasicAccessor {
        JTSGeometryAccessor(Method accessor, String propertyName) {
            super(accessor, propertyName);
        }

        Object getValueFrom(Object object) {
            Object value = super.getValueFrom(object);
            return value == null ? null : JTS.from((com.vividsolutions.jts.geom.Geometry) value);
        }

        Class getReturnType() {
            return Geometry.class;
        }


    }

}
