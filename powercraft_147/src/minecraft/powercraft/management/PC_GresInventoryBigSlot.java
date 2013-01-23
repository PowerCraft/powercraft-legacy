package powercraft.management;

import net.minecraft.inventory.Slot;


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
	public PC_VecI getMinSize() {
		return calcSize();
	}

	@Override
	public PC_VecI calcSize() {
		return new PC_VecI(26, 26);
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

		if (slot != null) {
			slot.xDisplayPosition = posOnScrren.x + 5;
			slot.yDisplayPosition = posOnScrren.y + 5;
		}

		renderImage(posOffset, texture, new PC_VecI(26, 26), new PC_VecI(18, 66));
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
	
	protected void visibleChanged(boolean show){
		if(!show){
			if (slot != null) {
				slot.xDisplayPosition = -999;
				slot.yDisplayPosition = -999;
			}
		}
	}
	
}
