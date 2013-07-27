package powercraft.api.registry;

import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import powercraft.api.block.PC_ITileEntitySpecialRenderer;
import powercraft.api.block.PC_TileEntity;
import powercraft.core.PC_Core;
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

		if (icon.equals("DefaultMaschineTexture")) {
			return PC_TextureRegistry.iconRegistry.registerIcon(PC_Core.instance.getMetadata().modId + ":maschineDefault/DefaultMaschineTexture");
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
		PC_BlockRegistry.getSpecialRenderer().bindTexture(texture);
	}
	
}
