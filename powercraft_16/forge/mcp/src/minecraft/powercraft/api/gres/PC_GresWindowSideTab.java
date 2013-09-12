package powercraft.api.gres;

import powercraft.api.PC_RectI;
import powercraft.api.PC_Vec2I;

public class PC_GresWindowSideTab extends PC_GresContainer {

	private static PC_GresWindowSideTab openSideTab;
	
	private final PC_Vec2I size = new PC_Vec2I(16, 16);
	
	public PC_GresWindowSideTab(){
		
	}
	
	@Override
	protected PC_Vec2I calculateMinSize() {
		return new PC_Vec2I(16, 16);
	}

	@Override
	protected PC_Vec2I calculateMaxSize() {
		return new PC_Vec2I(100, 100);
	}

	@Override
	protected PC_Vec2I calculatePrefSize() {
		return new PC_Vec2I(-1, -1);
	}
	
	@Override
	protected void paint(PC_RectI scissor, float timeStamp) {
		
	}
	
	@Override
	protected void notifyChange() {
		super.notifyChange();
		rect.setSize(size);
	}

	@Override
	protected void onTick() {
		super.onTick();
		if(openSideTab==this){
			size.setTo(size.add(2).max(16));
			rect.setSize(size);
		}else{
			size.setTo(size.sub(2).max(16));
			rect.setSize(size);
		}
	}
	
}
