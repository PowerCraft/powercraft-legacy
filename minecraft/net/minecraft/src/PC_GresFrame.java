package net.minecraft.src;

public class PC_GresFrame extends PC_GresWidget {

	/**
	 * horizontal layout
	 */
	int border = 5;
	
	public PC_GresFrame() {
		super();
	}
	
	@Override
	public PC_CoordI calcSize() {
		calcChildPositions();
		if (size.x < minSize.x + border*2) size.x = minSize.x + border*2;
		if (size.y < minSize.y + border*2) size.y = minSize.y + border*2;

		return size.copy();
	}

	@Override
	public void calcChildPositions() {
		int xx = 0, xSize = 0;
		for (PC_GresWidget w: childs) {
			w.calcChildPositions();
			PC_CoordI csize = w.calcSize();
			if (csize.x + xSize + border*2 > size.x || csize.y > size.y) {
				if (csize.x + xSize + border*2 > size.x) size.x = csize.x + xSize + border*2;
				if (csize.y + border*2 > size.y) size.y = csize.y + border*2;
				if (parent != null) parent.calcChildPositions();
				calcChildPositions();
				return;
			}
			xSize += csize.x + widgetMargin;
			// childs.get(i).setPosition(xx, height/2 - childs.get(i).getSize().y/2);
			// xx += size.x + widgetDistance;
		}
		xSize -= widgetMargin;
		for (PC_GresWidget w: childs) {
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
	protected void render(PC_CoordI mpos) {
		drawHorizontalLine(mpos.x + pos.x, mpos.x + pos.x + size.x - 1, mpos.y + pos.y, 0xffffffff);
		drawVerticalLine(mpos.x + pos.x, mpos.y + pos.y, mpos.y + pos.y + size.y, 0xffffffff);
		
		drawHorizontalLine(mpos.x + pos.x + 1, mpos.x + pos.x + size.x, mpos.y + pos.y + size.y, 0xff373737);
		drawVerticalLine(mpos.x + pos.x + size.x, mpos.y + pos.y, mpos.y + pos.y + size.y, 0xff373737);
		
		drawHorizontalLine(mpos.x + pos.x, mpos.x + pos.x, mpos.y + pos.y + size.y, 0xff8B8B8B);
		drawHorizontalLine(mpos.x + pos.x + size.x, mpos.x + pos.x + size.x, mpos.y + pos.y, 0xff8B8B8B);
		
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
		return calcSize();
	}

	@Override
	public void mouseWheel(int i) {}

	@Override
	public void addedToWidget() {}

}
