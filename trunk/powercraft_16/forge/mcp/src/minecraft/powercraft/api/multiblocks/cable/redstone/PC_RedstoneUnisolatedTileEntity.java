package powercraft.api.multiblocks.cable.redstone;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import powercraft.api.multiblocks.PC_MultiblockTileEntity;
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

	@Override
	protected int canConnectToMultiblock(PC_MultiblockTileEntity multiblock) {
		if(multiblock instanceof PC_RedstoneUnisolatedTileEntity){
			PC_RedstoneUnisolatedTileEntity unisolated = (PC_RedstoneUnisolatedTileEntity)multiblock;
			int connection = 0xFFFF;
			int length = 16;
			if(unisolated.getCenterThickness()>0)
				length = unisolated.getCenterThickness()+2+unisolated.getThickness()*2;
			return connection | (length<<16);
		}else if(multiblock instanceof PC_RedstoneIsolatedTileEntity){
			PC_RedstoneIsolatedTileEntity isolated = (PC_RedstoneIsolatedTileEntity)multiblock;
			if(isolated.getCableCount()==1){
				int connection = isolated.getMask();
				int length = 16;
				if(isolated.getCenterThickness()>0)
					length = isolated.getCenterThickness()+2+isolated.getThickness()*2;
				return connection | (length<<16);
			}
		}
		return 0;
	}

	@Override
	protected int getMask() {
		return 0xFFFF;
	}
	
}
