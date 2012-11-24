package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Block;
import net.minecraft.src.BlockFurnace;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemHoe;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ItemSword;
import net.minecraft.src.ItemTool;
import net.minecraft.src.Material;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntity;

public class TileEntityFurnace extends TileEntity implements IInventory {

   private ItemStack[] field_70404_d = new ItemStack[3];
   public int field_70407_a = 0;
   public int field_70405_b = 0;
   public int field_70406_c = 0;


   public int func_70302_i_() {
      return this.field_70404_d.length;
   }

   public ItemStack func_70301_a(int p_70301_1_) {
      return this.field_70404_d[p_70301_1_];
   }

   public ItemStack func_70298_a(int p_70298_1_, int p_70298_2_) {
      if(this.field_70404_d[p_70298_1_] != null) {
         ItemStack var3;
         if(this.field_70404_d[p_70298_1_].field_77994_a <= p_70298_2_) {
            var3 = this.field_70404_d[p_70298_1_];
            this.field_70404_d[p_70298_1_] = null;
            return var3;
         } else {
            var3 = this.field_70404_d[p_70298_1_].func_77979_a(p_70298_2_);
            if(this.field_70404_d[p_70298_1_].field_77994_a == 0) {
               this.field_70404_d[p_70298_1_] = null;
            }

            return var3;
         }
      } else {
         return null;
      }
   }

   public ItemStack func_70304_b(int p_70304_1_) {
      if(this.field_70404_d[p_70304_1_] != null) {
         ItemStack var2 = this.field_70404_d[p_70304_1_];
         this.field_70404_d[p_70304_1_] = null;
         return var2;
      } else {
         return null;
      }
   }

   public void func_70299_a(int p_70299_1_, ItemStack p_70299_2_) {
      this.field_70404_d[p_70299_1_] = p_70299_2_;
      if(p_70299_2_ != null && p_70299_2_.field_77994_a > this.func_70297_j_()) {
         p_70299_2_.field_77994_a = this.func_70297_j_();
      }

   }

   public String func_70303_b() {
      return "container.furnace";
   }

   public void func_70307_a(NBTTagCompound p_70307_1_) {
      super.func_70307_a(p_70307_1_);
      NBTTagList var2 = p_70307_1_.func_74761_m("Items");
      this.field_70404_d = new ItemStack[this.func_70302_i_()];

      for(int var3 = 0; var3 < var2.func_74745_c(); ++var3) {
         NBTTagCompound var4 = (NBTTagCompound)var2.func_74743_b(var3);
         byte var5 = var4.func_74771_c("Slot");
         if(var5 >= 0 && var5 < this.field_70404_d.length) {
            this.field_70404_d[var5] = ItemStack.func_77949_a(var4);
         }
      }

      this.field_70407_a = p_70307_1_.func_74765_d("BurnTime");
      this.field_70406_c = p_70307_1_.func_74765_d("CookTime");
      this.field_70405_b = func_70398_a(this.field_70404_d[1]);
   }

   public void func_70310_b(NBTTagCompound p_70310_1_) {
      super.func_70310_b(p_70310_1_);
      p_70310_1_.func_74777_a("BurnTime", (short)this.field_70407_a);
      p_70310_1_.func_74777_a("CookTime", (short)this.field_70406_c);
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.field_70404_d.length; ++var3) {
         if(this.field_70404_d[var3] != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.func_74774_a("Slot", (byte)var3);
            this.field_70404_d[var3].func_77955_b(var4);
            var2.func_74742_a(var4);
         }
      }

