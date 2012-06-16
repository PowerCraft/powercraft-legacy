package net.minecraft.src;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

/**
 * 
 * Base class for GUI-system
 * 
 * @authors XOR19 & Rapus95
 * @copy (c) 2012
 *
 */
@SuppressWarnings("javadoc")
public abstract class PC_GresWidget extends Gui {
	
	/**
	 * 
	 * aligners horizontal
	 * 
	 * @authors XOR19 & Rapus95
	 * @copy (c) 2012
	 *
	 */
	public enum PC_GresAlignH{
		/**ALIGNER_LEFT */
		LEFT,
		/**ALIGNER_RIGHT */
		RIGHT,
		/**ALIGNER_CENTER */
		CENTER,
		/**ALIGNER_STRETCH */
		STRETCH
	}
	
	/**
	 * 
	 * aligners vertical
	 * 
	 * @authors XOR19 & Rapus95
	 * @copy (c) 2012
	 *
	 */
	public enum PC_GresAlignV{
		/**ALIGNER_TOP */
		TOP,
		/**ALIGNER_BOTTOM */
		BOTTOM,
		/**ALIGNER_CENTER */
		CENTER,
		/**ALIGNER_STRETCH */
		STRETCH
	}
	
	protected PC_GresWidget parent = null;
	protected ArrayList<PC_GresWidget> childs = new ArrayList<PC_GresWidget>();
	protected FontRenderer fontRenderer = null;
	protected int x = 0, y = 0, width = 0, height = 0, minWidth=0, minHeight=0, widgetDistance=5, cursorCounter=0;
	protected boolean canAddWidget = true, isMouseOver = false, enabled = true, hasFocus = false;
	protected String title = "";
	protected PC_GresAlignH alignerHorizontal = PC_GresAlignH.CENTER;
	protected PC_GresAlignV alignerVertical = PC_GresAlignV.CENTER;
	
	public PC_GresWidget(){
		int[] size = getMinSize();
		width = size[0];
		height = size[1];
		minWidth = size[0];
		minHeight = size[1];
	}
	
	public PC_GresWidget(String title){
		this.title = title;
		int[] size = getMinSize();
		width = size[0];
		height = size[1];
		minWidth = size[0];
		minHeight = size[1];
	}
	
	public PC_GresWidget(int width, int height){
		this.width = width;
		this.height = height;
		minWidth = width;
		minHeight = height;
	}
	
	public PC_GresWidget(int width, int height, String title){
		this.width = width;
		this.height = height;
		minWidth = width;
		minHeight = height;
		this.title = title;
	}
	
	public FontRenderer getFontRenderer(){
		return fontRenderer;
	}
	
	public PC_GresWidget setFontRenderer(FontRenderer fontRenderer){
		this.fontRenderer = fontRenderer;
		return this;
	}
	
	public PC_GresAlignH getHorizontalAligner(){
		return alignerHorizontal;
	}
	
	public PC_GresWidget setHorizontalAligner(PC_GresAlignH alignerHorizontal){
		this.alignerHorizontal = alignerHorizontal;
		return this;
	}
	
	public PC_GresAlignV getVerticalAligner(){
		return alignerVertical;
	}
	
	public PC_GresWidget setVerticalAligner(PC_GresAlignV alignerVertical){
		this.alignerVertical = alignerVertical;
		return this;
	}
	
	public boolean getFokus(){
		return hasFocus;
	}
	
	public PC_GresWidget setFokus(boolean fokus){
		hasFocus = fokus;
		return this;
	}
	
	public String getTitle(){
		return title;
	}
	
	public PC_GresWidget setTitle(String title){
		this.title = title;
		if(parent!=null)
			parent.calcChildPositions();
		return this;
	}
	
	public void updateCursorCounter(){
		cursorCounter++;
	}
	
	public abstract int[] getMinSize();
	public abstract int[] calcSize();
	
	public int[] getSize(){
		return new int[]{width, height};
	}
	
	public PC_GresWidget setSize(int width, int height, boolean calcParent){
		this.width = width;
		this.height = height;
		if(parent!=null && calcParent)
			parent.calcChildPositions();
		return this;
	}
	
	public PC_GresWidget setSize(int width, int height){
		return setSize(width, height, true);
	}
	
	public int[] getPosition(){
		return new int[]{x, y};
	}
	
	public PC_GresWidget setPosition(int x, int y){
		this.x = x;
		this.y = y;
		return this;
	}
	
	public boolean isEnable(){
		return enabled;
	}
	
	public PC_GresWidget enable(boolean enabled){
		this.enabled = enabled;
		return this;
	}
	
	public abstract void calcChildPositions();
	
