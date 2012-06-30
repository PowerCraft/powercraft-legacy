package net.minecraft.src;


import java.util.HashSet;

import org.lwjgl.opengl.GL11;


/**
 * Resizable GUI radio button
 * 
 * @author XOR19, MightyPork
 * @copy (c) 2012
 */
public class PC_GresRadioButton extends PC_GresWidget {

	/**
	 * Radio Group for Radio Button
	 * 
	 * @author MightyPork
	 * @copy (c) 2012
	 */
	public static class PC_GresRadioGroup extends HashSet<PC_GresRadioButton> {

	}

	private static final int WIDTH = 11;
	private boolean checked = false;
	private PC_GresRadioGroup radioGroup;

	/**
	 * Radio btn
	 * 
	 * @param title label
	 * @param group radio group
	 */
	public PC_GresRadioButton(String title, PC_GresRadioGroup group) {
		super(title);
		canAddWidget = false;
		color[textColorEnabled] = 0x000000;
		color[textColorShadowEnabled] = 0xAAAAAA;
		color[textColorDisabled] = 0x707070;
		color[textColorShadowDisabled] = 0xAAAAAA;

		radioGroup = group;
		radioGroup.add(this);
	}

	/**
	 * @return is button selected
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * Set selected state, if "true", clear all others.
	 * 
	 * @param state on/off
	 * @return this
	 */
	public PC_GresRadioButton check(boolean state) {
		checked = state;

		if (checked) {
			for (PC_GresRadioButton btn : radioGroup) {
				if (btn != this) {
					btn.check(false);
				}
			}
		}

		return this;
	}

	@Override
	public PC_CoordI calcSize() {

		size.setTo(getStringWidth(text), getLineHeight()).add(WIDTH + 3, 0);

		if (size.y < WIDTH) {
			size.y = WIDTH;
		}
		return size.copy();
	}

	@Override
	public PC_CoordI getMinSize() {
		return calcSize();
	}

	@Override
	public void calcChildPositions() {

	}

	@Override
	protected void render(PC_CoordI offsetPos) {
		String texture = mod_PCcore.getImgDir() + "gres/widgets.png";
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(texture));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		int state = 0;
		if (isChecked()) {
			state = 1;
		}
		if (!isEnabled()) {
			state += 2;
		}

		drawTexturedModalRect(pos.x + offsetPos.x, pos.y + offsetPos.y, WIDTH * state, WIDTH, WIDTH, WIDTH);

		drawString(text, offsetPos.x + pos.x + WIDTH + 3, offsetPos.y + pos.y + 2);
	}

	@Override
	public boolean mouseOver(PC_CoordI mpos) {
		return true;
	}

	@Override
	public boolean mouseClick(PC_CoordI mpos, int key) {
		if (!enabled) {
			return false;
		}
		if (key != -1) {
			check(true);
			return true;
		}
		return false;
	}

	@Override
	public void mouseMove(PC_CoordI mpos) {

	}

	@Override
	public boolean keyTyped(char c, int key) {
		return false;
	}

	@Override
	public void mouseWheel(int i) {}

	@Override
	public void addedToWidget() {}
}
