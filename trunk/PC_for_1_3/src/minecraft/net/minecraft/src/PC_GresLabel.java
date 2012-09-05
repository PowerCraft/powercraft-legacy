package net.minecraft.src;


/**
 * Resizable GUI plain text label
 * 
 * @author XOR19, Rapus95, MightyPork
 * @copy (c) 2012
 */
public class PC_GresLabel extends PC_GresWidget {

	/**
	 * Text label
	 * 
	 * @param label text
	 */
	public PC_GresLabel(String label) {
		super(label);
		canAddWidget = false;

		alignH = PC_GresAlign.LEFT;
		widgetMargin = 4;
	}

	@Override
	public PC_CoordI calcSize() {
		size.setTo(getStringWidth(text), getLineHeight());
		if (size.x < minSize.x) {
			size.x = minSize.x;
		}
		return size.copy();
	}

	@Override
	protected PC_RectI render(PC_CoordI offsetPos, PC_RectI scissorOld, double scale) {
		int wid = getStringWidth(text);
		int xstart = offsetPos.x + pos.x;

		switch (alignH) {
			case LEFT:
				break;
			case CENTER:
				xstart = xstart + size.x / 2 - wid / 2;
				break;
			case RIGHT:
				xstart = xstart + size.x - wid;
		}

		drawString(text, xstart, offsetPos.y + pos.y);

		return null;
	}

	@Override
	public MouseOver mouseOver(PC_CoordI mpos) {
		return MouseOver.THIS;
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
