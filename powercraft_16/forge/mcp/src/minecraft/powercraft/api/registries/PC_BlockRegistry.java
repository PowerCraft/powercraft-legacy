package powercraft.api.registries;

import net.minecraft.block.Block;
import net.minecraftforge.common.Configuration;
import powercraft.api.PC_Logger;
import powercraft.api.PC_Module;
import powercraft.api.PC_Security;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.blocks.PC_TileEntity;
import cpw.mods.fml.common.registry.GameRegistry;

public class PC_BlockRegistry {

	public static Block registerBlock(PC_Module module, Class<? extends Block> clazz) {

		if(!PC_Security.allowedCallerNoException(PC_Module.class)){
			PC_Logger.warning("PC_BlockRegistry.registerBlock shouln't be called. Use instead @PC_FieldGenerator");
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
				GameRegistry.registerTileEntity(tileEntity, tileEntity.getName());
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
