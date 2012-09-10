package org.geolatte.common.dataformats.json.to;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link CrsTo}.
 *
 * @author Bert Vanhooff [<a href="http://www.qmino.com">Qmino bvba</a>]
 */
public class CrsToTest {

    @Test
    public void testEquals() throws Exception {

        // Checks a number of standard properties of the equals contract
        EqualsVerifier.forClass(CrsTo.class).withRedefinedSuperclass().suppress(Warning.NONFINAL_FIELDS).verify();

        CrsTo first = null;
        CrsTo second = null;

        // Equal
        first = ToTestHelper.createCrsTo("EPSG:900913");
        second = ToTestHelper.createCrsTo("EPSG:900913");
        Assert.assertTrue(first.equals(second));
        Assert.assertTrue(second.equals(first));

        // Unequal
        first = ToTestHelper.createCrsTo("EPSG:4326");
        second = ToTestHelper.createCrsTo("EPSG:900913");
        EqualsVerifier.forExamples(first, second);
        Assert.assertFalse(first.equals(second));
        Assert.assertFalse(second.equals(first));
    }
}
