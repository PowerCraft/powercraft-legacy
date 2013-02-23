package powercraft.management.registry;

import powercraft.management.PC_GlobalVariables;
import powercraft.management.PC_Utils.GameInfo;

public final class PC_SoundRegistry {

	public static boolean isSoundEnabled() {
		if (GameInfo.isServer()) {
			return false;
		}
		return PC_GlobalVariables.soundEnabled;
	}
	
	public static void playSound(double x, double y, double z, String sound, float soundVolume, float pitch) {
		if (isSoundEnabled()) {
			PC_RegistryServer.getInstance().playSound(x, y, z, sound, soundVolume, pitch);
		}
	}
	
}
