package powercraft.api;


import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import powercraft.api.blocks.PC_IBlock;
import powercraft.api.items.PC_Item;
import powercraft.core.PC_Core;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class PC_ClientRegistry extends PC_Registry {

	private static IconRegister iconRegistry;
	private static String moduleName;
	private static String objectName;

	public static void registerIcons(PC_IBlock block, IconRegister iconRegistry) {

		PC_ClientRegistry.iconRegistry = iconRegistry;
		PC_Module module = block.getModule();
		moduleName = module.getMetadata().modId;
		objectName = block.getBlockInfo().blockid();
		block.loadIcons();
		PC_ClientRegistry.iconRegistry = null;
		moduleName = null;
		objectName = null;
	}


	public static void registerIcons(PC_Item item, IconRegister iconRegistry) {

		PC_ClientRegistry.iconRegistry = iconRegistry;
		PC_Module module = item.getModule();
		moduleName = module.getMetadata().modId;
		objectName = item.getItemInfo().itemid();
		item.loadIcons();
		PC_ClientRegistry.iconRegistry = null;
		moduleName = null;
		objectName = null;
	}


	public static Icon registerIcon(String icon) {

		return registerIcon(icon, objectName);
	}


	public static Icon registerIcon(String icon, String objectName) {

		if (icon.equals("DefaultMaschineTexture")) {
			return iconRegistry.registerIcon(PC_Core.instance.getMetadata().modId + ":maschineDefault/DefaultMaschineTexture");
		}
		return iconRegistry.registerIcon(moduleName + ":" + objectName + "/" + icon);
	}

	@Override
	protected void iRegisterLanguage(String key, String value){
		LanguageRegistry.instance().addStringLocalization(key, value);
	}
	
}
