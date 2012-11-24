package net.minecraft.src;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.BehaviorProjectileDispense;
import net.minecraft.src.EntitySnowball;
import net.minecraft.src.IPosition;
import net.minecraft.src.IProjectile;
import net.minecraft.src.World;

public class BehaviorSnowballDispense extends BehaviorProjectileDispense {

   // $FF: synthetic field
   final MinecraftServer field_73716_a;


   public BehaviorSnowballDispense(MinecraftServer p_i5005_1_) {
      this.field_73716_a = p_i5005_1_;
   }

   protected IProjectile func_82499_a(World p_82499_1_, IPosition p_82499_2_) {
      return new EntitySnowball(p_82499_1_, p_82499_2_.func_82615_a(), p_82499_2_.func_82617_b(), p_82499_2_.func_82616_c());
   }
}
