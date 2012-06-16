package net.minecraft.src;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;

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



	/** Minecraft instance */
	protected static Minecraft mc = PC_Utils.mc();

	/**
	 * 
	 * align horizontal
	 * 
	 * @authors XOR19 & Rapus95
	 * @copy (c) 2012
	 * 
	 */
	public enum PC_GresAlignH {
		/** ALIGNER_LEFT */
		LEFT,
		/** ALIGNER_RIGHT */
		RIGHT,
		/** ALIGNER_CENTER */
		CENTER,
		/** ALIGNER_STRETCH */
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
	public enum PC_GresAlignV {
		/** ALIGNER_TOP */
		TOP,
		/** ALIGNER_BOTTOM */
		BOTTOM,
		/** ALIGNER_CENTER */
		CENTER,
		/** ALIGNER_STRETCH */
		STRETCH
	}

	@SuppressWarnings("javadoc")
	public static final int textColorEnabled = 0, textColorShadowEnabled = 1, textColorDisabled = 2, textColorShadowDisabled = 3;

	/** Array of text colors */
	protected int color[] = { 0xff000000, 0x00000000, 0xff000000, 0x00000000 };

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
	protected int widgetMargin = 5;

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

	/** Widget's label (text in title or on button or whatever) */
	protected String text = "";

	/** Horizontal Align */
	protected PC_GresAlignH alignH = PC_GresAlignH.CENTER;

	/** Vertical Align */
	protected PC_GresAlignV alignV = PC_GresAlignV.CENTER;

	/** Widget ID */
	public int id = -1;
	
	

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
	 * @return
	 */
	public PC_GresWidget setId(int id) {
		this.id = id;
		return this;
	}

	/**
	 * Get widget ID
	 * 
	 * @param id
	 * @return
	 */
	public int getId() {
		return this.id;
	}



	/**
	 * @return widget's font renderer
	 */
	public FontRenderer getFontRenderer() {
		if (fontRenderer == null) return mc.fontRenderer;
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
		return this;
	}

	/**
	 * Get horizontal align
	 * 
	 * @return horizontal align
	 */
	public PC_GresAlignH getAlignH() {
		return alignH;
	}

	/**
	 * Set horizontal align
	 * 
	 * @param alignHorizontal horizontal align
	 * @return this
	 */
	public PC_GresWidget setAlignH(PC_GresAlignH alignHorizontal) {
		this.alignH = alignHorizontal;
		return this;
	}

	/**
	 * Get vertical align
	 * 
	 * @return vertical align
	 */
	public PC_GresAlignV getAlignV() {
		return alignV;
	}

	/**
	 * Set vertical align
	 * 
	 * @param alignVertical vertical align
	 * @return this
	 */
	public PC_GresWidget setAlignV(PC_GresAlignV alignVertical) {
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
		if (parent != null) parent.calcChildPositions();
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
	public abstract PC_CoordI getMinSize();

	/**
	 * @param minSize the minSize to set
	 */
	public PC_GresWidget setMinSize(PC_CoordI minSize) {
		this.minSize = minSize;
		return this;
	}

	/**
	 * @return newly calculated size, {width, height}
	 */
	public abstract PC_CoordI calcSize();

	/**
	 * Get widget size
	 * 
	 * @return {width, height}
	 */
	public PC_CoordI getSize() {
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
		if (parent != null && calcParent) parent.calcChildPositions();
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
		if (childs != null) for (int i = 0; i < childs.size(); i++) {
			childs.get(i).calcChildPositions();
			PC_CoordI childSize = childs.get(i).calcSize();
			if (childSize.y > maxh) maxh = childSize.y;
			if (childSize.x > size.x || childSize.y > size.y) {
				if (childSize.x > size.x) size.x = childSize.x;
				if (childSize.y > size.y) size.y = childSize.y;
				if (parent != null) parent.calcChildPositions();
				calcChildPositions();
				return;
			}
			if (xx + size.x > size.x) {
				xx = 0;
				yy += maxh + widgetMargin;
			}
			childs.get(i).setPosition(xx, yy);
			xx += size.x + widgetMargin;
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
		if (!canAddWidget) { return null; }
		newwidget.parent = this;
		newwidget.setFontRenderer(this.fontRenderer);
		childs.add(newwidget);
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
		if (parent != null) parent.calcChildPositions();
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
		if (colorIndex < 0 || colorIndex > 3) return this;
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
		if (colorIndex < 0 || colorIndex > 3) return 0;
		return color[colorIndex];
	}

	/**
	 * Get string length from font renderer
	 * 
	 * @param text the string
	 * @return length in pixels
	 */
	protected int getStringLength(String text) {
		if (fontRenderer != null) return fontRenderer.getStringWidth(text);
		else return mc.fontRenderer.getStringWidth(text);
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
		fr.drawString(text, x, y, color[enabled ? textColorEnabled : textColorDisabled]);

	}

	/**
	 * Render this and all children at correct positions
	 * 
	 * @param posOffset offset from top left
	 */
	public void updateRenderer(PC_CoordI posOffset) {
		this.render(posOffset);
		if (childs != null) for (int i = 0; i < childs.size(); i++)
			childs.get(i).updateRenderer(posOffset.offset(pos));
	}

	/**
	 * Do render this widget
	 * 
	 * @param posOffset offset from top left
	 */
	protected abstract void render(PC_CoordI posOffset);

	/**
	 * Get the widget under mouse cursor. First tries children, then self, null at last.
	 * 
	 * @param mousePos mouse absolute x
	 * @return the widget under mouse
	 */
	public PC_GresWidget getWidgetUnderMouse(PC_CoordI mousePos) {
		PC_GresWidget widget;
		PC_CoordI mpos = mousePos.offset(-pos.x, -pos.y);

		// mouse not over this widget
		if (mpos.x < 0 || mpos.x >= size.x || mpos.y < 0 || mpos.y >= size.y || mouseOver(mpos) == false) {
			this.isMouseOver = false;

			if (childs != null) {
				for (int i = 0; i < childs.size(); i++) {
					childs.get(i).getWidgetUnderMouse(new PC_CoordI(-1, -1));
				}
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

		if (mouseOver(mpos) == false) {
			this.isMouseOver = false;
			return null;
		}
		return this;
	}

	/**
	 * Get absolute position on screen
	 * 
	 * @return { x, y}
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
	 * Render texture using 9patch-like scaling method.<br>
	 * 
	 * <pre>
	 * +------+--+ 
	 * |      |  | 
	 * |      |  | 
	 * +------+--+ 
	 * |      |  | 
	 * +------+--+
	 * </pre>
	 * 
	 * @param offset offset relative to parent top left
	 * @param texture texture to render (filename)
	 * @param rectSize rectangle size
	 * @param imgOffset offset within the texture image (from top left)
	 * @param imageSize size of the whole "scalable" region in texture file (eg. the whole huge "button" field)
	 * @param borderRight width of the right-hand-side column appended after the left part
	 * @param borderBottom height of the bottom row appended below the upper part
	 */
	protected void renderTextureSliced(PC_CoordI offset, String texture, PC_CoordI rectSize, PC_CoordI imgOffset, PC_CoordI imageSize) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(texture));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);


		/*
		 * AAAAA BBBBB
		 * AAAAA BBBBB
		 * AAAAA BBBBB
		 * CCCCC DDDDD
		 * CCCCC DDDDD
		 * CCCCC DDDDD
		 */

		// @formatter:off
		
		int rectxhalf1 = (int)Math.floor(rectSize.x/2F);
		int rectxhalf2 = (int)Math.ceil(rectSize.x/2F);
		int rectyhalf1 = (int)Math.floor(rectSize.y/2F);
		int rectyhalf2 = (int)Math.ceil(rectSize.y/2F);
		
		
		// A
		drawTexturedModalRect(
				pos.x + offset.x,
				pos.y + offset.y,
				imgOffset.x,
				imgOffset.y,
				rectxhalf1,
				rectyhalf1);
		
		// B
		drawTexturedModalRect(
				pos.x + offset.x + rectxhalf1,
				pos.y + offset.y,
				imgOffset.x + imageSize.x - rectxhalf2,
				imgOffset.y,
				rectxhalf2,
				rectyhalf1);
		
		//left bottom wide
		drawTexturedModalRect(
				pos.x + offset.x,
				pos.y + offset.y + rectyhalf1,
				imgOffset.x,
				imgOffset.y + imageSize.y - rectyhalf2,
				rectxhalf1,
				rectyhalf2);
		
		//right bottom square
		drawTexturedModalRect(
				pos.x + offset.x + rectxhalf1,
				pos.y + offset.y + rectyhalf1,
				imgOffset.x + imageSize.x - rectxhalf2,
				imgOffset.y + imageSize.y - rectyhalf2,
				rectxhalf2,
				rectyhalf2);
		
		// @formatter:on
	}


	/**
	 * is mouse over widget?
	 * 
	 * @param mousePos mouse position
	 * @return is over
	 */
	public abstract boolean mouseOver(PC_CoordI mousePos);

	public abstract boolean mouseClick(PC_CoordI mousePos, int key);

	public abstract void mouseMove(PC_CoordI mousePos);

	/**
	 * On key pressed.
	 * 
	 * @param c character of the key
	 * @param key key index
	 * @return true if key was valid and was used.
	 */
	public abstract boolean keyTyped(char c, int key);

	/**
	 * Draw point on screen
	 * 
	 * @param point the point pos
	 * @param c color
	 */
	protected void drawPoint(PC_CoordI point, int c) {
		drawHorizontalLine(point.x, point.x, point.y, c);
	}

}
