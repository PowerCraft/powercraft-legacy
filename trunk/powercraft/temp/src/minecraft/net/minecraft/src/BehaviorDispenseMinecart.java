package net.minecraft.src;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.BehaviorDefaultDispenseItem;
import net.minecraft.src.BlockRail;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EnumFacing;
import net.minecraft.src.IBlockSource;
import net.minecraft.src.ItemMinecart;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class BehaviorDispenseMinecart extends BehaviorDefaultDispenseItem {

   private final BehaviorDefaultDispenseItem field_82491_c;
   // $FF: synthetic field
   final MinecraftServer field_82490_b;


   public BehaviorDispenseMinecart(MinecraftServer p_i5048_1_) {
      this.field_82490_b = p_i5048_1_;
      this.field_82491_c = new BehaviorDefaultDispenseItem();
   }

   public ItemStack func_82487_b(IBlockSource p_82487_1_, ItemStack p_82487_2_) {
      EnumFacing var3 = EnumFacing.func_82600_a(p_82487_1_.func_82620_h());
      World var4 = p_82487_1_.func_82618_k();
      double var5 = p_82487_1_.func_82615_a() + (double)((float)var3.func_82601_c() * 1.125F);
      double var7 = p_82487_1_.func_82617_b();
      double var9 = p_82487_1_.func_82616_c() + (double)((float)var3.func_82599_e() * 1.125F);
      int var11 = p_82487_1_.func_82623_d() + var3.func_82601_c();
      int var12 = p_82487_1_.func_82622_e();
      int var13 = p_82487_1_.func_82621_f() + var3.func_82599_e();
      int var14 = var4.func_72798_a(var11, var12, var13);
      double var15;
      if(BlockRail.func_72184_d(var14)) {
         var15 = 0.0D;
      } else {
         if(var14 != 0 || !BlockRail.func_72184_d(var4.func_72798_a(var11, var12 - 1, var13))) {
            return this.field_82491_c.func_82482_a(p_82487_1_, p_82487_2_);
         }

         var15 = -1.0D;
      }

      EntityMinecart var17 = new EntityMinecart(var4, var5, var7 + var15, var9, ((ItemMinecart)p_82487_2_.func_77973_b()).field_77841_a);
      var4.func_72838_d(var17);
      p_82487_2_.func_77979_a(1);
      return p_82487_2_;
   }

   protected void func_82485_a(IBlockSource p_82485_1_) {
      p_82485_1_.func_82618_k().func_72926_e(1000, p_82485_1_.func_82623_d(), p_82485_1_.func_82622_e(), p_82485_1_.func_82621_f(), 0);
   }
}
