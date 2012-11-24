package argo.saj;

import argo.saj.InvalidSyntaxException;
import argo.saj.JsonListener;
import argo.saj.PositionTrackingPushbackReader;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

@SideOnly(Side.CLIENT)
public final class SajParser {

   public void func_74552_a(Reader p_74552_1_, JsonListener p_74552_2_) throws IOException, InvalidSyntaxException {
      PositionTrackingPushbackReader var3 = new PositionTrackingPushbackReader(p_74552_1_);
      char var4 = (char)var3.func_74564_a();
      switch(var4) {
      case 91:
         var3.func_74567_a(var4);
         p_74552_2_.func_74656_b();
         this.func_74559_a(var3, p_74552_2_);
         break;
      case 123:
         var3.func_74567_a(var4);
         p_74552_2_.func_74656_b();
         this.func_74555_b(var3, p_74552_2_);
         break;
      default:
         throw new InvalidSyntaxException("Expected either [ or { but got [" + var4 + "].", var3);
      }

      int var5 = this.func_74553_l(var3);
      if(var5 != -1) {
         throw new InvalidSyntaxException("Got unexpected trailing character [" + (char)var5 + "].", var3);
      } else {
         p_74552_2_.func_74657_c();
      }
   }

   private void func_74559_a(PositionTrackingPushbackReader p_74559_1_, JsonListener p_74559_2_) throws IOException, InvalidSyntaxException {
      char var3 = (char)this.func_74553_l(p_74559_1_);
      if(var3 != 91) {
         throw new InvalidSyntaxException("Expected object to start with [ but got [" + var3 + "].", p_74559_1_);
      } else {
         p_74559_2_.func_74652_d();
         char var4 = (char)this.func_74553_l(p_74559_1_);
         p_74559_1_.func_74567_a(var4);
         if(var4 != 93) {
            this.func_74545_d(p_74559_1_, p_74559_2_);
         }

         boolean var5 = false;

         while(!var5) {
            char var6 = (char)this.func_74553_l(p_74559_1_);
            switch(var6) {
            case 44:
               this.func_74545_d(p_74559_1_, p_74559_2_);
               break;
            case 93:
               var5 = true;
               break;
            default:
               throw new InvalidSyntaxException("Expected either , or ] but got [" + var6 + "].", p_74559_1_);
            }
         }

         p_74559_2_.func_74655_e();
      }
   }

   private void func_74555_b(PositionTrackingPushbackReader p_74555_1_, JsonListener p_74555_2_) throws IOException, InvalidSyntaxException {
      char var3 = (char)this.func_74553_l(p_74555_1_);
      if(var3 != 123) {
         throw new InvalidSyntaxException("Expected object to start with { but got [" + var3 + "].", p_74555_1_);
      } else {
         p_74555_2_.func_74651_f();
         char var4 = (char)this.func_74553_l(p_74555_1_);
         p_74555_1_.func_74567_a(var4);
         if(var4 != 125) {
            this.func_74558_c(p_74555_1_, p_74555_2_);
         }

         boolean var5 = false;

         while(!var5) {
            char var6 = (char)this.func_74553_l(p_74555_1_);
            switch(var6) {
            case 44:
               this.func_74558_c(p_74555_1_, p_74555_2_);
               break;
            case 125:
               var5 = true;
               break;
            default:
               throw new InvalidSyntaxException("Expected either , or } but got [" + var6 + "].", p_74555_1_);
            }
         }

         p_74555_2_.func_74653_g();
      }
   }

   private void func_74558_c(PositionTrackingPushbackReader p_74558_1_, JsonListener p_74558_2_) throws IOException, InvalidSyntaxException {
      char var3 = (char)this.func_74553_l(p_74558_1_);
      if(34 != var3) {
         throw new InvalidSyntaxException("Expected object identifier to begin with [\"] but got [" + var3 + "].", p_74558_1_);
      } else {
         p_74558_1_.func_74567_a(var3);
         p_74558_2_.func_74648_a(this.func_74548_i(p_74558_1_));
         char var4 = (char)this.func_74553_l(p_74558_1_);
         if(var4 != 58) {
            throw new InvalidSyntaxException("Expected object identifier to be followed by : but got [" + var4 + "].", p_74558_1_);
         } else {
            this.func_74545_d(p_74558_1_, p_74558_2_);
            p_74558_2_.func_74658_h();
         }
      }
   }

