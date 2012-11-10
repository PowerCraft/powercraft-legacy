package powercraft.core;


import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;

import org.lwjgl.opengl.GL11;


/**
 * Base class for GUI-system
 * 
 * @authors XOR19, Rapus95, MightyPork
 * @copy (c) 2012
 */
public abstract class PC_GresWidget extends Gui {

	/** zero coord */
	public static final PC_CoordI zerosize = new PC_CoordI(0, 0);


	/** Minecraft instance */
	protected static Minecraft mc = PC_ClientUtils.mc();

	protected static String imgdir = mod_PowerCraftCore.getInstance().getTextureDirectory();
	
	protected enum MouseOver{
		NON,
		THIS,
		CHILD
	}
	
	/**
	 * align vertical
	 * 
	 * @authors XOR19 & Rapus95
	 * @copy (c) 2012
	 */
	public enum PC_GresAlign {
		/** LEFT */
		LEFT,
		/** RIGHT */
		RIGHT,
		/** TOP */
		TOP,
		/** BOTTOM */
		BOTTOM,
		/** CENTER */
		CENTER,
		/** STRETCH */
		STRETCH,
		/** JUSTIFIED */
		JUSTIFIED
	}

	@SuppressWarnings("javadoc")
	public static final int textColorEnabled = 0, textColorShadowEnabled = 1, textColorDisabled = 2, textColorShadowDisabled = 3, textColorHover = 4,
			textColorClicked = 5;

	/** Array of text colors */
	protected int color[] = { 0x000000, 0, 0x333333, 0, 0x000000, 0x000000 };

	/** Parent widget */
	protected PC_GresWidget parent = null;


	/** List of children */
	protected ArrayList<PC_GresWidget> childs = new ArrayList<PC_GresWidget>();

	/** Font renderer */
	protected FontRenderer fontRenderer = null;

	/** pos of left top corner */
	protected PC_CoordI pos = new PC_CoordI(0, 0);

	/** Widget size */
	protected PC_CoordI size = new PC_CoordI(0, 0);

	/** Minimal allowed widget size */
	protected PC_CoordI minSize = new PC_CoordI(0, 0);

	/** Distance from other widgets in group. */
	protected int widgetMargin = 4;

	/** Counter used for the automatic resizing */
	protected int cursorCounter = 0;

	/** Can add child widgets */
	protected boolean canAddWidget = true;

	/** Is mouse over this widget? */
	protected boolean isMouseOver = false;

	/** Is widget enabled = clickable */
	protected boolean enabled = true;

	/** Is widget focused (used mainly for text edits) */
	protected boolean hasFocus = false;

	/** Is visible */
	protected boolean visible = true;

	/**
	 * Set visibility. Invisible widgets dont take space in layouts.
	 * Same as css display:none
	 * @param show flag visible
	 * @return this
	 */
	protected PC_GresWidget setVisible(boolean show) {
		visible = show;
		updateVisible(show);
		return this;
	}

	private void updateVisible(boolean show){
		visibleChanged(show);
		for(PC_GresWidget w:childs){
			w.updateVisible(show);
		}
	}
	
	protected void visibleChanged(boolean show){}
	
	/**
	 * @return true if is visible
	 */
	protected boolean isVisible() {
		return visible;
	}

	/** Widget's label (text in title or on button or whatever) */
	protected String text = "";

	/** Horizontal Align */
	protected PC_GresAlign alignH = PC_GresAlign.CENTER;

	/** Vertical Align */
	protected PC_GresAlign alignV = PC_GresAlign.CENTER;

	/** Container Manager */
	protected PC_IGresGui gui = null;

	/** Widget ID (general purpose) */
	public int id = -1;

	/** Additional widget tag (general purpose) */
	public String tag = "";

	/**
	 * A widget
	 */
	public PC_GresWidget() {
		PC_CoordI minSize = getMinSize();
		this.size = minSize.copy();
		this.minSize = minSize.copy();
	}

	/**
	 * A widget
	 * 
	 * @param label widget's label / text
	 */
	public PC_GresWidget(String label) {
		this.text = label;
		PC_CoordI minSize = getMinSize();
		this.size = minSize.copy();
		this.minSize = minSize.copy();
	}

	/**
	 * A widget
	 * 
	 * @param width widget minWidth
	 * @param height widget minHeight
	 */
	public PC_GresWidget(int width, int height) {
		PC_CoordI minSize = new PC_CoordI(width, height);
		this.size = minSize.copy();
		this.minSize = minSize.copy();
	}

