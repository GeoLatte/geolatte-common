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

package org.geolatte.testobjects;

import org.geolatte.common.transformer.Transformation;
import org.geolatte.common.transformer.TransformationException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 03-Jun-2010<br>
 * <i>Creation-Time</i>:  10:16:49<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class ProgrammableFilter<Source> implements Transformation<Source,Boolean> {

    private int invocationCounter = 0;
    private ArrayList<Integer> falseOnInvocations;

    /**
     * Creates a filter transformation that produces FALSE on each 'falseOnInvocation'th invocation of {@link #transform(Object)}.
     *
     * @param falseOnInvocations The iterations on which false is returned by the transform method.
     */
    public ProgrammableFilter(Integer ... falseOnInvocations) {

        this.falseOnInvocations = new ArrayList<Integer>(Arrays.asList(falseOnInvocations));
    }

    /**
     * {@inheritDoc}
     */
    public Boolean transform(Source input) throws TransformationException {

        invocationCounter++;

        return !falseOnInvocations.contains(invocationCounter);
    }
}
