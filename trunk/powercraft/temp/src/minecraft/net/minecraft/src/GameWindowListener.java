package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SideOnly(Side.CLIENT)
public final class GameWindowListener extends WindowAdapter {

   public void windowClosing(WindowEvent p_windowClosing_1_) {
      System.err.println("Someone is closing me!");
      System.exit(1);
   }
}
