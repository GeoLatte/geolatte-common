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
import java.util.List;

/**
 * Simple sink that just collects the input it collects and makes it available through the getCollectedOutput() method.
 * The output is available after the start() method has been called.
 *
 * @param <T> The data type accepted by this sink.
 *
 * <p>
 * <i>Creation-Date</i>: 19-Mar-2010<br>
 * <i>Creation-Time</i>:  16:14:36<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class SimpleTransformerSink<T> extends TransformerSink<T> {

    ArrayList<T> collectedOutput;

    private Iterable<? extends T> input;

    /**
     * Sets the data input source for this sink.
     *
     * @param input The data source.
     */
    public void setInput(Iterable<? extends T> input) {
        this.input = input;
    }

    /**
     * Starts iterating over its input.
     */
    public void start() {
        
        collectedOutput = new ArrayList<T>();

        for (T element : input) {

            collectedOutput.add(element);
        }
    }

    /**
     * Pulls all elements from its input and makes them available as the return value of this method.
     *
     * @return The collected output from this sink.
     */
    public List<T> getCollectedOutput() {

        return collectedOutput;
    }
}
