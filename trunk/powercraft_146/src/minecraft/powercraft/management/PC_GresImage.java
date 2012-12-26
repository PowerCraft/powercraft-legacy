package powercraft.management;


import org.lwjgl.opengl.GL11;


/**
 * Resizable GUI image widget
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PC_GresImage extends PC_GresWidget {

	private PC_VecI imgSize = new PC_VecI(0, 0);
	private PC_VecI imgOffset = new PC_VecI(0, 0);
	private String texture;

	/**
	 * Image from a texture file.
	 * 
	 * @param texture Name of a texture file inside minecraft.jar. Must be
	 *            256x256!
	 * @param offsetX start of texture x
	 * @param offsetY start of texture y
	 * @param imgW image width
	 * @param imgH image height
	 */
	public PC_GresImage(String texture, int offsetX, int offsetY, int imgW, int imgH) {
		super("");
		imgSize.setTo(imgW, imgH, 0);
		size.setTo(imgSize);
		imgOffset.setTo(offsetX, offsetY, 0);
		canAddWidget = false;
		this.texture = texture;
	}

	@Override
	public PC_VecI calcSize() {
		return size.copy();
	}

	@Override
	public void calcChildPositions() {

	}

	@Override
	protected PC_RectI render(PC_VecI offsetPos, PC_RectI scissorOld, double scale) {

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(texture));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		drawTexturedModalRect(pos.x + offsetPos.x, pos.y + offsetPos.y, imgOffset.x, imgOffset.y, imgSize.x, imgSize.y);

		GL11.glDisable(GL11.GL_BLEND);

		return null;
	}

	@Override
	public MouseOver mouseOver(PC_VecI mpos) {
		return MouseOver.THIS;
	}



	@Override
	public boolean mouseClick(PC_VecI mpos, int key) {
		return false;
	}

	@Override
	public boolean keyTyped(char c, int key) {
		return false;
	}

	@Override
	public void mouseMove(PC_VecI mpos) {}

	@Override
	public PC_VecI getMinSize() {
		return calcSize();
	}

	@Override
	public void mouseWheel(int i) {}

	@Override
	public void addedToWidget() {}
}
