package powercraft.api.registries;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import powercraft.api.PC_Logger;
import powercraft.api.multiblocks.PC_MultiblockItem;
import powercraft.api.multiblocks.PC_MultiblockTileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class PC_MultiblockRegistry {

	private static HashMap<String, Class<? extends PC_MultiblockTileEntity>> multiblockTileEntitys = new HashMap<String, Class<? extends PC_MultiblockTileEntity>>();
	private static List<PC_MultiblockItem> multiblockItems = new ArrayList<PC_MultiblockItem>();


	public static PC_MultiblockTileEntity createMultiblockTileEntityFromName(String multiblockTileEntityName) {

		Class<? extends PC_MultiblockTileEntity> clazz = multiblockTileEntitys.get(multiblockTileEntityName);
		if (clazz == null) {
			PC_Logger.severe("No MultiblockTileEntity found for name %s", multiblockTileEntityName);
			return null;
		}
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			PC_Logger.severe("Error while try to create MultiblockTileEntity %s", clazz);
		}
		return null;
	}


	public static boolean isMultiblockTileEntity(PC_MultiblockTileEntity mulitblockTileEntity, String multiblockTileEntityName) {

		Class<? extends PC_MultiblockTileEntity> clazz = multiblockTileEntitys.get(multiblockTileEntityName);
		if (clazz == null) {
			PC_Logger.severe("No MultiblockTileEntity found for name %s", multiblockTileEntityName);
			return false;
		}
		if (mulitblockTileEntity == null) return false;
		return mulitblockTileEntity.getClass() == clazz;
	}


	public static void registerMultiblockTileEntity(PC_MultiblockItem multiblockItem, Class<? extends PC_MultiblockTileEntity> tileEntityClass) {

		multiblockTileEntitys.put(tileEntityClass.getName(), tileEntityClass);
		multiblockItems.add(multiblockItem);
	}


	@SideOnly(Side.CLIENT)
	public static void loadIcons() {

		for (PC_MultiblockItem multiblockItem : multiblockItems) {
			multiblockItem.loadMultiblockItem();
		}
	}


	public static String getMultiblockTileEntityName(PC_MultiblockTileEntity multiblockTileEntity) {

		return multiblockTileEntity.getClass().getName();
	}

}
