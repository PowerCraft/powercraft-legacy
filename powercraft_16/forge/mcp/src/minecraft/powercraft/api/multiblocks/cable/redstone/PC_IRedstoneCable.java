package powercraft.api.multiblocks.cable.redstone;

import java.util.List;

import net.minecraft.world.World;
import powercraft.api.PC_Vec3IWithRotation;
import powercraft.api.grids.PC_IGridProvider;

public interface PC_IRedstoneCable extends PC_IGridProvider<PC_RedstoneGrid, PC_IRedstoneCable> {

	public void addPoweringBlocks(List<PC_Vec3IWithRotation> poweringBlocks);

	public World getWorld();

	public void onRedstonePowerChange();

}
