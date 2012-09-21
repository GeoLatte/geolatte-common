package org.geolatte.common.dataformats.json.to;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link NamedCrsPropertyTo}.
 *
 * @author Bert Vanhooff [<a href="http://www.qmino.com">Qmino bvba</a>]
 */
public class NamedCrsPropertyToTest {

    @Test
    public void testEquals() throws Exception {

        // Checks a number of standard properties of the equals contract
        EqualsVerifier.forClass(NamedCrsPropertyTo.class).withRedefinedSuperclass().suppress(Warning.NONFINAL_FIELDS).verify();

        NamedCrsPropertyTo first = null;
        NamedCrsPropertyTo second = null;

        // Equal
        first = new NamedCrsPropertyTo("name");
        second = new NamedCrsPropertyTo("name");
        Assert.assertTrue(first.equals(second));
        Assert.assertTrue(second.equals(first));

        // Unequal
        first = new NamedCrsPropertyTo("name");
        second = new NamedCrsPropertyTo("eman");
        EqualsVerifier.forExamples(first, second);
        Assert.assertFalse(first.equals(second));
        Assert.assertFalse(second.equals(first));
    }
}
