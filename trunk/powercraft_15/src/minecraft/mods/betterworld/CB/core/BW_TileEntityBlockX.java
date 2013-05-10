package mods.betterworld.CB.core;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class BW_TileEntityBlockX extends TileEntity{
	
	private int blockDamageID;
	private String userName;
	private boolean incomplete;
	
	
	public BW_TileEntityBlockX(){}
	
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getBlockDamageID()
	{
		System.out.println("Gesendet: "+blockDamageID);
		return blockDamageID;
	}
	public void setBlockDamageID(int i)
	{
		blockDamageID = i;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);
		blockDamageID = par1nbtTagCompound.getInteger("type");
		userName = par1nbtTagCompound.getString("UName");
		
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger("type", blockDamageID);
		par1nbtTagCompound.setString("UName", userName);
		
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("type", blockDamageID);
		nbt.setString("UName", userName);
		
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, nbt);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		blockDamageID = pkt.customParam1.getInteger("type");
		userName = pkt.customParam1.getString("UName");
		
	}
	
}
