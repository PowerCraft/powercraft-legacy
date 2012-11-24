package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Iterator;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet132TileEntityData;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class TileEntityMobSpawner extends TileEntity {

   public int field_70394_a = -1;
   private String field_70390_d = "Pig";
   private NBTTagCompound field_70391_e = null;
   public double field_70392_b;
   public double field_70393_c = 0.0D;
   private int field_70388_f = 200;
   private int field_70389_g = 800;
   private int field_70395_h = 4;
   @SideOnly(Side.CLIENT)
   private Entity field_70396_i;
   private int field_82350_j = 6;
   private int field_82349_r = 16;
   private int field_82348_s = 4;


   public TileEntityMobSpawner() {
      this.field_70394_a = 20;
   }

   @SideOnly(Side.CLIENT)
   public String func_70384_a() {
      return this.field_70390_d;
   }

   public void func_70385_a(String p_70385_1_) {
      this.field_70390_d = p_70385_1_;
   }

   public boolean func_70386_b() {
      return this.field_70331_k.func_72977_a((double)this.field_70329_l + 0.5D, (double)this.field_70330_m + 0.5D, (double)this.field_70327_n + 0.5D, (double)this.field_82349_r) != null;
   }

   public void func_70316_g() {
      if(this.func_70386_b()) {
         if(this.field_70331_k.field_72995_K) {
            double var1 = (double)((float)this.field_70329_l + this.field_70331_k.field_73012_v.nextFloat());
            double var3 = (double)((float)this.field_70330_m + this.field_70331_k.field_73012_v.nextFloat());
            double var5 = (double)((float)this.field_70327_n + this.field_70331_k.field_73012_v.nextFloat());
            this.field_70331_k.func_72869_a("smoke", var1, var3, var5, 0.0D, 0.0D, 0.0D);
            this.field_70331_k.func_72869_a("flame", var1, var3, var5, 0.0D, 0.0D, 0.0D);
            if(this.field_70394_a > 0) {
               --this.field_70394_a;
            }

            this.field_70393_c = this.field_70392_b;
            this.field_70392_b = (this.field_70392_b + (double)(1000.0F / ((float)this.field_70394_a + 200.0F))) % 360.0D;
         } else {
            if(this.field_70394_a == -1) {
               this.func_70387_f();
            }

            if(this.field_70394_a > 0) {
               --this.field_70394_a;
               return;
            }

            for(int var11 = 0; var11 < this.field_70395_h; ++var11) {
               Entity var2 = EntityList.func_75620_a(this.field_70390_d, this.field_70331_k);
               if(var2 == null) {
                  return;
               }

               int var12 = this.field_70331_k.func_72872_a(var2.getClass(), AxisAlignedBB.func_72332_a().func_72299_a((double)this.field_70329_l, (double)this.field_70330_m, (double)this.field_70327_n, (double)(this.field_70329_l + 1), (double)(this.field_70330_m + 1), (double)(this.field_70327_n + 1)).func_72314_b((double)(this.field_82348_s * 2), 4.0D, (double)(this.field_82348_s * 2))).size();
               if(var12 >= this.field_82350_j) {
                  this.func_70387_f();
                  return;
               }

               if(var2 != null) {
                  double var4 = (double)this.field_70329_l + (this.field_70331_k.field_73012_v.nextDouble() - this.field_70331_k.field_73012_v.nextDouble()) * (double)this.field_82348_s;
                  double var6 = (double)(this.field_70330_m + this.field_70331_k.field_73012_v.nextInt(3) - 1);
                  double var8 = (double)this.field_70327_n + (this.field_70331_k.field_73012_v.nextDouble() - this.field_70331_k.field_73012_v.nextDouble()) * (double)this.field_82348_s;
                  EntityLiving var10 = var2 instanceof EntityLiving?(EntityLiving)var2:null;
                  var2.func_70012_b(var4, var6, var8, this.field_70331_k.field_73012_v.nextFloat() * 360.0F, 0.0F);
                  if(var10 == null || var10.func_70601_bi()) {
                     this.func_70383_a(var2);
                     this.field_70331_k.func_72838_d(var2);
                     this.field_70331_k.func_72926_e(2004, this.field_70329_l, this.field_70330_m, this.field_70327_n, 0);
                     if(var10 != null) {
                        var10.func_70656_aK();
                     }

                     this.func_70387_f();
                  }
               }
            }
         }

         super.func_70316_g();
      }
   }

   public void func_70383_a(Entity p_70383_1_) {
      if(this.field_70391_e != null) {
         NBTTagCompound var2 = new NBTTagCompound();
         p_70383_1_.func_70039_c(var2);
         Iterator var3 = this.field_70391_e.func_74758_c().iterator();

         while(var3.hasNext()) {
            NBTBase var4 = (NBTBase)var3.next();
            var2.func_74782_a(var4.func_74740_e(), var4.func_74737_b());
         }

         p_70383_1_.func_70020_e(var2);
      } else if(p_70383_1_ instanceof EntityLiving && p_70383_1_.field_70170_p != null) {
         ((EntityLiving)p_70383_1_).func_82163_bD();
      }

   }

   private void func_70387_f() {
      if(this.field_70389_g <= this.field_70388_f) {
         this.field_70394_a = this.field_70388_f;
      } else {
         this.field_70394_a = this.field_70388_f + this.field_70331_k.field_73012_v.nextInt(this.field_70389_g - this.field_70388_f);
      }

      this.field_70331_k.func_72965_b(this.field_70329_l, this.field_70330_m, this.field_70327_n, this.func_70311_o().field_71990_ca, 1, 0);
   }

   public void func_70307_a(NBTTagCompound p_70307_1_) {
      super.func_70307_a(p_70307_1_);
      this.field_70390_d = p_70307_1_.func_74779_i("EntityId");
      this.field_70394_a = p_70307_1_.func_74765_d("Delay");
      if(p_70307_1_.func_74764_b("SpawnData")) {
         this.field_70391_e = p_70307_1_.func_74775_l("SpawnData");
      } else {
         this.field_70391_e = null;
      }

      if(p_70307_1_.func_74764_b("MinSpawnDelay")) {
         this.field_70388_f = p_70307_1_.func_74765_d("MinSpawnDelay");
         this.field_70389_g = p_70307_1_.func_74765_d("MaxSpawnDelay");
         this.field_70395_h = p_70307_1_.func_74765_d("SpawnCount");
      }

      if(p_70307_1_.func_74764_b("MaxNearbyEntities")) {
         this.field_82350_j = p_70307_1_.func_74765_d("MaxNearbyEntities");
         this.field_82349_r = p_70307_1_.func_74765_d("RequiredPlayerRange");
      }

      if(p_70307_1_.func_74764_b("SpawnRange")) {
         this.field_82348_s = p_70307_1_.func_74765_d("SpawnRange");
      }

   }

   public void func_70310_b(NBTTagCompound p_70310_1_) {
      super.func_70310_b(p_70310_1_);
      p_70310_1_.func_74778_a("EntityId", this.field_70390_d);
      p_70310_1_.func_74777_a("Delay", (short)this.field_70394_a);
      p_70310_1_.func_74777_a("MinSpawnDelay", (short)this.field_70388_f);
      p_70310_1_.func_74777_a("MaxSpawnDelay", (short)this.field_70389_g);
      p_70310_1_.func_74777_a("SpawnCount", (short)this.field_70395_h);
      p_70310_1_.func_74777_a("MaxNearbyEntities", (short)this.field_82350_j);
      p_70310_1_.func_74777_a("RequiredPlayerRange", (short)this.field_82349_r);
      p_70310_1_.func_74777_a("SpawnRange", (short)this.field_82348_s);
      if(this.field_70391_e != null) {
         p_70310_1_.func_74766_a("SpawnData", this.field_70391_e);
      }

   }

   @SideOnly(Side.CLIENT)
   public Entity func_70382_c() {
      if(this.field_70396_i == null) {
         Entity var1 = EntityList.func_75620_a(this.func_70384_a(), (World)null);
         this.func_70383_a(var1);
         this.field_70396_i = var1;
      }

      return this.field_70396_i;
   }

   public Packet func_70319_e() {
      NBTTagCompound var1 = new NBTTagCompound();
      this.func_70310_b(var1);
      return new Packet132TileEntityData(this.field_70329_l, this.field_70330_m, this.field_70327_n, 1, var1);
   }

   public void func_70315_b(int p_70315_1_, int p_70315_2_) {
      if(p_70315_1_ == 1 && this.field_70331_k.field_72995_K) {
         this.field_70394_a = this.field_70388_f;
      }

   }
}
