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
 * <i>Creation-Date</i>: 13-Aug-2010<br>
 * <i>Creation-Time</i>:  13:46:01<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class FilterableObject2 {

    private int id;

    private int anInteger;
    private String aString;

    private FilterableObject3 aSecondChildObject;

    public FilterableObject2() {
        
        aSecondChildObject = new FilterableObject3();
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

    public FilterableObject3 getaSecondChildObject() {
        return aSecondChildObject;
    }

    public void setaSecondChildObject(FilterableObject3 aChildObject) {
        this.aSecondChildObject = aChildObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilterableObject2)) return false;

        FilterableObject2 that = (FilterableObject2) o;

        if (anInteger != that.anInteger) return false;
        if (id != that.id) return false;
        if (aSecondChildObject != null ? !aSecondChildObject.equals(that.aSecondChildObject) : that.aSecondChildObject != null)
            return false;
        if (aString != null ? !aString.equals(that.aString) : that.aString != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + anInteger;
        result = 31 * result + (aString != null ? aString.hashCode() : 0);
        result = 31 * result + (aSecondChildObject != null ? aSecondChildObject.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FilterableObject2{" +
                "id=" + id +
                ", anInteger=" + anInteger +
                ", aString='" + aString + '\'' +
                ", aSecondChildObject=" + aSecondChildObject +
                '}';
    }
}
