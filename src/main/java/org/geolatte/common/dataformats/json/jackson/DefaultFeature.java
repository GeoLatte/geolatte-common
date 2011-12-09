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

import org.geolatte.common.Feature;
import org.geolatte.geom.Geometry;

import java.util.*;

/**
 * A default feature is a simple implementation of the {@link Feature} interface that does not use reflection.
 * <i>Creation-Date</i>: 1-sep-2010<br>
 * <i>Creation-Time</i>: 9:50:05<br>
 *
 * @author Yves Vandewoude
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class DefaultFeature implements Feature {

    // Propertynames are kept seperately and are a superset of the keyset of properties. If the value of a
    // certain property is null, but the property exists, the property is present in propertyName but not in
    // properties.
    private Set<String> propertyNames = new HashSet<String>();
    private Map<String, Object> properties = new HashMap<String, Object>();

    // We keep id and geometry seperate
    private String geomPropertyName;
    private String idPropertyName;
    private Geometry geomValue;
    private Object idValue;

    /**
     * Default constructor.
     */
    public DefaultFeature() {
    }

    /**
     * Constructs a DefaultFeature as a copy of the given feature.
     * All properties of the given feature are copied. The id property is stored as 'id'. If another property of the
     * given feature has the same name ('id'), it is ignored.
     * @param feature The feature to copy.
     */
    public DefaultFeature(Feature feature) {

        if (feature.hasGeometry()) {
            setGeometry(feature.getGeometryName(), feature.getGeometry());
        }
        if (feature.hasId()) {
            setId("id", feature.getId());
        }
        for (String propertyName : feature.getProperties()) {
            if (!"id".equals(propertyName)) {
                addProperty(propertyName ,feature.getProperty(propertyName));
            }
        }
    }

    /**
     * Sets the geometry property of the feature. This method also allows wiping the current geometry
     * by setting the name of the property to null. The value will in that case be ignored.
     * @param name the name of the geometry property, or null if the geometry property is to be wiped
     * @param value the value of the geometry property of this feature
     */
    public void setGeometry(String name, Geometry value)
    {
        if (name == null)
        {
            geomPropertyName = null;
            geomValue = null;
        }
        else
        {
            geomPropertyName= name;
            geomValue = value;
        }
    }

    /**
     * Sets the id property of the feature. This method also allows wiping the current id
     * by setting the name of the id to null. The value will in that case be ignored.
     * @param name the name of the id property, or null if the geometry property is to be wiped
     * @param value the value of the id property of this feature
     */
    public void setId(String name, Object value)
    {
        if (name == null)
        {
           idPropertyName = null;
           idValue = null;
        }
        else
        {
            idPropertyName= name;
            idValue = value;
        }
    }

    /**
     * The name of the property to add. if it already exists, the value is updated. If the propertyname is null,
     * the property addition is ignored.
     * @param propertyName the name of the property to add
     * @param value the value to assign to the given property.
     */
    public void addProperty(String propertyName, Object value)
    {
        if (propertyName != null)
        {
            propertyNames.add(propertyName);
            if (value == null)
            {
                properties.remove(propertyName);
            }
            else
            {
                properties.put(propertyName, value);
            }
        }
    }


    /**
     * If a property with the specified name exists, it is removed from this feature. if the given propertyname
     * is null, the call is ignored. Note that this method has no effect whatsoever regarding the geometry or id property.
     * @param propertyName the name of the property to wipe.
     */
    public void wipeProperty(String propertyName)
    {
        if (propertyName != null)
        {
            propertyNames.remove(propertyName);
            if (properties.containsKey(propertyName))
            {
                properties.remove(propertyName);
            }
        }
    }

    public boolean hasProperty(String propertyName, boolean trueForSpecialProperties) {
        return propertyNames.contains(propertyName)
                || trueForSpecialProperties && (geomPropertyName!=null && geomPropertyName.equals(propertyName)
                                                || idPropertyName != null && idPropertyName.equals(propertyName));

    }

    public Collection<String> getProperties() {
        return new ArrayList<String>(propertyNames);
    }

    public Object getProperty(String propertyName) {
        return properties.get(propertyName);
    }

    public Object getId() {
        if (idPropertyName != null)
        {
            return idValue;
        }
        return null;
    }

    public Geometry getGeometry() {
        if (geomPropertyName != null)
        {
            return geomValue;
        }
        return null;
    }

    public String getGeometryName() {
        return geomPropertyName;
    }

    public boolean hasId() {
        return idPropertyName != null;
    }

    public boolean hasGeometry() {
        return geomPropertyName != null;
    }
}
