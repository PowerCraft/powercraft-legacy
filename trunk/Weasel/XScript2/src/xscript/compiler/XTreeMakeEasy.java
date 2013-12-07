package xscript.compiler;

import xscript.compiler.XTree.XConstant;
import xscript.compiler.XTree.XOperatorPrefixSuffix;
import xscript.compiler.XTree.XOperatorStatement;
import xscript.compiler.XTree.XStatement;


public class XTreeMakeEasy extends XTreeChanger {

	@SuppressWarnings("unchecked")
	@Override
	protected <T extends XTree> T visitTree(T tree){
		super.visitTree(tree);
		if(tree instanceof XOperatorStatement){
			tree = (T) makeEasy((XOperatorStatement) tree);
		}else if(tree instanceof XOperatorPrefixSuffix){
			
		}
		return tree;
	}
	
	private XConstant connect(XConstant const1, XConstant const2, XOperator operator){
		if(operator==XOperator.ADD){
			return XConstantOperators.add(const1, const2);
		}
		return null;
	}
	
	private XStatement makeEasy(XOperatorStatement tree){
		if(tree.left instanceof XConstant && tree.right instanceof XConstant){
			XStatement ret = connect((XConstant)tree.left, (XConstant)tree.right, tree.operator);
			if(ret!=null)
				return ret;
		}
		return tree;
	}
	
}
