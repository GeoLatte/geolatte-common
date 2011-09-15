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

package org.geolatte.common.geo;

/**
 * Generic interface for a typeconverter. A typeconverter is a class that converts a string into a type specified
 * by the generic parameter.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 10-mei-2010<br>
 * <i>Creation-Time</i>: 10:36:07<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public interface TypeConverter<T> {

        /**
         * Converts the given string into an object of T. The method must be threadsafe.
         * @param inputString The object to convert
         * @return An object of class T that is initialized based on the given string.
         * @throws TypeConversionException If the string is invalid and can not be converted into an object of T.
         */
        T convert(String inputString)
                throws TypeConversionException;

    }
