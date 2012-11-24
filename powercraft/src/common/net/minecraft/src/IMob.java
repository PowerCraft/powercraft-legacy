package net.minecraft.src;

public interface IMob extends IAnimals
{
    IEntitySelector mobSelector = new FilterIMob();
}
