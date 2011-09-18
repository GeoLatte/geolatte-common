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

package org.geolatte.common.reflection;

import org.junit.Assert;
import org.junit.Test;

/**
 * Dummy test for exception class
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 19-apr-2010<br>
 * <i>Creation-Time</i>: 16:20:29<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class InvalidObjectReaderExceptionTest {

    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    @Test
    public void ConstructorTest()
    {
        InvalidObjectReaderException exc = new InvalidObjectReaderException();
        Assert.assertEquals(null, exc.getMessage());
        Assert.assertEquals(null, exc.getCause());

        String message = "Hallo";
        Exception cause = new IllegalArgumentException();
        exc = new InvalidObjectReaderException(message);
        Assert.assertEquals(message, exc.getMessage());
        Assert.assertEquals(null, exc.getCause());

        exc = new InvalidObjectReaderException(cause);
        Assert.assertEquals(cause, exc.getCause());

        exc = new InvalidObjectReaderException(message, cause);
        Assert.assertEquals(message, exc.getMessage());
        Assert.assertEquals(cause, exc.getCause());
    }
}
