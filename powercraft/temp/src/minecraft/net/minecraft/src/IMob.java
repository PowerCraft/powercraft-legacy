package net.minecraft.src;

import net.minecraft.src.FilterIMob;
import net.minecraft.src.IAnimals;
import net.minecraft.src.IEntitySelector;

public interface IMob extends IAnimals {

   IEntitySelector field_82192_a = new FilterIMob();


}
