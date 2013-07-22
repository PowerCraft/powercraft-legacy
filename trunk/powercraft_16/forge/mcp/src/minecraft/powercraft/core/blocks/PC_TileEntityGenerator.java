package powercraft.core.blocks;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.api.PC_FuelHandler;
import powercraft.api.PC_Utils;
import powercraft.api.blocks.PC_TileEntityWithInventory;
import powercraft.api.energy.PC_IEnergyProvider;
import powercraft.api.gres.PC_GresBaseWithInventory;
import powercraft.api.gres.PC_IGresClient;
import powercraft.api.gres.PC_IGresGuiOpenHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class PC_TileEntityGenerator extends PC_TileEntityWithInventory implements PC_IGresGuiOpenHandler, PC_IEnergyProvider {

	public static final int maxHeat = 1000;
	private int burnTime = 0;
	private int maxBurnTime = 1;
	private float energy = 0;
	private int heat = 0;
	private int oldHeat10 = 0;


	public PC_TileEntityGenerator() {

		super(1);
	}


	@Override
	public float getEnergyForUsage() {

		return energy;
	}


	@Override
	public float getEnergy(float energy) {

		if (this.energy >= energy) {
			this.energy -= energy;
		} else {
			energy -= this.energy;
			this.energy = 0;
		}
		return energy;
	}


	@Override
	@SideOnly(Side.CLIENT)
	public PC_IGresClient openClientGui(EntityPlayer player) {

		return new PC_GuiGenerator(this, player);
	}


	@Override
	public PC_GresBaseWithInventory openServerGui(EntityPlayer player) {

		return new PC_ContainerGenerator(this, player);
	}


	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {

		return PC_FuelHandler.isItemFuel(itemstack);
	}


	@Override
	public void updateEntity() {

		if (burnTime <= 0 && !isClient()) {
			if (inventoryContents[0] != null) {
				maxBurnTime = PC_FuelHandler.getItemBurnTime(inventoryContents[0]);
				burnTime = maxBurnTime;
				if (burnTime > 0) {
					decrStackSize(0, 1);
				}
			}
			sendToClient();
		}
		if (burnTime > 0) {
			burnTime--;
			sendProgressBarUpdate(0, burnTime * 1000 / maxBurnTime);
			if (heat < maxHeat) {
				heat++;
				sendProgressBarUpdate(1, heat);
			}
			energy = heat / 10;
		} else {
			if (heat > 0) heat -= 3;
			if (heat < 0) heat = 0;
			sendProgressBarUpdate(1, heat);
			energy = heat / 10;
		}
		if (!isClient()) {
			if (oldHeat10 != (heat + 9) / 10) {
				oldHeat10 = (heat + 9) / 10;
				sendToClient();
			}
		}
		super.updateEntity();
	}


	public int getHeat() {

		return heat;
	}


	@Override
	public void sendProgressBarUpdates() {

		sendProgressBarUpdate(0, burnTime * 1000 / maxBurnTime);
		sendProgressBarUpdate(1, heat);
	}


	@Override
	public void loadFromNBT(NBTTagCompound nbtTagCompound) {

		burnTime = nbtTagCompound.getInteger("burnTime");
		maxBurnTime = nbtTagCompound.getInteger("maxBurnTime");
		heat = nbtTagCompound.getInteger("heat");
		renderUpdate();
		lightUpdate();
	}


	@Override
	public void saveToNBT(NBTTagCompound nbtTagCompound) {

		nbtTagCompound.setInteger("burnTime", burnTime);
		nbtTagCompound.setInteger("maxBurnTime", maxBurnTime);
		nbtTagCompound.setInteger("heat", heat);
	}


	@Override
	public int getLightValue() {

		if (heat == 0) return 0;
		return (int) (heat / (float) maxHeat * 7) + 6;
	}


	@Override
	public boolean canProviderTubeConnectTo(int side) {

		return side != PC_Utils.getRotation(worldObj, xCoord, yCoord, zCoord);
	}

}
