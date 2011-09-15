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

package org.geolatte.common.cql;

import org.geolatte.common.expressions.Filter;
import org.geolatte.common.cql.lexer.Lexer;
import org.geolatte.common.cql.lexer.LexerException;
import org.geolatte.common.cql.node.Start;
import org.geolatte.common.cql.parser.Parser;
import org.geolatte.common.cql.parser.ParserException;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.text.ParseException;

/**
 * <p>
 * Utility class that translates CQL expressions such as "(AnAttribute > 5) and (AnotherOne LIKE 'this')" into an executable filter or Hibernate criteria.
 * CQL - Common Catalogue Query Language - is described in the OGC Catalogue Services Specification. Our implementation is based on that specification but might differ slightly in some areas.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 26-May-2010<br>
 * <i>Creation-Time</i>:  11:38:44<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class CQL {

     /**
      * Creates an executable object filter based on the given CQL expression.
      *
      * The filter constructed is applicable to objects of all 
      * @param cqlExpression The CQL expression.
      * @return An object filter that behaves according to the given CQL expression.
      */
    public static CqlFilter toFilter(String cqlExpression){

        return new CqlFilter(cqlExpression);
    }

    /**
     * Creates an executable object filter based on the given CQL expression.
     * This filter is only applicable for the given class.
     * @param cqlExpression The CQL expression.
     * @param clazz The type for which to construct the filter.
     * @return An object filter that behaves according to the given CQL expression.
     * @throws java.text.ParseException When parsing fails for any reason (parser, lexer, IO)
     */
    public static Filter toStaticFilter(String cqlExpression, Class clazz) throws ParseException {

        try {
            Parser p = new Parser( new Lexer( new PushbackReader(new StringReader(cqlExpression), 1024)));
            // Parse the input.
            Start tree = p.parse();

            // Build the filter expression
            FilterExpressionBuilder builder = new FilterExpressionBuilder();
            tree.apply(builder);

            // Wrap in a filter
            return new Filter(builder.getExp());
        }
        catch(ParserException e) {

            ParseException parseException = new ParseException(e.getMessage(), e.getToken().getPos());
            parseException.initCause(e);
            throw parseException;
        }
        catch (LexerException e) {

            ParseException parseException = new ParseException(e.getMessage(), 0);
            parseException.initCause(e);
            throw parseException;
        }
        catch (IOException e) {

            ParseException parseException = new ParseException(e.getMessage(), 0);
            parseException.initCause(e);
            throw parseException;
        }
    }



}
