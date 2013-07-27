package powercraft.checkpoint.block;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.api.block.PC_TileEntity;

public class PCcp_TileEntityCheckpoint extends PC_TileEntity {

	private int var;

	@Override
	public void loadFromNBT(NBTTagCompound nbtTagCompound) {
		super.loadFromNBT(nbtTagCompound);
		var = nbtTagCompound.getInteger("var");
	}

	@Override
	public void saveToNBT(NBTTagCompound nbtTagCompound) {
		super.saveToNBT(nbtTagCompound);
		nbtTagCompound.setInteger("var", var);
	}
	
}
