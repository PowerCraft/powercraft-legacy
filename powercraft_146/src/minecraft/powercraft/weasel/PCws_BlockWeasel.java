package powercraft.weasel;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_VecI;

public class PCws_BlockWeasel extends PC_Block {

	protected PCws_BlockWeasel(int id) {
		super(id, 6, Material.ground);
		setHardness(0.5F);
		setLightValue(0);
		setStepSound(Block.soundWoodFootstep);
		disableStats();
		setRequiresSelfNotify();
		setResistance(60.0F);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
	}

	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public static boolean[] getWeaselInputStates(World world, PC_VecI pos) {
		// TODO Auto-generated method stub
		return null;
	}

}
