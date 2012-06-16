package net.minecraft.src;

public class PC_GresLabel extends PC_GresWidget {
	int color = 0x000000;

	public PC_GresLabel(String title){
		super(title);
		canAddWidget = false;
	}
	
	@Override
	public int[] calcSize() {
		width = PC_Utils.mc().fontRenderer.getStringWidth(title);
		height = PC_Utils.mc().fontRenderer.FONT_HEIGHT;
		return new int[]{width, height};
	}

	@Override
	protected void render(int xOffset, int yOffset) {
		if(fontRenderer!=null){
			fontRenderer.drawStringWithShadow(title, xOffset + x, yOffset + y, color);
		}else{
			PC_Utils.mc().fontRenderer.drawString(title, xOffset + x, yOffset + y, color);
		}
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
