package net.minecraft.src;

public class BehaviorDefaultDispenseItem implements IBehaviorDispenseItem
{
    public final ItemStack func_82482_a(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        ItemStack var3 = this.func_82487_b(par1IBlockSource, par2ItemStack);
        this.func_82485_a(par1IBlockSource);
        this.func_82489_a(par1IBlockSource, EnumFacing.func_82600_a(par1IBlockSource.func_82620_h()));
        return var3;
    }

    protected ItemStack func_82487_b(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        EnumFacing var3 = EnumFacing.func_82600_a(par1IBlockSource.func_82620_h());
        IPosition var4 = BlockDispenser.func_82525_a(par1IBlockSource);
        ItemStack var5 = par2ItemStack.splitStack(1);
        func_82486_a(par1IBlockSource.func_82618_k(), var5, 6, var3, var4);
        return par2ItemStack;
    }

    public static void func_82486_a(World par0World, ItemStack par1ItemStack, int par2, EnumFacing par3EnumFacing, IPosition par4IPosition)
    {
        double var5 = par4IPosition.func_82615_a();
        double var7 = par4IPosition.func_82617_b();
        double var9 = par4IPosition.func_82616_c();
        EntityItem var11 = new EntityItem(par0World, var5, var7 - 0.3D, var9, par1ItemStack);
        double var12 = par0World.rand.nextDouble() * 0.1D + 0.2D;
        var11.motionX = (double)par3EnumFacing.func_82601_c() * var12;
        var11.motionY = 0.20000000298023224D;
        var11.motionZ = (double)par3EnumFacing.func_82599_e() * var12;
        var11.motionX += par0World.rand.nextGaussian() * 0.007499999832361937D * (double)par2;
        var11.motionY += par0World.rand.nextGaussian() * 0.007499999832361937D * (double)par2;
        var11.motionZ += par0World.rand.nextGaussian() * 0.007499999832361937D * (double)par2;
        par0World.spawnEntityInWorld(var11);
    }

    protected void func_82485_a(IBlockSource par1IBlockSource)
    {
        par1IBlockSource.func_82618_k().playAuxSFX(1000, par1IBlockSource.func_82623_d(), par1IBlockSource.func_82622_e(), par1IBlockSource.func_82621_f(), 0);
    }

    protected void func_82489_a(IBlockSource par1IBlockSource, EnumFacing par2EnumFacing)
    {
        par1IBlockSource.func_82618_k().playAuxSFX(2000, par1IBlockSource.func_82623_d(), par1IBlockSource.func_82622_e(), par1IBlockSource.func_82621_f(), this.func_82488_a(par2EnumFacing));
    }

    private int func_82488_a(EnumFacing par1EnumFacing)
    {
        return par1EnumFacing.func_82601_c() + 1 + (par1EnumFacing.func_82599_e() + 1) * 3;
    }
}
