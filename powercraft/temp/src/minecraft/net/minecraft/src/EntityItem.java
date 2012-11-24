package net.minecraft.src;

import java.util.Iterator;
import net.minecraft.src.AchievementList;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.StatCollector;
import net.minecraft.src.World;

public class EntityItem extends Entity {

   public ItemStack field_70294_a;
   public int field_70292_b = 0;
   public int field_70293_c;
   private int field_70291_e = 5;
   public float field_70290_d = (float)(Math.random() * 3.141592653589793D * 2.0D);


   public EntityItem(World p_i3539_1_, double p_i3539_2_, double p_i3539_4_, double p_i3539_6_, ItemStack p_i3539_8_) {
      super(p_i3539_1_);
      this.func_70105_a(0.25F, 0.25F);
      this.field_70129_M = this.field_70131_O / 2.0F;
      this.func_70107_b(p_i3539_2_, p_i3539_4_, p_i3539_6_);
      this.field_70294_a = p_i3539_8_;
      this.field_70177_z = (float)(Math.random() * 360.0D);
      this.field_70159_w = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
      this.field_70181_x = 0.20000000298023224D;
      this.field_70179_y = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
   }

   protected boolean func_70041_e_() {
      return false;
   }

   public EntityItem(World p_i3540_1_) {
      super(p_i3540_1_);
      this.func_70105_a(0.25F, 0.25F);
      this.field_70129_M = this.field_70131_O / 2.0F;
   }

   protected void func_70088_a() {}

