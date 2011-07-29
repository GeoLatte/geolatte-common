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

package org.geolatte.core.transformer;

/**
 * Exception thrown when an element could not be transformed by a Transformation.
 * 
 * <p>
 * <i>Creation-Date</i>: 23-Apr-2010<br>
 * <i>Creation-Time</i>:  15:10:27<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class TransformationException extends Exception {

    private Object failedObject;

    /**
     * Constructs a new exception with the object that caused failure of the transformation.
     * This object can be retrieved through {@link #getFailedObject()}.
     *
     * Further documentation, see {@link Exception}
     *
     * @param failedObject The transformation input object that caused the transformation to fail. This object must be of the Source type of the Transformation<Source, Target>. Unfortunately, we cannot enforce this through generics as Exceptions cannot be generic.
     */
    public TransformationException(Object failedObject) {

        this.failedObject = failedObject;
    }

    /**
     * Constructs a new exception with the object that caused failure of the transformation.
     * This object can be retrieved through {@link #getFailedObject()}.
     *
     * Futher documentation, see {@link Exception}
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     * @param failedObject The transformation input object that caused the transformation to fail.  This object must be of the Source type of the Transformation<Source, Target>. Unfortunately, we cannot enforce this through generics as Exceptions cannot be generic.
     */
    public TransformationException(String message, Object failedObject) {
        super(message);
        this.failedObject = failedObject;
    }

    /**
     * Constructs a new exception with the object that caused failure of the transformation.
     * This object can be retrieved through {@link #getFailedObject()}.
     *
     * Futher documentation, see {@link Exception}
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @param failedObject The transformation input object that caused the transformation to fail.  This object must be of the Source type of the Transformation<Source, Target>. Unfortunately, we cannot enforce this through generics as Exceptions cannot be generic.
     */
    public TransformationException(String message, Throwable cause, Object failedObject) {
        super(message, cause);
        this.failedObject = failedObject;
    }

    /**
     * Constructs a new exception with the object that caused failure of the transformation.
     * This object can be retrieved through {@link #getFailedObject()}.
     *
     * Futher documentation, see {@link Exception}
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @param failedObject The transformation input object that caused the transformation to fail.  This object must be of the Source type of the Transformation<Source, Target>. Unfortunately, we cannot enforce this through generics as Exceptions cannot be generic.
     */
    public TransformationException(Throwable cause, Object failedObject) {
        super(cause);
        this.failedObject = failedObject;
    }

    /**
     * The transformation input object that caused this exception. This the type of this object is the Source type of the transformation that threw this exception.
     * @return The input object that failed to be transformed.
     */
    public Object getFailedObject() {

        return failedObject;
    }
}
