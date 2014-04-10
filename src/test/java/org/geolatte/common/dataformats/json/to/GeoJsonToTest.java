package org.geolatte.common.dataformats.json.to;

import junit.framework.Assert;
import org.junit.Test;

/**
 *
 */
public class GeoJsonToTest {

    @Test
    public void testCreateBoundingBoxUsesZeroInsteadOfDouble_MIN_VALUE() {

        double[] boundingBox = GeoJsonTo.createBoundingBox(new double[][]{new double[]{0.0, 1.0}, new double[]{0.0, 1.0}});

        Assert.assertEquals(0.0,boundingBox[0]);
        Assert.assertEquals(1.0,boundingBox[1]);
        Assert.assertEquals(0.0,boundingBox[2]);
        Assert.assertEquals(1.0,boundingBox[3]);
    }

}
