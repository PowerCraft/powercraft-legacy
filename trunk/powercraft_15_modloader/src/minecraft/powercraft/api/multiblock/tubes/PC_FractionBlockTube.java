package powercraft.api.multiblock.tubes;

import net.minecraft.src.NBTTagCompound;
import powercraft.api.multiblock.PC_FractionBlock;
import powercraft.api.multiblock.PC_FractionSide;
import powercraft.api.multiblock.PC_IMultiblock;

public class PC_FractionBlockTube extends PC_FractionBlock {

	public PC_FractionBlockTube(){
		thick = 8.0f/16.0f;
	}
	
	@Override
	public boolean canPlaceOnPosition(PC_IMultiblock multiblock, PC_FractionSide side) {
		return side == PC_FractionSide.MIDDLE;
	}

	@Override
	public void renderFraction(PC_IMultiblock multiblock, PC_FractionSide side, Object renderer) {
		
	}

	@Override
	protected void loadFromNBT(NBTTagCompound nbttag) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void saveToNBT(NBTTagCompound nbttag) {
		// TODO Auto-generated method stub

	}

}
