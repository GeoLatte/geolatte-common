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


import org.geolatte.geom.Envelope;

/**
 * Class that converts a series of (bbox)-coordinates in a string into an Envelope. The class also offers some 
 * parsing methods for bbox coordinates.
 * <p>
 * <i>Creation-Date</i>: 10-mei-2010<br>
 * <i>Creation-Time</i>: 10:33:14<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class EnvelopeConverter implements TypeConverter<Envelope>{
    /**
     * Converts the given string into an Envelope.
     * <br>
     * The inputstring is simply a list of 4 doubles seperated by a , The first two numbers are the first
     * corner of the envelope, the two last make up the second corner. If there are more than 4 numbers present,
     * all numbers are ignored except for the first 4. If there are less than 4 or if the string contains other
     * data except for the expected numberlist, a typeconversionexception is thrown.     *
     *
     * @param inputString The inputstring
     * @return An envelope that consists of the given coordinates.
     * @throws TypeConversionException If the string is invalid and can not be converted into an object of T.
     * @see TypeConverter#convert(String)
     */
    public Envelope convert(String inputString)
            throws TypeConversionException {
        double[] coordinates = getCoordinates(inputString);
        return createEnvelope(coordinates);
    }

    /**
     * Creates an envelope based on a list of coordinates. If less than two coordinates are in the list,
     * a typeconversion exception is thrown. If more than two coordinates are in the list, the minimum X- and
     * Y-coordinates are searched.
     *
     * @param coordinates A list with the coordinates of the envelope
     * @return An envelope with the given coordinates
     * @throws TypeConversionException If the creation of the envelope failed
     */
    public Envelope createEnvelope(double[] coordinates)
            throws TypeConversionException {
        if (coordinates.length < 4) {
            throw new TypeConversionException("Not enough coordinates in inputstring");
        } else {
            double minX = Double.MAX_VALUE;
            double minY = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE;
            double maxY = Double.MIN_VALUE;
            for (int i = 0 ; i < coordinates.length; i++){
                if (i % 2 == 0) {
                    if (coordinates[i] < minX) minX = coordinates[i];
                    if (coordinates[i] > maxX) maxX = coordinates[i];
                } else {
                    if (coordinates[i] < minY) minY = coordinates[i];
                    if (coordinates[i] > maxY) maxY = coordinates[i];
                }
            }
            return new Envelope(minX, minY, maxX, maxY);
        }
    }

    /**
     * Retrieves a list of coordinates from the given string. If the number of numbers in the string is odd,
     * the last number is ignored.
     *
     * @param inputString The inputstring
     * @return A list of coordinates
     * @throws TypeConversionException If the conversion failed
     */
    public double[] getCoordinates(String inputString)
            throws TypeConversionException {
        String[] cstr = inputString.split(",");
        int coordLength;
        if (cstr.length % 2 == 0) {
            coordLength = cstr.length;
        } else {
            coordLength = cstr.length -1;
        }

        double[] coordinates = new double[coordLength];
        try {
            for (int index = 0; index < coordLength; index++) {
                coordinates[index] = Double.parseDouble(cstr[index]);
            }
            return coordinates;
        } catch (NumberFormatException e) {
            throw new TypeConversionException("String contains non-numeric data. Impossible to create envelope", e);
        }
    }
}

