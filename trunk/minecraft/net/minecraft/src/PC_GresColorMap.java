package net.minecraft.src;

import org.lwjgl.opengl.GL11;

import weasel.obj.WeaselObject;

public class PC_GresColorMap extends PC_GresWidget {

	private int colorArray[][] = null;
	private char lastKey;
	private PC_CoordI lastMousePos;
	private int lastMouseKey;
	private String lastEvent;
	private int px = 3;
	
	/**
	 * Set map scale - size of 1 pixel. default is 3.
	 * @param scale
	 * @return
	 */
	public PC_GresColorMap setScale(int scale) {
		px = scale;
		size = calcSize();
		return this;
	}
	
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
		PC_CoordI co = lastMousePos.copy();
		return new PC_CoordI(co.x/px,co.y/px);
	}
	
	public int getLastMouseKey(){
		return lastMouseKey;
	}
	
	public String getLastEvent(){
		return lastEvent;
	}
	
	@Override
	public PC_CoordI calcSize() {
		if(colorArray==null)
			return new PC_CoordI(0,0);
		else
			return new PC_CoordI(Math.round(colorArray.length*px+2*px),Math.round(colorArray[0].length*px+2*px));
	}

	@Override
	public void calcChildPositions() {}

	private boolean dragging = false;
	
	@Override
	protected void render(PC_CoordI posOffset) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
        double posX, posY, pixelW, pixelH;
        int color;
        boolean border=false;
        float bdrdist = 0.4F;
        pixelW = 1.0D;
        pixelH = 1.0D;
        if(colorArray!=null){
	        for(int x=-1; x<colorArray.length+1; x++){
	        	for(int y=-1; y<colorArray[0].length+1; y++){
	        		border=false;
	        		if(x==-1||y==-1||x==colorArray.length||y==colorArray[x].length) {
	        			color = 0x000000;
	        			border=true;
	        		}else {
	        			color = colorArray[x][y];}
	        		if(color != -1){
	            		posX = x*px + pos.x + posOffset.x+(border?bdrdist:0);
	            		posY = (y+1)*px + pos.y + posOffset.y+(border?bdrdist:0);
	            		tessellator.setColorRGBA((color >> 16) & 0xFF,  (color >> 8) & 0xFF, color & 0xFF, 255);
			            tessellator.addVertex(posX, posY, 0.0D);
			            tessellator.addVertex(posX + pixelW*px-(border?bdrdist*2:0), posY, 0.0D);
			            tessellator.addVertex(posX + pixelW*px-(border?bdrdist*2:0), posY + pixelH*px-(border?bdrdist*2:0), 0.0D);
			            tessellator.addVertex(posX, posY + pixelH*px-(border?bdrdist*2:0), 0.0D);
			            tessellator.addVertex(posX + pixelW*px-(border?bdrdist*2:0), posY, 0.0D);
			            tessellator.addVertex(posX, posY, 0.0D);
			            tessellator.addVertex(posX, posY + pixelH*px-(border?bdrdist*2:0), 0.0D);
			            tessellator.addVertex(posX + pixelW*px-(border?bdrdist*2:0), posY + pixelH*px-(border?bdrdist*2:0), 0.0D);
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
		dragging = (key != -1);
		mousePos.y-=px;	
		if(mousePos.x>=size.x-2*px) return false;
		if(mousePos.y>=size.y-2*px) return false;
		lastMousePos = mousePos.copy();
		lastMouseKey = key;
		lastEvent = (key != -1)?"down":"up";
		return true;
	}

	@Override
	public void mouseMove(PC_CoordI mousePos) {
		if (dragging) {
			mouseClick(mousePos, lastMouseKey);
			lastEvent = "move";
			((PC_GresGui) this.getContainerManager().gresGui).gui.actionPerformed(this, this.getContainerManager().gresGui);
		}
	}

	@Override
	public void mouseWheel(int i) {
	}

	@Override
	public boolean keyTyped(char c, int key) {
		lastKey = c;
		lastEvent = "key";
		return true;
	}

	@Override
	public void addedToWidget() {}

}
