package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.ContainerFurnace;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.StatCollector;
import net.minecraft.src.TileEntityFurnace;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiFurnace extends GuiContainer {

   private TileEntityFurnace field_74204_o;


   public GuiFurnace(InventoryPlayer p_i3094_1_, TileEntityFurnace p_i3094_2_) {
      super(new ContainerFurnace(p_i3094_1_, p_i3094_2_));
      this.field_74204_o = p_i3094_2_;
   }

   protected void func_74189_g(int p_74189_1_, int p_74189_2_) {
      this.field_73886_k.func_78276_b(StatCollector.func_74838_a("container.furnace"), 60, 6, 4210752);
      this.field_73886_k.func_78276_b(StatCollector.func_74838_a("container.inventory"), 8, this.field_74195_c - 96 + 2, 4210752);
   }

   protected void func_74185_a(float p_74185_1_, int p_74185_2_, int p_74185_3_) {
      int var4 = this.field_73882_e.field_71446_o.func_78341_b("/gui/furnace.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_73882_e.field_71446_o.func_78342_b(var4);
      int var5 = (this.field_73880_f - this.field_74194_b) / 2;
      int var6 = (this.field_73881_g - this.field_74195_c) / 2;
      this.func_73729_b(var5, var6, 0, 0, this.field_74194_b, this.field_74195_c);
      int var7;
      if(this.field_74204_o.func_70400_i()) {
         var7 = this.field_74204_o.func_70403_d(12);
         this.func_73729_b(var5 + 56, var6 + 36 + 12 - var7, 176, 12 - var7, 14, var7 + 2);
      }

      var7 = this.field_74204_o.func_70397_c(24);
      this.func_73729_b(var5 + 79, var6 + 34, 176, 14, var7 + 1, 16);
   }
}
