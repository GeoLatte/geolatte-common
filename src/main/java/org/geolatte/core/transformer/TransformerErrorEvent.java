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
 * <p>
 * An event that indicates that a transformation error has occurred inside a Transformer.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 29-Apr-2010<br>
 * <i>Creation-Time</i>:  09:18:17<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5 
 */
public class TransformerErrorEvent extends ErrorEvent {

    private Object failedObject;
    private TransformerErrorEvent nestedEvent;


    /**
     * Constructs a TransformerErrorEvent
     *
     * @param source The object on which the Event initially occurred.
     * @param failedObject The input object that caused a transformation error. Can be null.
     * @throws IllegalArgumentException if source is null.
     */
    public TransformerErrorEvent(Object source, Object failedObject) {
        super(source);

        this.failedObject = failedObject;
    }

    /**
     * Constructs a TransformerErrorEvent
     *
     * @param source The object on which the Event initially occurred.
     * @param failedObject The input object that caused a transformation error. Can be null.
     * @param exception The exception that was thrown during transformer execution. Can be null
     * @throws IllegalArgumentException if source is null.
     */
    public TransformerErrorEvent(Object source, Object failedObject, Exception exception) {
        super(source, exception);

        this.failedObject = failedObject;
    }

    /**
     * Constructs an event with the exception that caused the error.
     *
     * @param source    The object on which the Event initially occurred.
     * @param exception The exception that caused the error.
     * @param failedObject The input object that caused a transformation error. Can be null.
     * @throws IllegalArgumentException if source is null.
     */
    public TransformerErrorEvent(Object source, Object failedObject, Exception exception, TransformerErrorEvent nestedEvent) {
        super(source, exception);

        this.failedObject = failedObject;
        this.nestedEvent = nestedEvent;
    }

    /**
     * Gets the input object that caused the transformation error if available.
     * @return The object that caused the transformation error.
     */
    public Object getFailedObject() {
        return failedObject;
    }

    /**
     * Gets the cause of this event.
     * @return The event that caused this event to be fired.
     */
    public TransformerErrorEvent getNestedEvent() {
        return nestedEvent;
    }
}
