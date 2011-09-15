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

package org.geolatte.common.transformer;

/**
 * This class wraps a Transformation and offers an untyped transformGeneric() method.
 * This method delegates to the typed transform() method of the encapsulated Transformation
 *
 * @param <Source> The source type of the transformation.
 * @param <Target> The target type of the transformation.
 *
 * <p>
 * <i>Creation-Date</i>: 19-Mar-2010<br>
 * <i>Creation-Time</i>:  11:21:23 <br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
class GenericTransformationWrapper<Source, Target> implements Transformation<Source, Target> {

    private Transformation<Source, Target> transformation;

    GenericTransformationWrapper(Transformation<Source, Target> delegateTransformation) {

        transformation = delegateTransformation;
    }

    public Target transform(Source input) throws TransformationException {

        return transformation.transform(input);
    }

    public Target transformGeneric(Object input) throws TransformationException {

        return transformation.transform((Source)input);
    }
}
