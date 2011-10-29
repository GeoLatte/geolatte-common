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

package org.geolatte.common.cql;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.text.ParseException;

/**
 * <p>
 * Data driven test for CQL->Filter expressions.
 * The actual test cases are described in the <resources>/cqlTestInput.xml file. 
 * </p>
 * <p>
 * <i>Creation-Date</i>: 26-May-2010<br>
 * <i>Creation-Time</i>:  14:24:42<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
@SuppressWarnings({"ParameterizedParametersStaticCollection"}) // @Parameterized.Parameters is defined in super class
@RunWith(Parameterized.class)
public class FilterExpressionBuilderTest extends AbstractCriteriaBuilderTest {

    /**
     * Constructor
     *
     * @param cqlString      The CQL string to test.
     * @param testObject     The object to give to the filter.
     * @param expectedResult The expected outcome of the test.
     */
    public FilterExpressionBuilderTest(String cqlString, Object testObject, boolean expectedResult) {
        super(cqlString, testObject, expectedResult);
    }

    /**
     * Verifies a single test case.
     * @throws Exception
     */
    @Test
    public void parameterizedTest() throws Exception {

        // Try/catch and re-throw enables us to see which test failed exactly
        try{
            System.out.println("Verifying that \"" + cqlString + "\" is " + expectedResult + " with ");
            System.out.println("    " + testObject);


            try {
                CqlFilter filter = Cql.toFilter(cqlString);
                Assert.assertEquals(expectedResult, filter.evaluate(testObject));
            }
            catch (ParseException e) {
                Assert.fail("Parsing failed");
            }
        }
        catch(AssertionError e) {

            throw new AssertionError(e.getMessage() + " / CQL=" + cqlString + " | Expected=" + expectedResult + " | object=" + testObject.toString());
        }
        catch(Exception e) {

            throw new Exception(e.getMessage() + " / CQL=" + cqlString + " | Expected=" + expectedResult + " | object=" + testObject.toString(), e);
        }
    }
}
