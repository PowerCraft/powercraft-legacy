package powercraft.api.inventory;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;

public class PC_SlotNoPickup extends PC_Slot {

	public PC_SlotNoPickup() {
		super(new IInventory() {
			@Override
			public void setInventorySlotContents(int i, ItemStack itemstack) {
			}

			@Override
			public void openChest() {
			}

			@Override
			public void onInventoryChanged() {
			}

			@Override
			public boolean isUseableByPlayer(EntityPlayer entityplayer) {
				return true;
			}

			@Override
			public ItemStack getStackInSlotOnClosing(int i) {
				return null;
			}

			@Override
			public ItemStack getStackInSlot(int i) {
				return null;
			}

			@Override
			public int getSizeInventory() {
				return 1;
			}

			@Override
			public int getInventoryStackLimit() {
				return 0;
			}

			@Override
			public String getInvName() {
				return "FAKE";
			}

			@Override
			public ItemStack decrStackSize(int i, int j) {
				return null;
			}

			@Override
			public void closeChest() {
			}

			@Override
			public boolean isInvNameLocalized() {
				return false;
			}

			@Override
			public boolean isStackValidForSlot(int var1, ItemStack var2) {
				return false;
			}
		}, 0);
	}

	public PC_SlotNoPickup(IInventory inv, int slot) {
		super(inv, slot);
	}

	@Override
	public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
		return false;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		return false;
	}

	@Override
	public boolean renderTooltipWhenEmpty() {
		return true;
	}

	@Override
	public boolean renderGrayWhenEmpty() {
		return false;
	}
}