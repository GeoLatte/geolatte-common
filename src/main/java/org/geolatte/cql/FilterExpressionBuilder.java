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

package org.geolatte.cql;

import com.vividsolutions.jts.geom.Geometry;
import org.geolatte.core.expressions.ComparableExpression;
import org.geolatte.core.expressions.EmptyBasicTypeSwitch;
import org.geolatte.core.expressions.Expression;
import org.geolatte.core.expressions.Expressions;
import org.geolatte.core.reflection.EntityClassReader;
import org.geolatte.cql.node.*;

import java.util.Date;
import java.util.HashMap;

/**
 * <p>
 * Treewalker that builds an executable {@link org.geolatte.core.expressions.Expression} based on a given CQL AST. Use as follows:
 * <pre>
 * {@code
 * FilterExpressionBuilder builder = new FilterExpressionBuilder(clazz); // builder for the given class
 * tree.apply(builder); // with tree, the root element of the AST as returned by the parser.
 * Expression result = builder.getExp();
 * }
 * </pre>
 * </p>
 * <p>
 * <i>Creation-Date</i>: 26-May-2010<br>
 * <i>Creation-Time</i>:  11:52:36<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
class FilterExpressionBuilder extends AbstractBuilder {

    private Expression<Boolean> exp = null;
    private EntityClassReader reader;

    // A map of all translated nodes as they are visited depth first.
    private HashMap<Node, Expression> translatedExpressions = new HashMap<Node, Expression>();

    public FilterExpressionBuilder(Class clazz) {

        reader = EntityClassReader.getClassReaderFor(clazz);
    }

    public FilterExpressionBuilder() {

        
    }

    public Expression<Boolean> getExp() {
        return exp;
    }

    @Override
    public void outStart(Start node) {

        node.getPExpr().apply(this);

        exp = (Expression<Boolean>)translatedExpressions.get(node.getPExpr());
    }

    @Override
    public void outAAndExpr(AAndExpr node) {

        translatedExpressions.put(node, Expressions.and(translatedExpressions.get(node.getLeft()), translatedExpressions.get(node.getRight())));
    }

    @Override
    public void outAOrExpr(AOrExpr node)
    {
        translatedExpressions.put(node, Expressions.or(translatedExpressions.get(node.getLeft()), translatedExpressions.get(node.getRight())));
    }

    @Override
    public void outANotExpr(ANotExpr node)
    {
       translatedExpressions.put(node, Expressions.not(translatedExpressions.get(node.getExpr())));
    }

    @Override
    public void outAGtExpr(final AGtExpr node) {

        translatedExpressions.get(node.getLeft()).switchOn(new EmptyBasicTypeSwitch() {
            @Override
            public void caseNumber(Expression<Number> expr) {
                translatedExpressions.put(node, Expressions.isGreaterThan((ComparableExpression<Number>)translatedExpressions.get(node.getLeft()), Expressions.constant(Double.parseDouble(getLiteral(node.getRight()).toString()))));
            }
            @Override
            public void caseBoolean(Expression<Boolean> expr) {
                translatedExpressions.put(node, Expressions.isGreaterThan((ComparableExpression<Boolean>)translatedExpressions.get(node.getLeft()), Expressions.constant((Boolean)Boolean.parseBoolean(getLiteral(node.getRight()).toString()))));
            }
            @Override
            public void caseString(Expression<String> expr) {
                translatedExpressions.put(node, Expressions.isGreaterThan((ComparableExpression<String>)translatedExpressions.get(node.getLeft()), Expressions.constant(getLiteral(node.getRight()).toString())));
            }
        });
    }

    @Override
    public void outALtExpr(final ALtExpr node)
    {
        translatedExpressions.get(node.getLeft()).switchOn(new EmptyBasicTypeSwitch() {
            @Override
            public void caseNumber(Expression<Number> expr) {
                translatedExpressions.put(node, Expressions.isLessThan((ComparableExpression<Number>)translatedExpressions.get(node.getLeft()), Expressions.constant(Double.parseDouble(getLiteral(node.getRight()).toString()))));
            }
            @Override
            public void caseBoolean(Expression<Boolean> expr) {
                translatedExpressions.put(node, Expressions.isLessThan((ComparableExpression<Boolean>)translatedExpressions.get(node.getLeft()), Expressions.constant((Boolean)Boolean.parseBoolean(getLiteral(node.getRight()).toString()))));
            }
            @Override
            public void caseString(Expression<String> expr) {
                translatedExpressions.put(node, Expressions.isLessThan((ComparableExpression<String>)translatedExpressions.get(node.getLeft()), Expressions.constant(getLiteral(node.getRight()).toString())));
            }
        });
    }

    @Override
    public void outAGteExpr(final AGteExpr node)
    {
        translatedExpressions.get(node.getLeft()).switchOn(new EmptyBasicTypeSwitch() {
            @Override
            public void caseNumber(Expression<Number> expr) {
                translatedExpressions.put(node, Expressions.isGreaterThanOrEqual((ComparableExpression<Number>)translatedExpressions.get(node.getLeft()), Expressions.constant(Double.parseDouble(getLiteral(node.getRight()).toString()))));
            }
            @Override
            public void caseBoolean(Expression<Boolean> expr) {
                translatedExpressions.put(node, Expressions.isGreaterThanOrEqual((ComparableExpression<Boolean>)translatedExpressions.get(node.getLeft()), Expressions.constant((Boolean)Boolean.parseBoolean(getLiteral(node.getRight()).toString()))));
            }
            @Override
            public void caseString(Expression<String> expr) {
                translatedExpressions.put(node, Expressions.isGreaterThanOrEqual((ComparableExpression<String>)translatedExpressions.get(node.getLeft()), Expressions.constant(getLiteral(node.getRight()).toString())));
            }
        });
    }

    @Override
    public void outALteExpr(final ALteExpr node)
    {
        translatedExpressions.get(node.getLeft()).switchOn(new EmptyBasicTypeSwitch() {
            @Override
            public void caseNumber(Expression<Number> expr) {
                translatedExpressions.put(node, Expressions.isLessThanOrEqual((ComparableExpression<Number>)translatedExpressions.get(node.getLeft()), Expressions.constant(Double.parseDouble(getLiteral(node.getRight()).toString()))));
            }
            @Override
            public void caseBoolean(Expression<Boolean> expr) {
                translatedExpressions.put(node, Expressions.isLessThanOrEqual((ComparableExpression<Boolean>)translatedExpressions.get(node.getLeft()), Expressions.constant((Boolean)Boolean.parseBoolean(getLiteral(node.getRight()).toString()))));
            }
            @Override
            public void caseString(Expression<String> expr) {
                translatedExpressions.put(node, Expressions.isLessThanOrEqual((ComparableExpression<String>)translatedExpressions.get(node.getLeft()), Expressions.constant(getLiteral(node.getRight()).toString())));
            }
        });
    }

    @Override
    public void outAEqExpr(final AEqExpr node)
    {
        translatedExpressions.get(node.getLeft()).switchOn(new EmptyBasicTypeSwitch() {
            @Override
            public void caseNumber(Expression<Number> expr) {
                translatedExpressions.put(node, Expressions.isEqual((ComparableExpression<Number>)translatedExpressions.get(node.getLeft()), Expressions.constant(Double.parseDouble(getLiteral(node.getRight()).toString()))));
            }
            @Override
            public void caseBoolean(Expression<Boolean> expr) {
                translatedExpressions.put(node, Expressions.isEqual((ComparableExpression<Boolean>)translatedExpressions.get(node.getLeft()), Expressions.constant((Boolean)Boolean.parseBoolean(getLiteral(node.getRight()).toString()))));
            }
            @Override
            public void caseString(Expression<String> expr) {
                translatedExpressions.put(node, Expressions.isEqual((ComparableExpression<String>)translatedExpressions.get(node.getLeft()), Expressions.constant(getLiteral(node.getRight()).toString())));
            }
        });
    }

    @Override
    public void outANeqExpr(final ANeqExpr node)
    {
        translatedExpressions.get(node.getLeft()).switchOn(new EmptyBasicTypeSwitch() {
            @Override
            public void caseNumber(Expression<Number> expr) {
                translatedExpressions.put(node, Expressions.isNotEqual((ComparableExpression<Number>)translatedExpressions.get(node.getLeft()), Expressions.constant(Double.parseDouble(getLiteral(node.getRight()).toString()))));
            }
            @Override
            public void caseBoolean(Expression<Boolean> expr) {
                translatedExpressions.put(node, Expressions.isNotEqual((ComparableExpression<Boolean>)translatedExpressions.get(node.getLeft()), Expressions.constant((Boolean)Boolean.parseBoolean(getLiteral(node.getRight()).toString()))));
            }
            @Override
            public void caseString(Expression<String> expr) {
                translatedExpressions.put(node, Expressions.isNotEqual((ComparableExpression<String>)translatedExpressions.get(node.getLeft()), Expressions.constant(getLiteral(node.getRight()).toString())));
            }
        });
    }

    @Override
    public void outALikeExpr(ALikeExpr node) {

        translatedExpressions.put(node, Expressions.like(translatedExpressions.get(node.getLeft()), Expressions.constant(escapeStringLiteral(getLiteral(node.getRight()).toString()))));
    }

    @Override
    public void outANotLikeExpr (ANotLikeExpr node) {

        translatedExpressions.put(node, Expressions.notLike(translatedExpressions.get(node.getLeft()), Expressions.constant(escapeStringLiteral(getLiteral(node.getRight()).toString()))));
    }

    @Override
    public void outAIlikeExpr(AIlikeExpr node) {

        translatedExpressions.put(node, Expressions.like(translatedExpressions.get(node.getLeft()), Expressions.constant(escapeStringLiteral(getLiteral(node.getRight()).toString())), true));
    }

    @Override
    public void outANotIlikeExpr (ANotIlikeExpr node) {

        translatedExpressions.put(node, Expressions.notLike(translatedExpressions.get(node.getLeft()), Expressions.constant(escapeStringLiteral(getLiteral(node.getRight()).toString())), true));
    }

    @Override
    public void outABeforeExpr(ABeforeExpr node) {

        translatedExpressions.put(node, Expressions.isBefore(translatedExpressions.get(node.getAttr()), Expressions.constant(parseDate(getLiteral(node.getDateTime()).toString()))));
    }

    @Override
    public void outAAfterExpr(AAfterExpr node) {

        translatedExpressions.put(node, Expressions.isAfter(translatedExpressions.get(node.getAttr()), Expressions.constant(parseDate(getLiteral(node.getDateTime()).toString()))));
    }

    @Override
    public void outADuringExpr(ADuringExpr node) {

        PTimespanLiteral timespan = node.getTimeSpan();

        Date lowDate;
        Date highDate;

        if (timespan instanceof AFromToTimespanLiteral ) {

            AFromToTimespanLiteral fromToTimespan = (AFromToTimespanLiteral)timespan;
            lowDate = parseDate(fromToTimespan.getFrom().getText().trim());
            highDate = parseDate(fromToTimespan.getTo().getText().trim());

            translatedExpressions.put(node, Expressions.isBetween((ComparableExpression<Date>)translatedExpressions.get(node.getAttr()), (ComparableExpression<Date>)Expressions.constant(lowDate), (ComparableExpression<Date>)Expressions.constant(highDate)));
        }
    }


    @Override
    public void outAExistsExpr(AExistsExpr node) {

        PAttr attribute = node.getAttr();
        translatedExpressions.put(node, Expressions.exists(getPropertyPath(attribute)));
    }

    @Override
    public void outADoesNotExistExpr(ADoesNotExistExpr node) {

        PAttr attribute = node.getAttr();
        translatedExpressions.put(node, Expressions.doesNotExist(getPropertyPath(attribute)));
    }

    @Override
    public void outAGeoEqualsExpr(AGeoEqualsExpr node) {

        translatedExpressions.put(node, Expressions.geoEquals(translatedExpressions.get(node.getLeft()), Expressions.constant((Geometry) getLiteral(node.getRight()))));
    }
    
    @Override
    public void outACompoundIdAttr(ACompoundIdAttr node) {

        outPAttr(node);
    }

    @Override
    public void caseAIdAttr(AIdAttr node) {

        outPAttr(node);
    }

    private void outPAttr(PAttr node) {

        // if this is part of a compound attribute.. skip.. the top compound attribute is the final attribute. Without this test, we treat every part as a separate attribute while the parts on themselves have no meaning.
        if (!((node.parent() != null) && !(node.parent() instanceof ACompoundIdAttr)))
            return;

        String attributeName = getPropertyPath(node);
        Class attributeType = reader.getPropertyType(attributeName);

        if (Number.class.isAssignableFrom(attributeType) ||
            int.class.isAssignableFrom(attributeType) ||
            long.class.isAssignableFrom(attributeType) ||
            short.class.isAssignableFrom(attributeType) ||
            float.class.isAssignableFrom(attributeType) ||
            double.class.isAssignableFrom(attributeType) ||
            byte.class.isAssignableFrom(attributeType))
            translatedExpressions.put(node, Expressions.numberProperty(attributeName));
        else if (String.class.isAssignableFrom(attributeType))
            translatedExpressions.put(node, Expressions.stringProperty(attributeName));
        else if (Boolean.class.isAssignableFrom(attributeType))
            translatedExpressions.put(node, Expressions.booleanProperty(attributeName));
        else if (Date.class.isAssignableFrom(attributeType))
            translatedExpressions.put(node, Expressions.dateProperty(attributeName));
        else if (Geometry.class.isAssignableFrom(attributeType))
            translatedExpressions.put(node, Expressions.geometryProperty(attributeName));
    }

    /**
     * Escapes the given string or remove cql escape sequences if necessary.
     * @param string The sting to escape.
     * @return The escaped string.
     */
    private String escapeStringLiteral(String string) {

        return string.replace("''", "'"); // '' -> '
    }
}
