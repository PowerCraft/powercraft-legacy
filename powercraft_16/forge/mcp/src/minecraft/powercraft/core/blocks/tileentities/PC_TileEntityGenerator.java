package powercraft.core.blocks.tileentities;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import powercraft.api.PC_Direction;
import powercraft.api.PC_FieldDescription;
import powercraft.api.PC_FuelHandler;
import powercraft.api.PC_Utils;
import powercraft.api.blocks.PC_TileEntityWithInventory;
import powercraft.api.energy.PC_IEnergyProvider;
import powercraft.api.gres.PC_GresBaseWithInventory;
import powercraft.api.gres.PC_IGresClient;
import powercraft.api.gres.PC_IGresGuiOpenHandler;
import powercraft.api.inventory.PC_Inventory;
import powercraft.core.blocks.guis.PC_ContainerGenerator;
import powercraft.core.blocks.guis.PC_GuiGenerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class PC_TileEntityGenerator extends PC_TileEntityWithInventory implements PC_IGresGuiOpenHandler, PC_IEnergyProvider {

	public static final int maxHeat = 1000;
	
	@PC_FieldDescription(sync=true)
	private int burnTime = 0;
	
	@PC_FieldDescription(sync=true)
	private int maxBurnTime = 1;
	
	private float energy = 0;
	
	@PC_FieldDescription(sync=true)
	private int heat = 0;
	
	private int oldHeat10 = 0;


	public PC_TileEntityGenerator() {
		super("Generator", new PC_Inventory[]{new PC_Inventory("Generator", 1, 64, PC_Inventory.SIDEINSERTABLE|PC_Inventory.SIDEEXTRACTABLE)});
	}


	@Override
	public float getEnergyForUsage(PC_Direction side) {

		return energy;
	}


	@Override
	public float getEnergy(PC_Direction side, float energy) {

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
			ItemStack content = getStackInSlot(0);
			if (content != null) {
				maxBurnTime = PC_FuelHandler.getItemBurnTime(content);
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
	public int getLightValue() {

		if (heat == 0) return 0;
		return (int) (heat / (float) maxHeat * 7) + 6;
	}


	@Override
	public boolean canProviderTubeConnectTo(PC_Direction side) {
		
		return side != PC_Utils.getRotation(worldObj, xCoord, yCoord, zCoord);
	}
	
	@Override
	public void onLoadedFromNBT() {
		super.onLoadedFromNBT();
		renderUpdate();
	}

}
