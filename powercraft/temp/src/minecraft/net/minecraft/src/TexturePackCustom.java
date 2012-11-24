package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.TexturePackImplementation;

@SideOnly(Side.CLIENT)
public class TexturePackCustom extends TexturePackImplementation {

   private ZipFile field_77550_e;


   public TexturePackCustom(String p_i3031_1_, File p_i3031_2_) {
      super(p_i3031_1_, p_i3031_2_, p_i3031_2_.getName());
   }

   public void func_77533_a(RenderEngine p_77533_1_) {
      super.func_77533_a(p_77533_1_);

      try {
         if(this.field_77550_e != null) {
            this.field_77550_e.close();
         }
      } catch (IOException var3) {
         ;
      }

      this.field_77550_e = null;
   }

   public InputStream func_77532_a(String p_77532_1_) {
      this.func_77549_g();

      try {
         ZipEntry var2 = this.field_77550_e.getEntry(p_77532_1_.substring(1));
         if(var2 != null) {
            return this.field_77550_e.getInputStream(var2);
         }
      } catch (Exception var3) {
         ;
      }

      return super.func_77532_a(p_77532_1_);
   }

   private void func_77549_g() {
      if(this.field_77550_e == null) {
         try {
            this.field_77550_e = new ZipFile(this.field_77548_a);
         } catch (IOException var2) {
            ;
         }

      }
   }
}
