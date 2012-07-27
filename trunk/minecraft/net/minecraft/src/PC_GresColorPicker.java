package net.minecraft.src;


import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import weasel.obj.WeaselObject;


public class PC_GresColorPicker extends PC_GresWidget {

	private int[][] colorArray = new int[40][20];
	private int px = 1;
	private int color = 0x000000;
	private int lx=-1,ly=-1;

	public PC_GresColorPicker(int color, int width, int height) {
		super("");
		canAddWidget = false;
		colorArray = new int[width][height];
		size = calcSize();

		if (parent != null) parent.calcChildPositions();

		float[] hsv = {0,1,1};
		
		int he = colorArray[0].length;
		int wi = colorArray.length;
		
		int col = 0;
		for(hsv[0]=0; hsv[0] <=1; hsv[0]+=1F/wi,col++) {
			if(col >= colorArray.length) col = colorArray.length-1;
			int i=0;
			for (int row = 0; row<=he/2; row++) {	
				float mp = (1F / (colorArray[0].length/2)) * i++;
				Color cc = new Color();
				cc.fromHSB(hsv[0], mp, hsv[2]);
				colorArray[col][row] = clr(cc.getRed(),cc.getGreen(),cc.getBlue());
			}
			i=0;
			for (int row = he/2+1; row<he; row++) {	
				float mp = 1F-(1F / (colorArray[0].length/2)) * i++;
				Color cc = new Color();
				cc.fromHSB(hsv[0], hsv[1], mp);
				colorArray[col][row] = clr(cc.getRed(),cc.getGreen(),cc.getBlue());
			}
		}
		
		this.setColor(color);
	}
	
	public void setColor(int color) {
		lx=-1; ly=-1;
		for (int x = 0; x < colorArray.length; x++) {
			for (int y = 0; y < colorArray[0].length; y++) {
				if(color == colorArray[x][y]) {
					lx = x; ly = y;
					break;
				}
				
			}
		}
		this.color = color;
	}
	
	public int getColor() {
		return color;
	}

	private int clr(float r, float g, float b) {
		return 
					Math.round(Math.min(255,Math.max(0,r))) << 16 |
					Math.round(Math.min(255,Math.max(0,g))) << 8 |
					Math.round(Math.min(255,Math.max(0,b)));
	}
	
	@Override
	public PC_CoordI calcSize() {
		if(!visible) return zerosize;
		if (colorArray == null)
			return zerosize;
		else
			return new PC_CoordI(Math.round(colorArray.length * px), Math.round(colorArray[0].length * px));
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
		pixelW = 1.0D;
		pixelH = 1.0D;
		if (colorArray != null) {
			for (int x = 0; x < colorArray.length; x++) {
				for (int y = 0; y < colorArray[0].length; y++) {
					color = colorArray[x][y];
					if (color != -1) {
						
						if(System.currentTimeMillis()%1000<500 && x==lx && y==ly) {
							color = ~color;
						}
						
						
						posX = x * px + pos.x + posOffset.x;
						posY = y * px + pos.y + posOffset.y;
						tessellator.setColorRGBA((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, 255);
						tessellator.addVertex(posX, posY, 0.0D);
						tessellator.addVertex(posX + pixelW * px, posY, 0.0D);
						tessellator.addVertex(posX + pixelW * px, posY + pixelH * px, 0.0D);
						tessellator.addVertex(posX, posY + pixelH * px, 0.0D);
						tessellator.addVertex(posX + pixelW * px, posY, 0.0D);
						tessellator.addVertex(posX, posY, 0.0D);
						tessellator.addVertex(posX, posY + pixelH * px, 0.0D);
						tessellator.addVertex(posX + pixelW * px, posY + pixelH * px, 0.0D);
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
		if (mousePos.x >= size.x) return false;
		if (mousePos.y >= size.y) return false;
		if (mousePos.y < 0) return false;
		if (mousePos.x < 0) return false;
		lx = mousePos.x / px;
		ly = mousePos.y / px;
		color = colorArray[lx][ly];
		return true;
	}

	@Override
	public void mouseMove(PC_CoordI mousePos) {
		if (dragging) {
			mouseClick(mousePos, 0);
			((PC_GresGui) this.getContainerManager().gresGui).gui.actionPerformed(this, this.getContainerManager().gresGui);
		}
	}

	@Override
	public void addedToWidget() {}

	@Override
	public void mouseWheel(int i) {}

	@Override
	public boolean keyTyped(char c, int key) {
		return false;
	}

}
