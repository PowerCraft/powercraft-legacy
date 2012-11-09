package powercraft.core;

import java.util.List;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class PCco_ItemCraftingTool extends PC_Item {

	/**
	 * @param i ID
	 */
	public PCco_ItemCraftingTool(int i) {
		super(i);
		setMaxDamage(0);
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.tabTools);
		setIconIndex(0);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if(!world.isRemote)
			PC_Utils.openGres("CraftingTool", entityplayer);
		return itemstack;
	}

	@Override
	public String[] getDefaultNames() {
		return new String[]{getItemName(), "Crafting Tool"};
	}
	
}
