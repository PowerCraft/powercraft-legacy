package powercraft.api.gres;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

import org.lwjgl.input.Mouse;

import powercraft.api.PC_Vec2I;


public class PC_GresContainerGui extends GuiContainer implements PC_IGresGui {

	private PC_GresGuiHandler guiHandler;
	private int buttons;
	private int lastEventButton;
	private long holdTime;


	public PC_GresContainerGui(PC_IGresClient gui) {

		super((PC_GresBaseWithInventory) gui);
		guiHandler = new PC_GresGuiHandler(this, gui);
	}


	@Override
	public void initGui() {

		xSize = width;
		ySize = height;
		super.initGui();
		guiHandler.eventInitGui();
	}


	@Override
	public void drawScreen(int mouseX, int mouseY, float timeStamp) {

		guiHandler.eventDrawScreen(new PC_Vec2I(mouseX, mouseY), timeStamp);
	}


	@Override
	protected void keyTyped(char key, int keyCode) {

		guiHandler.eventKeyTyped(key, keyCode);
	}


	@Override
	public void updateScreen() {

		guiHandler.eventUpdateScreen();
	}


	@Override
	protected void drawGuiContainerBackgroundLayer(float timeStamp, int mouseX, int mouseY) {

	}


	@Override
	public Minecraft getMinecraft() {

		return mc;
	}


	@Override
	public PC_Vec2I getSize() {

		return new PC_Vec2I(width, height);
	}


	private void eventMouseButtonDown(PC_Vec2I mouse, int buttons, int eventButton) {

		guiHandler.eventMouseButtonDown(mouse, buttons, eventButton);
		mouseClicked(mouse.x, mouse.y, eventButton);
		holdTime = Minecraft.getSystemTime();
		lastEventButton = eventButton;
	}


	private void eventMouseButtonUp(PC_Vec2I mouse, int buttons, int eventButton) {

		guiHandler.eventMouseButtonUp(mouse, buttons, eventButton);
		mouseMovedOrUp(mouse.x, mouse.y, eventButton);
		if (lastEventButton == eventButton) {
			lastEventButton = -1;
		}
	}


	private void eventMouseMove(PC_Vec2I mouse, int buttons) {

		guiHandler.eventMouseMove(mouse, buttons);
		if (lastEventButton != -1) {
			mouseClickMove(mouse.x, mouse.y, lastEventButton, Minecraft.getSystemTime() - holdTime);
		}
	}


	private void eventMouseWheel(PC_Vec2I mouse, int buttons, int wheel) {

		guiHandler.eventMouseWheel(mouse, buttons, wheel);
	}


	@Override
	public void handleMouseInput() {

		int x = Mouse.getEventX() * width / mc.displayWidth;
		int y = height - Mouse.getEventY() * height / mc.displayHeight - 1;
		PC_Vec2I mouse = new PC_Vec2I(x, y);
		int eventButton = Mouse.getEventButton();
		int eventWheel = Mouse.getEventDWheel();
		if (Mouse.getEventDX() != 0 || Mouse.getEventDY() != 0) {
			eventMouseMove(mouse, buttons);
		}
		if (eventButton != -1) {
			if (Mouse.getEventButtonState()) {
				buttons |= 1 << eventButton;
				eventMouseButtonDown(mouse, buttons, eventButton);
			} else {
				buttons &= ~(1 << eventButton);
				eventMouseButtonUp(mouse, buttons, eventButton);
			}
		}
		if (eventWheel != 0) {
			eventMouseWheel(mouse, buttons, eventWheel>0?1:-1);
		}
	}


	@Override
	public void drawMouseItemStack(PC_Vec2I mouse, float timeStamp) {

		super.drawScreen(mouse.x, mouse.y, timeStamp);
	}


	@Override
	protected void drawSlotInventory(Slot slot) {

		if (slot.xDisplayPosition != -10000 && slot.yDisplayPosition != -10000) {
			super.drawSlotInventory(slot);
		}
	}


	@Override
	public void drawDefaultBackground() {

	}

}
