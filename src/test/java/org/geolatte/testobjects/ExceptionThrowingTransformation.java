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

import org.geolatte.core.transformer.Transformation;
import org.geolatte.core.transformer.TransformationException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>
 * Test transformation, which throws TransformationExceptions.
 * This transformation just copies its input to its output or throws an exception.
 * </p>
 *
 * <p>
 * <i>Creation-Date</i>: 23-Apr-2010<br>
 * <i>Creation-Time</i>:  16:42:34<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class ExceptionThrowingTransformation<Source> implements Transformation<Source, Source> {

    private int invocationCounter = 0;
    private ArrayList<Integer> throwOnInvocations;

    /**
     * Creates a transformation that throws a TransformationException of the 'throwsOnInvocation'th invocation of {@link #transform(Object)}.
     *
     * @param throwOnInvocations The iterations on which an exception is thrown.
     */
    public ExceptionThrowingTransformation(Integer ... throwOnInvocations) {

        this.throwOnInvocations = new ArrayList<Integer>(Arrays.asList(throwOnInvocations));
    }

    /**
     * {@inheritDoc}
     */
    public Source transform(Source input) throws TransformationException {

        invocationCounter++;

        if (throwOnInvocations.contains(invocationCounter))
            throw new TransformationException("Deliberate error on " + invocationCounter + "th invocation for testing purposes", input);

        return input;
    }
}
