package powercraft.api.registry;

import net.minecraft.src.Icon;
import powercraft.api.block.PC_Block;
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
	
	public static String getTextureName(PC_ModuleObject module, String texuteName) {
		return "PowerCraft-" + module.getModuleName() + "-" + texuteName;
	}
	
	public static void onIconLoading(PC_Block block, Object iconRegister) {
		PC_RegistryServer.getInstance().onIconLoading(block, iconRegister);
	}
	
	public static Icon registerIcon(PC_ModuleObject module, String texture) {
		return PC_RegistryServer.getInstance().registerIcon(getTextureName(module, texture));
	}
	
}
