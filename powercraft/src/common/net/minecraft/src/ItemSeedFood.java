package net.minecraft.src;

public class ItemSeedFood extends ItemFood
{
    private int field_82808_b;
    private int field_82809_c;

    public ItemSeedFood(int par1, int par2, float par3, int par4, int par5)
    {
        super(par1, par2, par3, false);
        this.field_82808_b = par4;
        this.field_82809_c = par5;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (par7 != 1)
        {
            return false;
        }
        else if (par2EntityPlayer.func_82247_a(par4, par5, par6, par7, par1ItemStack) && par2EntityPlayer.func_82247_a(par4, par5 + 1, par6, par7, par1ItemStack))
        {
            int var11 = par3World.getBlockId(par4, par5, par6);

            if (var11 == this.field_82809_c && par3World.isAirBlock(par4, par5 + 1, par6))
            {
                par3World.setBlockWithNotify(par4, par5 + 1, par6, this.field_82808_b);
                --par1ItemStack.stackSize;
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
}