   private void func_74545_d(PositionTrackingPushbackReader p_74545_1_, JsonListener p_74545_2_) throws IOException, InvalidSyntaxException {
      char var3 = (char)this.func_74553_l(p_74545_1_);
      switch(var3) {
      case 34:
         p_74545_1_.func_74567_a(var3);
         p_74545_2_.func_74647_c(this.func_74548_i(p_74545_1_));
         break;
      case 45:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
         p_74545_1_.func_74567_a(var3);
         p_74545_2_.func_74650_b(this.func_74549_a(p_74545_1_));
         break;
      case 91:
         p_74545_1_.func_74567_a(var3);
         this.func_74559_a(p_74545_1_, p_74545_2_);
         break;
      case 102:
         char[] var6 = new char[4];
         int var7 = p_74545_1_.func_74565_b(var6);
         if(var7 != 4 || var6[0] != 97 || var6[1] != 108 || var6[2] != 115 || var6[3] != 101) {
            p_74545_1_.func_74566_a(var6);
            throw new InvalidSyntaxException("Expected \'f\' to be followed by [[a, l, s, e]], but got [" + Arrays.toString(var6) + "].", p_74545_1_);
         }

         p_74545_2_.func_74649_j();
         break;
      case 110:
         char[] var8 = new char[3];
         int var9 = p_74545_1_.func_74565_b(var8);
         if(var9 != 3 || var8[0] != 117 || var8[1] != 108 || var8[2] != 108) {
            p_74545_1_.func_74566_a(var8);
            throw new InvalidSyntaxException("Expected \'n\' to be followed by [[u, l, l]], but got [" + Arrays.toString(var8) + "].", p_74545_1_);
         }

         p_74545_2_.func_74646_k();
         break;
      case 116:
         char[] var4 = new char[3];
         int var5 = p_74545_1_.func_74565_b(var4);
         if(var5 != 3 || var4[0] != 114 || var4[1] != 117 || var4[2] != 101) {
            p_74545_1_.func_74566_a(var4);
            throw new InvalidSyntaxException("Expected \'t\' to be followed by [[r, u, e]], but got [" + Arrays.toString(var4) + "].", p_74545_1_);
         }

         p_74545_2_.func_74654_i();
         break;
      case 123:
         p_74545_1_.func_74567_a(var3);
         this.func_74555_b(p_74545_1_, p_74545_2_);
         break;
      default:
         throw new InvalidSyntaxException("Invalid character at start of value [" + var3 + "].", p_74545_1_);
      }

   }

   private String func_74549_a(PositionTrackingPushbackReader p_74549_1_) throws IOException, InvalidSyntaxException {
      StringBuilder var2 = new StringBuilder();
      char var3 = (char)p_74549_1_.func_74564_a();
      if(45 == var3) {
         var2.append('-');
      } else {
         p_74549_1_.func_74567_a(var3);
      }

      var2.append(this.func_74557_b(p_74549_1_));
      return var2.toString();
   }

   private String func_74557_b(PositionTrackingPushbackReader p_74557_1_) throws IOException, InvalidSyntaxException {
      StringBuilder var2 = new StringBuilder();
      char var3 = (char)p_74557_1_.func_74564_a();
      if(48 == var3) {
         var2.append('0');
         var2.append(this.func_74556_f(p_74557_1_));
         var2.append(this.func_74546_g(p_74557_1_));
      } else {
         p_74557_1_.func_74567_a(var3);
         var2.append(this.func_74560_c(p_74557_1_));
         var2.append(this.func_74551_e(p_74557_1_));
         var2.append(this.func_74556_f(p_74557_1_));
         var2.append(this.func_74546_g(p_74557_1_));
      }

      return var2.toString();
   }

   private char func_74560_c(PositionTrackingPushbackReader p_74560_1_) throws IOException, InvalidSyntaxException {
      char var3 = (char)p_74560_1_.func_74564_a();
      switch(var3) {
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
         return var3;
      default:
         throw new InvalidSyntaxException("Expected a digit 1 - 9 but got [" + var3 + "].", p_74560_1_);
      }
   }

   private char func_74554_d(PositionTrackingPushbackReader p_74554_1_) throws IOException, InvalidSyntaxException {
      char var3 = (char)p_74554_1_.func_74564_a();
      switch(var3) {
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
         return var3;
      default:
         throw new InvalidSyntaxException("Expected a digit 1 - 9 but got [" + var3 + "].", p_74554_1_);
      }
   }

