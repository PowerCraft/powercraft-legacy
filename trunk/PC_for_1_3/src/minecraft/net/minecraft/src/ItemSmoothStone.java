package net.minecraft.src;

public class ItemSmoothStone extends ItemBlock
{
    private Block field_77887_a;

    public ItemSmoothStone(int par1, Block par2Block)
    {
        super(par1);
        this.field_77887_a = par2Block;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    public int getIconFromDamage(int par1)
    {
        return this.field_77887_a.getBlockTextureFromSideAndMetadata(2, par1);
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

        if (var2 < 0 || var2 >= BlockStoneBrick.field_72188_a.length)
        {
            var2 = 0;
        }

        return super.getItemName() + "." + BlockStoneBrick.field_72188_a[var2];
    }
}
