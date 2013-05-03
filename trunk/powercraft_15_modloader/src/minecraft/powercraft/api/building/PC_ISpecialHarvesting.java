package powercraft.api.building;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import powercraft.api.utils.PC_Struct2;
import powercraft.api.utils.PC_VecI;

public interface PC_ISpecialHarvesting {

	public boolean useFor(World world, int x, int y, int z, Block block, int meta, int priority);
	
	public List<PC_Struct2<PC_VecI, ItemStack>> harvest(World world, int x, int y, int z, Block block, int meta, int fortune);

}
