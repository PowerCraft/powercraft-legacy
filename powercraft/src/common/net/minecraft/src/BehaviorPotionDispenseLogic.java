package net.minecraft.src;

class BehaviorPotionDispenseLogic extends BehaviorProjectileDispense
{
    final ItemStack field_82501_b;

    final BehaviorPotionDispense field_82502_c;

    BehaviorPotionDispenseLogic(BehaviorPotionDispense par1, ItemStack par2)
    {
        this.field_82502_c = par1;
        this.field_82501_b = par2;
    }

    protected IProjectile func_82499_a(World par1World, IPosition par2IPosition)
    {
        return new EntityPotion(par1World, par2IPosition.func_82615_a(), par2IPosition.func_82617_b(), par2IPosition.func_82616_c(), this.field_82501_b.copy());
    }

    protected float func_82498_a()
    {
        return super.func_82498_a() * 0.5F;
    }

    protected float func_82500_b()
    {
        return super.func_82500_b() * 1.25F;
    }
}
