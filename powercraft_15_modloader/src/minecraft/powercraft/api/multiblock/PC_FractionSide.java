package powercraft.api.multiblock;

import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_VecI;

public enum PC_FractionSide {
	MIDDLE(PC_FractionType.CUBE), 
	TOP(PC_FractionType.PLAIN), 
	BOTTOM(PC_FractionType.PLAIN), 
	FRONT(PC_FractionType.PLAIN),
	FRONTTOP(PC_FractionType.EDGE), 
	FRONTBOTTOM(PC_FractionType.EDGE), 
	FRONTLEFT(PC_FractionType.EDGE),
	FRONTLEFTTOP(PC_FractionType.CORNER), 
	FRONTLEFTBOTTOM(PC_FractionType.CORNER),
	LEFT(PC_FractionType.PLAIN), 
	LEFTTOP(PC_FractionType.EDGE), 
	LEFTBOTTOM(PC_FractionType.EDGE), 
	BACKLEFT(PC_FractionType.EDGE), 
	BACKLEFTTOP(PC_FractionType.CORNER),
	BACKLEFTBOTTOM(PC_FractionType.CORNER), 
	BACK(PC_FractionType.PLAIN), 
	BACKTOP(PC_FractionType.EDGE), 
	BACKBOTTOM(PC_FractionType.EDGE), 
	BACKRIGHT(PC_FractionType.EDGE), 
	BACKRIGHTTOP(PC_FractionType.CORNER), 
	BACKRIGHTBOTTOM(PC_FractionType.CORNER), 
	RIGHT(PC_FractionType.PLAIN), 
	RIGHTTOP(PC_FractionType.EDGE), 
	RIGHTBOTTOM(PC_FractionType.EDGE), 
	FRONTRIGHT(PC_FractionType.EDGE), 
	FRONTRIGHTOP(PC_FractionType.CORNER), 
	FRONTRIGHTBOTTOM(PC_FractionType.CORNER);

	public final int index;
	public final PC_FractionType type;
	
	PC_FractionSide(PC_FractionType type){
		index=Counter.index++;
		this.type = type;
	}
	
	public static PC_FractionSide fromIndex(int i) {
		return values()[i];
	}
	
	public static PC_Direction getDir(PC_FractionSide side){
		if(side==PC_FractionSide.BOTTOM){
			return PC_Direction.BOTTOM;
		}else if(side==PC_FractionSide.TOP){
			return PC_Direction.TOP;
		}else if(side==PC_FractionSide.LEFT){
			return PC_Direction.LEFT;
		}else if(side==PC_FractionSide.RIGHT){
			return PC_Direction.RIGHT;
		}else if(side==PC_FractionSide.FRONT){
			return PC_Direction.FRONT;
		}else if(side==PC_FractionSide.BACK){
			return PC_Direction.BACK;
		}
		return null;
	}
	
	public static PC_FractionSide getFractionSide(PC_Direction dir){
		if(dir==PC_Direction.BOTTOM){
			return PC_FractionSide.BOTTOM;
		}else if(dir==PC_Direction.TOP){
			return PC_FractionSide.TOP;
		}else if(dir==PC_Direction.LEFT){
			return PC_FractionSide.LEFT;
		}else if(dir==PC_Direction.RIGHT){
			return PC_FractionSide.RIGHT;
		}else if(dir==PC_Direction.FRONT){
			return PC_FractionSide.FRONT;
		}else if(dir==PC_Direction.BACK){
			return PC_FractionSide.BACK;
		}
		return null;
	}
	
	private static class Counter{
		public static int index=0;
	}
	
}
