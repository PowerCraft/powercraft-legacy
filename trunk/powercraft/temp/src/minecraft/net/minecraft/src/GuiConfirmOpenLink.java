package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiYesNo;
import net.minecraft.src.StringTranslate;

@SideOnly(Side.CLIENT)
public abstract class GuiConfirmOpenLink extends GuiYesNo {

   private String field_73947_a;
   private String field_73946_b;


   public GuiConfirmOpenLink(GuiScreen p_i3102_1_, String p_i3102_2_, int p_i3102_3_) {
      super(p_i3102_1_, StringTranslate.func_74808_a().func_74805_b("chat.link.confirm"), p_i3102_2_, p_i3102_3_);
      StringTranslate var4 = StringTranslate.func_74808_a();
      this.field_73941_c = var4.func_74805_b("gui.yes");
      this.field_73939_d = var4.func_74805_b("gui.no");
      this.field_73946_b = var4.func_74805_b("chat.copy");
      this.field_73947_a = var4.func_74805_b("chat.link.warning");
   }

   public void func_73866_w_() {
      this.field_73887_h.add(new GuiButton(0, this.field_73880_f / 3 - 83 + 0, this.field_73881_g / 6 + 96, 100, 20, this.field_73941_c));
      this.field_73887_h.add(new GuiButton(2, this.field_73880_f / 3 - 83 + 105, this.field_73881_g / 6 + 96, 100, 20, this.field_73946_b));
      this.field_73887_h.add(new GuiButton(1, this.field_73880_f / 3 - 83 + 210, this.field_73881_g / 6 + 96, 100, 20, this.field_73939_d));
   }

   protected void func_73875_a(GuiButton p_73875_1_) {
      if(p_73875_1_.field_73741_f == 2) {
         this.func_73945_e();
         super.func_73875_a((GuiButton)this.field_73887_h.get(1));
      } else {
         super.func_73875_a(p_73875_1_);
      }

   }

   public abstract void func_73945_e();

   public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
      super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
      this.func_73732_a(this.field_73886_k, this.field_73947_a, this.field_73880_f / 2, 110, 16764108);
   }
}
