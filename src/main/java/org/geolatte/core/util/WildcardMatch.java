package org.geolatte.core.util;

/**
 * Based on http://th-mack.de/international/download/index.html with minor adaptations. Retained most of the documentation as well.
 *
 * This class allows for simple wildcard pattern matching. Possible
 * patterns allow to match single characters ('?') or any count of
 * characters ('*').<p>
 * Wildcard characters can be escaped (default: by an '\').<p>
 * This class always matches for the whole word.<p>
 * Examples:
 * <pre>
 * WildcardMatch wm = new WildcardMatch();
 * System.out.println(wm.match("CfgOptions.class", "C*.class"));      // true
 * System.out.println(wm.match("CfgOptions.class", "?gOpti*c?as?"));  // false
 * System.out.println(wm.match("CfgOptions.class", "??gOpti*c?ass")); // true
 * System.out.println(wm.match("What's this?",     "What*\\?"));      // true
 * System.out.println(wm.match("What's this?",     "What*?"));        // true
 * System.out.println(wm.match("A \\ backslash", "*\\\\?back*"));     // true
 * </pre>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */

public class WildcardMatch {

    private char singleCharacterWildcard = '?';   // Default single character wildcard
    private char multipleCharacterWildcard = '*'; // Default multiple character wildcard
    private char escapeCharacter = '\\';          // Default escape character

    private boolean caseSensitive = true;
    private boolean isEscaped = false;
    private int escCnt = 0;

    /**
     * Constructs a WildCardMatch object with the specified single and multiple character wildcard and the given escape
     * character.
     * @param singleCharWildcard    The single character wildcard.
     * @param multipleCharsWildcard The multiple character wildcard.
     * @param escapeChar            The escape character.
     */
    public WildcardMatch(char singleCharWildcard, char multipleCharsWildcard, char escapeChar) {

        setWildcardChars(singleCharWildcard, multipleCharsWildcard);
        setEscapeChar(escapeChar);
    }

    /**
     * Constructs a WildCardMatch object with the specified single and multiple character wildcard and escape character
     * \ (backslash).
     * @param singleCharWildcard    The single character wildcard.
     * @param multipleCharsWildcard The multiple character wildcard.
     */
    public WildcardMatch(char singleCharWildcard, char multipleCharsWildcard) {

        setWildcardChars(singleCharWildcard, multipleCharsWildcard);
    }

    /**
     * Constructs a WildCardMatch object with single character wildcard ?, multiple character wildcard * and escape
     * character \ (backslash).
     */
    public WildcardMatch() {

    }

    /**
     * Sets new characters to be used as wildcard characters, overriding the
     * the default of '?' for any single character match and '*' for any
     * amount of characters, including 0 characters.
     *
     * @param singleChar    The char used to match exactly ONE character.
     * @param multipleChars The char used to match any amount of characters
     *                      including o characters.
     */
    public void setWildcardChars(char singleChar, char multipleChars) {
        this.singleCharacterWildcard = singleChar;
        this.multipleCharacterWildcard = multipleChars;
    }

    /**
     * Sets the new character to be used as an escape character, overriding the
     * the default of '\'.
     *
     * @param escapeChar The char used to match escape wildcard characters.
     */
    public void setEscapeChar(char escapeChar) {
        this.escapeCharacter = escapeChar;
    }

    /**
     * Returns the character used to specify exactly one character.
     *
     * @return Wildcard character matching any single character.
     */
    public char getSingleWildcardChar() {
        return singleCharacterWildcard;
    }

    /**
     * Returns the character used to specify any amount of characters.
     *
     * @return Wildcard character matching any count of characters.
     */
    public char getMultipleWildcardChar() {
        return multipleCharacterWildcard;
    }

    /**
     * Returns the character used to escape the wildcard functionality of a
     * wildcard character. If two escape characters are used in sequence, they
     * mean the escape character itself. It defaults to '\'.
     *
     * @return Escape character.
     */
    public char getEscapeChar() {
        return escapeCharacter;
    }

