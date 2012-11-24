package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import net.minecraft.src.GuiLogOutputHandler;

@SideOnly(Side.SERVER)
class GuiLogFormatter extends Formatter {

   // $FF: synthetic field
   final GuiLogOutputHandler field_79029_a;


   GuiLogFormatter(GuiLogOutputHandler p_i4101_1_) {
      this.field_79029_a = p_i4101_1_;
   }

   public String format(LogRecord p_format_1_) {
      StringBuilder var2 = new StringBuilder();
      Level var3 = p_format_1_.getLevel();
      if(var3 == Level.FINEST) {
         var2.append("[FINEST] ");
      } else if(var3 == Level.FINER) {
         var2.append("[FINER] ");
      } else if(var3 == Level.FINE) {
         var2.append("[FINE] ");
      } else if(var3 == Level.INFO) {
         var2.append("[INFO] ");
      } else if(var3 == Level.WARNING) {
         var2.append("[WARNING] ");
      } else if(var3 == Level.SEVERE) {
         var2.append("[SEVERE] ");
      } else if(var3 == Level.SEVERE) {
         var2.append("[").append(var3.getLocalizedName()).append("] ");
      }

      var2.append(p_format_1_.getMessage());
      var2.append('\n');
      Throwable var4 = p_format_1_.getThrown();
      if(var4 != null) {
         StringWriter var5 = new StringWriter();
         var4.printStackTrace(new PrintWriter(var5));
         var2.append(var5.toString());
      }

      return var2.toString();
   }
}
