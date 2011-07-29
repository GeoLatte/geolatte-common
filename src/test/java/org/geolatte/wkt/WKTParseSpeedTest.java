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

package org.geolatte.wkt;

import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>
 * An executable speed test for a number of selected WKT strings. Measures the time to parse each string with both the GeoLatte and the JTS parser.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 19-Jul-2010<br>
 * <i>Creation-Time</i>:  22:42:24<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class WKTParseSpeedTest {

    private int PARSE_REPETITIONS = 10000;
    private static ArrayList<String> wellKnownTextStrings =
            new ArrayList<String>(Arrays.asList(
                    "POINT (21 42)",
                    "POINT EMPTY",
                    "LINESTRING (5 5,10 10,20 20)",
                    "LINESTRING (5 5,10 10,20 20,10 10,20 20,10 10,20 20,10 10,20 20,10 10,20 20,10 10,20 20,10 10,20 20)"));

    public static void main(String[] args) {

        WKTParseSpeedTest speedTest = new WKTParseSpeedTest();

        System.out.println("Parsing following well known text strings " + speedTest.PARSE_REPETITIONS + " times each.");

        for (String wellKnownText : wellKnownTextStrings) {

            double timeJTS = 1;
            double timeGL = 1;

            try {
                timeJTS = speedTest.parseJTS(wellKnownText);
            }
            catch (ParseException e) {

                System.err.println("JTS parser failed");
            }

            timeGL = speedTest.parseGeoLatte(wellKnownText);

            System.out.println("WKT = " + wellKnownText);
            System.out.println("  JTS (" + roundTimeSpan(timeJTS) + "ms) | GeoLatte (" + roundTimeSpan(timeGL) + "ms) | JTS/GeoLatte = " + roundTimeSpan(timeJTS/timeGL));
        }
    }

    /**
     * Parses the given text with the JTS parser and measures the time.
     * @param wellKnownText
     * @return The time taken to parse the given string for PARSE_REPETITIONS times.
     * @throws ParseException
     */
    private double parseJTS(String wellKnownText) throws ParseException {

        // Test with JTS
        WKTReader wktReader = new WKTReader();

        final long startTime = System.nanoTime();
        final long endTime;

        for ( int i=0; i<PARSE_REPETITIONS; i++)
            wktReader.read(wellKnownText);

        endTime = System.nanoTime();
        final long duration = (endTime - startTime);

        return duration / 1000000; // nano -> muliseconds
    }

    /**
     * Parses the given text with the GeoLatte parser ({@link WKT.toGeometry()}) and measures the time.
     * @param wellKnownText
     * @return The time taken to parse the given string for PARSE_REPETITIONS times.
     * @throws ParseException
     */
    private double parseGeoLatte(String wellKnownText) {

        final long startTime = System.nanoTime();
        final long endTime;

        for ( int i=0; i<PARSE_REPETITIONS; i++)
            WKT.toGeometry(wellKnownText);

        endTime = System.nanoTime();
        final long duration = (endTime - startTime);

        return duration / 1000000; // nano -> muliseconds
    }

    private static double roundTimeSpan(double time) {

        return Math.round((time * 100)) / 100.0;
    }
}
