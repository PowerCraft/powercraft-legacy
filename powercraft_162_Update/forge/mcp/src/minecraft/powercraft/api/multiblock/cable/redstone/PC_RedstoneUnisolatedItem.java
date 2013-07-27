package powercraft.api.multiblock.cable.redstone;


import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;
import powercraft.api.item.PC_ItemInfo;
import powercraft.api.multiblock.PC_MultiblockTileEntity;
import powercraft.api.multiblock.cable.PC_CableItem;
import powercraft.api.registry.PC_TextureRegistry;


@PC_ItemInfo(name = "Unisolated Redstone", itemid = "unisolatedRedstone", defaultid = 17001)
public class PC_RedstoneUnisolatedItem extends PC_CableItem {

	private static Icon cableIcon;
	public static PC_RedstoneUnisolatedItem item;


	public PC_RedstoneUnisolatedItem(int id) {

		super(id);
		setCreativeTab(CreativeTabs.tabRedstone);
		item = this;
	}


	@Override
	public Class<? extends PC_MultiblockTileEntity> getTileEntityClass() {

		return PC_RedstoneUnisolatedTileEntity.class;
	}


	@Override
	public void loadMultiblockItem() {

		cableIcon = PC_TextureRegistry.registerIcon("redstone", itemInfo.itemid());
	}


	@Override
	public void registerRecipes() {

	}


	@Override
	public void loadIcons() {

		itemIcon = PC_TextureRegistry.registerIcon("redstone");
	}


	public static Icon getCableIcon() {

		return cableIcon;
	}

}
