package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntitySpider;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.World;

public class EntityCaveSpider extends EntitySpider {

   public EntityCaveSpider(World p_i3546_1_) {
      super(p_i3546_1_);
      this.field_70750_az = "/mob/cavespider.png";
      this.func_70105_a(0.7F, 0.5F);
   }

   public int func_70667_aM() {
      return 12;
   }

   @SideOnly(Side.CLIENT)
   public float func_70840_n() {
      return 0.7F;
   }

   public boolean func_70652_k(Entity p_70652_1_) {
      if(super.func_70652_k(p_70652_1_)) {
         if(p_70652_1_ instanceof EntityLiving) {
            byte var2 = 0;
            if(this.field_70170_p.field_73013_u > 1) {
               if(this.field_70170_p.field_73013_u == 2) {
                  var2 = 7;
               } else if(this.field_70170_p.field_73013_u == 3) {
                  var2 = 15;
               }
            }

            if(var2 > 0) {
               ((EntityLiving)p_70652_1_).func_70690_d(new PotionEffect(Potion.field_76436_u.field_76415_H, var2 * 20, 0));
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public void func_82163_bD() {}
}
