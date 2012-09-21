package org.geolatte.common.dataformats.json.to;

import junit.framework.Assert;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link GeometryCollectionTo}
 *
 * @author Bert Vanhooff [<a href="http://www.qmino.com">Qmino bvba</a>]
 */
public class GeometryCollectionToTest {

    @Test
    public void testEquals() throws Exception {

        // Checks a number of standard properties of the equals contract
        EqualsVerifier.forClass(GeometryCollectionTo.class).withRedefinedSuperclass().suppress(Warning.NONFINAL_FIELDS).verify();

        CrsTo crsTo1 = ToTestHelper.createCrsTo("EPSG:900913");
        CrsTo crsTo2 = ToTestHelper.createCrsTo("EPSG:4326");

        GeoJsonTo geoTo1 = Mockito.mock(GeoJsonTo.class);
        GeoJsonTo geoTo2 = Mockito.mock(GeoJsonTo.class);

        GeometryCollectionTo first = null;
        GeometryCollectionTo second = null;

        first = new GeometryCollectionTo();
        second = new GeometryCollectionTo();
        Assert.assertTrue(first.equals(second));
        Assert.assertTrue(second.equals(first));

        first = new GeometryCollectionTo(crsTo1, new GeoJsonTo[]{});
        second = new GeometryCollectionTo(crsTo1, new GeoJsonTo[]{});
        Assert.assertTrue(first.equals(second));
        Assert.assertTrue(second.equals(first));

        first = new GeometryCollectionTo(crsTo1, new GeoJsonTo[]{geoTo1});
        second = new GeometryCollectionTo(crsTo1, new GeoJsonTo[]{geoTo1});
        Assert.assertTrue(first.equals(second));
        Assert.assertTrue(second.equals(first));

        first = new GeometryCollectionTo(crsTo1, new GeoJsonTo[]{geoTo1});
        second = new GeometryCollectionTo(crsTo1, new GeoJsonTo[]{geoTo1, geoTo2});
        EqualsVerifier.forExamples(first, second).withRedefinedSuperclass().suppress(Warning.NONFINAL_FIELDS).verify();

        first = new GeometryCollectionTo(crsTo1, new GeoJsonTo[]{});
        second = new GeometryCollectionTo(crsTo2, new GeoJsonTo[]{});
        EqualsVerifier.forExamples(first, second).withRedefinedSuperclass().suppress(Warning.NONFINAL_FIELDS).verify();

        first = new GeometryCollectionTo(crsTo1, new GeoJsonTo[]{geoTo1});
        second = new GeometryCollectionTo(crsTo1, new GeoJsonTo[]{geoTo2});
        EqualsVerifier.forExamples(first, second).withRedefinedSuperclass().suppress(Warning.NONFINAL_FIELDS).verify();
    }
}
