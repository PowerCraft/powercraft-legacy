package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockDirt extends Block
{
    protected BlockDirt(int par1, int par2)
    {
        super(par1, par2, Material.ground);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
}
