package powercraft.launcher.updategui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import powercraft.launcher.PC_LauncherClientUtils;
import powercraft.launcher.update.PC_UpdateXMLFile.XMLPackTag;
import powercraft.launcher.update.PC_UpdateXMLFile.XMLVersionTag;
import powercraft.launcher.updategui.PC_GuiScroll.ScrollElement;

public class PC_GuiScrollElementPack extends ScrollElement {

	private XMLPackTag updatePackInfo;
	public PC_GuiScroll scroll;
	private Minecraft mc = PC_LauncherClientUtils.mc();
	
	public PC_GuiScrollElementPack(XMLPackTag updatePackInfo) {
		this.updatePackInfo = updatePackInfo;
		ScaledResolution resolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		scroll = new PC_GuiScroll(110, 20, 100, resolution.getScaledHeight()-50);
		for(XMLVersionTag versionTag : updatePackInfo.getVersions()){
			scroll.add(new PC_GuiScrollElementPackVersionInfo(versionTag, updatePackInfo));
		}
	}

	public XMLPackTag getUpdateInfo() {
		return updatePackInfo;
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawString(fontRenderer, updatePackInfo.getName(), 2, 2, 0xffffff);
	}
	
	@Override
	public boolean showSelection() {
		return true;
	}

	@Override
	public int getHeight() {
		return 14;
	}

}