   public void func_70071_h_() {
      super.func_70071_h_();
      if(this.field_70293_c > 0) {
         --this.field_70293_c;
      }

      this.field_70169_q = this.field_70165_t;
      this.field_70167_r = this.field_70163_u;
      this.field_70166_s = this.field_70161_v;
      this.field_70181_x -= 0.03999999910593033D;
      this.field_70145_X = this.func_70048_i(this.field_70165_t, (this.field_70121_D.field_72338_b + this.field_70121_D.field_72337_e) / 2.0D, this.field_70161_v);
      this.func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
      boolean var1 = (int)this.field_70169_q != (int)this.field_70165_t || (int)this.field_70167_r != (int)this.field_70163_u || (int)this.field_70166_s != (int)this.field_70161_v;
      if(var1) {
         if(this.field_70170_p.func_72803_f(MathHelper.func_76128_c(this.field_70165_t), MathHelper.func_76128_c(this.field_70163_u), MathHelper.func_76128_c(this.field_70161_v)) == Material.field_76256_h) {
            this.field_70181_x = 0.20000000298023224D;
            this.field_70159_w = (double)((this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F);
            this.field_70179_y = (double)((this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F);
            this.func_85030_a("random.fizz", 0.4F, 2.0F + this.field_70146_Z.nextFloat() * 0.4F);
         }

         if(!this.field_70170_p.field_72995_K) {
            this.func_85054_d();
         }
      }

      float var2 = 0.98F;
      if(this.field_70122_E) {
         var2 = 0.58800006F;
         int var3 = this.field_70170_p.func_72798_a(MathHelper.func_76128_c(this.field_70165_t), MathHelper.func_76128_c(this.field_70121_D.field_72338_b) - 1, MathHelper.func_76128_c(this.field_70161_v));
         if(var3 > 0) {
            var2 = Block.field_71973_m[var3].field_72016_cq * 0.98F;
         }
      }

      this.field_70159_w *= (double)var2;
      this.field_70181_x *= 0.9800000190734863D;
      this.field_70179_y *= (double)var2;
      if(this.field_70122_E) {
         this.field_70181_x *= -0.5D;
      }

      ++this.field_70292_b;
      if(!this.field_70170_p.field_72995_K && this.field_70292_b >= 6000) {
         this.func_70106_y();
      }

   }

   private void func_85054_d() {
      Iterator var1 = this.field_70170_p.func_72872_a(EntityItem.class, this.field_70121_D.func_72314_b(0.5D, 0.0D, 0.5D)).iterator();

      while(var1.hasNext()) {
         EntityItem var2 = (EntityItem)var1.next();
         this.func_70289_a(var2);
      }

   }

   public boolean func_70289_a(EntityItem p_70289_1_) {
      if(p_70289_1_ == this) {
         return false;
      } else if(p_70289_1_.func_70089_S() && this.func_70089_S()) {
         if(p_70289_1_.field_70294_a.func_77973_b() != this.field_70294_a.func_77973_b()) {
            return false;
         } else if(p_70289_1_.field_70294_a.func_77942_o() ^ this.field_70294_a.func_77942_o()) {
            return false;
         } else if(p_70289_1_.field_70294_a.func_77942_o() && !p_70289_1_.field_70294_a.func_77978_p().equals(this.field_70294_a.func_77978_p())) {
            return false;
         } else if(p_70289_1_.field_70294_a.func_77973_b().func_77614_k() && p_70289_1_.field_70294_a.func_77960_j() != this.field_70294_a.func_77960_j()) {
            return false;
         } else if(p_70289_1_.field_70294_a.field_77994_a < this.field_70294_a.field_77994_a) {
            return p_70289_1_.func_70289_a(this);
         } else if(p_70289_1_.field_70294_a.field_77994_a + this.field_70294_a.field_77994_a > p_70289_1_.field_70294_a.func_77976_d()) {
            return false;
         } else {
            p_70289_1_.field_70294_a.field_77994_a += this.field_70294_a.field_77994_a;
            p_70289_1_.field_70293_c = Math.max(p_70289_1_.field_70293_c, this.field_70293_c);
            p_70289_1_.field_70292_b = Math.min(p_70289_1_.field_70292_b, this.field_70292_b);
            this.func_70106_y();
            return true;
         }
      } else {
         return false;
      }
   }

   public void func_70288_d() {
      this.field_70292_b = 4800;
   }

   public boolean func_70072_I() {
      return this.field_70170_p.func_72918_a(this.field_70121_D, Material.field_76244_g, this);
   }

   protected void func_70081_e(int p_70081_1_) {
      this.func_70097_a(DamageSource.field_76372_a, p_70081_1_);
   }

   public boolean func_70097_a(DamageSource p_70097_1_, int p_70097_2_) {
      if(this.func_85032_ar()) {
         return false;
      } else {
         this.func_70018_K();
         this.field_70291_e -= p_70097_2_;
         if(this.field_70291_e <= 0) {
            this.func_70106_y();
         }

         return false;
      }
   }

   public void func_70014_b(NBTTagCompound p_70014_1_) {
      p_70014_1_.func_74777_a("Health", (short)((byte)this.field_70291_e));
      p_70014_1_.func_74777_a("Age", (short)this.field_70292_b);
      if(this.field_70294_a != null) {
         p_70014_1_.func_74766_a("Item", this.field_70294_a.func_77955_b(new NBTTagCompound()));
      }

   }

   public void func_70037_a(NBTTagCompound p_70037_1_) {
      this.field_70291_e = p_70037_1_.func_74765_d("Health") & 255;
      this.field_70292_b = p_70037_1_.func_74765_d("Age");
      NBTTagCompound var2 = p_70037_1_.func_74775_l("Item");
      this.field_70294_a = ItemStack.func_77949_a(var2);
      if(this.field_70294_a == null) {
         this.func_70106_y();
      }

   }

   public void func_70100_b_(EntityPlayer p_70100_1_) {
      if(!this.field_70170_p.field_72995_K) {
         int var2 = this.field_70294_a.field_77994_a;
         if(this.field_70293_c == 0 && p_70100_1_.field_71071_by.func_70441_a(this.field_70294_a)) {
            if(this.field_70294_a.field_77993_c == Block.field_71951_J.field_71990_ca) {
               p_70100_1_.func_71029_a(AchievementList.field_76005_g);
            }

            if(this.field_70294_a.field_77993_c == Item.field_77770_aF.field_77779_bT) {
               p_70100_1_.func_71029_a(AchievementList.field_76022_t);
            }

            if(this.field_70294_a.field_77993_c == Item.field_77702_n.field_77779_bT) {
               p_70100_1_.func_71029_a(AchievementList.field_76019_w);
            }

            if(this.field_70294_a.field_77993_c == Item.field_77731_bo.field_77779_bT) {
               p_70100_1_.func_71029_a(AchievementList.field_76027_z);
            }

            this.func_85030_a("random.pop", 0.2F, ((this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            p_70100_1_.func_71001_a(this, var2);
            if(this.field_70294_a.field_77994_a <= 0) {
               this.func_70106_y();
            }
         }

      }
   }

   public String func_70023_ak() {
      return StatCollector.func_74838_a("item." + this.field_70294_a.func_77977_a());
   }

   public boolean func_70075_an() {
      return false;
   }

   public void func_71027_c(int p_71027_1_) {
      super.func_71027_c(p_71027_1_);
      if(!this.field_70170_p.field_72995_K) {
         this.func_85054_d();
      }

   }
}
