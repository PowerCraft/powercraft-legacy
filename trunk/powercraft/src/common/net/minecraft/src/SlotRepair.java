package net.minecraft.src;

class SlotRepair extends Slot
{
    final World field_82875_a;

    final int field_82873_b;

    final int field_82874_c;

    final int field_82871_d;

    final ContainerRepair field_82872_e;

    SlotRepair(ContainerRepair par1ContainerRepair, IInventory par2IInventory, int par3, int par4, int par5, World par6World, int par7, int par8, int par9)
    {
        super(par2IInventory, par3, par4, par5);
        this.field_82872_e = par1ContainerRepair;
        this.field_82875_a = par6World;
        this.field_82873_b = par7;
        this.field_82874_c = par8;
        this.field_82871_d = par9;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return false;
    }

    public boolean func_82869_a(EntityPlayer par1EntityPlayer)
    {
        return (par1EntityPlayer.capabilities.isCreativeMode || par1EntityPlayer.experienceLevel >= this.field_82872_e.field_82854_e) && this.field_82872_e.field_82854_e > 0 && this.getHasStack();
    }

    public void func_82870_a(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
    {
        if (!par1EntityPlayer.capabilities.isCreativeMode)
        {
            par1EntityPlayer.func_82242_a(-this.field_82872_e.field_82854_e);
        }

        ContainerRepair.func_82851_a(this.field_82872_e).setInventorySlotContents(0, (ItemStack)null);

        if (ContainerRepair.func_82849_b(this.field_82872_e) > 0)
        {
            ItemStack var3 = ContainerRepair.func_82851_a(this.field_82872_e).getStackInSlot(1);

            if (var3 != null && var3.stackSize > ContainerRepair.func_82849_b(this.field_82872_e))
            {
                var3.stackSize -= ContainerRepair.func_82849_b(this.field_82872_e);
                ContainerRepair.func_82851_a(this.field_82872_e).setInventorySlotContents(1, var3);
            }
            else
            {
                ContainerRepair.func_82851_a(this.field_82872_e).setInventorySlotContents(1, (ItemStack)null);
            }
        }
        else
        {
            ContainerRepair.func_82851_a(this.field_82872_e).setInventorySlotContents(1, (ItemStack)null);
        }

        this.field_82872_e.field_82854_e = 0;

        if (!par1EntityPlayer.capabilities.isCreativeMode && !this.field_82875_a.isRemote && this.field_82875_a.getBlockId(this.field_82873_b, this.field_82874_c, this.field_82871_d) == Block.field_82510_ck.blockID && par1EntityPlayer.getRNG().nextFloat() < 0.12F)
        {
            int var6 = this.field_82875_a.getBlockMetadata(this.field_82873_b, this.field_82874_c, this.field_82871_d);
            int var4 = var6 & 3;
            int var5 = var6 >> 2;
            ++var5;

            if (var5 > 2)
            {
                this.field_82875_a.setBlockWithNotify(this.field_82873_b, this.field_82874_c, this.field_82871_d, 0);
                this.field_82875_a.playAuxSFX(1020, this.field_82873_b, this.field_82874_c, this.field_82871_d, 0);
            }
            else
            {
                this.field_82875_a.setBlockMetadataWithNotify(this.field_82873_b, this.field_82874_c, this.field_82871_d, var4 | var5 << 2);
                this.field_82875_a.playAuxSFX(1021, this.field_82873_b, this.field_82874_c, this.field_82871_d, 0);
            }
        }
        else if (!this.field_82875_a.isRemote)
        {
            this.field_82875_a.playAuxSFX(1021, this.field_82873_b, this.field_82874_c, this.field_82871_d, 0);
        }
    }
}
