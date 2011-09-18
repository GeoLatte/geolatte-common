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

import org.geolatte.common.reflection.EntityClassReader;
import org.geolatte.common.reflection.InvalidObjectReaderException;

/**
 * <p>
 * Represents a Number valued property.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 9-apr-2010<br>
 * <i>Creation-Time</i>:  11:48:54<br>
 * </p>
 *
 * @author Peter Rigole
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class NumberProperty extends NumberExpression implements PropertyExpression<Number> {

    private final String propertyName;

    /**
     * Constructor.
     *
     * @param propertyName The name of the property that will be evaluated.
     */
    public NumberProperty(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * Evaluates the property with name propertyName on the given object.
     *
     * @param o The object to evaluate a property on.
     * @return The value of the property on the given object.
     */
    public Number evaluate(Object o) {

        EntityClassReader classReader = EntityClassReader.getClassReaderFor(o.getClass());
        Object result = null;
        try {
            result = classReader.getPropertyValue(o, propertyName);
        }
        catch (InvalidObjectReaderException e) {
            ; // can never occur
        }

        if (!(result instanceof Number))
                return null;

        return (Number)result;
    }

    public String getPropertyName() {
        return propertyName;
    }

}