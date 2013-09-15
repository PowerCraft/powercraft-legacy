package powercraft.api.gres;

import powercraft.api.PC_RectI;
import powercraft.api.PC_Vec2I;

class PC_GresScrollAreaContainer extends PC_GresContainer {
	
	private PC_GresScrollArea scrollArea;
	
	protected PC_GresScrollAreaContainer(PC_GresScrollArea scrollArea){
		this.scrollArea = scrollArea;
	}
	
	@Override
	protected void setParent(PC_GresContainer parent) {}

	@Override
	protected PC_Vec2I calculateMinSize() {
		return new PC_Vec2I(-1, -1);
	}

	@Override
	protected PC_Vec2I calculateMaxSize() {
		return new PC_Vec2I(-1, -1);
	}

	@Override
	protected PC_Vec2I calculatePrefSize() {
		return new PC_Vec2I(-1, -1);
	}

	@Override
	protected void paint(PC_RectI scissor, double scale, int displayHeight, float timeStamp) {}

	@Override
	public boolean isRecursiveVisible() {
		return visible && scrollArea.isRecursiveVisible();
	}
	
	@Override
	public boolean isRecursiveEnabled() {
		return enabled && scrollArea.isRecursiveEnabled();
	}
	
	@Override
	protected void notifyParentOfChange() {
		scrollArea.notifyChange();
	}

	@Override
	protected void handleMouseWheel(PC_Vec2I mouse, int buttons, int wheel) {
		scrollArea.onMouseWheel(mouse, buttons, wheel);
	}
	
	@Override
	protected PC_Vec2I getRealLocation() {
		return rect.getLocation().add(scrollArea.getRealLocation());
	}
	
	@Override
	public PC_GresGuiHandler getGuiHandler() {
		return scrollArea.getGuiHandler();
	}
	
	@Override
	protected void moveToTop(){
		scrollArea.moveToTop();
	}
	
	@Override
	protected void moveToBottom(){
		scrollArea.moveToBottom();
	}
	
}
