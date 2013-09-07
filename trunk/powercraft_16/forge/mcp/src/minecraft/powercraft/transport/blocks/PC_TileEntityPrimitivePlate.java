package powercraft.transport.blocks;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.api.blocks.PC_TileEntity;

public class PC_TileEntityPrimitivePlate extends PC_TileEntity
{
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
