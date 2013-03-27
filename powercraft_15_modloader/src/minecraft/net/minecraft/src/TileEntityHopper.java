package net.minecraft.src;

import java.util.List;

public class TileEntityHopper extends TileEntity implements Hopper
{
    private ItemStack[] field_94124_b = new ItemStack[5];

    /** The name that is displayed if the hopper was renamed */
    private String inventoryName;
    private int field_98048_c = -1;

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        this.field_94124_b = new ItemStack[this.getSizeInventory()];

        if (par1NBTTagCompound.hasKey("CustomName"))
        {
            this.inventoryName = par1NBTTagCompound.getString("CustomName");
        }

        this.field_98048_c = par1NBTTagCompound.getInteger("TransferCooldown");

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.field_94124_b.length)
            {
                this.field_94124_b[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.field_94124_b.length; ++var3)
        {
            if (this.field_94124_b[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.field_94124_b[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        par1NBTTagCompound.setTag("Items", var2);
        par1NBTTagCompound.setInteger("TransferCooldown", this.field_98048_c);

        if (this.isInvNameLocalized())
        {
            par1NBTTagCompound.setString("CustomName", this.inventoryName);
        }
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void onInventoryChanged()
    {
        super.onInventoryChanged();
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.field_94124_b.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int par1)
    {
        return this.field_94124_b[par1];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.field_94124_b[par1] != null)
        {
            ItemStack var3;

            if (this.field_94124_b[par1].stackSize <= par2)
            {
                var3 = this.field_94124_b[par1];
                this.field_94124_b[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.field_94124_b[par1].splitStack(par2);

                if (this.field_94124_b[par1].stackSize == 0)
                {
                    this.field_94124_b[par1] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.field_94124_b[par1] != null)
        {
            ItemStack var2 = this.field_94124_b[par1];
            this.field_94124_b[par1] = null;
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
        this.field_94124_b[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return this.isInvNameLocalized() ? this.inventoryName : "container.hopper";
    }

    /**
     * If this returns false, the inventory name will be used as an unlocalized name, and translated into the player's
     * language. Otherwise it will be used directly.
     */
    public boolean isInvNameLocalized()
    {
        return this.inventoryName != null && this.inventoryName.length() > 0;
    }

    public void func_96115_a(String par1Str)
    {
        this.inventoryName = par1Str;
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
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void openChest() {}

    public void closeChest() {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isStackValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return true;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        if (this.worldObj != null && !this.worldObj.isRemote)
        {
            --this.field_98048_c;

            if (!this.func_98047_l())
            {
                this.func_98046_c(0);
                this.func_98045_j();
            }
        }
    }

    public boolean func_98045_j()
    {
        if (this.worldObj != null && !this.worldObj.isRemote)
        {
            if (!this.func_98047_l() && BlockHopper.func_94452_d(this.getBlockMetadata()))
            {
                boolean var1 = this.func_94116_j() | func_96116_a(this);

                if (var1)
                {
                    this.func_98046_c(8);
                    this.onInventoryChanged();
                    return true;
                }
            }

            return false;
        }
        else
        {
            return false;
        }
    }

    private boolean func_94116_j()
    {
        IInventory var1 = this.func_94119_v();

        if (var1 == null)
        {
            return false;
        }
        else
        {
            for (int var2 = 0; var2 < this.getSizeInventory(); ++var2)
            {
                if (this.getStackInSlot(var2) != null)
                {
                    ItemStack var3 = this.getStackInSlot(var2).copy();
                    ItemStack var4 = func_94117_a(var1, this.decrStackSize(var2, 1), Facing.faceToSide[BlockHopper.func_94451_c(this.getBlockMetadata())]);

                    if (var4 == null || var4.stackSize == 0)
                    {
                        var1.onInventoryChanged();
                        return true;
                    }

                    this.setInventorySlotContents(var2, var3);
                }
            }

            return false;
        }
    }

    public static boolean func_96116_a(Hopper par0Hopper)
    {
        IInventory var1 = func_96118_b(par0Hopper);

        if (var1 != null)
        {
            byte var2 = 0;

            if (var1 instanceof ISidedInventory && var2 > -1)
            {
                ISidedInventory var7 = (ISidedInventory)var1;
                int[] var8 = var7.getSizeInventorySide(var2);

                for (int var5 = 0; var5 < var8.length; ++var5)
                {
                    if (func_102012_a(par0Hopper, var1, var8[var5], var2))
                    {
                        return true;
                    }
                }
            }
            else
            {
                int var3 = var1.getSizeInventory();

                for (int var4 = 0; var4 < var3; ++var4)
                {
                    if (func_102012_a(par0Hopper, var1, var4, var2))
                    {
                        return true;
                    }
                }
            }
        }
        else
        {
            EntityItem var6 = func_96119_a(par0Hopper.getWorldObj(), par0Hopper.func_96107_aA(), par0Hopper.func_96109_aB() + 1.0D, par0Hopper.func_96108_aC());

            if (var6 != null)
            {
                return func_96114_a(par0Hopper, var6);
            }
        }

        return false;
    }

    private static boolean func_102012_a(Hopper par0Hopper, IInventory par1IInventory, int par2, int par3)
    {
        ItemStack var4 = par1IInventory.getStackInSlot(par2);

        if (var4 != null && func_102013_b(par1IInventory, var4, par2, par3))
        {
            ItemStack var5 = var4.copy();
            ItemStack var6 = func_94117_a(par0Hopper, par1IInventory.decrStackSize(par2, 1), -1);

            if (var6 == null || var6.stackSize == 0)
            {
                par1IInventory.onInventoryChanged();
                return true;
            }

            par1IInventory.setInventorySlotContents(par2, var5);
        }

        return false;
    }

    public static boolean func_96114_a(IInventory par0IInventory, EntityItem par1EntityItem)
    {
        boolean var2 = false;

        if (par1EntityItem == null)
        {
            return false;
        }
        else
        {
            ItemStack var3 = par1EntityItem.getEntityItem().copy();
            ItemStack var4 = func_94117_a(par0IInventory, var3, -1);

            if (var4 != null && var4.stackSize != 0)
            {
                par1EntityItem.setEntityItemStack(var4);
            }
            else
            {
                var2 = true;
                par1EntityItem.setDead();
            }

            return var2;
        }
    }

    public static ItemStack func_94117_a(IInventory par1IInventory, ItemStack par2ItemStack, int par3)
    {
        if (par1IInventory instanceof ISidedInventory && par3 > -1)
        {
            ISidedInventory var6 = (ISidedInventory)par1IInventory;
            int[] var7 = var6.getSizeInventorySide(par3);

            for (int var5 = 0; var5 < var7.length && par2ItemStack != null && par2ItemStack.stackSize > 0; ++var5)
            {
                par2ItemStack = func_102014_c(par1IInventory, par2ItemStack, var7[var5], par3);
            }
        }
        else
        {
            int var3 = par1IInventory.getSizeInventory();

            for (int var4 = 0; var4 < var3 && par2ItemStack != null && par2ItemStack.stackSize > 0; ++var4)
            {
                par2ItemStack = func_102014_c(par1IInventory, par2ItemStack, var4, par3);
            }
        }

        if (par2ItemStack != null && par2ItemStack.stackSize == 0)
        {
            par2ItemStack = null;
        }

        return par2ItemStack;
    }

    private static boolean func_102015_a(IInventory par0IInventory, ItemStack par1ItemStack, int par2, int par3)
    {
        return !par0IInventory.isStackValidForSlot(par2, par1ItemStack) ? false : !(par0IInventory instanceof ISidedInventory) || ((ISidedInventory)par0IInventory).func_102007_a(par2, par1ItemStack, par3);
    }

    private static boolean func_102013_b(IInventory par0IInventory, ItemStack par1ItemStack, int par2, int par3)
    {
        return !(par0IInventory instanceof ISidedInventory) || ((ISidedInventory)par0IInventory).func_102008_b(par2, par1ItemStack, par3);
    }

    private static ItemStack func_102014_c(IInventory par0IInventory, ItemStack par1ItemStack, int par2, int par3)
    {
        ItemStack var4 = par0IInventory.getStackInSlot(par2);

        if (func_102015_a(par0IInventory, par1ItemStack, par2, par3))
        {
            boolean var5 = false;

            if (var4 == null)
            {
                par0IInventory.setInventorySlotContents(par2, par1ItemStack);
                par1ItemStack = null;
                var5 = true;
            }
            else if (func_94114_a(var4, par1ItemStack))
            {
                int var6 = par1ItemStack.getMaxStackSize() - var4.stackSize;
                int var7 = Math.min(par1ItemStack.stackSize, var6);
                par1ItemStack.stackSize -= var7;
                var4.stackSize += var7;
                var5 = var7 > 0;
            }

            if (var5)
            {
                if (par0IInventory instanceof TileEntityHopper)
                {
                    ((TileEntityHopper)par0IInventory).func_98046_c(8);
                }

                par0IInventory.onInventoryChanged();
            }
        }

        return par1ItemStack;
    }

    private IInventory func_94119_v()
    {
        int var1 = BlockHopper.func_94451_c(this.getBlockMetadata());
        return func_96117_b(this.getWorldObj(), (double)(this.xCoord + Facing.offsetsXForSide[var1]), (double)(this.yCoord + Facing.offsetsYForSide[var1]), (double)(this.zCoord + Facing.offsetsZForSide[var1]));
    }

    public static IInventory func_96118_b(Hopper par0Hopper)
    {
        return func_96117_b(par0Hopper.getWorldObj(), par0Hopper.func_96107_aA(), par0Hopper.func_96109_aB() + 1.0D, par0Hopper.func_96108_aC());
    }

    public static EntityItem func_96119_a(World par0World, double par1, double par3, double par5)
    {
        List var7 = par0World.selectEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getAABBPool().getAABB(par1, par3, par5, par1 + 1.0D, par3 + 1.0D, par5 + 1.0D), IEntitySelector.field_94557_a);
        return var7.size() > 0 ? (EntityItem)var7.get(0) : null;
    }

    public static IInventory func_96117_b(World par0World, double par1, double par3, double par5)
    {
        IInventory var7 = null;
        int var8 = MathHelper.floor_double(par1);
        int var9 = MathHelper.floor_double(par3);
        int var10 = MathHelper.floor_double(par5);
        TileEntity var11 = par0World.getBlockTileEntity(var8, var9, var10);

        if (var11 != null && var11 instanceof IInventory)
        {
            var7 = (IInventory)var11;

            if (var7 instanceof TileEntityChest)
            {
                int var12 = par0World.getBlockId(var8, var9, var10);
                Block var13 = Block.blocksList[var12];

                if (var13 instanceof BlockChest)
                {
                    var7 = ((BlockChest)var13).func_94442_h_(par0World, var8, var9, var10);
                }
            }
        }

        if (var7 == null)
        {
            List var14 = par0World.func_94576_a((Entity)null, AxisAlignedBB.getAABBPool().getAABB(par1, par3, par5, par1 + 1.0D, par3 + 1.0D, par5 + 1.0D), IEntitySelector.field_96566_b);

            if (var14 != null && var14.size() > 0)
            {
                var7 = (IInventory)var14.get(par0World.rand.nextInt(var14.size()));
            }
        }

        return var7;
    }

    private static boolean func_94114_a(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
        return par1ItemStack.itemID != par2ItemStack.itemID ? false : (par1ItemStack.getItemDamage() != par2ItemStack.getItemDamage() ? false : (par1ItemStack.stackSize > par1ItemStack.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(par1ItemStack, par2ItemStack)));
    }

    public double func_96107_aA()
    {
        return (double)this.xCoord;
    }

    public double func_96109_aB()
    {
        return (double)this.yCoord;
    }

    public double func_96108_aC()
    {
        return (double)this.zCoord;
    }

    public void func_98046_c(int par1)
    {
        this.field_98048_c = par1;
    }

    public boolean func_98047_l()
    {
        return this.field_98048_c > 0;
    }
}
