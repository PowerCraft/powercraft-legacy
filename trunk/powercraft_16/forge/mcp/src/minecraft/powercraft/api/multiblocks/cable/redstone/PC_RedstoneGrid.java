package powercraft.api.multiblocks.cable.redstone;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.world.World;
import powercraft.api.PC_Utils;
import powercraft.api.PC_Vec3IWithRotation;
import powercraft.api.grids.PC_Grid;

public class PC_RedstoneGrid extends PC_Grid<PC_RedstoneGrid, PC_IRedstoneCable> {

	private int power=0;

	@Override
	public void onUpdateTick(PC_IRedstoneCable node) {
		int newPower = 0;
		List<PC_Vec3IWithRotation> poweringBlocks = new ArrayList<PC_Vec3IWithRotation>();
		for(PC_IRedstoneCable ioNode:ioNodes){
			ioNode.addPoweringBlocks(poweringBlocks);
		}
		World world = node.getWorld();
		for(PC_Vec3IWithRotation poweringBlock:poweringBlocks){
			Block block = PC_Utils.getBlock(world, poweringBlock);
			if(block instanceof BlockRedstoneWire){
				int p = PC_Utils.getMD(world, poweringBlock);
				if(p>newPower){
					newPower = p-1;
				}
			}else if(block!=null && block.canProvidePower()){
				int p = world.getIndirectPowerLevelTo(poweringBlock.x, poweringBlock.y, poweringBlock.z, poweringBlock.dir.ordinal());
				if(p>newPower){
					newPower = p;
				}
			}
		}
		if(power!=newPower){
			power = newPower;
			for(PC_IRedstoneCable ioNode:ioNodes){
				ioNode.onRedstonePowerChange();
			}
			for(PC_IRedstoneCable edge:edges){
				edge.onRedstonePowerChange();
			}
		}
	}

	public int getRedstonePowerValue(){
		if(Block.redstoneWire.canProvidePower())
			return power;
		return power==0?0:power-1;
	}
	
}
