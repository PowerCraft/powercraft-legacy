package net.minecraft.src;

import net.minecraft.client.Minecraft;

/**
 * Hypertext-like GUI element.
 * 
 * @author MightyPork
 * @copy 2012
 */
public class PC_GuiClickableText extends GuiButton {
	private final FontRenderer fontRenderer;

	private final int width;
	private final int height;

	private final int xPosition;
	private final int yPosition;

	private boolean enabled;
	private boolean drawButton;

	private String displayString;

	/**
	 * Clickable text
	 * 
	 * @param fr font renderer
	 * @param i id
	 * @param j x
	 * @param k y
	 * @param s label string
	 */
	public PC_GuiClickableText(FontRenderer fr, int i, int j, int k, String s) {
		super(i, j, k, s);

		fontRenderer = fr;
		width = fontRenderer.getStringWidth(s) + 4;
		height = 12;
		enabled = true;
		drawButton = true;
		xPosition = j;
		yPosition = k;
		displayString = s;
	}

	@Override
	protected int getHoverState(boolean flag) {
		byte byte0 = 1;
		if (!enabled) {
			byte0 = 0;
		} else if (flag) {
			byte0 = 2;
		}
		return byte0;
	}

	@Override
	public void drawButton(Minecraft minecraft, int i, int j) {
		if (!drawButton) { return; }

		boolean flag = i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height;
		getHoverState(flag);

		mouseDragged(minecraft, i, j);

		if (!enabled) {
			fontRenderer.drawString(displayString, xPosition + 3, yPosition + 3, 0xff999999);
			fontRenderer.drawString(displayString, xPosition + 2, yPosition + 2, 0xff606060);
		} else if (flag) {
			fontRenderer.drawString(displayString, xPosition + 3, yPosition + 4, 0xff9999BB);
			fontRenderer.drawString(displayString, xPosition + 2, yPosition + 3, 0xff000099);
		} else {
			fontRenderer.drawString(displayString, xPosition + 3, yPosition + 3, 0xffAAAAAA);
			fontRenderer.drawString(displayString, xPosition + 2, yPosition + 2, 0xff000000);
		}
	}

	@Override
	protected void mouseDragged(Minecraft minecraft, int i, int j) {}

	@Override
	public void mouseReleased(int i, int j) {}

	@Override
	public boolean mousePressed(Minecraft minecraft, int i, int j) {
		return enabled && drawButton && i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height;
	}
}
