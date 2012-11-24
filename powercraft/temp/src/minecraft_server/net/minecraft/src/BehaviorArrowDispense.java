package net.minecraft.src;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.BehaviorProjectileDispense;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.IPosition;
import net.minecraft.src.IProjectile;
import net.minecraft.src.World;

public class BehaviorArrowDispense extends BehaviorProjectileDispense {

   // $FF: synthetic field
   final MinecraftServer field_74267_a;


   public BehaviorArrowDispense(MinecraftServer p_i5002_1_) {
      this.field_74267_a = p_i5002_1_;
   }

   protected IProjectile func_82499_a(World p_82499_1_, IPosition p_82499_2_) {
      EntityArrow var3 = new EntityArrow(p_82499_1_, p_82499_2_.func_82615_a(), p_82499_2_.func_82617_b(), p_82499_2_.func_82616_c());
      var3.field_70251_a = 1;
      return var3;
   }
}
