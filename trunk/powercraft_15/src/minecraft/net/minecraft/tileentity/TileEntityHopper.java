package net.minecraft.tileentity;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHopper;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

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
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");
        this.field_94124_b = new ItemStack[this.getSizeInventory()];

        if (par1NBTTagCompound.hasKey("CustomName"))
        {
            this.inventoryName = par1NBTTagCompound.getString("CustomName");
        }

        this.field_98048_c = par1NBTTagCompound.getInteger("TransferCooldown");

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.field_94124_b.length)
            {
                this.field_94124_b[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.field_94124_b.length; ++i)
        {
            if (this.field_94124_b[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.field_94124_b[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);
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
            ItemStack itemstack;

            if (this.field_94124_b[par1].stackSize <= par2)
            {
                itemstack = this.field_94124_b[par1];
                this.field_94124_b[par1] = null;
                return itemstack;
            }
            else
            {
                itemstack = this.field_94124_b[par1].splitStack(par2);

                if (this.field_94124_b[par1].stackSize == 0)
                {
                    this.field_94124_b[par1] = null;
                }

                return itemstack;
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
            ItemStack itemstack = this.field_94124_b[par1];
            this.field_94124_b[par1] = null;
            return itemstack;
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
                boolean flag = this.func_94116_j() | func_96116_a(this);

                if (flag)
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
        IInventory iinventory = this.func_94119_v();

        if (iinventory == null)
        {
            return false;
        }
        else
        {
            for (int i = 0; i < this.getSizeInventory(); ++i)
            {
                if (this.getStackInSlot(i) != null)
                {
                    ItemStack itemstack = this.getStackInSlot(i).copy();
                    ItemStack itemstack1 = func_94117_a(iinventory, this.decrStackSize(i, 1), Facing.faceToSide[BlockHopper.func_94451_c(this.getBlockMetadata())]);

                    if (itemstack1 == null || itemstack1.stackSize == 0)
                    {
                        iinventory.onInventoryChanged();
                        return true;
                    }

                    this.setInventorySlotContents(i, itemstack);
                }
            }

            return false;
        }
    }

    public static boolean func_96116_a(Hopper par0Hopper)
    {
        IInventory iinventory = func_96118_b(par0Hopper);

        if (iinventory != null)
        {
            byte b0 = 0;

            if (iinventory instanceof ISidedInventory && b0 > -1)
            {
                ISidedInventory isidedinventory = (ISidedInventory)iinventory;
                int[] aint = isidedinventory.getSizeInventorySide(b0);

                for (int i = 0; i < aint.length; ++i)
                {
                    if (func_102012_a(par0Hopper, iinventory, aint[i], b0))
                    {
                        return true;
                    }
                }
            }
            else
            {
                int j = iinventory.getSizeInventory();

                for (int k = 0; k < j; ++k)
                {
                    if (func_102012_a(par0Hopper, iinventory, k, b0))
                    {
                        return true;
                    }
                }
            }
        }
        else
        {
            EntityItem entityitem = func_96119_a(par0Hopper.getWorldObj(), par0Hopper.func_96107_aA(), par0Hopper.func_96109_aB() + 1.0D, par0Hopper.func_96108_aC());

            if (entityitem != null)
            {
                return func_96114_a(par0Hopper, entityitem);
            }
        }

        return false;
    }

    private static boolean func_102012_a(Hopper par0Hopper, IInventory par1IInventory, int par2, int par3)
    {
        ItemStack itemstack = par1IInventory.getStackInSlot(par2);

        if (itemstack != null && func_102013_b(par1IInventory, itemstack, par2, par3))
        {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = func_94117_a(par0Hopper, par1IInventory.decrStackSize(par2, 1), -1);

            if (itemstack2 == null || itemstack2.stackSize == 0)
            {
                par1IInventory.onInventoryChanged();
                return true;
            }

            par1IInventory.setInventorySlotContents(par2, itemstack1);
        }

        return false;
    }

    public static boolean func_96114_a(IInventory par0IInventory, EntityItem par1EntityItem)
    {
        boolean flag = false;

        if (par1EntityItem == null)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = par1EntityItem.getEntityItem().copy();
            ItemStack itemstack1 = func_94117_a(par0IInventory, itemstack, -1);

            if (itemstack1 != null && itemstack1.stackSize != 0)
            {
                par1EntityItem.setEntityItemStack(itemstack1);
            }
            else
            {
                flag = true;
                par1EntityItem.setDead();
            }

            return flag;
        }
    }

    public static ItemStack func_94117_a(IInventory par1IInventory, ItemStack par2ItemStack, int par3)
    {
        if (par1IInventory instanceof ISidedInventory && par3 > -1)
        {
            ISidedInventory isidedinventory = (ISidedInventory)par1IInventory;
            int[] aint = isidedinventory.getSizeInventorySide(par3);

            for (int j = 0; j < aint.length && par2ItemStack != null && par2ItemStack.stackSize > 0; ++j)
            {
                par2ItemStack = func_102014_c(par1IInventory, par2ItemStack, aint[j], par3);
            }
        }
        else
        {
            int k = par1IInventory.getSizeInventory();

            for (int l = 0; l < k && par2ItemStack != null && par2ItemStack.stackSize > 0; ++l)
            {
                par2ItemStack = func_102014_c(par1IInventory, par2ItemStack, l, par3);
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
        ItemStack itemstack1 = par0IInventory.getStackInSlot(par2);

        if (func_102015_a(par0IInventory, par1ItemStack, par2, par3))
        {
            boolean flag = false;

            if (itemstack1 == null)
            {
                par0IInventory.setInventorySlotContents(par2, par1ItemStack);
                par1ItemStack = null;
                flag = true;
            }
            else if (func_94114_a(itemstack1, par1ItemStack))
            {
                int k = par1ItemStack.getMaxStackSize() - itemstack1.stackSize;
                int l = Math.min(par1ItemStack.stackSize, k);
                par1ItemStack.stackSize -= l;
                itemstack1.stackSize += l;
                flag = l > 0;
            }

            if (flag)
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
        int i = BlockHopper.func_94451_c(this.getBlockMetadata());
        return func_96117_b(this.getWorldObj(), (double)(this.xCoord + Facing.offsetsXForSide[i]), (double)(this.yCoord + Facing.offsetsYForSide[i]), (double)(this.zCoord + Facing.offsetsZForSide[i]));
    }

    public static IInventory func_96118_b(Hopper par0Hopper)
    {
        return func_96117_b(par0Hopper.getWorldObj(), par0Hopper.func_96107_aA(), par0Hopper.func_96109_aB() + 1.0D, par0Hopper.func_96108_aC());
    }

    public static EntityItem func_96119_a(World par0World, double par1, double par3, double par5)
    {
        List list = par0World.selectEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getAABBPool().getAABB(par1, par3, par5, par1 + 1.0D, par3 + 1.0D, par5 + 1.0D), IEntitySelector.field_94557_a);
        return list.size() > 0 ? (EntityItem)list.get(0) : null;
    }

    public static IInventory func_96117_b(World par0World, double par1, double par3, double par5)
    {
        IInventory iinventory = null;
        int i = MathHelper.floor_double(par1);
        int j = MathHelper.floor_double(par3);
        int k = MathHelper.floor_double(par5);
        TileEntity tileentity = par0World.getBlockTileEntity(i, j, k);

        if (tileentity != null && tileentity instanceof IInventory)
        {
            iinventory = (IInventory)tileentity;

            if (iinventory instanceof TileEntityChest)
            {
                int l = par0World.getBlockId(i, j, k);
                Block block = Block.blocksList[l];

                if (block instanceof BlockChest)
                {
                    iinventory = ((BlockChest)block).func_94442_h_(par0World, i, j, k);
                }
            }
        }

        if (iinventory == null)
        {
            List list = par0World.func_94576_a((Entity)null, AxisAlignedBB.getAABBPool().getAABB(par1, par3, par5, par1 + 1.0D, par3 + 1.0D, par5 + 1.0D), IEntitySelector.field_96566_b);

            if (list != null && list.size() > 0)
            {
                iinventory = (IInventory)list.get(par0World.rand.nextInt(list.size()));
            }
        }

        return iinventory;
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
