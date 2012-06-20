package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class PC_GresInventoryBigSlot extends PC_GresWidget {

	/** The slots */
	public Slot slot;
	
	/**
	 * Inventory widget, with empty slot grid. To be filled using setSlot()
	 * @param width grid width
	 * @param height grid height
	 */
	public PC_GresInventoryBigSlot(Slot slot){
		super(26, 26);
		canAddWidget = false;
		this.slot = slot;
	}
	
	@Override
	public PC_CoordI getMinSize() {
		return calcSize();
	}

	@Override
	public PC_CoordI calcSize() {
		return new PC_CoordI(26, 26);
	}

	@Override
	public void calcChildPositions() {
	}

	@SuppressWarnings("null")
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
		if(slot!=null){
			slot.xDisplayPosition = posOnScrren.x + 5;
			slot.yDisplayPosition = posOnScrren.y + 5;
		}
		drawTexturedModalRect(pos.x + posOffset.x, pos.y+posOffset.y, 18, 66, 26, 26);
	}

	@Override
	public boolean mouseOver(PC_CoordI mousePos) {
		return true;
	}

	@Override
	public boolean mouseClick(PC_CoordI mousePos, int key) {
		return key!=-1;
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
		
		if(containerManager!=null && (parent==null || !(parent instanceof PC_GresInventoryPlayer))){		
			containerManager.addSlot(slot);
		}
		
	}
	
	/**
	 * Set single slot
	 * @param slot the slot
	 * @return this
	 */
	public PC_GresInventoryBigSlot setSlot(Slot slot){
		if(containerManager!=null){
			if(this.slot==null){
				if(slot!=null){
					containerManager.addSlot(slot);
				}
			}else{
				if(slot==null){
					containerManager.removeSlot(this.slot.slotNumber);
				}else{
					containerManager.setSlot(this.slot.slotNumber, slot);
				}
			}
		}
		this.slot = slot;
		return this;
	}
	
	/**
	 * Get slot
	 * @return Slot
	 */
	public Slot getSlot(){
		return this.slot;
	}
	

}
