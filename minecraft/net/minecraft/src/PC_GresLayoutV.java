package net.minecraft.src;

/**
 * Resizable GUI vertical layout
 * 
 * @author XOR19
 * @copy (c) 2012
 * 
 */
public class PC_GresLayoutV extends PC_GresWidget {

	/**
	 * vertical layout
	 */
	public PC_GresLayoutV() {
		super();
	}

	@Override
	public PC_CoordI calcSize() {
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
		int yy = 0, ySize = 0;
		int lastmargin = 0;
		for (PC_GresWidget w : childs) {
			w.calcChildPositions();
			PC_CoordI csize = w.calcSize();
			if (csize.x > size.x || ySize + csize.y > size.y) {
				if (csize.x > size.x) {
					size.x = csize.x;
				}
				if (ySize + csize.y > size.y) {
					size.y = ySize + csize.y;
				}
				if (parent != null) {
					parent.calcChildPositions();
				}
				calcChildPositions();
				return;
			}
			;
			lastmargin = w.widgetMargin;
			ySize += csize.y + w.widgetMargin;
		}
		// ySize -= lastmargin;
		for (PC_GresWidget w : childs) {
			PC_CoordI csize = w.getSize();
			int xPos = 0;
			int yPos = 0;
			switch (alignH) {
				case LEFT:
					xPos = 0;
					break;
				case RIGHT:
					xPos = size.x - csize.x;
					break;
				case CENTER:
					xPos = size.x / 2 - csize.x / 2;
					break;
				case STRETCH:
					xPos = 0;
					w.setSize(size.x, w.getSize().y, false);
					break;
			}
			switch (alignV) {
				case TOP:
					yPos = yy;
					break;
				case BOTTOM:
					yPos = size.y - ySize + yy;
					break;
				case CENTER:
					yPos = size.y / 2 - ySize / 2 + yy;
					break;
				case STRETCH:
					yPos = yy;
					break;
			}
			w.setPosition(xPos, yPos);
			yy += csize.y + w.widgetMargin;
		}
	}

	@Override
	protected void render(PC_CoordI pos) {}

	@Override
	public boolean mouseOver(PC_CoordI pos) {
		return true;
	}

	@Override
	public boolean mouseClick(PC_CoordI pos, int key) {
		return false;
	}

	@Override
	public boolean keyTyped(char c, int key) {
		return false;
	}

	@Override
	public void mouseMove(PC_CoordI pos) {}

	@Override
	public PC_CoordI getMinSize() {
		return calcSize();
	}

	@Override
	public void mouseWheel(int i) {}

	@Override
	public void addedToWidget() {}
}