   private String func_74551_e(PositionTrackingPushbackReader p_74551_1_) throws IOException {
      StringBuilder var2 = new StringBuilder();
      boolean var3 = false;

      while(!var3) {
         char var4 = (char)p_74551_1_.func_74564_a();
         switch(var4) {
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
            var2.append(var4);
            break;
         default:
            var3 = true;
            p_74551_1_.func_74567_a(var4);
         }
      }

      return var2.toString();
   }

   private String func_74556_f(PositionTrackingPushbackReader p_74556_1_) throws IOException, InvalidSyntaxException {
      StringBuilder var2 = new StringBuilder();
      char var3 = (char)p_74556_1_.func_74564_a();
      if(var3 == 46) {
         var2.append('.');
         var2.append(this.func_74554_d(p_74556_1_));
         var2.append(this.func_74551_e(p_74556_1_));
      } else {
         p_74556_1_.func_74567_a(var3);
      }

      return var2.toString();
   }

   private String func_74546_g(PositionTrackingPushbackReader p_74546_1_) throws IOException, InvalidSyntaxException {
      StringBuilder var2 = new StringBuilder();
      char var3 = (char)p_74546_1_.func_74564_a();
      if(var3 != 46 && var3 != 69) {
         p_74546_1_.func_74567_a(var3);
      } else {
         var2.append('E');
         var2.append(this.func_74547_h(p_74546_1_));
         var2.append(this.func_74554_d(p_74546_1_));
         var2.append(this.func_74551_e(p_74546_1_));
      }

      return var2.toString();
   }

   private String func_74547_h(PositionTrackingPushbackReader p_74547_1_) throws IOException {
      StringBuilder var2 = new StringBuilder();
      char var3 = (char)p_74547_1_.func_74564_a();
      if(var3 != 43 && var3 != 45) {
         p_74547_1_.func_74567_a(var3);
      } else {
         var2.append(var3);
      }

      return var2.toString();
   }

   private String func_74548_i(PositionTrackingPushbackReader p_74548_1_) throws IOException, InvalidSyntaxException {
      StringBuilder var2 = new StringBuilder();
      char var3 = (char)p_74548_1_.func_74564_a();
      if(34 != var3) {
         throw new InvalidSyntaxException("Expected [\"] but got [" + var3 + "].", p_74548_1_);
      } else {
         boolean var4 = false;

         while(!var4) {
            char var5 = (char)p_74548_1_.func_74564_a();
            switch(var5) {
            case 34:
               var4 = true;
               break;
            case 92:
               char var6 = this.func_74544_j(p_74548_1_);
               var2.append(var6);
               break;
            default:
               var2.append(var5);
            }
         }

         return var2.toString();
      }
   }

   private char func_74544_j(PositionTrackingPushbackReader p_74544_1_) throws IOException, InvalidSyntaxException {
      char var3 = (char)p_74544_1_.func_74564_a();
      char var2;
      switch(var3) {
      case 34:
         var2 = 34;
         break;
      case 47:
         var2 = 47;
         break;
      case 92:
         var2 = 92;
         break;
      case 98:
         var2 = 8;
         break;
      case 102:
         var2 = 12;
         break;
      case 110:
         var2 = 10;
         break;
      case 114:
         var2 = 13;
         break;
      case 116:
         var2 = 9;
         break;
      case 117:
         var2 = (char)this.func_74550_k(p_74544_1_);
         break;
      default:
         throw new InvalidSyntaxException("Unrecognised escape character [" + var3 + "].", p_74544_1_);
      }

      return var2;
   }

   private int func_74550_k(PositionTrackingPushbackReader p_74550_1_) throws IOException, InvalidSyntaxException {
      char[] var2 = new char[4];
      int var3 = p_74550_1_.func_74565_b(var2);
      if(var3 != 4) {
         throw new InvalidSyntaxException("Expected a 4 digit hexidecimal number but got only [" + var3 + "], namely [" + String.valueOf(var2, 0, var3) + "].", p_74550_1_);
      } else {
         try {
            int var4 = Integer.parseInt(String.valueOf(var2), 16);
            return var4;
         } catch (NumberFormatException var6) {
            p_74550_1_.func_74566_a(var2);
            throw new InvalidSyntaxException("Unable to parse [" + String.valueOf(var2) + "] as a hexidecimal number.", var6, p_74550_1_);
         }
      }
   }

   private int func_74553_l(PositionTrackingPushbackReader p_74553_1_) throws IOException {
      boolean var3 = false;

      int var2;
      do {
         var2 = p_74553_1_.func_74564_a();
         switch(var2) {
         case 9:
         case 10:
         case 13:
         case 32:
            break;
         default:
            var3 = true;
         }
      } while(!var3);

      return var2;
   }
}
