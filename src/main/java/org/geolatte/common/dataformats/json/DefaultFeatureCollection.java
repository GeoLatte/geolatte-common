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

package org.geolatte.common.dataformats.json;

import org.geolatte.common.Feature;
import org.geolatte.common.FeatureCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A trivial base implementation of the featurecollection interface
 * <p>
 * <i>Creation-Date</i>: 1-sep-2010<br>
 * <i>Creation-Time</i>: 18:46:30<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class DefaultFeatureCollection implements FeatureCollection {

    private List<Feature> featureList;

    /**
     * Creates an empty featurecollection.
     */
    public DefaultFeatureCollection()
    {
        featureList = new ArrayList<Feature>();
    }

    /**
     * Constructor that initializes a featurecollection based on a list of features
     * @param features a collection of features
     */
    public DefaultFeatureCollection(Collection<Feature> features)
    {
        featureList = new ArrayList<Feature>(features);
    }

    /**
     * Adds a feature to this collection. Duplicates are permitted.
     * @param toAdd the feature to add.
     */
    public void addFeature(Feature toAdd)
    {
        featureList.add(toAdd);
    }

    /**
     * Removes one instance of the given feature if it is present in the featurelist.
     * @param toRemove the instance to remove from this collection
     * @return true if an item was removed, false otherwise
     */
    public boolean removeFeature(Feature toRemove)
    {
        return featureList.remove(toRemove);
    }

    /**
     * @return a direct reference to the list of features in this featurecollection
     */
    public List<Feature> getFeatures() {
        return featureList;
    }
}