	/**
	 * A widget
	 * 
	 * @param width widget minWidth
	 * @param height widget minHeight
	 * @param label widget label / text
	 */
	public PC_GresWidget(int width, int height, String label) {
		this(width, height);
		this.text = label;
	}


	/**
	 * Set widget ID
	 * 
	 * @param id
	 * @return this
	 */
	public PC_GresWidget setId(int id) {
		this.id = id;
		return this;
	}

	/**
	 * Get widget ID
	 * 
	 * @return id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Get widget tag
	 * 
	 * @return tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Set widget tag
	 * 
	 * @param tag the tag
	 * @return this
	 */
	public PC_GresWidget setTag(String tag) {
		this.tag = tag;
		return this;
	}



	/**
	 * @return widget's font renderer
	 */
	public FontRenderer getFontRenderer() {
		if (fontRenderer == null) {
			return mc.fontRenderer;
		}
		return fontRenderer;
	}

	/**
	 * Set widget's font renderer
	 * 
	 * @param fontRenderer the font renderer
	 * @return this
	 */
	public PC_GresWidget setFontRenderer(FontRenderer fontRenderer) {
		this.fontRenderer = fontRenderer;
		for (PC_GresWidget w : childs) {
			w.setFontRenderer(fontRenderer);
		}
		return this;
	}

	/**
	 * Get horizontal align
	 * 
	 * @return horizontal align
	 */
	public PC_GresAlign getAlignH() {
		return alignH;
	}

	/**
	 * Set horizontal align
	 * 
	 * @param alignHorizontal horizontal align
	 * @return this
	 */
	public PC_GresWidget setAlignH(PC_GresAlign alignHorizontal) {
		this.alignH = alignHorizontal;
		return this;
	}

	/**
	 * Get vertical align
	 * 
	 * @return vertical align
	 */
	public PC_GresAlign getAlignV() {
		return alignV;
	}

	/**
	 * Set vertical align
	 * 
	 * @param alignVertical vertical align
	 * @return this
	 */
	public PC_GresWidget setAlignV(PC_GresAlign alignVertical) {
		this.alignV = alignVertical;
		return this;
	}

	/**
	 * @return has focus
	 */
	public boolean getFocus() {
		return hasFocus;
	}

	/**
	 * Set focus state
	 * 
	 * @param focus focused
	 * @return this
	 */
	public PC_GresWidget setFocus(boolean focus) {
		hasFocus = focus;
		return this;
	}

	/**
	 * @return widget's text / label
	 */
	public String getText() {
		return text;
	}

	/**
	 * Set widget's label, resize if needed
	 * 
	 * @param text new text / label
	 * @return this
	 */
	public PC_GresWidget setText(String text) {
		this.text = text;
		if (parent != null) {
			parent.calcChildPositions();
		}
		return this;
	}

	/**
	 * Increment cursor counter, used for text field animations
	 */
	public void updateCursorCounter() {
		cursorCounter++;
	}

	/**
	 * @return minimal size, {width,height}
	 */
	public PC_CoordI getMinSize() {
		if (!visible) return new PC_CoordI(0, 0);
		return calcSize().copy();
	}

	/**
	 * @param minSize the minSize to set
	 * @return this
	 */
	public PC_GresWidget setMinSize(PC_CoordI minSize) {
		this.minSize = minSize;
		return this;
	}

	/**
	 * set min size
	 * 
	 * @param w width
	 * @param h height
	 * @return this
	 */
	public PC_GresWidget setMinSize(int w, int h) {
		this.minSize.setTo(w, h);
		return this;
	}

	/**
	 * set min size width
	 * 
	 * @param w width
	 * @return this
	 */
	public PC_GresWidget setMinWidth(int w) {
		this.minSize.setTo(w, this.minSize.y);
		return this;
	}

	/**
	 * set min size height
	 * 
	 * @param h height
	 * @return this
	 */
	public PC_GresWidget setMinHeight(int h) {
		this.minSize.setTo(this.minSize.x, h);
		return this;
	}

	/**
	 * Set widget margin
	 * 
	 * @param widgetMargin
	 * @return this
	 */
	public PC_GresWidget setWidgetMargin(int widgetMargin) {
		this.widgetMargin = widgetMargin;
		return this;
	}

	/**
	 * @return newly calculated size, {width, height}
	 */
	public abstract PC_CoordI calcSize();

