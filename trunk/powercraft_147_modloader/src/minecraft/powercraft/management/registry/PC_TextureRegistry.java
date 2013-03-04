package powercraft.management.registry;

import powercraft.launcher.PC_ModuleObject;

public final class PC_TextureRegistry {

	public static void registerTexture(String texture) {
		PC_RegistryServer.getInstance().registerTexture(texture);
	}
	
	public static void registerTextureFiles(String... textureFiles) {
		for(String texture:textureFiles){
			registerTexture(texture);
		}
	}
	
	public static String getPowerCraftLoaderImageDir() {
		return "/powercraft/management/textures/";
	}

	public static String getGresImgDir() {
		return getPowerCraftLoaderImageDir() + "gres/";
	}

	public static String getTextureDirectory(PC_ModuleObject module) {
		return "/powercraft/" + module.getName().toLowerCase()
				+ "/textures/";
	}

	public static String getTerrainFile(PC_ModuleObject module) {
		return getTextureDirectory(module)
				+ "tiles.png";
	}
	
}
