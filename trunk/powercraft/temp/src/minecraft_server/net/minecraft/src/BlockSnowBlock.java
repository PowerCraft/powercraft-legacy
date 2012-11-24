package net.minecraft.src;

import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.Item;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockSnowBlock extends Block {

   protected BlockSnowBlock(int p_i3995_1_, int p_i3995_2_) {
      super(p_i3995_1_, p_i3995_2_, Material.field_76258_w);
      this.func_71907_b(true);
      this.func_71849_a(CreativeTabs.field_78030_b);
   }

   public int func_71885_a(int p_71885_1_, Random p_71885_2_, int p_71885_3_) {
      return Item.field_77768_aD.field_77779_bT;
   }

   public int func_71925_a(Random p_71925_1_) {
      return 4;
   }

   public void func_71847_b(World p_71847_1_, int p_71847_2_, int p_71847_3_, int p_71847_4_, Random p_71847_5_) {
      if(p_71847_1_.func_72972_b(EnumSkyBlock.Block, p_71847_2_, p_71847_3_, p_71847_4_) > 11) {
         this.func_71897_c(p_71847_1_, p_71847_2_, p_71847_3_, p_71847_4_, p_71847_1_.func_72805_g(p_71847_2_, p_71847_3_, p_71847_4_), 0);
         p_71847_1_.func_72859_e(p_71847_2_, p_71847_3_, p_71847_4_, 0);
      }

   }
}