	/**
	 * Get the Container Manager
	 * 
	 * @return the Container Manager
	 */
	public PC_IGresGui getGui() {
		return gui;
	}

	/**
	 * Set the Container Manager
	 * 
	 * @param containerManager the new Container Manager
	 * @return this
	 */
	public PC_GresWidget setGui(PC_IGresGui gui) {
		this.gui = gui;
		for (PC_GresWidget w : childs) {
			w.setGui(gui);
		}
		return this;
	}

	/**
	 * Get widget size
	 * 
	 * @return {width, height}
	 */
	public PC_CoordI getSize() {
		if (!visible) return new PC_CoordI(0, 0);
		return size.copy();
	}

	/**
	 * Set widget size
	 * 
	 * @param width width
	 * @param height height
	 * @param calcParent flag whether to ask parent for position recalculation
	 * @return this
	 */
	public PC_GresWidget setSize(int width, int height, boolean calcParent) {
		this.size.setTo(width, height);
		if (parent != null && calcParent) {
			parent.calcChildPositions();
		}
		return this;
	}

	/**
	 * Set size, recalculate position
	 * 
	 * @param width width
	 * @param height height
	 * @return this
	 */
	public PC_GresWidget setSize(int width, int height) {
		return setSize(width, height, true);
	}

	/**
	 * Set size, recalculate position
	 * 
	 * @param width width
	 * @param height height
	 * @return this
	 */
	public PC_GresWidget setSize(PC_CoordI size) {
		this.size.setTo(size);
		return this;
	}
	
	/**
	 * Get position
	 * 
	 * @return {x, y}
	 */
	public PC_CoordI getPosition() {
		return pos;
	}

	/**
	 * Set position of the widget
	 * 
	 * @param x x
	 * @param y y
	 * @return this
	 */
	public PC_GresWidget setPosition(int x, int y) {
		this.pos.setTo(x, y);
		return this;
	}

