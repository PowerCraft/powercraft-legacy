package net.minecraft.src;


/**
 * Resizable GUI button
 * 
 * @author XOR19, Rapus95, MightyPork
 * @copy (c) 2012
 */
public class PC_GresButton extends PC_GresWidget {

	/**
	 * Button inner padding - distance from the borders. Added twice, once on
	 * each side.
	 */
	protected PC_CoordI buttonScale = new PC_CoordI(6, 6);

	/** Flag that is currently under pressed mouse */
	protected boolean isClicked = false;

	/**
	 * @param label button label
	 */
	public PC_GresButton(String label) {
		super(label);
		canAddWidget = false;
		minSize.setTo(60, 0);
		buttonScale = new PC_CoordI(6, 6);
	}

	/**
	 * Set distance from text to borders of the box.
	 * 
	 * @param x distance horizontally
	 * @param y distance vertically
	 * @return this
	 */
	public PC_GresButton setButtonPadding(int x, int y) {
		buttonScale = new PC_CoordI(x, y);
		return this;
	}

	@Override
	public PC_CoordI calcSize() {
		if (!visible) return zerosize;
		FontRenderer fontRenderer = getFontRenderer();

		if (buttonScale == null) buttonScale = new PC_CoordI(6, 6);

		size.setTo(fontRenderer.getStringWidth(text), fontRenderer.FONT_HEIGHT).add(buttonScale).add(buttonScale);

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

	}

	@Override
	protected void render(PC_CoordI offsetPos) {

		int state;
		if (!enabled || !parent.enabled) {
			state = 0; // disabled
		} else if (isClicked) {
			state = 3; // enabled and clicked
		} else if (isMouseOver) {
			state = 2; // enabled and hover
		} else {
			state = 1; // enabled and not hover
		}

		int txC = 0xe0e0e0;

		if (state == 0) {
			txC = 0xa0a0a0; // dark
		}
		if (state == 1) {
			txC = 0xe0e0e0; // light
		}
		if (state > 1) {
			txC = 0xffffa0; // yellow
		}

		renderTextureSliced(offsetPos, mod_PCcore.getImgDir() + "gres/button.png", size, new PC_CoordI(0, state * 50), new PC_CoordI(256, 50));

		drawCenteredString(getFontRenderer(), text, offsetPos.x + pos.x + size.x / 2, offsetPos.y + pos.y + (size.y - getFontRenderer().FONT_HEIGHT)
				/ 2, txC);
	}

	@Override
	public boolean mouseOver(PC_CoordI mpos) {
		isMouseOver = true;
		return true;
	}


	@Override
	public boolean mouseClick(PC_CoordI mpos, int key) {
		if (!enabled || !visible) {
			return false;
		}
		if (!parent.enabled) {
			return false;
		}
		if (isClicked && key == -1) {
			isClicked = false;
			return true;
		}
		isClicked = key == -1 ? false : true;
		if (key != -1) mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
		return false;
	}

	@Override
	public boolean keyTyped(char c, int key) {
		return false;
	}

	@Override
	public void mouseMove(PC_CoordI mpos) {
		if (mpos.x < 0 || mpos.x >= size.x || mpos.y < 0 || mpos.y >= size.y || mouseOver(mpos) == false) {
			isClicked = false;
		}
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
