package net.minecraft.src;

public class PC_GresButton extends PC_GresWidget {
	
	private boolean isClicked = false;
	
	public PC_GresButton(String label){
		super(label);
		canAddWidget = false;
	}
	
	@Override
	public int[] calcSize() {
		width = PC_Utils.mc().fontRenderer.getStringWidth(label);
		height = PC_Utils.mc().fontRenderer.FONT_HEIGHT;
		width += 12;
		height += 12;
		if(width<40)
			width = 40;
		return new int[]{width, height};
	}

	@Override
	public void calcChildPositions() {

	}

	@Override
	protected void render(int xOffset, int yOffset) {
		
		int state = (!enabled)?0:(isClicked?3:(isMouseOver?2:1));
		
		int txC = 0xe0e0e0;
		
		if (state == 0)
		{
			txC = 0xffa0a0a0;
		}
		else if (state > 1)
		{
			txC = 0xffffa0;
		}

		renderTextureSliced(xOffset, yOffset, mod_PCcore.getImgDir()+"button.png", width, height, 0, state*50, 256, 50, 5, 5);
		
		if(fontRenderer!=null)
			drawCenteredString(fontRenderer, label, xOffset + x + width / 2, yOffset + y + (height - 8) / 2, txC);
		else
			drawCenteredString(PC_Utils.mc().fontRenderer, label, xOffset + x + width / 2, yOffset + y + (height - 8) / 2, txC);
	}

	@Override
	public boolean mouseOver(int x, int y) {
		isMouseOver = true;
		return true;
	}

	
	
	@Override
	public boolean mouseClick(int x, int y, int key) {
		if(!enabled)
			return false;
		if(isClicked && key==-1){
			isClicked = false;
			return true;
		}
		isClicked = key==-1?false:true;
		return false;
	}

	@Override
	public void keyTyped(char c, int key) {
		
	}

	@Override
	public void mouseMove(int x, int y) {
		if(x<0 || x>=width || y<0 || y>=height || mouseOver(x, y)==false){
			isClicked = false;
		}
	}

	@Override
	public int[] getMinSize() {
		return calcSize();
	}

}
