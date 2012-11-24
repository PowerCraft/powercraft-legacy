package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.ContainerBrewingStand;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.StatCollector;
import net.minecraft.src.TileEntityBrewingStand;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiBrewingStand extends GuiContainer {

   private TileEntityBrewingStand field_74217_o;


   public GuiBrewingStand(InventoryPlayer p_i3092_1_, TileEntityBrewingStand p_i3092_2_) {
      super(new ContainerBrewingStand(p_i3092_1_, p_i3092_2_));
      this.field_74217_o = p_i3092_2_;
   }

   protected void func_74189_g(int p_74189_1_, int p_74189_2_) {
      this.field_73886_k.func_78276_b(StatCollector.func_74838_a("container.brewing"), 56, 6, 4210752);
      this.field_73886_k.func_78276_b(StatCollector.func_74838_a("container.inventory"), 8, this.field_74195_c - 96 + 2, 4210752);
   }

   protected void func_74185_a(float p_74185_1_, int p_74185_2_, int p_74185_3_) {
      int var4 = this.field_73882_e.field_71446_o.func_78341_b("/gui/alchemy.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_73882_e.field_71446_o.func_78342_b(var4);
      int var5 = (this.field_73880_f - this.field_74194_b) / 2;
      int var6 = (this.field_73881_g - this.field_74195_c) / 2;
      this.func_73729_b(var5, var6, 0, 0, this.field_74194_b, this.field_74195_c);
      int var7 = this.field_74217_o.func_70355_t_();
      if(var7 > 0) {
         int var8 = (int)(28.0F * (1.0F - (float)var7 / 400.0F));
         if(var8 > 0) {
            this.func_73729_b(var5 + 97, var6 + 16, 176, 0, 9, var8);
         }

         int var9 = var7 / 2 % 7;
         switch(var9) {
         case 0:
            var8 = 29;
            break;
         case 1:
            var8 = 24;
            break;
         case 2:
            var8 = 20;
            break;
         case 3:
            var8 = 16;
            break;
         case 4:
            var8 = 11;
            break;
         case 5:
            var8 = 6;
            break;
         case 6:
            var8 = 0;
         }

         if(var8 > 0) {
            this.func_73729_b(var5 + 65, var6 + 14 + 29 - var8, 185, 29 - var8, 12, var8);
         }
      }

   }
}
