package net.minecraft.src;


/**
 * Frame widget with padding and horizontal layout
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PC_GresFrame extends PC_GresWidget {


	/** distance from borders to contents. */
	int framePadding = 5;

	/**
	 * frame widget H
	 */
	public PC_GresFrame() {
		super();
	}

	@Override
	public PC_CoordI calcSize() {
		if (!visible) return zerosize;
		calcChildPositions();
		if (size.x < minSize.x + framePadding * 2) {
			size.x = minSize.x + framePadding * 2;
		}
		if (size.y < minSize.y + framePadding * 2) {
			size.y = minSize.y + framePadding * 2;
		}

		return size.copy();
	}

	@Override
	public void calcChildPositions() {
		if (!visible) return;
		int xx = 0, xSize = 0;
		for (PC_GresWidget w : childs) {
			w.calcChildPositions();
			PC_CoordI csize = w.calcSize();
			if (csize.x + xSize + framePadding * 2 > size.x || csize.y > size.y) {
				if (csize.x + xSize + framePadding * 2 > size.x) {
					size.x = csize.x + xSize + framePadding * 2;
				}
				if (csize.y + framePadding * 2 > size.y) {
					size.y = csize.y + framePadding * 2;
				}
				if (parent != null) {
					parent.calcChildPositions();
				}
				calcChildPositions();
				return;
			}
			xSize += csize.x + widgetMargin;
			// childs.get(i).setPosition(xx, height/2 - childs.get(i).getSize().y/2);
			// xx += size.x + widgetDistance;
		}
		xSize -= widgetMargin;
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
	protected PC_RectI render(PC_CoordI mpos, PC_RectI scissorOld, double scale) {
		renderTextureSliced(mpos, mod_PCcore.getImgDir() + "gres/frame.png", size, new PC_CoordI(0, 0), new PC_CoordI(256, 256));
		return null;
	}

	@Override
	public boolean mouseOver(PC_CoordI mpos) {
		if (!visible) return false;
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
