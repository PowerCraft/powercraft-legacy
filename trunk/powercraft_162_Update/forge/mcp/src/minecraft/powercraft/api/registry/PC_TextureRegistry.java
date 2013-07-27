package powercraft.api.registry;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import powercraft.api.PC_Security;
import powercraft.api.block.PC_Block;
import powercraft.api.block.PC_BlockWithoutTileEntity;
import powercraft.api.block.PC_IBlock;
import powercraft.api.item.PC_Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PC_TextureRegistry {

	@SideOnly(Side.CLIENT)
	protected static IconRegister iconRegistry;
	private static String objectName;
	
	@SideOnly(Side.CLIENT)
	public static void registerIcons(PC_IBlock block, IconRegister iconRegistry) {

		if(PC_Security.allowedCaller("PC_TextureRegistry.registerIcons(PC_IBlock block, IconRegister iconRegistry)", PC_Block.class, PC_BlockWithoutTileEntity.class)){
			PC_ModuleRegistry.setActiveModule(block.getModule());
			PC_TextureRegistry.iconRegistry = iconRegistry;
			objectName = block.getBlockInfo().blockid();
			block.loadIcons();
			PC_TextureRegistry.iconRegistry = null;
			objectName = null;
			PC_ModuleRegistry.releaseActiveModule();
		}
	}

	@SideOnly(Side.CLIENT)
	public static void registerIcons(PC_Item item, IconRegister iconRegistry) {

		if(PC_Security.allowedCaller("PC_TextureRegistry.registerIcons(PC_Item item, IconRegister iconRegistry)", PC_Item.class)){
			PC_ModuleRegistry.setActiveModule(item.getModule());
			PC_TextureRegistry.iconRegistry = iconRegistry;
			objectName = item.getItemInfo().itemid();
			item.loadIcons();
			PC_TextureRegistry.iconRegistry = null;
			objectName = null;
			PC_ModuleRegistry.releaseActiveModule();
		}
	}


	public static Icon registerIcon(String icon) {

		return registerIcon(icon, objectName);
	}


	public static Icon registerIcon(String icon, String objectName) {

		return PC_Registry.sidedRegistry.registerIcon(icon, objectName);
	}

	public static void bindTexture(ResourceLocation texture) {
		PC_Registry.sidedRegistry.bindTexture(texture);
	}
	
}
