package powercraft.api.multiblocks.cable.redstone;


import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;
import powercraft.api.items.PC_ItemInfo;
import powercraft.api.multiblocks.PC_MultiblockTileEntity;
import powercraft.api.multiblocks.cable.PC_CableItem;
import powercraft.api.registries.PC_TextureRegistry;


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
