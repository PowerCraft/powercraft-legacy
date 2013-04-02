package powercraft.api.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ModLoader;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.annotation.PC_Shining;
import powercraft.api.block.PC_Block;
import powercraft.api.block.PC_ItemBlock;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.tileentity.PC_ITileEntityRenderer;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_GlobalVariables;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;
import powercraft.launcher.PC_Property;
import powercraft.launcher.loader.PC_ModuleObject;

public final class PC_BlockRegistry {
	
	protected static TreeMap<String, PC_Block> blocks = new TreeMap<String, PC_Block>();
	
	public static <T extends PC_Block> T register(PC_ModuleObject module, Class<T> blockClass, Class<? extends PC_ItemBlock> itemBlockClass,
			Class<? extends PC_TileEntity> tileEntityClass) {
		final PC_Property config = module.getConfig().getProperty(blockClass.getSimpleName(), null, null);
		try {
			
			if (!config.getBoolean("enabled", true)) {
				return null;
			}
			
			PC_Block block;
			PC_Block blockOff;
			
			if (blockClass.isAnnotationPresent(PC_Shining.class)) {
				int idOn = config.getInt("defaultID.on", -1);
				if (idOn == -1) {
					idOn = getFreeBlockID();
					config.setInt("defaultID.on", idOn);
				}
				if (!isBlockIDFree(idOn)) {
					idOn = getFreeBlockID();
				}
				block = PC_ReflectHelper.create(blockClass, idOn, true);
				int idOff = config.getInt("defaultID.off", -1);
				if (idOff == -1) {
					idOff = getFreeBlockID();
					config.setInt("defaultID.off", idOff);
				}
				if (!isBlockIDFree(idOff)) {
					idOff = getFreeBlockID();
				}
				blockOff = PC_ReflectHelper.create(blockClass, idOff, false);
				PC_ReflectHelper.setFieldsWithAnnotationTo(blockClass, blockClass, PC_Shining.ON.class, block);
				PC_ReflectHelper.setFieldsWithAnnotationTo(blockClass, blockClass, PC_Shining.OFF.class, blockOff);
				blockOff.getIndirectPowerOutput(blockClass.getSimpleName());
				blockOff.setModule(module);
				blocks.put(blockClass.getSimpleName() + ".Off", blockOff);
				registerBlock(blockOff, null);
				ItemBlock itemBlock = (ItemBlock) Item.itemsList[blockOff.blockID];
				blockOff.setItemBlock(itemBlock);
			} else {
				int id = config.getInt("defaultID", -1);
				if (id == -1) {
					id = getFreeBlockID();
					config.setInt("defaultID", id);
				}
				if (!isBlockIDFree(id)) {
					id = getFreeBlockID();
				}
				block = PC_ReflectHelper.create(blockClass, id);
			}
			
			blocks.put(blockClass.getSimpleName(), block);
			block.getIndirectPowerOutput(blockClass.getSimpleName());
			block.setModule(module);
			block.initConfig(config);
			
			if (itemBlockClass == null) {
				itemBlockClass = PC_ItemBlock.class;
			}
			registerBlock(block, itemBlockClass);
			
			PC_ItemBlock itemBlock = (PC_ItemBlock) Item.itemsList[block.blockID];
			
			block.setItemBlock(itemBlock);
			
			itemBlock.setModule(module);
			List<LangEntry> l = itemBlock.getNames(new ArrayList<LangEntry>());
			if (l != null) {
				PC_LangRegistry.registerLanguage(module, l.toArray(new LangEntry[0]));
			}
			
			if (tileEntityClass != null) {
				if (PC_ITileEntityRenderer.class.isAssignableFrom(tileEntityClass))
					PC_RegistryServer.getInstance().tileEntitySpecialRenderer(tileEntityClass);
				else
					ModLoader.registerTileEntity(tileEntityClass, tileEntityClass.getName());
			}
			
			return (T) block;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void registerBlock(PC_Block block, Class<? extends ItemBlock> itemBlock) {
		if (itemBlock == null)
			ModLoader.registerBlock(block);
		else
			ModLoader.registerBlock(block, itemBlock);
	}
	
	public static <T extends PC_Block> T register(PC_ModuleObject module, Class<T> blockClass) {
		
		Class<? extends PC_ItemBlock> itemBlockClass = null;
		Class<? extends PC_TileEntity> tileEntityClass = null;
		
		PC_BlockInfo blockInfo = PC_ReflectHelper.getAnnotation(blockClass, PC_BlockInfo.class);
		
		if (blockInfo != null) {
			
			if (blockInfo.itemBlock() != PC_BlockInfo.PC_FakeItemBlock.class) {
				itemBlockClass = blockInfo.itemBlock();
			}
			
			if (blockInfo.tileEntity() != PC_BlockInfo.PC_FakeTileEntity.class) {
				tileEntityClass = blockInfo.tileEntity();
			}
			
		}
		
		return register(module, blockClass, itemBlockClass, tileEntityClass);
	}
	
	public static int getFreeBlockID() {
		for (int i = PC_GlobalVariables.blockStartIndex; i < Block.blocksList.length; i++) {
			if (Block.blocksList[i] == null)
				return i;
		}
		for (int i = 1; i < PC_GlobalVariables.blockStartIndex && i < Block.blocksList.length; i++) {
			if (Block.blocksList[i] == null)
				return i;
		}
		return -1;
	}
	
	public static boolean isBlockIDFree(int id) {
		if (id <= 0)
			return false;
		return Block.blocksList[id] == null;
	}
	
	public static PC_Block getPCBlockByName(String name) {
		if (blocks.containsKey(name)) {
			return blocks.get(name);
		}
		return null;
	}
	
	public static int getPCBlockIDByName(String name) {
		if (blocks.containsKey(name)) {
			return blocks.get(name).blockID;
		}
		return 0;
	}
	
	public static TreeMap<String, PC_Block> getPCBlocks() {
		return new TreeMap<String, PC_Block>(blocks);
	}
	
	public static boolean isBlock(IBlockAccess world, PC_VecI pos, String... names) {
		Block block = PC_Utils.getBlock(world, pos);
		if (block instanceof PC_Block) {
			for (String name : names)
				if (block == getPCBlockByName(name))
					return true;
		}
		return false;
	}
	
}
