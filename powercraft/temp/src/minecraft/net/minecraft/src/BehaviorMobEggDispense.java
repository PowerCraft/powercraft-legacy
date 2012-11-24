package net.minecraft.src;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.BehaviorDefaultDispenseItem;
import net.minecraft.src.EnumFacing;
import net.minecraft.src.IBlockSource;
import net.minecraft.src.ItemMonsterPlacer;
import net.minecraft.src.ItemStack;

public class BehaviorMobEggDispense extends BehaviorDefaultDispenseItem {

   // $FF: synthetic field
   final MinecraftServer field_74270_a;


   public BehaviorMobEggDispense(MinecraftServer p_i5009_1_) {
      this.field_74270_a = p_i5009_1_;
   }

   public ItemStack func_82487_b(IBlockSource p_82487_1_, ItemStack p_82487_2_) {
      EnumFacing var3 = EnumFacing.func_82600_a(p_82487_1_.func_82620_h());
      double var4 = p_82487_1_.func_82615_a() + (double)var3.func_82601_c();
      double var6 = (double)((float)p_82487_1_.func_82622_e() + 0.2F);
      double var8 = p_82487_1_.func_82616_c() + (double)var3.func_82599_e();
      ItemMonsterPlacer.func_77840_a(p_82487_1_.func_82618_k(), p_82487_2_.func_77960_j(), var4, var6, var8);
      p_82487_2_.func_77979_a(1);
      return p_82487_2_;
   }

   protected void func_82485_a(IBlockSource p_82485_1_) {
      p_82485_1_.func_82618_k().func_72926_e(1002, p_82485_1_.func_82623_d(), p_82485_1_.func_82622_e(), p_82485_1_.func_82621_f(), 0);
   }
}
