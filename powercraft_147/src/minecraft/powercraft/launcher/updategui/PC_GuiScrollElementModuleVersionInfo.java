package powercraft.launcher.updategui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import powercraft.launcher.PC_LauncherClientUtils;
import powercraft.launcher.PC_Version;
import powercraft.launcher.loader.PC_ModuleVersion;
import powercraft.launcher.update.PC_UpdateManager.ModuleUpdateInfo;
import powercraft.launcher.update.PC_UpdateXMLFile.XMLVersionTag;
import powercraft.launcher.updategui.PC_GuiScroll.ScrollElement;

public class PC_GuiScrollElementModuleVersionInfo extends ScrollElement{

	private XMLVersionTag version;
	private PC_ModuleVersion moduleVersion;
	private ModuleUpdateInfo updateInfo;
	public PC_GuiScroll scroll;
	private static Minecraft mc = PC_LauncherClientUtils.mc();
	
	public PC_GuiScrollElementModuleVersionInfo(PC_ModuleVersion moduleVersion, XMLVersionTag version, ModuleUpdateInfo updateInfo){
		this.version = version;
		this.moduleVersion = moduleVersion;
		this.updateInfo = updateInfo;
		ScaledResolution resolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		scroll = new PC_GuiScroll(210, 20, resolution.getScaledWidth()-220, resolution.getScaledHeight()-80);
		if(version!=null){
			scroll.add(new PC_GuiScrollElementText(version.getInfo()));
		}else{
			scroll.add(new PC_GuiScrollElementText("No info for module"));
		}
	}

	public PC_Version getVersion(){
		if(version!=null)
			return version.getVersion();
		return moduleVersion.getVersion();
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawString(fontRenderer, "v"+getVersion()+getAdditional(), 2, 2, 0xffffff);
	}
	
	private String getAdditional(){
		if(updateInfo.oldVersion==null){
			return " *NEW*";
		}else if(updateInfo.oldVersion.equals(getVersion())){
			return " (Active)";
		}else if(moduleVersion==null && updateInfo.oldVersion.compareTo(getVersion())<0){
			return " *NEW*";
		}
		return "";
	}

	@Override
	public int getHeight() {
		return 14;
	}
	
	@Override
	public boolean showSelection() {
		return true;
	}

	public XMLVersionTag getVersionXML() {
		return version;
	}
	
	
}
