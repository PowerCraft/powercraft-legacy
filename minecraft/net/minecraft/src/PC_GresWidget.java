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
public abstract class PC_GresWidget extends Gui {
	
	/**
	 * 
	 * align horizontal
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
	 * align vertical
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
	
	@SuppressWarnings("javadoc")
	public static final int textColorEnabled = 0, textColorShadowEnabled = 1, textColorDisabled = 2, textColorShadowDisabled = 3;

	/** Array of text colors */
	protected int color[] = {0xff000000, 0x00000000, 0xff000000, 0x00000000};
	
	/** Parent widget */
	protected PC_GresWidget parent = null;
	/** List of children */
	protected ArrayList<PC_GresWidget> childs = new ArrayList<PC_GresWidget>();
	/** Font renderer */
	protected FontRenderer fontRenderer = null;
	/** X pos of left top corner */
	protected int x = 0;
	/** Y pos of left top corner */
	protected int y = 0;
	/** Widget width */
	protected int width = 0;
	/** Widget height */
	protected int height = 0;
	/** Minimal allowed widget width */
	protected int minWidth=0;
	/** Minimal allowed widget height */
	protected int minHeight=0;
	/** Distance from other widgets in group. */
	protected int widgetMargin=5;
	/** Counter used for the automatic resizing */
	protected int cursorCounter=0;
	/** Can add child widgets */
	protected boolean canAddWidget = true;
	/** Is mouse over this widget? */
	protected boolean isMouseOver = false;
	/** Is widget enabled = clickable */
	protected boolean enabled = true;
	/** Is widget focused (used mainly for text edits) */
	protected boolean hasFocus = false;
	/** Widget's label (text in title or on button or whatever) */
	protected String label = "";
	/** Horizontal Align */
	protected PC_GresAlignH alignH = PC_GresAlignH.CENTER;
	/** Vertical Align */
	protected PC_GresAlignV alignV = PC_GresAlignV.CENTER;
	
	/**
	 * A widget
	 */
	public PC_GresWidget(){
		int[] size = getMinSize();
		width = size[0];
		height = size[1];
		minWidth = size[0];
		minHeight = size[1];
		
	}
	
	/**
	 * A widget
	 * @param label widget's label / text
	 */
	public PC_GresWidget(String label){
		this.label = label;
		int[] size = getMinSize();
		width = size[0];
		height = size[1];
		minWidth = size[0];
		minHeight = size[1];
	}
	
	/**
	 * A widget
	 * @param width widget minWidth
	 * @param height widget minHeight
	 */
	public PC_GresWidget(int width, int height){
		this.width = width;
		this.height = height;
		minWidth = width;
		minHeight = height;
	}
	
	/**
	 * A widget
	 * @param width widget minWidth
	 * @param height widget minHeight
	 * @param label widget label / text
	 */
	public PC_GresWidget(int width, int height, String label){
		this(width,height);
		this.label = label;
	}
	
	/**
	 * @return widget's font renderer
	 */
	public FontRenderer getFontRenderer(){
		return fontRenderer;
	}
	
	/**
	 * Set widget's font renderer
	 * @param fontRenderer the font renderer
	 * @return this
	 */
	public PC_GresWidget setFontRenderer(FontRenderer fontRenderer){
		this.fontRenderer = fontRenderer;
		return this;
	}
	
	/**
	 * Get horizontal align
	 * @return horizontal align
	 */
	public PC_GresAlignH getAlignH(){
		return alignH;
	}
	
	/**
	 * Set horizontal align
	 * @param alignHorizontal horizontal align
	 * @return this
	 */
	public PC_GresWidget setAlignH(PC_GresAlignH alignHorizontal){
		this.alignH = alignHorizontal;
		return this;
	}
	
	/**
	 * Get vertical align
	 * @return vertical align
	 */
	public PC_GresAlignV getAlignV(){
		return alignV;
	}
	
	/**
	 * Set vertical align
	 * @param alignVertical vertical align
	 * @return this
	 */
	public PC_GresWidget setVerticalAligner(PC_GresAlignV alignVertical){
		this.alignV = alignVertical;
		return this;
	}
	
