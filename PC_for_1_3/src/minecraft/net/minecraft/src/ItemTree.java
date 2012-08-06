package net.minecraft.src;

public class ItemTree extends ItemBlock
{
    private Block field_77892_a;

    public ItemTree(int par1, Block par2Block)
    {
        super(par1);
        this.field_77892_a = par2Block;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    public int getIconFromDamage(int par1)
    {
        return this.field_77892_a.getBlockTextureFromSideAndMetadata(2, par1);
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

        if (var2 < 0 || var2 >= BlockLog.woodType.length)
        {
            var2 = 0;
        }

        return super.getItemName() + "." + BlockLog.woodType[var2];
    }
}
