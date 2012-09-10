package org.geolatte.common.dataformats.json.to;

/**
 * Helper class for To tests.
 *
 * @author Bert Vanhooff [<a href="http://www.qmino.com">Qmino bvba</a>]
 */
public class ToTestHelper {

    public static NamedCrsPropertyTo createNamedCrsPropertyTo(String name) {

        NamedCrsPropertyTo namedCrsPropertyTo = new NamedCrsPropertyTo();
        namedCrsPropertyTo.setName(name);

        return namedCrsPropertyTo;
    }

    public static CrsTo createCrsTo(String name) {

        CrsTo crsTo = new CrsTo();
        NamedCrsPropertyTo namedCrsPropertyTo = createNamedCrsPropertyTo(name);
        crsTo.setProperties(namedCrsPropertyTo);

        return crsTo;
    }
}
