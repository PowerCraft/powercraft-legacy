package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class BehaviorDispenseMinecart extends BehaviorDefaultDispenseItem
{
    private final BehaviorDefaultDispenseItem field_82491_c;

    final MinecraftServer field_82490_b;

    public BehaviorDispenseMinecart(MinecraftServer par1MinecraftServer)
    {
        this.field_82490_b = par1MinecraftServer;
        this.field_82491_c = new BehaviorDefaultDispenseItem();
    }

    public ItemStack func_82487_b(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        EnumFacing var3 = EnumFacing.func_82600_a(par1IBlockSource.func_82620_h());
        World var4 = par1IBlockSource.func_82618_k();
        double var5 = par1IBlockSource.func_82615_a() + (double)((float)var3.func_82601_c() * 1.125F);
        double var7 = par1IBlockSource.func_82617_b();
        double var9 = par1IBlockSource.func_82616_c() + (double)((float)var3.func_82599_e() * 1.125F);
        int var11 = par1IBlockSource.func_82623_d() + var3.func_82601_c();
        int var12 = par1IBlockSource.func_82622_e();
        int var13 = par1IBlockSource.func_82621_f() + var3.func_82599_e();
        int var14 = var4.getBlockId(var11, var12, var13);
        double var15;

        if (BlockRail.isRailBlock(var14))
        {
            var15 = 0.0D;
        }
        else
        {
            if (var14 != 0 || !BlockRail.isRailBlock(var4.getBlockId(var11, var12 - 1, var13)))
            {
                return this.field_82491_c.func_82482_a(par1IBlockSource, par2ItemStack);
            }

            var15 = -1.0D;
        }

        EntityMinecart var17 = new EntityMinecart(var4, var5, var7 + var15, var9, ((ItemMinecart)par2ItemStack.getItem()).minecartType);
        var4.spawnEntityInWorld(var17);
        par2ItemStack.splitStack(1);
        return par2ItemStack;
    }

    protected void func_82485_a(IBlockSource par1IBlockSource)
    {
        par1IBlockSource.func_82618_k().playAuxSFX(1000, par1IBlockSource.func_82623_d(), par1IBlockSource.func_82622_e(), par1IBlockSource.func_82621_f(), 0);
    }
}
