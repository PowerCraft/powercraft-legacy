package codechicken.core.liquid;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.LiquidStack;

public class ExtendedLiquidTank implements ILiquidTank
{
    private LiquidStack liquid;
    private boolean changeType;
    private int capacity;
    
    public ExtendedLiquidTank(LiquidStack type, int capacity)
    {
        if(type == null)
        {
            liquid = new LiquidStack(0, 0);
            changeType = true;
        }
        else
            liquid = new LiquidStack(type.itemID, 0, type.itemMeta);
        this.capacity = capacity;
    }
    
    public ExtendedLiquidTank(int capacity)
    {
        this(null, capacity);
    }
    
    @Override
    public LiquidStack getLiquid()
    {
        return liquid.copy();
    }

    @Override
    public int getCapacity()
    {
        return capacity;
    }

    public boolean canAccept(LiquidStack type)
    {
        return type == null || type.itemID <= 0 || (liquid.amount == 0 && changeType) || liquid.isLiquidEqual(type);
    }

    @Override
    public int fill(LiquidStack resource, boolean doFill)
    {
        if(resource == null || resource.itemID <= 0)
            return 0;

        if(!canAccept(resource))
            return 0;
        
        int tofill = Math.min(getCapacity()-liquid.amount, resource.amount);
        if(doFill && tofill > 0)
        {
            liquid.amount+=tofill;
            liquid.itemID = resource.itemID;
            liquid.itemMeta = resource.itemMeta;
            onLiquidChanged();
        }
        
        return tofill;
    }

    @Override
    public LiquidStack drain(int maxDrain, boolean doDrain)
    {
        if(liquid.amount == 0 || maxDrain <= 0)
            return null;
        
        int todrain = Math.min(maxDrain, liquid.amount);
        if(doDrain && todrain > 0)
        {
            liquid.amount-=todrain;
            onLiquidChanged();
        }
        return new LiquidStack(liquid.itemID, todrain, liquid.itemMeta);
    }

    @Override
    public int getTankPressure()
    {
        return 0;
    }
    
    public void onLiquidChanged()
    {
    }

    public void fromTag(NBTTagCompound tag)
    {
        LiquidStack nbtLiquid = LiquidStack.loadLiquidStackFromNBT(tag);
        if(nbtLiquid != null)
            liquid = nbtLiquid;
    }
    
    public NBTTagCompound toTag()
    {
        return liquid.writeToNBT(new NBTTagCompound());
    }
}
