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


import org.geolatte.geom.Geometry;

import java.util.Date;

/**
 * <p>
 * This interface is part of the Visitor patter implemented on the {@link Expression} class that allows switching on Expression type.
 * This interface represents the Visitor part, {@link Expression} itself represents the Element that accepts a BasicTypeSwitch.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 09-Aug-2010<br>
 * <i>Creation-Time</i>:  14:39:23<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public interface BasicTypeSwitch {

    /**
     * Method called when {@link Expression#switchOn(BasicTypeSwitch)} is called on a Number valued expression.
     * @param value The Expression on which the switch was invoked.
     */
    void caseNumber(Expression<Number> value);

    /**
     * Method called when {@link Expression#switchOn(BasicTypeSwitch)} is called on a Short valued expression.
     * @param value The Expression on which the switch was invoked.
     */
    void caseShort(Expression<Short> value);

    /**
     * Method called when {@link Expression#switchOn(BasicTypeSwitch)} is called on an Integer valued expression.
     * @param value The Expression on which the switch was invoked.
     */
    void caseInteger(Expression<Integer> value);

    /**
     * Method called when {@link Expression#switchOn(BasicTypeSwitch)} is called on a Long valued expression.
     * @param value The Expression on which the switch was invoked.
     */
    void caseLong(Expression<Long> value);

    /**
     * Method called when {@link Expression#switchOn(BasicTypeSwitch)} is called on a Float valued expression.
     * @param value The Expression on which the switch was invoked.
     */
    void caseFloat(Expression<Float> value);

    /**
     * Method called when {@link Expression#switchOn(BasicTypeSwitch)} is called on a Double valued expression.
     * @param value The Expression on which the switch was invoked.
     */
    void caseDouble(Expression<Double> value);

    /**
     * Method called when {@link Expression#switchOn(BasicTypeSwitch)} is called on a Boolean valued expression.
     * @param value The Expression on which the switch was invoked.
     */
    void caseBoolean(Expression<Boolean> value);

    /**
     * Method called when {@link Expression#switchOn(BasicTypeSwitch)} is called on a String valued expression.
     * @param value The Expression on which the switch was invoked.
     */
    void caseString(Expression<String> value);

    /**
     * Method called when {@link Expression#switchOn(BasicTypeSwitch)} is called on a Date valued expression.
     * @param value The Expression on which the switch was invoked.
     */
    void caseDate(Expression<Date> value);

    /**
     * Method called when {@link Expression#switchOn(BasicTypeSwitch)} is called on a {@link Geometry} valued expression.
     * @param value The Expression on which the switch was invoked.
     */
    void caseGeometry(Expression<Geometry> value);
}
