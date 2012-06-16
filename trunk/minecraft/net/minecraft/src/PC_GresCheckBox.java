package net.minecraft.src;

public class PC_GresCheckBox extends PC_GresWidget {

	private static final int WIDTH = 11;
	private boolean checked = false;
	
	public PC_GresCheckBox(String title){
		super(title);
		canAddWidget = false;
	}
	
	public boolean isChecked() {
		return checked;
	}

	public PC_GresCheckBox check(boolean state) {
		checked = state;
		return this;
	}
	
	@Override
	public int[] calcSize() {
		width = PC_Utils.mc().fontRenderer.getStringWidth(label);
		height = PC_Utils.mc().fontRenderer.FONT_HEIGHT;
		width += WIDTH + 3;
		if(height<WIDTH)
			height = WIDTH;
		return new int[]{width, height};
	}
	
	@Override
	public int[] getMinSize() {
		return calcSize();
	}

	@Override
	public void calcChildPositions() {
		
	}

	@Override
	protected void render(int xOffset, int yOffset) {
		int c1 = 0xff555555, c2 = 0xffffffff, c3 = 0xff8b8b8b;
		
		drawHorizontalLine(xOffset + x, xOffset + x + WIDTH - 2, yOffset + y, c1);
		drawHorizontalLine(xOffset + x + 1, xOffset + x + WIDTH - 1, yOffset + y + WIDTH - 1, c2);
		
		drawVerticalLine(xOffset + x, yOffset + y, yOffset + y + WIDTH - 1, c1);
		drawVerticalLine(xOffset + x + WIDTH - 1, yOffset + y, yOffset + y + WIDTH - 1, c2);
		
		drawPoint(xOffset + x, yOffset + y + WIDTH - 1, c3);
		drawPoint(xOffset + x + WIDTH - 1, yOffset + y, c3);
		
		if (checked)
			drawString(fontRenderer, "x", xOffset + x + 3, yOffset + y + 1, 0x000000);
		
		drawString(fontRenderer, label, xOffset + x + WIDTH + 3, yOffset + y + 1, 0x000000);
	}

	@Override
	public boolean mouseOver(int x, int y) {
		return true;
	}

	@Override
	public boolean mouseClick(int x, int y, int key) {
		if(!enabled)
			return false;
		if(key!=-1)
			checked ^= true;
		return true;
	}

	@Override
	public void mouseMove(int x, int y) {
		
	}

	@Override
	public void keyTyped(char c, int key) {
		
	}
	
}
