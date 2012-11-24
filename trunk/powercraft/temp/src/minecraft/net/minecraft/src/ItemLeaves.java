package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Block;
import net.minecraft.src.BlockLeaves;
import net.minecraft.src.ColorizerFoliage;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class ItemLeaves extends ItemBlock {

   public ItemLeaves(int p_i3667_1_) {
      super(p_i3667_1_);
      this.func_77656_e(0);
      this.func_77627_a(true);
   }

   public int func_77647_b(int p_77647_1_) {
      return p_77647_1_ | 4;
   }

   @SideOnly(Side.CLIENT)
   public int func_77617_a(int p_77617_1_) {
      return Block.field_71952_K.func_71858_a(0, p_77617_1_);
   }

   @SideOnly(Side.CLIENT)
   public int func_82790_a(ItemStack p_82790_1_, int p_82790_2_) {
      int var3 = p_82790_1_.func_77960_j();
      return (var3 & 1) == 1?ColorizerFoliage.func_77466_a():((var3 & 2) == 2?ColorizerFoliage.func_77469_b():ColorizerFoliage.func_77468_c());
   }

   public String func_77667_c(ItemStack p_77667_1_) {
      int var2 = p_77667_1_.func_77960_j();
      if(var2 < 0 || var2 >= BlockLeaves.field_72136_a.length) {
         var2 = 0;
      }

      return super.func_77658_a() + "." + BlockLeaves.field_72136_a[var2];
   }
}
