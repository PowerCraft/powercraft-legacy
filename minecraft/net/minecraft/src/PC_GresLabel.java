package net.minecraft.src;

public class PC_GresLabel extends PC_GresWidget {

	public PC_GresLabel(String label){
		super(label);
		canAddWidget = false;
	}
	
	@Override
	public PC_CoordI calcSize() {
		FontRenderer fontRenderer = getFontRenderer();
		size.setTo(fontRenderer.getStringWidth(label), fontRenderer.FONT_HEIGHT);
		return size.copy();
	}

	@Override
	protected void render(PC_CoordI offsetPos) {
		drawString(label, offsetPos.x+pos.x, offsetPos.y+pos.y);
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
	public void keyTyped(char c, int key) {

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
