package net.minecraft.src;

import java.util.Random;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.BehaviorDefaultDispenseItem;
import net.minecraft.src.BlockDispenser;
import net.minecraft.src.EntitySmallFireball;
import net.minecraft.src.EnumFacing;
import net.minecraft.src.IBlockSource;
import net.minecraft.src.IPosition;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class BehaviorDispenseFireball extends BehaviorDefaultDispenseItem {

   // $FF: synthetic field
   final MinecraftServer field_82504_b;


   public BehaviorDispenseFireball(MinecraftServer p_i5047_1_) {
      this.field_82504_b = p_i5047_1_;
   }

   public ItemStack func_82487_b(IBlockSource p_82487_1_, ItemStack p_82487_2_) {
      EnumFacing var3 = EnumFacing.func_82600_a(p_82487_1_.func_82620_h());
      IPosition var4 = BlockDispenser.func_82525_a(p_82487_1_);
      double var5 = var4.func_82615_a() + (double)((float)var3.func_82601_c() * 0.3F);
      double var7 = var4.func_82617_b();
      double var9 = var4.func_82616_c() + (double)((float)var3.func_82599_e() * 0.3F);
      World var11 = p_82487_1_.func_82618_k();
      Random var12 = var11.field_73012_v;
      double var13 = var12.nextGaussian() * 0.05D + (double)var3.func_82601_c();
      double var15 = var12.nextGaussian() * 0.05D;
      double var17 = var12.nextGaussian() * 0.05D + (double)var3.func_82599_e();
      var11.func_72838_d(new EntitySmallFireball(var11, var5, var7, var9, var13, var15, var17));
      p_82487_2_.func_77979_a(1);
      return p_82487_2_;
   }

   protected void func_82485_a(IBlockSource p_82485_1_) {
      p_82485_1_.func_82618_k().func_72926_e(1009, p_82485_1_.func_82623_d(), p_82485_1_.func_82622_e(), p_82485_1_.func_82621_f(), 0);
   }
}
