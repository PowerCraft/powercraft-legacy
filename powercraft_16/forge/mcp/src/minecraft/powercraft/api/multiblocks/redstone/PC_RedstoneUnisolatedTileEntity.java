package powercraft.api.multiblocks.redstone;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import powercraft.api.PC_Utils;
import powercraft.api.energy.PC_ConduitEnergyItem;
import powercraft.api.multiblocks.PC_MultiblockTileEntity;

public class PC_RedstoneUnisolatedTileEntity extends PC_RedstoneTileEntity implements PC_IRedstoneCable {

	public PC_RedstoneUnisolatedTileEntity() {
		super(1, 2);
	}

	private PC_RedstoneGrid grid;
	
	@Override
	public PC_RedstoneGrid getGrid() {
		return grid;
	}

	@Override
	public void setGrid(PC_RedstoneGrid grid) {
		this.grid = grid;
	}

	@Override
	protected PC_IRedstoneCable getCableType(int cableID) {
		return this;
	}

	@Override
	protected Icon getCableIcon() {
		return PC_RedstoneUnisolatedItem.getCableIcon();
	}

	@Override
	protected Icon getCableLineIcon(int index) {
		return null;
	}

	@Override
	public List<ItemStack> getDrop() {
		List<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(PC_RedstoneUnisolatedItem.item, 1, 0));
		return drops;
	}
	
}
