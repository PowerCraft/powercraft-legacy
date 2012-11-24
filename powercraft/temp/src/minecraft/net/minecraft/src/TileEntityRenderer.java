package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.RenderEnchantmentTable;
import net.minecraft.src.RenderEndPortal;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityBeacon;
import net.minecraft.src.TileEntityBeaconRenderer;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.TileEntityChestRenderer;
import net.minecraft.src.TileEntityEnchantmentTable;
import net.minecraft.src.TileEntityEndPortal;
import net.minecraft.src.TileEntityEnderChest;
import net.minecraft.src.TileEntityEnderChestRenderer;
import net.minecraft.src.TileEntityMobSpawner;
import net.minecraft.src.TileEntityMobSpawnerRenderer;
import net.minecraft.src.TileEntityPiston;
import net.minecraft.src.TileEntityRendererPiston;
import net.minecraft.src.TileEntitySign;
import net.minecraft.src.TileEntitySignRenderer;
import net.minecraft.src.TileEntitySkull;
import net.minecraft.src.TileEntitySkullRenderer;
import net.minecraft.src.TileEntitySpecialRenderer;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityRenderer {

   public Map field_76966_m = new HashMap();
   public static TileEntityRenderer field_76963_a = new TileEntityRenderer();
   private FontRenderer field_76964_n;
   public static double field_76961_b;
   public static double field_76962_c;
   public static double field_76959_d;
   public RenderEngine field_76960_e;
   public World field_76957_f;
   public EntityLiving field_76958_g;
   public float field_76969_h;
   public float field_76970_i;
   public double field_76967_j;
   public double field_76968_k;
   public double field_76965_l;


   private TileEntityRenderer() {
      this.field_76966_m.put(TileEntitySign.class, new TileEntitySignRenderer());
      this.field_76966_m.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
      this.field_76966_m.put(TileEntityPiston.class, new TileEntityRendererPiston());
      this.field_76966_m.put(TileEntityChest.class, new TileEntityChestRenderer());
      this.field_76966_m.put(TileEntityEnderChest.class, new TileEntityEnderChestRenderer());
      this.field_76966_m.put(TileEntityEnchantmentTable.class, new RenderEnchantmentTable());
      this.field_76966_m.put(TileEntityEndPortal.class, new RenderEndPortal());
      this.field_76966_m.put(TileEntityBeacon.class, new TileEntityBeaconRenderer());
      this.field_76966_m.put(TileEntitySkull.class, new TileEntitySkullRenderer());
      Iterator var1 = this.field_76966_m.values().iterator();

      while(var1.hasNext()) {
         TileEntitySpecialRenderer var2 = (TileEntitySpecialRenderer)var1.next();
         var2.func_76893_a(this);
      }

   }

   public TileEntitySpecialRenderer func_76951_a(Class p_76951_1_) {
      TileEntitySpecialRenderer var2 = (TileEntitySpecialRenderer)this.field_76966_m.get(p_76951_1_);
      if(var2 == null && p_76951_1_ != TileEntity.class) {
         var2 = this.func_76951_a(p_76951_1_.getSuperclass());
         this.field_76966_m.put(p_76951_1_, var2);
      }

      return var2;
   }

   public boolean func_76952_a(TileEntity p_76952_1_) {
      return this.func_76956_b(p_76952_1_) != null;
   }

   public TileEntitySpecialRenderer func_76956_b(TileEntity p_76956_1_) {
      return p_76956_1_ == null?null:this.func_76951_a(p_76956_1_.getClass());
   }

   public void func_76953_a(World p_76953_1_, RenderEngine p_76953_2_, FontRenderer p_76953_3_, EntityLiving p_76953_4_, float p_76953_5_) {
      if(this.field_76957_f != p_76953_1_) {
         this.func_76955_a(p_76953_1_);
      }

      this.field_76960_e = p_76953_2_;
      this.field_76958_g = p_76953_4_;
      this.field_76964_n = p_76953_3_;
      this.field_76969_h = p_76953_4_.field_70126_B + (p_76953_4_.field_70177_z - p_76953_4_.field_70126_B) * p_76953_5_;
      this.field_76970_i = p_76953_4_.field_70127_C + (p_76953_4_.field_70125_A - p_76953_4_.field_70127_C) * p_76953_5_;
      this.field_76967_j = p_76953_4_.field_70142_S + (p_76953_4_.field_70165_t - p_76953_4_.field_70142_S) * (double)p_76953_5_;
      this.field_76968_k = p_76953_4_.field_70137_T + (p_76953_4_.field_70163_u - p_76953_4_.field_70137_T) * (double)p_76953_5_;
      this.field_76965_l = p_76953_4_.field_70136_U + (p_76953_4_.field_70161_v - p_76953_4_.field_70136_U) * (double)p_76953_5_;
   }

   public void func_76950_a(TileEntity p_76950_1_, float p_76950_2_) {
      if(p_76950_1_.func_70318_a(this.field_76967_j, this.field_76968_k, this.field_76965_l) < p_76950_1_.func_82115_m()) {
         int var3 = this.field_76957_f.func_72802_i(p_76950_1_.field_70329_l, p_76950_1_.field_70330_m, p_76950_1_.field_70327_n, 0);
         int var4 = var3 % 65536;
         int var5 = var3 / 65536;
         OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)var4 / 1.0F, (float)var5 / 1.0F);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.func_76949_a(p_76950_1_, (double)p_76950_1_.field_70329_l - field_76961_b, (double)p_76950_1_.field_70330_m - field_76962_c, (double)p_76950_1_.field_70327_n - field_76959_d, p_76950_2_);
      }

   }

   public void func_76949_a(TileEntity p_76949_1_, double p_76949_2_, double p_76949_4_, double p_76949_6_, float p_76949_8_) {
      TileEntitySpecialRenderer var9 = this.func_76956_b(p_76949_1_);
      if(var9 != null) {
         var9.func_76894_a(p_76949_1_, p_76949_2_, p_76949_4_, p_76949_6_, p_76949_8_);
      }

   }

   public void func_76955_a(World p_76955_1_) {
      this.field_76957_f = p_76955_1_;
      Iterator var2 = this.field_76966_m.values().iterator();

      while(var2.hasNext()) {
         TileEntitySpecialRenderer var3 = (TileEntitySpecialRenderer)var2.next();
         if(var3 != null) {
            var3.func_76896_a(p_76955_1_);
         }
      }

   }

   public FontRenderer func_76954_a() {
      return this.field_76964_n;
   }

}
