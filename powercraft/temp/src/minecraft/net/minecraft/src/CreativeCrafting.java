package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Container;
import net.minecraft.src.ICrafting;
import net.minecraft.src.ItemStack;

@SideOnly(Side.CLIENT)
public class CreativeCrafting implements ICrafting {

   private final Minecraft field_82250_a;


   public CreativeCrafting(Minecraft p_i5008_1_) {
      this.field_82250_a = p_i5008_1_;
   }

   public void func_71110_a(Container p_71110_1_, List p_71110_2_) {}

   public void func_71111_a(Container p_71111_1_, int p_71111_2_, ItemStack p_71111_3_) {
      this.field_82250_a.field_71442_b.func_78761_a(p_71111_3_, p_71111_2_);
   }

   public void func_71112_a(Container p_71112_1_, int p_71112_2_, int p_71112_3_) {}
}
