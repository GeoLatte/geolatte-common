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

import java.util.ArrayList;

/**
 * A very simple TransformerSource that simply passes a given data collection to its output() method.
 *
 * @param <T> The data type accepted by this transformer source.
 *
 * <p>
 * <i>Creation-Date</i>: 19-Mar-2010<br>
 * <i>Creation-Time</i>:  14:45:44<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class SimpleTransformerSource<T> extends TransformerSource<T> {

    private Iterable<T> outputData;

    /**
     * Initializes a SimpleTransformationSource with the given data. If the given data is null, the output() will be empty.
     *
     * @param outputData The initial data.
     */
    public SimpleTransformerSource(Iterable<T> outputData) {

        if (outputData == null)
            this.outputData = new ArrayList<T>();
        else
            this.outputData = outputData;

    }

    /**
     * {@inheritDoc}
     */
    public Iterable<T> output() {
        return outputData;
    }
}
