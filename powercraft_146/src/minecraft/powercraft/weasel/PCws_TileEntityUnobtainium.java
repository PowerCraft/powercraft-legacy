package powercraft.weasel;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_INBT;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.SaveHandler;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;

public class PCws_TileEntityUnobtainium extends PC_TileEntity {

	public static class UnobtainiumData implements PC_INBT<UnobtainiumData>{
		
		public PC_VecI treeCoord;
		public World world;
		public long lastUpdate = System.currentTimeMillis();
		
		@Override
		public UnobtainiumData readFromNBT(NBTTagCompound nbttag) {
			SaveHandler.loadFromNBT(nbttag, "treeCoord", treeCoord = new PC_VecI());
			return this;
		}

		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound nbttag) {
			SaveHandler.saveToNBT(nbttag, "treeCoord", treeCoord);
			return nbttag;
		}
		
		public void update(){
			long time = System.currentTimeMillis();
			if(lastUpdate+1000<time){
				lastUpdate = time;
				treeCoord.y = world.getHeightValue(treeCoord.x, treeCoord.z);
				ValueWriting.setBID(world, treeCoord, Block.wood.blockID, 0);
			}
		}
		
	}

	private UnobtainiumData data;
	private boolean firstTick = true;
	
	public UnobtainiumData getUnobtainiumData(){
		return data;
	}
	
	public void setUnobtainiumData(UnobtainiumData data){
		 this.data = data;
	}
	
	private void updateUnobtainiumData(World world, int x, int y, int z){
		int r=3;
		for(int xx=-r; xx<=r; xx++){
			for(int yy=-r; yy<=r; yy++){
				for(int zz=-r; zz<=r; zz++){
					TileEntity te = GameInfo.getTE(world, x + xx, y + yy, z + zz);
					if(te instanceof PCws_TileEntityUnobtainium){
						UnobtainiumData otherData = ((PCws_TileEntityUnobtainium) te).getUnobtainiumData();
						if(otherData!=null){
							if(otherData!=data){
								data = otherData;
								return;
							}
						}
					}
				}
			}
		}
		if(data==null)
			data = new UnobtainiumData();
		if(data.world==null)
			data.world = worldObj;
		if(data.treeCoord==null)
			data.treeCoord = new PC_VecI(x, y, z);
	}
	
	@Override
	public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		//updateUnobtainiumData(world, x, y, z);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		//SaveHandler.loadFromNBT(nbtTagCompound, "data", data = new UnobtainiumData());
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		//SaveHandler.saveToNBT(nbtTagCompound, "data", data);
	}

	@Override
	public void updateEntity() {
		/*if(data==null || firstTick){
			firstTick = false;
			if(worldObj!=null){
				updateUnobtainiumData(worldObj, xCoord, yCoord, zCoord);
			}else{
				return;
			}
		}
		if(data.world==null)
			data.world = worldObj;
		data.update();*/
	}

	/*@Override
	public boolean canUpdate() {
		return true;
	}*/
	
	
	
}
