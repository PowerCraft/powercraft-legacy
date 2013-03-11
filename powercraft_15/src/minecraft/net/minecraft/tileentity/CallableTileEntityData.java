package net.minecraft.tileentity;

import java.util.concurrent.Callable;

class CallableTileEntityData implements Callable
{
    final TileEntity field_94612_a;

    CallableTileEntityData(TileEntity par1TileEntity)
    {
        this.field_94612_a = par1TileEntity;
    }

    public String func_94611_a()
    {
        int i = this.field_94612_a.worldObj.getBlockMetadata(this.field_94612_a.xCoord, this.field_94612_a.yCoord, this.field_94612_a.zCoord);

        if (i < 0)
        {
            return "Unknown? (Got " + i + ")";
        }
        else
        {
            String s = String.format("%4s", new Object[] {Integer.toBinaryString(i)}).replace(" ", "0");
            return String.format("%1$d / 0x%1$X / 0b%2$s", new Object[] {Integer.valueOf(i), s});
        }
    }

    public Object call()
    {
        return this.func_94611_a();
    }
}
