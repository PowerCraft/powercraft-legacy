package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/**
 * Resizable GUI image widget
 * 
 * @author MightyPork
 * @copy (c) 2012
 *
 */
public class PC_GresImage extends PC_GresWidget {
	
	private PC_CoordI imgSize = new PC_CoordI(0,0);
	private PC_CoordI imgOffset = new PC_CoordI(0,0);
	private String texture;
	
	/**
	 * Image from a texture file.
	 * @param texture Name of a texture file inside minecraft.jar. Must be 256x256!
	 * @param offsetX start of texture x
	 * @param offsetY start of texture y
	 * @param imgW image width
	 * @param imgH image height
	 */
	public PC_GresImage(String texture, int offsetX, int offsetY, int imgW, int imgH){
		super("");
		imgSize.setTo(imgW,imgH);
		size.setTo(imgSize);
		imgOffset.setTo(offsetX,offsetY);
		canAddWidget = false;
		this.texture = texture;
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
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		drawTexturedModalRect(pos.x+offsetPos.x, pos.y+offsetPos.y, imgOffset.x, imgOffset.y, imgSize.x, imgSize.y);
		
		GL11.glDisable(GL11.GL_BLEND);
		
		
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
	public void mouseMove(PC_CoordI mpos) {
	}

	@Override
	public PC_CoordI getMinSize() {
		return calcSize();
	}

}
