package powercraft.core.blocks.tileentities;


import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import powercraft.api.PC_Direction;
import powercraft.api.PC_FieldDescription;
import powercraft.api.blocks.PC_TileEntity;
import powercraft.api.energy.PC_IEnergyPuffer;
import powercraft.api.gres.PC_GresBaseWithInventory;
import powercraft.api.gres.PC_IGresGui;
import powercraft.api.gres.PC_IGresGuiOpenHandler;
import powercraft.core.blocks.guis.PC_GuiPuffer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class PC_TileEntityPuffer extends PC_TileEntity implements PC_IGresGuiOpenHandler, PC_IEnergyPuffer {

	private static final int maxEnergyInput = 100;
	private static final int maxEnergyOutput = 100;
	public static final int maxEnergy = 10000;

	@PC_FieldDescription(sync=true)
	private float energy = 0;
	private float request = 0;
	private float forUsage = 0;


	@Override
	public float getEnergyRequest(PC_Direction side) {

		return request;
	}


	@Override
	public float consumeEnergy(PC_Direction side, float energy) {

		sendToClient();
		if (energy <= request) {
			request -= energy;
			this.energy += energy;
			return 0;
		} 
		energy -= request;
		this.energy += request;
		request = 0;
		return energy;
	}


	@Override
	public float getEnergyForUsage(PC_Direction side) {

		return forUsage;
	}


	@Override
	public float getEnergy(PC_Direction side, float energy) {

		sendToClient();
		if (energy <= forUsage) {
			forUsage -= energy;
			this.energy -= energy;
			return energy;
		}
		energy = forUsage;
		this.energy -= forUsage;
		forUsage = 0;
		return energy;
	}


	@Override
	public float getEnergyLevel(PC_Direction side) {

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
	public PC_IGresGui openClientGui(EntityPlayer player) {

		return new PC_GuiPuffer(this, player);
	}


	@Override
	public PC_GresBaseWithInventory openServerGui(EntityPlayer player) {

		return null;
	}


	@Override
	public boolean canConsumerTubeConnectTo(PC_Direction side) {

		return true;
	}


	@Override
	public boolean canProviderTubeConnectTo(PC_Direction side) {

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


	@Override
	public void onLoadedFromNBT() {
		super.onLoadedFromNBT();
		renderUpdate();
	}
	
}
