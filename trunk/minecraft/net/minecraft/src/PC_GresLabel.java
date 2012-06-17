package net.minecraft.src;

/**
 * Resizable GUI plain text label
 * 
 * @author XOR19, Rapus95, MightyPork
 * @copy (c) 2012
 *
 */
public class PC_GresLabel extends PC_GresWidget {

	/**
	 * Text label
	 * @param label text
	 */
	public PC_GresLabel(String label){
		super(label);
		canAddWidget = false;
	}
	
	@Override
	public PC_CoordI calcSize() {
		size.setTo(getStringWidth(text), getLineHeight());
		if(size.x < minSize.x) size.x = minSize.x;
		return size.copy();
	}

	@Override
	protected void render(PC_CoordI offsetPos) {
		drawString(text, offsetPos.x+pos.x, offsetPos.y+pos.y);
	}

	@Override
	public boolean mouseOver(PC_CoordI mpos) {
		return false;
	}

	@Override
	public boolean mouseClick(PC_CoordI mpos, int key) {
		return false;
	}

	@Override
	public boolean keyTyped(char c, int key) {
		return false;
	}

	@Override
	public void calcChildPositions() {
		
	}

	@Override
	public void mouseMove(PC_CoordI mpos) {

	}

	@Override
	public PC_CoordI getMinSize() {
		return calcSize();
	}

}
