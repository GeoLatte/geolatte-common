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

package org.geolatte.core.expressions;

/**
 * <p>
 * Abstract base class for Like and NotLike expressions.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 9-apr-2010<br>
 * <i>Creation-Time</i>:  11:48:54<br>
 * </p>
 *
 * @author Peter Rigole
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public abstract class StringLikeComparisonExpression extends BooleanBinaryStringExpression {

    private char wildcard;
    private boolean caseInsensitive = false;

    /**
     * Constructor of a binary expression with '%' as the wildcardcharacter
     *
     * @param left  The left expression.
     * @param right The right expression.
     */
    public StringLikeComparisonExpression(Expression<String> left, Expression<String> right) {
        this(left, right, '%');
    }

    /**
     * Constructor of a binary expression with '%' as the wildcardcharacter
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @param caseInsensitive Set to true if case should be ignored.
     */
    public StringLikeComparisonExpression(Expression<String> left, Expression<String> right, boolean caseInsensitive) {
        this(left, right, '%', caseInsensitive);
    }

    /**
     * Constructor of a binary expression.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @param wildCardCharacter the character to use as a wildcard
     */
    public StringLikeComparisonExpression(Expression<String> left, Expression<String> right, char wildCardCharacter) {
        this(left, right, wildCardCharacter, false);
    }

    /**
     * Constructor of a binary expression.
     *
     * @param left  The left expression.
     * @param right The right expression.
     * @param wildCardCharacter the character to use as a wildcard
     */
    public StringLikeComparisonExpression(Expression<String> left, Expression<String> right, char wildCardCharacter, boolean caseInsensitive) {
        super(left, right);
        wildcard = wildCardCharacter;
        this.caseInsensitive = caseInsensitive; 
    }

    //TODO -- is this still used??
    /**
     * Matches the given text with the given pattern.
     * Based on http://www.adarshr.com/papers/wildcard, added checks for wildcard at the start and at the end
     * @param text The text to match
     * @param pattern The pattern
     * @param multiCharacterWildCardChar The character to use as wildcard.
     * @return True if the text matches the pattern. False otherwise.
     */
    public static boolean wildCardMatch(String text, String pattern, char multiCharacterWildCardChar, boolean caseInsensitive)
    {
        if (caseInsensitive)
        {
            text = text.toLowerCase();
            pattern = pattern.toLowerCase();
        }

        String wildCardString = "" + multiCharacterWildCardChar;
        if (!Character.isLetter(multiCharacterWildCardChar))
        {
            wildCardString = "\\" + wildCardString;
        }
        // Create the cards by splitting using a RegEx. If more speed
        // is desired, a simpler character based splitting can be done.
        String [] cards = pattern.split(wildCardString);

        // Don't allow anything before the pattern begin unless the pattern begins with '%' - "abcd" should not match "cdef", but should match "%cdef"
        if ( cards.length != 0 && (pattern.charAt(0) != multiCharacterWildCardChar && text.indexOf(cards[0]) != 0))
            return false;

        // Iterate over the cards.
        for (String card : cards)
        {
            int idx = text.indexOf(card);

            // Card not detected in the text.
            if(idx == -1)
            {
                return false;
            }

            // Move ahead, towards the right of the text.
            text = text.substring(idx + card.length());
        }

        // Don't allow anything beyond the pattern end unless the pattern and with '%'
        return !(pattern.charAt(pattern.length() - 1) != multiCharacterWildCardChar && text.length() != 0);

    }

    /**
     * Gets the wildcard character.
     * @return The wildcard character.
     */
    protected char getWildcardChar() {
        return wildcard;
    }

    /**
     * Gets whether the string comparison should ignore case.
     * @return A value indicating whether the string comparisoin should ignore case.
     */
    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }
}