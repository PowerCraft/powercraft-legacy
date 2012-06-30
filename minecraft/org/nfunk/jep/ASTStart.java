/*****************************************************************************

 JEP 2.4.1, Extensions 1.1.1
      April 30 2007
      (c) Copyright 2007, Nathan Funk and Richard Morris
      See LICENSE-*.txt for license information.

 *****************************************************************************/
/* Generated By:JJTree: Do not edit this line. ASTStart.java */
package org.nfunk.jep;


/**
 * Start Node
 */
public class ASTStart extends SimpleNode {
	public ASTStart(int id) {
		super(id);
	}

	public ASTStart(Parser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	@Override
	public Object jjtAccept(ParserVisitor visitor, Object data) throws ParseException {
		return visitor.visit(this, data);
	}
}
