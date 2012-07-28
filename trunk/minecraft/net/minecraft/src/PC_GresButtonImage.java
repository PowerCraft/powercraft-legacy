package net.minecraft.src;


import org.lwjgl.opengl.GL11;


/**
 * Resizable GUI button with image
 * 
 * @author XOR19, Rapus95, MightyPork
 * @copy (c) 2012
 */
public class PC_GresButtonImage extends PC_GresButton {

	private String texture;
	private PC_CoordI textureLeftTop, imageSize;


	/**
	 * @param imageFile
	 * @param leftTop
	 * @param imageSize
	 */
	public PC_GresButtonImage(String imageFile, PC_CoordI leftTop, PC_CoordI imageSize) {
		super("");
		canAddWidget = false;
		minSize.setTo(imageSize);
		this.texture = imageFile;
		buttonScale = new PC_CoordI(4, 4);
		this.textureLeftTop = leftTop;
		this.imageSize = imageSize;
	}

	@Override
	public PC_CoordI calcSize() {
		if(!visible) return zerosize;

		if (buttonScale == null) buttonScale = new PC_CoordI(4, 4);
		if (size == null) size = new PC_CoordI();
		if (imageSize == null) imageSize = new PC_CoordI();

		size.setTo(imageSize).add(buttonScale).add(buttonScale);

		if (size.x < minSize.x) {
			size.x = minSize.x;
		}
		if (size.y < minSize.y) {
			size.y = minSize.y;
		}

		return size.copy();
	}

	@Override
	protected void render(PC_CoordI offsetPos) {

		int state;
		if (!enabled) {
			state = 0; // disabled
		} else if (isClicked) {
			state = 3; // enabled and clicked
		} else if (isMouseOver) {
			state = 2; // enabled and hover
		} else {
			state = 1; // enabled and not hover
		}

		renderTextureSliced(offsetPos, mod_PCcore.getImgDir() + "gres/button.png", size, new PC_CoordI(0, state * 50), new PC_CoordI(256, 50));

		// and here goes the image
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, PC_Utils.mc().renderEngine.getTexture(texture));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		drawTexturedModalRect(pos.x + offsetPos.x + (size.x - imageSize.x) / 2, pos.y + offsetPos.y + (size.y - imageSize.y) / 2, textureLeftTop.x, textureLeftTop.y, imageSize.x, imageSize.y);

		GL11.glDisable(GL11.GL_BLEND);

	}

}
