package org.geolatte.common.dataformats.json.to;

import junit.framework.Assert;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

/**
 * Unit test for {@link MultiPointTo}.
 *
 * @author Bert Vanhooff [<a href="http://www.qmino.com">Qmino bvba</a>]
 */
public class MultiPointToTest {

    @Test
    public void testEquals() throws Exception {

        // Checks a number of standard properties of the equals contract
        EqualsVerifier.forClass(MultiPointTo.class).withRedefinedSuperclass().suppress(Warning.NONFINAL_FIELDS).verify();

        CrsTo crsTo1 = ToTestHelper.createCrsTo("EPSG:900913");
        CrsTo crsTo2 = ToTestHelper.createCrsTo("EPSG:4326");

        MultiPointTo first = null;
        MultiPointTo second = null;

        // Equal objects
        first = new MultiPointTo(crsTo1, new double[][]{{1, 2}, {3, 4}});
        second = new MultiPointTo(crsTo1, new double[][]{{1, 2}, {3, 4}});
        Assert.assertTrue(first.equals(second));
        Assert.assertTrue(second.equals(first));

        // Different CRS
        first = new MultiPointTo(crsTo1, new double[][]{{1, 2}, {3, 4}});
        second = new MultiPointTo(crsTo2, new double[][]{{1, 2}, {3, 4}});
        EqualsVerifier.forExamples(first, second).withRedefinedSuperclass().suppress(Warning.NONFINAL_FIELDS).verify();

        // Different Coordinates
        first = new MultiPointTo(crsTo1, new double[][]{{1, 2}, {3, 4}});
        second = new MultiPointTo(crsTo1, new double[][]{{5, 2}, {3, 4}});
        EqualsVerifier.forExamples(first, second).withRedefinedSuperclass().suppress(Warning.NONFINAL_FIELDS).verify();
    }
}
