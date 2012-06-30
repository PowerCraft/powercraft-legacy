/*****************************************************************************

 JEP 2.4.1, Extensions 1.1.1
      April 30 2007
      (c) Copyright 2007, Nathan Funk and Richard Morris
      See LICENSE-*.txt for license information.

 *****************************************************************************/
/* Generated By:JJTree: Do not edit this line. ASTVarNode.java */

package org.nfunk.jep;


/**
 * Variable Node
 */
public class ASTVarNode extends SimpleNode {

	//private String varName;
	private Variable var;

	public ASTVarNode(int id) {
		super(id);
		var = null;
	}

	public ASTVarNode(Parser p, int id) {
		super(p, id);
	}

	/**
	 * Accept the visitor.
	 */
	@Override
	public Object jjtAccept(ParserVisitor visitor, Object data) throws ParseException {
		return visitor.visit(this, data);
	}

	/**
	 * Sets the name of the variable.
	 */
	//public void setName(String varName_in)
	//{
	//	var = varName_in;
	//}
	public void setVar(Variable variable) {
		var = variable;
	}

	public Variable getVar() {
		return var;
	}

	/**
	 * Returns the name of the variable.
	 */
	public String getName() {
		return var.getName();
	}

	/**
	 * Creates a string containing the variable's name and value
	 */
	@Override
	public String toString() {
		String temp = "Variable: \"" + getName() + "\"";

		return temp;
	}
}
