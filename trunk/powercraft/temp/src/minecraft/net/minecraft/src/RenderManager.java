package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.src.Block;
import net.minecraft.src.CrashReport;
import net.minecraft.src.CrashReportCategory;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityBat;
import net.minecraft.src.EntityBlaze;
import net.minecraft.src.EntityBoat;
import net.minecraft.src.EntityCaveSpider;
import net.minecraft.src.EntityChicken;
import net.minecraft.src.EntityCow;
import net.minecraft.src.EntityCreeper;
import net.minecraft.src.EntityDragon;
import net.minecraft.src.EntityEgg;
import net.minecraft.src.EntityEnderCrystal;
import net.minecraft.src.EntityEnderEye;
import net.minecraft.src.EntityEnderPearl;
import net.minecraft.src.EntityEnderman;
import net.minecraft.src.EntityExpBottle;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityFishHook;
import net.minecraft.src.EntityGhast;
import net.minecraft.src.EntityGiantZombie;
import net.minecraft.src.EntityIronGolem;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityItemFrame;
import net.minecraft.src.EntityLargeFireball;
import net.minecraft.src.EntityLightningBolt;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMagmaCube;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EntityMooshroom;
import net.minecraft.src.EntityOcelot;
import net.minecraft.src.EntityPainting;
import net.minecraft.src.EntityPig;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPotion;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.EntitySilverfish;
import net.minecraft.src.EntitySkeleton;
import net.minecraft.src.EntitySlime;
import net.minecraft.src.EntitySmallFireball;
import net.minecraft.src.EntitySnowball;
import net.minecraft.src.EntitySnowman;
import net.minecraft.src.EntitySpider;
import net.minecraft.src.EntitySquid;
import net.minecraft.src.EntityTNTPrimed;
import net.minecraft.src.EntityVillager;
import net.minecraft.src.EntityWitch;
import net.minecraft.src.EntityWither;
import net.minecraft.src.EntityWitherSkull;
import net.minecraft.src.EntityWolf;
import net.minecraft.src.EntityXPOrb;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GameSettings;
import net.minecraft.src.Item;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.ModelChicken;
import net.minecraft.src.ModelCow;
import net.minecraft.src.ModelOcelot;
import net.minecraft.src.ModelPig;
import net.minecraft.src.ModelSheep1;
import net.minecraft.src.ModelSheep2;
import net.minecraft.src.ModelSlime;
import net.minecraft.src.ModelSquid;
import net.minecraft.src.ModelWolf;
import net.minecraft.src.ModelZombie;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.Render;
import net.minecraft.src.RenderArrow;
import net.minecraft.src.RenderBat;
import net.minecraft.src.RenderBlaze;
import net.minecraft.src.RenderBoat;
import net.minecraft.src.RenderChicken;
import net.minecraft.src.RenderCow;
import net.minecraft.src.RenderCreeper;
import net.minecraft.src.RenderDragon;
import net.minecraft.src.RenderEnderCrystal;
import net.minecraft.src.RenderEnderman;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderEntity;
import net.minecraft.src.RenderFallingSand;
import net.minecraft.src.RenderFireball;
import net.minecraft.src.RenderFish;
import net.minecraft.src.RenderGhast;
import net.minecraft.src.RenderGiantZombie;
import net.minecraft.src.RenderIronGolem;
import net.minecraft.src.RenderItem;
import net.minecraft.src.RenderItemFrame;
import net.minecraft.src.RenderLightningBolt;
import net.minecraft.src.RenderLiving;
import net.minecraft.src.RenderMagmaCube;
import net.minecraft.src.RenderMinecart;
import net.minecraft.src.RenderMooshroom;
import net.minecraft.src.RenderOcelot;
import net.minecraft.src.RenderPainting;
import net.minecraft.src.RenderPig;
import net.minecraft.src.RenderPlayer;
import net.minecraft.src.RenderSheep;
import net.minecraft.src.RenderSilverfish;
import net.minecraft.src.RenderSkeleton;
import net.minecraft.src.RenderSlime;
import net.minecraft.src.RenderSnowMan;
import net.minecraft.src.RenderSnowball;
import net.minecraft.src.RenderSpider;
import net.minecraft.src.RenderSquid;
import net.minecraft.src.RenderTNTPrimed;
import net.minecraft.src.RenderVillager;
import net.minecraft.src.RenderWitch;
import net.minecraft.src.RenderWither;
import net.minecraft.src.RenderWitherSkull;
import net.minecraft.src.RenderWolf;
import net.minecraft.src.RenderXPOrb;
import net.minecraft.src.RenderZombie;
import net.minecraft.src.ReportedException;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderManager {

   public Map field_78729_o = new HashMap();
   public static RenderManager field_78727_a = new RenderManager();
   private FontRenderer field_78736_p;
   public static double field_78725_b;
   public static double field_78726_c;
   public static double field_78723_d;
   public RenderEngine field_78724_e;
   public ItemRenderer field_78721_f;
   public World field_78722_g;
   public EntityLiving field_78734_h;
   public float field_78735_i;
   public float field_78732_j;
   public GameSettings field_78733_k;
   public double field_78730_l;
   public double field_78731_m;
   public double field_78728_n;
   public static boolean field_85095_o = false;


   private RenderManager() {
      this.field_78729_o.put(EntitySpider.class, new RenderSpider());
      this.field_78729_o.put(EntityCaveSpider.class, new RenderSpider());
      this.field_78729_o.put(EntityPig.class, new RenderPig(new ModelPig(), new ModelPig(0.5F), 0.7F));
      this.field_78729_o.put(EntitySheep.class, new RenderSheep(new ModelSheep2(), new ModelSheep1(), 0.7F));
      this.field_78729_o.put(EntityCow.class, new RenderCow(new ModelCow(), 0.7F));
      this.field_78729_o.put(EntityMooshroom.class, new RenderMooshroom(new ModelCow(), 0.7F));
      this.field_78729_o.put(EntityWolf.class, new RenderWolf(new ModelWolf(), new ModelWolf(), 0.5F));
      this.field_78729_o.put(EntityChicken.class, new RenderChicken(new ModelChicken(), 0.3F));
      this.field_78729_o.put(EntityOcelot.class, new RenderOcelot(new ModelOcelot(), 0.4F));
      this.field_78729_o.put(EntitySilverfish.class, new RenderSilverfish());
      this.field_78729_o.put(EntityCreeper.class, new RenderCreeper());
      this.field_78729_o.put(EntityEnderman.class, new RenderEnderman());
      this.field_78729_o.put(EntitySnowman.class, new RenderSnowMan());
      this.field_78729_o.put(EntitySkeleton.class, new RenderSkeleton());
      this.field_78729_o.put(EntityWitch.class, new RenderWitch());
      this.field_78729_o.put(EntityBlaze.class, new RenderBlaze());
      this.field_78729_o.put(EntityZombie.class, new RenderZombie());
      this.field_78729_o.put(EntitySlime.class, new RenderSlime(new ModelSlime(16), new ModelSlime(0), 0.25F));
      this.field_78729_o.put(EntityMagmaCube.class, new RenderMagmaCube());
      this.field_78729_o.put(EntityPlayer.class, new RenderPlayer());
      this.field_78729_o.put(EntityGiantZombie.class, new RenderGiantZombie(new ModelZombie(), 0.5F, 6.0F));
      this.field_78729_o.put(EntityGhast.class, new RenderGhast());
      this.field_78729_o.put(EntitySquid.class, new RenderSquid(new ModelSquid(), 0.7F));
      this.field_78729_o.put(EntityVillager.class, new RenderVillager());
      this.field_78729_o.put(EntityIronGolem.class, new RenderIronGolem());
      this.field_78729_o.put(EntityLiving.class, new RenderLiving(new ModelBiped(), 0.5F));
      this.field_78729_o.put(EntityBat.class, new RenderBat());
      this.field_78729_o.put(EntityDragon.class, new RenderDragon());
      this.field_78729_o.put(EntityEnderCrystal.class, new RenderEnderCrystal());
      this.field_78729_o.put(EntityWither.class, new RenderWither());
      this.field_78729_o.put(Entity.class, new RenderEntity());
      this.field_78729_o.put(EntityPainting.class, new RenderPainting());
      this.field_78729_o.put(EntityItemFrame.class, new RenderItemFrame());
      this.field_78729_o.put(EntityArrow.class, new RenderArrow());
      this.field_78729_o.put(EntitySnowball.class, new RenderSnowball(Item.field_77768_aD.func_77617_a(0)));
      this.field_78729_o.put(EntityEnderPearl.class, new RenderSnowball(Item.field_77730_bn.func_77617_a(0)));
      this.field_78729_o.put(EntityEnderEye.class, new RenderSnowball(Item.field_77748_bA.func_77617_a(0)));
      this.field_78729_o.put(EntityEgg.class, new RenderSnowball(Item.field_77764_aP.func_77617_a(0)));
      this.field_78729_o.put(EntityPotion.class, new RenderSnowball(154));
      this.field_78729_o.put(EntityExpBottle.class, new RenderSnowball(Item.field_77809_bD.func_77617_a(0)));
      this.field_78729_o.put(EntityLargeFireball.class, new RenderFireball(2.0F));
      this.field_78729_o.put(EntitySmallFireball.class, new RenderFireball(0.5F));
      this.field_78729_o.put(EntityWitherSkull.class, new RenderWitherSkull());
      this.field_78729_o.put(EntityItem.class, new RenderItem());
      this.field_78729_o.put(EntityXPOrb.class, new RenderXPOrb());
      this.field_78729_o.put(EntityTNTPrimed.class, new RenderTNTPrimed());
      this.field_78729_o.put(EntityFallingSand.class, new RenderFallingSand());
      this.field_78729_o.put(EntityMinecart.class, new RenderMinecart());
      this.field_78729_o.put(EntityBoat.class, new RenderBoat());
      this.field_78729_o.put(EntityFishHook.class, new RenderFish());
      this.field_78729_o.put(EntityLightningBolt.class, new RenderLightningBolt());
      Iterator var1 = this.field_78729_o.values().iterator();

      while(var1.hasNext()) {
         Render var2 = (Render)var1.next();
         var2.func_76976_a(this);
      }

   }

   public Render func_78715_a(Class p_78715_1_) {
      Render var2 = (Render)this.field_78729_o.get(p_78715_1_);
      if(var2 == null && p_78715_1_ != Entity.class) {
         var2 = this.func_78715_a(p_78715_1_.getSuperclass());
         this.field_78729_o.put(p_78715_1_, var2);
      }

      return var2;
   }

   public Render func_78713_a(Entity p_78713_1_) {
      return this.func_78715_a(p_78713_1_.getClass());
   }

   public void func_78718_a(World p_78718_1_, RenderEngine p_78718_2_, FontRenderer p_78718_3_, EntityLiving p_78718_4_, GameSettings p_78718_5_, float p_78718_6_) {
      this.field_78722_g = p_78718_1_;
      this.field_78724_e = p_78718_2_;
      this.field_78733_k = p_78718_5_;
      this.field_78734_h = p_78718_4_;
      this.field_78736_p = p_78718_3_;
      if(p_78718_4_.func_70608_bn()) {
         int var7 = p_78718_1_.func_72798_a(MathHelper.func_76128_c(p_78718_4_.field_70165_t), MathHelper.func_76128_c(p_78718_4_.field_70163_u), MathHelper.func_76128_c(p_78718_4_.field_70161_v));
         if(var7 == Block.field_71959_S.field_71990_ca) {
            int var8 = p_78718_1_.func_72805_g(MathHelper.func_76128_c(p_78718_4_.field_70165_t), MathHelper.func_76128_c(p_78718_4_.field_70163_u), MathHelper.func_76128_c(p_78718_4_.field_70161_v));
            int var9 = var8 & 3;
            this.field_78735_i = (float)(var9 * 90 + 180);
            this.field_78732_j = 0.0F;
         }
      } else {
         this.field_78735_i = p_78718_4_.field_70126_B + (p_78718_4_.field_70177_z - p_78718_4_.field_70126_B) * p_78718_6_;
         this.field_78732_j = p_78718_4_.field_70127_C + (p_78718_4_.field_70125_A - p_78718_4_.field_70127_C) * p_78718_6_;
      }

      if(p_78718_5_.field_74320_O == 2) {
         this.field_78735_i += 180.0F;
      }

      this.field_78730_l = p_78718_4_.field_70142_S + (p_78718_4_.field_70165_t - p_78718_4_.field_70142_S) * (double)p_78718_6_;
      this.field_78731_m = p_78718_4_.field_70137_T + (p_78718_4_.field_70163_u - p_78718_4_.field_70137_T) * (double)p_78718_6_;
      this.field_78728_n = p_78718_4_.field_70136_U + (p_78718_4_.field_70161_v - p_78718_4_.field_70136_U) * (double)p_78718_6_;
   }

   public void func_78720_a(Entity p_78720_1_, float p_78720_2_) {
      double var3 = p_78720_1_.field_70142_S + (p_78720_1_.field_70165_t - p_78720_1_.field_70142_S) * (double)p_78720_2_;
      double var5 = p_78720_1_.field_70137_T + (p_78720_1_.field_70163_u - p_78720_1_.field_70137_T) * (double)p_78720_2_;
      double var7 = p_78720_1_.field_70136_U + (p_78720_1_.field_70161_v - p_78720_1_.field_70136_U) * (double)p_78720_2_;
      float var9 = p_78720_1_.field_70126_B + (p_78720_1_.field_70177_z - p_78720_1_.field_70126_B) * p_78720_2_;
      int var10 = p_78720_1_.func_70070_b(p_78720_2_);
      if(p_78720_1_.func_70027_ad()) {
         var10 = 15728880;
      }

      int var11 = var10 % 65536;
      int var12 = var10 / 65536;
      OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)var11 / 1.0F, (float)var12 / 1.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.func_78719_a(p_78720_1_, var3 - field_78725_b, var5 - field_78726_c, var7 - field_78723_d, var9, p_78720_2_);
   }

   public void func_78719_a(Entity p_78719_1_, double p_78719_2_, double p_78719_4_, double p_78719_6_, float p_78719_8_, float p_78719_9_) {
      Render var10 = null;

      try {
         var10 = this.func_78713_a(p_78719_1_);
         if(var10 != null && this.field_78724_e != null) {
            if(field_85095_o) {
               try {
                  this.func_85094_b(p_78719_1_, p_78719_2_, p_78719_4_, p_78719_6_, p_78719_8_, p_78719_9_);
               } catch (Throwable var17) {
                  throw new ReportedException(CrashReport.func_85055_a(var17, "Rendering entity hitbox in world"));
               }
            }

            try {
               var10.func_76986_a(p_78719_1_, p_78719_2_, p_78719_4_, p_78719_6_, p_78719_8_, p_78719_9_);
            } catch (Throwable var16) {
               throw new ReportedException(CrashReport.func_85055_a(var16, "Rendering entity in world"));
            }

            try {
               var10.func_76979_b(p_78719_1_, p_78719_2_, p_78719_4_, p_78719_6_, p_78719_8_, p_78719_9_);
            } catch (Throwable var15) {
               throw new ReportedException(CrashReport.func_85055_a(var15, "Post-rendering entity in world"));
            }
         }

      } catch (Throwable var18) {
         CrashReport var12 = CrashReport.func_85055_a(var18, "Rendering entity in world");
         CrashReportCategory var13 = var12.func_85058_a("Entity being rendered");
         p_78719_1_.func_85029_a(var13);
         CrashReportCategory var14 = var12.func_85058_a("Renderer details");
         var14.func_71507_a("Assigned renderer", var10);
         var14.func_71507_a("Location", CrashReportCategory.func_85074_a(p_78719_2_, p_78719_4_, p_78719_6_));
         var14.func_71507_a("Rotation", Float.valueOf(p_78719_8_));
         var14.func_71507_a("Delta", Float.valueOf(p_78719_9_));
         throw new ReportedException(var12);
      }
   }

   private void func_85094_b(Entity p_85094_1_, double p_85094_2_, double p_85094_4_, double p_85094_6_, float p_85094_8_, float p_85094_9_) {
      GL11.glDepthMask(false);
      GL11.glDisable(3553);
      GL11.glDisable(2896);
      GL11.glDisable(2884);
      GL11.glDisable(3042);
      GL11.glPushMatrix();
      Tessellator var10 = Tessellator.field_78398_a;
      var10.func_78382_b();
      var10.func_78370_a(255, 255, 255, 32);
      double var11 = (double)(-p_85094_1_.field_70130_N / 2.0F);
      double var13 = (double)(-p_85094_1_.field_70130_N / 2.0F);
      double var15 = (double)(p_85094_1_.field_70130_N / 2.0F);
      double var17 = (double)(-p_85094_1_.field_70130_N / 2.0F);
      double var19 = (double)(-p_85094_1_.field_70130_N / 2.0F);
      double var21 = (double)(p_85094_1_.field_70130_N / 2.0F);
      double var23 = (double)(p_85094_1_.field_70130_N / 2.0F);
      double var25 = (double)(p_85094_1_.field_70130_N / 2.0F);
      double var27 = (double)p_85094_1_.field_70131_O;
      var10.func_78377_a(p_85094_2_ + var11, p_85094_4_ + var27, p_85094_6_ + var13);
      var10.func_78377_a(p_85094_2_ + var11, p_85094_4_, p_85094_6_ + var13);
      var10.func_78377_a(p_85094_2_ + var15, p_85094_4_, p_85094_6_ + var17);
      var10.func_78377_a(p_85094_2_ + var15, p_85094_4_ + var27, p_85094_6_ + var17);
      var10.func_78377_a(p_85094_2_ + var23, p_85094_4_ + var27, p_85094_6_ + var25);
      var10.func_78377_a(p_85094_2_ + var23, p_85094_4_, p_85094_6_ + var25);
      var10.func_78377_a(p_85094_2_ + var19, p_85094_4_, p_85094_6_ + var21);
      var10.func_78377_a(p_85094_2_ + var19, p_85094_4_ + var27, p_85094_6_ + var21);
      var10.func_78377_a(p_85094_2_ + var15, p_85094_4_ + var27, p_85094_6_ + var17);
      var10.func_78377_a(p_85094_2_ + var15, p_85094_4_, p_85094_6_ + var17);
      var10.func_78377_a(p_85094_2_ + var23, p_85094_4_, p_85094_6_ + var25);
      var10.func_78377_a(p_85094_2_ + var23, p_85094_4_ + var27, p_85094_6_ + var25);
      var10.func_78377_a(p_85094_2_ + var19, p_85094_4_ + var27, p_85094_6_ + var21);
      var10.func_78377_a(p_85094_2_ + var19, p_85094_4_, p_85094_6_ + var21);
      var10.func_78377_a(p_85094_2_ + var11, p_85094_4_, p_85094_6_ + var13);
      var10.func_78377_a(p_85094_2_ + var11, p_85094_4_ + var27, p_85094_6_ + var13);
      var10.func_78381_a();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glEnable(2896);
      GL11.glEnable(2884);
      GL11.glDisable(3042);
      GL11.glDepthMask(true);
   }

   public void func_78717_a(World p_78717_1_) {
      this.field_78722_g = p_78717_1_;
   }

   public double func_78714_a(double p_78714_1_, double p_78714_3_, double p_78714_5_) {
      double var7 = p_78714_1_ - this.field_78730_l;
      double var9 = p_78714_3_ - this.field_78731_m;
      double var11 = p_78714_5_ - this.field_78728_n;
      return var7 * var7 + var9 * var9 + var11 * var11;
   }

   public FontRenderer func_78716_a() {
      return this.field_78736_p;
   }

}
