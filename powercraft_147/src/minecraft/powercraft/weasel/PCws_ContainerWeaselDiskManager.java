package powercraft.weasel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;
import powercraft.management.gres.PC_GresBaseWithInventory;
import powercraft.management.inventory.PC_ISpecialAccessInventory;
import powercraft.management.inventory.PC_Slot;
import powercraft.management.tileentity.PC_TileEntity;

public class PCws_ContainerWeaselDiskManager extends PC_GresBaseWithInventory<PC_TileEntity> {
	
	protected IInventory inventory;
	
	public PCws_ContainerWeaselDiskManager(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, te, o);
	}

	@Override
	protected void init(Object[] o) {}

	@Override
	protected PC_Slot[] getAllSlots() {
		
		invSlots = new PC_Slot[3];
		inventory = new WeaselDiskManagerInventory(thePlayer.worldObj, tileEntity.getCoord());
		invSlots[0] = new PC_Slot(inventory, 0);
		invSlots[1] = new PC_Slot(inventory, 1);
		invSlots[2] = new PC_Slot(inventory, 2);
		
		return invSlots;
	}

	@Override
    public void onCraftGuiClosed(EntityPlayer par1EntityPlayer)
    {
		inventory.closeChest();
    }
	
	protected static class WeaselDiskManagerInventory implements IInventory, PC_ISpecialAccessInventory{

		private ItemStack is[] = new ItemStack[3];
		private World world;
		private PC_VecI pos;
		
		public WeaselDiskManagerInventory(World world, PC_VecI pos){
			this.world = world;
			this.pos = pos;
		}
		
		@Override
		public int getSizeInventory() {
			return 3;
		}

		@Override
		public ItemStack getStackInSlot(int var1) {
			return is[var1];
		}

		@Override
		public ItemStack decrStackSize(int var1, int var2) {
			ItemStack i = is[var1];
			is[var1] = null;
			return i;
		}

		@Override
		public ItemStack getStackInSlotOnClosing(int var1) {
			return is[var1];
		}

		@Override
		public void setInventorySlotContents(int var1, ItemStack var2) {
			is[var1] = var2;
		}

		@Override
		public String getInvName() {
			return "WeaselDiskManagerInventory";
		}

		@Override
		public int getInventoryStackLimit() {
			return 1;
		}

		@Override
		public void onInventoryChanged() {
			
		}

		@Override
		public boolean isUseableByPlayer(EntityPlayer var1) {
			return true;
		}

		@Override
		public void openChest() {
			
		}

		@Override
		public void closeChest() {
			if(!world.isRemote){
				for(int i=0; i<3; i++){
					if(is[i]!=null){
						ValueWriting.dropItemStack(world, is[i], pos);
					}
				}
			}
		}

		@Override
		public boolean insertStackIntoInventory(ItemStack stack) {
			for(int i=0; i<3; i++){
				if(is[i]==null){
					is[i] = stack;
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean needsSpecialInserter() {
			return false;
		}

		@Override
		public boolean canPlayerInsertStackTo(int slot, ItemStack stack) {
			return stack.itemID == PCws_App.weaselDisk.itemID;
		}

		@Override
		public boolean canMachineInsertStackTo(int slot, ItemStack stack) {
			return canPlayerInsertStackTo(slot, stack);
		}

		@Override
		public boolean canDispenseStackFrom(int slot) {
			return false;
		}

		@Override
		public boolean canDropStackFrom(int slot) {
			return false;
		}

		@Override
		public int getSlotStackLimit(int slotIndex) {
			return getInventoryStackLimit();
		}

		@Override
		public boolean canPlayerTakeStack(int slotIndex,
				EntityPlayer entityPlayer) {
			return true;
		}
		
	}
	
	@Override
	protected boolean canShiftTransfer() {
		return true;
	}
	
}
