package powercraft.api.multiblocks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import powercraft.api.blocks.PC_TileEntity;

public class PC_TileEntityMultiblock extends PC_TileEntity {

	private PC_MultiblockTileEntity[] tileEntities = new PC_MultiblockTileEntity[27];
	
	@Override
	public boolean canUpdate() {
		for(int i=0; i<tileEntities.length; i++){
			if(tileEntities[i]!=null){
				tileEntities[i].onLoaded();
			}
		}
		return true;
	}

	@Override
	public void updateEntity() {
		for(int i=0; i<tileEntities.length; i++){
			if(tileEntities[i]!=null){
				tileEntities[i].update();
			}
		}
		super.updateEntity();
	}

	@Override
	public void invalidate() {
		super.invalidate();
		for(int i=0; i<tileEntities.length; i++){
			if(tileEntities[i]!=null){
				tileEntities[i].invalidate();
			}
		}
	}

	@Override
	public void validate() {
		super.validate();
		for(int i=0; i<tileEntities.length; i++){
			if(tileEntities[i]!=null){
				tileEntities[i].validate();
			}
		}
	}

	@Override
	public void onBlockBreak() {
		for(int i=0; i<tileEntities.length; i++){
			if(tileEntities[i]!=null){
				tileEntities[i].onBreak();
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(Random random) {
		for(int i=0; i<tileEntities.length; i++){
			if(tileEntities[i]!=null){
				tileEntities[i].randomDisplayTick(random);
			}
		}
	}

	@Override
	public void onNeighborBlockChange(int neighborID) {
		for(int i=0; i<tileEntities.length; i++){
			if(tileEntities[i]!=null){
				tileEntities[i].onNeighborBlockChange(neighborID);
			}
		}
	}

	@Override
	public int getRedstonePowerValue(int side) {
		int maxRedstonePowerValue = 0;
		for(int i=0; i<tileEntities.length; i++){
			if(tileEntities[i]!=null){
				int redstonePowerValue = tileEntities[i].getRedstonePowerValue(side);
				if(redstonePowerValue>maxRedstonePowerValue){
					maxRedstonePowerValue = redstonePowerValue;
				}
			}
		}
		return maxRedstonePowerValue;
	}

	@Override
	public int getLightValue() {
		int maxLightValue = 0;
		for(int i=0; i<tileEntities.length; i++){
			if(tileEntities[i]!=null){
				int lightValue = tileEntities[i].getLightValue();
				if(lightValue>maxLightValue){
					maxLightValue = lightValue;
				}
			}
		}
		return maxLightValue;
	}

	@Override
	public boolean removeBlockByPlayer(EntityPlayer player) {
		// TODO Auto-generated method stub
		return super.removeBlockByPlayer(player);
	}

	@Override
	public boolean canConnectRedstone(int side) {
		boolean canConnectRedstone = false;
		for(int i=0; i<tileEntities.length; i++){
			if(tileEntities[i]!=null){
				canConnectRedstone |= tileEntities[i].canConnectRedstone(side);
			}
		}
		return false;
	}

	@Override
	public int getLightOpacity() {
		int maxLightOpacity = 0;
		for(int i=0; i<tileEntities.length; i++){
			if(tileEntities[i]!=null){
				int lightOpacity = tileEntities[i].getLightOpacity();
				if(lightOpacity>maxLightOpacity){
					maxLightOpacity = lightOpacity;
				}
			}
		}
		return maxLightOpacity;
	}

	@Override
	public void onChunkUnload() {
		for(int i=0; i<tileEntities.length; i++){
			if(tileEntities[i]!=null){
				tileEntities[i].onChunkUnload();
			}
		}
	}

	public boolean setCenter(PC_MultiblockTileEntity tileEntity) {
		return setMultiblockTileEntity(0, tileEntity);
	}

	public PC_MultiblockTileEntity getCenter() {
		return tileEntities[0];
	}
	
	public boolean setMultiblockTileEntity(int index, PC_MultiblockTileEntity tileEntity){
		if(tileEntities[index] == null){
			tileEntities[index] = tileEntity;
			tileEntity.setIndexAndMultiblock(index, this);
			tileEntity.onAdded();
		}else{
			if(tileEntities[index].canMixWith(tileEntity)){
				tileEntities[index] = tileEntities[index].mixWith(tileEntity);
				tileEntities[index].setIndexAndMultiblock(index, this);
			}else{
				return false;
			}
		}
		notifyNeighbors();
		sendToClient();
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean renderWorldBlock(RenderBlocks renderer) {
		for(int i=0; i<tileEntities.length; i++){
			if(tileEntities[i]!=null){
				tileEntities[i].renderWorldBlock(renderer);
			}
		}
		return true;
	}
	
	public void loadFromNBT(NBTTagCompound nbtTagCompound) {
		for(int i=0; i<tileEntities.length; i++){
			if(nbtTagCompound.hasKey("data["+i+"]")){
				String multiblockTileEntityName = nbtTagCompound.getString("multiblockTileEntityName["+i+"]");
				if(!PC_MultiblockRegistry.isMultiblockTileEntity(tileEntities[i], multiblockTileEntityName)){
					tileEntities[i] = PC_MultiblockRegistry.createMultiblockTileEntityFromName(multiblockTileEntityName);
					tileEntities[i].setIndexAndMultiblock(i, this);
				}
				tileEntities[i].loadFromNBT(nbtTagCompound.getCompoundTag("data["+i+"]"));
			}else{
				tileEntities[i] = null;
			}
		}
	}
	
	public void saveToNBT(NBTTagCompound nbtTagCompound) {
		for(int i=0; i<tileEntities.length; i++){
			if(tileEntities[i]!=null){
				nbtTagCompound.setString("multiblockTileEntityName["+i+"]", PC_MultiblockRegistry.getMultiblockTileEntityName(tileEntities[i]));
				NBTTagCompound compound = new NBTTagCompound();
				tileEntities[i].saveToNBT(compound);
				nbtTagCompound.setCompoundTag("data["+i+"]", compound);
			}
		}
	}	
	
}
