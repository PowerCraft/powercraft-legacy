package powercraft.api.multiblock.redstone;

import net.minecraft.src.Block;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import powercraft.api.multiblock.PC_FractionBlock;
import powercraft.api.multiblock.PC_FractionSide;
import powercraft.api.multiblock.PC_IMultiblock;
import powercraft.api.multiblock.cables.PC_CableTypes;
import powercraft.api.multiblock.cables.PC_FractionBlockCable;

public class PC_FractionBlockRedstoneMultiCable extends PC_FractionBlockCable {

	private int[] powerValue = new int[16];
	public int cables;
	
	public PC_FractionBlockRedstoneMultiCable(){
		
	}
	
	public PC_FractionBlockRedstoneMultiCable(int cables){
		this.cables = cables;
		calcThick();
	}
	
	public float getThick(int mask){
		final int sizes[] = {0, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4};
		int num=0;
		for(int i=0; i<16; i++){
			if((mask & 1<<i)!=0){
				num++;
			}
		}
		return sizes[num]/16.0f;
	}
	
	public void calcThick(){
		thick = getThick(cables);
		width = thick;
	}
	
	@Override
	public void loadFromNBT(NBTTagCompound nbttag) {
		powerValue = nbttag.getIntArray("powerValue");
		cables = nbttag.getInteger("cables");
	}

	@Override
	public void saveToNBT(NBTTagCompound nbttag) {
		nbttag.setIntArray("powerValue", powerValue);
		nbttag.setInteger("cables", cables);
	}

	@Override
	public PC_FractionBlock mixWithFraction(PC_FractionBlock otherFraction) {
		if(otherFraction instanceof PC_FractionBlockRedstoneCable){
			if((1<<((PC_FractionBlockRedstoneCable) otherFraction).cable & cables)==0){
				cables |= 1<<((PC_FractionBlockRedstoneCable) otherFraction).cable;
				calcThick();
				return this;
			}
		}
		return null;
	}
	
	@Override
	public boolean canMixWithFraction(PC_FractionBlock otherFraction){
		if(otherFraction instanceof PC_FractionBlockRedstoneCable){
			return (1<<((PC_FractionBlockRedstoneCable) otherFraction).cable & cables)==0;
		}
		return false;
	}

	@Override
	public PC_CableTypes getCableType() {
		return PC_CableTypes.REDSTONE;
	}

	@Override
	public int getCableMask(){
		return cables;
	}

	@Override
	public int getConnectionMaskToBlockAndTileEntity(PC_IMultiblock multiblock, PC_FractionSide fromIndex, Block block, TileEntity tileEntity) {
		return 0;
	}
	
	@Override
	public void updatePowerValue(PC_IMultiblock multiblock, PC_FractionSide fromIndex){
		
	}
	
}
