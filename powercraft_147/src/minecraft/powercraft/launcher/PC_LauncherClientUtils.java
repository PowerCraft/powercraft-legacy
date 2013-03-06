package powercraft.launcher;

import java.io.File;
import java.util.HashMap;

import powercraft.launcher.loader.PC_ModuleObject;
import powercraft.launcher.update.PC_UpdateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;

public class PC_LauncherClientUtils extends PC_LauncherUtils {
	
	public static Minecraft mc() {
		return Minecraft.getMinecraft();
	}

	@Override
	public void updatePowerCraft() {
		PC_UpdateManager.startUpdateInfoDownload();
		File moduleFiles = PC_LauncherUtils.getPowerCraftModuleFile();
		HashMap<String, PC_ModuleObject> modules = searchModules(false).getModules();
		PC_UpdateManager.moduleInfos(modules);
	}
	
	@Override
	protected boolean pIsClient(){
		return true;
	}

	@Override
	protected File pGetMCDirectory() {
		return mc().getMinecraftDir();
	}
	
}
