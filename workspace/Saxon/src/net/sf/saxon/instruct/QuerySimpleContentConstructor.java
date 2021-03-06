package net.sf.saxon.instruct;

import net.sf.saxon.expr.Atomizer;
import net.sf.saxon.expr.Expression;
import net.sf.saxon.expr.StaticProperty;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.FastStringBuffer;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.BuiltInAtomicType;
import net.sf.saxon.value.AtomicValue;
import net.sf.saxon.value.StringValue;
import net.sf.saxon.event.SequenceReceiver;

/**
 * This class implements the rules for an XQuery simple content constructor, which are used in constructing
 * the string value of an attribute node, text node, comment node, etc, from the value of the select
 * expression or the contained sequence constructor. These differ slightly from the XSLT rules implemented
 * in the superclass - specifically, the sequence is simply atomized, whereas XSLT takes special steps to
 * concatenate adjacent text nodes before inserting separators.
 */

public class QuerySimpleContentConstructor extends SimpleContentConstructor {

    boolean noNodeIfEmpty;

    public QuerySimpleContentConstructor(Expression select, Expression separator, boolean noNodeIfEmpty) {
        super(select, separator);
        this.noNodeIfEmpty = noNodeIfEmpty;
    }

    public boolean isNoNodeWhenEmpty() {
        return noNodeIfEmpty;
    }

    /**
     * Compute the cardinality of the result of the expression.
     * @return the cardinality, @link {StaticProperty.EXACTLY_ONE}
     */

    protected int computeCardinality() {
        if (noNodeIfEmpty) {
            return StaticProperty.ALLOWS_ZERO_OR_ONE;
        } else {
            return StaticProperty.EXACTLY_ONE;
        }
    }

    /**
     * Copy an expression. This makes a deep copy.
     * @return the copy of the original expression
     */

    public Expression copy() {
        return new QuerySimpleContentConstructor(select.copy(), separator.copy(), noNodeIfEmpty);
    }

    /**
    * Expand the stylesheet elements subordinate to this one, returning the result
    * as a string. The expansion must not generate any element or attribute nodes.
    * @param context The dynamic context for the transformation
    */

    public CharSequence expandChildren(XPathContext context) throws XPathException {
        Item item = select.evaluateItem(context);
        if (item==null) {
            return (noNodeIfEmpty ? null : "");
        } else {
            return item.getStringValueCS();
        }
    }

    /**
     * Evaluate an expression as a single item. This always returns either a single Item or
     * null (denoting the empty sequence). No conversion is done. This method should not be
     * used unless the static type of the expression is a subtype of "item" or "item?": that is,
     * it should not be called if the expression may return a sequence. There is no guarantee that
     * this condition will be detected.
     *
     * @param context The context in which the expression is to be evaluated
     * @return the node or atomic value that results from evaluating the
     *         expression; or null to indicate that the result is an empty
     *         sequence
     * @throws net.sf.saxon.trans.XPathException
     *          if any dynamic error occurs evaluating the
     *          expression
     */

    public Item evaluateItem(XPathContext context) throws XPathException {

        if (isSingleton && isAtomic) {
            // optimize for this case
            Item item = select.evaluateItem(context);
            if (item == null) {
                if (noNodeIfEmpty) {
                    return null;
                } else {
                    return StringValue.EMPTY_STRING;
                }
            }
            if (item instanceof StringValue) {
                return item;
            } else {
                return ((AtomicValue)item).convert(BuiltInAtomicType.STRING, true, context).asAtomic();
            }
        }
        SequenceIterator iter = select.iterate(context);
        if (!isAtomic) {
            iter = Atomizer.getAtomizingIterator(iter);
        }
        FastStringBuffer sb = new FastStringBuffer(FastStringBuffer.SMALL);
        boolean first = true;
        String sep = " ";
        while (true) {
            Item item = iter.next();
            if (item==null) {
                break;
            }
            if (!first) {
                sb.append(sep);
            }
            first = false;
            sb.append(item.getStringValueCS());
        }
        if (first && noNodeIfEmpty) {
            return null;
        }
        return StringValue.makeStringValue(sb.condense());
    }

    /**
     * Process the instruction, without returning any tail calls
     * @param context The dynamic context, giving access to the current node,
     *                the current variables, etc.
     */

    public void process(XPathContext context, int locationId, int options) throws XPathException {
        SequenceReceiver out = context.getReceiver();
        if (isSingleton && isAtomic) {
            Item item = select.evaluateItem(context);
            if (item != null) {
                out.characters(item.getStringValueCS(), locationId, options);
            }
        } else {
            SequenceIterator iter = select.iterate(context);
            if (!isAtomic) {
                iter = Atomizer.getAtomizingIterator(iter);
            }
            boolean first = true;
            while (true) {
                Item item = iter.next();
                if (item==null) {
                    break;
                }
                if (!first) {
                    out.characters(" ", locationId, options);
                }
                first = false;
                out.characters(item.getStringValueCS(), locationId, options);
            }
        }
    }
}


//
// The contents of this file are subject to the Mozilla Public License Version 1.0 (the "License");
// you may not use this file except in compliance with the License. You may obtain a copy of the
// License at http://www.mozilla.org/MPL/
//
// Software distributed under the License is distributed on an "AS IS" basis,
// WITHOUT WARRANTY OF ANY KIND, either express or implied.
// See the License for the specific language governing rights and limitations under the License.
//
// The Original Code is: all this file.
//
// The Initial Developer of the Original Code is Michael H. Kay.
//
// Portions created by (your name) are Copyright (C) (your legal entity). All Rights Reserved.
//
// Contributor(s): none.
//
