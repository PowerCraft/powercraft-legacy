package powercraft.management;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface PC_IFurnaceRecipe {

	public PC_Struct2<List<ItemStack>, Float> getOutput(World world, PC_InventoryFurnace inv, ItemStack fuel);
	
}
