package powercraft.api.registries;

import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import powercraft.api.PC_Renderer;
import powercraft.api.blocks.PC_ITileEntitySpecialRenderer;
import powercraft.api.blocks.PC_TileEntity;
import powercraft.api.items.PC_Item;
import powercraft.core.PCco_ModuleCore;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
class PC_SidedRegistryClient extends PC_SidedRegistry {
	
	protected PC_SidedRegistryClient(){}

	@Override
	protected void registerLanguage(String key, String value) {
		LanguageRegistry.instance().addStringLocalization(key, value);
	}

	@Override
	protected Icon registerIcon(String icon, String objectName) {

		if (icon.equals("DefaultMachineTexture")) {
			return PC_TextureRegistry.iconRegistry.registerIcon(PCco_ModuleCore.instance.getMetadata().modId + ":machineDefault/DefaultMachineTexture");
		}
		return PC_TextureRegistry.iconRegistry.registerIcon(PC_ModuleRegistry.getActiveModule().getMetadata().modId + ":" + objectName + "/" + icon);
	}
	
	@Override
	protected void registerTileEntity(Class<? extends PC_TileEntity> tileEntity) {
		if(PC_ITileEntitySpecialRenderer.class.isAssignableFrom(tileEntity)){
			ClientRegistry.registerTileEntity(tileEntity, tileEntity.getName(), PC_BlockRegistry.getSpecialRenderer());
		}else{
			GameRegistry.registerTileEntity(tileEntity, tileEntity.getName());
		}
	}
	
	@Override
	protected void bindTexture(ResourceLocation texture) {
	//	PC_BlockRegistry.getSpecialRenderer().bindTexture(texture);
	}
	
	@Override
	protected void registerItemRenderer(PC_Item item) {
		MinecraftForgeClient.registerItemRenderer(item.itemID, PC_Renderer.getRenderer());
	}
	
}
