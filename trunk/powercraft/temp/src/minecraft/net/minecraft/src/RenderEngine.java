package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.GameSettings;
import net.minecraft.src.IImageBuffer;
import net.minecraft.src.ITexturePack;
import net.minecraft.src.IntHashMap;
import net.minecraft.src.TextureFX;
import net.minecraft.src.TexturePackList;
import net.minecraft.src.ThreadDownloadImageData;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderEngine {

   private HashMap field_78362_c = new HashMap();
   private HashMap field_78359_d = new HashMap();
   private IntHashMap field_78360_e = new IntHashMap();
   private IntBuffer field_78357_f = GLAllocation.func_74527_f(1);
   private ByteBuffer field_78358_g = GLAllocation.func_74524_c(16777216);
   public List field_78367_h = new ArrayList();
   private Map field_78368_i = new HashMap();
   private GameSettings field_78365_j;
   public boolean field_78363_a = false;
   public boolean field_78361_b = false;
   public TexturePackList field_78366_k;
   private BufferedImage field_78364_l = new BufferedImage(64, 64, 2);


   public RenderEngine(TexturePackList p_i3192_1_, GameSettings p_i3192_2_) {
      this.field_78366_k = p_i3192_1_;
      this.field_78365_j = p_i3192_2_;
      Graphics var3 = this.field_78364_l.getGraphics();
      var3.setColor(Color.WHITE);
      var3.fillRect(0, 0, 64, 64);
      var3.setColor(Color.BLACK);
      var3.drawString("missingtex", 1, 10);
      var3.dispose();
   }

   public int[] func_78346_a(String p_78346_1_) {
      ITexturePack var2 = this.field_78366_k.func_77292_e();
      int[] var3 = (int[])this.field_78359_d.get(p_78346_1_);
      if(var3 != null) {
         return var3;
      } else {
         try {
            Object var4 = null;
            int[] var7;
            if(p_78346_1_.startsWith("##")) {
               var7 = this.func_78348_b(this.func_78354_c(this.func_78345_a(var2.func_77532_a(p_78346_1_.substring(2)))));
            } else if(p_78346_1_.startsWith("%clamp%")) {
               this.field_78363_a = true;
               var7 = this.func_78348_b(this.func_78345_a(var2.func_77532_a(p_78346_1_.substring(7))));
               this.field_78363_a = false;
            } else if(p_78346_1_.startsWith("%blur%")) {
               this.field_78361_b = true;
               this.field_78363_a = true;
               var7 = this.func_78348_b(this.func_78345_a(var2.func_77532_a(p_78346_1_.substring(6))));
               this.field_78363_a = false;
               this.field_78361_b = false;
            } else {
               InputStream var8 = var2.func_77532_a(p_78346_1_);
               if(var8 == null) {
                  var7 = this.func_78348_b(this.field_78364_l);
               } else {
                  var7 = this.func_78348_b(this.func_78345_a(var8));
               }
            }

            this.field_78359_d.put(p_78346_1_, var7);
            return var7;
         } catch (IOException var6) {
            var6.printStackTrace();
            int[] var5 = this.func_78348_b(this.field_78364_l);
            this.field_78359_d.put(p_78346_1_, var5);
            return var5;
         }
      }
   }

   private int[] func_78348_b(BufferedImage p_78348_1_) {
      int var2 = p_78348_1_.getWidth();
      int var3 = p_78348_1_.getHeight();
      int[] var4 = new int[var2 * var3];
      p_78348_1_.getRGB(0, 0, var2, var3, var4, 0, var2);
      return var4;
   }

   private int[] func_78340_a(BufferedImage p_78340_1_, int[] p_78340_2_) {
      int var3 = p_78340_1_.getWidth();
      int var4 = p_78340_1_.getHeight();
      p_78340_1_.getRGB(0, 0, var3, var4, p_78340_2_, 0, var3);
      return p_78340_2_;
   }

   public int func_78341_b(String p_78341_1_) {
      Integer var2 = (Integer)this.field_78362_c.get(p_78341_1_);
      if(var2 != null) {
         return var2.intValue();
      } else {
         ITexturePack var6 = this.field_78366_k.func_77292_e();

         try {
            this.field_78357_f.clear();
            GLAllocation.func_74528_a(this.field_78357_f);
            int var3 = this.field_78357_f.get(0);
            if(p_78341_1_.startsWith("##")) {
               this.func_78351_a(this.func_78354_c(this.func_78345_a(var6.func_77532_a(p_78341_1_.substring(2)))), var3);
            } else if(p_78341_1_.startsWith("%clamp%")) {
               this.field_78363_a = true;
               this.func_78351_a(this.func_78345_a(var6.func_77532_a(p_78341_1_.substring(7))), var3);
               this.field_78363_a = false;
            } else if(p_78341_1_.startsWith("%blur%")) {
               this.field_78361_b = true;
               this.func_78351_a(this.func_78345_a(var6.func_77532_a(p_78341_1_.substring(6))), var3);
               this.field_78361_b = false;
            } else if(p_78341_1_.startsWith("%blurclamp%")) {
               this.field_78361_b = true;
               this.field_78363_a = true;
               this.func_78351_a(this.func_78345_a(var6.func_77532_a(p_78341_1_.substring(11))), var3);
               this.field_78361_b = false;
               this.field_78363_a = false;
            } else {
               InputStream var7 = var6.func_77532_a(p_78341_1_);
               if(var7 == null) {
                  this.func_78351_a(this.field_78364_l, var3);
               } else {
                  this.func_78351_a(this.func_78345_a(var7), var3);
               }
            }

            this.field_78362_c.put(p_78341_1_, Integer.valueOf(var3));
            return var3;
         } catch (Exception var5) {
            var5.printStackTrace();
            GLAllocation.func_74528_a(this.field_78357_f);
            int var4 = this.field_78357_f.get(0);
            this.func_78351_a(this.field_78364_l, var4);
            this.field_78362_c.put(p_78341_1_, Integer.valueOf(var4));
            return var4;
         }
      }
   }

   private BufferedImage func_78354_c(BufferedImage p_78354_1_) {
      int var2 = p_78354_1_.getWidth() / 16;
      BufferedImage var3 = new BufferedImage(16, p_78354_1_.getHeight() * var2, 2);
      Graphics var4 = var3.getGraphics();

      for(int var5 = 0; var5 < var2; ++var5) {
         var4.drawImage(p_78354_1_, -var5 * 16, var5 * p_78354_1_.getHeight(), (ImageObserver)null);
      }

      var4.dispose();
      return var3;
   }

   public int func_78353_a(BufferedImage p_78353_1_) {
      this.field_78357_f.clear();
      GLAllocation.func_74528_a(this.field_78357_f);
      int var2 = this.field_78357_f.get(0);
      this.func_78351_a(p_78353_1_, var2);
      this.field_78360_e.func_76038_a(var2, p_78353_1_);
      return var2;
   }

   public void func_78351_a(BufferedImage p_78351_1_, int p_78351_2_) {
      GL11.glBindTexture(3553, p_78351_2_);
      GL11.glTexParameteri(3553, 10241, 9728);
      GL11.glTexParameteri(3553, 10240, 9728);
      if(this.field_78361_b) {
         GL11.glTexParameteri(3553, 10241, 9729);
         GL11.glTexParameteri(3553, 10240, 9729);
      }

      if(this.field_78363_a) {
         GL11.glTexParameteri(3553, 10242, 10496);
         GL11.glTexParameteri(3553, 10243, 10496);
      } else {
         GL11.glTexParameteri(3553, 10242, 10497);
         GL11.glTexParameteri(3553, 10243, 10497);
      }

      int var3 = p_78351_1_.getWidth();
      int var4 = p_78351_1_.getHeight();
      int[] var5 = new int[var3 * var4];
      byte[] var6 = new byte[var3 * var4 * 4];
      p_78351_1_.getRGB(0, 0, var3, var4, var5, 0, var3);

      for(int var7 = 0; var7 < var5.length; ++var7) {
         int var8 = var5[var7] >> 24 & 255;
         int var9 = var5[var7] >> 16 & 255;
         int var10 = var5[var7] >> 8 & 255;
         int var11 = var5[var7] & 255;
         if(this.field_78365_j != null && this.field_78365_j.field_74337_g) {
            int var12 = (var9 * 30 + var10 * 59 + var11 * 11) / 100;
            int var13 = (var9 * 30 + var10 * 70) / 100;
            int var14 = (var9 * 30 + var11 * 70) / 100;
            var9 = var12;
            var10 = var13;
            var11 = var14;
         }

         var6[var7 * 4 + 0] = (byte)var9;
         var6[var7 * 4 + 1] = (byte)var10;
         var6[var7 * 4 + 2] = (byte)var11;
         var6[var7 * 4 + 3] = (byte)var8;
      }

      this.field_78358_g.clear();
      this.field_78358_g.put(var6);
      this.field_78358_g.position(0).limit(var6.length);
      GL11.glTexImage2D(3553, 0, 6408, var3, var4, 0, 6408, 5121, this.field_78358_g);
   }

   public void func_78349_a(int[] p_78349_1_, int p_78349_2_, int p_78349_3_, int p_78349_4_) {
      GL11.glBindTexture(3553, p_78349_4_);
      GL11.glTexParameteri(3553, 10241, 9728);
      GL11.glTexParameteri(3553, 10240, 9728);
      if(this.field_78361_b) {
         GL11.glTexParameteri(3553, 10241, 9729);
         GL11.glTexParameteri(3553, 10240, 9729);
      }

      if(this.field_78363_a) {
         GL11.glTexParameteri(3553, 10242, 10496);
         GL11.glTexParameteri(3553, 10243, 10496);
      } else {
         GL11.glTexParameteri(3553, 10242, 10497);
         GL11.glTexParameteri(3553, 10243, 10497);
      }

      byte[] var5 = new byte[p_78349_2_ * p_78349_3_ * 4];

      for(int var6 = 0; var6 < p_78349_1_.length; ++var6) {
         int var7 = p_78349_1_[var6] >> 24 & 255;
         int var8 = p_78349_1_[var6] >> 16 & 255;
         int var9 = p_78349_1_[var6] >> 8 & 255;
         int var10 = p_78349_1_[var6] & 255;
         if(this.field_78365_j != null && this.field_78365_j.field_74337_g) {
            int var11 = (var8 * 30 + var9 * 59 + var10 * 11) / 100;
            int var12 = (var8 * 30 + var9 * 70) / 100;
            int var13 = (var8 * 30 + var10 * 70) / 100;
            var8 = var11;
            var9 = var12;
            var10 = var13;
         }

         var5[var6 * 4 + 0] = (byte)var8;
         var5[var6 * 4 + 1] = (byte)var9;
         var5[var6 * 4 + 2] = (byte)var10;
         var5[var6 * 4 + 3] = (byte)var7;
      }

      this.field_78358_g.clear();
      this.field_78358_g.put(var5);
      this.field_78358_g.position(0).limit(var5.length);
      GL11.glTexSubImage2D(3553, 0, 0, 0, p_78349_2_, p_78349_3_, 6408, 5121, this.field_78358_g);
   }

   public void func_78344_a(int p_78344_1_) {
      this.field_78360_e.func_76049_d(p_78344_1_);
      this.field_78357_f.clear();
      this.field_78357_f.put(p_78344_1_);
      this.field_78357_f.flip();
      GL11.glDeleteTextures(this.field_78357_f);
   }

   public int func_78350_a(String p_78350_1_, String p_78350_2_) {
      ThreadDownloadImageData var3 = (ThreadDownloadImageData)this.field_78368_i.get(p_78350_1_);
      if(var3 != null && var3.field_78462_a != null && !var3.field_78459_d) {
         if(var3.field_78461_c < 0) {
            var3.field_78461_c = this.func_78353_a(var3.field_78462_a);
         } else {
            this.func_78351_a(var3.field_78462_a, var3.field_78461_c);
         }

         var3.field_78459_d = true;
      }

      return var3 != null && var3.field_78461_c >= 0?var3.field_78461_c:(p_78350_2_ == null?-1:this.func_78341_b(p_78350_2_));
   }

   public boolean func_82773_c(String p_82773_1_) {
      return this.field_78368_i.containsKey(p_82773_1_);
   }

   public ThreadDownloadImageData func_78356_a(String p_78356_1_, IImageBuffer p_78356_2_) {
      ThreadDownloadImageData var3 = (ThreadDownloadImageData)this.field_78368_i.get(p_78356_1_);
      if(var3 == null) {
         this.field_78368_i.put(p_78356_1_, new ThreadDownloadImageData(p_78356_1_, p_78356_2_));
      } else {
         ++var3.field_78460_b;
      }

      return var3;
   }

   public void func_78347_c(String p_78347_1_) {
      ThreadDownloadImageData var2 = (ThreadDownloadImageData)this.field_78368_i.get(p_78347_1_);
      if(var2 != null) {
         --var2.field_78460_b;
         if(var2.field_78460_b == 0) {
            if(var2.field_78461_c >= 0) {
               this.func_78344_a(var2.field_78461_c);
            }

            this.field_78368_i.remove(p_78347_1_);
         }
      }

   }

   public void func_78355_a(TextureFX p_78355_1_) {
      this.field_78367_h.add(p_78355_1_);
      p_78355_1_.func_76846_a();
   }

   public void func_78343_a() {
      int var1 = -1;

      for(int var2 = 0; var2 < this.field_78367_h.size(); ++var2) {
         TextureFX var3 = (TextureFX)this.field_78367_h.get(var2);
         var3.field_76851_c = this.field_78365_j.field_74337_g;
         var3.func_76846_a();
         var1 = this.func_82772_a(var3, var1);
      }

   }

   public int func_82772_a(TextureFX p_82772_1_, int p_82772_2_) {
      this.field_78358_g.clear();
      this.field_78358_g.put(p_82772_1_.field_76852_a);
      this.field_78358_g.position(0).limit(p_82772_1_.field_76852_a.length);
      if(p_82772_1_.field_76850_b != p_82772_2_) {
         p_82772_1_.func_76845_a(this);
         p_82772_2_ = p_82772_1_.field_76850_b;
      }

      for(int var3 = 0; var3 < p_82772_1_.field_76849_e; ++var3) {
         for(int var4 = 0; var4 < p_82772_1_.field_76849_e; ++var4) {
            GL11.glTexSubImage2D(3553, 0, p_82772_1_.field_76850_b % 16 * 16 + var3 * 16, p_82772_1_.field_76850_b / 16 * 16 + var4 * 16, 16, 16, 6408, 5121, this.field_78358_g);
         }
      }

      return p_82772_2_;
   }

   public void func_78352_b() {
      ITexturePack var1 = this.field_78366_k.func_77292_e();
      Iterator var2 = this.field_78360_e.func_76039_d().iterator();

      BufferedImage var4;
      while(var2.hasNext()) {
         int var3 = ((Integer)var2.next()).intValue();
         var4 = (BufferedImage)this.field_78360_e.func_76041_a(var3);
         this.func_78351_a(var4, var3);
      }

      ThreadDownloadImageData var8;
      for(var2 = this.field_78368_i.values().iterator(); var2.hasNext(); var8.field_78459_d = false) {
         var8 = (ThreadDownloadImageData)var2.next();
      }

      var2 = this.field_78362_c.keySet().iterator();

      String var9;
      while(var2.hasNext()) {
         var9 = (String)var2.next();

         try {
            if(var9.startsWith("##")) {
               var4 = this.func_78354_c(this.func_78345_a(var1.func_77532_a(var9.substring(2))));
            } else if(var9.startsWith("%clamp%")) {
               this.field_78363_a = true;
               var4 = this.func_78345_a(var1.func_77532_a(var9.substring(7)));
            } else if(var9.startsWith("%blur%")) {
               this.field_78361_b = true;
               var4 = this.func_78345_a(var1.func_77532_a(var9.substring(6)));
            } else if(var9.startsWith("%blurclamp%")) {
               this.field_78361_b = true;
               this.field_78363_a = true;
               var4 = this.func_78345_a(var1.func_77532_a(var9.substring(11)));
            } else {
               var4 = this.func_78345_a(var1.func_77532_a(var9));
            }

            int var5 = ((Integer)this.field_78362_c.get(var9)).intValue();
            this.func_78351_a(var4, var5);
            this.field_78361_b = false;
            this.field_78363_a = false;
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      }

      var2 = this.field_78359_d.keySet().iterator();

      while(var2.hasNext()) {
         var9 = (String)var2.next();

         try {
            if(var9.startsWith("##")) {
               var4 = this.func_78354_c(this.func_78345_a(var1.func_77532_a(var9.substring(2))));
            } else if(var9.startsWith("%clamp%")) {
               this.field_78363_a = true;
               var4 = this.func_78345_a(var1.func_77532_a(var9.substring(7)));
            } else if(var9.startsWith("%blur%")) {
               this.field_78361_b = true;
               var4 = this.func_78345_a(var1.func_77532_a(var9.substring(6)));
            } else {
               var4 = this.func_78345_a(var1.func_77532_a(var9));
            }

            this.func_78340_a(var4, (int[])this.field_78359_d.get(var9));
            this.field_78361_b = false;
            this.field_78363_a = false;
         } catch (IOException var6) {
            var6.printStackTrace();
         }
      }

   }

   private BufferedImage func_78345_a(InputStream p_78345_1_) throws IOException {
      BufferedImage var2 = ImageIO.read(p_78345_1_);
      p_78345_1_.close();
      return var2;
   }

   public void func_78342_b(int p_78342_1_) {
      if(p_78342_1_ >= 0) {
         GL11.glBindTexture(3553, p_78342_1_);
      }
   }
}
