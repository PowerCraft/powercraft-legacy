package net.minecraft.src;


/**
 * Resizable GUI horizontal layout
 * 
 * @author XOR19
 * @copy (c) 2012
 */
public class PC_GresLayoutH extends PC_GresWidget {

	/**
	 * horizontal layout
	 */
	public PC_GresLayoutH() {
		super();
	}

	@Override
	public PC_CoordI calcSize() {
		if (!visible) return zerosize;
		calcChildPositions();
		if (size.x < minSize.x) {
			size.x = minSize.x;
		}
		if (size.y < minSize.y) {
			size.y = minSize.y;
		}

		return size.copy();
	}

	@Override
	public void calcChildPositions() {
		if (!visible) return;
		int xx = 0, xSize = 0;
		int lastcm = 0;
		for (PC_GresWidget child : childs) {
			child.calcChildPositions();
			PC_CoordI csize = child.calcSize();
			if (csize.x + xSize > size.x || csize.y > size.y) {
				if (csize.x + xSize > size.x) {
					size.x = csize.x + xSize;
				}
				if (csize.y > size.y) {
					size.y = csize.y + child.widgetMargin;
				}
				if (parent != null) {
					parent.calcChildPositions();
				}
				calcChildPositions();
				return;
			}
			xSize += csize.x + child.widgetMargin;
			lastcm = child.widgetMargin;
		}
		xSize -= lastcm;
		for (PC_GresWidget child : childs) {
			PC_CoordI csize = child.getSize();
			int xPos = 0;
			int yPos = 0;
			switch (alignH) {
				case LEFT:
					xPos = xx;
					break;
				case RIGHT:
					xPos = size.x - xSize + xx;
					break;
				case CENTER:
					xPos = size.x / 2 - xSize / 2 + xx;
					break;
				case STRETCH:
					xPos = xx;
					break;
			}
			switch (alignV) {
				case TOP:
					yPos = 0;
					break;
				case BOTTOM:
					yPos = size.y - csize.y;
					break;
				case CENTER:
					yPos = size.y / 2 - csize.y / 2;
					break;
				case STRETCH:
					yPos = 0;
					child.setSize(child.getSize().x, size.y, false);
					break;
			}
			child.setPosition(xPos, yPos);
			xx += csize.x + child.widgetMargin;
		}
	}

	@Override
	protected PC_RectI render(PC_CoordI offsetPos, PC_RectI scissorOld, double scale) {
		return null;
	}

	@Override
	public boolean mouseOver(PC_CoordI mpos) {
		return true;
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
	public void mouseMove(PC_CoordI mpos) {}

	@Override
	public PC_CoordI getMinSize() {
		if (!visible) return zerosize;
		return calcSize();
	}

	@Override
	public void mouseWheel(int i) {}

	@Override
	public void addedToWidget() {}
}
