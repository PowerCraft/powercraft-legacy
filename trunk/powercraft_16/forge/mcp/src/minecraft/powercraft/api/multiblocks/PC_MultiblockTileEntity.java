package powercraft.api.multiblocks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class PC_MultiblockTileEntity {
	
	protected int index;
	protected PC_TileEntityMultiblock multiblock;
	
	public boolean isClient(){
		return multiblock.isClient();
	}
	
	public void setIndexAndMultiblock(int index, PC_TileEntityMultiblock multiblock){
		this.index = index;
		this.multiblock = multiblock;
	}
	
	public void onAdded(){
		
	}
	
	public void onBreak(){
		
	}
	
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(Random random) {
		
	}

	public void onNeighborBlockChange(int neighborID) {
		
	}

	public int getRedstonePowerValue(int side) {
		return 0;
	}

	public int getLightValue() {
		return 0;
	}

	public boolean canConnectRedstone(int side) {
		return false;
	}

	public int getLightOpacity() {
		return 0;
	}

	public void onChunkUnload() {
		
	}
	
	@SideOnly(Side.CLIENT)
	public void renderWorldBlock(RenderBlocks renderer){
		
	}

	public boolean canMixWith(PC_MultiblockTileEntity tileEntity) {
		return false;
	}

	public PC_MultiblockTileEntity mixWith(PC_MultiblockTileEntity tileEntity) {
		return this;
	}

	public void loadFromNBT(NBTTagCompound nbtCompoundTag) {
		
	}

	public void saveToNBT(NBTTagCompound nbtCompoundTag) {
		
	}

	public void invalidate() {
		
	}

	public void validate() {
		
	}

	public void onLoaded() {
		
	}

	public void update() {
		
	}
	
}
