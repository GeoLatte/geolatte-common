/*
 * This file is part of the GeoLatte project.
 *
 * GeoLatte is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GeoLatte is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GeoLatte.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2010 - 2010 and Ownership of code is shared by:
 * Qmino bvba - Romeinsestraat 18 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.common.expressions.geo;

import com.vividsolutions.jts.geom.Geometry;
import org.geolatte.common.expressions.PropertyExpression;
import org.geolatte.common.reflection.EntityClassReader;
import org.geolatte.common.reflection.InvalidObjectReaderException;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 31-Aug-2010<br>
 * <i>Creation-Time</i>:  20:49:36<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class GeometryProperty extends GeometryExpression implements PropertyExpression<Geometry> {

    private final String propertyName;

    /**
     * Constructor.
     *
     * @param propertyName The name of the property that will be evaluated.
     */
    public GeometryProperty(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * Gets the property's name.
     *
     * @return The name of the property.
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Evaluates the given object against this expression.
     *
     * @param o The object to evaluate.
     * @return True if the given object matches this expression, false otherwise.
     */
    public Geometry evaluate(Object o) {

        EntityClassReader classReader = EntityClassReader.getClassReaderFor(o.getClass(), null, "id");
        Object result = null;
        try {
            result = classReader.getPropertyValue(o, propertyName);
        }
        catch (InvalidObjectReaderException e) {
            ; // can never occur
        }

        if (!(result instanceof Geometry))
                return null;

        return (Geometry)result;
    }
}
