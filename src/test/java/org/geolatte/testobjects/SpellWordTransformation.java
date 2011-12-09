package org.geolatte.testobjects;

import org.geolatte.common.transformer.OneToManyTransformation;
import org.geolatte.common.transformer.TransformationException;

import java.util.Iterator;

/**
 * <p>
 * Expanding transformation that takes in a string and returns its characters one by one.
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class SpellWordTransformation implements OneToManyTransformation<String, Character> {

    public Iterator<Character> transform(final String input) throws TransformationException {

        return new Iterator<Character>() {

            private int currentPos = 0;
            private int length = input.length();

            public boolean hasNext() {

                return currentPos < length;
            }

            public Character next() {

                return input.charAt(currentPos++);
            }

            public void remove() {

                throw new UnsupportedOperationException();
            }
        };
    }
}
