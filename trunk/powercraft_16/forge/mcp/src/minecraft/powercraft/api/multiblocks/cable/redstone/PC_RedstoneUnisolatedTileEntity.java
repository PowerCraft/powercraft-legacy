package powercraft.api.multiblocks.cable.redstone;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import powercraft.api.multiblocks.cable.PC_CableTileEntity;

public class PC_RedstoneUnisolatedTileEntity extends PC_CableTileEntity implements PC_IRedstoneCable {

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
