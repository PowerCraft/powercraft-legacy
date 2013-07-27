package powercraft.tutorial;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.api.block.PC_TileEntity;

public class PC_TileEntityTutorial extends PC_TileEntity {

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
