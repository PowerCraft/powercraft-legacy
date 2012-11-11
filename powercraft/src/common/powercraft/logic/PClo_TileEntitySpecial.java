package powercraft.logic;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.core.PC_PacketHandler;
import powercraft.core.PC_TileEntity;
import powercraft.core.PC_Utils;

public class PClo_TileEntitySpecial extends PC_TileEntity {
	
	private int type=-1;

	public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ){
		type = stack.getItemDamage();
	}
	
	public int getType() {
		return type;
	}
	
	@Override
	public void updateEntity() {
		int nextUpdate = 0;
		boolean shouldState = false;
		
		int rot = PClo_BlockSpecial.getRotation_static(PC_Utils.getMD(worldObj, xCoord, yCoord, zCoord));
		
		int xAdd=0, zAdd=0;
		
		if (rot == 0) {
			zAdd=1;
		}else if (rot == 1) {
			xAdd = -1;
		}else if (rot == 2) {
			zAdd = -1;
		}else if (rot == 3) {
			xAdd = 1;
		}
		
		switch(type){
		case PClo_SpecialType.DAY:
			shouldState = worldObj.isDaytime();
			break;
		case PClo_SpecialType.NIGHT:
			shouldState = !worldObj.isDaytime();
			break;
		case PClo_SpecialType.RAIN:
			shouldState = worldObj.isRaining();
			break;
		case PClo_SpecialType.CHEST_EMPTY:
			shouldState = PC_Utils.isChestEmpty(worldObj, xCoord + xAdd, yCoord, zCoord + zAdd);
			break;
		case PClo_SpecialType.CHEST_FULL:
			shouldState = PC_Utils.isChestFull(worldObj, xCoord + xAdd, yCoord, zCoord + zAdd, false);
			break;
		default:
			return;
		}
		
		if(PClo_BlockSpecial.isActive(worldObj, xCoord, yCoord, zCoord) != shouldState)
			worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, PC_Utils.getBID(worldObj, xCoord, yCoord, zCoord), mod_PowerCraftLogic.special.tickRate());
		
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		type = nbtTagCompound.getInteger("type");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		nbtTagCompound.setInteger("type", type);
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
			}
		}
		PC_Utils.hugeUpdate(worldObj, xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
		worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public Object[] getData() {
		return new Object[]{
				"type", type
		};
	}	
	
}
