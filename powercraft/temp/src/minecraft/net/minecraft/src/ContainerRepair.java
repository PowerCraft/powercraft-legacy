package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.src.Block;
import net.minecraft.src.Container;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EnchantmentHelper;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryCraftResult;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.InventoryRepair;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import net.minecraft.src.SlotRepair;
import net.minecraft.src.World;

public class ContainerRepair extends Container {

   private IInventory field_82852_f = new InventoryCraftResult();
   private IInventory field_82853_g = new InventoryRepair(this, "Repair", 2);
   private World field_82860_h;
   private int field_82861_i;
   private int field_82858_j;
   private int field_82859_k;
   public int field_82854_e = 0;
   private int field_82856_l = 0;
   private String field_82857_m;
   private final EntityPlayer field_82855_n;


   public ContainerRepair(InventoryPlayer p_i5080_1_, World p_i5080_2_, int p_i5080_3_, int p_i5080_4_, int p_i5080_5_, EntityPlayer p_i5080_6_) {
      this.field_82860_h = p_i5080_2_;
      this.field_82861_i = p_i5080_3_;
      this.field_82858_j = p_i5080_4_;
      this.field_82859_k = p_i5080_5_;
      this.field_82855_n = p_i5080_6_;
      this.func_75146_a(new Slot(this.field_82853_g, 0, 27, 47));
      this.func_75146_a(new Slot(this.field_82853_g, 1, 76, 47));
      this.func_75146_a(new SlotRepair(this, this.field_82852_f, 2, 134, 47, p_i5080_2_, p_i5080_3_, p_i5080_4_, p_i5080_5_));

      int var7;
      for(var7 = 0; var7 < 3; ++var7) {
         for(int var8 = 0; var8 < 9; ++var8) {
            this.func_75146_a(new Slot(p_i5080_1_, var8 + var7 * 9 + 9, 8 + var8 * 18, 84 + var7 * 18));
         }
      }

      for(var7 = 0; var7 < 9; ++var7) {
         this.func_75146_a(new Slot(p_i5080_1_, var7, 8 + var7 * 18, 142));
      }

   }

   public void func_75130_a(IInventory p_75130_1_) {
      super.func_75130_a(p_75130_1_);
      if(p_75130_1_ == this.field_82853_g) {
         this.func_82848_d();
      }

   }

