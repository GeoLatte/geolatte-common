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

package org.geolatte.common.cql;

import org.geolatte.common.cql.analysis.DepthFirstAdapter;
import org.geolatte.common.cql.node.*;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.codec.Wkt;
import org.geolatte.geom.codec.WktParseException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 20-Jul-2010<br>
 * <i>Creation-Time</i>:  18:49:41<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class AbstractBuilder extends DepthFirstAdapter {

    protected HashMap<Node, Object> translatedLiterals = new HashMap<Node, Object>();


    //
    // Literals
    //

    private void putLiteral(Node node, Object value) {

        translatedLiterals.put(node, value);
        node = node.parent();

        if (node instanceof PLiteral)
            translatedLiterals.put(node, value);
    }

    protected Object getLiteral(Node node) {

        return translatedLiterals.get(node);
    }

    @Override
    public void outADefaultStringLiteral(ADefaultStringLiteral node) {

        putLiteral(node, node.getCharacterStringLiteral().getText().trim().substring(1, node.getCharacterStringLiteral().getText().length()-1));
    }

    @Override
    public void outADefaultNumericLiteral(ADefaultNumericLiteral node) {

        putLiteral(node, node.getSignedNumericLiteral().getText());
    }

    @Override
    public void outATrueBooleanLiteral(ATrueBooleanLiteral node) {

        putLiteral(node, "true");
    }

    @Override
    public void outAFalseBooleanLiteral(AFalseBooleanLiteral node) {

        putLiteral(node, "false");
    }

    @Override
    public void outAFromToTimespanLiteral(AFromToTimespanLiteral node) {

        putLiteral(node, new FromToTimeSpan(parseDate(node.getFrom().getText().trim()), parseDate(node.getTo().getText().trim())));
    }

    @Override
    public void outAUnknownBooleanLiteral(AUnknownBooleanLiteral node) {

        putLiteral(node, "null");
    }

    @Override
    public void outADefaultDurationLiteral(ADefaultDurationLiteral node) {

        int years = Integer.parseInt(node.getYears().getText());
        int months = Integer.parseInt(node.getMonths().getText());
        int days = Integer.parseInt(node.getDays().getText());
        int hours = Integer.parseInt(node.getHours().getText());
        int minutes = Integer.parseInt(node.getMinutes().getText());
        int seconds = Integer.parseInt(node.getSeconds().getText());

        putLiteral(node, new Duration(years, months, days, hours, minutes, seconds));
    }

    @Override
    public void outADefaultDatetimeLiteral(ADefaultDatetimeLiteral node) {

        putLiteral(node, node.getDatetime().getText());
    }

    @Override
    public void outAPointGeometryLiteral(APointGeometryLiteral node) {

        String wkt = node.getWktPointLiteral().getText();
        try {

            Geometry geo = Wkt.fromWkt(wkt);
            putLiteral(node, geo);
        }
        catch (WktParseException e) {
            throw new RuntimeException("Could not parse WKT: " + wkt, e);
        }
    }

    //
    // Utility methods
    //

    public static Date parseDate(String dateString) {

        Date date = null;

        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            date = formatter.parse(dateString);
        }
        catch (ParseException e) {
            ; // Cannot occur
        }

        return date;
    }

    protected class FromToTimeSpan {

        public Date from;
        public Date to;

        private FromToTimeSpan(Date from, Date to) {
            this.from = from;
            this.to = to;
        }
    }

    protected class Duration {

        private int years;
        private int months;
        private int days;
        private int hours;
        private int minutes;
        private int seconds;

        private Duration(int years, int months, int days, int hours, int minutes, int seconds) {
            this.years = years;
            this.months = months;
            this.days = days;
            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
        }

        public int getYears() {
            return years;
        }

        public int getMonths() {
            return months;
        }

        public int getDays() {
            return days;
        }

        public int getHours() {
            return hours;
        }

        public int getMinutes() {
            return minutes;
        }

        public int getSeconds() {
            return seconds;
        }
    }

    protected List<String> getPropertyParts(PAttr attr) {
        
        List<String> parts = new ArrayList<String>();

        if (attr instanceof AIdAttr) {

            parts.add(((AIdAttr)attr).getIdentifier().getText().trim());
            return parts;
        }
        else {
            ACompoundIdAttr compoundAttr = (ACompoundIdAttr)attr;
            parts.add(compoundAttr.getIdentifier().getText().trim());
            parts.addAll(getPropertyParts(compoundAttr.getAttr()));
            return parts;
        }
    }

    /**
     * Creates a dot-separated string of the given parts.
     * @param parts
     * @return
     */
    protected String getPropertyPath(Collection<String> parts) {

        StringBuilder propertyPath = new StringBuilder();
        Iterator<String> partsIterator = parts.iterator();
        propertyPath.append(partsIterator.next());
        while (partsIterator.hasNext())
            propertyPath.append(".").append(partsIterator.next());

        return propertyPath.toString();
    }

    protected String getPropertyPath(PAttr attr) {

        Iterator<String> propertyPartsIterator = getPropertyParts(attr).iterator();
        String propertyPath = propertyPartsIterator.next();

        while (propertyPartsIterator.hasNext())
        {
            String propertyPart = propertyPartsIterator.next();
            propertyPath += "." + propertyPart;
        }

        return propertyPath;
    }

    /**
     * Gets the last part of a (compound) property expression.
     * @param attr The compound or simple property expression.
     * @return The last component of a property expression.
     */
    protected String getLastPropertyPart(PAttr attr) {

        if (attr instanceof AIdAttr)
            return ((AIdAttr)attr).getIdentifier().getText().trim();

        ACompoundIdAttr compoundAttr = (ACompoundIdAttr)attr;
            return getLastPropertyPart(compoundAttr.getAttr());
    }
}
