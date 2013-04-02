package powercraft.api.hacks;

import net.minecraft.src.Block;
import net.minecraft.src.BlockFire;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;
import powercraft.api.block.PC_Block;
import powercraft.api.utils.PC_Utils;

public class PC_BlockFireHack extends BlockFire {
	
	public PC_BlockFireHack(Block fire) {
		super(fire.blockID);
	}
	
	@Override
	public boolean canBlockCatchFire(IBlockAccess world, int x, int y, int z) {
		Block b = PC_Utils.getBlock(world, x, y, z);
		if (b == null) {
			return false;
		} else if (b instanceof PC_Block) {
			return ((PC_Block) b).isFlammable(world, x, y, z, PC_Utils.getMD(world, x, y, z));
		} else if (b.blockID < 256) {
			return super.canBlockCatchFire(world, x, y, z);
		}
		return false;
	}
	
	@Override
	public int getChanceToEncourageFire(World world, int x, int y, int z, int chance) {
		Block b = PC_Utils.getBlock(world, x, y, z);
		int newChance = 0;
		if (b == null) {
			newChance = 0;
		} else if (b instanceof PC_Block) {
			newChance = ((PC_Block) b).getFlammability(world, x, y, z, PC_Utils.getMD(world, x, y, z));
		} else if (b.blockID < 256) {
			newChance = super.getChanceToEncourageFire(world, x, y, z, 0);
		}
		return newChance > chance ? newChance : chance;
	}
	
}
