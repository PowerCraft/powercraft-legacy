package net.minecraft.src;

/**
 * Resizable GUI hypertext link-like widget
 * 
 * @author MightyPork
 * @copy (c) 2012
 *
 */
public class PC_GresLink extends PC_GresWidget {
	
	private boolean isClicked = false;
	
	/**
	 * Link button
	 * @param label label
	 */
	public PC_GresLink(String label){
		super(label);
		canAddWidget = false;
		minSize.setTo(10,0);
	}
	
	@Override
	public PC_CoordI calcSize() {
		
		size.setTo(getStringWidth(text),getLineHeight()).add(2,0);
		
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
		
		int textColor = 0x000000;
		
		if(state == 0) textColor = 0xa0a0a0; // gray
		if(state == 1) textColor = 0x000000; // black
		if(state == 2) textColor = 0x0000ff; // blue, hover
		if(state == 3) textColor = 0xff0000; // red, activated
		
		drawStringColor(text, offsetPos.x+pos.x, offsetPos.y+pos.y, textColor);
		
		int yy = offsetPos.y+pos.y+getFontRenderer().FONT_HEIGHT;
		
		drawRect(offsetPos.x+pos.x, yy, offsetPos.x+size.x+pos.x+1, yy+1, textColor|0xff000000);
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

	@Override
	public void mouseWheel(int i) {
	}
	
}
