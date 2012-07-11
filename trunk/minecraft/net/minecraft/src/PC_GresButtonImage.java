package net.minecraft.src;

import org.lwjgl.opengl.GL11;


/**
 * Resizable GUI button with image
 * 
 * @author XOR19, Rapus95, MightyPork
 * @copy (c) 2012
 */
public class PC_GresButtonImage extends PC_GresButton {

	private PC_CoordI buttonScale;

	private boolean isClicked = false;
	private String texture;
	private PC_CoordI textureLeftTop, imageSize;

	/**
	 * @param label button label
	 */
	public PC_GresButtonImage(String label, String imageFile, PC_CoordI leftTop, PC_CoordI imageSize) {
		super(label);
		canAddWidget = false;
		minSize.setTo(imageSize);
		buttonScale = new PC_CoordI(4, 4);
		this.textureLeftTop = leftTop;
		this.imageSize = imageSize;
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

		drawTexturedModalRect(pos.x + offsetPos.x + (size.x - imageSize.x)/2, pos.y + offsetPos.y + (size.y - imageSize.y)/2, textureLeftTop.x, textureLeftTop.y, imageSize.x, imageSize.y);

		GL11.glDisable(GL11.GL_BLEND);
		
	}

}
