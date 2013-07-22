package powercraft.core.blocks;


import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.api.blocks.PC_TileEntity;
import powercraft.api.energy.PC_IEnergyPuffer;
import powercraft.api.gres.PC_GresBaseWithInventory;
import powercraft.api.gres.PC_IGresClient;
import powercraft.api.gres.PC_IGresGuiOpenHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class PC_TileEntityPuffer extends PC_TileEntity implements PC_IGresGuiOpenHandler, PC_IEnergyPuffer {

	private static final int maxEnergyInput = 100;
	private static final int maxEnergyOutput = 100;
	public static final int maxEnergy = 10000;

	private float energy = 0;
	private float request = 0;
	private float forUsage = 0;


	@Override
	public float getEnergyRequest() {

		return request;
	}


	@Override
	public float consumeEnergy(float energy) {

		sendToClient();
		if (energy <= request) {
			request -= energy;
			this.energy += energy;
			return 0;
		} else {
			energy -= request;
			this.energy += request;
			request = 0;
			return energy;
		}
	}


	@Override
	public float getEnergyForUsage() {

		return forUsage;
	}


	@Override
	public float getEnergy(float energy) {

		sendToClient();
		if (energy <= forUsage) {
			forUsage -= energy;
			this.energy -= energy;
			return energy;
		} else {
			energy = forUsage;
			this.energy -= forUsage;
			forUsage = 0;
			return energy;
		}
	}


	@Override
	public float getEnergyLevel() {

		return energy;
	}


	@Override
	public void updateEntity() {

		request = energy + maxEnergyInput > maxEnergy ? maxEnergy - energy : maxEnergyInput;
		forUsage = maxEnergyOutput > energy ? energy : maxEnergyOutput;
		super.updateEntity();
	}


	@Override
	@SideOnly(Side.CLIENT)
	public PC_IGresClient openClientGui(EntityPlayer player) {

		return new PC_GuiPuffer(this, player);
	}


	@Override
	public PC_GresBaseWithInventory openServerGui(EntityPlayer player) {

		return null;
	}


	@Override
	public void loadFromNBT(NBTTagCompound nbtTagCompound) {

		energy = nbtTagCompound.getFloat("energy");
		renderUpdate();
	}


	@Override
	public void saveToNBT(NBTTagCompound nbtTagCompound) {

		nbtTagCompound.setFloat("energy", energy);
	}


	@Override
	public boolean canConsumerTubeConnectTo(int side) {

		return true;
	}


	@Override
	public boolean canProviderTubeConnectTo(int side) {

		return true;
	}


	public void setEnergy(int energy) {

		this.energy = energy;
		sendToClient();
	}


	@Override
	public ArrayList<ItemStack> getBlockDropped(int fortune) {

		Block block = getBlockType();
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		int id = block.idDropped(blockMetadata, worldObj.rand, fortune);
		if (id > 0) {
			ret.add(new ItemStack(id, 1, (int) energy));
		}
		return ret;
	}

}
