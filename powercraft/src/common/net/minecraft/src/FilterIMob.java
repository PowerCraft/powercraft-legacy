package net.minecraft.src;

final class FilterIMob implements IEntitySelector
{
    public boolean func_82704_a(Entity par1Entity)
    {
        return par1Entity instanceof IMob;
    }
}
