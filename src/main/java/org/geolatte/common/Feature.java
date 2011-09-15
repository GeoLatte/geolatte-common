/*
 * This file is part of the GeoLatte project.
 *
 *     GeoLatte is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     GeoLatte is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with GeoLatte.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2010 - 2010 and Ownership of code is shared by:
 * Qmino bvba - Romeinsestraat 18 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.common;

import com.vividsolutions.jts.geom.Geometry;

import java.util.Collection;

/**
 * The feature interface is a minimal interface for features through which the different properties of the
 * feature can be conveniently retrieved without the need for a reflectionapi.
 * <p>
 * <i>Creation-Date</i>: 19-apr-2010<br>
 * <i>Creation-Time</i>:  10:14:14<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public interface Feature {

    /**
     * Returns whether a property with the given name exists in the underlying feature. A property is any
     * value for whom a gettermethod exists without parameters with the exception of class (since the getClass
     * method is defined in object, class is not considered to be a property of the feature).
     * @param propertyName the name of the property to check
     * @param trueForSpecialProperties whether the method should return true if the given propertyname is a special
     * case (ie: it corresponds with the id or the geometryproperty)
     * @return whether a property with the given name exists.
     * @throws IllegalArgumentException if propertyName is null
     */
    boolean hasProperty(String propertyName, boolean trueForSpecialProperties);

    /**
     * @return Returns a list of all properties in the underlying feature, excluding the geometry and id properties of the
     * class.
     */
    Collection<String> getProperties();

    /**
     * Returns the value of the property with the given name or null if no such property is found.
     * @param propertyName The name of the property whose contents is to be retrieved. A null response can
     * therefore either mean that the property does not exist, or that the property exists but is indeed null.
     * In order to distinguish between both, the getProperties() method can be used to see which properties exist
     * for this feature.
     * @return The value of the property with the given name.
     * @throws IllegalArgumentException if propertyName is null
     */
    Object getProperty(String propertyName);

    /**
     * Returns the id of this object (if it exists), or null if it does not. The id of the
     * feature is defined as the value of the property with the name "id".
     * @return the value of the id field, or null if no such field exists.
     */
    Object getId();

    /**
     * Return the geometry associated with this object. If no geometry field exists, null is
     * returned. If more than one field exists, a random geometry property is returned. Any property
     * with Geometry or a subtype as its type is a possible geometry.
     * @return the geometry property of this feature (if it exists).
     */
    Geometry getGeometry();

    /**
     * Gets the name of the geometry property returned by {@link #getGeometry}.
     * @return The name of the geometry property. Null if no geometry property exists.
     */
    String getGeometryName();

    /**
     * @return Whether this feature has an id property. This allows you to better interpret the meaning of a null
     * returnvalue in getId()
     */
    boolean hasId();

    /**
     * @return Whether this feature has a geometry property. This allows you to better interpret the meaning of a null
     * returnvalue in getGeometry()
     */
    boolean hasGeometry();
}
