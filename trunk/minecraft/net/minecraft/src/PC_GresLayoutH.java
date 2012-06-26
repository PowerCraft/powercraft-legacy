package net.minecraft.src;

/**
 * Resizable GUI horizontal layout
 * 
 * @author XOR19
 * @copy (c) 2012
 * 
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
		int xx = 0, xSize = 0;
		for (PC_GresWidget w : childs) {
			w.calcChildPositions();
			PC_CoordI csize = w.calcSize();
			if (csize.x + xSize > size.x || csize.y > size.y) {
				if (csize.x + xSize > size.x) {
					size.x = csize.x + xSize;
				}
				if (csize.y > size.y) {
					size.y = csize.y;
				}
				if (parent != null) {
					parent.calcChildPositions();
				}
				calcChildPositions();
				return;
			}
			xSize += csize.x + w.widgetMargin;
			// childs.get(i).setPosition(xx, height/2 - childs.get(i).getSize().y/2);
			// xx += size.x + widgetDistance;
		}
		// xSize -= widgetMargin;
		for (PC_GresWidget w : childs) {
			PC_CoordI csize = w.getSize();
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
					w.setSize(w.getSize().x, size.y, false);
					break;
			}
			w.setPosition(xPos, yPos);
			xx += csize.x + w.widgetMargin;
		}
	}

	@Override
	protected void render(PC_CoordI mpos) {}

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
		return calcSize();
	}

	@Override
	public void mouseWheel(int i) {}

	@Override
	public void addedToWidget() {}
}
