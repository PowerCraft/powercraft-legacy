package powercraft.api.multiblock.redstone;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockRedstoneWire;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntity;
import powercraft.api.multiblock.PC_BlockMultiblock;
import powercraft.api.multiblock.PC_FractionBlock;
import powercraft.api.multiblock.PC_FractionSide;
import powercraft.api.multiblock.PC_IMultiblock;
import powercraft.api.multiblock.cables.PC_CableTypes;
import powercraft.api.multiblock.cables.PC_FractionBlockCable;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

public class PC_FractionBlockRedstoneCable extends PC_FractionBlockCable {
	
	public int cable;
	public int powerValue=0;
	
	public PC_FractionBlockRedstoneCable(){
		
	}
	
	public PC_FractionBlockRedstoneCable(int cable){
		this.cable = cable;
	}
	
	@Override
	public void loadFromNBT(NBTTagCompound nbttag) {
		cable = nbttag.getInteger("cable");
		powerValue = nbttag.getInteger("powerValue");
	}

	@Override
	public void saveToNBT(NBTTagCompound nbttag) {
		nbttag.setInteger("cable", cable);
		nbttag.setInteger("powerValue", powerValue);

	}

	@Override
	public PC_FractionBlock mixWithFraction(PC_FractionBlock otherFraction) {
		if(otherFraction instanceof PC_FractionBlockRedstoneCable){
			if(((PC_FractionBlockRedstoneCable) otherFraction).cable != cable){
				return new PC_FractionBlockRedstoneMultiCable(1<<((PC_FractionBlockRedstoneCable) otherFraction).cable|1<<cable);
			}
		}
		return null;
	}
	
	@Override
	public boolean canMixWithFraction(PC_FractionBlock otherFraction){
		if(otherFraction instanceof PC_FractionBlockRedstoneCable){
			return ((PC_FractionBlockRedstoneCable) otherFraction).cable != cable;
		}
		return false;
	}

	@Override
	public PC_CableTypes getCableType() {
		return PC_CableTypes.REDSTONE;
	}

	@Override
	public int getCableMask(){
		return 1<<cable;
	}

	@Override
	public int getConnectionMaskToBlockAndTileEntity(PC_IMultiblock multiblock, PC_FractionSide fromIndex, Block block, TileEntity tileEntity) {
		if(block instanceof BlockRedstoneWire){
			return fromIndex == PC_FractionSide.BOTTOM?getCableMask():0;
		}
		return block.canProvidePower()?getCableMask():0;
	}
	
	@Override
	public void updatePowerValue(PC_IMultiblock multiblock, PC_FractionSide fromIndex){
		
	}
	
}
