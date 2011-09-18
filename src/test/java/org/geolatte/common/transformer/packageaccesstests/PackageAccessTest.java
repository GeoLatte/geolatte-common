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

package org.geolatte.common.transformer.packageaccesstests;

import org.geolatte.common.transformer.DefaultTransformer;
import org.geolatte.testobjects.CapitalizeTransformation;
import org.junit.Test;

/**
 * <p>
 * This class contains some syntactic checks with respect to accessibility of methods and classes (especially package local) for third party users. Hence it lives in a rather artificial namespace.
 * It does not contain any functional tests and should always pass. In case an access modifier is changed, this class may cause build errors.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 05-May-2010<br>
 * <i>Creation-Time</i>:  14:52:16<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class PackageAccessTest {

    @Test
    public void test_TransformersPackageAccess() {

        DefaultTransformer<String, String> transformer = new DefaultTransformer<String, String>(new CapitalizeTransformation());
        // Should not be possible
        //transformer.setInput();
        //transformer.output();
    }
}
