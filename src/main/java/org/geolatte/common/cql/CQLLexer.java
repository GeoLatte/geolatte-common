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

import org.geolatte.common.cql.lexer.Lexer;
import org.geolatte.common.cql.node.*;

import java.io.PushbackReader;

/**
 * <p>
 * This is the CQL Lexer. It takes care of correctly parsing WKT strings. The generated {@link Lexer} class does not
 * work when parsing CQL strings that include WKT.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 12-Aug-2010<br>
 * <i>Creation-Time</i>:  11:20:34<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class CQLLexer extends Lexer {

    private Token wktToken = null;
    private int openBraces = 0;
    private String accumulatedWktText = "";

    public CQLLexer(@SuppressWarnings("hiding") PushbackReader in) {
        super(in);
    }

    @Override
    protected void filter() {

        if (state == State.WKT) {

            if (wktToken == null) { // just entering the wkt state ..

                if (token instanceof TWktEmptySetLiteral) { // nothing special needed for the empty geometry.. return to
                                                        // normal and continue
                    state = State.NORMAL;
                    return;
                }

                wktToken = token; // save the current token (we will add text to this)
                openBraces = 0; // reset open braces count (when it is back to 0, we are ready)
                accumulatedWktText = token.getText();
                token = null; // continue parsing the next token
                return;
            }
            else { // already in wkt state ..

                boolean tokenIsLeftParen = token instanceof TLeftParen;
                boolean tokenIsRightParen = token instanceof TRightParen;

                // Count the open braces
                if (tokenIsLeftParen)
                    openBraces++;
                else if (tokenIsRightParen)
                    openBraces--;

                accumulatedWktText += token.getText();

                // We are done if open braces count is back to 0 (can only happen if the current token is a right brace)
                // or is we have an empty token
                if (((tokenIsRightParen) && openBraces == 0) || token instanceof TWktEmptySetLiteral) {

                    state = State.NORMAL; // Set state back to normal
                    wktToken.setText(accumulatedWktText); // Set token text
                    token = wktToken; // 'return' the cached token
                }
                else // We are not done yet
                    token = null; // continue parsing the next token
            }
        }
    }
}
