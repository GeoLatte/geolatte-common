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

package org.geolatte.core.expressions;

import org.geolatte.core.transformer.Transformation;
import org.geolatte.core.transformer.TransformationException;

/**
 * <p>
 * A {@link org.geolatte.core.transformer.Transformation} that applies a {@link Filter} to its input object and returns a Boolean indicating whether the input passed the filter.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 01-Jul-2010<br>
 * <i>Creation-Time</i>:  15:13:32<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 * @param <T> The input type of this Transformation.
 */
public class FilterTransformation<T> implements Transformation<T, Boolean> {

    private Filter filter;

    /**
     * Initializes a <pre>FilterTransformation</pre> using the given {@link Filter}}
     * @param filter The filter to apply. If null, no filtering is done (every object passes).
     */
    public FilterTransformation(Filter filter) {

        this.filter = filter;
    }

    public Boolean transform(T input) throws TransformationException {

        if (filter == null)
            return true;

        return filter.evaluate(input);
    }
}