	public void calcChildPositionsNormal(){
		int maxh=0, xx=0, yy=0;
		if(childs!=null)
			for(int i=0; i<childs.size(); i++){
				childs.get(i).calcChildPositions();
				int[] size = childs.get(i).calcSize();
				if(size[1]>maxh)
					maxh = size[1];
				if(size[0]>width||size[1]>height){
					if(size[0]>width)
						width = size[0];
					if(size[1]>height)
						height = size[1];
					if(parent!=null)
						parent.calcChildPositions();
					calcChildPositions();
					return;
				}
				if(xx + size[0]>width){
					xx=0;
					yy+=maxh + widgetDistance;
				}
				childs.get(i).setPosition(xx, yy);
				xx += size[0] + widgetDistance;
			}
	}
	
	public PC_GresWidget getParent(){
		return parent;
	}
	
	
	public PC_GresWidget add(PC_GresWidget newwidget){
		if(!canAddWidget)
			return null;
		newwidget.parent = this;
		newwidget.setFontRenderer(this.fontRenderer);
		childs.add(newwidget);
		calcChildPositions();
		return this; //newwidget;
	}
	
	public void updateRenderer(int xOffset, int yOffset){
		this.render(xOffset, yOffset);
		if(childs != null)
			for(int i=0; i<childs.size(); i++)
				childs.get(i).updateRenderer(xOffset + x, yOffset + y);
	}
	
	protected abstract void render(int xOffset, int yOffset);
	
	public PC_GresWidget getWidgetUnderMouse(int x, int y){
		PC_GresWidget widget;
		x -= this.x;
		y -= this.y;
		if(x<0 || x>=width || y<0 || y>=height || mouseOver(x, y)==false){
			this.isMouseOver = false;
			if(childs != null)
				for(int i=0; i<childs.size(); i++){
					childs.get(i).getWidgetUnderMouse(-1, -1);
				}
			return null;
		}
		this.isMouseOver = true;
		if(childs != null)
			for(int i=0; i<childs.size(); i++){
				widget = childs.get(i).getWidgetUnderMouse(x, y);
				if(widget != null){
					for(i++; i<childs.size(); i++){
						childs.get(i).getWidgetUnderMouse(-1, -1);
					}
					return widget;
				}
			}
		if(mouseOver(x, y)==false){
			this.isMouseOver = false;
			return null;
		}
		return this;
	}
	
	public int[] getPositionOnScreen(){
		int[] pos;
		if(parent!=null){
			pos = parent.getPositionOnScreen();
			pos[0] += x;
			pos[1] += y;
		}else
			pos = new int[]{x, y};
		return pos;
	}
	
	/**
	 * Render texture using 9patch-like scaling method.<br>
	 * <pre>
	 * r e c t W
	 * +------+--+ r
	 * |      |  | e
	 * |      |  | c
	 * +------+--+ t
	 * |      |  | H
	 * +------+--+
	 * </pre>
	 * 
	 * @param xOffset offset X relative to parent top left (x)
	 * @param yOffset offset Y relative to parent top left (y)
	 * @param texture texture to render (filename)
	 * @param rectW rectangle width
	 * @param rectH rectangle height
	 * @param imgOffsetX offset X within the texture image
	 * @param imgOffsetY offset Y within the texture image
	 * @param imgW width of the whole "scalable" region in texture file (eg. the whole huge "button" field)
	 * @param imgH height of the whole "scalable" region in texture file (eg. the whole huge "button" field)
	 * @param borderRight width of the right-hand-side column appended after the left part 
	 * @param borderBottom height of the bottom row appended below the upper part
	 */
	protected void renderTextureSliced(int xOffset, int yOffset, String texture, int rectW, int rectH, int imgOffsetX, int imgOffsetY, int imgW,
			int imgH, int borderRight, int borderBottom) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, PC_Utils.mc().renderEngine.getTexture(texture));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		// left top huge
		drawTexturedModalRect(
				x + xOffset,
				y + yOffset,
				imgOffsetX,
				imgOffsetY,
				rectW - borderRight,
				rectH - borderBottom);
		
		// right top thin
		drawTexturedModalRect(
				x + xOffset + rectW - borderRight,
				y + yOffset,
				imgOffsetX + imgW - borderRight,
				imgOffsetY,
				borderRight,
				rectH - borderBottom);
		
		//left bottom wide
		drawTexturedModalRect(
				x + xOffset,
				y + yOffset + rectH - borderBottom,
				imgOffsetX,
				imgOffsetY + imgH - borderBottom,
				rectW - borderRight,
				borderBottom);
		
		//right bottom square
		drawTexturedModalRect(
				x + xOffset + rectW - borderRight,
				y + yOffset + rectH - borderBottom,
				imgOffsetX + imgW - borderRight,
				imgOffsetY + imgH - borderBottom,
				borderRight,
				borderBottom);
	}
	
	public abstract boolean mouseOver(int x, int y);
	
	public abstract boolean mouseClick(int x, int y, int key);
	
	public abstract void mouseMove(int x, int y);
	
	public abstract void keyTyped(char c, int key);
	
	protected void drawPoint(int x, int y, int c){
		drawHorizontalLine(x, x, y, c);
	}
	
}
