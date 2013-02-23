package powercraft.weasel;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.IBlockAccess;
import powercraft.management.PC_Block;
import powercraft.management.PC_MathHelper;
import powercraft.management.PC_Property;
import powercraft.management.PC_VecI;
import powercraft.management.registry.PC_MSGRegistry;

public class PCws_BlockSilicon extends PC_Block {

	public static int genCrystalsInChunk;
    public static int genCrystalsDepositMaxCount;
    public static int genCrystalsMaxY;
    public static int genCrystalsMinY;
	
	public PCws_BlockSilicon(int id) {
		super(id, 2, Material.rock);
		setHardness(0.7F);
		setResistance(10.0F);
		setStepSound(Block.soundStoneFootstep);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			return "Silicon";
		case PC_MSGRegistry.MSG_LOAD_FROM_CONFIG:
			genCrystalsInChunk = ((PC_Property)obj[0]).getInt("spawn.in_chunk", 1, "Number of deposits in each 16x16 chunk.");
    		genCrystalsDepositMaxCount = ((PC_Property)obj[0]).getInt("spawn.deposit_max_size", 10, "Highest silicon count in one deposit");
    		genCrystalsMaxY = ((PC_Property)obj[0]).getInt("spawn.min_y", 10, "Min Y coordinate of silicon deposits.");
    		genCrystalsMinY = ((PC_Property)obj[0]).getInt("spawn.max_y", 30, "Max Y coordinate of silicon deposits.");
			break;
		case PC_MSGRegistry.MSG_SPAWNS_IN_CHUNK:{
			return genCrystalsInChunk;
		}case PC_MSGRegistry.MSG_BLOCKS_ON_SPAWN_POINT:
			return genCrystalsDepositMaxCount;
		case PC_MSGRegistry.MSG_SPAWN_POINT:
			return new PC_VecI(((Random)obj[0]).nextInt(16),
					((Random)obj[0]).nextInt(PC_MathHelper.clamp_int(genCrystalsMaxY - genCrystalsMinY, 1, 255)) + genCrystalsMinY,
					((Random)obj[0]).nextInt(16));
		case PC_MSGRegistry.MSG_SPAWN_POINT_METADATA:
			return 0;
		}
		return null;
	}

}
