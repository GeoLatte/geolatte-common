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

package org.geolatte.testobjects;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 19-Aug-2010<br>
 * <i>Creation-Time</i>:  14:27:00<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class FilterableObject3 {
    private int id;

    private int anInteger;
    private String aString;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilterableObject3)) return false;

        FilterableObject3 that = (FilterableObject3) o;

        if (anInteger != that.anInteger) return false;
        if (id != that.id) return false;
        if (aString != null ? !aString.equals(that.aString) : that.aString != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + anInteger;
        result = 31 * result + (aString != null ? aString.hashCode() : 0);
        return result;
    }

    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }

    public int getAnInteger() {
        return anInteger;
    }

    public void setAnInteger(int anInteger) {
        this.anInteger = anInteger;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FilterableObject3{" +
                "id=" + id +
                ", anInteger=" + anInteger +
                ", aString='" + aString + '\'' +
                '}';
    }
}
