package net.minecraft.src;

/**
 * The Crafting Tool item
 * 
 * @author Ondrej Hruska
 * @copy (c) 2012
 */
public class PCco_ItemCraftingTool extends Item {

	/**
	 * @param i ID
	 */
	public PCco_ItemCraftingTool(int i) {
		super(i);
		setMaxDamage(0);
		setMaxStackSize(1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		ModLoader.openGUI(entityplayer, new PCco_GuiCraftingTool(entityplayer));

		return itemstack;
	}
}
