package powercraft.api.registry;


public final class PC_TextureRegistry {

	public static void registerTexture(String texture) {
		PC_RegistryServer.getInstance().registerTexture(texture);
	}
	
	public static String getPowerCraftImageDir() {
		return "/textures/";
	}

	public static String getGresImgDir() {
		return getPowerCraftImageDir() + "gres/";
	}
	
}
