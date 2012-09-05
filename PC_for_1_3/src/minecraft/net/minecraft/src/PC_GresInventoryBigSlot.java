package net.minecraft.src;


/**
 * Inventory with single huge slot, used in crafting gui
 * 
 * @author XOR19
 */
public class PC_GresInventoryBigSlot extends PC_GresWidget {

	/** The slots */
	public Slot slot;

	/**
	 * Inventory widget, with empty slot grid. To be filled using setSlot()
	 * 
	 * @param slot the only slot in this inventory
	 */
	public PC_GresInventoryBigSlot(Slot slot) {
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
	public void calcChildPositions() {}

	@Override
	protected PC_RectI render(PC_CoordI posOffset, PC_RectI scissorOld, double scale) {
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

		if (slot != null) {
			slot.xDisplayPosition = posOnScrren.x + 5;
			slot.yDisplayPosition = posOnScrren.y + 5;
		}

		renderImage(posOffset, texture, new PC_CoordI(26, 26), new PC_CoordI(18, 66));
		return null;
	}

	@Override
	public MouseOver mouseOver(PC_CoordI mousePos) {
		return MouseOver.THIS;
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

	}

	/**
	 * Set single slot
	 * 
	 * @param slot the slot
	 * @return this
	 */
	public PC_GresInventoryBigSlot setSlot(Slot slot) {
		this.slot = slot;
		return this;
	}

	/**
	 * Get slot
	 * 
	 * @return Slot
	 */
	public Slot getSlot() {
		return this.slot;
	}


}
