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

package org.geolatte.core.transformer;
/**
 * This interface is the basic representation of a transformation that takes a single input element and transforms it into a single output element.
 * @param <Source> The input type of this Transformation.
 * @param <Target> The output type of this Transformation.
 *
 * <p>
 * <i>Creation-Date</i>: 18-Mar-2010<br>
 * <i>Creation-Time</i>:  16:40:03<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public interface Transformation<Source, Target> {

    /**
     * Transforms a single input object into an output object.
     * @param input The given input
     * @return The output of the transformation
     * @throws TransformationException If for some reason, the transformation can not be executed
     */
    public Target transform(Source input) throws TransformationException;
}
