package mods.betterworld.CB;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class BWCB_ContainerBrickMachine extends Container {
	private BWCB_TileEntityBlockMachineBrick machine;
	private int SmeltTime = 0;
	private int BurnTime = 0;
	private int ItemBurnTime = 0;

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	public BWCB_ContainerBrickMachine(InventoryPlayer inventoryPlayer,
			BWCB_TileEntityBlockMachineBrick tileentity) {
		this.machine = tileentity;

		int var3;

		for (var3 = 0; var3 < 3; ++var3) {
			for (int var4 = 0; var4 < 9; ++var4) {
				this.addSlotToContainer(new Slot(inventoryPlayer, var4 + var3
						* 9 + 9, 8 + var4 * 18, 118 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 9; ++var3) {
			this.addSlotToContainer(new Slot(inventoryPlayer, var3,
					8 + var3 * 18, 176));
		}
	}



	/**
	 * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
	 
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int var1 = 0; var1 < this.crafters.size(); ++var1) {
			ICrafting var2 = (ICrafting) this.crafters.get(var1);

			if (this.SmeltTime != this.blaster.smeltingTime) {
				var2.sendProgressBarUpdate(this, 0, this.blaster.smeltingTime);
			}

			if (this.BurnTime != this.blaster.furnaceBurnTime) {
				var2.sendProgressBarUpdate(this, 2,
						this.blaster.furnaceBurnTime);
			}

			if (this.ItemBurnTime != this.blaster.currentItemBurnTime) {
				var2.sendProgressBarUpdate(this, 3,
						this.blaster.currentItemBurnTime);
			}
		}

		this.SmeltTime = this.blaster.smeltingTime;
		this.BurnTime = this.blaster.furnaceBurnTime;
		this.ItemBurnTime = this.blaster.currentItemBurnTime;
	}
	
	public boolean canInteractWith(EntityPlayer var1) {
		return this.machine.isUseableByPlayer(var1);
	}
*/
	
}
