package xscript.compiler;

public class XLineDesk {

	public int startLine;
	public int startLinePos;
	public int endLine;
	public int endLinePos;
	
	public XLineDesk(int startLine, int startLinePos, int endLine, int endLinePos){
		this.startLine = startLine;
		this.startLinePos = startLinePos;
		this.endLine = endLine;
		this.endLinePos = endLinePos;
	}

	public XLineDesk(XLineDesk lineDesk) {
		startLine = lineDesk.startLine;
		startLinePos = lineDesk.startLinePos;
		endLine = lineDesk.endLine;
		endLinePos = lineDesk.endLinePos;
	}

	@Override
	public String toString() {
		return "line:" + startLine + ":" + startLinePos + "=>" + endLine + ":" + endLinePos;
	}
	
}
