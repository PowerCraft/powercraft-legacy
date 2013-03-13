package powercraft.api.registry;

import powercraft.launcher.loader.PC_ModuleObject;

public final class PC_TextureRegistry {

	public static void registerTexture(String texture) {
		PC_RegistryServer.getInstance().registerTexture(texture);
	}
	
	public static String getPowerCraftLoaderImageDir() {
		return "/powercraft/api/textures/";
	}

	public static String getGresImgDir() {
		return getPowerCraftLoaderImageDir() + "gres/";
	}

	public static String getTextureDirectory(PC_ModuleObject module) {
		return "powercraft/" + module.getModuleName().toLowerCase()
				+ "/textures";
	}

	public static String getItemAndBlockTextureDirectory(PC_ModuleObject module) {
		return "powercraft/" + module.getModuleName().toLowerCase();
	}
	
	public static String getTerrainFile() {
		return "tiles.png";
	}
	
}