    /**
     * Makes pattern matching case insensitive.
     *
     * @param caseSensitive false for case insensitivity. Default is case
     *                      sensitive match.
     */
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    /**
     * Returns the current state of case sensitivity.
     *
     * @return true for case sensitive pattern matching, false otherwise.
     */
    public boolean getCaseSensitive() {
        return caseSensitive;
    }

    boolean preceededByMultipleChar = false;

    /**
     * Matches a string against a pattern with wildcards. Two wildcard types
     * are supported: single character match (defaults to '?') and ANY
     * character match ('*'), matching any count of characters including 0.
     * Wildcard characters may be escaped by an escape character, which
     * defaults to '\'.
     *
     * @param s       The string, in which the search should be performed.
     * @param pattern The search pattern string including wildcards.
     * @return true, if string 's' matches 'pattern'.
     */
    public boolean match(String s, String pattern) {

        preceededByMultipleChar = false;
        isEscaped = false;
        if (!caseSensitive) {
            pattern = pattern.toLowerCase();
            s = s.toLowerCase();
        }
        int
                offset = 0;

        while (true) {
            String ps = getNextSubString(pattern);
            int len = ps.length();
            pattern = pattern.substring(len + escCnt);

            if (len > 0 && isWildcard(ps.charAt(0)) && escCnt == 0) {
                offset = getWildcardOffset(ps.charAt(0));
                if (isSingleWildcardChar(ps.charAt(0))) {
                    s = s.substring(1);
// This is not yet enough: If a '*' precedes '?', 's' might be SHORTER
// than seen here, for this we need preceededByMultipleChar variable...
                }
                if (pattern.length() == 0) {
                    return s.length() <= offset || preceededByMultipleChar;
                }
            } else {
                int idx = s.indexOf(ps);
                if (idx < 0 || (idx > offset && !preceededByMultipleChar)) {
                    return false;
                }
                s = s.substring(idx + len);
                preceededByMultipleChar = false;
            }
            if (pattern.length() == 0) {
                return (s.length() == 0);
            }
        }
    }

    private String getNextSubString(String pat) {
        escCnt = 0;
        if ("".equals(pat)) {
            return "";
        }
        if (isWildcard(pat.charAt(0))) {
            // if '?' is preceeded by '*', we need special considerations:
            if (pat.length() > 1 &&
                    !isSingleWildcardChar(pat.charAt(0)) &&
                    isSingleWildcardChar(pat.charAt(1))) {
                preceededByMultipleChar = true;
            }
            return pat.substring(0, 1);
        } else {
            String s = "";
            int i = 0;
            while (i < pat.length() &&
                    !isWildcard(pat.charAt(i), isEscaped)) {
                if (pat.charAt(i) == escapeCharacter) {
                    isEscaped = !isEscaped;
                    if (!isEscaped) {
                        s += pat.charAt(i);
                    }
                    else {
                        escCnt++; // bertvh: Changed this from the original implementation (this always increased escCnt). Placing it in an else branch allows escaping the escape symbol itself.
                    }
                } else if (isWildcard(pat.charAt(i))) {
                    isEscaped = false;
                    s += pat.charAt(i);
                } else {
                    s += pat.charAt(i);
                }
                i++;
            }
            return s;
        }
    }


    private boolean isWildcard(char c, boolean isEsc) {
        return !isEsc && isWildcard(c);
    }

    private boolean isSingleWildcardChar(char c) {
        return (c == singleCharacterWildcard);
    }

    private boolean isWildcard(char c) {
        return (c == multipleCharacterWildcard || c == singleCharacterWildcard);
    }

    private int getWildcardOffset(char c) {
        if (c == multipleCharacterWildcard) {
            return Integer.MAX_VALUE;
        }
        return 0;
    }
}