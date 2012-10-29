package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class BehaviorMobEggDispense extends BehaviorDefaultDispenseItem
{
    /** Gets Minecraft Server players. */
    final MinecraftServer minecraftServerPlayers;

    public BehaviorMobEggDispense(MinecraftServer par1)
    {
        this.minecraftServerPlayers = par1;
    }

    public ItemStack func_82487_b(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        EnumFacing var3 = EnumFacing.func_82600_a(par1IBlockSource.func_82620_h());
        double var4 = par1IBlockSource.func_82615_a() + (double)var3.func_82601_c();
        double var6 = (double)((float)par1IBlockSource.func_82622_e() + 0.2F);
        double var8 = par1IBlockSource.func_82616_c() + (double)var3.func_82599_e();
        ItemMonsterPlacer.spawnCreature(par1IBlockSource.func_82618_k(), par2ItemStack.getItemDamage(), var4, var6, var8);
        par2ItemStack.splitStack(1);
        return par2ItemStack;
    }

    protected void func_82485_a(IBlockSource par1IBlockSource)
    {
        par1IBlockSource.func_82618_k().playAuxSFX(1002, par1IBlockSource.func_82623_d(), par1IBlockSource.func_82622_e(), par1IBlockSource.func_82621_f(), 0);
    }
}
