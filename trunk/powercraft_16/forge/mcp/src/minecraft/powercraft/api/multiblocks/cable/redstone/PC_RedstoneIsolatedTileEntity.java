package powercraft.api.multiblocks.cable.redstone;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import powercraft.api.multiblocks.PC_MultiblockTileEntity;
import powercraft.api.multiblocks.cable.PC_CableTileEntity;

public class PC_RedstoneIsolatedTileEntity extends PC_CableTileEntity {

	private PC_RedstoneCable cable[] = new PC_RedstoneCable[16];
	
	public PC_RedstoneIsolatedTileEntity(int cableType) {
		super(2, 4);
		cable[cableType] = new PC_RedstoneCable(1<<cableType);
	}

	@Override
	protected PC_IRedstoneCable getCableType(int cableID){
		return cable[cableID];
	}

	@Override
	public boolean canMixWith(PC_MultiblockTileEntity tileEntity) {
		if(tileEntity instanceof PC_RedstoneIsolatedTileEntity){
			PC_RedstoneIsolatedTileEntity redstoneIsolated = (PC_RedstoneIsolatedTileEntity)tileEntity;
			for(int i=0; i<cable.length; i++){
				if(cable[i]!=null && redstoneIsolated.cable[i]!=null)
					return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public PC_MultiblockTileEntity mixWith(PC_MultiblockTileEntity tileEntity) {
		PC_RedstoneIsolatedTileEntity redstoneIsolated = (PC_RedstoneIsolatedTileEntity)tileEntity;
		for(int i=0; i<cable.length; i++){
			if(cable[i]==null && redstoneIsolated.cable[i]!=null){
				cable[i] = redstoneIsolated.cable[i];
				cable[i].setTileEntity(this);
			}
		}
		return this;
	}

	@Override
	protected Icon getCableIcon() {
		return null;
	}

	@Override
	protected Icon getCableLineIcon(int index) {
		return null;
	}
	
	@Override
	public List<ItemStack> getDrop() {
		List<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(PC_RedstoneIsolatedItem.item, 1, 0));
		return drops;
	}
	
}
