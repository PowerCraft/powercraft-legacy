package powercraft.launcher.loader;

import powercraft.launcher.PC_LauncherUtils;

public enum PC_ModLoader {
	
	RISUGAMIS_MODLOADER("ModLoader"), FORGE_MODLOADER("Forge"), ALL(null);
	
	private final String name;
	
	PC_ModLoader(String name) {
		this.name = name;
	}
	
	public String getName() {
		if (name == null)
			return PC_LauncherUtils.getModLoader().getName();
		return name;
	}
	
}
