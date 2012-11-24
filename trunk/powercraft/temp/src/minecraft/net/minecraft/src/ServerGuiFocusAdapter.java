package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import net.minecraft.src.ServerGUI;

@SideOnly(Side.SERVER)
class ServerGuiFocusAdapter extends FocusAdapter {

   // $FF: synthetic field
   final ServerGUI field_79028_a;


   ServerGuiFocusAdapter(ServerGUI p_i4107_1_) {
      this.field_79028_a = p_i4107_1_;
   }

   public void focusGained(FocusEvent p_focusGained_1_) {}
}
