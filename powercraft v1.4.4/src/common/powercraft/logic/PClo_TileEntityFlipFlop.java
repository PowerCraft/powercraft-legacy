package powercraft.logic;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import powercraft.core.PC_PacketHandler;
import powercraft.core.PC_TileEntity;
import powercraft.core.PC_Utils;

public class PClo_TileEntityFlipFlop extends PC_TileEntity {
	
	private int type=-1;
	private boolean clock = false;
	
	public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ){
		type = stack.getItemDamage();
	}
	
	public int getType() {
		return type;
	}
	
	public boolean getClock(){
		return clock;
	}
	
	public void setClock(boolean state){
		clock = state;
		PC_PacketHandler.setTileEntity(this, "clock", clock);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		type = nbtTagCompound.getInteger("type");
		clock = nbtTagCompound.getBoolean("clock");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		nbtTagCompound.setInteger("type", type);
		nbtTagCompound.setBoolean("clock", clock);
	}

	@Override
	public void setData(Object[] o) {
		int p = 0;
		while(p<o.length){
			String var = (String)o[p++];
			if(var.equals("type")){
				if(type==-1)
					type = (Integer)o[p++];
				else
					p++;
			}else if(var.equals("clock")){
				clock = (Boolean)o[p++];
			}
		}
		PC_Utils.hugeUpdate(worldObj, xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
		worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public Object[] getData() {
		return new Object[]{
				"type", type,
				"clock", clock
		};
	}	
	
}
