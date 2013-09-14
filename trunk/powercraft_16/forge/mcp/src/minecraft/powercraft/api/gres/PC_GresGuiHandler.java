package powercraft.api.gres;


import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import powercraft.api.PC_RectI;
import powercraft.api.PC_Vec2I;
import powercraft.api.gres.events.PC_GresKeyEvent;
import powercraft.api.gres.events.PC_GresPaintEvent;
import powercraft.api.gres.events.PC_GresPrePostEvent.EventType;
import powercraft.api.gres.events.PC_GresTickEvent;


public class PC_GresGuiHandler extends PC_GresContainer {

	private final PC_IGresGui gui;
	private final PC_IGresClient client;
	private boolean initialized;
	private PC_GresComponent focusedComponent = this;
	private PC_GresComponent mouseOverComponent = this;


	public PC_GresGuiHandler(PC_IGresGui gui, PC_IGresClient client) {

		this.gui = gui;
		this.client = client;
	}


	public PC_IGresClient getClient() {

		return client;
	}


	public void close() {

		gui.getMinecraft().setIngameFocus();
	}


	@Override
	public void setVisible(boolean visible) {

		if (!visible) close();
	}


	@Override
	protected void setParentVisible(boolean visible) {

		throw new IllegalArgumentException("GresGuiHandler can't have a parent");
	}


	@Override
	protected void setParent(PC_GresContainer parent) {

		throw new IllegalArgumentException("GresGuiHandler can't have a parent");
	}


	@Override
	public PC_GresGuiHandler getGuiHandler() {

		return this;
	}


	@Override
	public void setRect(PC_RectI rect) {

		throw new IllegalArgumentException("GresGuiHandler can't set rect");
	}


	@Override
	public void setSize(PC_Vec2I size) {

		throw new IllegalArgumentException("GresGuiHandler can't set size");
	}


	@Override
	public void setMinSize(PC_Vec2I minSize) {

		throw new IllegalArgumentException("GresGuiHandler can't set minsize");
	}


	@Override
	protected PC_Vec2I calculateMinSize() {

		return new PC_Vec2I();
	}


	@Override
	public void setMaxSize(PC_Vec2I maxSize) {

		throw new IllegalArgumentException("GresGuiHandler can't set maxsize");
	}


	@Override
	protected PC_Vec2I calculateMaxSize() {

		return new PC_Vec2I();
	}


	@Override
	public void setPrefSize(PC_Vec2I prefSize) {

		throw new IllegalArgumentException("GresGuiHandler can't set prefsize");
	}


	@Override
	protected PC_Vec2I calculatePrefSize() {

		return new PC_Vec2I();
	}


	@Override
	protected void paint(PC_RectI scissor, double scale, int displayHeight, float timeStamp) {

		drawGradientRect(0, 0, rect.width, rect.height, -1072689136, -804253680);
	}


	protected void eventInitGui() {

		minSize.setTo(gui.getSize());
		maxSize.setTo(minSize);
		prefSize.setTo(minSize);
		super.setSize(minSize);
		if (!initialized) {
			client.initGui(this);
			initialized = true;
		}
	}


	protected void eventUpdateScreen() {

		fireEvent(new PC_GresTickEvent(this, EventType.PRE));
		onTick();
		fireEvent(new PC_GresTickEvent(this, EventType.POST));
	}


	protected void eventDrawScreen(PC_Vec2I mouse, float timeStamp) {

		Minecraft mc = gui.getMinecraft();
		ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		fireEvent(new PC_GresPaintEvent(this, EventType.PRE, timeStamp));
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		doPaint(new PC_Vec2I(0, 0), null, scaledresolution.getScaleFactor(), mc.displayHeight, timeStamp);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		drawMouseItemStack(mouse, timeStamp);
		if (mc.thePlayer.inventory.getItemStack() == null) {
			drawTooltip(mouse);
		}
		fireEvent(new PC_GresPaintEvent(this, EventType.POST, timeStamp));
	}


	private void drawMouseItemStack(PC_Vec2I mouse, float timeStamp) {

		gui.drawMouseItemStack(mouse, timeStamp);
	}


