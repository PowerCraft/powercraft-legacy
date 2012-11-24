package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IMerchant;
import net.minecraft.src.InventoryMerchant;
import net.minecraft.src.MerchantRecipe;
import net.minecraft.src.MerchantRecipeList;

@SideOnly(Side.CLIENT)
public class NpcMerchant implements IMerchant {

   private InventoryMerchant field_70937_a;
   private EntityPlayer field_70935_b;
   private MerchantRecipeList field_70936_c;


   public NpcMerchant(EntityPlayer p_i3559_1_) {
      this.field_70935_b = p_i3559_1_;
      this.field_70937_a = new InventoryMerchant(p_i3559_1_, this);
   }

   public EntityPlayer func_70931_l_() {
      return this.field_70935_b;
   }

   public void func_70932_a_(EntityPlayer p_70932_1_) {}

   public MerchantRecipeList func_70934_b(EntityPlayer p_70934_1_) {
      return this.field_70936_c;
   }

   public void func_70930_a(MerchantRecipeList p_70930_1_) {
      this.field_70936_c = p_70930_1_;
   }

   public void func_70933_a(MerchantRecipe p_70933_1_) {}
}
