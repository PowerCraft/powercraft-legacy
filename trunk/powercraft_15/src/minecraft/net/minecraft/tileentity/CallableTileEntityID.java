package net.minecraft.tileentity;

import java.util.concurrent.Callable;
import net.minecraft.block.Block;

class CallableTileEntityID implements Callable
{
    final TileEntity field_94610_a;

    CallableTileEntityID(TileEntity par1TileEntity)
    {
        this.field_94610_a = par1TileEntity;
    }

    public String func_94609_a()
    {
        int i = this.field_94610_a.worldObj.getBlockId(this.field_94610_a.xCoord, this.field_94610_a.yCoord, this.field_94610_a.zCoord);

        try
        {
            return String.format("ID #%d (%s // %s)", new Object[] {Integer.valueOf(i), Block.blocksList[i].getUnlocalizedName(), Block.blocksList[i].getClass().getCanonicalName()});
        }
        catch (Throwable throwable)
        {
            return "ID #" + i;
        }
    }

    public Object call()
    {
        return this.func_94609_a();
    }
}
