package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/**
 * Gres Inventory with a grid of slots.
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
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

	/**
	 * Inventory widget, auto-filled with slots from an inventory.
	 * 
	 * @param inventory the inventory to take slots from
	 * @param width grid width
	 * @param height grid height
	 */
	public PC_GresInventory(IInventory inventory, int width, int height) {
		super(width * 18, height * 18);

		gridHeight = height;
		gridWidth = width;

		canAddWidget = false;

		slots = new Slot[gridWidth][gridHeight];

		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				if (i + j * width < inventory.getSizeInventory()) {
					setSlot(new Slot(inventory, i + j * width, 0, 0), i, j);
				}
			}
		}
	}

	/**
	 * Inventory widget with SINGLE SLOT from an inventory
	 * 
	 * @param inventory the inventory to take slots from
	 * @param slot slot number
	 */
	public PC_GresInventory(IInventory inventory, int slot) {
		super(18, 18);

		gridHeight = 1;
		gridWidth = 1;

		canAddWidget = false;

		slots = new Slot[1][1];

		setSlot(new Slot(inventory, slot, 0, 0), 0, 0);

	}

	/**
	 * Inventory widget auto-filled with slots, with START and END slot.
	 * 
	 * @param inventory the inventory to take slots from
	 * @param width grid width
	 * @param height grid height
	 * @param slotStart index of first (left top) slot
	 * @param slotEnd index of last (right bottom, usually) slot
	 */
	public PC_GresInventory(IInventory inventory, int width, int height, int slotStart, int slotEnd) {
		super(width * 18, height * 18);

		gridHeight = height;
		gridWidth = width;

		canAddWidget = false;

		slots = new Slot[gridWidth][gridHeight];

		adding:
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {

				if (slotStart + i + j * width > slotEnd) {
					break adding;
				}

				if (slotStart + i + j * width < inventory.getSizeInventory()) {
					setSlot(new Slot(inventory, slotStart + i + j * width, 0, 0), i, j);
				}

			}
		}
	}

	@Override
	public PC_CoordI getMinSize() {
		return calcSize();
	}

	@Override
	public PC_CoordI calcSize() {
		return new PC_CoordI(gridWidth * 18, gridHeight * 18);
	}

	@Override
	public void calcChildPositions() {}

	@SuppressWarnings("null")
	@Override
	protected void render(PC_CoordI posOffset) {
		String texture = mod_PCcore.getImgDir() + "gres/widgets.png";
		PC_CoordI posOnScrren = getPositionOnScreen();
		PC_CoordI widgetPos = null;
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
	}

	@Override
	public boolean mouseOver(PC_CoordI mousePos) {
		return true;
	}

	@Override
	public boolean mouseClick(PC_CoordI mousePos, int key) {
		return key != -1;
	}

	@Override
	public void mouseMove(PC_CoordI mousePos) {}

	@Override
	public void mouseWheel(int i) {}

	@Override
	public boolean keyTyped(char c, int key) {
		return false;
	}

	@Override
	public void addedToWidget() {

		if (containerManager != null && (parent == null || !(parent instanceof PC_GresInventoryPlayer))) {

			for (int x = 0; x < gridWidth; x++) {
				for (int y = 0; y < gridHeight; y++) {

					if (slots[x][y] != null) {
						containerManager.addSlot(slots[x][y]);
					}

				}
			}

		}

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
			if (containerManager != null) {
				if (this.slots[x][y] == null) {
					if (slot != null) {
						containerManager.addSlot(slot);
					}
				} else {
					if (slot == null) {
						containerManager.removeSlot(this.slots[x][y].slotNumber);
					} else {
						containerManager.setSlot(this.slots[x][y].slotNumber, slot);
					}
				}
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
		if (x >= 0 && x < this.slots.length && y >= 0 && y < this.slots[x].length) { return this.slots[x][y]; }
		return null;
	}

}
