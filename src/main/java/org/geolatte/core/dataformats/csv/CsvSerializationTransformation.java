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

package org.geolatte.core.dataformats.csv;

import com.vividsolutions.jts.geom.Geometry;
import org.geolatte.core.reflection.EntityClassReader;
import org.geolatte.core.reflection.InvalidObjectReaderException;
import org.geolatte.core.transformer.Transformation;
import org.geolatte.core.transformer.TransformationException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * This transformer takes a feature and serializes it into a CSV representation. The geometry is shown in WKT
 * format whereas the format for the date as well as the separator for the different columns can be set by the user.
 * This transformation is stateless with the exception of its configuration (the dateformatter and separator).
 * <i>Creation-Date</i>: 9-jul-2010<br>
 * <i>Creation-Time</i>: 11:35:11<br>
 * </p>
 *
 * @author Yves Vandewoude
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class CsvSerializationTransformation<T> implements Transformation<T, String> {

    private char separator;
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private Class<? extends T> entityClass;
    private EntityClassReader reader;
    private List<String> columnList;

    /**
     * Base constructor.
     *
     * @param objectClass the class of the objects to transform
     * @param csvChar     the character to use to separate different columns.
     */
    public CsvSerializationTransformation(Class<? extends T> objectClass, char csvChar) {
        separator = csvChar;
        entityClass = objectClass;
        reader = EntityClassReader.getClassReaderFor(objectClass);
        this.columnList = new ArrayList<String>();
    }

    /**
     * Base constructor.
     *
     * @param objectClass the class of the objects to transform
     * @param csvChar     the character to use to separate different columns.
     * @param columnList  if set, only the columns in the given list will be used and in that order. If not,
     *                    all columns will be serialized in a random order.
     */
    public CsvSerializationTransformation(Class<? extends T> objectClass, char csvChar, List<String> columnList) {
        separator = csvChar;
        entityClass = objectClass;
        reader = EntityClassReader.getClassReaderFor(objectClass);
        this.columnList = columnList == null ? new ArrayList<String>() : columnList;
    }

    /**
     * The formatter to use for the different
     *
     * @param formatterToUse the formatter to use for dates
     */
    public void setDateFormatter(DateFormat formatterToUse) {
        formatter = formatterToUse;
    }

    /**
     * @return the header (all propertynames seperated by the separator)
     */
    public String getHeader() {
        StringBuilder sb = new StringBuilder();
        if (columnList.size() > 0) {
            for (String s : columnList) {
                sb.append(s);
                sb.append(separator);
            }
        } else {
            if (reader.getIdName() != null) {
                sb.append(reader.getIdName());
                sb.append(separator);
            }
            for (String name : reader.getProperties()) {
                sb.append(name);
                sb.append(separator);
            }
            if (reader.getGeometryName() != null) {
                sb.append(reader.getGeometryName());
                sb.append(separator);
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public String transform(T input) throws TransformationException {
        if (input.getClass() != entityClass) {
            // Can't happen
            throw new TransformationException("Object not of correct type");
        }
        try {
            StringBuilder sb = new StringBuilder();
            if (columnList.size() > 0) {
                for (String column : columnList) {
                    if (column.equalsIgnoreCase(reader.getIdName())) {
                        Object id = reader.getId(input);
                        sb.append(id == null ? "" : id);
                        sb.append(separator);
                    } else if (column.equalsIgnoreCase(reader.getGeometryName())) {
                        Geometry geom = reader.getGeometry(input);
                        sb.append(geom == null ? "" : geom.toText());
                        sb.append(separator);
                    } else {
                        Object value = reader.getPropertyValue(input, column);
                        value = null == value ? "" : value;
                        if (value instanceof Date) {
                            sb.append(formatter.format((Date) value));
                        } else {
                            sb.append(value);
                        }
                        sb.append(separator);
                    }
                }
            } else {
                if (reader.getIdName() != null) {
                    Object id = reader.getId(input);
                    sb.append(id == null ? "" : id);
                    sb.append(separator);
                }
                for (String name : reader.getProperties()) {
                    Object value = reader.getPropertyValue(input, name);
                    value = null == value ? "" : value;
                    if (value instanceof Date) {
                        sb.append(formatter.format((Date) value));
                    } else {
                        sb.append(value);
                    }
                    sb.append(separator);
                }
                if (reader.getGeometryName() != null) {
                    Geometry geom = reader.getGeometry(input);
                    sb.append(geom == null ? "" : geom.toText());
                    sb.append(separator);
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (InvalidObjectReaderException e) {
            // Can't happen!
            throw new TransformationException("Object not of correct type.", e);
        }
    }
}
