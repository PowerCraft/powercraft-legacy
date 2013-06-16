package powercraft.api.multiblock;

import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_VecI;

public interface PC_IMultiblock {

	public World getWorld();
	
	public PC_VecI getCoord();
	
	public boolean addFraction(ItemStack itemStack, PC_Direction pcDir, float xHit, float yHit, float zHit, PC_FractionBlock fractionBlock);

	public PC_FractionBlock getFractionOnSide(PC_FractionSide side);
	
}
