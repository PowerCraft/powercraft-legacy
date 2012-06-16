package net.minecraft.src;

public class PC_GresButton extends PC_GresWidget {
	
	private boolean isClicked = false;
	
	public PC_GresButton(String label){
		super(label);
		canAddWidget = false;
		minSize.setTo(40,0);
	}
	
	@Override
	public PC_CoordI calcSize() {
		FontRenderer fontRenderer = getFontRenderer();
		
		size.setTo(fontRenderer.getStringWidth(text),fontRenderer.FONT_HEIGHT).add(12,12);
		
		if(size.x < minSize.x) size.x = minSize.x;
		
		return size.copy();
	}

	@Override
	public void calcChildPositions() {

	}

	@Override
	protected void render(PC_CoordI offsetPos) {
		
		int state;
		if(!enabled){
			state = 0; //disabled
		}else if(isClicked){
			state = 3; // enabled and clicked
		}else if(isMouseOver){
			state = 2; // enabled and hover
		}else{
			state = 1; // enabled and not hover
		}
		
		int txC = 0xe0e0e0;
		
		if(state == 0) txC = 0xa0a0a0; // dark
		if(state == 1) txC = 0xe0e0e0; // light
		if (state > 1) txC = 0xffffa0; // yellow

		renderTextureSliced(offsetPos, mod_PCcore.getImgDir()+"button.png", size, new PC_CoordI(0, state*50), new PC_CoordI(256, 50));
		
		if(fontRenderer!=null)
			drawCenteredString(fontRenderer, text, offsetPos.x + pos.x + size.x / 2, offsetPos.y + pos.y + (size.y - 8) / 2, txC);
		else
			drawCenteredString(PC_Utils.mc().fontRenderer, text, offsetPos.x + pos.x + size.x / 2, offsetPos.y + pos.y + (size.y - 8) / 2, txC);
	}

	@Override
	public boolean mouseOver(PC_CoordI mpos) {
		isMouseOver = true;
		return true;
	}
	
	
	@Override
	public boolean mouseClick(PC_CoordI mpos, int key) {
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
	public boolean keyTyped(char c, int key) {
		return false;
	}

	@Override
	public void mouseMove(PC_CoordI mpos) {
		if(mpos.x<0 || mpos.x>=size.x || mpos.y<0 || mpos.y>=size.y || mouseOver(mpos)==false){
			isClicked = false;
		}
	}

	@Override
	public PC_CoordI getMinSize() {
		return calcSize();
	}

}
