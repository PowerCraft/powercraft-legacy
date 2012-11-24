package net.minecraftforge.liquids;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

public class LiquidStack
{
    public int itemID;
    public int amount;
    public int itemMeta;

    private LiquidStack()
    {
    }

    public LiquidStack(int itemID, int amount)
    {
        this(itemID, amount, 0);
    }

    public LiquidStack(Item item, int amount)
    {
        this(item.shiftedIndex, amount, 0);
    }

    public LiquidStack(Block block, int amount)
    {
        this(block.blockID, amount, 0);
    }

    public LiquidStack(int itemID, int amount, int itemDamage)
    {
        this.itemID = itemID;
        this.amount = amount;
        this.itemMeta = itemDamage;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setShort("Id", (short) itemID);
        nbttagcompound.setInteger("Amount", amount);
        nbttagcompound.setShort("Meta", (short) itemMeta);
        return nbttagcompound;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        itemID = nbttagcompound.getShort("Id");
        amount = nbttagcompound.getInteger("Amount");
        itemMeta = nbttagcompound.getShort("Meta");
    }

    public LiquidStack copy()
    {
        return new LiquidStack(itemID, amount, itemMeta);
    }

    public boolean isLiquidEqual(LiquidStack other)
    {
        if (other == null)
        {
            return false;
        }

        return itemID == other.itemID && itemMeta == other.itemMeta;
    }

    public boolean containsLiquid(LiquidStack other)
    {
        if (!isLiquidEqual(other))
        {
            return false;
        }

        return amount >= other.amount;
    }

    public boolean isLiquidEqual(ItemStack other)
    {
        if (other == null)
        {
            return false;
        }

        return itemID == other.itemID && itemMeta == other.getItemDamage();
    }

    public ItemStack asItemStack()
    {
        return new ItemStack(itemID, 1, itemMeta);
    }

    public static LiquidStack loadLiquidStackFromNBT(NBTTagCompound nbttagcompound)
    {
        LiquidStack liquidstack = new LiquidStack();
        liquidstack.readFromNBT(nbttagcompound);
        return liquidstack.itemID == 0 ? null : liquidstack;
    }
}