	private void drawTooltip(PC_Vec2I mouse) {

		List<String> list = mouseOverComponent.getTooltip(mouse.sub(mouseOverComponent.getRealLocation()));
		if (list != null && !list.isEmpty()) {
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			int maxWidth = 0;
			for (String s : list) {
				int width = fontRenderer.getStringWidth(s);
				if (width > maxWidth) {
					maxWidth = width;
				}
			}

			int x = mouse.x + 12;
			int y = mouse.y - 12;
			int k1 = 8;

			if (list.size() > 1) {
				k1 += 2 + (list.size() - 1) * 10;
			}

			if (x + maxWidth > rect.width) {
				x -= 28 + maxWidth;
			}

			if (y + k1 + 6 > rect.height) {
				y = rect.height - k1 - 6;
			}

			final int l1 = -267386864;
			this.drawGradientRect(x - 3, y - 4, maxWidth + 6, 1, l1, l1);
			this.drawGradientRect(x - 3, y + k1 + 3, maxWidth + 6, 1, l1, l1);
			this.drawGradientRect(x - 3, y - 3, maxWidth + 6, k1 + 6, l1, l1);
			this.drawGradientRect(x - 4, y - 3, 1, k1 + 6, l1, l1);
			this.drawGradientRect(x + maxWidth + 3, y - 3, 1, k1 + 6, l1, l1);
			final int i2 = 1347420415;
			final int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
			this.drawGradientRect(x - 3, y - 3 + 1, 1, k1 + 4, i2, j2);
			this.drawGradientRect(x + maxWidth + 2, y - 3 + 1, 1, k1 + 4, i2, j2);
			this.drawGradientRect(x - 3, y - 3, maxWidth + 6, 1, i2, i2);
			this.drawGradientRect(x - 3, y + k1 + 2, maxWidth + 6, 1, j2, j2);

			boolean isMainLine = true;
			for (String s : list) {
				fontRenderer.drawStringWithShadow(s, x, y, -1);
				if (isMainLine) {
					y += 2;
					isMainLine = false;
				}
				y += 10;
			}
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			RenderHelper.enableStandardItemLighting();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
	}


	protected void eventKeyTyped(char key, int keyCode) {

		if (focusedComponent == this || !focusedComponent.onKeyTyped(key, keyCode)) {
			PC_GresKeyEvent event = new PC_GresKeyEvent(this, key, keyCode);
			fireEvent(event);
			if (!event.isConsumed()) {
				tryActionOnKeyTyped(key, keyCode);
			}
		}
	}


	private void checkMouseOverComponent(PC_Vec2I mouse, int buttons) {

		PC_GresComponent newMouseOverComponent = getComponentAtPosition(mouse);
		if (newMouseOverComponent == null) {
			newMouseOverComponent = this;
		}
		if (newMouseOverComponent != mouseOverComponent) {
			mouseOverComponent.onMouseLeave(mouse.sub(mouseOverComponent.getRealLocation()), buttons);
			newMouseOverComponent.onMouseEnter(mouse.sub(newMouseOverComponent.getRealLocation()), buttons);
			mouseOverComponent = newMouseOverComponent;
		}
	}


	protected void eventMouseButtonDown(PC_Vec2I mouse, int buttons, int eventButton) {

		setFokus(mouseOverComponent);
		focusedComponent.onMouseButtonDown(mouse.sub(focusedComponent.getRealLocation()), buttons, eventButton);
	}


	protected void eventMouseButtonUp(PC_Vec2I mouse, int buttons, int eventButton) {

		focusedComponent.onMouseButtonUp(mouse.sub(focusedComponent.getRealLocation()), buttons, eventButton);
	}


	protected void eventMouseMove(PC_Vec2I mouse, int buttons) {

		checkMouseOverComponent(mouse, buttons);
		mouseOverComponent.onMouseMove(mouse.sub(mouseOverComponent.getRealLocation()), buttons);

	}


	protected void eventMouseWheel(PC_Vec2I mouse, int buttons, int wheel) {

		focusedComponent.onMouseWheel(mouse.sub(focusedComponent.getRealLocation()), buttons, wheel);
	}


	@Override
	protected void notifyChange() {

		updateLayout();
	}


	public void setFokus(PC_GresComponent focusedComponent) {
		if(this.focusedComponent != focusedComponent){
			if(this.focusedComponent!=null){
				this.focusedComponent.onFokusLost();
			}
			this.focusedComponent = focusedComponent;
			if(focusedComponent!=null){
				focusedComponent.onFokusGot();
			}
		}
	}

}
