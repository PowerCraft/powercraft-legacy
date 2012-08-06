package net.minecraft.src;

public class InventoryMerchant implements IInventory
{
    private final IMerchant field_70476_a;
    private ItemStack[] field_70474_b = new ItemStack[3];
    private final EntityPlayer field_70475_c;
    private MerchantRecipe field_70472_d;
    private int field_70473_e;

    public InventoryMerchant(EntityPlayer par1EntityPlayer, IMerchant par2IMerchant)
    {
        this.field_70475_c = par1EntityPlayer;
        this.field_70476_a = par2IMerchant;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.field_70474_b.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int par1)
    {
        return this.field_70474_b[par1];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.field_70474_b[par1] != null)
        {
            ItemStack var3;

            if (par1 == 2)
            {
                var3 = this.field_70474_b[par1];
                this.field_70474_b[par1] = null;
                return var3;
            }
            else if (this.field_70474_b[par1].stackSize <= par2)
            {
                var3 = this.field_70474_b[par1];
                this.field_70474_b[par1] = null;

                if (this.func_70469_d(par1))
                {
                    this.func_70470_g();
                }

                return var3;
            }
            else
            {
                var3 = this.field_70474_b[par1].splitStack(par2);

                if (this.field_70474_b[par1].stackSize == 0)
                {
                    this.field_70474_b[par1] = null;
                }

                if (this.func_70469_d(par1))
                {
                    this.func_70470_g();
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    private boolean func_70469_d(int par1)
    {
        return par1 == 0 || par1 == 1;
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.field_70474_b[par1] != null)
        {
            ItemStack var2 = this.field_70474_b[par1];
            this.field_70474_b[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.field_70474_b[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }

        if (this.func_70469_d(par1))
        {
            this.func_70470_g();
        }
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "mob.villager";
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.field_70476_a.getCustomer() == par1EntityPlayer;
    }

    public void openChest() {}

    public void closeChest() {}

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void onInventoryChanged()
    {
        this.func_70470_g();
    }

    public void func_70470_g()
    {
        this.field_70472_d = null;
        ItemStack var1 = this.field_70474_b[0];
        ItemStack var2 = this.field_70474_b[1];

        if (var1 == null)
        {
            var1 = var2;
            var2 = null;
        }

        if (var1 == null)
        {
            this.setInventorySlotContents(2, (ItemStack)null);
        }
        else
        {
            MerchantRecipeList var3 = this.field_70476_a.func_70934_b(this.field_70475_c);

            if (var3 != null)
            {
                MerchantRecipe var4 = var3.func_77203_a(var1, var2, this.field_70473_e);

                if (var4 != null)
                {
                    this.field_70472_d = var4;
                    this.setInventorySlotContents(2, var4.getItemToSell().copy());
                }
                else if (var2 != null)
                {
                    var4 = var3.func_77203_a(var2, var1, this.field_70473_e);

                    if (var4 != null)
                    {
                        this.field_70472_d = var4;
                        this.setInventorySlotContents(2, var4.getItemToSell().copy());
                    }
                    else
                    {
                        this.setInventorySlotContents(2, (ItemStack)null);
                    }
                }
                else
                {
                    this.setInventorySlotContents(2, (ItemStack)null);
                }
            }
        }
    }

    public MerchantRecipe func_70468_h()
    {
        return this.field_70472_d;
    }

    public void func_70471_c(int par1)
    {
        this.field_70473_e = par1;
        this.func_70470_g();
    }
}