	/**
	 * @return is enabled?
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * set "enabled" flag
	 * 
	 * @param enabled state
	 * @return this
	 */
	public PC_GresWidget enable(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	/**
	 * Refresh calculated children positions
	 */
	public abstract void calcChildPositions();

	/**
	 * Default implementation of child position calculation
	 */
	public void calcChildPositionsDefault() {
		int maxh = 0, xx = 0, yy = 0;
		if (childs != null) {
			for (int i = 0; i < childs.size(); i++) {
				childs.get(i).calcChildPositions();
				PC_CoordI childSize = childs.get(i).calcSize();
				if (!childs.get(i).isVisible()) childSize = new PC_CoordI(0, 0);
				if (childSize.y > maxh) {
					maxh = childSize.y;
				}
				if (childSize.x > size.x || childSize.y > size.y) {
					if (childSize.x > size.x) {
						size.x = childSize.x;
					}
					if (childSize.y > size.y) {
						size.y = childSize.y;
					}
					if (parent != null) {
						parent.calcChildPositions();
					}
					calcChildPositions();
					return;
				}
				if (xx + childSize.x > size.x) {
					xx = 0;
					yy += maxh + widgetMargin;
				}
				childs.get(i).setPosition(xx, yy);
				xx += size.x + widgetMargin;
			}
		}
	}

	/**
	 * @return parent widget
	 */
	public PC_GresWidget getParent() {
		return parent;
	}


	/**
	 * Add child widget
	 * 
	 * @param newwidget new widget
	 * @return this
	 */
	public PC_GresWidget add(PC_GresWidget newwidget) {
		if (!canAddWidget) {
			return null;
		}
		newwidget.parent = this;
		newwidget.setFontRenderer(fontRenderer);
		newwidget.setGui(gui);
		childs.add(newwidget);
		newwidget.callAddedToWidget();
		calcChildPositions();
		return this;
	}

	/**
	 * Remove child widget, even from children's lists
	 * 
	 * @param removewidget widget to remove from child list
	 * @return this
	 */
	public PC_GresWidget remove(PC_GresWidget removewidget) {
		if (!childs.remove(removewidget)) {
			for (int i = 0; i < childs.size(); i++) {
				childs.get(i).remove(removewidget);
			}
		}
		calcChildPositions();
		return this;
	}

	/**
	 * Remove all children
	 * 
	 * @return this
	 */
	public PC_GresWidget removeAll() {
		childs.removeAll(childs);
		if (parent != null) {
			parent.calcChildPositions();
		}
		return this;
	}

	/**
	 * Set color to index
	 * 
	 * @param colorIndex color index (constant)
	 * @param color the color, eg. 0xFFFFFF.
	 * @return this
	 */
	public PC_GresWidget setColor(int colorIndex, int color) {
		if (colorIndex < 0 || colorIndex > 5) {
			return this;
		}
		this.color[colorIndex] = color;
		return this;
	}

	/**
	 * Get color for index
	 * 
	 * @param colorIndex color index (constant)
	 * @return color number, eg. 0xFFFFFF
	 */
	public int getColor(int colorIndex) {
		if (colorIndex < 0 || colorIndex > 5) {
			return 0;
		}
		return color[colorIndex];
	}

	/**
	 * Get string length from font renderer
	 * 
	 * @param text the string
	 * @return length in pixels
	 */
	protected int getStringWidth(String text) {
		FontRenderer fr = getFontRenderer();
		return fr.getStringWidth(text);
	}

	/**
	 * Get char height
	 * 
	 * @return height in pixels
	 */
	protected int getLineHeight() {
		return getFontRenderer().FONT_HEIGHT;
	}

	/**
	 * Draw string, using colors from the color array.
	 * 
	 * @param text text to draw (usually the label)
	 * @param x pos x
	 * @param y pos y
	 */
	protected void drawString(String text, int x, int y) {
		FontRenderer fr = getFontRenderer();
		if (color[enabled ? textColorShadowEnabled : textColorShadowDisabled] != 0) {
			fr.drawString(text, x + 1, y + 1, color[enabled ? textColorShadowEnabled : textColorShadowDisabled]);
		}
		fr.drawString(text, x, y, color[enabled ? (isMouseOver ? textColorHover : textColorEnabled) : textColorDisabled]);
	}

	/**
	 * Draw string, using overide color
	 * 
	 * @param text text to draw (usually the label)
	 * @param x pos x
	 * @param y pos y
	 * @param colorOverride custom color
	 */
	protected void drawStringColor(String text, int x, int y, int colorOverride) {
		FontRenderer fr = getFontRenderer();
		if (color[enabled ? textColorShadowEnabled : textColorShadowDisabled] != 0) {
			fr.drawString(text, x + 1, y + 1, color[enabled ? textColorShadowEnabled : textColorShadowDisabled]);
		}
		fr.drawString(text, x, y, colorOverride);
	}
	
	public static PC_RectI setDrawRect(PC_RectI old, PC_RectI _new, double scale){
		PC_RectI rect = old.averageQuantity(_new);
		if(rect.width<=0 || rect.height<=0) return null;
		int h = mc.displayHeight;
		GL11.glScissor((int)(rect.x * scale), h - (int)((rect.y + rect.height) * scale), (int)(rect.width * scale), (int)(rect.height * scale));
		return rect;
	}
	
	/**
	 * Render this and all children at correct positions
	 * 
	 * @param posOffset offset from top left
	 */
	public void updateRenderer(PC_CoordI posOffset, PC_RectI scissorOld, double scale) {
		if (!visible) return;
		
		PC_RectI scissorNew = setDrawRect(scissorOld, new PC_RectI(posOffset.x + pos.x, posOffset.y + pos.y, size.x, size.y), scale);
		if(scissorNew==null)return;
		
		PC_RectI rect = render(posOffset, scissorNew, scale);
		if(rect!=null)
			scissorNew = setDrawRect(scissorNew, rect, scale);
		
		if(mod_PowerCraftCore.guiOverlayer){
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor4f(scissorNew.width/100.0f, scissorNew.height/100.0f, 0.0f, 0.4f);
			GL11.glVertex2f(-1000, -1000);
			GL11.glVertex2f(-1000, 1000);
			GL11.glVertex2f(1000, 1000);
			GL11.glVertex2f(1000, -1000);
			GL11.glEnd();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
		}
		
		childRenderer(posOffset.offset(pos), scissorNew, scale);
	}

	public void childRenderer(PC_CoordI posOffset, PC_RectI scissorNew, double scale){
		if (childs != null) {
			for (int i = 0; i < childs.size(); i++) {
				if (childs.get(i).visible) childs.get(i).updateRenderer(posOffset, scissorNew, scale);
			}
		}
	}
	
	/**
	 * Do render this widget
	 * 
	 * @param posOffset offset from top left
	 */
	protected abstract PC_RectI render(PC_CoordI posOffset, PC_RectI scissorOld, double scale);

	/**
	 * Get the widget under mouse cursor. First tries children, then self, null
	 * at last.
	 * 
	 * @param mousePos mouse absolute x
	 * @return the widget under mouse
	 */
	public PC_GresWidget getWidgetUnderMouse(PC_CoordI mousePos) {
		if (!visible) return null;
		PC_GresWidget widget;
		PC_CoordI mpos = mousePos.offset(-pos.x, -pos.y);
			
		
		MouseOver mo = MouseOver.NON;
		
		// mouse not over this widget
		if (mpos.x < 0 || mpos.x >= size.x || mpos.y < 0 || mpos.y >= size.y || (mo = mouseOver(mpos)) != MouseOver.CHILD) {
			this.isMouseOver = false;

			if (childs != null) {
				for (int i = 0; i < childs.size(); i++) {
					childs.get(i).getWidgetUnderMouse(new PC_CoordI(-1, -1));
				}
			}

			if(mo==MouseOver.THIS){
				this.isMouseOver = true;
				return this;
			}
			
			return null;
		}

		this.isMouseOver = true;

		if (childs != null) {
			for (int i = 0; i < childs.size(); i++) {

				widget = childs.get(i).getWidgetUnderMouse(mpos);
				if (widget != null) {
					for (i++; i < childs.size(); i++) {
						childs.get(i).getWidgetUnderMouse(new PC_CoordI(-1, -1));
					}
					return widget;
				}

			}
		}

		return this;
	}

	/**
	 * Get absolute position on screen
	 * 
	 * @return coord of top left.
	 */
	public PC_CoordI getPositionOnScreen() {
		PC_CoordI position;
		if (parent != null) {
			position = parent.getPositionOnScreen().offset(pos);

		} else {
			position = pos.copy();
		}
		return position;
	}

	/**
	 * Render textured rect with Alpha support at given position.
	 * 
	 * @param offset offset relative to root top left
	 * @param texture texture to render (filename)
	 * @param rectSize size of the rendered texture
	 * @param imgOffset offset within the texture image (from top left)
	 */
	protected void renderImage(PC_CoordI offset, String texture, PC_CoordI rectSize, PC_CoordI imgOffset) {

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(texture));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		drawTexturedModalRect(pos.x + offset.x, pos.y + offset.y, imgOffset.x, imgOffset.y, rectSize.x, rectSize.y);

		GL11.glDisable(GL11.GL_BLEND);

	}

