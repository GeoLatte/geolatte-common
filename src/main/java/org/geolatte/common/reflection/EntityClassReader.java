/*
 * This file is part of the GeoLatte project.
 *
 * GeoLatte is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GeoLatte is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GeoLatte.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2010 - 2010 and Ownership of code is shared by:
 * Qmino bvba - Romeinsestraat 18 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.common.reflection;

import org.geolatte.common.Feature;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.jts.JTS;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Convenience class that allows one to easily access properties from entity classes. The {@link EntityClassReader} uses a variety of optimizations
 * to ensure that repetitive reads of the same properties on different objects are relatively efficient.
 * <p/>
 * In addition, the class allows one to retrieve a wrapped version of an object if wanted.
 * Note: <ul>
 * <li>The method {@link EntityClassReader#getId(Object)} is identical to {@link EntityClassReader#getPropertyValue(Object, String)}
 * <li>The method {@link EntityClassReader#getGeometry(Object)} will return the value of the first field encountered that can be assigned to a geometry.
 * If more than one of those fields are present, you will therefore get a random one! If no geometry fields exist, null
 * will always be returned.
 * </ul>
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 9-apr-2010<br>
 * <i>Creation-Time</i>:  11:48:54<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class EntityClassReader {

    // Reusable empty object for reflection invoke calls.
    private static final Object[] EMPTY = new Object[0];
    // Map with all readers, to avoid duplicate readers
    private static Map<Class, Map<String, EntityClassReader>> allReaders = new HashMap<Class, Map<String, EntityClassReader>>();

    // Map with all property getter names, except for the id and the geometry property
    private Map<String, Method> methodMap = new HashMap<String, Method>();

    // Methods to access the geometry and id respectively
    private Method geometryGetter;
    private Method idGetter;

    // determines whether the read class uses JTS (rather than Geolatte-geom)
    private boolean usesJTS = false;

    // Class mapped by this reader
    private Class entityClass;
    /*
     * The declaredmethods of the feature-interface which is accessed by the proxy implementation
     */
    private Method proxyIdGetMethod;
    private Method proxyExistsMethod;
    private Method proxyGeomGetMethod;
    private Method proxyGeomNameGetMethod;
    private Method proxyPropertiesGetMethod;
    private Method proxyAttributeGetMethod;

    /*
     * Create a reader for a given entity class which allows for easy access to properties of an object of an unknown
     * class. Although any class can be given to the reader, the reader is made specifically for features.
     * <p/>
     * A feature has two <i>special</i> properties, which do not appear in the list of properties but
     * are instead retrieved through a dedicated method: getGeometry() and getId(). In this default constructor,
     * the first property encountered with type geometry (or a subclass of geometry) is considered the geometry,
     * and the property with name "id" is considered the id. For a custom mapping of those properties, use an
     * alternative constructor. Note that if for some reason the property 'id' is of type geometry, and it is
     * encountered as the first property of that type, it functions as both the id and the geometry of the feature.
     * <p/>
     * Presence of those properties is not required. If they are not present, calling getId() or getGeometry()
     * will just return null.
     *
     * @param entityClass the class for which a reader is desired.
     * @throws IllegalArgumentException if the given class is null
     */

    private EntityClassReader(Class entityClass) {
        if (entityClass == null) {
            throw new IllegalArgumentException("Given entityclass may not be null");
        }
        this.entityClass = entityClass;
        for (Method m : entityClass.getMethods()) {
            if (m.getParameterTypes().length == 0 && m.getName().startsWith("get")) {
                String propertyName = decapitalize(m.getName().substring(3));
                if (!"class".equals(propertyName)) {
                    boolean toAdd = true;
                    if (Geometry.class.isAssignableFrom(m.getReturnType()) && geometryGetter == null) {
                        geometryGetter = m;
                        toAdd = false;
                        usesJTS = false;
                    }
                    if (com.vividsolutions.jts.geom.Geometry.class.isAssignableFrom(m.getReturnType()) && geometryGetter == null) {
                        geometryGetter = m;
                        toAdd = false;
                        usesJTS = true;
                    }
                    if ("id".equals(propertyName)) {
                        idGetter = m;
                        toAdd = false;
                    }
                    if (toAdd) {
                        methodMap.put(propertyName, m);
                    }
                }
            }
            }
        }

    /*
     * Create a reader for a given entityclass which allows for easy access to properties of an object of an unknown
     * class. Although any class can be given to the reader, the reader is made specifically for features.
     * <p/>
     * A feature has two <i>special</i> properties, which do not appear in the list of properties but
     * are instead retrieved through a dedicated method: getGeometry() and getId(), the names of those properties
     * are specified in this constructor. If for some reason, the given properties would not exist in the
     * given class, or if the given property names are null, calling getId() or getGeometry() respectively
     * will just return null. If the property that corresponds with the geometryPropertyName exists but corresponds with
     * a non-geometry type, it is ignored and getGeometry() will return null. The property will, however, be added
     * as a normal property.
     *
     * @param entityClass          the class for which a reader is desired.
     * @param geometryPropertyName the name of the property to map as the geometry. If null, no property will be mapped as the geometry property.
     * @param idPropertyName       the name of the property to use as the objectid. If null, no property will be mapped as the geometry property.
     * @throws IllegalArgumentException if the given class is null
     */

    private EntityClassReader(Class entityClass, String geometryPropertyName, String idPropertyName) {
        if (entityClass == null) {
            throw new IllegalArgumentException("Given entityclass may not be null");
        }
        this.entityClass = entityClass;
        for (Method m : entityClass.getMethods()) {
            if (m.getParameterTypes().length == 0 && m.getName().startsWith("get") && m.getReturnType() != void.class) {
                String propertyName = decapitalize(m.getName().substring(3));
                if (!"class".equals(propertyName)) {
                    boolean toAdd = true;
                    if (geometryPropertyName != null && geometryPropertyName.equals(propertyName) &&
                            Geometry.class.isAssignableFrom(m.getReturnType())) {
                        geometryGetter = m;
                        toAdd = false;
                    }
                    if (idPropertyName != null && idPropertyName.equals(propertyName)) {
                        idGetter = m;
                        toAdd = false;
                    }
                    if (toAdd) {
                        methodMap.put(propertyName, m);
                    }
                }
            }
        }
    }

    /**
     * Factorymethod that returns the reader for a given entity class which allows for easy access to properties of an
     * object of an unknown class. Although any class can be given to the reader, the reader is made specifically for features.
     * <p/>
     * A feature has two <i>special</i> properties, which do not appear in the list of properties but
     * are instead retrieved through a dedicated method: getGeometry() and getId(). The reader retrieved by this method
     * will consider the first property encountered with type geometry (or a subclass of geometry) to be the geometry,
     * and the property with name "id" is considered the id. For a custom mapping of those properties, the alternative
     * factory method should be used. If for some reason the property 'id' is of type geometry, and it is
     * encountered as the first property of that type, it functions as both the id and the geometry of the feature.
     * <p/>
     * Presence of those properties is not required. If they are not present, calling getId() or getGeometry()
     * will just return null.
     *
     * @param entityClass the class for which a reader is desired.
     * @return the reader for the given class, which is unique. Subsequent calls to this with the same entityclass will
     *         return the same readerobject.
     */
    public static synchronized EntityClassReader getClassReaderFor(Class entityClass) {
        if (entityClass == null) {
            return null;
        }
        Map<String, EntityClassReader> readersForClass = allReaders.get(entityClass);
        if (readersForClass == null) {
            readersForClass = new HashMap<String, EntityClassReader>();
            allReaders.put(entityClass, readersForClass);
        }
        if (!readersForClass.containsKey(" ")) {
            readersForClass.put(" ", new EntityClassReader(entityClass));
        }
        return readersForClass.get(" ");
    }

    /**
     * Factorymethod that returns the reader for a given entity class which allows for easy access to properties of an
     * object of an unknown class. Although any class can be given to the reader, the reader is made specifically for features.
     * <p/>
     * A feature has two <i>special</i> properties, which do not appear in the list of properties but
     * are instead retrieved through a dedicated method: getGeometry() and getId(). The reader retrieved by this method
     * allows the user to specify the name of the geometry and id property. If for some reason, the given properties
     * would not exist in the given class, or if the given property names are null, calling getId() or getGeometry() respectively
     * will just return null. If the property that corresponds with the geometryPropertyName exists but corresponds with
     * a non-geometry type, it is ignored and getGeometry() will return null. The property will, however, be added
     * as a normal property.
     * <p/>
     *
     * @param entityClass          the class for which a reader is desired.
     * @param geometryPropertyName the name of the property representing the geometry of the feature
     * @param idPropertyName       the name of the property representing the id of the feature
     * @return the reader for the given class, which is unique. Subsequent calls to this with the same entityclass and
     *         propertynames will return the same readerobject.
     */
    public static synchronized EntityClassReader getClassReaderFor(Class entityClass, String geometryPropertyName, String idPropertyName) {
        if (entityClass == null) {
            return null;
        }
        String entryKey = "" + geometryPropertyName + "|" + idPropertyName;
        Map<String, EntityClassReader> readersForClass = allReaders.get(entityClass);
        if (readersForClass == null) {
            readersForClass = new HashMap<String, EntityClassReader>();
            allReaders.put(entityClass, readersForClass);
        }
        if (!readersForClass.containsKey(entryKey)) {
            readersForClass.put(entryKey, new EntityClassReader(entityClass, geometryPropertyName, idPropertyName));
        }
        return readersForClass.get(entryKey);
    }

    /**
     * Returns the name of the geometryfield of the given entity. If no geometry field exists, null is returned.
     *
     * @return the name of the geometryproperty, or null if no geometryproperty exists
     */
    public String getGeometryName() {
        String result = null;
        if (geometryGetter != null) {
            result = decapitalize(geometryGetter.getName().substring(3));
        }
        return result;
    }

    /**
     * Returns the name of the idfield of the given entity. If no id field exists, null is returned.
     *
     * @return the name of the idproperty, or null if no geometryproperty exists
     */
    public String getIdName() {
        String result = null;
        if (idGetter != null) {
            result = decapitalize(idGetter.getName().substring(3));
        }
        return result;
    }

    /**
     * Returns the value of the id of the given object if it is present. If no id property
     * could be found, null is returned.
     *
     * @param objectToGet the object from which the id is desired.
     * @return the value of the id property of the given object
     * @throws InvalidObjectReaderException If the object given is not of the entityclass this
     *                                      reader was instantiated with.
     * @throws IllegalArgumentException     if the given object is null
     */
    public Object getId(Object objectToGet)
            throws InvalidObjectReaderException {
        if (objectToGet == null) {
            throw new IllegalArgumentException("Given object may not be null");
        }
        if (objectToGet.getClass() != entityClass) {
            throw new InvalidObjectReaderException("Class of target object does not correspond with entityclass of this reader.");
        }
        try {
            if (idGetter == null) {
                return null;
            } else {
                return idGetter.invoke(objectToGet, EMPTY);
            }
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        }
    }

    /**
     * Will retrieve the value of the geometryfield in the given object. If no geometryfield
     * exists, null is returned. If more than one exist, a random one is returned.
     *
     * @param objectToGet the object from which the geometry is to be fetched.
     * @return the geometry of the given object
     * @throws InvalidObjectReaderException If the class of objectToGet does not correspond with the entityclass of this reader.
     * @throws IllegalArgumentException     the given object may not be null
     */
    public Geometry getGeometry(Object objectToGet) throws InvalidObjectReaderException {
        if (objectToGet == null) {
            throw new IllegalArgumentException("The given object may not be null");
        }
        if (objectToGet.getClass() != entityClass) {
            throw new InvalidObjectReaderException("Class of target object does not correspond with entityclass of this reader.");
        }
        if (geometryGetter == null) {
            return null;
        } else {
            try {
                if (usesJTS) {
                    return JTS.from((com.vividsolutions.jts.geom.Geometry)geometryGetter.invoke(objectToGet, EMPTY));
                }
                return (Geometry) geometryGetter.invoke(objectToGet, EMPTY);
            } catch (IllegalAccessException e) {
                return null;
            } catch (InvocationTargetException e) {
                return null;
            }
        }
    }

    /**
     * Returns the value of the property with a given name from the given object
     *
     * @param objectToGet  The object from which the property value is to be retrieved
     * @param propertyPath The dot-separated path of the property to retrieve. E.g. directly property: "name",
     *                     sub property: "streetAddress.number"
     * @return The value of the named property in the given object or null if the property can not
     *         be found
     * @throws InvalidObjectReaderException If the class of the given object does not correspond with the entityclass
     *                                      of this reader
     * @throws IllegalArgumentException     if the given objectToGet or propertyValue is null
     */
    public Object getPropertyValue(Object objectToGet, String propertyPath) throws InvalidObjectReaderException {

        if (objectToGet == null || propertyPath == null) {
            throw new IllegalArgumentException("Given object/propertyname may not be null");
        }
        if (objectToGet.getClass() != entityClass) {
            throw new InvalidObjectReaderException("Class of target object does not correspond with entityclass of this reader.");
        }

        StringTokenizer tokenizer = new StringTokenizer(propertyPath, ".", false);
        return getPropertyValue(objectToGet, tokenizer);
    }

    /**
     * Recursive method to get the value of a property path.
     *
     * @param objectToGet       The object from which the property value is to be retrieved.
     * @param propertyPathParts A set of property names, with enumeration pointer pointed at the last handled part.
     * @return The value of the named property in the given object or null if the property can not be found.
     */
    private Object getPropertyValue(Object objectToGet, StringTokenizer propertyPathParts) {

        String propertyName = propertyPathParts.nextToken();
        Object propertyValue = null;

        if (methodMap.containsKey(propertyName)) {
            try {
                propertyValue = methodMap.get(propertyName).invoke(objectToGet, EMPTY);
            } catch (IllegalAccessException e) {
                propertyValue = null;
            } catch (InvocationTargetException e) {
                propertyValue = null;
            }
        }

        if (!propertyPathParts.hasMoreTokens() || propertyValue == null)
            return propertyValue;
        else {

            EntityClassReader currentPathReader = new EntityClassReader(propertyValue.getClass());
            return currentPathReader.getPropertyValue(propertyValue, propertyPathParts);
        }
    }

    /**
     * Retrieves the type of the given property path.
     *
     * @param propertyPath The dot-separated path of the property type to retrieve. E.g. directly property: "name",
     *                     sub property: "streetAddress.number"
     * @return the type of the property with the given name or null if the propertyName is not a property of the class managed
     *         by this reader.
     * @throws IllegalArgumentException if propertyName is null.
     */
    public Class getPropertyType(String propertyPath) {

        if (propertyPath == null) {
            throw new IllegalArgumentException("Propertyname may not be null");
        }

        StringTokenizer tokenizer = new StringTokenizer(propertyPath, ".", false);
        return getPropertyType(tokenizer);
    }

    /**
     * Recursive method to retrieve the type of the given property path.
     *
     * @param propertyPathParts A set of property names, with enumeration pointer pointed at the last handled part.
     * @return The value of the named property in the given object or null if the property can not be found.
     */
    private Class getPropertyType(StringTokenizer propertyPathParts) {

        String propertyName = propertyPathParts.nextToken();
        Class propertyType = null;

        if (methodMap.containsKey(propertyName)) {
            propertyType = methodMap.get(propertyName).getReturnType();
        } else if (propertyName.equals(getIdName())) {
            propertyType = idGetter.getReturnType();
        } else if (propertyName.equals(getGeometryName())) {
            propertyType = geometryGetter.getReturnType();
        }

        if (!propertyPathParts.hasMoreTokens() || propertyType == null)
            return propertyType;
        else {

            EntityClassReader currentPathReader = new EntityClassReader(propertyType);
            return currentPathReader.getPropertyType(propertyPathParts);
        }
    }

    /**
     * <p>
     * Parses a given string into an object whose class corresponds with the type of the property given. For example,
     * if you give the method "4", and you ask it to parse like "width", and width is of the type Long, you will
     * receive an object of the type Long. If the property of the underlying class has a primitive type as its type,
     * the method will return the boxed variant instead.
     * </p>
     * <p>
     * <b>Currently, this method is only implemented for all numeric and boolean java types.</b>
     * </p>
     *
     * @param stringToParse The string to convert into an object
     * @param propertyPath  Dot-separated path to the property whose type is to be used as the class to convert to
     * @return A converted instance of the given string into the type of the given property. If the propertyName does
     *         not correspond with a property name, null is returned. If the propertyName has any other type than a supported type
     *         the original string is returned.
     */
    public Object parseAsPropertyType(String stringToParse, String propertyPath) {

        Class propertyType = getPropertyType(propertyPath);
        if (propertyType == null) {
            return null;
        }
        NumberTransformer parser = transformers.get(propertyType);
        return parser == null ? stringToParse : parser.parseObject(stringToParse);
    }

    /**
     * Returns whether a 'normal' property exists with a given name. A property a is said to exist
     * if the underlying class contains a method A getA().
     * <p/>
     * The id and geometry of the feature are not considered as normal properties. Depending on the value of the second
     * parameter, the method will return true or false if the given propertyname corresponds with the id or geometryfield
     *
     * @param propertyName             the name of the property to check
     * @param trueForSpecialProperties the answer to return if the given property corresponds with either the id or the
     *                                 geometry property of the feature
     * @return whether a property with that name exists
     * @throws IllegalArgumentException if the given propertyname is null
     */
    public boolean exists(String propertyName, boolean trueForSpecialProperties) {
        if (propertyName == null) {
            throw new IllegalArgumentException("Given object may not be null");
        }
        boolean normalProperty = methodMap.containsKey(propertyName);
        return normalProperty || (trueForSpecialProperties &&
                (getIdName() != null && getIdName().equals(propertyName) ||
                        getGeometryName() != null && getGeometryName().equals(propertyName)));
    }

    /**
     * Get a list of all properties of the underlying class.
     *
     * @return the list of all properties of the given class.
     */
    public Collection<String> getProperties() {
        return methodMap.keySet();
    }

    /**
     * Wraps the given object in a feature interface. This method will first validate whether the
     * class it represents is indeed a feature (containing exactly one geometry and id) and will then
     * generate a wrapper around the original object that obeys to the Feature interface.
     * If this method is invoked multiple times on the same object, the same wrapper will be returned in
     * each of these calls.
     *
     * @param objectToTransform the object for which a feature is desired.
     * @return a feature wrapper around the given object
     * @throws InvalidObjectReaderException If the class of objectToTransform does not correspond with the entityclass
     *                                      of this reader
     * @throws IllegalArgumentException     if the given objectToTransform is null
     */
    public Feature asFeature(Object objectToTransform) throws InvalidObjectReaderException {
        if (objectToTransform == null) {
            throw new IllegalArgumentException("Given object may not be null");
        }
        if (proxyPropertiesGetMethod == null) {
            Class[] emptyArray = new Class[]{};
            try {
                proxyGeomGetMethod = Feature.class.getDeclaredMethod("getGeometry", emptyArray);
                proxyGeomNameGetMethod = Feature.class.getDeclaredMethod("getGeometryName", emptyArray);
                proxyIdGetMethod = Feature.class.getDeclaredMethod("getId", emptyArray);
                proxyExistsMethod = Feature.class.getDeclaredMethod("hasProperty", new Class[]{String.class, boolean.class});
                proxyAttributeGetMethod = Feature.class.getDeclaredMethod("getProperty", new Class[]{String.class});
                proxyPropertiesGetMethod = Feature.class.getDeclaredMethod("getProperties", emptyArray);
            } catch (NoSuchMethodException e) {
                // This can not happen,  since we control the feature interface class. So this must be some version-
                // error in the geolatte library!
                throw new RuntimeException("Version mismatch: apparantly certain methods are not present in the Feature interface");
            }
        }

        if (objectToTransform.getClass() != entityClass) {
            throw new InvalidObjectReaderException("Class of target object does not correspond with entityclass of this reader.");
        }
        Feature proxy = (Feature) Proxy.newProxyInstance(entityClass.getClassLoader(),
                new Class[]{Feature.class}, new ObjectInvocationHandler(objectToTransform));
        return proxy;
    }

    /**
     * Returns a string that is identical to the original string except that the fist character is now lowercase
     * (regardless of how it was originally)
     *
     * @param inputString the string to capitalize
     * @return a capitalized version of the string
     */
    private String decapitalize(String inputString) {
        return inputString.length() > 1 ? Character.toLowerCase(inputString.charAt(0)) + inputString.substring(1)
                : inputString.toUpperCase();
    }

    /**
     * The objectinvocationhandler class is an internal class that implements the proxy interface of a feature
     * by redirecting their calls to the creating entityclassreader.
     * by redirecting their calls to the creating entityclassreader.
     */
    class ObjectInvocationHandler implements InvocationHandler {
        private Object target;

        ObjectInvocationHandler(Object targetObject) {
            target = targetObject;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (proxyAttributeGetMethod.equals(method)) {
                return EntityClassReader.this.getPropertyValue(target, (String) args[0]);
            } else if (proxyIdGetMethod.equals(method)) {
                return EntityClassReader.this.getId(target);
            } else if (proxyGeomGetMethod.equals(method)) {
                return EntityClassReader.this.getGeometry(target);
            } else if (proxyGeomNameGetMethod.equals(method)) {
                return EntityClassReader.this.getGeometryName();
            } else if (proxyPropertiesGetMethod.equals(method)) {
                return EntityClassReader.this.getProperties();
            } else if (proxyExistsMethod.equals(method)) {
                return EntityClassReader.this.exists((String) args[0], (Boolean) args[1]);
            } else {
                // This should not happen! I use a runtimeexception since we can not
                // change the interface of the invoke method.
                throw new RuntimeException("Unexisting method invoked on proxy.");
            }
        }
    }


    // Static objectconvertors.
    private static Map<Class, NumberTransformer> transformers = new HashMap<Class, NumberTransformer>();

    static {
        transformers.put(Integer.class, new IntegerTransformer());
        transformers.put(int.class, new IntegerTransformer());
        transformers.put(Long.class, new LongTransformer());
        transformers.put(long.class, new LongTransformer());
        transformers.put(Double.class, new DoubleTransformer());
        transformers.put(double.class, new DoubleTransformer());
        transformers.put(Short.class, new ShortTransformer());
        transformers.put(short.class, new ShortTransformer());
        transformers.put(Float.class, new FloatTransformer());
        transformers.put(float.class, new FloatTransformer());
        transformers.put(Boolean.class, new BooleanTransformer());
        transformers.put(boolean.class, new BooleanTransformer());
        transformers.put(Byte.class, new ByteTransformer());
        transformers.put(byte.class, new ByteTransformer());
        transformers.put(BigDecimal.class, new BigDecimalTransformer());
    }

    private static interface NumberTransformer {
        Object parseObject(String s);
    }

    private static class IntegerTransformer implements NumberTransformer {
        public Object parseObject(String s) {
            return Integer.valueOf(s);
        }
    }

    private static class BigDecimalTransformer implements NumberTransformer {
        public Object parseObject(String s) {
            return BigDecimal.valueOf(Double.valueOf(s));
        }
    }

    private static class DoubleTransformer implements NumberTransformer {
        public Object parseObject(String s) {
            return Double.valueOf(s);
        }
    }

    private static class LongTransformer implements NumberTransformer {
        public Object parseObject(String s) {
            return Long.valueOf(s);
        }
    }

    private static class BooleanTransformer implements NumberTransformer {
        public Object parseObject(String s) {
            return Boolean.valueOf(s);
        }
    }

    private static class FloatTransformer implements NumberTransformer {
        public Object parseObject(String s) {
            return Float.valueOf(s);
        }
    }

    private static class ByteTransformer implements NumberTransformer {
        public Object parseObject(String s) {
            return Byte.valueOf(s);
        }
    }

    private static class ShortTransformer implements NumberTransformer {
        public Object parseObject(String s) {
            return Short.valueOf(s);
        }
    }
}
