package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/**
 * 
 * Window for GUI
 * 
 * @authors XOR19 & Rapus95
 * @copy (c) 2012
 *
 */

public class PC_GresWindow extends PC_GresWidget {

	/**
	 * distance to the window frame
	 */
	static final int distanceToFrame = 10;
	/**
	 * Image for the left upper corner
	 */
	String overlayFile;
	
	/**
	 * 
	 * @param minX minimal X size
	 * @param minY minimal Y size
	 * @param title title of the window
	 * @param overlayFile image for the left upper corner
	 */
	public PC_GresWindow(int minX, int minY, String title, String overlayFile){
		super(minX, minY, title);
		this.overlayFile = overlayFile;
	}
	
	@Override
	public int[] calcSize() {
		int textWidth = PC_Utils.mc().fontRenderer.getStringWidth(title);
		if(width<textWidth+distanceToFrame*2 + 12)
			width = textWidth+distanceToFrame*2 + 12;
		calcChildPositions();
		return new int[]{width, height};
	}

	public void render(int xOffset, int yOffset) {
		
		renderTextureSliced(xOffset, yOffset, mod_PCcore.getImgDir()+"dialog.png", width, height, 0, 0, 256, 256, 22, 22);
		
		if(fontRenderer!=null)
			fontRenderer.drawString(title, xOffset + x + (width)/2 - fontRenderer.getStringWidth(title)/2, yOffset + y + 6, 0xff000000);
		else
			PC_Utils.mc().fontRenderer.drawString(title, xOffset + x + (width)/2 - PC_Utils.mc().fontRenderer.getStringWidth(title)/2, yOffset + y + 6, 0xff000000);
		
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
	public void calcChildPositions() {
		int yy = 0, minySize=0, minmaxxSize=0, maxxSize=0, ySize=0, yPlus = PC_Utils.mc().fontRenderer.FONT_HEIGHT + 15;
		int childNum = childs.size();
		for(int i=0; i<childNum; i++){
			childs.get(i).calcChildPositions();
			int[] size = childs.get(i).calcSize();
			int[] minSize = childs.get(i).getMinSize();
			ySize += size[1] + widgetDistance;
			minySize += minSize[1] + widgetDistance;
			if(maxxSize<size[0])
				maxxSize = size[0];
			if(minmaxxSize<minSize[0])
				minmaxxSize = minSize[0];
		}
		
		if(alignerVertical == PC_GresAlignV.STRETCH){
			maxxSize = minmaxxSize;
			ySize = minySize;
			for(int i=0; i<childNum; i++){
				int[] minSize = childs.get(i).getMinSize();
				childs.get(i).setSize(minSize[0], minSize[1], false);
			}
		}
		if(maxxSize + distanceToFrame*2>width||ySize + yPlus + distanceToFrame>height){
			if(maxxSize + distanceToFrame*2>width)
				width = maxxSize+distanceToFrame*2;
			if(ySize + yPlus + distanceToFrame>height)
				height = ySize + yPlus + distanceToFrame;
			if(parent!=null)
				parent.calcChildPositions();
			calcChildPositions();
			return;
		}
		
		ySize -= widgetDistance;
		
		for(int i=0; i<childNum; i++){
			int[] size = childs.get(i).getSize();
			int xPos=0;
			int yPos=0;
			int s=0;
			switch(alignerHorizontal){
				case LEFT:
					xPos = distanceToFrame;
					break;
				case RIGHT:
					xPos = width - childs.get(i).getSize()[0] - distanceToFrame;
					break;
				case CENTER:
					xPos = width/2 - childs.get(i).getSize()[0]/2;
					break;
				case STRETCH:
					xPos = distanceToFrame;
					childs.get(i).setSize(width - distanceToFrame*2, childs.get(i).getSize()[1], false);
					break;
			}
			switch(alignerVertical){
				case TOP:
					yPos = yPlus + yy;
					break;
				case BOTTOM:
					yPos = height - distanceToFrame - ySize + yy;
					break;
				case CENTER:
					yPos = (height+yPlus-distanceToFrame)/2 - ySize/2 + yy;
					break;
				case STRETCH:
					s = (height - yPlus - distanceToFrame - ySize + widgetDistance - widgetDistance*childNum )/childNum;
					childs.get(i).setSize(childs.get(i).getSize()[0], childs.get(i).getSize()[1] + s, false);
					yPos = yPlus + yy;
					break;
			}
			childs.get(i).setPosition(xPos, yPos);
			yy += size[1] + widgetDistance + s;
		}
		
	}

	@Override
	public void mouseMove(int x, int y) {
	}

	@Override
	public int[] getMinSize() {
		return calcSize();
	}
	
}
