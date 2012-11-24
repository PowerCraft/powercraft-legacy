package net.minecraft.src;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.BehaviorDefaultDispenseItem;
import net.minecraft.src.EnumFacing;
import net.minecraft.src.IBlockSource;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBucket;
import net.minecraft.src.ItemStack;

public class BehaviorBucketFullDispense extends BehaviorDefaultDispenseItem {

   private final BehaviorDefaultDispenseItem field_82495_c;
   // $FF: synthetic field
   final MinecraftServer field_82494_b;


   public BehaviorBucketFullDispense(MinecraftServer p_i5003_1_) {
      this.field_82494_b = p_i5003_1_;
      this.field_82495_c = new BehaviorDefaultDispenseItem();
   }

   public ItemStack func_82487_b(IBlockSource p_82487_1_, ItemStack p_82487_2_) {
      ItemBucket var3 = (ItemBucket)p_82487_2_.func_77973_b();
      int var4 = p_82487_1_.func_82623_d();
      int var5 = p_82487_1_.func_82622_e();
      int var6 = p_82487_1_.func_82621_f();
      EnumFacing var7 = EnumFacing.func_82600_a(p_82487_1_.func_82620_h());
      if(var3.func_77875_a(p_82487_1_.func_82618_k(), (double)var4, (double)var5, (double)var6, var4 + var7.func_82601_c(), var5, var6 + var7.func_82599_e())) {
         p_82487_2_.field_77993_c = Item.field_77788_aw.field_77779_bT;
         p_82487_2_.field_77994_a = 1;
         return p_82487_2_;
      } else {
         return this.field_82495_c.func_82482_a(p_82487_1_, p_82487_2_);
      }
   }
}
