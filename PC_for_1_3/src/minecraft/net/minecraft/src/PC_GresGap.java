package net.minecraft.src;


/**
 * GUI gap with fixed size
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PC_GresGap extends PC_GresWidget {

	/**
	 * gap
	 * 
	 * @param width min width
	 * @param height min height
	 */
	public PC_GresGap(int width, int height) {
		super(width, height);
		canAddWidget = false;
	}

	@Override
	public PC_CoordI calcSize() {
		if (!visible) return zerosize;
		return minSize.copy();
	}

	@Override
	protected void render(PC_CoordI offsetPos) {}

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

	@Override
	public void mouseWheel(int i) {}

	@Override
	public void addedToWidget() {}
}
