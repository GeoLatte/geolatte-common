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

package org.geolatte.common.reflection;

import org.geolatte.common.Feature;
import org.geolatte.common.transformer.Transformation;
import org.geolatte.common.transformer.TransformationException;

import java.util.HashMap;

/**
 * A base transformation that wraps any given object into a feature interface. The transformation
 * may be subsequently called with objects of different classes.
 * If a given object is null, null will be returned as output.

 * <p>
 * <i>Creation-Date</i>: 23-apr-2010<br>
 * <i>Creation-Time</i>: 12:41:46<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class ObjectToFeatureTransformation implements Transformation<Object, Feature> {

    HashMap<Class, EntityClassReader> readers = new HashMap<Class, EntityClassReader>();

    /**
     * Transforms any object into a feature. If the given object is null, null is returned.
     * @param input The given input
     * @return a feature-wrapper around the given object
     * @throws TransformationException if the object can not be completed
     */
    public Feature transform(Object input) throws TransformationException {
        if (input == null) {
            return null;
        } else {
            EntityClassReader reader = readers.get(input.getClass());
            if (reader == null) {
                reader = EntityClassReader.getClassReaderFor(input.getClass());
                readers.put(input.getClass(), reader);
            }
            try {
                return reader.asFeature(input);
            } catch (InvalidObjectReaderException e) {
                // Can't happen!
                throw new TransformationException("Transformation failed", e);
            }
        }
    }
}
