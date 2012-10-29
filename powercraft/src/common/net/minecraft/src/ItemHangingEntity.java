package net.minecraft.src;

public class ItemHangingEntity extends Item
{
    private final Class field_82811_a;

    public ItemHangingEntity(int par1, Class par2Class)
    {
        super(par1);
        this.field_82811_a = par2Class;
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (par7 == 0)
        {
            return false;
        }
        else if (par7 == 1)
        {
            return false;
        }
        else
        {
            int var11 = Direction.vineGrowth[par7];
            EntityHanging var12 = this.func_82810_a(par3World, par4, par5, par6, var11);

            if (!par2EntityPlayer.func_82247_a(par4, par5, par6, par7, par1ItemStack))
            {
                return false;
            }
            else
            {
                if (var12 != null && var12.onValidSurface())
                {
                    if (!par3World.isRemote)
                    {
                        par3World.spawnEntityInWorld(var12);
                    }

                    --par1ItemStack.stackSize;
                }

                return true;
            }
        }
    }

    private EntityHanging func_82810_a(World par1World, int par2, int par3, int par4, int par5)
    {
        return (EntityHanging)(this.field_82811_a == EntityPainting.class ? new EntityPainting(par1World, par2, par3, par4, par5) : (this.field_82811_a == EntityItemFrame.class ? new EntityItemFrame(par1World, par2, par3, par4, par5) : null));
    }
}
