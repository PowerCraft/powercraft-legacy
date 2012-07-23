package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class PC_GresColorMap extends PC_GresWidget {

	private int colorArray[][] = null;
	private char lastKey;
	private PC_CoordI lastMousePos;
	private int lastMouseKey;
	private int lastEvent;
	
	public PC_GresColorMap(int colorArray[][]){
		super("");
		this.colorArray = colorArray;
		canAddWidget = false;
		size = calcSize();
		if(parent!=null)
			parent.calcChildPositions();
	}
	
	public void setColorArray(int colorArray[][]){
		this.colorArray = colorArray;
		size = calcSize();
		if(parent!=null)
			parent.calcChildPositions();
	}
	
	public char getLastKey(){
		return lastKey;
	}
	
	public PC_CoordI getLastMousePos(){
		return lastMousePos.copy();
	}
	
	public int getLastMouseKey(){
		return lastMouseKey;
	}
	
	public int getLastEvent(){
		return lastEvent;
	}
	
	@Override
	public PC_CoordI calcSize() {
		if(colorArray==null)
			return new PC_CoordI(0,0);
		else
			return new PC_CoordI(colorArray.length+2,colorArray[0].length+2);
	}

	@Override
	public void calcChildPositions() {}

	@Override
	protected void render(PC_CoordI posOffset) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
        double posX, posY, pixelW, pixelH;
        int color;
        pixelW = 1.0D;
        pixelH = 1.0D;
        if(colorArray!=null){
	        for(int i=-1; i<colorArray.length+1; i++){
	        	for(int j=-1; j<colorArray[0].length+1; j++){
	        		if(i==-1||j==-1||i==colorArray.length||j==colorArray[i].length)
	        			color = 0xff000000;
	        		else
	        			color = colorArray[i][j];
	        		if(((color>>24) & 0xFF) != 0){
	            		posX = i + pos.x + posOffset.x;
	            		posY = j + pos.y + posOffset.y;
	            		tessellator.setColorRGBA((color >> 16) & 0xFF,  (color >> 8) & 0xFF, color & 0xFF, 255);
			            tessellator.addVertex(posX, posY, 0.0D);
			            tessellator.addVertex(posX + pixelW, posY, 0.0D);
			            tessellator.addVertex(posX + pixelW, posY + pixelH, 0.0D);
			            tessellator.addVertex(posX, posY + pixelH, 0.0D);
			            tessellator.addVertex(posX + pixelW, posY, 0.0D);
			            tessellator.addVertex(posX, posY, 0.0D);
			            tessellator.addVertex(posX, posY + pixelH, 0.0D);
			            tessellator.addVertex(posX + pixelW, posY + pixelH, 0.0D);
	        		}
	        	}
	        }
        }
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	@Override
	public boolean mouseOver(PC_CoordI mousePos) {
		return true;
	}

	@Override
	public boolean mouseClick(PC_CoordI mousePos, int key) {
		lastMousePos = mousePos.copy();
		lastMouseKey = key;
		lastEvent = 2;
		return true;
	}

	@Override
	public void mouseMove(PC_CoordI mousePos) {
		lastMousePos = mousePos.copy();
		lastEvent = 3;
	}

	@Override
	public void mouseWheel(int i) {
	}

	@Override
	public boolean keyTyped(char c, int key) {
		lastKey = c;
		lastEvent = 1;
		return true;
	}

	@Override
	public void addedToWidget() {}

}
