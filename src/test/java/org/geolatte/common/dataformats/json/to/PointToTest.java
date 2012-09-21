package org.geolatte.common.dataformats.json.to;

import junit.framework.Assert;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

/**
 * Unit test for {@link PointTo}
 *
 * @author Bert Vanhooff [<a href="http://www.qmino.com">Qmino bvba</a>]
 */
public class PointToTest {

    @Test
    public void testEquals() throws Exception {

        // Checks a number of standard properties of the equals contract
        EqualsVerifier.forClass(PointTo.class).withRedefinedSuperclass().suppress(Warning.NONFINAL_FIELDS).verify();

        CrsTo crsTo1 = ToTestHelper.createCrsTo("EPSG:900913");
        CrsTo crsTo2 = ToTestHelper.createCrsTo("EPSG:4326");

        PointTo first = null;
        PointTo second = null;

        // Equal objects
        first = new PointTo(crsTo1, new double[]{1, 2});
        second = new PointTo(crsTo1, new double[]{1, 2});
        Assert.assertTrue(first.equals(second));
        Assert.assertTrue(second.equals(first));

        // Different CRS
        first = new PointTo(crsTo1, new double[]{1, 2});
        second = new PointTo(crsTo2, new double[]{1, 2});
        EqualsVerifier.forExamples(first, second).withRedefinedSuperclass().suppress(Warning.NONFINAL_FIELDS).verify();

        // Different Coordinates
        first = new PointTo(crsTo1, new double[]{1, 2});
        second = new PointTo(crsTo1, new double[]{2, 2});
        EqualsVerifier.forExamples(first, second).withRedefinedSuperclass().suppress(Warning.NONFINAL_FIELDS).verify();

        first = new PointTo(crsTo1, new double[]{2, 2});
        second = new PointTo(crsTo1, new double[]{2, 1});
        EqualsVerifier.forExamples(first, second).withRedefinedSuperclass().suppress(Warning.NONFINAL_FIELDS).verify();
    }
}
