package net.minecraft.src;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.lwjgl.input.Keyboard;


/**
 * Direct Crafting slot, used in Crafting Tool.<br>
 * "No matter HOW it works. Just use it."
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PC_SlotSelective extends Slot {

	public PC_SlotSelective(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}
	
	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		
		if(inventory instanceof PC_ISpecialAccessInventory) {
			return ((PC_ISpecialAccessInventory) inventory).canPlayerInsertStackTo(this.slotNumber, par1ItemStack);
		}
		
		return super.isItemValid(par1ItemStack);
	}

}
