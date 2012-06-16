package net.minecraft.src;

/**
 * 
 * GuiScreen class
 * 
 * @authors XOR19 & Rapus95
 * @copy (c) 2012
 *
 */
public class PC_GresGui extends GuiScreen implements PC_IGresGui {

	private PC_IGresBase gui;
	private PC_GresVGroup child;
	private PC_GresWidget lastFocus;
	private boolean pauseGame=false;
	
	/**
	 * Constructor for creating a gui
	 * @param gui the gui
	 */
	public PC_GresGui(PC_IGresBase gui){
		this.gui = gui;
	}
	
	@Override
	public PC_GresWidget add(PC_GresWidget widget) {
		return child.add(widget);
	}
	
	@Override
	public void setPauseGame(boolean b){
		pauseGame = b;
	}
	
	@Override
	public void close(){
		mc.displayGuiScreen(null);
		mc.setIngameFocus();
	}
	
	@Override
	public void updateScreen() {
		if(lastFocus!=null)
			lastFocus.updateCursorCounter();
	}
	
	@Override
	public void initGui() {
		child = new PC_GresVGroup();
		child.setFontRenderer(fontRenderer);
		child.setSize(width, height);
		gui.initGui(this);
	}

	@Override
	public void onGuiClosed() {
		gui.onGuiClosed(this);
	}
	
	@Override
	protected void keyTyped(char c, int i) {
		if(lastFocus!=null){
			if(gui.isKeyValid(c, i, lastFocus, this)){
				lastFocus.keyTyped(c, i);
				gui.actionPerformed(lastFocus, this);
			}
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
		if(lastFocus!=null){
			PC_CoordI fpos = lastFocus.getPositionOnScreen();
			lastFocus.mouseMove(new PC_CoordI(i - fpos.x, j - fpos.y));
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
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		child.updateRenderer(new PC_CoordI(0, 0));
		super.drawScreen(i, j, f);
	}
	
}
