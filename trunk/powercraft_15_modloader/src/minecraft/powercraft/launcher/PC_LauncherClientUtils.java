package powercraft.launcher;

import java.io.File;

import net.minecraft.client.Minecraft;
import powercraft.launcher.update.PC_UpdateManager;
import powercraft.launcher.updategui.PC_GuiUpdate;

public class PC_LauncherClientUtils extends PC_LauncherUtils {
	
	public static Minecraft mc() {
		return Minecraft.getMinecraft();
	}
	
	@Override
	public void pLookForUpdates(){
		PC_UpdateManager.lookForUpdates();
	}
	
	@Override
	public void pOpenUpdateGui(boolean requestDownloadTarget){
		PC_GuiUpdate.show(requestDownloadTarget);
	}
	
	@Override
	protected boolean pIsClient() {
		return true;
	}
	
	@Override
	protected File pGetMCDirectory() {
		return mc().getMinecraftDir();
	}
	
}
