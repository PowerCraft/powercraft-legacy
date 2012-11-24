package net.minecraft.src;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityDamageSource;
import net.minecraft.src.EntityDamageSourceIndirect;
import net.minecraft.src.EntityFireball;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.StatCollector;

public class DamageSource {

   public static DamageSource field_76372_a = (new DamageSource("inFire")).func_76361_j();
   public static DamageSource field_76370_b = (new DamageSource("onFire")).func_76348_h().func_76361_j();
   public static DamageSource field_76371_c = (new DamageSource("lava")).func_76361_j();
   public static DamageSource field_76368_d = (new DamageSource("inWall")).func_76348_h();
   public static DamageSource field_76369_e = (new DamageSource("drown")).func_76348_h();
   public static DamageSource field_76366_f = (new DamageSource("starve")).func_76348_h();
   public static DamageSource field_76367_g = new DamageSource("cactus");
   public static DamageSource field_76379_h = (new DamageSource("fall")).func_76348_h();
   public static DamageSource field_76380_i = (new DamageSource("outOfWorld")).func_76348_h().func_76359_i();
   public static DamageSource field_76377_j = (new DamageSource("generic")).func_76348_h();
   public static DamageSource field_76378_k = (new DamageSource("explosion")).func_76351_m();
   public static DamageSource field_76375_l = new DamageSource("explosion");
   public static DamageSource field_76376_m = (new DamageSource("magic")).func_76348_h().func_82726_p();
   public static DamageSource field_82727_n = (new DamageSource("wither")).func_76348_h();
   public static DamageSource field_82728_o = new DamageSource("anvil");
   public static DamageSource field_82729_p = new DamageSource("fallingBlock");
   private boolean field_76374_o = false;
   private boolean field_76385_p = false;
   private float field_76384_q = 0.3F;
   private boolean field_76383_r;
   private boolean field_76382_s;
   private boolean field_76381_t;
   private boolean field_82730_x = false;
   public String field_76373_n;


   public static DamageSource func_76358_a(EntityLiving p_76358_0_) {
      return new EntityDamageSource("mob", p_76358_0_);
   }

   public static DamageSource func_76365_a(EntityPlayer p_76365_0_) {
      return new EntityDamageSource("player", p_76365_0_);
   }

   public static DamageSource func_76353_a(EntityArrow p_76353_0_, Entity p_76353_1_) {
      return (new EntityDamageSourceIndirect("arrow", p_76353_0_, p_76353_1_)).func_76349_b();
   }

   public static DamageSource func_76362_a(EntityFireball p_76362_0_, Entity p_76362_1_) {
      return p_76362_1_ == null?(new EntityDamageSourceIndirect("onFire", p_76362_0_, p_76362_0_)).func_76361_j().func_76349_b():(new EntityDamageSourceIndirect("fireball", p_76362_0_, p_76362_1_)).func_76361_j().func_76349_b();
   }

   public static DamageSource func_76356_a(Entity p_76356_0_, Entity p_76356_1_) {
      return (new EntityDamageSourceIndirect("thrown", p_76356_0_, p_76356_1_)).func_76349_b();
   }

   public static DamageSource func_76354_b(Entity p_76354_0_, Entity p_76354_1_) {
      return (new EntityDamageSourceIndirect("indirectMagic", p_76354_0_, p_76354_1_)).func_76348_h().func_82726_p();
   }

   public boolean func_76352_a() {
      return this.field_76382_s;
   }

   public DamageSource func_76349_b() {
      this.field_76382_s = true;
      return this;
   }

   public boolean func_76363_c() {
      return this.field_76374_o;
   }

   public float func_76345_d() {
      return this.field_76384_q;
   }

   public boolean func_76357_e() {
      return this.field_76385_p;
   }

   protected DamageSource(String p_i3429_1_) {
      this.field_76373_n = p_i3429_1_;
   }

   public Entity func_76364_f() {
      return this.func_76346_g();
   }

   public Entity func_76346_g() {
      return null;
   }

   protected DamageSource func_76348_h() {
      this.field_76374_o = true;
      this.field_76384_q = 0.0F;
      return this;
   }

   protected DamageSource func_76359_i() {
      this.field_76385_p = true;
      return this;
   }

   protected DamageSource func_76361_j() {
      this.field_76383_r = true;
      return this;
   }

   public String func_76360_b(EntityPlayer p_76360_1_) {
      return StatCollector.func_74837_a("death." + this.field_76373_n, new Object[]{p_76360_1_.field_71092_bJ});
   }

   public boolean func_76347_k() {
      return this.field_76383_r;
   }

   public String func_76355_l() {
      return this.field_76373_n;
   }

   public DamageSource func_76351_m() {
      this.field_76381_t = true;
      return this;
   }

   public boolean func_76350_n() {
      return this.field_76381_t;
   }

   public boolean func_82725_o() {
      return this.field_82730_x;
   }

   public DamageSource func_82726_p() {
      this.field_82730_x = true;
      return this;
   }

}
