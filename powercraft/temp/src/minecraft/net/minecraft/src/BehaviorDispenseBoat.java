package net.minecraft.src;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.BehaviorDefaultDispenseItem;
import net.minecraft.src.EntityBoat;
import net.minecraft.src.EnumFacing;
import net.minecraft.src.IBlockSource;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BehaviorDispenseBoat extends BehaviorDefaultDispenseItem {

   private final BehaviorDefaultDispenseItem field_82493_c;
   // $FF: synthetic field
   final MinecraftServer field_82492_b;


   public BehaviorDispenseBoat(MinecraftServer p_i5049_1_) {
      this.field_82492_b = p_i5049_1_;
      this.field_82493_c = new BehaviorDefaultDispenseItem();
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
      Material var14 = var4.func_72803_f(var11, var12, var13);
      double var15;
      if(Material.field_76244_g.equals(var14)) {
         var15 = 1.0D;
      } else {
         if(!Material.field_76249_a.equals(var14) || !Material.field_76244_g.equals(var4.func_72803_f(var11, var12 - 1, var13))) {
            return this.field_82493_c.func_82482_a(p_82487_1_, p_82487_2_);
         }

         var15 = 0.0D;
      }

      EntityBoat var17 = new EntityBoat(var4, var5, var7 + var15, var9);
      var4.func_72838_d(var17);
      p_82487_2_.func_77979_a(1);
      return p_82487_2_;
   }

   protected void func_82485_a(IBlockSource p_82485_1_) {
      p_82485_1_.func_82618_k().func_72926_e(1000, p_82485_1_.func_82623_d(), p_82485_1_.func_82622_e(), p_82485_1_.func_82621_f(), 0);
   }
}
