package weasel.compiler.tokenmap;

import java.util.ArrayList;
import java.util.List;

import weasel.compiler.WeaselToken;

public class WeaselTokenMapOperatorBlock extends WeaselTokenMapOperator {

	public List<WeaselTokenMap> childs = new ArrayList<WeaselTokenMap>();

	public WeaselTokenMapOperatorBlock(WeaselToken token, List<WeaselTokenMap> childs) {
		super(token);
		this.childs = childs;
	}

	public void addChild(WeaselTokenMap child){
		childs.add(child);
	}
	
	@Override
	public String toString() {
		return "["+left+"]"+token+childs+"["+right+"]";
	}
	
}
