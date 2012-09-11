package org.geolatte.common.dataformats.json.to;

import junit.framework.Assert;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

/**
 * Unit test for {@link MultiLineStringTo}.
 *
 * @author Bert Vanhooff [<a href="http://www.qmino.com">Qmino bvba</a>]
 */
public class MultiLineStringToTest {

    @Test
    public void testEquals() throws Exception {

        // Checks a number of standard properties of the equals contract
        EqualsVerifier.forClass(MultiLineStringTo.class).withRedefinedSuperclass().suppress(Warning.NONFINAL_FIELDS).verify();

        CrsTo crsTo1 = ToTestHelper.createCrsTo("EPSG:900913");
        CrsTo crsTo2 = ToTestHelper.createCrsTo("EPSG:4326");

        MultiLineStringTo first = null;
        MultiLineStringTo second = null;

        // Equal objects
        first = new MultiLineStringTo(crsTo1, new double[][][]{{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}});
        second = new MultiLineStringTo(crsTo1, new double[][][]{{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}});
        Assert.assertTrue(first.equals(second));
        Assert.assertTrue(second.equals(first));

        // Different CRS
        first = new MultiLineStringTo(crsTo1, new double[][][]{{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}});
        second = new MultiLineStringTo(crsTo2, new double[][][]{{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}});
        EqualsVerifier.forExamples(first, second).withRedefinedSuperclass().suppress(Warning.NONFINAL_FIELDS).verify();

        // Different Coordinates
        first = new MultiLineStringTo(crsTo1, new double[][][]{{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}});
        second = new MultiLineStringTo(crsTo1, new double[][][]{{{9, 2}, {3, 4}}, {{5, 6}, {7, 8}}});
        EqualsVerifier.forExamples(first, second).withRedefinedSuperclass().suppress(Warning.NONFINAL_FIELDS).verify();
    }
}
