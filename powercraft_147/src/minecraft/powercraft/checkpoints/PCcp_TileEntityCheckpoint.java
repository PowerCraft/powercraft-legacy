package powercraft.checkpoints;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.ValueWriting;

public class PCcp_TileEntityCheckpoint extends PC_TileEntity implements IInventory {

	private boolean collideTriggerd;
	private ItemStack[] inventory = new ItemStack[36];
	
	public void setCollideTriggerd(boolean b){
		if(collideTriggerd!=b){
			collideTriggerd = b;
			PC_PacketHandler.setTileEntity(this, "collideTriggerd", collideTriggerd);
		}
	}
	
	public boolean isCollideTriggerd(){
		return collideTriggerd;
	}
	
	@Override
	public int getSizeInventory() {
		return inventory.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int var1) {
		return inventory[var1];
	}
	
	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		if (inventory[var1] != null) {
			if (inventory[var1].stackSize <= var2) {
				ItemStack itemstack = inventory[var1];
				inventory[var1] = null;
				onInventoryChanged();
				return itemstack;
			}
			ItemStack itemstack1 = inventory[var1].splitStack(var2);
			if (inventory[var1].stackSize == 0) {
				inventory[var1] = null;
			}
			onInventoryChanged();
			return itemstack1;
		} else {
			return null;
		}
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return inventory[var1];
	}
	
	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		inventory[var1] = var2;
	}
	
	@Override
	public String getInvName() {
		return "Checkpoint";
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}
	
	@Override
	public void openChest() {}
	
	@Override
	public void closeChest() {}

	@Override
	public void setData(Object[] o) {
		int p = 0;

        while (p < o.length)
        {
            String var = (String)o[p++];

            if (var.equals("collideTriggerd"))
            {
            	collideTriggerd = (Boolean)o[p++];
            }
   
        }
	}

	@Override
	public Object[] getData() {
		return new Object[]{
				"collideTriggerd", collideTriggerd
		};
	}
}
