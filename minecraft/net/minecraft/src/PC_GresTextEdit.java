package net.minecraft.src;

import org.lwjgl.input.Keyboard;

public class PC_GresTextEdit extends PC_GresWidget {

	private int maxChars;
	
	PC_GresTextEdit(String title, int chars){
		super((chars+1)*10, PC_Utils.mc().fontRenderer.FONT_HEIGHT + 12, title);
		maxChars = chars;
		canAddWidget = false;
	}
	
	@Override
	public int[] calcSize() {
		return new int[]{width, height};
	}

	@Override
	public void calcChildPositions() {
	}

	@Override
	protected void render(int xOffset, int yOffset) {
		
		drawHorizontalLine(xOffset + x, xOffset + x + width - 1, yOffset + y, 0xffA0A0A0);
		drawHorizontalLine(xOffset + x, xOffset + x + width - 1, yOffset + y + height - 1, 0xffA0A0A0);
		
		drawVerticalLine(xOffset + x, yOffset + y, yOffset + y + height - 1, 0xffA0A0A0);
		drawVerticalLine(xOffset + x + width - 1, yOffset + y, yOffset + y + height - 1, 0xffA0A0A0);
		
		drawRect(xOffset + x + 1, yOffset + y + 1, xOffset + x + width - 1, yOffset + y + height - 1, 0xff000000);
		
		if(label.length()>maxChars)
			label = label.substring(0, maxChars);
		
		if(fontRenderer!=null)
			fontRenderer.drawStringWithShadow(label + (hasFocus&& (cursorCounter / 6) % 2 == 0?"_":""), xOffset + x + 6, yOffset + y + (height - 8) / 2, 0xffffffff);
		else
			PC_Utils.mc().fontRenderer.drawStringWithShadow(label + (hasFocus&& (cursorCounter / 6) % 2 == 0?"_":""), xOffset + x + 6, yOffset + y + (height - 8) / 2, 0xffffffff);
		
	}

	@Override
	public boolean mouseOver(int x, int y) {
		return true;
	}

	@Override
	public boolean mouseClick(int x, int y, int key) {
		return key!=0;
	}

	@Override
	public void keyTyped(char c, int key) {
		if (!enabled || !hasFocus)
			return;
		switch(key){
			case Keyboard.KEY_BACK:
				if(label.length()>1)
					label = label.substring(0, label.length()-1);
				else if(label.length()==1)
					label = "";
				break;
			default:
				if(ChatAllowedCharacters.isAllowedCharacter(c))
					label += c;
				break;
		}
	}

	@Override
	public void mouseMove(int x, int y) {
	}

	@Override
	public int[] getMinSize() {
		return new int[]{(maxChars+1)*10, PC_Utils.mc().fontRenderer.FONT_HEIGHT + 12};
	}

}
