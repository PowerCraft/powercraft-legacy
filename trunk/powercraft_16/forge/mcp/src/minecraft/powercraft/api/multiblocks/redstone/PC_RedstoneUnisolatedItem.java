package powercraft.api.multiblocks.redstone;

import powercraft.api.multiblocks.PC_MultiblockTileEntity;

public class PC_RedstoneUnisolatedItem extends PC_RedstoneItem {

	public PC_RedstoneUnisolatedItem(int id) {
		super(id);
	}

	@Override
	public Class<? extends PC_MultiblockTileEntity> getTileEntityClass() {
		return PC_RedstoneUnisolatedTileEntity.class;
	}

	@Override
	public void loadMultiblockItem() {
		
	}

	@Override
	public void registerRecipes() {
		
	}

	@Override
	public void loadIcons() {
		
	}

}
