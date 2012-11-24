package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.MinecraftError;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LoadingScreenRenderer implements IProgressUpdate {

   private String field_73727_a = "";
   private Minecraft field_73725_b;
   private String field_73726_c = "";
   private long field_73723_d = Minecraft.func_71386_F();
   private boolean field_73724_e = false;


   public LoadingScreenRenderer(Minecraft p_i3004_1_) {
      this.field_73725_b = p_i3004_1_;
   }

   public void func_73721_b(String p_73721_1_) {
      this.field_73724_e = false;
      this.func_73722_d(p_73721_1_);
   }

   public void func_73720_a(String p_73720_1_) {
      this.field_73724_e = true;
      this.func_73722_d(p_73720_1_);
   }

   public void func_73722_d(String p_73722_1_) {
      this.field_73726_c = p_73722_1_;
      if(!this.field_73725_b.field_71425_J) {
         if(!this.field_73724_e) {
            throw new MinecraftError();
         }
      } else {
         ScaledResolution var2 = new ScaledResolution(this.field_73725_b.field_71474_y, this.field_73725_b.field_71443_c, this.field_73725_b.field_71440_d);
         GL11.glClear(256);
         GL11.glMatrixMode(5889);
         GL11.glLoadIdentity();
         GL11.glOrtho(0.0D, var2.func_78327_c(), var2.func_78324_d(), 0.0D, 100.0D, 300.0D);
         GL11.glMatrixMode(5888);
         GL11.glLoadIdentity();
         GL11.glTranslatef(0.0F, 0.0F, -200.0F);
      }
   }

   public void func_73719_c(String p_73719_1_) {
      if(!this.field_73725_b.field_71425_J) {
         if(!this.field_73724_e) {
            throw new MinecraftError();
         }
      } else {
         this.field_73723_d = 0L;
         this.field_73727_a = p_73719_1_;
         this.func_73718_a(-1);
         this.field_73723_d = 0L;
      }
   }

   public void func_73718_a(int p_73718_1_) {
      if(!this.field_73725_b.field_71425_J) {
         if(!this.field_73724_e) {
            throw new MinecraftError();
         }
      } else {
         long var2 = Minecraft.func_71386_F();
         if(var2 - this.field_73723_d >= 100L) {
            this.field_73723_d = var2;
            ScaledResolution var4 = new ScaledResolution(this.field_73725_b.field_71474_y, this.field_73725_b.field_71443_c, this.field_73725_b.field_71440_d);
            int var5 = var4.func_78326_a();
            int var6 = var4.func_78328_b();
            GL11.glClear(256);
            GL11.glMatrixMode(5889);
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0D, var4.func_78327_c(), var4.func_78324_d(), 0.0D, 100.0D, 300.0D);
            GL11.glMatrixMode(5888);
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, 0.0F, -200.0F);
            GL11.glClear(16640);
            Tessellator var7 = Tessellator.field_78398_a;
            int var8 = this.field_73725_b.field_71446_o.func_78341_b("/gui/background.png");
            GL11.glBindTexture(3553, var8);
            float var9 = 32.0F;
            var7.func_78382_b();
            var7.func_78378_d(4210752);
            var7.func_78374_a(0.0D, (double)var6, 0.0D, 0.0D, (double)((float)var6 / var9));
            var7.func_78374_a((double)var5, (double)var6, 0.0D, (double)((float)var5 / var9), (double)((float)var6 / var9));
            var7.func_78374_a((double)var5, 0.0D, 0.0D, (double)((float)var5 / var9), 0.0D);
            var7.func_78374_a(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
            var7.func_78381_a();
            if(p_73718_1_ >= 0) {
               byte var10 = 100;
               byte var11 = 2;
               int var12 = var5 / 2 - var10 / 2;
               int var13 = var6 / 2 + 16;
               GL11.glDisable(3553);
               var7.func_78382_b();
               var7.func_78378_d(8421504);
               var7.func_78377_a((double)var12, (double)var13, 0.0D);
               var7.func_78377_a((double)var12, (double)(var13 + var11), 0.0D);
               var7.func_78377_a((double)(var12 + var10), (double)(var13 + var11), 0.0D);
               var7.func_78377_a((double)(var12 + var10), (double)var13, 0.0D);
               var7.func_78378_d(8454016);
               var7.func_78377_a((double)var12, (double)var13, 0.0D);
               var7.func_78377_a((double)var12, (double)(var13 + var11), 0.0D);
               var7.func_78377_a((double)(var12 + p_73718_1_), (double)(var13 + var11), 0.0D);
               var7.func_78377_a((double)(var12 + p_73718_1_), (double)var13, 0.0D);
               var7.func_78381_a();
               GL11.glEnable(3553);
            }

            this.field_73725_b.field_71466_p.func_78261_a(this.field_73726_c, (var5 - this.field_73725_b.field_71466_p.func_78256_a(this.field_73726_c)) / 2, var6 / 2 - 4 - 16, 16777215);
            this.field_73725_b.field_71466_p.func_78261_a(this.field_73727_a, (var5 - this.field_73725_b.field_71466_p.func_78256_a(this.field_73727_a)) / 2, var6 / 2 - 4 + 8, 16777215);
            Display.update();

            try {
               Thread.yield();
            } catch (Exception var14) {
               ;
            }

         }
      }
   }

   public void func_73717_a() {}
}
