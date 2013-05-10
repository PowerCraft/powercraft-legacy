package mods.betterworld.DeCB.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.betterworld.CB.BWCB;
import mods.betterworld.DeCB.BWDeCB;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTab extends CreativeTabs {
	public CreativeTab() {
		super("BW-Deco");

	}

	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(BWDeCB.blockWallStone, 0, 0);
	}
}