	/**
	 * @return has focus
	 */
	public boolean getFocus(){
		return hasFocus;
	}
	
	/**
	 * Set focus state
	 * @param focus focused
	 * @return this
	 */
	public PC_GresWidget setFocus(boolean focus){
		hasFocus = focus;
		return this;
	}
	
	/**
	 * @return widget's label
	 */
	public String getLabel(){
		return label;
	}
	
	/**
	 * Set widget's label, resize if needed
	 * @param label new label
	 * @return this
	 */
	public PC_GresWidget setLabel(String label){
		this.label = label;
		if(parent!=null)
			parent.calcChildPositions();
		return this;
	}
	
	/**
	 * Increment cursor counter, used for text field animations
	 */
	public void updateCursorCounter(){
		cursorCounter++;
	}
	
	/**
	 * @return minimal size, {width,height}
	 */
	public abstract int[] getMinSize();
	/**
	 * @return newly calculated size, {width, height}
	 */
	public abstract int[] calcSize();
	
	/**
	 * Get widget size
	 * @return {width, height}
	 */
	public int[] getSize(){
		return new int[]{width, height};
	}
	
	/**
	 * Set widget size
	 * @param width width
	 * @param height height
	 * @param calcParent flag whether to ask parent for position recalculation
	 * @return this
	 */
	public PC_GresWidget setSize(int width, int height, boolean calcParent){
		this.width = width;
		this.height = height;
		if(parent!=null && calcParent)
			parent.calcChildPositions();
		return this;
	}
	
	/**
	 * Set size, recalculate position
	 * @param width width
	 * @param height height
	 * @return this
	 */
	public PC_GresWidget setSize(int width, int height){
		return setSize(width, height, true);
	}
	
	/**
	 * Get position
	 * @return {x, y}
	 */
	public int[] getPosition(){
		return new int[]{x, y};
	}
	
	/**
	 * Set position of the widget
	 * @param x x
	 * @param y y
	 * @return this
	 */
	public PC_GresWidget setPosition(int x, int y){
		this.x = x;
		this.y = y;
		return this;
	}
	
	/**
	 * @return is enabled?
	 */
	public boolean isEnabled(){
		return enabled;
	}
	
	/**
	 * set "enabled" flag
	 * @param enabled state
	 * @return this
	 */
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
					yy+=maxh + widgetMargin;
				}
				childs.get(i).setPosition(xx, yy);
				xx += size[0] + widgetMargin;
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
	
	public PC_GresWidget remove(PC_GresWidget removewidget){
		if(!childs.remove(removewidget))
			for(int i=0; i<childs.size(); i++)
				childs.get(i).remove(removewidget);
		calcChildPositions();
		return this;
	}
	
	public PC_GresWidget removeAll(){
		childs.removeAll(childs);
		if(parent!=null)
			parent.calcChildPositions();
		return this;
	}
		
	public PC_GresWidget setColor(int colorIndex, int color){
		if(colorIndex<0||colorIndex>3)
			return this;
		this.color[colorIndex] = color;
		return this;
	}
	
	public int getColor(int colorIndex){
		if(colorIndex<0||colorIndex>3)
			return 0;
		return color[colorIndex];
	}
	
	protected int getStringLength(String text){
		if(fontRenderer!=null)
			return fontRenderer.getStringWidth(text);
		else
			return PC_Utils.mc().fontRenderer.getStringWidth(text);
	}
	
	protected void drawString(String text, int x, int y){
		if(fontRenderer!=null){
			if(color[enabled?textColorShadowEnabled:textColorShadowDisabled]!=0)
				fontRenderer.drawString(text, x + 1, y + 1, color[enabled?textColorShadowEnabled:textColorShadowDisabled]);
			fontRenderer.drawString(text, x, y, color[enabled?textColorEnabled:textColorDisabled]);
		}else{
			if(color[enabled?textColorShadowEnabled:textColorShadowDisabled]!=0)
				PC_Utils.mc().fontRenderer.drawString(text, x + 1, y + 1, color[enabled?textColorShadowEnabled:textColorShadowDisabled]);
			PC_Utils.mc().fontRenderer.drawString(text, x, y, color[enabled?textColorEnabled:textColorDisabled]);
		}
		
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
