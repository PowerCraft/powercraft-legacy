package net.minecraft.src;


/**
 * Resizable GUI vertical separation line
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PC_GresSeparatorV extends PC_GresWidget {
	/** Line color (0xrrggbb) */
	int lineColor = 0x555555;

	/**
	 * vertical separator
	 * 
	 * @param width width
	 * @param height min height
	 */
	public PC_GresSeparatorV(int width, int height) {
		super(width, height);
		canAddWidget = false;
		setMinWidth(3);
		setMinSize(width,height);
	}

	/**
	 * @return the line color
	 */
	public int getLineColor() {
		return lineColor;
	}

	/**
	 * Set line color.
	 * 
	 * @param lineColor the line color to set
	 */
	public PC_GresSeparatorV setLineColor(int lineColor) {
		this.lineColor = lineColor;
		return this;
	}


	@Override
	public PC_CoordI calcSize() {
		return minSize.copy();
	}

	@Override
	protected void render(PC_CoordI off) {
		drawRect(off.x + size.x / 2 + pos.x, off.y + pos.y, off.x + size.x / 2 + pos.x+1, off.y + size.y + pos.y+1, lineColor | 0xff000000);
	}

	@Override
	public boolean mouseOver(PC_CoordI mpos) {
		return false;
	}

	@Override
	public boolean mouseClick(PC_CoordI mpos, int key) {
		return false;
	}

	@Override
	public boolean keyTyped(char c, int key) {
		return false;
	}

	@Override
	public void calcChildPositions() {

	}

	@Override
	public void mouseMove(PC_CoordI mpos) {

	}

	@Override
	public PC_CoordI getMinSize() {
		return calcSize();
	}

	@Override
	public void mouseWheel(int i) {}

	@Override
	public void addedToWidget() {}
}
