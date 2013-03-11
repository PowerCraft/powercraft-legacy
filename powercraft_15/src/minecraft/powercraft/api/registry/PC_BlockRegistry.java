package powercraft.api.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.IBlockAccess;
import powercraft.launcher.PC_Property;
import powercraft.launcher.mod_PowerCraft;
import powercraft.launcher.loader.PC_ModuleObject;
import powercraft.api.PC_GlobalVariables;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.PC_VecI;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.annotation.PC_Shining;
import powercraft.api.block.PC_Block;
import powercraft.api.block.PC_BlockOre;
import powercraft.api.block.PC_ItemBlock;
import powercraft.api.reflect.PC_FieldWithAnnotation;
import powercraft.api.reflect.PC_IFieldAnnotationIterator;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.tileentity.PC_ITileEntityRenderer;
import powercraft.api.tileentity.PC_TileEntity;
import cpw.mods.fml.common.registry.GameRegistry;

public final class PC_BlockRegistry {

	protected static TreeMap<String, PC_Block> blocks = new TreeMap<String, PC_Block>();
	
	public static <T extends PC_Block> T register(PC_ModuleObject module, Class<T> blockClass, Class<? extends PC_ItemBlock> itemBlockClass, Class<? extends PC_TileEntity> tileEntityClass){
		final PC_Property config = module.getConfig().getProperty(blockClass.getSimpleName(), null, null);
		try {
			
			if(!config.getBoolean("enabled", true)){
				return null;
			}
			
			PC_Block block;
			PC_Block blockOff;
			
			if (blockClass.isAnnotationPresent(PC_Shining.class)) {
				int idOn = config.getInt("defaultID.on", -1);
				if(idOn==-1){
					idOn = getFreeBlockID();
					config.setInt("defaultID.on", idOn);
				}
				if (!isBlockIDFree(idOn)) {
					idOn = getFreeBlockID();
				}
				block = PC_ReflectHelper.create(blockClass, idOn, true);
				int idOff = config.getInt("defaultID.off", -1);
				if(idOff==-1){
					idOff = getFreeBlockID();
					config.setInt("defaultID.off", idOff);
				}
				if (!isBlockIDFree(idOff)) {
					idOff = getFreeBlockID();
				}
				blockOff = PC_ReflectHelper.create(blockClass, idOff, false);
				PC_ReflectHelper.setFieldsWithAnnotationTo(blockClass, blockClass,
						PC_Shining.ON.class, block);
				PC_ReflectHelper.setFieldsWithAnnotationTo(blockClass, blockClass,
						PC_Shining.OFF.class, blockOff);
				blockOff.setUnlocalizedName(blockClass.getSimpleName());
				blockOff.setModule(module);
				blockOff.setTextureFile(PC_TextureRegistry.getTextureDirectory(module)+"tiles.png");
				PC_MSGRegistry.registerMSGObject(blockOff);
				blocks.put(blockClass.getSimpleName() + ".Off", blockOff);
				registerBlock(blockOff, null);
				ItemBlock itemBlock = (ItemBlock) Item.itemsList[blockOff.blockID];
				blockOff.setItemBlock(itemBlock);
			} else {
				int id = config.getInt("defaultID", -1);
				if(id==-1){
					id = getFreeBlockID();
					config.setInt("defaultID", id);
				}
				if (!isBlockIDFree(id)) {
					id = getFreeBlockID();
				}
				block = PC_ReflectHelper.create(blockClass, id);
			}
			
			PC_MSGRegistry.registerMSGObject(block);
			blocks.put(blockClass.getSimpleName(), block);
			block.setUnlocalizedName(blockClass.getSimpleName());
			block.setModule(module);
			block.setTextureFile(PC_TextureRegistry.getTextureDirectory(module)+"tiles.png");

			if(block instanceof PC_BlockOre){
				PC_BlockOre blockOre = (PC_BlockOre)block;
				blockOre.setGenOresInChunk(config.getInt("spawn.in_chunk", blockOre.getGenOresInChunk(), "Number of deposits in each 16x16 chunk."));
				blockOre.setGenOresDepositMaxCount(config.getInt("spawn.deposit_max_size", blockOre.getGenOresDepositMaxCount(), "Highest Ore count in one deposit"));
				blockOre.setGenOresMaxY(config.getInt("spawn.max_y", blockOre.getGenOresMaxY(), "Max Y coordinate of ore deposits."));
				blockOre.setGenOresMinY(config.getInt("spawn.min_y", blockOre.getGenOresMinY(), "Min Y coordinate of ore deposits."));
			}
			
			PC_ReflectHelper.getAllFieldsWithAnnotation(block.getClass(), block, PC_BlockInfo.ConfigEntry.class, new PC_IFieldAnnotationIterator<PC_BlockInfo.ConfigEntry>() {

				@Override
				public boolean onFieldWithAnnotation(PC_FieldWithAnnotation<PC_BlockInfo.ConfigEntry> fieldWithAnnotation) {
					String entry = fieldWithAnnotation.getAnnotation().entryName();
					String comment[] = fieldWithAnnotation.getAnnotation().comment();
					if(comment.length==0){
						comment = null;
					}
					Class<?> c = fieldWithAnnotation.getFieldClass();
					if(c==Boolean.class || c==boolean.class){
						boolean defaultValue = (Boolean)fieldWithAnnotation.getValue();
						boolean value = config.getBoolean(entry, defaultValue, comment);
						fieldWithAnnotation.setValue(value);
					}else if(c==Integer.class || c==int.class){
						int defaultValue = (Integer)fieldWithAnnotation.getValue();
						int value = config.getInt(entry, defaultValue, comment);
						fieldWithAnnotation.setValue(value);
					}else if(c==Float.class || c==float.class){
						float defaultValue = (Float)fieldWithAnnotation.getValue();
						float value = config.getFloat(entry, defaultValue, comment);
						fieldWithAnnotation.setValue(value);
					}else if(c==String.class){
						String defaultValue = (String)fieldWithAnnotation.getValue();
						String value = config.getString(entry, defaultValue, comment);
						fieldWithAnnotation.setValue(value);
					}
					return false;
				}
				
			});
			block.msg(PC_MSGRegistry.MSG_LOAD_FROM_CONFIG, config);

			registerBlock(block, itemBlockClass);

			ItemBlock itemBlock = (ItemBlock) Item.itemsList[block.blockID];

			block.setItemBlock(itemBlock);

			if (itemBlockClass == null) {
				PC_LangRegistry.registerLanguage(
						module,
						new LangEntry(block
								.getUnlocalizedName(), (String) block
								.msg(PC_MSGRegistry.MSG_DEFAULT_NAME)));
			} else {
				PC_ItemBlock ib = (PC_ItemBlock) itemBlock;
				ib.setModule(module);
				List<LangEntry> l = (List<LangEntry>) ib
						.msg(PC_MSGRegistry.MSG_DEFAULT_NAME,
								new ArrayList<LangEntry>());
				if (l != null) {
					PC_LangRegistry.registerLanguage(module,
							l.toArray(new LangEntry[0]));
				}
			}

			if (tileEntityClass != null) {
				GameRegistry.registerTileEntity(tileEntityClass, tileEntityClass.getName());
				if (PC_ITileEntityRenderer.class.isAssignableFrom(tileEntityClass))
					PC_RegistryServer.getInstance().tileEntitySpecialRenderer(tileEntityClass);
			}

			return (T)block;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private static void registerBlock(PC_Block block, Class<? extends ItemBlock> itemBlock) {
		if(itemBlock==null)
			GameRegistry.registerBlock(block);
		else
			GameRegistry.registerBlock(block, itemBlock);
	}
	
	public static <T extends PC_Block> T register(PC_ModuleObject module, Class<T> blockClass){
		
		Class<? extends PC_ItemBlock> itemBlockClass = null;
		Class<? extends PC_TileEntity> tileEntityClass = null;
		
		PC_BlockInfo blockInfo = PC_ReflectHelper.getAnnotation(blockClass, PC_BlockInfo.class);
		
		if(blockInfo!=null){
		
			if(blockInfo.itemBlock() != PC_BlockInfo.PC_FakeItemBlock.class){
				itemBlockClass = blockInfo.itemBlock();
			}
			
			if(blockInfo.tileEntity() != PC_BlockInfo.PC_FakeTileEntity.class){
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
		for (int i = 1; i < PC_GlobalVariables.blockStartIndex
				&& i < Block.blocksList.length; i++) {
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
		Block block = GameInfo.getBlock(world, pos.x, pos.y, pos.z);
		if(block instanceof PC_Block){
			for (String name : names)
				if (block == getPCBlockByName(name))
					return true;
		}
		return false;
	}
	
}
