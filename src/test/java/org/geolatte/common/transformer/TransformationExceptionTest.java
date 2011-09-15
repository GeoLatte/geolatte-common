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

package org.geolatte.common.transformer;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the TransformationException class. This is mainly to keep test coverage high since the class is hardly worth testing.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 23-Apr-2010<br>
 * <i>Creation-Time</i>:  17:54:31<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class TransformationExceptionTest {

    @Test
    public void test_Constructor_failedObject() {

        Object failedObject = new Object();
        TransformationException exception = new TransformationException(failedObject);
        Assert.assertEquals(failedObject, exception.getFailedObject());

        failedObject = null;
        exception = new TransformationException(failedObject);
        Assert.assertEquals(failedObject, exception.getFailedObject());
    }

    @Test
    public void test_Constructor_message_failedObject() {

        Object failedObject = new Object();
        TransformationException exception = new TransformationException("test", failedObject);
        Assert.assertEquals(failedObject, exception.getFailedObject());

        failedObject = null;
        exception = new TransformationException("test", failedObject);
        Assert.assertEquals(failedObject, exception.getFailedObject());
    }

    @Test
    public void test_Constructor_message_throwable_failedObject() {

        Object failedObject = new Object();
        TransformationException exception = new TransformationException("test", new Exception(), failedObject);
        Assert.assertEquals(failedObject, exception.getFailedObject());

        failedObject = null;
        exception = new TransformationException("test", new Exception(), failedObject);
        Assert.assertEquals(failedObject, exception.getFailedObject());
    }

    @Test
    public void test_Constructor_throwable_failedObject() {

        Object failedObject = new Object();
        TransformationException exception = new TransformationException(new Exception(), failedObject);
        Assert.assertEquals(failedObject, exception.getFailedObject());

        failedObject = null;
        exception = new TransformationException(new Exception(), failedObject);
        Assert.assertEquals(failedObject, exception.getFailedObject());
    }
}
