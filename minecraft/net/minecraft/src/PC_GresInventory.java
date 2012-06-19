package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class PC_GresInventory extends PC_GresWidget {

	public Slot slots[][];
	public PC_CoordI inventorySize = new PC_CoordI(0, 0);
	
	PC_GresInventory(PC_CoordI sizeSlotGrid){
		super(sizeSlotGrid.x*18, sizeSlotGrid.y*18);
		inventorySize = sizeSlotGrid.copy();
		canAddWidget = false;
		slots = new Slot[sizeSlotGrid.x][sizeSlotGrid.y];
	}
	
	@Override
	public PC_CoordI getMinSize() {
		return calcSize();
	}

	@Override
	public PC_CoordI calcSize() {
		return new PC_CoordI(inventorySize.x*18, inventorySize.y*18);
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
		for(int x=0; x<inventorySize.x; x++){
			for(int y=0; y<inventorySize.y; y++){
				if(slots[x][y]!=null){
					slots[x][y].xDisplayPosition = posOnScrren.x + x*18 + 1;
					slots[x][y].yDisplayPosition = posOnScrren.y + y*18 + 1;
				}
				drawTexturedModalRect(pos.x + posOffset.x + x*18, pos.y+posOffset.y+ y*18, 0, 66, 18, 18);
			}
		}
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
			
			for(int x=0; x<inventorySize.x; x++){				
				for(int y=0; y<inventorySize.y; y++){
					
					if(slots[x][y]!=null){
						containerManager.addSlot(slots[x][y]);
					}		
					
				}				
			}
			
		}
		
	}
	
	public PC_GresInventory setSlot(Slot slot, int x, int y){
		if(x>=0 && x<this.slots.length && y>=0 && y<this.slots[x].length){
			if(containerManager!=null){
				if(this.slots[x][y]==null){
					if(slot!=null){
						containerManager.addSlot(slot);
					}
				}else{
					if(slot==null){
						containerManager.removeSlot(this.slots[x][y].slotNumber);
					}else{
						containerManager.setSlot(this.slots[x][y].slotNumber, slot);
					}
				}
			}
			this.slots[x][y] = slot;
		}
		return this;
	}
	
	public Slot getSlot(int x, int y){
		if(x>=0 && x<this.slots.length && y>=0 && y<this.slots[x].length){
			return this.slots[x][y];
		}
		return null;
	}
	
}
