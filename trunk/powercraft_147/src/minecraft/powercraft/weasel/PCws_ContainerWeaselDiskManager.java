package powercraft.weasel;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercraft.management.PC_GresBaseWithInventory;
import powercraft.management.PC_ISpecialAccessInventory;
import powercraft.management.PC_Slot;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;

public class PCws_ContainerWeaselDiskManager extends PC_GresBaseWithInventory<PC_TileEntity> {
	
	protected List<Slot> lSlot;
	protected IInventory inventory;
	protected PC_VecI pos;
	
	public PCws_ContainerWeaselDiskManager(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, te, o);
	}

	@Override
	protected void init(Object[] o) {
		pos = new PC_VecI((Integer)o[0], (Integer)o[1], (Integer)o[2]);
	}

	@Override
	protected List<Slot> getAllSlots(List<Slot> slots) {
		lSlot = new ArrayList<Slot>();
		inventory = new WeaselDiskManagerInventory(thePlayer.worldObj, pos);
		lSlot.add(new PC_Slot(inventory, 0));
		lSlot.add(new PC_Slot(inventory, 1));
		lSlot.add(new PC_Slot(inventory, 2));
		slots.addAll(lSlot);
		return slots;
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
	
}
