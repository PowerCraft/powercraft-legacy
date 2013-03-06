package powercraft.launcher.updategui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import powercraft.launcher.PC_LauncherClientUtils;
import powercraft.launcher.PC_Version;
import powercraft.launcher.loader.PC_ModuleVersion;
import powercraft.launcher.update.PC_UpdateManager.ModuleUpdateInfo;
import powercraft.launcher.update.PC_UpdateXMLFile.XMLVersionTag;
import powercraft.launcher.updategui.PC_GuiScroll.ScrollElement;

public class PC_GuiScrollElementModule extends ScrollElement {
	
	private String moduleName;
	private PC_Version currentVersion;
	public PC_GuiScroll scroll;
	private Minecraft mc = PC_LauncherClientUtils.mc();
	private ModuleUpdateInfo updateInfo;
	
	public PC_GuiScrollElementModule(ModuleUpdateInfo updateInfo) {
		this.updateInfo = updateInfo;
		moduleName = updateInfo.xmlModule.getName();
		currentVersion = updateInfo.oldVersion;
		ScaledResolution resolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		scroll = new PC_GuiScroll(110, 20, 100, resolution.getScaledHeight()-50);
		List<PC_Version> versionList = new ArrayList<PC_Version>();
		for(XMLVersionTag versionTag : updateInfo.xmlModule.getVersions()){
			if(!versionList.contains(versionTag.getVersion()))
				versionList.add(versionTag.getVersion());
		}
		for(PC_ModuleVersion moduleVersion : updateInfo.module.getVersions()){
			if(!versionList.contains(moduleVersion.getVersion()))
				versionList.add(moduleVersion.getVersion());
		}
		PC_Version[] versions = versionList.toArray(new PC_Version[0]);
		Arrays.sort(versions);
		for(int i=versions.length-1; i>=0; i--){
			PC_Version version = versions[i];
			PC_ModuleVersion moduleVersion = updateInfo.module.getVersion(version);
			XMLVersionTag versionTag = updateInfo.xmlModule.getVersion(version);
			scroll.add(new PC_GuiScrollElementModuleVersionInfo(moduleVersion, versionTag, updateInfo));
		}
	}

	public ModuleUpdateInfo getUpdateInfo(){
		return updateInfo;
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawString(fontRenderer, moduleName, 2, 2, 0xffffff);
		if(currentVersion==null){
			drawString(fontRenderer, "*NEW*", 2, 12, 0xffffff);
		}else{
			drawString(fontRenderer, currentVersion.toString(), 2, 12, 0xffffff);
		}
	}

	@Override
	public int getHeight() {
		return 24;
	}

	@Override
	public boolean showSelection() {
		return true;
	}
	
}
