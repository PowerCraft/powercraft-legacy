package weasel.compiler.tokenmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import weasel.compiler.WeaselToken;

public class WeaselTokenMapOperatorBlock extends WeaselTokenMapOperator {

	public List<WeaselTokenMap> childs = new ArrayList<WeaselTokenMap>();

	public WeaselTokenMapOperatorBlock(WeaselToken token, WeaselTokenMap...childs) {
		super(token);
		this.childs.addAll(Arrays.asList(childs));
	}

	public void addChild(WeaselTokenMap child){
		childs.add(child);
	}
	
	@Override
	public String toString() {
		return "["+left+"]"+token+childs+"["+right+"]";
	}
	
}
