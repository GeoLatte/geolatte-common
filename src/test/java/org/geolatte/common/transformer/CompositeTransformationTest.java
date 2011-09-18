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

import org.geolatte.testobjects.CapitalizeTransformation;
import org.geolatte.testobjects.CharacterCountTransformation;
import org.geolatte.testobjects.CharacterNumberTransformation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * <i>Creation-Date</i>: 18-Mar-2010<br>
 * <i>Creation-Time</i>:  18:44:00<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class CompositeTransformationTest {

    private Transformation<Double, String> numberTransformation;
    private Transformation<String, String> capTransformation;
    private Transformation<String, Integer> countTransformation;

    @Before
    public void setUp() throws Exception {

        numberTransformation = new CharacterNumberTransformation();
        capTransformation = new CapitalizeTransformation();
        countTransformation = new CharacterCountTransformation();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test_CreateAndExecuteChain() throws Exception {

        TransformationChainFactory<Double, Integer> compositeTransformationFactory = new TransformationChainFactory<Double, Integer>();

        Transformation<Double, Integer> compositeTransformation = compositeTransformationFactory.first(numberTransformation)
                                                                                                .add(capTransformation)
                                                                                                .add(capTransformation)
                                                                                                .last(countTransformation);

        Integer output = compositeTransformation.transform(1234.5678);
        Assert.assertEquals(9, output);

        // Try to reuse the same factory (should reset the chain)
        compositeTransformation = compositeTransformationFactory.first(numberTransformation)
                                                                .last(countTransformation);

        output = compositeTransformation.transform(8765.4321);
        Assert.assertEquals(9, output);
        
    }
}
