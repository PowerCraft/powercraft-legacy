package powercraft.logic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.management.PC_ISpecialAccessInventory;
import powercraft.management.PC_InvUtils;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;

public class PClo_TileEntitySpecial extends PC_TileEntity implements IInventory, PC_ISpecialAccessInventory
{
    private ItemStack inv[] = new ItemStack[1];
    private int type = 0;

    public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        type = stack.getItemDamage();
    }

    public int getType()
    {
        return type;
    }

    @Override
    public void updateEntity()
    {
        int nextUpdate = 0;
        boolean shouldState = false;
        int rot = PClo_BlockSpecial.getRotation_static(GameInfo.getMD(worldObj, xCoord, yCoord, zCoord));
        int xAdd = 0, zAdd = 0;

        if (rot == 0)
        {
            zAdd = 1;
        }
        else if (rot == 1)
        {
            xAdd = -1;
        }
        else if (rot == 2)
        {
            zAdd = -1;
        }
        else if (rot == 3)
        {
            xAdd = 1;
        }

        switch (type)
        {
            case PClo_SpecialType.DAY:
                shouldState = worldObj.isDaytime();
                break;

            case PClo_SpecialType.NIGHT:
                shouldState = !worldObj.isDaytime();
                break;

            case PClo_SpecialType.RAIN:
                shouldState = worldObj.isRaining();
                break;

            case PClo_SpecialType.CHEST_EMPTY:
                shouldState = GameInfo.isChestEmpty(worldObj, xCoord + xAdd, yCoord, zCoord + zAdd, inv[0]);
                break;

            case PClo_SpecialType.CHEST_FULL:
                shouldState = GameInfo.isChestFull(worldObj, xCoord + xAdd, yCoord, zCoord + zAdd, inv[0]);
                break;

            case PClo_SpecialType.SPECIAL:
                ValueWriting.preventSpawnerSpawning(worldObj, xCoord + 1, yCoord, zCoord);
                ValueWriting.preventSpawnerSpawning(worldObj, xCoord - 1, yCoord, zCoord);
                ValueWriting.preventSpawnerSpawning(worldObj, xCoord, yCoord + 1, zCoord);
                ValueWriting.preventSpawnerSpawning(worldObj, xCoord, yCoord, zCoord + 1);
                ValueWriting.preventSpawnerSpawning(worldObj, xCoord, yCoord, zCoord - 1);

            default:
                return;
        }

        if (PClo_BlockSpecial.isActive(worldObj, xCoord, yCoord, zCoord) != shouldState)
        {
            worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, GameInfo.getBID(worldObj, xCoord, yCoord, zCoord), 1);
        }
    }

    @Override
    public boolean canUpdate()
    {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        PC_InvUtils.loadInventoryFromNBT(nbtTagCompound, "Items", this);
        type = nbtTagCompound.getInteger("type");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        PC_InvUtils.saveInventoryToNBT(nbtTagCompound, "Items", this);
        nbtTagCompound.setInteger("type", type);
    }

    @Override
    public void setData(Object[] o)
    {
        int p = 0;

        while (p < o.length)
        {
            String var = (String)o[p++];

            if (var.equals("type"))
            {
                type = (Integer)o[p++];
            }
        }

        ValueWriting.hugeUpdate(worldObj, xCoord, yCoord, zCoord);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public Object[] getData()
    {
        return new Object[]
                {
                    "type", type
                };
    }

    @Override
    public boolean insertStackIntoInventory(ItemStack stack)
    {
        return false;
    }

    @Override
    public boolean needsSpecialInserter()
    {
        return true;
    }

    @Override
    public boolean canPlayerInsertStackTo(int slot, ItemStack stack)
    {
        return true;
    }

    @Override
    public boolean canMachineInsertStackTo(int slot, ItemStack stack)
    {
        return false;
    }

    @Override
    public boolean canDispenseStackFrom(int slot)
    {
        return false;
    }

    @Override
    public int getSizeInventory()
    {
        return inv.length;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return inv[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        if (inv[i] != null)
        {
            if (inv[i].stackSize <= j)
            {
                ItemStack itemstack = inv[i];
                inv[i] = null;
                onInventoryChanged();
                return itemstack;
            }

            ItemStack itemstack1 = inv[i].splitStack(j);

            if (inv[i].stackSize == 0)
            {
                inv[i] = null;
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
    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (inv[i] != null)
        {
            ItemStack itemstack = inv[i];
            inv[i] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        inv[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }

        onInventoryChanged();
    }

    @Override
    public String getInvName()
    {
        return "Special Inventory";
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return false;
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest() {}

	@Override
	public boolean canDropStackFrom(int slot) {
		return true;
	}
}
