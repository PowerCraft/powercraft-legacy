package powercraft.machines;

import java.util.List;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import powercraft.management.PC_ISpecialAccessInventory;
import powercraft.management.PC_IStateReportingInventory;
import powercraft.management.PC_InvUtils;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;

public class PCma_TileEntityTransmutabox extends PC_TileEntity implements IInventory, PC_ISpecialAccessInventory, PC_IStateReportingInventory
{
    private ItemStack[] itemStacks = new ItemStack[49];
    private int burnTime = 0;
    private int loadTime = 0;

    public int getLoadTime()
    {
        return loadTime;
    }

    @Override
    public boolean insertStackIntoInventory(ItemStack stack)
    {
        return false;
    }

    @Override
    public boolean needsSpecialInserter()
    {
        return false;
    }

    @Override
    public boolean canPlayerInsertStackTo(int slot, ItemStack stack)
    {
        if (slot >= 1 && slot < 9)
        {
            return GameInfo.isFuel(stack);
        }

        return true;
    }

    @Override
    public boolean canMachineInsertStackTo(int slot, ItemStack stack)
    {
        if (slot == 0)
        {
            return false;
        }

        if (slot >= 1 && slot < 9)
        {
            return GameInfo.isFuel(stack);
        }

        return true;
    }

    @Override
    public boolean canDispenseStackFrom(int slot)
    {
        return true;
    }

    @Override
    public int getSizeInventory()
    {
        return 49;
    }

    @Override
    public ItemStack getStackInSlot(int var1)
    {
        return itemStacks[var1];
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        if (itemStacks[i] != null)
        {
            if (itemStacks[i].stackSize <= j)
            {
                ItemStack itemstack = itemStacks[i];
                itemStacks[i] = null;
                onInventoryChanged();
                return itemstack;
            }

            ItemStack itemstack1 = itemStacks[i].splitStack(j);

            if (itemStacks[i].stackSize == 0)
            {
                itemStacks[i] = null;
            }

            onInventoryChanged();
            return itemstack1;
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1)
    {
        if (itemStacks[var1] != null)
        {
            ItemStack itemstack = itemStacks[var1];
            itemStacks[var1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2)
    {
        itemStacks[var1] = var2;

        if (var2 != null && var2.stackSize > getInventoryStackLimit())
        {
            var2.stackSize = getInventoryStackLimit();
        }

        onInventoryChanged();
    }

    @Override
    public String getInvName()
    {
        return "Transmutabox Inventory";
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return true;
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest() {}

    @Override
    public boolean isContainerEmpty()
    {
        for (int i = 9; i < 49; i++)
        {
            if (getStackInSlot(i) != null)
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isContainerFull()
    {
        for (int i = 9; i < 49; i++)
        {
            if (getStackInSlot(i) == null && getStackInSlot(i + 9) != null)
            {
                return false;
            }
            else if (getStackInSlot(i) != null && getStackInSlot(i + 9) != null)
            {
                if (getStackInSlot(i).stackSize < Math.min(getStackInSlot(i).getMaxStackSize(), getInventoryStackLimit()))
                {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean hasContainerNoFreeSlots()
    {
        for (int i = 9; i < 49; i++)
        {
            if (getStackInSlot(i) == null && getStackInSlot(i + 9) != null)
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean hasInventoryPlaceFor(ItemStack itemStack)
    {
        for (int i = 9; i < 49; i++)
        {
            if (getStackInSlot(i) == null || (getStackInSlot(i).isItemEqual(itemStack) && getStackInSlot(i).stackSize < Math.min(getInventoryStackLimit(), getStackInSlot(i).getMaxStackSize())))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isContainerEmptyOf(ItemStack itemStack)
    {
        for (int i = 9; i < 49; i++)
        {
            if (getStackInSlot(i) != null && !getStackInSlot(i).isItemEqual(itemStack))
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public void updateEntity()
    {
        if (!worldObj.isRemote)
        {
            for (int i = 1; i < 9 && burnTime <= 0; i++)
            {
                if (itemStacks[i] != null)
                {
                    burnTime += PC_InvUtils.getFuelValue(itemStacks[i], 1f);

                    if (--itemStacks[i].stackSize == 0)
                    {
                        itemStacks[i] = null;
                    }
                }
            }

            if (burnTime > 0)
            {
                loadTime++;
                burnTime--;
                PC_PacketHandler.setTileEntity(this, "loadTime", loadTime);
            }

            if (loadTime > 1000)
            {
                loadTime = 0;
                change(10);
            }
        }
    }

    @Override
    public boolean canUpdate()
    {
        return true;
    }

    public void change(int i)
    {
        int changed = 0;

        if (itemStacks[0] == null)
        {
            return;
        }

        for (int j = 9; j < 49; j++)
        {
            if (itemStacks[j] != null && !itemStacks[j].isItemEqual(itemStacks[0]))
            {
                if (itemStacks[j].stackSize > i - changed)
                {
                    itemStacks[j].stackSize -= i - changed;
                    changed = i;
                }
                else
                {
                    changed += itemStacks[j].stackSize;
                    itemStacks[j] = null;
                }
            }
        }

        for (int j = 9; j < 49 && changed > 0; j++)
        {
            if (itemStacks[j] == null)
            {
                itemStacks[j] = itemStacks[0].copy();

                if (itemStacks[j].getMaxStackSize() > changed)
                {
                    itemStacks[j].stackSize = changed;
                    changed = 0;
                }
                else
                {
                    itemStacks[j].stackSize = itemStacks[j].getMaxStackSize();
                    changed -= itemStacks[j].getMaxStackSize();
                }
            }
            else if (itemStacks[j].isItemEqual(itemStacks[0]))
            {
                if (itemStacks[j].getMaxStackSize() - itemStacks[j].stackSize > changed)
                {
                    itemStacks[j].stackSize += changed;
                    changed = 0;
                }
                else
                {
                    changed -= itemStacks[j].getMaxStackSize() - itemStacks[j].stackSize;
                    itemStacks[j].stackSize = itemStacks[j].getMaxStackSize();
                }
            }
        }

        double d = 3D;
        List<Entity> list = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(xCoord - d, yCoord - d, zCoord - d,
                xCoord + d, yCoord + 6D + d, zCoord + d));

        for (int l = 0; l < list.size(); l++)
        {
            Entity entity = list.get(l);

            if (!(entity instanceof EntityItem))
            {
                entity.attackEntityFrom(DamageSource.inFire, 5);
            }
        }

        PC_PacketHandler.setTileEntity(this, "change");
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        PC_InvUtils.loadInventoryFromNBT(nbttagcompound, "Items", this);
        burnTime = nbttagcompound.getInteger("burnTime");
        loadTime = nbttagcompound.getInteger("loadTime");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        PC_InvUtils.saveInventoryToNBT(nbttagcompound, "Items", this);
        nbttagcompound.setInteger("burnTime", burnTime);
        nbttagcompound.setInteger("loadTime", loadTime);
    }

    @Override
    public void setData(Object[] o)
    {
        int p = 0;

        while (p < o.length)
        {
            String var = (String)o[p++];

            if (var.equals("change"))
            {
                worldObj.createExplosion(null, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, 1.7F, true);
                PC_Utils.playSound(xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, "random.explode", 1.5F,
                        (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
            }
            else if (var.equals("loadTime"))
            {
                loadTime = (Integer)o[p++];
            }
        }
    }

    @Override
    public Object[] getData()
    {
        return new Object[]
                {
                    "loadTime", loadTime
                };
    }
}
