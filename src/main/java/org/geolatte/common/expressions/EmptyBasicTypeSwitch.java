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

package org.geolatte.common.expressions;

import com.vividsolutions.jts.geom.Geometry;

import java.util.Date;

/**
 * <p>
 * A BasicTypeSwitch that does nothing by default.
 * </p>
 * <p>
 * This class is more convenient to use than the raw interface if you only need to react on a subset of the case methods offered.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 09-Aug-2010<br>
 * <i>Creation-Time</i>:  15:06:14<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class EmptyBasicTypeSwitch implements BasicTypeSwitch {

    public void caseNumber(Expression<Number> value) {
    }

    public void caseShort(Expression<Short> value) {
    }

    public void caseInteger(Expression<Integer> value) {
    }

    public void caseLong(Expression<Long> value) {
    }

    public void caseFloat(Expression<Float> value) {
    }

    public void caseDouble(Expression<Double> value) {
    }

    public void caseBoolean(Expression<Boolean> value) {
    }

    public void caseString(Expression<String> value) {
    }

    public void caseDate(Expression<Date> dateExpression) {
    }

    public void caseGeometry(Expression<Geometry> value) {
    }
}