      p_70310_1_.func_74782_a("Items", var2);
   }

   public int func_70297_j_() {
      return 64;
   }

   @SideOnly(Side.CLIENT)
   public int func_70397_c(int p_70397_1_) {
      return this.field_70406_c * p_70397_1_ / 200;
   }

   @SideOnly(Side.CLIENT)
   public int func_70403_d(int p_70403_1_) {
      if(this.field_70405_b == 0) {
         this.field_70405_b = 200;
      }

      return this.field_70407_a * p_70403_1_ / this.field_70405_b;
   }

   public boolean func_70400_i() {
      return this.field_70407_a > 0;
   }

   public void func_70316_g() {
      boolean var1 = this.field_70407_a > 0;
      boolean var2 = false;
      if(this.field_70407_a > 0) {
         --this.field_70407_a;
      }

      if(!this.field_70331_k.field_72995_K) {
         if(this.field_70407_a == 0 && this.func_70402_r()) {
            this.field_70405_b = this.field_70407_a = func_70398_a(this.field_70404_d[1]);
            if(this.field_70407_a > 0) {
               var2 = true;
               if(this.field_70404_d[1] != null) {
                  --this.field_70404_d[1].field_77994_a;
                  if(this.field_70404_d[1].field_77994_a == 0) {
                     Item var3 = this.field_70404_d[1].func_77973_b().func_77668_q();
                     this.field_70404_d[1] = var3 != null?new ItemStack(var3):null;
                  }
               }
            }
         }

         if(this.func_70400_i() && this.func_70402_r()) {
            ++this.field_70406_c;
            if(this.field_70406_c == 200) {
               this.field_70406_c = 0;
               this.func_70399_k();
               var2 = true;
            }
         } else {
            this.field_70406_c = 0;
         }

         if(var1 != this.field_70407_a > 0) {
            var2 = true;
            BlockFurnace.func_72286_a(this.field_70407_a > 0, this.field_70331_k, this.field_70329_l, this.field_70330_m, this.field_70327_n);
         }
      }

      if(var2) {
         this.func_70296_d();
      }

   }

   private boolean func_70402_r() {
      if(this.field_70404_d[0] == null) {
         return false;
      } else {
         ItemStack var1 = FurnaceRecipes.func_77602_a().func_77603_b(this.field_70404_d[0].func_77973_b().field_77779_bT);
         return var1 == null?false:(this.field_70404_d[2] == null?true:(!this.field_70404_d[2].func_77969_a(var1)?false:(this.field_70404_d[2].field_77994_a < this.func_70297_j_() && this.field_70404_d[2].field_77994_a < this.field_70404_d[2].func_77976_d()?true:this.field_70404_d[2].field_77994_a < var1.func_77976_d())));
      }
   }

   public void func_70399_k() {
      if(this.func_70402_r()) {
         ItemStack var1 = FurnaceRecipes.func_77602_a().func_77603_b(this.field_70404_d[0].func_77973_b().field_77779_bT);
         if(this.field_70404_d[2] == null) {
            this.field_70404_d[2] = var1.func_77946_l();
         } else if(this.field_70404_d[2].field_77993_c == var1.field_77993_c) {
            ++this.field_70404_d[2].field_77994_a;
         }

         --this.field_70404_d[0].field_77994_a;
         if(this.field_70404_d[0].field_77994_a <= 0) {
            this.field_70404_d[0] = null;
         }

      }
   }

   public static int func_70398_a(ItemStack p_70398_0_) {
      if(p_70398_0_ == null) {
         return 0;
      } else {
         int var1 = p_70398_0_.func_77973_b().field_77779_bT;
         Item var2 = p_70398_0_.func_77973_b();
         if(var1 < 256 && Block.field_71973_m[var1] != null) {
            Block var3 = Block.field_71973_m[var1];
            if(var3 == Block.field_72092_bO) {
               return 150;
            }

            if(var3.field_72018_cp == Material.field_76245_d) {
               return 300;
            }
         }

         return var2 instanceof ItemTool && ((ItemTool)var2).func_77861_e().equals("WOOD")?200:(var2 instanceof ItemSword && ((ItemSword)var2).func_77825_f().equals("WOOD")?200:(var2 instanceof ItemHoe && ((ItemHoe)var2).func_77842_f().equals("WOOD")?200:(var1 == Item.field_77669_D.field_77779_bT?100:(var1 == Item.field_77705_m.field_77779_bT?1600:(var1 == Item.field_77775_ay.field_77779_bT?20000:(var1 == Block.field_71987_y.field_71990_ca?100:(var1 == Item.field_77731_bo.field_77779_bT?2400:0)))))));
      }
   }

   public static boolean func_70401_b(ItemStack p_70401_0_) {
      return func_70398_a(p_70401_0_) > 0;
   }

   public boolean func_70300_a(EntityPlayer p_70300_1_) {
      return this.field_70331_k.func_72796_p(this.field_70329_l, this.field_70330_m, this.field_70327_n) != this?false:p_70300_1_.func_70092_e((double)this.field_70329_l + 0.5D, (double)this.field_70330_m + 0.5D, (double)this.field_70327_n + 0.5D) <= 64.0D;
   }

   public void func_70295_k_() {}

   public void func_70305_f() {}
}
