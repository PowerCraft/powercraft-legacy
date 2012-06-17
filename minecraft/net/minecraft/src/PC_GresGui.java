package net.minecraft.src;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * 
 * GuiScreen class
 * 
 * @authors XOR19, Rapus95, MightyPork
 * @copy (c) 2012
 *
 */
public class PC_GresGui extends GuiContainer implements PC_IGresGui {

	private PC_IGresBase gui;
	private PC_GresLayoutV child;
	private PC_GresWidget lastFocus;
	private PC_GresContainerManager containerManager;
	private boolean pauseGame=false;
	
	/**
	 * Constructor for creating a gui
	 * @param gui the gui
	 */
	public PC_GresGui(PC_IGresBase gui){
		super(new PC_GresContainerManager());
		this.gui = gui;
		containerManager = (PC_GresContainerManager)inventorySlots;
	}
	
	@Override
	public PC_GresWidget add(PC_GresWidget widget) {
		PC_GresWidget c = child.add(widget);
		xSize = child.getSize().x;
		ySize = child.getSize().y;
		guiLeft = (width - xSize) / 2;
	    guiTop = (height - ySize) / 2;
	    child.setPosition(guiLeft, guiTop);
		return c;
	}
	
	@Override
	public void setPausesGame(boolean b){
		pauseGame = b;
	}
	
	@Override
	public void close(){
		mc.displayGuiScreen(null);
		mc.setIngameFocus();
	}
	
	@Override
	public void updateScreen() {
		if(lastFocus!=null){
			lastFocus.updateCursorCounter();
		}
	}
	
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		child = new PC_GresLayoutV();
		child.setFontRenderer(fontRenderer);
		child.setContainerManager(containerManager);
		child.setSize(0, 0);
		gui.initGui(this);
		super.initGui();
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		gui.onGuiClosed(this);
		super.onGuiClosed();
	}
	
	@Override
	protected void keyTyped(char c, int i) {
		
		boolean consumed = false;
		
		if(lastFocus!=null){
			if(lastFocus.keyTyped(c, i)){
				gui.actionPerformed(lastFocus, this);
				consumed = true;
			}
		}
		
		if(!consumed)
			super.keyTyped(c, i);
		if(!consumed && i == Keyboard.KEY_ESCAPE){
			gui.onEscapePressed(this);
		}else if(!consumed && i == Keyboard.KEY_RETURN){
			gui.onReturnPressed(this);
		}
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		
		PC_GresWidget newFocus = child.getWidgetUnderMouse(new PC_CoordI(i, j));
		
		if(newFocus != lastFocus){
			if(lastFocus!=null)
				lastFocus.setFocus(false);
			if(newFocus!=null)
				newFocus.setFocus(true);
			lastFocus = newFocus;
		}
		
		if(newFocus!=null){
			PC_CoordI fpos = newFocus.getPositionOnScreen();
			if(newFocus.mouseClick(new PC_CoordI(i - fpos.x, j - fpos.y), k)){
				gui.actionPerformed(newFocus, this);
			}
		}
	}
	
	private void mouseMoved(int i, int j){
		int wheel = Mouse.getDWheel();
		if(wheel<0)
			wheel = -1;
		if(wheel>0)
			wheel = 1;
		if(lastFocus!=null){
			PC_CoordI fpos = lastFocus.getPositionOnScreen();
			lastFocus.mouseMove(new PC_CoordI(i - fpos.x, j - fpos.y));
			lastFocus.mouseWheel(wheel);
		}
		child.getWidgetUnderMouse(new PC_CoordI(i, j));
	}
	
	private void mouseUp(int i, int j, int k){
		if(lastFocus!=null){
			PC_CoordI fpos = lastFocus.getPositionOnScreen();
			if(lastFocus.mouseClick(new PC_CoordI(i - fpos.x, j - fpos.y), -1)){
				gui.actionPerformed(lastFocus, this);
			}
		}
	}
	
	@Override
	protected void mouseMovedOrUp(int i, int j, int k) {
		super.mouseMovedOrUp(i, j, k);
		if(k!=-1)
			mouseUp(i, j, k);
		else
			mouseMoved(i, j);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return pauseGame;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		drawDefaultBackground();
		child.updateRenderer(new PC_CoordI(0, 0));
	}
	
}
