package xscript.compiler;

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

public interface XVisitor {

	public void visitTopLevel(XClassFile xClassFile);

	public void visitImport(XImport xImport);

	public void visitClassDecl(XClassDecl xClassDef);

	public void visitAnnotation(XAnnotation xAnnotation);

	public void visitModifier(XModifier xModifier);

	public void visitError(XError xError);

	public void visitIdent(XIdent xIdent);

	public void visitType(XType xType);

	public void visitTypeParam(XTypeParam xTypeParam);

	public void visitVarDecl(XVarDecl xVarDecl);

	public void visitMethodDecl(XMethodDecl xMethodDecl);

	public void visitBlock(XBlock xBlock);

	public void visitBreak(XBreak xBreak);

	public void visitContinue(XContinue xContinue);

	public void visitDo(XDo xDo);

	public void visitWhile(XWhile xWhile);

	public void visitFor(XFor xFor);

	public void visitIf(XIf xIf);

	public void visitReturn(XReturn xReturn);

	public void visitThrow(XThrow xThrow);

	public void visitVarDecls(XVarDecls xVarDecls);

	public void visitGroup(XGroup xGroup);

	public void visitThrow(XSynchroized xSynchroized);

	public void visitConstant(XConstant xConstant);

	public void visitMethodCall(XMethodCall xMethodCall);

	public void visitNew(XNew xNew);

	public void visitOperator(XOperatorStatement xOperatorStatement);

	public void visitOperatorPrefixSuffix(XOperatorPrefixSuffix xOperatorPrefixSuffix);

	public void visitIndex(XIndex xIndex);

	public void visitIfOperator(XIfOperator xIfOperator);

}
