package net.minecraft.src;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;

public class BlockOreStorage extends Block {

   public BlockOreStorage(int p_i3969_1_, int p_i3969_2_) {
      super(p_i3969_1_, Material.field_76243_f);
      this.field_72059_bZ = p_i3969_2_;
      this.func_71849_a(CreativeTabs.field_78030_b);
   }

   public int func_71851_a(int p_71851_1_) {
      return this.field_72059_bZ;
   }
}
