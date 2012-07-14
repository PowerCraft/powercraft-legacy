package net.minecraft.src;


/**
 * Window for GUI
 * 
 * @authors XOR19, Rapus95, MightyPork
 * @copy (c) 2012
 */

public class PC_GresWindow extends PC_GresWidget {

	/**
	 * distance to the window frame
	 */
	protected PC_CoordI padding = new PC_CoordI(10, 4);
	/**
	 * The gap right under the top title.<br>
	 * Applies only if title != ""
	 */
	protected int gapUnderTitle = 10;

	/**
	 * @param minX minimal X size
	 * @param minY minimal Y size
	 * @param title title of the window
	 */
	public PC_GresWindow(int minX, int minY, String title) {
		super(minX, minY, title);
	}

	/**
	 * Create window of width 240 and height auto.
	 * 
	 * @param title title of the window
	 */
	public PC_GresWindow(String title) {
		super(240, 0, title);
	}

	/**
	 * Create window of width 240 and height auto, no title.
	 */
	public PC_GresWindow() {
		super(240, 0, "");
	}

	/**
	 * Set standard width and stuff for an inventory screen.<br>
	 * Used to look exactly like normal inventory screens.
	 * 
	 * @return this
	 */
	public PC_GresWidget setWidthForInventory() {
		setMinWidth(176);
		padding.setTo(7, 7);
		this.size = minSize.copy();
		this.minSize = minSize.copy();
		calcSize();
		return this;
	}

