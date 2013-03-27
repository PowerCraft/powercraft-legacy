package net.minecraft.src;

public interface IEntitySelector
{
    IEntitySelector field_94557_a = new EntitySelectorAlive();
    IEntitySelector field_96566_b = new EntitySelectorInventory();

    /**
     * Return whether the specified entity is applicable to this filter.
     */
    boolean isEntityApplicable(Entity var1);
}
