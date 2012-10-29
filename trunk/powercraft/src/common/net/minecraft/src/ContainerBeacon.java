package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class ContainerBeacon extends Container
{
    private TileEntityBeacon field_82866_e;
    private final SlotBeacon field_82864_f;
    private int field_82865_g;
    private int field_82867_h;
    private int field_82868_i;

    public ContainerBeacon(InventoryPlayer par1InventoryPlayer, TileEntityBeacon par2TileEntityBeacon)
    {
        this.field_82866_e = par2TileEntityBeacon;
        this.addSlotToContainer(this.field_82864_f = new SlotBeacon(this, par2TileEntityBeacon, 0, 136, 110));
        byte var3 = 36;
        short var4 = 137;
        int var5;

        for (var5 = 0; var5 < 3; ++var5)
        {
            for (int var6 = 0; var6 < 9; ++var6)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var6 + var5 * 9 + 9, var3 + var6 * 18, var4 + var5 * 18));
            }
        }

        for (var5 = 0; var5 < 9; ++var5)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var5, var3 + var5 * 18, 58 + var4));
        }

        this.field_82865_g = par2TileEntityBeacon.func_82130_k();
        this.field_82867_h = par2TileEntityBeacon.func_82126_i();
        this.field_82868_i = par2TileEntityBeacon.func_82132_j();
    }

    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.updateCraftingInventoryInfo(this, 0, this.field_82865_g);
        par1ICrafting.updateCraftingInventoryInfo(this, 1, this.field_82867_h);
        par1ICrafting.updateCraftingInventoryInfo(this, 2, this.field_82868_i);
    }

    /**
     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
     */
    public void updateCraftingResults()
    {
        super.updateCraftingResults();
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
        if (par1 == 0)
        {
            this.field_82866_e.func_82129_c(par2);
        }

        if (par1 == 1)
        {
            this.field_82866_e.func_82128_d(par2);
        }

        if (par1 == 2)
        {
            this.field_82866_e.func_82127_e(par2);
        }
    }

    public TileEntityBeacon func_82863_d()
    {
        return this.field_82866_e;
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.field_82866_e.isUseableByPlayer(par1EntityPlayer);
    }

    public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(par2);

        if (var4 != null && var4.getHasStack())
        {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (par2 == 0)
            {
                if (!this.mergeItemStack(var5, 1, 37, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            }
            else if (!this.field_82864_f.getHasStack() && this.field_82864_f.isItemValid(var5) && var5.stackSize == 1)
            {
                if (!this.mergeItemStack(var5, 0, 1, false))
                {
                    return null;
                }
            }
            else if (par2 >= 1 && par2 < 28)
            {
                if (!this.mergeItemStack(var5, 28, 37, false))
                {
                    return null;
                }
            }
            else if (par2 >= 28 && par2 < 37)
            {
                if (!this.mergeItemStack(var5, 1, 28, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 1, 37, false))
            {
                return null;
            }

            if (var5.stackSize == 0)
            {
                var4.putStack((ItemStack)null);
            }
            else
            {
                var4.onSlotChanged();
            }

            if (var5.stackSize == var3.stackSize)
            {
                return null;
            }

            var4.func_82870_a(par1EntityPlayer, var5);
        }

        return var3;
    }
}