   public void func_82848_d() {
      ItemStack var1 = this.field_82853_g.func_70301_a(0);
      this.field_82854_e = 0;
      int var2 = 0;
      byte var3 = 0;
      int var4 = 0;
      if(var1 == null) {
         this.field_82852_f.func_70299_a(0, (ItemStack)null);
         this.field_82854_e = 0;
      } else {
         ItemStack var5 = var1.func_77946_l();
         ItemStack var6 = this.field_82853_g.func_70301_a(1);
         Map var7 = EnchantmentHelper.func_82781_a(var5);
         int var18 = var3 + var1.func_82838_A() + (var6 == null?0:var6.func_82838_A());
         this.field_82856_l = 0;
         int var8;
         int var9;
         int var10;
         int var12;
         Enchantment var21;
         Iterator var20;
         if(var6 != null) {
            if(var5.func_77984_f() && Item.field_77698_e[var5.field_77993_c].func_82789_a(var1, var6)) {
               var8 = Math.min(var5.func_77952_i(), var5.func_77958_k() / 4);
               if(var8 <= 0) {
                  this.field_82852_f.func_70299_a(0, (ItemStack)null);
                  this.field_82854_e = 0;
                  return;
               }

               for(var9 = 0; var8 > 0 && var9 < var6.field_77994_a; ++var9) {
                  var10 = var5.func_77952_i() - var8;
                  var5.func_77964_b(var10);
                  var2 += Math.max(1, var8 / 100) + var7.size();
                  var8 = Math.min(var5.func_77952_i(), var5.func_77958_k() / 4);
               }

               this.field_82856_l = var9;
            } else {
               if(var5.field_77993_c != var6.field_77993_c || !var5.func_77984_f()) {
                  this.field_82852_f.func_70299_a(0, (ItemStack)null);
                  this.field_82854_e = 0;
                  return;
               }

               if(var5.func_77984_f()) {
                  var8 = var1.func_77958_k() - var1.func_77952_i();
                  var9 = var6.func_77958_k() - var6.func_77952_i();
                  var10 = var9 + var5.func_77958_k() * 12 / 100;
                  int var11 = var8 + var10;
                  var12 = var5.func_77958_k() - var11;
                  if(var12 < 0) {
                     var12 = 0;
                  }

                  if(var12 < var5.func_77960_j()) {
                     var5.func_77964_b(var12);
                     var2 += Math.max(1, var10 / 100);
                  }
               }

               Map var19 = EnchantmentHelper.func_82781_a(var6);
               var20 = var19.keySet().iterator();

               while(var20.hasNext()) {
                  var10 = ((Integer)var20.next()).intValue();
                  var21 = Enchantment.field_77331_b[var10];
                  var12 = var7.containsKey(Integer.valueOf(var10))?((Integer)var7.get(Integer.valueOf(var10))).intValue():0;
                  int var13 = ((Integer)var19.get(Integer.valueOf(var10))).intValue();
                  int var10000;
                  if(var12 == var13) {
                     ++var13;
                     var10000 = var13;
                  } else {
                     var10000 = Math.max(var13, var12);
                  }

                  var13 = var10000;
                  int var14 = var13 - var12;
                  boolean var15 = true;
                  Iterator var16 = var7.keySet().iterator();

                  while(var16.hasNext()) {
                     int var17 = ((Integer)var16.next()).intValue();
                     if(var17 != var10 && !var21.func_77326_a(Enchantment.field_77331_b[var17])) {
                        var15 = false;
                        var2 += var14;
                     }
                  }

                  if(var15) {
                     if(var13 > var21.func_77325_b()) {
                        var13 = var21.func_77325_b();
                     }

                     var7.put(Integer.valueOf(var10), Integer.valueOf(var13));
                     byte var23 = 0;
                     switch(var21.func_77324_c()) {
                     case 1:
                        var23 = 8;
                        break;
                     case 2:
                        var23 = 4;
                     case 3:
                     case 4:
                     case 6:
                     case 7:
                     case 8:
                     case 9:
                     default:
                        break;
                     case 5:
                        var23 = 2;
                        break;
                     case 10:
                        var23 = 1;
                     }

                     var2 += var23 * var14;
                  }
               }
            }
         }

         if(this.field_82857_m != null && !this.field_82857_m.equalsIgnoreCase(var1.func_82833_r()) && this.field_82857_m.length() > 0) {
            var4 = var1.func_77984_f()?7:var1.field_77994_a * 5;
            var2 += var4;
            if(var1.func_82837_s()) {
               var18 += var4 / 2;
            }

            var5.func_82834_c(this.field_82857_m);
         }

         var8 = 0;

         byte var22;
         for(var20 = var7.keySet().iterator(); var20.hasNext(); var18 += var8 + var12 * var22) {
            var10 = ((Integer)var20.next()).intValue();
            var21 = Enchantment.field_77331_b[var10];
            var12 = ((Integer)var7.get(Integer.valueOf(var10))).intValue();
            var22 = 0;
            ++var8;
            switch(var21.func_77324_c()) {
            case 1:
               var22 = 8;
               break;
            case 2:
               var22 = 4;
            case 3:
            case 4:
            case 6:
            case 7:
            case 8:
            case 9:
            default:
               break;
            case 5:
               var22 = 2;
               break;
            case 10:
               var22 = 1;
            }
         }

         this.field_82854_e = var18 + var2;
         if(var2 <= 0) {
            var5 = null;
         }

         if(var4 == var2 && var4 > 0 && this.field_82854_e >= 40) {
            System.out.println("Naming an item only, cost too high; giving discount to cap cost to 39 levels");
            this.field_82854_e = 39;
         }

         if(this.field_82854_e >= 40 && !this.field_82855_n.field_71075_bZ.field_75098_d) {
            var5 = null;
         }

         if(var5 != null) {
            var9 = var5.func_82838_A();
            if(var6 != null && var9 < var6.func_82838_A()) {
               var9 = var6.func_82838_A();
            }

            if(var5.func_82837_s()) {
               var9 -= 9;
            }

            if(var9 < 0) {
               var9 = 0;
            }

            var9 += 2;
            var5.func_82841_c(var9);
            EnchantmentHelper.func_82782_a(var7, var5);
         }

         this.field_82852_f.func_70299_a(0, var5);
         this.func_75142_b();
      }
   }

