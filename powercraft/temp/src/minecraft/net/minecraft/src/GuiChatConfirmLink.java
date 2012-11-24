package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.ChatClickData;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiConfirmOpenLink;
import net.minecraft.src.GuiScreen;

@SideOnly(Side.CLIENT)
class GuiChatConfirmLink extends GuiConfirmOpenLink {

   // $FF: synthetic field
   final ChatClickData field_73949_a;
   // $FF: synthetic field
   final GuiChat field_73948_b;


   GuiChatConfirmLink(GuiChat p_i3045_1_, GuiScreen p_i3045_2_, String p_i3045_3_, int p_i3045_4_, ChatClickData p_i3045_5_) {
      super(p_i3045_2_, p_i3045_3_, p_i3045_4_);
      this.field_73948_b = p_i3045_1_;
      this.field_73949_a = p_i3045_5_;
   }

   public void func_73945_e() {
      func_73865_d(this.field_73949_a.func_78309_f());
   }
}
