package net.minecraft.src;

public abstract class BehaviorProjectileDispense extends BehaviorDefaultDispenseItem
{
    public ItemStack func_82487_b(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        World var3 = par1IBlockSource.func_82618_k();
        IPosition var4 = BlockDispenser.func_82525_a(par1IBlockSource);
        EnumFacing var5 = EnumFacing.func_82600_a(par1IBlockSource.func_82620_h());
        IProjectile var6 = this.func_82499_a(var3, var4);
        var6.setThrowableHeading((double)var5.func_82601_c(), 0.10000000149011612D, (double)var5.func_82599_e(), this.func_82500_b(), this.func_82498_a());
        var3.spawnEntityInWorld((Entity)var6);
        par2ItemStack.splitStack(1);
        return par2ItemStack;
    }

    protected void func_82485_a(IBlockSource par1IBlockSource)
    {
        par1IBlockSource.func_82618_k().playAuxSFX(1002, par1IBlockSource.func_82623_d(), par1IBlockSource.func_82622_e(), par1IBlockSource.func_82621_f(), 0);
    }

    protected abstract IProjectile func_82499_a(World var1, IPosition var2);

    protected float func_82498_a()
    {
        return 6.0F;
    }

    protected float func_82500_b()
    {
        return 1.1F;
    }
}
