package xscript.compiler;

import java.util.List;

import xscript.compiler.XTree.XAnnotation;
import xscript.compiler.XTree.XBlock;
import xscript.compiler.XTree.XBreak;
import xscript.compiler.XTree.XClassDecl;
import xscript.compiler.XTree.XClassFile;
import xscript.compiler.XTree.XConstant;
import xscript.compiler.XTree.XContinue;
import xscript.compiler.XTree.XDo;
import xscript.compiler.XTree.XError;
import xscript.compiler.XTree.XFor;
import xscript.compiler.XTree.XGroup;
import xscript.compiler.XTree.XIdent;
import xscript.compiler.XTree.XIf;
import xscript.compiler.XTree.XIfOperator;
import xscript.compiler.XTree.XImport;
import xscript.compiler.XTree.XIndex;
import xscript.compiler.XTree.XMethodCall;
import xscript.compiler.XTree.XMethodDecl;
import xscript.compiler.XTree.XModifier;
import xscript.compiler.XTree.XNew;
import xscript.compiler.XTree.XOperatorPrefixSuffix;
import xscript.compiler.XTree.XOperatorStatement;
import xscript.compiler.XTree.XReturn;
import xscript.compiler.XTree.XSynchroized;
import xscript.compiler.XTree.XThrow;
import xscript.compiler.XTree.XType;
import xscript.compiler.XTree.XTypeParam;
import xscript.compiler.XTree.XVarDecl;
import xscript.compiler.XTree.XVarDecls;
import xscript.compiler.XTree.XWhile;

public class XTreeChanger implements XVisitor {

	protected <T extends XTree> T visitTree(T tree){
		if(tree!=null){
			tree.accept(this);
		}
		return tree;
	}
	
	protected <T extends XTree> List<T> visitTree(List<T> tree){
		if(tree!=null){
			for(int i=0; i<tree.size(); i++){
				tree.set(i, (T)visitTree(tree.get(i)));
			}
		}
		return tree;
	}
	
	@Override
	public void visitTopLevel(XClassFile xClassFile) {
		xClassFile.packAnnotations = visitTree(xClassFile.packAnnotations);
		xClassFile.packID = visitTree(xClassFile.packID);
		xClassFile.defs = visitTree(xClassFile.defs);
	}

	@Override
	public void visitImport(XImport xImport) {}

	@Override
	public void visitClassDecl(XClassDecl xClassDef) {
		xClassDef.modifier = visitTree(xClassDef.modifier);
		xClassDef.typeParam = visitTree(xClassDef.typeParam);
		xClassDef.superClasses = visitTree(xClassDef.superClasses);
		xClassDef.defs = visitTree(xClassDef.defs);
	}

	@Override
	public void visitAnnotation(XAnnotation xAnnotation) {}

	@Override
	public void visitModifier(XModifier xModifier) {
		xModifier.annotations = visitTree(xModifier.annotations);
	}

	@Override
	public void visitError(XError xError) {}

	@Override
	public void visitIdent(XIdent xIdent) {}

	@Override
	public void visitType(XType xType) {
		xType.name = visitTree(xType.name);
		xType.typeParam = visitTree(xType.typeParam);
	}

	@Override
	public void visitTypeParam(XTypeParam xTypeParam) {
		xTypeParam.name = visitTree(xTypeParam.name);
		xTypeParam.extend = visitTree(xTypeParam.extend);
	}

	@Override
	public void visitVarDecl(XVarDecl xVarDecl) {
		xVarDecl.modifier = visitTree(xVarDecl.modifier);
		xVarDecl.type = visitTree(xVarDecl.type);
		xVarDecl.init = visitTree(xVarDecl.init);
	}

	@Override
	public void visitMethodDecl(XMethodDecl xMethodDecl) {
		xMethodDecl.modifier = visitTree(xMethodDecl.modifier);
		xMethodDecl.typeParam = visitTree(xMethodDecl.typeParam);
		xMethodDecl.returnType = visitTree(xMethodDecl.returnType);
		xMethodDecl.paramTypes = visitTree(xMethodDecl.paramTypes);
		xMethodDecl.throwList = visitTree(xMethodDecl.throwList);
		xMethodDecl.block = visitTree(xMethodDecl.block);
	}

	@Override
	public void visitBlock(XBlock xBlock) {
		xBlock.statements = visitTree(xBlock.statements);
	}

	@Override
	public void visitBreak(XBreak xBreak) {}

	@Override
	public void visitContinue(XContinue xContinue) {}

	@Override
	public void visitDo(XDo xDo) {
		xDo.doWhile = visitTree(xDo.doWhile);
		xDo.block = visitTree(xDo.block);
	}

	@Override
	public void visitWhile(XWhile xWhile) {
		xWhile.doWhile = visitTree(xWhile.doWhile);
		xWhile.block = visitTree(xWhile.block);
	}

	@Override
	public void visitFor(XFor xFor) {
		xFor.init = visitTree(xFor.init);
		xFor.doWhile = visitTree(xFor.doWhile);
		xFor.inc = visitTree(xFor.inc);
		xFor.block = visitTree(xFor.block);
	}

	@Override
	public void visitIf(XIf xIf) {
		xIf.iif = visitTree(xIf.iif);
		xIf.block = visitTree(xIf.block);
		xIf.block2 = visitTree(xIf.block2);
	}

	@Override
	public void visitReturn(XReturn xReturn) {
		xReturn.statement = visitTree(xReturn.statement);
	}

	@Override
	public void visitThrow(XThrow xThrow) {
		xThrow.statement = visitTree(xThrow.statement);
	}

	@Override
	public void visitVarDecls(XVarDecls xVarDecls) {
		xVarDecls.varDecls = visitTree(xVarDecls.varDecls);
	}

	@Override
	public void visitGroup(XGroup xGroup) {
		xGroup.statement = visitTree(xGroup.statement);
	}

	@Override
	public void visitThrow(XSynchroized xSynchroized) {
		xSynchroized.ident = visitTree(xSynchroized.ident);
		xSynchroized.block = visitTree(xSynchroized.block);
	}

	@Override
	public void visitConstant(XConstant xConstant) {}

	@Override
	public void visitMethodCall(XMethodCall xMethodCall) {
		xMethodCall.method = visitTree(xMethodCall.method);
		xMethodCall.params = visitTree(xMethodCall.params);
	}

	@Override
	public void visitNew(XNew xNew) {
		xNew.className = visitTree(xNew.className);
		xNew.params = visitTree(xNew.params);
	}

	@Override
	public void visitOperator(XOperatorStatement xOperatorStatement) {
		xOperatorStatement.left = visitTree(xOperatorStatement.left);
		xOperatorStatement.right = visitTree(xOperatorStatement.right);
	}

	@Override
	public void visitOperatorPrefixSuffix(XOperatorPrefixSuffix xOperatorPrefixSuffix) {
		xOperatorPrefixSuffix.statement = visitTree(xOperatorPrefixSuffix.statement);
	}

	@Override
	public void visitIndex(XIndex xIndex) {
		xIndex.array = visitTree(xIndex.array);
		xIndex.index = visitTree(xIndex.index);
	}

	@Override
	public void visitIfOperator(XIfOperator xIfOperator) {
		xIfOperator.left = visitTree(xIfOperator.left);
		xIfOperator.statement = visitTree(xIfOperator.statement);
		xIfOperator.right = visitTree(xIfOperator.right);
	}

}
