package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class ItemSandStone extends ItemBlock
{
    /** Instance of Block. */
    private Block theBlock;

    public ItemSandStone(int par1, Block par2Block)
    {
        super(par1);
        this.theBlock = par2Block;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets an icon index based on an item's damage value
     */
    public int getIconFromDamage(int par1)
    {
        return this.theBlock.getBlockTextureFromSideAndMetadata(2, par1);
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int par1)
    {
        return par1;
    }

    public String getItemNameIS(ItemStack par1ItemStack)
    {
        int var2 = par1ItemStack.getItemDamage();

        if (var2 < 0 || var2 >= BlockSandStone.SAND_STONE_TYPES.length)
        {
            var2 = 0;
        }

        return super.getItemName() + "." + BlockSandStone.SAND_STONE_TYPES[var2];
    }
}
