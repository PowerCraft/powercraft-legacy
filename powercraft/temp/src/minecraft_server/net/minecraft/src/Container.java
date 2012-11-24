package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public abstract class Container {

   public List field_75153_a = new ArrayList();
   public List field_75151_b = new ArrayList();
   public int field_75152_c = 0;
   private short field_75150_e = 0;
   protected List field_75149_d = new ArrayList();
   private Set field_75148_f = new HashSet();


   protected Slot func_75146_a(Slot p_75146_1_) {
      p_75146_1_.field_75222_d = this.field_75151_b.size();
      this.field_75151_b.add(p_75146_1_);
      this.field_75153_a.add((Object)null);
      return p_75146_1_;
   }

   public void func_75132_a(ICrafting p_75132_1_) {
      if(this.field_75149_d.contains(p_75132_1_)) {
         throw new IllegalArgumentException("Listener already listening");
      } else {
         this.field_75149_d.add(p_75132_1_);
         p_75132_1_.func_71110_a(this, this.func_75138_a());
         this.func_75142_b();
      }
   }

   public List func_75138_a() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < this.field_75151_b.size(); ++var2) {
         var1.add(((Slot)this.field_75151_b.get(var2)).func_75211_c());
      }

      return var1;
   }

   @SideOnly(Side.CLIENT)
   public void func_82847_b(ICrafting p_82847_1_) {
      this.field_75149_d.remove(p_82847_1_);
   }

   public void func_75142_b() {
      for(int var1 = 0; var1 < this.field_75151_b.size(); ++var1) {
         ItemStack var2 = ((Slot)this.field_75151_b.get(var1)).func_75211_c();
         ItemStack var3 = (ItemStack)this.field_75153_a.get(var1);
         if(!ItemStack.func_77989_b(var3, var2)) {
            var3 = var2 == null?null:var2.func_77946_l();
            this.field_75153_a.set(var1, var3);

            for(int var4 = 0; var4 < this.field_75149_d.size(); ++var4) {
               ((ICrafting)this.field_75149_d.get(var4)).func_71111_a(this, var1, var3);
            }
         }
      }

   }

   public boolean func_75140_a(EntityPlayer p_75140_1_, int p_75140_2_) {
      return false;
   }

   public Slot func_75147_a(IInventory p_75147_1_, int p_75147_2_) {
      for(int var3 = 0; var3 < this.field_75151_b.size(); ++var3) {
         Slot var4 = (Slot)this.field_75151_b.get(var3);
         if(var4.func_75217_a(p_75147_1_, p_75147_2_)) {
            return var4;
         }
      }

      return null;
   }

   public Slot func_75139_a(int p_75139_1_) {
      return (Slot)this.field_75151_b.get(p_75139_1_);
   }

   public ItemStack func_82846_b(EntityPlayer p_82846_1_, int p_82846_2_) {
      Slot var3 = (Slot)this.field_75151_b.get(p_82846_2_);
      return var3 != null?var3.func_75211_c():null;
   }

   public ItemStack func_75144_a(int p_75144_1_, int p_75144_2_, int p_75144_3_, EntityPlayer p_75144_4_) {
      ItemStack var5 = null;
      InventoryPlayer var6 = p_75144_4_.field_71071_by;
      Slot var7;
      ItemStack var8;
      int var10;
      ItemStack var11;
      if((p_75144_3_ == 0 || p_75144_3_ == 1) && (p_75144_2_ == 0 || p_75144_2_ == 1)) {
         if(p_75144_1_ == -999) {
            if(var6.func_70445_o() != null && p_75144_1_ == -999) {
               if(p_75144_2_ == 0) {
                  p_75144_4_.func_71021_b(var6.func_70445_o());
                  var6.func_70437_b((ItemStack)null);
               }

               if(p_75144_2_ == 1) {
                  p_75144_4_.func_71021_b(var6.func_70445_o().func_77979_a(1));
                  if(var6.func_70445_o().field_77994_a == 0) {
                     var6.func_70437_b((ItemStack)null);
                  }
               }
            }
         } else if(p_75144_3_ == 1) {
            var7 = (Slot)this.field_75151_b.get(p_75144_1_);
            if(var7 != null && var7.func_82869_a(p_75144_4_)) {
               var8 = this.func_82846_b(p_75144_4_, p_75144_1_);
               if(var8 != null) {
                  int var12 = var8.field_77993_c;
                  var5 = var8.func_77946_l();
                  if(var7 != null && var7.func_75211_c() != null && var7.func_75211_c().field_77993_c == var12) {
                     this.func_75133_b(p_75144_1_, p_75144_2_, true, p_75144_4_);
                  }
               }
            }
         } else {
            if(p_75144_1_ < 0) {
               return null;
            }

            var7 = (Slot)this.field_75151_b.get(p_75144_1_);
            if(var7 != null) {
               var8 = var7.func_75211_c();
               ItemStack var13 = var6.func_70445_o();
               if(var8 != null) {
                  var5 = var8.func_77946_l();
               }

               if(var8 == null) {
                  if(var13 != null && var7.func_75214_a(var13)) {
                     var10 = p_75144_2_ == 0?var13.field_77994_a:1;
                     if(var10 > var7.func_75219_a()) {
                        var10 = var7.func_75219_a();
                     }

                     var7.func_75215_d(var13.func_77979_a(var10));
                     if(var13.field_77994_a == 0) {
                        var6.func_70437_b((ItemStack)null);
                     }
                  }
               } else if(var7.func_82869_a(p_75144_4_)) {
                  if(var13 == null) {
                     var10 = p_75144_2_ == 0?var8.field_77994_a:(var8.field_77994_a + 1) / 2;
                     var11 = var7.func_75209_a(var10);
                     var6.func_70437_b(var11);
                     if(var8.field_77994_a == 0) {
                        var7.func_75215_d((ItemStack)null);
                     }

                     var7.func_82870_a(p_75144_4_, var6.func_70445_o());
                  } else if(var7.func_75214_a(var13)) {
                     if(var8.field_77993_c == var13.field_77993_c && (!var8.func_77981_g() || var8.func_77960_j() == var13.func_77960_j()) && ItemStack.func_77970_a(var8, var13)) {
                        var10 = p_75144_2_ == 0?var13.field_77994_a:1;
                        if(var10 > var7.func_75219_a() - var8.field_77994_a) {
                           var10 = var7.func_75219_a() - var8.field_77994_a;
                        }

                        if(var10 > var13.func_77976_d() - var8.field_77994_a) {
                           var10 = var13.func_77976_d() - var8.field_77994_a;
                        }

                        var13.func_77979_a(var10);
                        if(var13.field_77994_a == 0) {
                           var6.func_70437_b((ItemStack)null);
                        }

                        var8.field_77994_a += var10;
                     } else if(var13.field_77994_a <= var7.func_75219_a()) {
                        var7.func_75215_d(var13);
                        var6.func_70437_b(var8);
                     }
                  } else if(var8.field_77993_c == var13.field_77993_c && var13.func_77976_d() > 1 && (!var8.func_77981_g() || var8.func_77960_j() == var13.func_77960_j()) && ItemStack.func_77970_a(var8, var13)) {
                     var10 = var8.field_77994_a;
                     if(var10 > 0 && var10 + var13.field_77994_a <= var13.func_77976_d()) {
                        var13.field_77994_a += var10;
                        var8 = var7.func_75209_a(var10);
                        if(var8.field_77994_a == 0) {
                           var7.func_75215_d((ItemStack)null);
                        }

                        var7.func_82870_a(p_75144_4_, var6.func_70445_o());
                     }
                  }
               }

               var7.func_75218_e();
            }
         }
      } else if(p_75144_3_ == 2 && p_75144_2_ >= 0 && p_75144_2_ < 9) {
         var7 = (Slot)this.field_75151_b.get(p_75144_1_);
         if(var7.func_82869_a(p_75144_4_)) {
            var8 = var6.func_70301_a(p_75144_2_);
            boolean var9 = var8 == null || var7.field_75224_c == var6 && var7.func_75214_a(var8);
            var10 = -1;
            if(!var9) {
               var10 = var6.func_70447_i();
               var9 |= var10 > -1;
            }

            if(var7.func_75216_d() && var9) {
               var11 = var7.func_75211_c();
               var6.func_70299_a(p_75144_2_, var11);
               if((var7.field_75224_c != var6 || !var7.func_75214_a(var8)) && var8 != null) {
                  if(var10 > -1) {
                     var6.func_70441_a(var8);
                     var7.func_75215_d((ItemStack)null);
                     var7.func_82870_a(p_75144_4_, var11);
                  }
               } else {
                  var7.func_75215_d(var8);
                  var7.func_82870_a(p_75144_4_, var11);
               }
            } else if(!var7.func_75216_d() && var8 != null && var7.func_75214_a(var8)) {
               var6.func_70299_a(p_75144_2_, (ItemStack)null);
               var7.func_75215_d(var8);
            }
         }
      } else if(p_75144_3_ == 3 && p_75144_4_.field_71075_bZ.field_75098_d && var6.func_70445_o() == null && p_75144_1_ >= 0) {
         var7 = (Slot)this.field_75151_b.get(p_75144_1_);
         if(var7 != null && var7.func_75216_d()) {
            var8 = var7.func_75211_c().func_77946_l();
            var8.field_77994_a = var8.func_77976_d();
            var6.func_70437_b(var8);
         }
      }

      return var5;
   }

   protected void func_75133_b(int p_75133_1_, int p_75133_2_, boolean p_75133_3_, EntityPlayer p_75133_4_) {
      this.func_75144_a(p_75133_1_, p_75133_2_, 1, p_75133_4_);
   }

   public void func_75134_a(EntityPlayer p_75134_1_) {
      InventoryPlayer var2 = p_75134_1_.field_71071_by;
      if(var2.func_70445_o() != null) {
         p_75134_1_.func_71021_b(var2.func_70445_o());
         var2.func_70437_b((ItemStack)null);
      }

   }

   public void func_75130_a(IInventory p_75130_1_) {
      this.func_75142_b();
   }

   public void func_75141_a(int p_75141_1_, ItemStack p_75141_2_) {
      this.func_75139_a(p_75141_1_).func_75215_d(p_75141_2_);
   }

   @SideOnly(Side.CLIENT)
   public void func_75131_a(ItemStack[] p_75131_1_) {
      for(int var2 = 0; var2 < p_75131_1_.length; ++var2) {
         this.func_75139_a(var2).func_75215_d(p_75131_1_[var2]);
      }

   }

   @SideOnly(Side.CLIENT)
   public void func_75137_b(int p_75137_1_, int p_75137_2_) {}

   @SideOnly(Side.CLIENT)
   public short func_75136_a(InventoryPlayer p_75136_1_) {
      ++this.field_75150_e;
      return this.field_75150_e;
   }

   public boolean func_75129_b(EntityPlayer p_75129_1_) {
      return !this.field_75148_f.contains(p_75129_1_);
   }

   public void func_75128_a(EntityPlayer p_75128_1_, boolean p_75128_2_) {
      if(p_75128_2_) {
         this.field_75148_f.remove(p_75128_1_);
      } else {
         this.field_75148_f.add(p_75128_1_);
      }

   }

   public abstract boolean func_75145_c(EntityPlayer var1);

   protected boolean func_75135_a(ItemStack p_75135_1_, int p_75135_2_, int p_75135_3_, boolean p_75135_4_) {
      boolean var5 = false;
      int var6 = p_75135_2_;
      if(p_75135_4_) {
         var6 = p_75135_3_ - 1;
      }

      Slot var7;
      ItemStack var8;
      if(p_75135_1_.func_77985_e()) {
         while(p_75135_1_.field_77994_a > 0 && (!p_75135_4_ && var6 < p_75135_3_ || p_75135_4_ && var6 >= p_75135_2_)) {
            var7 = (Slot)this.field_75151_b.get(var6);
            var8 = var7.func_75211_c();
            if(var8 != null && var8.field_77993_c == p_75135_1_.field_77993_c && (!p_75135_1_.func_77981_g() || p_75135_1_.func_77960_j() == var8.func_77960_j()) && ItemStack.func_77970_a(p_75135_1_, var8)) {
               int var9 = var8.field_77994_a + p_75135_1_.field_77994_a;
               if(var9 <= p_75135_1_.func_77976_d()) {
                  p_75135_1_.field_77994_a = 0;
                  var8.field_77994_a = var9;
                  var7.func_75218_e();
                  var5 = true;
               } else if(var8.field_77994_a < p_75135_1_.func_77976_d()) {
                  p_75135_1_.field_77994_a -= p_75135_1_.func_77976_d() - var8.field_77994_a;
                  var8.field_77994_a = p_75135_1_.func_77976_d();
                  var7.func_75218_e();
                  var5 = true;
               }
            }

            if(p_75135_4_) {
               --var6;
            } else {
               ++var6;
            }
         }
      }

      if(p_75135_1_.field_77994_a > 0) {
         if(p_75135_4_) {
            var6 = p_75135_3_ - 1;
         } else {
            var6 = p_75135_2_;
         }

         while(!p_75135_4_ && var6 < p_75135_3_ || p_75135_4_ && var6 >= p_75135_2_) {
            var7 = (Slot)this.field_75151_b.get(var6);
            var8 = var7.func_75211_c();
            if(var8 == null) {
               var7.func_75215_d(p_75135_1_.func_77946_l());
               var7.func_75218_e();
               p_75135_1_.field_77994_a = 0;
               var5 = true;
               break;
            }

            if(p_75135_4_) {
               --var6;
            } else {
               ++var6;
            }
         }
      }

      return var5;
   }
}
