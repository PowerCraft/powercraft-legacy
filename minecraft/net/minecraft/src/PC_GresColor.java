package net.minecraft.src;


import org.lwjgl.opengl.GL11;


/**
 * Resizable GUI image widget
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PC_GresColor extends PC_GresWidget {

	private PC_CoordI imgSize = new PC_CoordI(11, 11);
	private PC_CoordI imgOffset = new PC_CoordI(57, 0);
	private String texture = mod_PCcore.getImgDir() + "gres/widgets.png";
	private PC_Color bulbColor;

	/**
	 * Image from a texture file.
	 * 
	 * @param color hex color
	 */
	public PC_GresColor(PC_Color color) {
		super("");
		size.setTo(imgSize);
		canAddWidget = false;
		this.bulbColor = color;
	}

	@Override
	public PC_CoordI calcSize() {
		return size.copy();
	}

	@Override
	public void calcChildPositions() {

	}

	@Override
	protected void render(PC_CoordI offsetPos) {

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, PC_Utils.mc().renderEngine.getTexture(texture));
		GL11.glColor4d(bulbColor.r, bulbColor.g, bulbColor.b, 1.0D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		drawTexturedModalRect(pos.x + offsetPos.x, pos.y + offsetPos.y, imgOffset.x, imgOffset.y, imgSize.x, imgSize.y);

		GL11.glDisable(GL11.GL_BLEND);


	}

	/**
	 * Set color displayed
	 * 
	 * @param color
	 */
	public void setColor(PC_Color color) {
		this.bulbColor = color;
	}

	@Override
	public boolean mouseOver(PC_CoordI mpos) {
		return true;
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
	public void mouseMove(PC_CoordI mpos) {}

	@Override
	public PC_CoordI getMinSize() {
		return calcSize();
	}

	@Override
	public void mouseWheel(int i) {}

	@Override
	public void addedToWidget() {}

	/**
	 * @return color currently shown
	 */
	public PC_Color getColor() {
		return bulbColor;
	}
}
