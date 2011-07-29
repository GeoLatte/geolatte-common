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

package org.geolatte.cql;

import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 12-Jul-2010<br>
 * <i>Creation-Time</i>:  11:56:53<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public abstract class  AbstractCriteriaBuilderTest {

    protected String cqlString;
    protected Object testObject;
    protected boolean expectedResult;

    /**
     * Creates the collection of data to run tests against. For each element in this collection, a FilterExpressionBuilderTest is instantiated and executed.
     * @return A collection of test data.
     * @throws Exception If a testcase could not be loaded
     */
    @Parameterized.Parameters
    public static Collection data() throws Exception {

        ArrayList<Object[]> list = new ArrayList<Object[]>();

        for (CQLTestCase testCase : CqlTestClassConstructor.loadTestCases()) {

            for (CQLTestCase.Verification verification : testCase.verifications)
                list.add(new Object[]{testCase.searchCondition, verification.generateTestObject(), verification.expectedResult});
        }

        return list;
    }

    /**
     * Constructor
     * @param cqlString The CQL string to test.
     * @param testObject The object to give to the filter.
     * @param expectedResult The expected outcome of the test.
     */
    public AbstractCriteriaBuilderTest(String cqlString, Object testObject, boolean expectedResult) {
        super();

        this.cqlString = cqlString;
        this.testObject = testObject;
        this.expectedResult = expectedResult;
    }
}
