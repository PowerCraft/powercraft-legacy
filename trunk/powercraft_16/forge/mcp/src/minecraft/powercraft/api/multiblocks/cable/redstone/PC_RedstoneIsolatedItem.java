package powercraft.api.multiblocks.cable.redstone;

import net.minecraft.item.ItemStack;
import powercraft.api.multiblocks.PC_MultiblockTileEntity;
import powercraft.api.multiblocks.cable.PC_CableItem;

public class PC_RedstoneIsolatedItem extends PC_CableItem {

	public static PC_RedstoneIsolatedItem item;
	
	public PC_RedstoneIsolatedItem(int id) {
		super(id);
		item = this;
	}

	@Override
	public Class<? extends PC_MultiblockTileEntity> getTileEntityClass() {
		return PC_RedstoneIsolatedTileEntity.class;
	}
	
	@Override
	public PC_MultiblockTileEntity getTileEntity(ItemStack itemStack) {
		return new PC_RedstoneIsolatedTileEntity(itemStack.getItemDamage());
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
