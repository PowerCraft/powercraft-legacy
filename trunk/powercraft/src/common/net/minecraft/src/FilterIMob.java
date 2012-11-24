package net.minecraft.src;

final class FilterIMob implements IEntitySelector
{
    public boolean isEntityApplicable(Entity par1Entity)
    {
        return par1Entity instanceof IMob;
    }
}
