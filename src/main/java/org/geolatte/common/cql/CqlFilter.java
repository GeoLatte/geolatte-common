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

import org.geolatte.common.expressions.Filter;
import org.geolatte.common.cql.lexer.LexerException;
import org.geolatte.common.cql.node.Start;
import org.geolatte.common.cql.parser.Parser;
import org.geolatte.common.cql.parser.ParserException;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.HashMap;

/**
 * <p>
 * Constructs a filter based on the given CQL string. The actual filter construction happens at evaluation time of an
 * object ({@link #evaluate(Object)}) since only then, the type of the object to filter is known and a meaningful filter
 * can be constructed. This does imply a performance penalty. However, the filters are cached so the penalty is limited
 * to the first time a new type is filtered.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 21-Jul-2010<br>
 * <i>Creation-Time</i>:  10:14:38<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class CqlFilter {

    private String cqlString;
    private final HashMap<Class, Filter> filterExpressions = new HashMap<Class, Filter>();

    public CqlFilter(String cqlString) throws IllegalArgumentException {

        this.cqlString = cqlString;
    }

    public Boolean evaluate(Object object) throws ParseException {

        Class clazz = object.getClass();
        Filter currentFilter = filterExpressions.get(clazz);

        try {
            Parser p = new Parser( new CqlLexer( new PushbackReader(new StringReader(cqlString), 1024)));
            // Parse the input.
            Start tree = p.parse();

            // Build the filter expression
            FilterExpressionBuilder builder = new FilterExpressionBuilder(clazz);
            tree.apply(builder);

            currentFilter = new Filter(builder.getExp());

            filterExpressions.put(clazz, currentFilter);
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

        return currentFilter.evaluate(object);
    }
}
