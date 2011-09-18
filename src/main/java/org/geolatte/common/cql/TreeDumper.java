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

import org.geolatte.common.cql.analysis.DepthFirstAdapter;
import org.geolatte.common.cql.node.Node;
import org.geolatte.common.cql.node.Start;
import org.geolatte.common.cql.node.Token;
import org.geolatte.common.cql.parser.Parser;

import java.io.*;

/**
 * <p>
 * Utility class that contains a main method that allows to test the CQL parser and print out the parsed result.
 * </p>
 * <p>
 * <i>Creation-Date</i>: 27-May-2010<br>
 * <i>Creation-Time</i>:  10:15:56<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */

public class TreeDumper extends DepthFirstAdapter {
    private int depth = 0;
    private PrintWriter out;

    public TreeDumper(PrintWriter out) {
        this.out = out;
    }

    public void defaultCase(Node node) {
        indent();
        out.println(((Token)node).getText());
    }

    public void defaultIn(Node node) {
        indent();
        printNodeName(node);
        out.println();

        depth = depth+1;
    }

    public void defaultOut(Node node) {
        depth = depth-1;
        out.flush();
    }

    private void printNodeName(Node node) {
        String fullName = node.getClass().getName();
        String name = fullName.substring(fullName.lastIndexOf('.')+1);

        out.print(name);
    }

    private void indent() {
        for (int i = 0; i < depth; i++) out.write("   ");
    }

    public static void main(String[] args) throws IOException {

        String cql = "";

        if (args.length == 0) {
            System.out.println("Usage: Type an expression as command-line argument or at the break, e.g., \"(A>5)\"");

            System.out.println("Type an expression: ");
            InputStreamReader converter = new InputStreamReader(System.in);
            BufferedReader in = new BufferedReader(converter);
            cql = in.readLine();
        }
        else
            cql = args[0];

        Parser parser = new Parser(new CQLLexer(new PushbackReader(new StringReader(cql), 1000)));

        try {
            Start start = parser.parse();
            System.out.println("Concrete Syntax Tree:");
            start.getPExpr().apply(new TreeDumper(new PrintWriter(System.out)));

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}

