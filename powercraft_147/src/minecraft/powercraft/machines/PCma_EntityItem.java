package powercraft.machines;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PCma_EntityItem extends EntityItem {

	public PCma_EntityItem(World world, double x, double y,
			double z, ItemStack itemStack) {
		super(world, x, y, z, itemStack);
	}

	@Override
	public boolean combineItems(EntityItem entityItem){
		if(ticksExisted>25)
			return super.combineItems(entityItem);
		return false;
	}
	
}
