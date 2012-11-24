package net.minecraft.src;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.BehaviorProjectileDispense;
import net.minecraft.src.EntityExpBottle;
import net.minecraft.src.IPosition;
import net.minecraft.src.IProjectile;
import net.minecraft.src.World;

public class BehaviorExpBottleDispense extends BehaviorProjectileDispense {

   // $FF: synthetic field
   final MinecraftServer field_74274_a;


   public BehaviorExpBottleDispense(MinecraftServer p_i5006_1_) {
      this.field_74274_a = p_i5006_1_;
   }

   protected IProjectile func_82499_a(World p_82499_1_, IPosition p_82499_2_) {
      return new EntityExpBottle(p_82499_1_, p_82499_2_.func_82615_a(), p_82499_2_.func_82617_b(), p_82499_2_.func_82616_c());
   }

   protected float func_82498_a() {
      return super.func_82498_a() * 0.5F;
   }

   protected float func_82500_b() {
      return super.func_82500_b() * 1.25F;
   }
}
