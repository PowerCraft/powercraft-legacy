package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.ContainerDispenser;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.StatCollector;
import net.minecraft.src.TileEntityDispenser;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiDispenser extends GuiContainer {

   public GuiDispenser(InventoryPlayer p_i3091_1_, TileEntityDispenser p_i3091_2_) {
      super(new ContainerDispenser(p_i3091_1_, p_i3091_2_));
   }

   protected void func_74189_g(int p_74189_1_, int p_74189_2_) {
      this.field_73886_k.func_78276_b(StatCollector.func_74838_a("container.dispenser"), 60, 6, 4210752);
      this.field_73886_k.func_78276_b(StatCollector.func_74838_a("container.inventory"), 8, this.field_74195_c - 96 + 2, 4210752);
   }

   protected void func_74185_a(float p_74185_1_, int p_74185_2_, int p_74185_3_) {
      int var4 = this.field_73882_e.field_71446_o.func_78341_b("/gui/trap.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_73882_e.field_71446_o.func_78342_b(var4);
      int var5 = (this.field_73880_f - this.field_74194_b) / 2;
      int var6 = (this.field_73881_g - this.field_74195_c) / 2;
      this.func_73729_b(var5, var6, 0, 0, this.field_74194_b, this.field_74195_c);
   }
}
