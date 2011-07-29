/*
 * This file is part of the GeoLatte project.
 *
 * GeoLatte is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GeoLatte is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GeoLatte.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2010 - 2010 and Ownership of code is shared by:
 * Qmino bvba - Romeinsestraat 18 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.cql;

import org.junit.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 22-Jun-2010<br>
 * <i>Creation-Time</i>:  18:32:18<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class CqlTestClassConstructor {

    private static final String TEST_DATA_RESOURCE = "/cqlTestInput.xml";

    public static Collection<CQLTestCase> loadTestCases() throws Exception {

        ArrayList<CQLTestCase> testCaseList = new ArrayList<CQLTestCase>();

        try {
            InputStream is = FilterExpressionBuilderTest.class.getResourceAsStream(TEST_DATA_RESOURCE);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(is);
            doc.getDocumentElement().normalize();
            NodeList testCases = doc.getElementsByTagName("testCase");

            // Iterate over all testcases
            for (int i = 0; i < testCases.getLength(); i++) {

                CQLTestCase testCase = new CQLTestCase();

                Element testCaseElement = (Element)testCases.item(i);
                testCase.searchCondition = testCaseElement.getElementsByTagName("searchCondition").item(0).getTextContent();

                // Create setup for all verifications
                NodeList verifications = testCaseElement.getElementsByTagName("verify");

                    for ( int j=0; j < verifications.getLength(); j++) {

                        Element verificationElement = (Element)verifications.item(j);
                        NodeList withNodes = verificationElement.getElementsByTagName("with");
                        if (withNodes.getLength() == 0)
                            continue; // with is not required
                        Node withNode = withNodes.item(0);
                        CQLTestCase.Verification verification = new CQLTestCase.Verification();

                        // SET ATTRIBUTE VALUES
                        for (int k=0; k < withNode.getChildNodes().getLength(); k++) {

                            Node attributeNode = withNode.getChildNodes().item(k);
                            if (!(attributeNode instanceof Element))
                                continue;

                            Element attributeElement = (Element)attributeNode;
                            verification.addProperty(attributeElement.getTagName(), attributeElement.getTextContent());
                        }

                        // RESULT
                        verification.expectedResult = Boolean.parseBoolean(verificationElement.getElementsByTagName("resultIs").item(0).getTextContent().trim());

                        testCase.verifications.add(verification);
                    }

                    testCaseList.add(testCase);
                }
        }
        catch (Exception e) {

            Assert.fail("Failed to load test from xml file.");
            e.printStackTrace();
            throw e;
        }

        return testCaseList;
    }
}
