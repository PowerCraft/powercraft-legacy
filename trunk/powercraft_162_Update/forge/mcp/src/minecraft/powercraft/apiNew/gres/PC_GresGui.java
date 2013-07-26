package powercraft.api.gres;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Mouse;

import powercraft.apiOld.PC_Vec2I;


public class PC_GresGui extends GuiScreen implements PC_IGresGui {

	private PC_GresGuiHandler guiHandler;
	private int buttons;
	private int wheel;


	public PC_GresGui(PC_IGresClient client) {

		guiHandler = new PC_GresGuiHandler(this, client);
	}


	@Override
	public void initGui() {

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
	public Minecraft getMinecraft() {

		return mc;
	}


	@Override
	public PC_Vec2I getSize() {

		return new PC_Vec2I(width, height);
	}


	private void eventMouseButtonDown(PC_Vec2I mouse, int buttons, int eventButton) {

		guiHandler.eventMouseButtonDown(mouse, buttons, eventButton);
	}


	private void eventMouseButtonUp(PC_Vec2I mouse, int buttons, int eventButton) {

		guiHandler.eventMouseButtonUp(mouse, buttons, eventButton);
	}


	private void eventMouseMove(PC_Vec2I mouse, int buttons) {

		guiHandler.eventMouseMove(mouse, buttons);
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
		if (eventWheel != wheel) {
			eventMouseWheel(mouse, buttons, wheel - eventWheel);
			wheel = eventWheel;
		}
	}


	@Override
	public void drawMouseItemStack(PC_Vec2I mouse, float timeStamp) {

	}


	@Override
	public boolean doesGuiPauseGame() {

		return false;
	}

}
