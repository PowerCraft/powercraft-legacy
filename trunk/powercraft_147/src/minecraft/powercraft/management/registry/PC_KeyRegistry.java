package powercraft.management.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.launcher.PC_Property;

public final class PC_KeyRegistry {

	protected static HashMap<EntityPlayer, List<String>> keyPressed = new HashMap<EntityPlayer, List<String>>();
	protected static int keyReverse;
	
	public static boolean isPlacingReversed(EntityPlayer player) {
		return isKeyPressed(player, "keyReverse");
	}
	
	public static boolean isKeyPressed(EntityPlayer player, String key) {
		if (!keyPressed.containsKey(player)) {
			return false;
		}

		List<String> keyList = keyPressed.get(player);
		return keyList.contains(key);
	}

	public static void watchForKey(String name, int key) {
		PC_RegistryServer.getInstance().watchForKey(name, key);
	}

	public static int watchForKey(PC_Property config, String name, int key,
			String... info) {
		key = config.getInt("key." + name, key, info);
		watchForKey(name, key);
		return key;
	}
	
	public static void setReverseKey(PC_Property config) {
		keyReverse = watchForKey(config, "keyReverse", 29, "Key for rotate placing");
	}

	protected static void onKeyEvent(EntityPlayer player, Boolean state, String key) {
		List<String> keyList;
		if (keyPressed.containsKey(player)) {
			keyList = keyPressed.get(player);
		} else {
			keyPressed.put(player, keyList = new ArrayList<String>());
		}

		if (state) {
			if (!keyList.contains(key)) {
				keyList.add(key);
			}
		} else {
			if (keyList.contains(key)) {
				keyList.remove((Object) key);
			}
		}
	}
	
}
