package net.minecraft.src;

public class PC_GresLabel extends PC_GresWidget {

	public PC_GresLabel(String label){
		super(label);
		canAddWidget = false;
	}
	
	@Override
	public int[] calcSize() {
		width = PC_Utils.mc().fontRenderer.getStringWidth(label);
		height = PC_Utils.mc().fontRenderer.FONT_HEIGHT;
		return new int[]{width, height};
	}

	@Override
	protected void render(int xOffset, int yOffset) {
		drawString(label, xOffset + x, yOffset + y);
	}

	@Override
	public boolean mouseOver(int x, int y) {
		return false;
	}

	@Override
	public boolean mouseClick(int x, int y, int key) {
		return false;
	}

	@Override
	public void keyTyped(char c, int key) {

	}

	@Override
	public void calcChildPositions() {
		
	}

	@Override
	public void mouseMove(int x, int y) {

	}

	@Override
	public int[] getMinSize() {
		return calcSize();
	}

}
