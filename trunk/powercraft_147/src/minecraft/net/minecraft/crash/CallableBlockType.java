package net.minecraft.crash;

import java.util.concurrent.Callable;
import net.minecraft.block.Block;

final class CallableBlockType implements Callable
{
    final int field_85080_a;

    CallableBlockType(int par1)
    {
        this.field_85080_a = par1;
    }

    public String func_85079_a()
    {
        try
        {
            return String.format("ID #%d (%s // %s)", new Object[] {Integer.valueOf(this.field_85080_a), Block.blocksList[this.field_85080_a].getBlockName(), Block.blocksList[this.field_85080_a].getClass().getCanonicalName()});
        }
        catch (Throwable var2)
        {
            return "ID #" + this.field_85080_a;
        }
    }

    public Object call()
    {
        return this.func_85079_a();
    }
}
