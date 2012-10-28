package net.minecraft.src;

import java.util.Random;
import net.minecraft.server.MinecraftServer;

public class BehaviorDispenseFireball extends BehaviorDefaultDispenseItem
{
    final MinecraftServer field_82504_b;

    public BehaviorDispenseFireball(MinecraftServer par1MinecraftServer)
    {
        this.field_82504_b = par1MinecraftServer;
    }

    public ItemStack func_82487_b(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        EnumFacing var3 = EnumFacing.func_82600_a(par1IBlockSource.func_82620_h());
        IPosition var4 = BlockDispenser.func_82525_a(par1IBlockSource);
        double var5 = var4.func_82615_a() + (double)((float)var3.func_82601_c() * 0.3F);
        double var7 = var4.func_82617_b();
        double var9 = var4.func_82616_c() + (double)((float)var3.func_82599_e() * 0.3F);
        World var11 = par1IBlockSource.func_82618_k();
        Random var12 = var11.rand;
        double var13 = var12.nextGaussian() * 0.05D + (double)var3.func_82601_c();
        double var15 = var12.nextGaussian() * 0.05D;
        double var17 = var12.nextGaussian() * 0.05D + (double)var3.func_82599_e();
        var11.spawnEntityInWorld(new EntitySmallFireball(var11, var5, var7, var9, var13, var15, var17));
        par2ItemStack.splitStack(1);
        return par2ItemStack;
    }

    protected void func_82485_a(IBlockSource par1IBlockSource)
    {
        par1IBlockSource.func_82618_k().playAuxSFX(1009, par1IBlockSource.func_82623_d(), par1IBlockSource.func_82622_e(), par1IBlockSource.func_82621_f(), 0);
    }
}
