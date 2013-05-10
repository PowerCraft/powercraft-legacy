package mods.betterworld.DeCB.tileEntity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class BWDeCB_TileEntityBlockFenceWood extends TileEntity {

	private int blockDamageID;
	private String userName;
	private boolean incomplete;
	private int lightValue;
	private boolean blockLocked;
	private int rotation;

	public BWDeCB_TileEntityBlockFenceWood() {
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);
		blockDamageID = par1nbtTagCompound.getInteger("type");
		userName = par1nbtTagCompound.getString("UName");
		lightValue = par1nbtTagCompound.getInteger("LightValue");
		blockLocked = par1nbtTagCompound.getBoolean("BlockLocked");
		rotation = par1nbtTagCompound.getInteger("BlockRotation");

	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger("type", blockDamageID);
		par1nbtTagCompound.setString("UName", userName);
		par1nbtTagCompound.setInteger("LightValue", lightValue);
		par1nbtTagCompound.setBoolean("BlockLocked", blockLocked);
		par1nbtTagCompound.setInteger("BlockRotation", rotation);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("type", blockDamageID);
		nbt.setString("UName", userName);
		nbt.setInteger("LightValue", lightValue);
		nbt.setBoolean("BlockLocked", blockLocked);
		nbt.setInteger("BlockRotation", rotation);

		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, nbt);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		blockDamageID = pkt.customParam1.getInteger("type");
		userName = pkt.customParam1.getString("UName");
		lightValue = pkt.customParam1.getInteger("LightValue");
		blockLocked = pkt.customParam1.getBoolean("BlockLocked");
		rotation = pkt.customParam1.getInteger("BlockRotation");
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getBlockDamageID() {
		return blockDamageID;
	}

	public void setBlockDamageID(int i) {
		blockDamageID = i;
	}

	public int getLightValue() {
		return lightValue;
	}

	public void setLightValue(int stoneLightValue) {
		this.lightValue = stoneLightValue;
	}

	public boolean isBlockLocked() {
		return blockLocked;
	}

	public void setBlockLocked(boolean blockLocked) {
		this.blockLocked = blockLocked;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int facing) {
		this.rotation = facing;
	}

}
