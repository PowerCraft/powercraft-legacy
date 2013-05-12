package powercraft.api.hooks;

import powercraft.launcher.updategui.PC_GuiScroll;

public class PC_GuiModScroll extends PC_GuiScroll {
	
	private int activeElement = -1;
	
	public PC_GuiModScroll(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	@Override
	public int getElementCount() {
		return PC_ModInfo.modInfos.size();
	}
	
	@Override
	public int getElementHeight(int element) {
		return 22;
	}
	
	@Override
	public boolean isElementActive(int element) {
		return activeElement == element;
	}
	
	@Override
	public void drawElement(int element, int par1, int par2, float par3) {
		PC_ModInfo modInfo = PC_ModInfo.modInfos.get(element);
		drawString(fontRenderer, modInfo.name, 2, 2, 0xffffff);
		String version = modInfo.version;
		int versionWidth = fontRenderer.getStringWidth(version);
		drawString(fontRenderer, version, 2, 12, 0xdddddd);
	}
	
	@Override
	public void clickElement(int element, int par1, int par2, int par3) {
		activeElement = element;
	}
	
	public PC_ModInfo getActiveModInfo() {
		if (activeElement == -1)
			return null;
		return PC_ModInfo.modInfos.get(activeElement);
	}
	
}
