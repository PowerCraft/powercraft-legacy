package net.minecraft.src;

/**
 * 
 * Window for GUI
 * 
 * @authors XOR19, Rapus95, MightyPork
 * @copy (c) 2012
 * 
 */

public class PC_GresWindow extends PC_GresWidget {

	/**
	 * distance to the window frame
	 */
	static final int distanceToFrame = 10;

	/**
	 * 
	 * @param minX minimal X size
	 * @param minY minimal Y size
	 * @param title title of the window
	 */
	public PC_GresWindow(int minX, int minY, String title) {
		super(minX, minY, title);
	}
	
	/**
	 * Create window of width 240 and height auto.
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

	@Override
	public PC_CoordI calcSize() {
		int textWidth = PC_Utils.mc().fontRenderer.getStringWidth(text);
		if (size.x < textWidth + distanceToFrame * 2 + 12) {
			size.x = textWidth + distanceToFrame * 2 + 12;
		}
		calcChildPositions();
		return size.copy();
	}

	@Override
	public void render(PC_CoordI offsetPos) {

		renderTextureSliced(offsetPos, mod_PCcore.getImgDir() + "gres/dialog.png", size, new PC_CoordI(0, 0), new PC_CoordI(256, 256));

		getFontRenderer().drawString(text, offsetPos.x + pos.x + (size.x) / 2 - fontRenderer.getStringWidth(text) / 2, offsetPos.y + pos.y + 8, 0x505050);

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
		int yy = 0, minySize = 0, minmaxxSize = 0, maxxSize = 0, ySize = 0, yPlus = PC_Utils.mc().fontRenderer.FONT_HEIGHT + 15;
		int childNum = childs.size();
		for (int i = 0; i < childNum; i++) {
			childs.get(i).calcChildPositions();
			PC_CoordI csize = childs.get(i).calcSize();
			PC_CoordI cminSize = childs.get(i).getMinSize();
			ySize += csize.y + widgetMargin;
			minySize += cminSize.y + widgetMargin;
			if (maxxSize < csize.x) maxxSize = csize.x;
			if (minmaxxSize < cminSize.x) minmaxxSize = cminSize.x;
		}

		if (alignV == PC_GresAlign.STRETCH) {
			maxxSize = minmaxxSize;
			ySize = minySize;
			for (int i = 0; i < childNum; i++) {
				PC_CoordI cminSize = childs.get(i).getMinSize();
				childs.get(i).setSize(cminSize.x, cminSize.y, false);
			}
		}

		if (maxxSize + distanceToFrame * 2 > size.x || ySize + yPlus + distanceToFrame > size.y) {
			if (maxxSize + distanceToFrame * 2 > size.x) size.x = maxxSize + distanceToFrame * 2;
			if (ySize + yPlus + distanceToFrame > size.y) size.y = ySize + yPlus + distanceToFrame;
			if (parent != null) parent.calcChildPositions();
			calcChildPositions();
			return;
		}

		ySize -= widgetMargin;

		for (int i = 0; i < childNum; i++) {
			PC_CoordI csize = childs.get(i).getSize();
			int xPos = 0;
			int yPos = 0;
			int s = 0;

			switch (alignH) {
				case LEFT:
					xPos = distanceToFrame;
					break;
				case RIGHT:
					xPos = size.x - childs.get(i).getSize().x - distanceToFrame;
					break;
				case CENTER:
					xPos = size.x / 2 - childs.get(i).getSize().x / 2;
					break;
				case STRETCH:
					xPos = distanceToFrame;
					childs.get(i).setSize(size.x - distanceToFrame * 2, childs.get(i).getSize().y, false);
					break;
			}

			switch (alignV) {
				case TOP:
					yPos = yPlus + yy;
					break;
				case BOTTOM:
					yPos = size.y - distanceToFrame - ySize + yy;
					break;
				case CENTER:
					yPos = (size.y + yPlus - distanceToFrame) / 2 - ySize / 2 + yy;
					break;
				case STRETCH:
					s = (size.y - yPlus - distanceToFrame - ySize + widgetMargin - widgetMargin * childNum) / childNum;
					childs.get(i).setSize(childs.get(i).getSize().x, childs.get(i).getSize().y + s, false);
					yPos = yPlus + yy;
					break;
			}

			childs.get(i).setPosition(xPos, yPos);
			yy += csize.y + widgetMargin + s;
		}

	}

	@Override
	public void mouseMove(PC_CoordI pos) {}

	@Override
	public PC_CoordI getMinSize() {
		return calcSize();
	}

	@Override
	public void mouseWheel(int i) {
	}
	
}
