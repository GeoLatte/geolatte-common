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

    @Test
    public void testCreateBoundingBoxIgnoresMValuesForPoint() {

        double[] boundingBox = GeoJsonTo.createBoundingBox(new double[]{0.0, 1.0, 2.0, 66.0});

        Assert.assertEquals(0.0,boundingBox[0]);
        Assert.assertEquals(1.0,boundingBox[1]);
        Assert.assertEquals(2.0,boundingBox[2]);
        Assert.assertEquals(0.0,boundingBox[3]);
        Assert.assertEquals(1.0,boundingBox[4]);
        Assert.assertEquals(2.0,boundingBox[5]);
    }

    @Test
    public void testCreateBoundingBoxIgnoresMValuesForLinestring() {

        double[] boundingBox = GeoJsonTo.createBoundingBox(new double[][]{new double[]{0.0, 1.0, 2.0, 66.0}, new double[]{3.0, 4.0, 5.0, 77.0}});

        Assert.assertEquals(0.0,boundingBox[0]);
        Assert.assertEquals(1.0,boundingBox[1]);
        Assert.assertEquals(2.0,boundingBox[2]);
        Assert.assertEquals(3.0,boundingBox[3]);
        Assert.assertEquals(4.0,boundingBox[4]);
        Assert.assertEquals(5.0,boundingBox[5]);

    }
}
