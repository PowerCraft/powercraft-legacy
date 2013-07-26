package powercraft.api.multiblocks.cable.redstone;


import java.util.List;

import net.minecraft.world.World;
import powercraft.apiOld.PC_Vec3IWithRotation;
import powercraft.apiOld.grids.PC_IGridProvider;


public interface PC_IRedstoneCable extends PC_IGridProvider<PC_RedstoneGrid, PC_IRedstoneCable> {

	public void addPoweringBlocks(List<PC_Vec3IWithRotation> poweringBlocks);


	public World getWorld();


	public void onRedstonePowerChange();

}
