// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst

package net.minecraft.src;

import java.util.List;

/**
 * Checkbox GUI element
 * 
 * @author MightyPork
 */
public class PC_GuiCheckBox extends Gui {
	private final FontRenderer fontRenderer;
	private final int xPos;
	private final int yPos;

	/** List of connected radio buttons (other checkboxes including this one) */
	public List<PC_GuiCheckBox> radios = null;

	private static final int WIDTH = 9;

	private String text;
	private boolean checked = false;


	/** GUI element focused */
	public boolean isEnabled;

	private int colorTextEnabled = 0x000000;
	private int colorTextDisabled = 0x707070;
	private int colorTextShadow = 0xAAAAAA;

	/**
	 * @param radio list of checkboxes in a radiogroup.
	 */
	public void setRadioList(List<PC_GuiCheckBox> radio) {
		radios = radio;
	}

	/**
	 * Set enabled and disabled text colors. Format is hex 0xRRGGBB.
	 * 
	 * @param en enabled color
	 * @param dis disabled color
	 */
	public void setTextColors(int en, int dis) {
		colorTextEnabled = en;
		colorTextDisabled = dis;
	}

	/**
	 * Is this checkbox checked?
	 * 
	 * @return checked state
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * Set checked state
	 * 
	 * @param state checked
	 */
	public void check(boolean state) {
		checked = state;
	}

	/**
	 * @param guiscreen the parent screen
	 * @param fontrenderer font renderer
	 * @param x x coord
	 * @param y y coord
	 * @param check is checked?
	 * @param s string label
	 */
	public PC_GuiCheckBox(GuiScreen guiscreen, FontRenderer fontrenderer, int x, int y, boolean check, String s) {
		isEnabled = true;
		fontRenderer = fontrenderer;
		xPos = x;
		yPos = y;
		checked = check;
		setText(s);
	}

	/**
	 * Set label text
	 * 
	 * @param s
	 */
	public void setText(String s) {
		text = s;
	}

	/**
	 * Get label text
	 * 
	 * @return text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Perform mouse click. If mouse is above this checkbox, change checked
	 * state.
	 * 
	 * @param i x coord
	 * @param j y coord
	 * @param k ? unused
	 */
	public void mouseClicked(int i, int j, int k) {
		boolean flag = isEnabled && i >= xPos && i < xPos + WIDTH + fontRenderer.getStringWidth(text) + 2 && j >= yPos && j < yPos + WIDTH;
		if (flag) {

			if (checked && radios != null) { return; }

			checked = !checked;

			if (checked) {
				if (radios != null) {
					for (PC_GuiCheckBox cb : radios) {
						if (cb != this) {
							cb.check(false);
						}
					}
				}
			}

		}
	}

	/**
	 * Draw the element on GUI screen
	 */
	public void drawCheckBox() {
		drawRect(xPos - 1, yPos - 1, xPos + WIDTH + 1, yPos + WIDTH + 1, 0xff8b8b8b);
		drawRect(xPos - 1, yPos - 1, xPos + WIDTH, yPos + WIDTH, 0xff555555);
		drawRect(xPos, yPos, xPos + WIDTH + 1, yPos + WIDTH + 1, 0xffffffff);
		drawRect(xPos, yPos, xPos + WIDTH, yPos + WIDTH, isEnabled ? 0xffc6c6c6 : 0xff969696);

		if (checked) {
			fontRenderer.drawString("x", xPos + 2 + 1, yPos + 1, colorTextShadow);
			fontRenderer.drawString("x", xPos + 2, yPos, isEnabled ? colorTextEnabled : colorTextDisabled);
		}

		fontRenderer.drawString(text, xPos + WIDTH + 3 + 1, yPos + 1 + 1, colorTextShadow);
		fontRenderer.drawString(text, xPos + WIDTH + 3, yPos + 1, isEnabled ? colorTextEnabled : colorTextDisabled);

	}
}
