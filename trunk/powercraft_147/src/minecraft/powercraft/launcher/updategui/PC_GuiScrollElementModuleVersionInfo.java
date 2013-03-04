package powercraft.launcher.updategui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import powercraft.launcher.PC_LauncherClientUtils;
import powercraft.launcher.PC_UpdateXMLFile.XMLVersionTag;
import powercraft.launcher.updategui.PC_GuiScroll.ScrollElement;

public class PC_GuiScrollElementModuleVersionInfo extends ScrollElement{

	private XMLVersionTag version;
	public PC_GuiScroll scroll;
	private static Minecraft mc = PC_LauncherClientUtils.mc();
	
	public PC_GuiScrollElementModuleVersionInfo(XMLVersionTag version){
		this.version = version;
		ScaledResolution resolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		scroll = new PC_GuiScroll(210, 20, resolution.getScaledWidth()-220, resolution.getScaledHeight()-80);
		scroll.add(new PC_GuiScrollElementText(version.getInfo()));
	}

	public XMLVersionTag getVersion(){
		return version;
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawString(fontRenderer, "v"+version.getVersion(), 2, 2, 0xffffff);
	}
	
	@Override
	public int getHeight() {
		return 14;
	}
	
	@Override
	public boolean showSelection() {
		return true;
	}
	
	
}
