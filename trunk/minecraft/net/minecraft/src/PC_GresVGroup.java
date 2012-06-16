package net.minecraft.src;

/**
 * Gui Resizable Vertical Group Layout Widget
 * 
 * @copy (c) 2012
 *
 */
public class PC_GresVGroup extends PC_GresWidget {

	public PC_GresVGroup() {
		super();
	}

	@Override
	public PC_CoordI calcSize() {
		calcChildPositions();
		return size.copy();
	}

	@Override
	public void calcChildPositions() {
		int yy = 0, ySize = 0;
		for (int i = 0; i < childs.size(); i++) {
			childs.get(i).calcChildPositions();
			PC_CoordI csize = childs.get(i).calcSize();
			if (csize.x > size.x || ySize + csize.y > size.y) {
				if (csize.x > size.x) size.x = csize.x;
				if (ySize + csize.y > size.y) size.y = ySize + csize.y;
				if (parent != null) parent.calcChildPositions();
				calcChildPositions();
				return;
			}
			ySize += csize.y + widgetMargin;
		}
		ySize -= widgetMargin;
		for (int i = 0; i < childs.size(); i++) {
			PC_CoordI csize = childs.get(i).getSize();
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
					childs.get(i).setSize(size.x, childs.get(i).getSize().y, false);
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
			childs.get(i).setPosition(xPos, yPos);
			yy += csize.y + widgetMargin;
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
	public void keyTyped(char c, int key) {}

	@Override
	public void mouseMove(PC_CoordI pos) {}

	@Override
	public PC_CoordI getMinSize() {
		return calcSize();
	}

}