   public void func_75132_a(ICrafting p_75132_1_) {
      super.func_75132_a(p_75132_1_);
      p_75132_1_.func_71112_a(this, 0, this.field_82854_e);
   }

   @SideOnly(Side.CLIENT)
   public void func_75137_b(int p_75137_1_, int p_75137_2_) {
      if(p_75137_1_ == 0) {
         this.field_82854_e = p_75137_2_;
      }

   }

   public void func_75134_a(EntityPlayer p_75134_1_) {
      super.func_75134_a(p_75134_1_);
      if(!this.field_82860_h.field_72995_K) {
         for(int var2 = 0; var2 < this.field_82853_g.func_70302_i_(); ++var2) {
            ItemStack var3 = this.field_82853_g.func_70304_b(var2);
            if(var3 != null) {
               p_75134_1_.func_71021_b(var3);
            }
         }

      }
   }

   public boolean func_75145_c(EntityPlayer p_75145_1_) {
      return this.field_82860_h.func_72798_a(this.field_82861_i, this.field_82858_j, this.field_82859_k) != Block.field_82510_ck.field_71990_ca?false:p_75145_1_.func_70092_e((double)this.field_82861_i + 0.5D, (double)this.field_82858_j + 0.5D, (double)this.field_82859_k + 0.5D) <= 64.0D;
   }

   public ItemStack func_82846_b(EntityPlayer p_82846_1_, int p_82846_2_) {
      ItemStack var3 = null;
      Slot var4 = (Slot)this.field_75151_b.get(p_82846_2_);
      if(var4 != null && var4.func_75216_d()) {
         ItemStack var5 = var4.func_75211_c();
         var3 = var5.func_77946_l();
         if(p_82846_2_ == 2) {
            if(!this.func_75135_a(var5, 3, 39, true)) {
               return null;
            }

            var4.func_75220_a(var5, var3);
         } else if(p_82846_2_ != 0 && p_82846_2_ != 1) {
            if(p_82846_2_ >= 3 && p_82846_2_ < 39 && !this.func_75135_a(var5, 0, 2, false)) {
               return null;
            }
         } else if(!this.func_75135_a(var5, 3, 39, false)) {
            return null;
         }

         if(var5.field_77994_a == 0) {
            var4.func_75215_d((ItemStack)null);
         } else {
            var4.func_75218_e();
         }

         if(var5.field_77994_a == var3.field_77994_a) {
            return null;
         }

         var4.func_82870_a(p_82846_1_, var5);
      }

      return var3;
   }

   public void func_82850_a(String p_82850_1_) {
      this.field_82857_m = p_82850_1_;
      if(this.func_75139_a(2).func_75216_d()) {
         this.func_75139_a(2).func_75211_c().func_82834_c(this.field_82857_m);
      }

      this.func_82848_d();
   }

   // $FF: synthetic method
   static IInventory func_82851_a(ContainerRepair p_82851_0_) {
      return p_82851_0_.field_82853_g;
   }

   // $FF: synthetic method
   static int func_82849_b(ContainerRepair p_82849_0_) {
      return p_82849_0_.field_82856_l;
   }
}
