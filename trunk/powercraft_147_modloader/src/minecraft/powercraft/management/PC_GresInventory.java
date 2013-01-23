package powercraft.management;


import net.minecraft.src.Slot;

import org.lwjgl.opengl.GL11;


/**
 * Gres Inventory with a grid of slots.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PC_GresInventory extends PC_GresWidget {

	/** The slots */
	public Slot slots[][];

	/** Inventory grid width */
	public int gridWidth = 0;

	/** Inventory grid height */
	public int gridHeight = 0;

	/**
	 * Inventory widget, with empty slot grid. To be filled using setSlot()
	 * 
	 * @param width grid width
	 * @param height grid height
	 */
	public PC_GresInventory(int width, int height) {
		super(width * 18, height * 18);

		gridHeight = height;
		gridWidth = width;

		canAddWidget = false;
		slots = new Slot[gridWidth][gridHeight];
	}

	@Override
	public PC_VecI getMinSize() {
		return calcSize();
	}

	@Override
	public PC_VecI calcSize() {
		return new PC_VecI(gridWidth * 18, gridHeight * 18);
	}

	@Override
	public void calcChildPositions() {}

	@Override
	protected PC_RectI render(PC_VecI posOffset, PC_RectI scissorOld, double scale) {
		String texture = imgdir + "widgets.png";
		PC_VecI posOnScrren = getPositionOnScreen();
		PC_VecI widgetPos = null;
		PC_GresWidget w = this;

		while (w != null) {
			widgetPos = w.getPosition();
			w = w.getParent();
		}

		posOnScrren.x -= widgetPos.x;
		posOnScrren.y -= widgetPos.y;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(texture));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				if (slots[x][y] != null) {
					slots[x][y].xDisplayPosition = posOnScrren.x + x * 18 + 1;
					slots[x][y].yDisplayPosition = posOnScrren.y + y * 18 + 1;
				}
				drawTexturedModalRect(pos.x + posOffset.x + x * 18, pos.y + posOffset.y + y * 18, 0, 66, 18, 18);
			}
		}
		return null;
	}

	@Override
	public MouseOver mouseOver(PC_VecI mousePos) {
		return MouseOver.THIS;
	}

	@Override
	public boolean mouseClick(PC_VecI mousePos, int key) {
		return key != -1;
	}

	@Override
	public void mouseMove(PC_VecI mousePos) {}

	@Override
	public void mouseWheel(int i) {}

	@Override
	public boolean keyTyped(char c, int key) {
		return false;
	}

	@Override
	public void addedToWidget() {

	}

	/**
	 * Set single slot
	 * 
	 * @param slot the slot
	 * @param x x position in grid
	 * @param y y position in grid
	 * @return this
	 */
	public PC_GresInventory setSlot(Slot slot, int x, int y) {
		if (x >= 0 && x < this.slots.length && y >= 0 && y < this.slots[x].length) {
			if(this.slots[x][y]!=null){
				this.slots[x][y].xDisplayPosition = -999;
				this.slots[x][y].yDisplayPosition = -999;
			}
			this.slots[x][y] = slot;
		}
		return this;
	}

	/**
	 * Get slot
	 * 
	 * @param x x position in grid
	 * @param y y position in grid
	 * @return this
	 */
	public Slot getSlot(int x, int y) {
		if (x >= 0 && x < this.slots.length && y >= 0 && y < this.slots[x].length) {
			return this.slots[x][y];
		}
		return null;
	}

	protected void visibleChanged(boolean show){
		if(!show){
			for (int x = 0; x < gridWidth; x++) {
				for (int y = 0; y < gridHeight; y++) {
					if (slots[x][y] != null) {
						slots[x][y].xDisplayPosition = -999;
						slots[x][y].yDisplayPosition = -999;
					}
				}
			}
		}
	}
	
}