	/**
	 * Render textured rect with Alpha support at given position.
	 * 
	 * @param gui the gui being drawed on
	 * @param texture texture to render (filename)
	 * @param startPos left top corner absolute position
	 * @param rectSize size of the rendered texture
	 * @param imgOffset offset within the texture image (from top left)
	 */
	protected static void renderImage_static(Gui gui, String texture, PC_CoordI startPos, PC_CoordI rectSize, PC_CoordI imgOffset) {

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(texture));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		gui.drawTexturedModalRect(startPos.x, startPos.y, imgOffset.x, imgOffset.y, rectSize.x, rectSize.y);

		GL11.glDisable(GL11.GL_BLEND);

	}

	/**
	 * Render texture using 9patch-like scaling method.<br>
	 * 
	 * @param offset offset relative to root top left
	 * @param texture texture to render (filename)
	 * @param rectSize rectangle size
	 * @param imgOffset offset within the texture image (from top left)
	 * @param imgSize size of the whole "scalable" region in texture file (eg.
	 *            the whole huge "button" field)
	 */
	protected void renderTextureSliced(PC_CoordI offset, String texture, PC_CoordI rectSize, PC_CoordI imgOffset, PC_CoordI imgSize) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(texture));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		int rxh1 = (int) Math.floor(rectSize.x / 2F);
		int rxh2 = (int) Math.ceil(rectSize.x / 2F);
		int ryh1 = (int) Math.floor(rectSize.y / 2F);
		int ryh2 = (int) Math.ceil(rectSize.y / 2F);


		// A
		drawTexturedModalRect(pos.x + offset.x, pos.y + offset.y, imgOffset.x, imgOffset.y, rxh1, ryh1);

		// B
		drawTexturedModalRect(pos.x + offset.x + rxh1, pos.y + offset.y, imgOffset.x + imgSize.x - rxh2, imgOffset.y, rxh2, ryh1);

		// left bottom wide
		drawTexturedModalRect(pos.x + offset.x, pos.y + offset.y + ryh1, imgOffset.x, imgOffset.y + imgSize.y - ryh2, rxh1, ryh2);

		// right bottom square
		drawTexturedModalRect(pos.x + offset.x + rxh1, pos.y + offset.y + ryh1, imgOffset.x + imgSize.x - rxh2, imgOffset.y + imgSize.y - ryh2, rxh2,
				ryh2);

		GL11.glDisable(GL11.GL_BLEND);

	}

	/**
	 * Render texture using 9patch-like scaling method.<br>
	 * 
	 * @param gui the gui being drawed on
	 * @param startPos offset relative to parent top left
	 * @param texture texture to render (filename)
	 * @param rectSize rectangle size
	 * @param imgOffset offset within the texture image (from top left)
	 * @param imgSize size of the whole "scalable" region in texture file (eg.
	 *            the whole huge "button" field)
	 */
	protected static void renderTextureSliced_static(Gui gui, PC_CoordI startPos, String texture, PC_CoordI rectSize, PC_CoordI imgOffset,
			PC_CoordI imgSize) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(texture));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		int rxh1 = (int) Math.floor(rectSize.x / 2F);
		int rxh2 = (int) Math.ceil(rectSize.x / 2F);
		int ryh1 = (int) Math.floor(rectSize.y / 2F);
		int ryh2 = (int) Math.ceil(rectSize.y / 2F);

		// A
		gui.drawTexturedModalRect(startPos.x, startPos.y, imgOffset.x, imgOffset.y, rxh1, ryh1);

		// B
		gui.drawTexturedModalRect(startPos.x + rxh1, startPos.y, imgOffset.x + imgSize.x - rxh2, imgOffset.y, rxh2, ryh1);

		// left bottom wide
		gui.drawTexturedModalRect(startPos.x, startPos.y + ryh1, imgOffset.x, imgOffset.y + imgSize.y - ryh2, rxh1, ryh2);

		// right bottom square
		gui.drawTexturedModalRect(startPos.x + rxh1, startPos.y + ryh1, imgOffset.x + imgSize.x - rxh2, imgOffset.y + imgSize.y - ryh2, rxh2, ryh2);


		GL11.glDisable(GL11.GL_BLEND);

	}


	/**
	 * Check if mouse is over widget.<br>
	 * The given coordinate is relative to widget's top left corner.
	 * 
	 * @param mousePos mouse position
	 * @return is over
	 */
	public abstract MouseOver mouseOver(PC_CoordI mousePos);

	/**
	 * Mouse clicked on widget.
	 * 
	 * @param mousePos mouse position
	 * @param key mouse button index, -1 = mouse up.
	 * @return event accepted
	 */
	public abstract boolean mouseClick(PC_CoordI mousePos, int key);

	/**
	 * On mouse moved. Last focused widget gets mouse move events.
	 * 
	 * @param mousePos current mouse position.
	 */
	public abstract void mouseMove(PC_CoordI mousePos);

	/**
	 * On mouse wheel moved. Last focused widget gets wheel move events.
	 * 
	 * @param i wheelmoved direction
	 */
	public abstract void mouseWheel(int i);

	/**
	 * On key pressed.
	 * 
	 * @param c character of the key
	 * @param key key index
	 * @return true if key was valid and was used.
	 */
	public abstract boolean keyTyped(char c, int key);

	/**
	 * Called when Widget added to another widget
	 */
	public void callAddedToWidget() {
		addedToWidget();
		for (PC_GresWidget w : childs) {
			w.callAddedToWidget();
		}
	}

	/**
	 * Called when Widget added to another widget
	 */
	public abstract void addedToWidget();

	protected static void drawButton(PC_GresWidget widget, PC_CoordI pos, PC_CoordI size, String text, int state){
	
		int txC = 0xe0e0e0;
	
		if (state == 0) {
			txC = 0xa0a0a0; // dark
		}
		if (state == 1) {
			txC = 0xe0e0e0; // light
		}
		if (state > 1) {
			txC = 0xffffa0; // yellow
		}
	
		renderTextureSliced_static(widget, pos, imgdir + "gres/button.png", size, new PC_CoordI(0, state * 50), new PC_CoordI(256, 50));
	
		widget.drawCenteredString(widget.getFontRenderer(), text, pos.x + size.x / 2, pos.y + (size.y - widget.getFontRenderer().FONT_HEIGHT)
				/ 2, txC);
	}
}
