package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class PC_GresInventory extends PC_GresWidget {

	Slot slot[][];
	
	PC_GresInventory(Slot slot[][]){
		super(slot.length*18, slot[0].length*18);
		canAddWidget = false;
		this.slot = slot;
	}
	
	@Override
	public PC_CoordI getMinSize() {
		return new PC_CoordI(slot.length*18, slot[0].length*18);
	}

	@Override
	public PC_CoordI calcSize() {
		return new PC_CoordI(slot.length*18, slot[0].length*18);
	}

	@Override
	public void calcChildPositions() {
	}

	@Override
	protected void render(PC_CoordI posOffset) {
		String texture = mod_PCcore.getImgDir() + "gres/widgets.png";
		PC_CoordI posOnScrren = getPositionOnScreen();
		PC_CoordI widgetPos = null;
		PC_GresWidget w = this;
		while(w!=null){
			widgetPos = w.getPosition();
			w = w.getParent();
		}
		posOnScrren.x -= widgetPos.x;
		posOnScrren.y -= widgetPos.y;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(texture));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		for(int x=0; x<slot.length; x++)
			for(int y=0; y<slot[x].length; y++){
				slot[x][y].xDisplayPosition = posOnScrren.x + x*18 + 1;
				slot[x][y].yDisplayPosition = posOnScrren.y + y*18 + 1;
				drawTexturedModalRect(pos.x + posOffset.x + x*18, pos.y+posOffset.y+ y*18, 0, 66, 18, 18);
			}
	}

	@Override
	public boolean mouseOver(PC_CoordI mousePos) {
		return false;
	}

	@Override
	public boolean mouseClick(PC_CoordI mousePos, int key) {
		return false;
	}

	@Override
	public void mouseMove(PC_CoordI mousePos) {
	}

	@Override
	public void mouseWheel(int i) {
	}

	@Override
	public boolean keyTyped(char c, int key) {
		return false;
	}
	
	@Override
	public void addedToWidget() {
		if(containerManager!=null)
			for(int x=0; x<slot.length; x++)
				for(int y=0; y<slot[x].length; y++)
					containerManager.addSlot(slot[x][y]);
	}
}
