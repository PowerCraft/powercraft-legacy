package net.minecraft.src;

import net.minecraft.src.Entity;
import net.minecraft.src.IEntitySelector;
import net.minecraft.src.IMob;

final class FilterIMob implements IEntitySelector {

   public boolean func_82704_a(Entity p_82704_1_) {
      return p_82704_1_ instanceof IMob;
   }
}
