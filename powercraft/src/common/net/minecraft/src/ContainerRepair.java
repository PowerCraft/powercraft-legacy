package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Iterator;
import java.util.Map;

public class ContainerRepair extends Container
{
    private IInventory field_82852_f = new InventoryCraftResult();
    private IInventory field_82853_g = new InventoryRepair(this, "Repair", 2);
    private World field_82860_h;
    private int field_82861_i;
    private int field_82858_j;
    private int field_82859_k;
    public int field_82854_e = 0;
    private int field_82856_l = 0;
    private String field_82857_m;
    private final EntityPlayer field_82855_n;

    public ContainerRepair(InventoryPlayer par1InventoryPlayer, World par2World, int par3, int par4, int par5, EntityPlayer par6EntityPlayer)
    {
        this.field_82860_h = par2World;
        this.field_82861_i = par3;
        this.field_82858_j = par4;
        this.field_82859_k = par5;
        this.field_82855_n = par6EntityPlayer;
        this.addSlotToContainer(new Slot(this.field_82853_g, 0, 27, 47));
        this.addSlotToContainer(new Slot(this.field_82853_g, 1, 76, 47));
        this.addSlotToContainer(new SlotRepair(this, this.field_82852_f, 2, 134, 47, par2World, par3, par4, par5));
        int var7;

        for (var7 = 0; var7 < 3; ++var7)
        {
            for (int var8 = 0; var8 < 9; ++var8)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var8 + var7 * 9 + 9, 8 + var8 * 18, 84 + var7 * 18));
            }
        }

        for (var7 = 0; var7 < 9; ++var7)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var7, 8 + var7 * 18, 142));
        }
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        super.onCraftMatrixChanged(par1IInventory);

        if (par1IInventory == this.field_82853_g)
        {
            this.func_82848_d();
        }
    }

    public void func_82848_d()
    {
        ItemStack var1 = this.field_82853_g.getStackInSlot(0);
        this.field_82854_e = 0;
        int var2 = 0;
        byte var3 = 0;
        int var4 = 0;

        if (var1 == null)
        {
            this.field_82852_f.setInventorySlotContents(0, (ItemStack)null);
            this.field_82854_e = 0;
        }
        else
        {
            ItemStack var5 = var1.copy();
            ItemStack var6 = this.field_82853_g.getStackInSlot(1);
            Map var7 = EnchantmentHelper.func_82781_a(var5);
            int var18 = var3 + var1.func_82838_A() + (var6 == null ? 0 : var6.func_82838_A());
            this.field_82856_l = 0;
            int var8;
            int var9;
            int var10;
            int var12;
            Enchantment var21;
            Iterator var20;

            if (var6 != null)
            {
                if (var5.isItemStackDamageable() && Item.itemsList[var5.itemID].func_82789_a(var1, var6))
                {
                    var8 = Math.min(var5.getItemDamageForDisplay(), var5.getMaxDamage() / 4);

                    if (var8 <= 0)
                    {
                        this.field_82852_f.setInventorySlotContents(0, (ItemStack)null);
                        this.field_82854_e = 0;
                        return;
                    }

                    for (var9 = 0; var8 > 0 && var9 < var6.stackSize; ++var9)
                    {
                        var10 = var5.getItemDamageForDisplay() - var8;
                        var5.setItemDamage(var10);
                        var2 += Math.max(1, var8 / 100) + var7.size();
                        var8 = Math.min(var5.getItemDamageForDisplay(), var5.getMaxDamage() / 4);
                    }

                    this.field_82856_l = var9;
                }
                else
                {
                    if (var5.itemID != var6.itemID || !var5.isItemStackDamageable())
                    {
                        this.field_82852_f.setInventorySlotContents(0, (ItemStack)null);
                        this.field_82854_e = 0;
                        return;
                    }

                    if (var5.isItemStackDamageable())
                    {
                        var8 = var1.getMaxDamage() - var1.getItemDamageForDisplay();
                        var9 = var6.getMaxDamage() - var6.getItemDamageForDisplay();
                        var10 = var9 + var5.getMaxDamage() * 12 / 100;
                        int var11 = var8 + var10;
                        var12 = var5.getMaxDamage() - var11;

                        if (var12 < 0)
                        {
                            var12 = 0;
                        }

                        if (var12 < var5.getItemDamage())
                        {
                            var5.setItemDamage(var12);
                            var2 += Math.max(1, var10 / 100);
                        }
                    }

                    Map var19 = EnchantmentHelper.func_82781_a(var6);
                    var20 = var19.keySet().iterator();

                    while (var20.hasNext())
                    {
                        var10 = ((Integer)var20.next()).intValue();
                        var21 = Enchantment.enchantmentsList[var10];
                        var12 = var7.containsKey(Integer.valueOf(var10)) ? ((Integer)var7.get(Integer.valueOf(var10))).intValue() : 0;
                        int var13 = ((Integer)var19.get(Integer.valueOf(var10))).intValue();
                        int var10000;

                        if (var12 == var13)
                        {
                            ++var13;
                            var10000 = var13;
                        }
                        else
                        {
                            var10000 = Math.max(var13, var12);
                        }

                        var13 = var10000;
                        int var14 = var13 - var12;
                        boolean var15 = true;
                        Iterator var16 = var7.keySet().iterator();

                        while (var16.hasNext())
                        {
                            int var17 = ((Integer)var16.next()).intValue();

                            if (var17 != var10 && !var21.canApplyTogether(Enchantment.enchantmentsList[var17]))
                            {
                                var15 = false;
                                var2 += var14;
                            }
                        }

                        if (var15)
                        {
                            if (var13 > var21.getMaxLevel())
                            {
                                var13 = var21.getMaxLevel();
                            }

                            var7.put(Integer.valueOf(var10), Integer.valueOf(var13));
                            byte var23 = 0;

                            switch (var21.getWeight())
                            {
                                case 1:
                                    var23 = 8;
                                    break;
                                case 2:
                                    var23 = 4;
                                case 3:
                                case 4:
                                case 6:
                                case 7:
                                case 8:
                                case 9:
                                default:
                                    break;
                                case 5:
                                    var23 = 2;
                                    break;
                                case 10:
                                    var23 = 1;
                            }

                            var2 += var23 * var14;
                        }
                    }
                }
            }

            if (this.field_82857_m != null && !this.field_82857_m.equalsIgnoreCase(var1.func_82833_r()) && this.field_82857_m.length() > 0)
            {
                var4 = var1.isItemStackDamageable() ? 7 : var1.stackSize * 5;
                var2 += var4;

                if (var1.func_82837_s())
                {
                    var18 += var4 / 2;
                }

                var5.func_82834_c(this.field_82857_m);
            }

            var8 = 0;
            byte var22;

            for (var20 = var7.keySet().iterator(); var20.hasNext(); var18 += var8 + var12 * var22)
            {
                var10 = ((Integer)var20.next()).intValue();
                var21 = Enchantment.enchantmentsList[var10];
                var12 = ((Integer)var7.get(Integer.valueOf(var10))).intValue();
                var22 = 0;
                ++var8;

                switch (var21.getWeight())
                {
                    case 1:
                        var22 = 8;
                        break;
                    case 2:
                        var22 = 4;
                    case 3:
                    case 4:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    default:
                        break;
                    case 5:
                        var22 = 2;
                        break;
                    case 10:
                        var22 = 1;
                }
            }

            this.field_82854_e = var18 + var2;

            if (var2 <= 0)
            {
                var5 = null;
            }

            if (var4 == var2 && var4 > 0 && this.field_82854_e >= 40)
            {
                System.out.println("Naming an item only, cost too high; giving discount to cap cost to 39 levels");
                this.field_82854_e = 39;
            }

            if (this.field_82854_e >= 40 && !this.field_82855_n.capabilities.isCreativeMode)
            {
                var5 = null;
            }

            if (var5 != null)
            {
                var9 = var5.func_82838_A();

                if (var6 != null && var9 < var6.func_82838_A())
                {
                    var9 = var6.func_82838_A();
                }

                if (var5.func_82837_s())
                {
                    var9 -= 5;
                }

                if (var9 < 0)
                {
                    var9 = 0;
                }

                var9 += 2;
                var5.func_82841_c(var9);
                EnchantmentHelper.func_82782_a(var7, var5);
            }

            this.field_82852_f.setInventorySlotContents(0, var5);
            this.updateCraftingResults();
        }
    }

    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.updateCraftingInventoryInfo(this, 0, this.field_82854_e);
    }

    /**
     * Callback for when the crafting gui is closed.
     */
    public void onCraftGuiClosed(EntityPlayer par1EntityPlayer)
    {
        super.onCraftGuiClosed(par1EntityPlayer);

        if (!this.field_82860_h.isRemote)
        {
            for (int var2 = 0; var2 < this.field_82853_g.getSizeInventory(); ++var2)
            {
                ItemStack var3 = this.field_82853_g.getStackInSlotOnClosing(var2);

                if (var3 != null)
                {
                    par1EntityPlayer.dropPlayerItem(var3);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
        if (par1 == 0)
        {
            this.field_82854_e = par2;
        }
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.field_82860_h.getBlockId(this.field_82861_i, this.field_82858_j, this.field_82859_k) != Block.field_82510_ck.blockID ? false : par1EntityPlayer.getDistanceSq((double)this.field_82861_i + 0.5D, (double)this.field_82858_j + 0.5D, (double)this.field_82859_k + 0.5D) <= 64.0D;
    }

    public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(par2);

        if (var4 != null && var4.getHasStack())
        {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (par2 == 2)
            {
                if (!this.mergeItemStack(var5, 3, 39, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            }
            else if (par2 != 0 && par2 != 1)
            {
                if (par2 >= 3 && par2 < 39 && !this.mergeItemStack(var5, 0, 2, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 3, 39, false))
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

    public void func_82850_a(String par1Str)
    {
        this.field_82857_m = par1Str;

        if (this.getSlot(2).getHasStack())
        {
            this.getSlot(2).getStack().func_82834_c(this.field_82857_m);
        }

        this.func_82848_d();
    }

    static IInventory func_82851_a(ContainerRepair par0ContainerRepair)
    {
        return par0ContainerRepair.field_82853_g;
    }

    static int func_82849_b(ContainerRepair par0ContainerRepair)
    {
        return par0ContainerRepair.field_82856_l;
    }
}
