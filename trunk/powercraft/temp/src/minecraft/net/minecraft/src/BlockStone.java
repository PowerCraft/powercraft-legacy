package net.minecraft.src;

import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;

public class BlockStone extends Block {

   public BlockStone(int p_i4001_1_, int p_i4001_2_) {
      super(p_i4001_1_, p_i4001_2_, Material.field_76246_e);
      this.func_71849_a(CreativeTabs.field_78030_b);
   }

   public int func_71885_a(int p_71885_1_, Random p_71885_2_, int p_71885_3_) {
      return Block.field_71978_w.field_71990_ca;
   }
}
