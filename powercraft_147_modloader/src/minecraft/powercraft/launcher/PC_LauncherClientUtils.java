package powercraft.launcher;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;

public class PC_LauncherClientUtils extends PC_LauncherUtils {

	public static Minecraft mc() {
		return ModLoader.getMinecraftInstance();
	}

}
