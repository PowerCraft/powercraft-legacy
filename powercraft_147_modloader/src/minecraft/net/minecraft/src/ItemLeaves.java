package net.minecraft.src;

public class ItemLeaves extends ItemBlock
{
    public ItemLeaves(int par1)
    {
        super(par1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int par1)
    {
        return par1 | 4;
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    public int getIconFromDamage(int par1)
    {
        return Block.leaves.getBlockTextureFromSideAndMetadata(0, par1);
    }

    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        int var3 = par1ItemStack.getItemDamage();
        return (var3 & 1) == 1 ? ColorizerFoliage.getFoliageColorPine() : ((var3 & 2) == 2 ? ColorizerFoliage.getFoliageColorBirch() : ColorizerFoliage.getFoliageColorBasic());
    }

    public String getItemNameIS(ItemStack par1ItemStack)
    {
        int var2 = par1ItemStack.getItemDamage();

        if (var2 < 0 || var2 >= BlockLeaves.LEAF_TYPES.length)
        {
            var2 = 0;
        }

        return super.getItemName() + "." + BlockLeaves.LEAF_TYPES[var2];
    }
}
