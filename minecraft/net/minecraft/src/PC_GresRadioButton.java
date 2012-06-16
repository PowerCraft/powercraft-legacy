package net.minecraft.src;

public class PC_GresRadioButton extends PC_GresWidget {

	private static final int WIDTH = 11;
	private boolean checked = false;
	private PC_GresRadioButton next = null;
	private PC_GresRadioButton prev = null;
	
	public PC_GresRadioButton(String title, PC_GresRadioButton otherRadio){
		super(title);
		canAddWidget = false;
		color[textColorEnabled] = 0x000000;
		color[textColorShadowEnabled] = 0xAAAAAA;
		color[textColorDisabled] = 0x707070;
		color[textColorShadowDisabled] = 0xAAAAAA;
		if(otherRadio!=null){
			next = otherRadio.next;
			otherRadio.next = this;
			if(next!=null)
				next.prev = this;
		}
		prev = otherRadio;
	}
	
	public boolean isChecked() {
		return checked;
	}

	public PC_GresRadioButton check() {
		checked = true;
		if(prev!=null)
			prev.uncheck(true);
		if(next!=null)
			next.uncheck(false);
		return this;
	}
	
	private void uncheck(boolean toLeft){
		checked = false;
		if(toLeft){
			if(prev!=null)
				prev.uncheck(true);
		}else
			if(next!=null)
				next.uncheck(false); 
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
		
		drawHorizontalLine(xOffset + x + 3, xOffset + x + WIDTH - 4, yOffset + y, c1);
		drawHorizontalLine(xOffset + x + 3, xOffset + x + WIDTH - 4, yOffset + y + WIDTH - 1, c2);
		
		drawVerticalLine(xOffset + x, yOffset + y + 2, yOffset + y + WIDTH - 3, c1);
		drawVerticalLine(xOffset + x + WIDTH - 1, yOffset + y + 2, yOffset + y + WIDTH - 3, c2);
		
		drawPoint(xOffset + x + 1, yOffset + y + 2, c1);
		drawPoint(xOffset + x + 2, yOffset + y + 1, c1);
		
		drawPoint(xOffset + x + 1, yOffset + y + WIDTH - 3, c3);
		drawPoint(xOffset + x + 2, yOffset + y + WIDTH - 2, c3);
		
		drawPoint(xOffset + x + WIDTH - 2, yOffset + y + 2, c3);
		drawPoint(xOffset + x + WIDTH - 3, yOffset + y + 1, c3);
		
		drawPoint(xOffset + x + WIDTH - 2, yOffset + y + WIDTH - 3, c2);
		drawPoint(xOffset + x + WIDTH - 3, yOffset + y + WIDTH - 2, c2);
		
		if (checked)
			drawString("o", xOffset + x + 3, yOffset + y + 1);
		
		drawString(label, xOffset + x + WIDTH + 3, yOffset + y + 1);
	}

	@Override
	public boolean mouseOver(int x, int y) {
		return true;
	}

	@Override
	public boolean mouseClick(int x, int y, int key) {
		if(!enabled)
			return false;
		if(key!=-1){
			check();
			return true;
		}
		return false;
	}

	@Override
	public void mouseMove(int x, int y) {
		
	}

	@Override
	public void keyTyped(char c, int key) {
		
	}

}
