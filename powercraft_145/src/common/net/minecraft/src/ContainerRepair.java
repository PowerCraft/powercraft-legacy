package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Iterator;
import java.util.Map;

public class ContainerRepair extends Container
{
    /** Here comes out item you merged and/or renamed. */
    private IInventory outputSlot = new InventoryCraftResult();

    /**
     * The 2slots where you put your items in that you want to merge and/or rename.
     */
    private IInventory inputSlots = new InventoryRepair(this, "Repair", 2);
    private World theWorld;
    private int field_82861_i;
    private int field_82858_j;
    private int field_82859_k;

    /** The maximum cost of repairing/renaming in the anvil. */
    public int maximumCost = 0;
    private int field_82856_l = 0;
    private String field_82857_m;

    /** The player that has this container open. */
    private final EntityPlayer thePlayer;

    public ContainerRepair(InventoryPlayer par1InventoryPlayer, World par2World, int par3, int par4, int par5, EntityPlayer par6EntityPlayer)
    {
        this.theWorld = par2World;
        this.field_82861_i = par3;
        this.field_82858_j = par4;
        this.field_82859_k = par5;
        this.thePlayer = par6EntityPlayer;
        this.addSlotToContainer(new Slot(this.inputSlots, 0, 27, 47));
        this.addSlotToContainer(new Slot(this.inputSlots, 1, 76, 47));
        this.addSlotToContainer(new SlotRepair(this, this.outputSlot, 2, 134, 47, par2World, par3, par4, par5));
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

        if (par1IInventory == this.inputSlots)
        {
            this.func_82848_d();
        }
    }

    public void func_82848_d()
    {
        ItemStack var1 = this.inputSlots.getStackInSlot(0);
        this.maximumCost = 0;
        int var2 = 0;
        byte var3 = 0;
        int var4 = 0;

        if (var1 == null)
        {
            this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
            this.maximumCost = 0;
        }
        else
        {
            ItemStack var5 = var1.copy();
            ItemStack var6 = this.inputSlots.getStackInSlot(1);
            Map var7 = EnchantmentHelper.getEnchantments(var5);
            int var18 = var3 + var1.getRepairCost() + (var6 == null ? 0 : var6.getRepairCost());
            this.field_82856_l = 0;
            int var8;
            int var9;
            int var10;
            int var12;
            Enchantment var21;
            Iterator var20;

            if (var6 != null)
            {
                if (var5.isItemStackDamageable() && Item.itemsList[var5.itemID].getIsRepairable(var1, var6))
                {
                    var8 = Math.min(var5.getItemDamageForDisplay(), var5.getMaxDamage() / 4);

                    if (var8 <= 0)
                    {
                        this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
                        this.maximumCost = 0;
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
                        this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
                        this.maximumCost = 0;
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

                    Map var19 = EnchantmentHelper.getEnchantments(var6);
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

            if (this.field_82857_m != null && !this.field_82857_m.equalsIgnoreCase(var1.getDisplayName()) && this.field_82857_m.length() > 0)
            {
                var4 = var1.isItemStackDamageable() ? 7 : var1.stackSize * 5;
                var2 += var4;

                if (var1.hasDisplayName())
                {
                    var18 += var4 / 2;
                }

                var5.setItemName(this.field_82857_m);
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

            this.maximumCost = var18 + var2;

            if (var2 <= 0)
            {
                var5 = null;
            }

            if (var4 == var2 && var4 > 0 && this.maximumCost >= 40)
            {
                System.out.println("Naming an item only, cost too high; giving discount to cap cost to 39 levels");
                this.maximumCost = 39;
            }

            if (this.maximumCost >= 40 && !this.thePlayer.capabilities.isCreativeMode)
            {
                var5 = null;
            }

            if (var5 != null)
            {
                var9 = var5.getRepairCost();

                if (var6 != null && var9 < var6.getRepairCost())
                {
                    var9 = var6.getRepairCost();
                }

                if (var5.hasDisplayName())
                {
                    var9 -= 9;
                }

                if (var9 < 0)
                {
                    var9 = 0;
                }

                var9 += 2;
                var5.setRepairCost(var9);
                EnchantmentHelper.setEnchantments(var7, var5);
            }

            this.outputSlot.setInventorySlotContents(0, var5);
            this.updateCraftingResults();
        }
    }

    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, this.maximumCost);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
        if (par1 == 0)
        {
            this.maximumCost = par2;
        }
    }

    /**
     * Callback for when the crafting gui is closed.
     */
    public void onCraftGuiClosed(EntityPlayer par1EntityPlayer)
    {
        super.onCraftGuiClosed(par1EntityPlayer);

        if (!this.theWorld.isRemote)
        {
            for (int var2 = 0; var2 < this.inputSlots.getSizeInventory(); ++var2)
            {
                ItemStack var3 = this.inputSlots.getStackInSlotOnClosing(var2);

                if (var3 != null)
                {
                    par1EntityPlayer.dropPlayerItem(var3);
                }
            }
        }
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.theWorld.getBlockId(this.field_82861_i, this.field_82858_j, this.field_82859_k) != Block.anvil.blockID ? false : par1EntityPlayer.getDistanceSq((double)this.field_82861_i + 0.5D, (double)this.field_82858_j + 0.5D, (double)this.field_82859_k + 0.5D) <= 64.0D;
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
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

            var4.onPickupFromSlot(par1EntityPlayer, var5);
        }

        return var3;
    }

    public void func_82850_a(String par1Str)
    {
        this.field_82857_m = par1Str;

        if (this.getSlot(2).getHasStack())
        {
            this.getSlot(2).getStack().setItemName(this.field_82857_m);
        }

        this.func_82848_d();
    }

    static IInventory func_82851_a(ContainerRepair par0ContainerRepair)
    {
        return par0ContainerRepair.inputSlots;
    }

    static int func_82849_b(ContainerRepair par0ContainerRepair)
    {
        return par0ContainerRepair.field_82856_l;
    }
}
