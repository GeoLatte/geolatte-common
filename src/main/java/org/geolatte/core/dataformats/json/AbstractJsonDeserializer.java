/*
 * This file is part of the GeoLatte project. This code is licenced under
 * the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 *
 * Copyright (C) 2010 - 2010 and Ownership of code is shared by:
 * Qmino bvba - Romeinsestraat 18 - 3001 Heverlee  (http://www.Qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.core.dataformats.json;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * The abstract JsonDeserializer is a convenience class allowing the implementor of an actual
 * JSON deserializer to more quickly implement the deserializer. The deserializer will first deserialize
 * the object to a Map<String,Object>, and then provides a number of convenient methods to get the values
 * of the parameters you need.
 *
 * @author Yves Vandewoude
 *         class org.geolatte.core.dataformats.json.AbstractJsonDeserializer
 *         company <a href="http://www.Qmino.com">Qmino</a>
 *         Creation-Date: 20-okt-2009
 *         Creation-Time: 10:16:22
 */
public abstract class AbstractJsonDeserializer<T> extends JsonDeserializer<T> {

    protected JsonMapper parent;

    /**
     * The abstract json deserializer first parses the json to a HashMap. To ensure threadsafety
     * the 'current' object being parsed is kept as a threadlocal field.
     */
    private static final ThreadLocal inputParams = new ThreadLocal() {
        @Override
        protected synchronized Object initialValue() {
            return null;
        }
    };

    public AbstractJsonDeserializer(JsonMapper owner) {
        parent = owner;
    }


