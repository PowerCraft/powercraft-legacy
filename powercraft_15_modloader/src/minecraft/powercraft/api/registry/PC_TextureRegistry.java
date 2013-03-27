package powercraft.api.registry;

import powercraft.launcher.loader.PC_ModuleObject;


public final class PC_TextureRegistry {

	public static void registerTexture(String texture) {
		PC_RegistryServer.getInstance().registerTexture(texture);
	}
	
	public static String getPowerCraftImageDir() {
		return "/textures/";
	}

	public static String getGresImgDir() {
		return getPowerCraftImageDir() + "PowerCraft-Api-gres/";
	}
	
	public static String getTextureName(PC_ModuleObject module, String texuteName){
		return "PowerCraft-"+module.getModuleName()+"-"+texuteName;
	}
	
}
