package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import net.minecraft.src.ITexturePack;
import net.minecraft.src.RenderEngine;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public abstract class TexturePackImplementation implements ITexturePack {

   private final String field_77545_e;
   private final String field_77542_f;
   protected final File field_77548_a;
   protected String field_77546_b;
   protected String field_77547_c;
   protected BufferedImage field_77544_d;
   private int field_77543_g;


   protected TexturePackImplementation(String p_i3027_1_, String p_i3027_2_) {
      this(p_i3027_1_, (File)null, p_i3027_2_);
   }

   protected TexturePackImplementation(String p_i3028_1_, File p_i3028_2_, String p_i3028_3_) {
      this.field_77543_g = -1;
      this.field_77545_e = p_i3028_1_;
      this.field_77542_f = p_i3028_3_;
      this.field_77548_a = p_i3028_2_;
      this.func_77539_g();
      this.func_77540_a();
   }

   private static String func_77541_b(String p_77541_0_) {
      if(p_77541_0_ != null && p_77541_0_.length() > 34) {
         p_77541_0_ = p_77541_0_.substring(0, 34);
      }

      return p_77541_0_;
   }

   private void func_77539_g() {
      InputStream var1 = null;

      try {
         var1 = this.func_77532_a("/pack.png");
         this.field_77544_d = ImageIO.read(var1);
      } catch (IOException var11) {
         ;
      } finally {
         try {
            var1.close();
         } catch (IOException var10) {
            ;
         }

      }

   }

   protected void func_77540_a() {
      InputStream var1 = null;
      BufferedReader var2 = null;

      try {
         var1 = this.func_77532_a("/pack.txt");
         var2 = new BufferedReader(new InputStreamReader(var1));
         this.field_77546_b = func_77541_b(var2.readLine());
         this.field_77547_c = func_77541_b(var2.readLine());
      } catch (IOException var12) {
         ;
      } finally {
         try {
            var2.close();
            var1.close();
         } catch (IOException var11) {
            ;
         }

      }

   }

   public void func_77533_a(RenderEngine p_77533_1_) {
      if(this.field_77544_d != null && this.field_77543_g != -1) {
         p_77533_1_.func_78344_a(this.field_77543_g);
      }

   }

   public void func_77535_b(RenderEngine p_77535_1_) {
      if(this.field_77544_d != null) {
         if(this.field_77543_g == -1) {
            this.field_77543_g = p_77535_1_.func_78353_a(this.field_77544_d);
         }

         p_77535_1_.func_78342_b(this.field_77543_g);
      } else {
         GL11.glBindTexture(3553, p_77535_1_.func_78341_b("/gui/unknown_pack.png"));
      }

   }

   public InputStream func_77532_a(String p_77532_1_) {
      return ITexturePack.class.getResourceAsStream(p_77532_1_);
   }

   public String func_77536_b() {
      return this.field_77545_e;
   }

   public String func_77538_c() {
      return this.field_77542_f;
   }

   public String func_77531_d() {
      return this.field_77546_b;
   }

   public String func_77537_e() {
      return this.field_77547_c;
   }

   public int func_77534_f() {
      return 16;
   }
}
