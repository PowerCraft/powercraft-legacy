package powercraft.management;

import net.minecraft.src.EntityItem;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class PC_EntityItem extends EntityItem {

	public PC_EntityItem(World world, double x, double y,
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