	@Override
	public PC_CoordI calcSize() {
		int textWidth = PC_Utils.mc().fontRenderer.getStringWidth(text);
		if (size.x < textWidth + padding.x * 2 + 12) {
			size.x = textWidth + padding.y * 2 + 12;
		}
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
	public void render(PC_CoordI offsetPos) {

		renderTextureSliced(offsetPos, mod_PCcore.getImgDir() + "gres/dialog.png", size, new PC_CoordI(0, 0), new PC_CoordI(256, 256));

		if (text.length() > 0) {
			getFontRenderer().drawString(text, offsetPos.x + pos.x + (size.x) / 2 - fontRenderer.getStringWidth(text) / 2, offsetPos.y + pos.y + 8, 0x404040);
		}

	}

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
	public void calcChildPositions() {

		int yy = 0, minySize = 0, minmaxxSize = 0, maxxSize = 0, ySize = 0, yPlus = getFontRenderer().FONT_HEIGHT + gapUnderTitle;

		if (text.length() == 0) {
			yPlus = 0;
		}

		int childNum = childs.size();
		for (int i = 0; i < childNum; i++) {
			PC_GresWidget child = childs.get(i);
			child.calcChildPositions();
			PC_CoordI csize = child.calcSize();
			PC_CoordI cminSize = child.getMinSize();
			ySize += csize.y + child.widgetMargin;
			minySize += cminSize.y + child.widgetMargin;
			if (maxxSize < csize.x) {
				maxxSize = csize.x;
			}
			if (minmaxxSize < cminSize.x) {
				minmaxxSize = cminSize.x;
			}
		}

		if (alignV == PC_GresAlign.STRETCH) {
			maxxSize = minmaxxSize;
			ySize = minySize;
			for (int i = 0; i < childNum; i++) {
				PC_CoordI cminSize = childs.get(i).getMinSize();
				childs.get(i).setSize(cminSize.x, cminSize.y, false);
			}
		}

		if (maxxSize + padding.x * 2 > size.x || ySize + yPlus + padding.y > size.y) {
			if (maxxSize + padding.x * 2 > size.x) {
				size.x = maxxSize + padding.x * 2;
			}
			if (ySize + yPlus + padding.y > size.y) {
				size.y = ySize + yPlus + padding.y;
			}
			if (parent != null) {
				parent.calcChildPositions();
			}
			calcChildPositions();
			return;
		}

		ySize -= widgetMargin;

		for (int i = 0; i < childNum; i++) {
			PC_GresWidget child = childs.get(i);

			PC_CoordI csize = child.getSize();
			int xPos = 0;
			int yPos = 0;
			int s = 0;

			switch (alignH) {
				case LEFT:
					xPos = padding.x;
					break;
				case RIGHT:
					xPos = size.x - child.getSize().x - padding.x;
					break;
				case CENTER:
					xPos = size.x / 2 - child.getSize().x / 2;
					break;
				case STRETCH:
					xPos = padding.x;
					child.setSize(size.x - padding.x * 2, child.getSize().y, false);
					break;
			}

			switch (alignV) {
				case TOP:
					yPos = yPlus + yy;
					break;
				case BOTTOM:
					yPos = size.y - padding.y - ySize + yy;
					break;
				case CENTER:
					yPos = (size.y + yPlus - padding.y) / 2 - ySize / 2 + yy;
					break;
//				case STRETCH:
//					s = (size.y - yPlus - padding.y - ySize + widgetMargin - widgetMargin * childNum) / childNum;
//					child.setSize(child.getSize().x, child.getSize().y + s, false);
//					yPos = yPlus + yy;
//					break;
			}

			child.setPosition(xPos, yPos);
			yy += csize.y + widgetMargin + s;
		}

/*		
		int yy = 0, minySize = 0, minmaxxSize = 0, maxxSize = 0, ySize = 0, yPlus = getFontRenderer().FONT_HEIGHT + gapUnderTitle;

		if (text.length() == 0) {
			yPlus = 0;
		}

		int childNum = childs.size();
		for (int i = 0; i < childNum; i++) {
			childs.get(i).calcChildPositions();
			PC_CoordI childSize = childs.get(i).calcSize();
			PC_CoordI childMinSize = childs.get(i).getMinSize();
			ySize += childSize.y + childs.get(i).widgetMargin;
			minySize += childMinSize.y + childs.get(i).widgetMargin;
			if (maxxSize < childSize.x) {
				maxxSize = childSize.x;
			}
			if (minmaxxSize < childMinSize.x) {
				minmaxxSize = childMinSize.x;
			}
		}

		if (alignV == PC_GresAlign.STRETCH) {
			maxxSize = minmaxxSize;
			ySize = minySize;
			for (int i = 0; i < childNum; i++) {
				PC_CoordI cminSize = childs.get(i).getMinSize();
				childs.get(i).setSize(cminSize.x, cminSize.y, false);
			}
		}

		if (maxxSize + padding.x * 2 > size.x || ySize + yPlus + padding.y > size.y) {
			if (maxxSize + padding.x * 2 > size.x) {
				size.x = maxxSize + padding.x * 2;
			}
			if (ySize + yPlus + padding.y > size.y) {
				size.y = ySize + yPlus + padding.y;
			}
			if (parent != null) {
				parent.calcChildPositions();
			}
			calcChildPositions();
			return;
		}

		//ySize -= widgetMargin;

		for (int i = 0; i < childNum; i++) {
			PC_CoordI csize = childs.get(i).getSize();
			int xPos = 0;
			int yPos = 0;
			int s = 0;

			switch (alignH) {
				case LEFT:
					xPos = padding.x;
					break;
				case RIGHT:
					xPos = size.x - childs.get(i).getSize().x - padding.x;
					break;
				case CENTER:
					xPos = size.x / 2 - childs.get(i).getSize().x / 2;
					break;
				case STRETCH:
					xPos = padding.x;
					childs.get(i).setSize(size.x - padding.x * 2, childs.get(i).getSize().y, false);
					break;
			}

			switch (alignV) {
				case TOP:
					yPos = yPlus + yy;
					break;
				case BOTTOM:
					yPos = size.y + yPlus - padding.y - ySize + yy;
					break;
				case CENTER:
					yPos = (size.y + yPlus - padding.y) / 2 - ySize / 2 + yy;
					break;
				case STRETCH:
					s = (size.y - yPlus - padding.y - ySize + widgetMargin - widgetMargin * childNum) / childNum;
					childs.get(i).setSize(childs.get(i).getSize().x, childs.get(i).getSize().y + s, false);
					yPos = yPlus + yy;
					break;
			}

			childs.get(i).setPosition(xPos, yPos);
			yy += csize.y + childs.get(i).widgetMargin + s;
		}
		
*/

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
