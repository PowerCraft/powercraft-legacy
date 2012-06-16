package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class PC_GresImage extends PC_GresWidget {
	
	private int imgWidth = 16, imgHeight = 16;
	private int imgOffsetX = 0, imgOffsetY = 0;
	private String texture;
	
	/**
	 * Image from a texture file.
	 * @param texture Name of a texture file inside minecraft.jar. Must be 256x256!
	 * @param xOffset start of texture x
	 * @param yOffset start of texture y
	 * @param w image width
	 * @param h image height
	 */
	public PC_GresImage(String texture, int xOffset, int yOffset, int w, int h){
		super("");
		imgWidth = w; imgHeight = h;
		imgOffsetX = xOffset; imgOffsetY = yOffset;
		canAddWidget = false;
		this.texture = texture;
	}
	
	@Override
	public int[] calcSize() {
		width = imgWidth;
		height = imgHeight;
		return new int[]{width, height};
	}

	@Override
	public void calcChildPositions() {

	}

	@Override
	protected void render(int xOffset, int yOffset) {
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, PC_Utils.mc().renderEngine.getTexture(texture));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		drawTexturedModalRect(x+xOffset, y+yOffset, imgOffsetX, imgOffsetY, imgWidth, imgHeight);
		
	}

	@Override
	public boolean mouseOver(int x, int y) {
		return true;
	}

	
	
	@Override
	public boolean mouseClick(int x, int y, int key) {
		return false;
	}

	@Override
	public void keyTyped(char c, int key) {
		
	}

	@Override
	public void mouseMove(int x, int y) {
	}

	@Override
	public int[] getMinSize() {
		return calcSize();
	}

}