    @Override
    public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        //    Ensure that no problems arrise when recursive calls occur.
        Map<String, Object> originalParams = (Map<String, Object>) inputParams.get();
        try {
            Map<String, Object> newValue = (Map<String, Object>) jp.readValueAs(Object.class);
            inputParams.set(newValue);
        } catch (ClassCastException exc) {
            throw new IOException("Expected a JSON Object, but this was not the case. ", exc);
        }
        T answer = deserialize(jp);
        inputParams.set(originalParams);
        return answer;
    }

    /**
     * The actual method to override when implementing the deserializer
     *
     * @param jsonParser the actual parser. Provided for flexibility, but unlikely to be needed as all properties are
     *                   already available.
     * @return A deserialized instance of the given json string
     * @throws IOException if the deserialization failed.
     */
    protected abstract T deserialize(JsonParser jsonParser) throws IOException;

    /**
     * Convenience method for subclasses.
     *
     * @param paramName    the name of the parameter
     * @param errorMessage the errormessage to add to the exception if the param does not exist.
     * @param mapToUse     the map to use as input parametersource
     * @return a stringparameter with given name. If it does not exist and the errormessage is provided,
     *         an IOException is thrown with that message. if the errormessage is not provided, null is returned.
     * @throws IOException Exception if the paramname does not exist and an errormessage is provided.
     */
    protected String getStringParam(String paramName, String errorMessage, Map<String, Object> mapToUse)
            throws IOException {
        return getTypedParam(paramName, errorMessage, String.class, mapToUse);
    }

    /**
     * Convenience method for subclasses. Uses the default map for parameterinput
     *
     * @param paramName    the name of the parameter
     * @param errorMessage the errormessage to add to the exception if the param does not exist.
     * @return a stringparameter with given name. If it does not exist and the errormessage is provided,
     *         an IOException is thrown with that message. if the errormessage is not provided, null is returned.
     * @throws IOException Exception if the paramname does not exist and an errormessage is provided.
     */
    protected String getStringParam(String paramName, String errorMessage)
            throws IOException {
        return getStringParam(paramName, errorMessage, (Map<String, Object>) inputParams.get());
    }

    /**
     * Convenience method for subclasses.
     *
     * @param paramName    the name of the parameter
     * @param errorMessage the errormessage to add to the exception if the param does not exist.
     * @param mapToUse     the map to use as input parametersource
     * @return a stringparameter with given name. If it does not exist and the errormessage is provided,
     *         an IOException is thrown with that message. if the errormessage is not provided, null is returned.
     * @throws IOException Exception if the paramname does not exist and an errormessage is provided.
     */
    public String getSubJson(String paramName, String errorMessage, Map<String, Object> mapToUse) throws IOException {
        Object o = mapToUse.get(paramName);
        if (o != null) {
            try {
                return parent.toJson(o);
            } catch (JsonException e) {
                throw new IOException(errorMessage, e);
            }
        } else {
            if (errorMessage != null) {
                throw new IOException(errorMessage);
            } else {
                return null;
            }
        }
    }

    /**
     * Convenience method for subclasses. Uses the default parametermap for this deserializer
     *
     * @param paramName    the name of the parameter
     * @param errorMessage the errormessage to add to the exception if the param does not exist.
     * @return a stringparameter with given name. If it does not exist and the errormessage is provided,
     *         an IOException is thrown with that message. if the errormessage is not provided, null is returned.
     * @throws IOException Exception if the paramname does not exist and an errormessage is provided.
     */
    public String getSubJson(String paramName, String errorMessage) throws IOException {
        return getSubJson(paramName, errorMessage, (Map<String, Object>) inputParams.get());
    }

    /**
     * Convenience method for subclasses. Retrieves a double with the given parametername, even if that
     * parametercontent is actually a string containing a number or a long or an int or... Anything that
     * can be converted to a double is returned. uses the default parameterhashmap from this deserializer.
     *
     * @param paramName    the name of the parameter
     * @param errorMessage the errormessage to add to the exception if the param does not exist.
     * @return a stringparameter with given name. If it does not exist and the errormessage is provided,
     *         an IOException is thrown with that message. if the errormessage is not provided, null is returned.
     * @throws IOException Exception if the paramname does not exist and an errormessage is provided.
     */
    protected Double getDoubleParam(String paramName, String errorMessage) throws IOException {
        return getDoubleParam(paramName, errorMessage, (Map<String, Object>) inputParams.get());
    }

    /**
     * Convenience method for subclasses. Retrieves a double with the given parametername, even if that
     * parametercontent is actually a string containing a number or a long or an int or... Anything that
     * can be converted to a double is returned.
     *
     * @param paramName    the name of the parameter
     * @param errorMessage the errormessage to add to the exception if the param does not exist.
     * @param mapToUse     an external hashmap to use as inputsource. Should not be null!
     * @return a stringparameter with given name. If it does not exist and the errormessage is provided,
     *         an IOException is thrown with that message. if the errormessage is not provided, null is returned.
     * @throws IOException Exception if the paramname does not exist and an errormessage is provided.
     */
    protected Double getDoubleParam(String paramName, String errorMessage, Map<String, Object> mapToUse)
            throws IOException {
        Object o = mapToUse.get(paramName);
        if (o != null) {
            try {
                return Double.parseDouble(o.toString());
            } catch (NumberFormatException ignored) {
            }
        }
        return getTypedParam(paramName, errorMessage, Double.class, mapToUse);
    }

    /**
     * Convenience method for subclasses.
     *
     * @param paramName    the name of the parameter
     * @param errorMessage the errormessage to add to the exception if the param does not exist.
     * @param mapToUse     the map to use as inputsource for parameters. Should not be null!
     * @return a stringparameter with given name. If it does not exist and the errormessage is provided,
     *         an IOException is thrown with that message. if the errormessage is not provided, null is returned.
     * @throws IOException Exception if the paramname does not exist and an errormessage is provided.
     */
    protected Integer getIntParam(String paramName, String errorMessage, Map<String, Object> mapToUse) throws IOException {
        Object o = mapToUse.get(paramName);
        if (o != null) {
            try {
                return Integer.parseInt(o.toString());
            } catch (NumberFormatException ignored) {
            }
        }
        return getTypedParam(paramName, errorMessage, Integer.class, mapToUse);
    }

    /**
     * Convenience method for subclasses.
     *
     * @param paramName    the name of the parameter
     * @param errorMessage the errormessage to add to the exception if the param does not exist.
     * @return a stringparameter with given name. If it does not exist and the errormessage is provided,
     *         an IOException is thrown with that message. if the errormessage is not provided, null is returned.
     * @throws IOException Exception if the paramname does not exist and an errormessage is provided.
     */
    protected Integer getIntParam(String paramName, String errorMessage) throws IOException {
        return getIntParam(paramName, errorMessage, (Map<String, Object>) inputParams.get());
    }

    /**
     * Convenience method for subclasses.
     *
     * @param paramName    the name of the parameter
     * @param errorMessage the errormessage to add to the exception if the param does not exist.
     * @param mapToUse     The map to use as inputsource for parameters. Should not be null.
     * @return a stringparameter with given name. If it does not exist and the errormessage is provided,
     *         an IOException is thrown with that message. if the errormessage is not provided, null is returned.
     * @throws IOException Exception if the paramname does not exist and an errormessage is provided.
     */
    protected Boolean getBooleanParam(String paramName, String errorMessage, Map<String, Object> mapToUse) throws IOException {
        Boolean result = getTypedParam(paramName, errorMessage, Boolean.class, mapToUse);
        if (result == null) {
            String s = getTypedParam(paramName, errorMessage, String.class, mapToUse);
            if (s != null) {
                return Boolean.parseBoolean(s);
            }
        }
        return result;
    }

    /**
     * Convenience method for subclasses.
     *
     * @param paramName    the name of the parameter
     * @param errorMessage the errormessage to add to the exception if the param does not exist.
     * @return a stringparameter with given name. If it does not exist and the errormessage is provided,
     *         an IOException is thrown with that message. if the errormessage is not provided, null is returned.
     * @throws IOException Exception if the paramname does not exist and an errormessage is provided.
     */
    protected Boolean getBooleanParam(String paramName, String errorMessage) throws IOException {
        return getBooleanParam(paramName, errorMessage, (Map<String, Object>) inputParams.get());
    }

    /**
     * Convenience method for subclasses.
     *
     * @param paramName    the name of the parameter
     * @param errorMessage the errormessage to add to the exception if the param does not exist.
     * @param clazz        the class of the parameter that should be parsed.
     * @param mapToUse     the map to use as inputparameter source
     * @return a stringparameter with given name. If it does not exist and the errormessage is provided,
     *         an IOException is thrown with that message. if the errormessage is not provided, null is returned.
     * @throws IOException Exception if the paramname does not exist and an errormessage is provided.
     */
    protected <A> A getTypedParam(String paramName, String errorMessage, Class<A> clazz, Map<String, Object> mapToUse)
            throws IOException {
        Object o = mapToUse.get(paramName);
        if (o != null && clazz.isAssignableFrom(o.getClass())) {
            return (A) o;
        } else {
            if (errorMessage != null) {
                throw new IOException(errorMessage);
            } else {
                return null;
            }
        }
    }

    /**
     * Convenience method for subclasses. Uses the default parametermap for this deserializer.
     *
     * @param paramName    the name of the parameter
     * @param errorMessage the errormessage to add to the exception if the param does not exist.
     * @param clazz        the class of the parameter that should be parsed.
     * @return a stringparameter with given name. If it does not exist and the errormessage is provided,
     *         an IOException is thrown with that message. if the errormessage is not provided, null is returned.
     * @throws IOException Exception if the paramname does not exist and an errormessage is provided.
     */
    protected <A> A getTypedParam(String paramName, String errorMessage, Class<A> clazz) throws IOException {
        return getTypedParam(paramName, errorMessage, clazz, (Map<String, Object>) inputParams.get());
    }


    /**
     * Convenience method. Parses a string into a double. If it can no be converted to a double, the
     * defaultvalue is returned. Depending on your choice, you can allow null as output or assign it some value
     * and have very convenient syntax such as: double d = parseDefault(myString, 0.0); which is a lot shorter
     * than dealing with all kinds of numberformatexceptions.
     *
     * @param input        The inputstring
     * @param defaultValue The value to assign in case of error
     * @return A double corresponding with the input, or defaultValue if no double can be extracted
     */
    public Double parseDefault(String input, Double defaultValue) {
        if (input == null) {
            return defaultValue;
        }
        Double answer = defaultValue;
        try {
            answer = Double.parseDouble(input);
        } catch (NumberFormatException ignored) {
        }
        return answer;
    }

    /**
     * Convenience method. Parses a string into a double. If it can no be converted to a double, the
     * defaultvalue is returned. Depending on your choice, you can allow null as output or assign it some value
     * and have very convenient syntax such as: double d = parseDefault(myString, 0.0); which is a lot shorter
     * than dealing with all kinds of numberformatexceptions.
     *
     * @param input        The inputstring
     * @param defaultValue The value to assign in case of error
     * @return A double corresponding with the input, or defaultValue if no double can be extracted
     */
    public Integer parseDefault(String input, Integer defaultValue) {
        if (input == null) {
            return defaultValue;
        }
        Integer answer = defaultValue;
        try {
            answer = Integer.parseInt(input);
        } catch (NumberFormatException ignored) {
        }
        return answer;
    }

    /**
     * Parses a date from either dd/MM/yyyy or yyyy-MM-dd format
     *
     * @param paramName    the name of the parameter containing the date
     * @param errorMessage the message to put in an error if one occurs
     * @param mapToUse     the external map that should be used as inputsource for parameters
     * @return a date object correcponding with the jsonobject
     * @throws IOException If something went wrong
     */
    protected Date getDateParam(String paramName, String errorMessage, Map<String, Object> mapToUse) throws IOException {
        String dateInString = getStringParam(paramName, errorMessage, mapToUse);
        if (dateInString == null) {
            if (errorMessage == null) {
                return null;
            } else {
                throw new IOException(errorMessage);
            }
        }
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            return formatter.parse(dateInString);
        } catch (ParseException ignored) {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                return formatter.parse(dateInString);
            } catch (ParseException e) {
                if (errorMessage == null) {
                    return null;
                } else {
                    throw new IOException(errorMessage, e);
                }
            }
        }
    }

    /**
     * Parses a date from either dd/MM/yyyy or yyyy-MM-dd format. Uses the default parameter inputmap.
     *
     * @param paramName    the name of the parameter containing the date
     * @param errorMessage the message to put in an error if one occurs
     * @return a date object correcponding with the jsonobject
     * @throws IOException If something went wrong
     */
    protected Date getDateParam(String paramName, String errorMessage) throws IOException {
        return getDateParam(paramName, errorMessage, (Map<String, Object>) inputParams.get());
    }
}
