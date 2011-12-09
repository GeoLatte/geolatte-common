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

package org.geolatte.testobjects;

import org.geolatte.geom.Geometry;
import org.geolatte.geom.codec.Wkt;
import org.geolatte.geom.codec.WktParseException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * This class does nothing besides exposing a number of properties that can be used to test filters.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 25-May-2010<br>
 * <i>Creation-Time</i>:  14:33:20<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class FilterableObject {

    private int id;

    public enum Properties {
        anInteger,
        anotherInteger,
        aLong,
        anotherLong,
        aShort,
        anotherShort,
        aDouble,
        anotherDouble,
        aFloat,
        anotherFloat,
        aString,
        anotherString,
        aBoolean,
        anotherBoolean,
        aDate,
        anotherDate,
        aBigDecimal,
        aBigInteger,
        aGeometry,
        aChildObject_anInteger
                { @Override
                  public String toString() {return "aChildObject.anInteger";}},
        aChildObject_aString
                { @Override
                  public String toString() {return "aChildObject.aString";}},
        aChildObject_aChildObject_anInteger
                { @Override
                  public String toString() {return "aChildObject.aSecondChildObject.anInteger";}},
        aChildObject_aChildObject_aString
                { @Override
                  public String toString() {return "aChildObject.aSecondChildObject.aString";}}
    }

    private int anInteger;
    private int anotherInteger;
    private long aLong;
    private long anotherLong;
    private short aShort;
    private short anotherShort;
    private double aDouble;
    private double anotherDouble;
    private float aFloat;
    private float anotherFloat;
    private String aString;
    private String anotherString;
    private Boolean aBoolean;
    private Boolean anotherBoolean;
    private Date aDate;
    private Date anotherDate;

    private BigDecimal aBigDecimal;
    private BigInteger aBigInteger;

    private Geometry aGeometry;

    private FilterableObject2 aChildObject;

    public FilterableObject() {

        aChildObject = new FilterableObject2();
    }

    /**
     * Sets the given property to the given value.
     * @param propertyName The name of the property to set. Only the values defined in {@link Properties} will work here.
     * @param propertyValue The value to set. This can be a string to parse or a native value.
     */
    public void setProperty(String propertyName, Object propertyValue) {

        if (propertyName.equals("anInteger"))
            setAnInteger(propertyValue instanceof String ? Integer.parseInt(propertyValue.toString()) : (Integer)propertyValue);
        else if (propertyName.equals("anotherInteger"))
            setAnotherInteger(propertyValue instanceof String ? Integer.parseInt(propertyValue.toString()) : (Integer) propertyValue);
        else if (propertyName.equals("aLong"))
            setaLong(propertyValue instanceof String ? Long.parseLong(propertyValue.toString()) : (Long) propertyValue);
        else if (propertyName.equals("anotherLong"))
            setAnotherLong(propertyValue instanceof String ? Integer.parseInt(propertyValue.toString()) : (Long) propertyValue);
        else if (propertyName.equals("aShort"))
            setaShort(propertyValue instanceof String ? Short.parseShort(propertyValue.toString()) : (Short) propertyValue);
        else if (propertyName.equals("anotherShort"))
            setAnotherShort(propertyValue instanceof String ? Short.parseShort(propertyValue.toString()) : (Short)propertyValue);
        else if (propertyName.equals("aDouble"))
            setaDouble(propertyValue instanceof String ? Double.parseDouble(propertyValue.toString()) : (Double)propertyValue);
        else if (propertyName.equals("anotherDouble"))
            setAnotherDouble(propertyValue instanceof String ? Double.parseDouble(propertyValue.toString()) : (Double)propertyValue);
        else if (propertyName.equals("aFloat"))
            setaFloat(propertyValue instanceof String ? Float.parseFloat(propertyValue.toString()) : (Float)propertyValue);
        else if (propertyName.equals("anotherFloat"))
            setAnotherFloat(propertyValue instanceof String ? Float.parseFloat(propertyValue.toString()) : (Float)propertyValue);
        else if (propertyName.equals("aString"))
            setaString(propertyValue.toString());
        else if (propertyName.equals("anotherString"))
            setAnotherString(propertyValue.toString());
        else if (propertyName.equals("aBoolean"))
            setaBoolean(propertyValue instanceof String ? Boolean.parseBoolean(propertyValue.toString()) : (Boolean)propertyValue);
        else if (propertyName.equals("anotherBoolean"))
            setAnotherBoolean(propertyValue instanceof String ? Boolean.parseBoolean(propertyValue.toString()) : (Boolean)propertyValue);
        else if (propertyName.equals("aBigDecimal"))
            setaBigDecimal(propertyValue instanceof String ? new BigDecimal(propertyValue.toString()) : (BigDecimal)propertyValue);
        else if (propertyName.equals("aBigInteger"))
            setaBigInteger(propertyValue instanceof String ? new BigInteger(propertyValue.toString()) : (BigInteger)propertyValue);
        else if (propertyName.equals("aGeometry")) {
            if (propertyValue instanceof String) {

                Geometry geometry = null;
                try {
                    geometry = Wkt.fromWkt(propertyValue.toString());
                }
                catch (WktParseException e) {
                    System.err.println(e.getMessage());
                }
                setaGeometry(geometry);
            } else {

                setaGeometry((Geometry) propertyValue);
            }
        }
        else if (propertyName.equals("aDate")) {

            if (propertyValue instanceof Date) {
                setaDate((Date)propertyValue);
            }
            else {
                try {
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    setaDate((Date)formatter.parse(propertyValue.toString()));
                }
                catch (ParseException e)
                {
                    ; // Should never happen
                }
            }
        }
        else if (propertyName.equals(Properties.aChildObject_anInteger.toString()))
            getaChildObject().setAnInteger(propertyValue instanceof String ? Integer.parseInt(propertyValue.toString()) : (Integer) propertyValue);
        else if (propertyName.equals(Properties.aChildObject_aString.toString()))
            getaChildObject().setaString(propertyValue.toString());
        else if (propertyName.equals(Properties.aChildObject_aChildObject_anInteger.toString()))
            getaChildObject().getaSecondChildObject().setAnInteger(propertyValue instanceof String ? Integer.parseInt(propertyValue.toString()) : (Integer) propertyValue);
        else if (propertyName.equals(Properties.aChildObject_aChildObject_aString.toString()))
            getaChildObject().getaSecondChildObject().setaString(propertyValue.toString());
        else
            System.err.println("Could not set property " + propertyName + ". Property may not exist or code to set a property might not be complete");
    }

    public short getaShort() {
        return aShort;
    }

    public void setaShort(short aShort) {
        this.aShort = aShort;
    }

    public short getAnotherShort() {
        return anotherShort;
    }

    public void setAnotherShort(short anotherShort) {
        this.anotherShort = anotherShort;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public int getAnInteger() {
        return anInteger;
    }

    public void setAnInteger(int anInteger) {
        this.anInteger = anInteger;
    }

    public int getAnotherInteger() {
        return anotherInteger;
    }

    public void setAnotherInteger(int anotherInteger) {
        this.anotherInteger = anotherInteger;
    }

    public long getaLong() {
        return aLong;
    }

    public void setaLong(long aLong) {
        this.aLong = aLong;
    }

    public long getAnotherLong() {
        return anotherLong;
    }

    public void setAnotherLong(long anotherLong) {
        this.anotherLong = anotherLong;
    }

    public double getaDouble() {
        return aDouble;
    }

    public void setaDouble(double aDouble) {
        this.aDouble = aDouble;
    }

    public double getAnotherDouble() {
        return anotherDouble;
    }

    public void setAnotherDouble(double anotherDouble) {
        this.anotherDouble = anotherDouble;
    }

    public float getaFloat() {
        return aFloat;
    }

    public void setaFloat(float aFloat) {
        this.aFloat = aFloat;
    }

    public float getAnotherFloat() {
        return anotherFloat;
    }

    public void setAnotherFloat(float anotherFloat) {
        this.anotherFloat = anotherFloat;
    }

    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }

    public String getAnotherString() {
        return anotherString;
    }

    public void setAnotherString(String anotherString) {
        this.anotherString = anotherString;
    }

    public Boolean getaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(Boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public Boolean getAnotherBoolean() {
        return anotherBoolean;
    }

    public void setAnotherBoolean(Boolean anotherBoolean) {
        this.anotherBoolean = anotherBoolean;
    }

    public Date getaDate() {
        return aDate;
    }

    public void setaDate(Date aDate) {
        this.aDate = aDate;
    }

    public Date getAnotherDate() {
        return anotherDate;
    }

    public void setAnotherDate(Date anotherDate) {
        this.anotherDate = anotherDate;
    }

    public BigDecimal getaBigDecimal() {
        return aBigDecimal;
    }

    public void setaBigDecimal(BigDecimal aBigDecimal) {
        this.aBigDecimal = aBigDecimal;
    }

    public BigInteger getaBigInteger() {
        return aBigInteger;
    }

    public void setaBigInteger(BigInteger aBigInteger) {
        this.aBigInteger = aBigInteger;
    }

    public Geometry getaGeometry() {
        return aGeometry;
    }

    public void setaGeometry(Geometry aGeometry) {
        this.aGeometry = aGeometry;
    }

    public FilterableObject2 getaChildObject() {
        return aChildObject;
    }

    public void setaChildObject(FilterableObject2 aChildObject) {
        this.aChildObject = aChildObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilterableObject)) return false;

        FilterableObject that = (FilterableObject) o;

        if (Double.compare(that.aDouble, aDouble) != 0) return false;
        if (Float.compare(that.aFloat, aFloat) != 0) return false;
        if (aLong != that.aLong) return false;
        if (aShort != that.aShort) return false;
        if (anInteger != that.anInteger) return false;
        if (Double.compare(that.anotherDouble, anotherDouble) != 0) return false;
        if (Float.compare(that.anotherFloat, anotherFloat) != 0) return false;
        if (anotherInteger != that.anotherInteger) return false;
        if (anotherLong != that.anotherLong) return false;
        if (anotherShort != that.anotherShort) return false;
        if (id != that.id) return false;
        if (aBigDecimal != null ? !aBigDecimal.equals(that.aBigDecimal) : that.aBigDecimal != null) return false;
        if (aBigInteger != null ? !aBigInteger.equals(that.aBigInteger) : that.aBigInteger != null) return false;
        if (aBoolean != null ? !aBoolean.equals(that.aBoolean) : that.aBoolean != null) return false;
        if (aChildObject != null ? !aChildObject.equals(that.aChildObject) : that.aChildObject != null) return false;
        if (aDate != null ? !aDate.equals(that.aDate) : that.aDate != null) return false;
        if (aGeometry != null ? !aGeometry.equals(that.aGeometry) : that.aGeometry != null) return false;
        if (aString != null ? !aString.equals(that.aString) : that.aString != null) return false;
        if (anotherBoolean != null ? !anotherBoolean.equals(that.anotherBoolean) : that.anotherBoolean != null)
            return false;
        if (anotherDate != null ? !anotherDate.equals(that.anotherDate) : that.anotherDate != null) return false;
        if (anotherString != null ? !anotherString.equals(that.anotherString) : that.anotherString != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + anInteger;
        result = 31 * result + anotherInteger;
        result = 31 * result + (int) (aLong ^ (aLong >>> 32));
        result = 31 * result + (int) (anotherLong ^ (anotherLong >>> 32));
        result = 31 * result + (int) aShort;
        result = 31 * result + (int) anotherShort;
        temp = aDouble != +0.0d ? Double.doubleToLongBits(aDouble) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = anotherDouble != +0.0d ? Double.doubleToLongBits(anotherDouble) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (aFloat != +0.0f ? Float.floatToIntBits(aFloat) : 0);
        result = 31 * result + (anotherFloat != +0.0f ? Float.floatToIntBits(anotherFloat) : 0);
        result = 31 * result + (aString != null ? aString.hashCode() : 0);
        result = 31 * result + (anotherString != null ? anotherString.hashCode() : 0);
        result = 31 * result + (aBoolean != null ? aBoolean.hashCode() : 0);
        result = 31 * result + (anotherBoolean != null ? anotherBoolean.hashCode() : 0);
        result = 31 * result + (aDate != null ? aDate.hashCode() : 0);
        result = 31 * result + (anotherDate != null ? anotherDate.hashCode() : 0);
        result = 31 * result + (aBigDecimal != null ? aBigDecimal.hashCode() : 0);
        result = 31 * result + (aBigInteger != null ? aBigInteger.hashCode() : 0);
        result = 31 * result + (aGeometry != null ? aGeometry.hashCode() : 0);
        result = 31 * result + (aChildObject != null ? aChildObject.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FilterableObject{" +
                "id=" + id +
                ", anInteger=" + anInteger +
                ", anotherInteger=" + anotherInteger +
                ", aLong=" + aLong +
                ", anotherLong=" + anotherLong +
                ", aShort=" + aShort +
                ", anotherShort=" + anotherShort +
                ", aDouble=" + aDouble +
                ", anotherDouble=" + anotherDouble +
                ", aFloat=" + aFloat +
                ", anotherFloat=" + anotherFloat +
                ", aString='" + aString + '\'' +
                ", anotherString='" + anotherString + '\'' +
                ", aBoolean=" + aBoolean +
                ", anotherBoolean=" + anotherBoolean +
                ", aDate=" + aDate +
                ", anotherDate=" + anotherDate +
                ", aBigDecimal=" + aBigDecimal +
                ", aBigInteger=" + aBigInteger +
                ", aGeometry=" + aGeometry +
                ", aChildObject=" + aChildObject +
                '}';
    }
}
