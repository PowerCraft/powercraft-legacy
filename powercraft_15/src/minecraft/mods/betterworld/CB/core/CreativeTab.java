package mods.betterworld.CB.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.betterworld.CB.BWCB;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTab extends CreativeTabs {
	public CreativeTab() {
		super("BW-Blocks");

	}

	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(BWCB.blockStone, 0, 0);
	}
}