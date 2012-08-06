package net.minecraft.src;

public class ItemSlab extends ItemBlock
{
    private final boolean field_77891_a;
    private final BlockHalfSlab field_77889_b;
    private final BlockHalfSlab field_77890_c;

    public ItemSlab(int par1, BlockHalfSlab par2BlockHalfSlab, BlockHalfSlab par3BlockHalfSlab, boolean par4)
    {
        super(par1);
        this.field_77889_b = par2BlockHalfSlab;
        this.field_77890_c = par3BlockHalfSlab;
        this.field_77891_a = par4;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
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
        return this.field_77889_b.getFullSlabName(par1ItemStack.getItemDamage());
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (this.field_77891_a)
        {
            return super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
        }
        else if (par1ItemStack.stackSize == 0)
        {
            return false;
        }
        else if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6))
        {
            return false;
        }
        else
        {
            int var11 = par3World.getBlockId(par4, par5, par6);
            int var12 = par3World.getBlockMetadata(par4, par5, par6);
            int var13 = var12 & 7;
            boolean var14 = (var12 & 8) != 0;

            if ((par7 == 1 && !var14 || par7 == 0 && var14) && var11 == this.field_77889_b.blockID && var13 == par1ItemStack.getItemDamage())
            {
                if (par3World.checkIfAABBIsClear(this.field_77890_c.getCollisionBoundingBoxFromPool(par3World, par4, par5, par6)) && par3World.setBlockAndMetadataWithNotify(par4, par5, par6, this.field_77890_c.blockID, var13))
                {
                    par3World.playSoundEffect((double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), this.field_77890_c.stepSound.getStepSound(), (this.field_77890_c.stepSound.getVolume() + 1.0F) / 2.0F, this.field_77890_c.stepSound.getPitch() * 0.8F);
                    --par1ItemStack.stackSize;
                }

                return true;
            }
            else
            {
                return this.func_77888_a(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7) ? true : super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
            }
        }
    }

    private boolean func_77888_a(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7)
    {
        if (par7 == 0)
        {
            --par5;
        }

        if (par7 == 1)
        {
            ++par5;
        }

        if (par7 == 2)
        {
            --par6;
        }

        if (par7 == 3)
        {
            ++par6;
        }

        if (par7 == 4)
        {
            --par4;
        }

        if (par7 == 5)
        {
            ++par4;
        }

        int var8 = par3World.getBlockId(par4, par5, par6);
        int var9 = par3World.getBlockMetadata(par4, par5, par6);
        int var10 = var9 & 7;

        if (var8 == this.field_77889_b.blockID && var10 == par1ItemStack.getItemDamage())
        {
            if (par3World.checkIfAABBIsClear(this.field_77890_c.getCollisionBoundingBoxFromPool(par3World, par4, par5, par6)) && par3World.setBlockAndMetadataWithNotify(par4, par5, par6, this.field_77890_c.blockID, var10))
            {
                par3World.playSoundEffect((double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), this.field_77890_c.stepSound.getStepSound(), (this.field_77890_c.stepSound.getVolume() + 1.0F) / 2.0F, this.field_77890_c.stepSound.getPitch() * 0.8F);
                --par1ItemStack.stackSize;
            }

            return true;
        }
        else
        {
            return false;
        }
    }
}
