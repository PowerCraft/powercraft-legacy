package powercraft.api.registries;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Configuration;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.apiOld.PC_Logger;
import powercraft.apiOld.PC_Module;
import powercraft.apiOld.PC_Security;
import powercraft.apiOld.blocks.PC_ITileEntitySpecialRenderer;
import powercraft.apiOld.blocks.PC_TileEntity;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PC_BlockRegistry {

	@SideOnly(Side.CLIENT)
	protected static class PC_TileEntitySpecialRenderer extends TileEntitySpecialRenderer{

		@Override
		public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float timeStamp) {
			if(tileEntity instanceof PC_ITileEntitySpecialRenderer){
				GL11.glPushMatrix();
				GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
				((PC_ITileEntitySpecialRenderer)tileEntity).renderTileEntityAt(timeStamp);
				GL11.glPopMatrix();
			}
		}
		
		public void bindTexture(ResourceLocation texture){
			func_110628_a(texture);
		}
		
	}
	
	@SideOnly(Side.CLIENT)
	private static PC_TileEntitySpecialRenderer specialRenderer;
	
	@SideOnly(Side.CLIENT)
	protected static PC_TileEntitySpecialRenderer getSpecialRenderer(){
		if(specialRenderer==null)
			specialRenderer = new PC_TileEntitySpecialRenderer();
		return specialRenderer;
	}
	
	public static Block registerBlock(PC_Module module, Class<? extends Block> clazz) {

		if(!PC_Security.allowedCallerNoException(PC_Module.class)){
			PC_Logger.warning("PC_BlockRegistry.registerBlock shouldn't be called. Use instead @PC_FieldGenerator");
		}
		PC_ModuleRegistry.setActiveModule(module);
		Configuration config = module.getConfig();
		PC_BlockInfo blockInfo = clazz.getAnnotation(PC_BlockInfo.class);
		int blockID = config.getBlock(blockInfo.blockid(), blockInfo.defaultid()).getInt();
		if (blockID == -1) {
			PC_Logger.info("Block %s disabled", blockInfo.name());
			return null;
		}
		try {
			Block block = clazz.getConstructor(int.class).newInstance(blockID);
			block.setUnlocalizedName(clazz.getSimpleName());
			PC_LanguageRegistry.registerLanguage(block.getUnlocalizedName()+".name", blockInfo.name());
			GameRegistry.registerBlock(block, blockInfo.itemBlock(), blockInfo.blockid());
			Class<? extends PC_TileEntity> tileEntity = blockInfo.tileEntity();
			if (tileEntity != PC_TileEntity.class) {
				PC_Registry.sidedRegistry.registerTileEntity(tileEntity);
			}
			PC_ModuleRegistry.releaseActiveModule();
			return block;
		} catch (Exception e) {
			e.printStackTrace();
			PC_Logger.severe("Failed to generate block %s", blockInfo.name());
		}
		PC_ModuleRegistry.releaseActiveModule();
		return null;
	}
	
}
