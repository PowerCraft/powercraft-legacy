package net.minecraft.src;

final class EntityWitherAttackFilter implements IEntitySelector
{
    public boolean func_82704_a(Entity par1Entity)
    {
        return par1Entity instanceof EntityLiving && ((EntityLiving)par1Entity).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD;
    }
}
