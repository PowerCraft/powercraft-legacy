package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Block;
import net.minecraft.src.BlockChest;
import net.minecraft.src.ModelChest;
import net.minecraft.src.ModelLargeChest;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityChestRenderer extends TileEntitySpecialRenderer {

   private ModelChest field_76913_a = new ModelChest();
   private ModelChest field_76912_c = new ModelLargeChest();


   public void func_76911_a(TileEntityChest p_76911_1_, double p_76911_2_, double p_76911_4_, double p_76911_6_, float p_76911_8_) {
      int var9;
      if(!p_76911_1_.func_70309_m()) {
         var9 = 0;
      } else {
         Block var10 = p_76911_1_.func_70311_o();
         var9 = p_76911_1_.func_70322_n();
         if(var10 != null && var9 == 0) {
            ((BlockChest)var10).func_72290_b_(p_76911_1_.func_70314_l(), p_76911_1_.field_70329_l, p_76911_1_.field_70330_m, p_76911_1_.field_70327_n);
            var9 = p_76911_1_.func_70322_n();
         }

         p_76911_1_.func_70418_i();
      }

      if(p_76911_1_.field_70423_b == null && p_76911_1_.field_70421_d == null) {
         ModelChest var14;
         if(p_76911_1_.field_70424_c == null && p_76911_1_.field_70422_e == null) {
            var14 = this.field_76913_a;
            this.func_76897_a("/item/chest.png");
         } else {
            var14 = this.field_76912_c;
            this.func_76897_a("/item/largechest.png");
         }

         GL11.glPushMatrix();
         GL11.glEnable('\u803a');
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glTranslatef((float)p_76911_2_, (float)p_76911_4_ + 1.0F, (float)p_76911_6_ + 1.0F);
         GL11.glScalef(1.0F, -1.0F, -1.0F);
         GL11.glTranslatef(0.5F, 0.5F, 0.5F);
         short var11 = 0;
         if(var9 == 2) {
            var11 = 180;
         }

         if(var9 == 3) {
            var11 = 0;
         }

         if(var9 == 4) {
            var11 = 90;
         }

         if(var9 == 5) {
            var11 = -90;
         }

         if(var9 == 2 && p_76911_1_.field_70424_c != null) {
            GL11.glTranslatef(1.0F, 0.0F, 0.0F);
         }

         if(var9 == 5 && p_76911_1_.field_70422_e != null) {
            GL11.glTranslatef(0.0F, 0.0F, -1.0F);
         }

         GL11.glRotatef((float)var11, 0.0F, 1.0F, 0.0F);
         GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
         float var12 = p_76911_1_.field_70420_g + (p_76911_1_.field_70419_f - p_76911_1_.field_70420_g) * p_76911_8_;
         float var13;
         if(p_76911_1_.field_70423_b != null) {
            var13 = p_76911_1_.field_70423_b.field_70420_g + (p_76911_1_.field_70423_b.field_70419_f - p_76911_1_.field_70423_b.field_70420_g) * p_76911_8_;
            if(var13 > var12) {
               var12 = var13;
            }
         }

         if(p_76911_1_.field_70421_d != null) {
            var13 = p_76911_1_.field_70421_d.field_70420_g + (p_76911_1_.field_70421_d.field_70419_f - p_76911_1_.field_70421_d.field_70420_g) * p_76911_8_;
            if(var13 > var12) {
               var12 = var13;
            }
         }

         var12 = 1.0F - var12;
         var12 = 1.0F - var12 * var12 * var12;
         var14.field_78234_a.field_78795_f = -(var12 * 3.1415927F / 2.0F);
         var14.func_78231_a();
         GL11.glDisable('\u803a');
         GL11.glPopMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void func_76894_a(TileEntity p_76894_1_, double p_76894_2_, double p_76894_4_, double p_76894_6_, float p_76894_8_) {
      this.func_76911_a((TileEntityChest)p_76894_1_, p_76894_2_, p_76894_4_, p_76894_6_, p_76894_8_);
   }
}
