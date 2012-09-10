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
public class MultiPolygonToTest {

    @Test
    public void testEquals() throws Exception {

        // Checks a number of standard properties of the equals contract
        EqualsVerifier.forClass(MultiPolygonTo.class).withRedefinedSuperclass().suppress(Warning.NONFINAL_FIELDS).verify();

        CrsTo crsTo1 = ToTestHelper.createCrsTo("EPSG:900913");
        CrsTo crsTo2 = ToTestHelper.createCrsTo("EPSG:4326");

        MultiPolygonTo first = null;
        MultiPolygonTo second = null;

        // Equal objects
        first = new MultiPolygonTo(crsTo1, new double[][][][]{{{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}},
                                                              {{{9, 10}, {11, 12}}, {{13, 14}, {15, 16}}}});
        second = new MultiPolygonTo(crsTo1, new double[][][][]{{{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}},
                                                               {{{9, 10}, {11, 12}}, {{13, 14}, {15, 16}}}});
        Assert.assertTrue(first.equals(second));
        Assert.assertTrue(second.equals(first));

        // Different CRS
        first = new MultiPolygonTo(crsTo1, new double[][][][]{{{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}},
                                                              {{{9, 10}, {11, 12}}, {{13, 14}, {15, 16}}}});
        second = new MultiPolygonTo(crsTo2, new double[][][][]{{{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}},
                                                               {{{9, 10}, {11, 12}}, {{13, 14}, {15, 16}}}});
        EqualsVerifier.forExamples(first, second).withRedefinedSuperclass().suppress(Warning.NONFINAL_FIELDS).verify();

        // Different Coordinates
        first = new MultiPolygonTo(crsTo1, new double[][][][]{{{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}},
                                                              {{{9, 10}, {11, 12}}, {{13, 14}, {15, 16}}}});
        second = new MultiPolygonTo(crsTo1, new double[][][][]{{{{17, 2}, {3, 4}}, {{5, 6}, {7, 8}}},
                                                               {{{9, 10}, {11, 12}}, {{13, 14}, {15, 16}}}});
        EqualsVerifier.forExamples(first, second).withRedefinedSuperclass().suppress(Warning.NONFINAL_FIELDS).verify();
    }
}
