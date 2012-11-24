package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiButtonNextPage;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NBTTagString;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.StatCollector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiScreenBook extends GuiScreen {

   private final EntityPlayer field_74169_a;
   private final ItemStack field_74167_b;
   private final boolean field_74168_c;
   private boolean field_74166_d;
   private boolean field_74172_m;
   private int field_74170_n;
   private int field_74171_o = 192;
   private int field_74180_p = 192;
   private int field_74179_q = 1;
   private int field_74178_r;
   private NBTTagList field_74177_s;
   private String field_74176_t = "";
   private GuiButtonNextPage field_74175_u;
   private GuiButtonNextPage field_74174_v;
   private GuiButton field_74173_w;
   private GuiButton field_74183_x;
   private GuiButton field_74182_y;
   private GuiButton field_74181_z;


   public GuiScreenBook(EntityPlayer p_i3085_1_, ItemStack p_i3085_2_, boolean p_i3085_3_) {
      this.field_74169_a = p_i3085_1_;
      this.field_74167_b = p_i3085_2_;
      this.field_74168_c = p_i3085_3_;
      if(p_i3085_2_.func_77942_o()) {
         NBTTagCompound var4 = p_i3085_2_.func_77978_p();
         this.field_74177_s = var4.func_74761_m("pages");
         if(this.field_74177_s != null) {
            this.field_74177_s = (NBTTagList)this.field_74177_s.func_74737_b();
            this.field_74179_q = this.field_74177_s.func_74745_c();
            if(this.field_74179_q < 1) {
               this.field_74179_q = 1;
            }
         }
      }

      if(this.field_74177_s == null && p_i3085_3_) {
         this.field_74177_s = new NBTTagList("pages");
         this.field_74177_s.func_74742_a(new NBTTagString("1", ""));
         this.field_74179_q = 1;
      }

   }

   public void func_73876_c() {
      super.func_73876_c();
      ++this.field_74170_n;
   }

   public void func_73866_w_() {
      this.field_73887_h.clear();
      Keyboard.enableRepeatEvents(true);
      if(this.field_74168_c) {
         this.field_73887_h.add(this.field_74183_x = new GuiButton(3, this.field_73880_f / 2 - 100, 4 + this.field_74180_p, 98, 20, StatCollector.func_74838_a("book.signButton")));
         this.field_73887_h.add(this.field_74173_w = new GuiButton(0, this.field_73880_f / 2 + 2, 4 + this.field_74180_p, 98, 20, StatCollector.func_74838_a("gui.done")));
         this.field_73887_h.add(this.field_74182_y = new GuiButton(5, this.field_73880_f / 2 - 100, 4 + this.field_74180_p, 98, 20, StatCollector.func_74838_a("book.finalizeButton")));
         this.field_73887_h.add(this.field_74181_z = new GuiButton(4, this.field_73880_f / 2 + 2, 4 + this.field_74180_p, 98, 20, StatCollector.func_74838_a("gui.cancel")));
      } else {
         this.field_73887_h.add(this.field_74173_w = new GuiButton(0, this.field_73880_f / 2 - 100, 4 + this.field_74180_p, 200, 20, StatCollector.func_74838_a("gui.done")));
      }

      int var1 = (this.field_73880_f - this.field_74171_o) / 2;
      byte var2 = 2;
      this.field_73887_h.add(this.field_74175_u = new GuiButtonNextPage(1, var1 + 120, var2 + 154, true));
      this.field_73887_h.add(this.field_74174_v = new GuiButtonNextPage(2, var1 + 38, var2 + 154, false));
      this.func_74161_g();
   }

   public void func_73874_b() {
      Keyboard.enableRepeatEvents(false);
   }

   private void func_74161_g() {
      this.field_74175_u.field_73748_h = !this.field_74172_m && (this.field_74178_r < this.field_74179_q - 1 || this.field_74168_c);
      this.field_74174_v.field_73748_h = !this.field_74172_m && this.field_74178_r > 0;
      this.field_74173_w.field_73748_h = !this.field_74168_c || !this.field_74172_m;
      if(this.field_74168_c) {
         this.field_74183_x.field_73748_h = !this.field_74172_m;
         this.field_74181_z.field_73748_h = this.field_74172_m;
         this.field_74182_y.field_73748_h = this.field_74172_m;
         this.field_74182_y.field_73742_g = this.field_74176_t.trim().length() > 0;
      }

   }

   private void func_74163_a(boolean p_74163_1_) {
      if(this.field_74168_c && this.field_74166_d) {
         if(this.field_74177_s != null) {
            while(this.field_74177_s.func_74745_c() > 1) {
               NBTTagString var2 = (NBTTagString)this.field_74177_s.func_74743_b(this.field_74177_s.func_74745_c() - 1);
               if(var2.field_74751_a != null && var2.field_74751_a.length() != 0) {
                  break;
               }

               this.field_74177_s.func_74744_a(this.field_74177_s.func_74745_c() - 1);
            }

            if(this.field_74167_b.func_77942_o()) {
               NBTTagCompound var7 = this.field_74167_b.func_77978_p();
               var7.func_74782_a("pages", this.field_74177_s);
            } else {
               this.field_74167_b.func_77983_a("pages", this.field_74177_s);
            }

            String var8 = "MC|BEdit";
            if(p_74163_1_) {
               var8 = "MC|BSign";
               this.field_74167_b.func_77983_a("author", new NBTTagString("author", this.field_74169_a.field_71092_bJ));
               this.field_74167_b.func_77983_a("title", new NBTTagString("title", this.field_74176_t.trim()));
               this.field_74167_b.field_77993_c = Item.field_77823_bG.field_77779_bT;
            }

            ByteArrayOutputStream var3 = new ByteArrayOutputStream();
            DataOutputStream var4 = new DataOutputStream(var3);

            try {
               Packet.func_73270_a(this.field_74167_b, var4);
               this.field_73882_e.func_71391_r().func_72552_c(new Packet250CustomPayload(var8, var3.toByteArray()));
            } catch (Exception var6) {
               var6.printStackTrace();
            }
         }

      }
   }

   protected void func_73875_a(GuiButton p_73875_1_) {
      if(p_73875_1_.field_73742_g) {
         if(p_73875_1_.field_73741_f == 0) {
            this.field_73882_e.func_71373_a((GuiScreen)null);
            this.func_74163_a(false);
         } else if(p_73875_1_.field_73741_f == 3 && this.field_74168_c) {
            this.field_74172_m = true;
         } else if(p_73875_1_.field_73741_f == 1) {
            if(this.field_74178_r < this.field_74179_q - 1) {
               ++this.field_74178_r;
            } else if(this.field_74168_c) {
               this.func_74165_h();
               if(this.field_74178_r < this.field_74179_q - 1) {
                  ++this.field_74178_r;
               }
            }
         } else if(p_73875_1_.field_73741_f == 2) {
            if(this.field_74178_r > 0) {
               --this.field_74178_r;
            }
         } else if(p_73875_1_.field_73741_f == 5 && this.field_74172_m) {
            this.func_74163_a(true);
            this.field_73882_e.func_71373_a((GuiScreen)null);
         } else if(p_73875_1_.field_73741_f == 4 && this.field_74172_m) {
            this.field_74172_m = false;
         }

         this.func_74161_g();
      }
   }

   private void func_74165_h() {
      if(this.field_74177_s != null && this.field_74177_s.func_74745_c() < 50) {
         this.field_74177_s.func_74742_a(new NBTTagString("" + (this.field_74179_q + 1), ""));
         ++this.field_74179_q;
         this.field_74166_d = true;
      }
   }

   protected void func_73869_a(char p_73869_1_, int p_73869_2_) {
      super.func_73869_a(p_73869_1_, p_73869_2_);
      if(this.field_74168_c) {
         if(this.field_74172_m) {
            this.func_74162_c(p_73869_1_, p_73869_2_);
         } else {
            this.func_74164_b(p_73869_1_, p_73869_2_);
         }

      }
   }

   private void func_74164_b(char p_74164_1_, int p_74164_2_) {
      switch(p_74164_1_) {
      case 22:
         this.func_74160_b(GuiScreen.func_73870_l());
         return;
      default:
         switch(p_74164_2_) {
         case 14:
            String var3 = this.func_74158_i();
            if(var3.length() > 0) {
               this.func_74159_a(var3.substring(0, var3.length() - 1));
            }

            return;
         case 28:
            this.func_74160_b("\n");
            return;
         default:
            if(ChatAllowedCharacters.func_71566_a(p_74164_1_)) {
               this.func_74160_b(Character.toString(p_74164_1_));
            }
         }
      }
   }

   private void func_74162_c(char p_74162_1_, int p_74162_2_) {
      switch(p_74162_2_) {
      case 14:
         if(this.field_74176_t.length() > 0) {
            this.field_74176_t = this.field_74176_t.substring(0, this.field_74176_t.length() - 1);
            this.func_74161_g();
         }

         return;
      case 28:
         if(this.field_74176_t.length() > 0) {
            this.func_74163_a(true);
            this.field_73882_e.func_71373_a((GuiScreen)null);
         }

         return;
      default:
         if(this.field_74176_t.length() < 16 && ChatAllowedCharacters.func_71566_a(p_74162_1_)) {
            this.field_74176_t = this.field_74176_t + Character.toString(p_74162_1_);
            this.func_74161_g();
            this.field_74166_d = true;
         }
      }
   }

   private String func_74158_i() {
      if(this.field_74177_s != null && this.field_74178_r >= 0 && this.field_74178_r < this.field_74177_s.func_74745_c()) {
         NBTTagString var1 = (NBTTagString)this.field_74177_s.func_74743_b(this.field_74178_r);
         return var1.toString();
      } else {
         return "";
      }
   }

   private void func_74159_a(String p_74159_1_) {
      if(this.field_74177_s != null && this.field_74178_r >= 0 && this.field_74178_r < this.field_74177_s.func_74745_c()) {
         NBTTagString var2 = (NBTTagString)this.field_74177_s.func_74743_b(this.field_74178_r);
         var2.field_74751_a = p_74159_1_;
         this.field_74166_d = true;
      }

   }

   private void func_74160_b(String p_74160_1_) {
      String var2 = this.func_74158_i();
      String var3 = var2 + p_74160_1_;
      int var4 = this.field_73886_k.func_78267_b(var3 + "\u00a70_", 118);
      if(var4 <= 118 && var3.length() < 256) {
         this.func_74159_a(var3);
      }

   }

   public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
      int var4 = this.field_73882_e.field_71446_o.func_78341_b("/gui/book.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_73882_e.field_71446_o.func_78342_b(var4);
      int var5 = (this.field_73880_f - this.field_74171_o) / 2;
      byte var6 = 2;
      this.func_73729_b(var5, var6, 0, 0, this.field_74171_o, this.field_74180_p);
      String var7;
      String var8;
      int var9;
      if(this.field_74172_m) {
         var7 = this.field_74176_t;
         if(this.field_74168_c) {
            if(this.field_74170_n / 6 % 2 == 0) {
               var7 = var7 + "\u00a70_";
            } else {
               var7 = var7 + "\u00a77_";
            }
         }

         var8 = StatCollector.func_74838_a("book.editTitle");
         var9 = this.field_73886_k.func_78256_a(var8);
         this.field_73886_k.func_78276_b(var8, var5 + 36 + (116 - var9) / 2, var6 + 16 + 16, 0);
         int var10 = this.field_73886_k.func_78256_a(var7);
         this.field_73886_k.func_78276_b(var7, var5 + 36 + (116 - var10) / 2, var6 + 48, 0);
         String var11 = String.format(StatCollector.func_74838_a("book.byAuthor"), new Object[]{this.field_74169_a.field_71092_bJ});
         int var12 = this.field_73886_k.func_78256_a(var11);
         this.field_73886_k.func_78276_b("\u00a78" + var11, var5 + 36 + (116 - var12) / 2, var6 + 48 + 10, 0);
         String var13 = StatCollector.func_74838_a("book.finalizeWarning");
         this.field_73886_k.func_78279_b(var13, var5 + 36, var6 + 80, 116, 0);
      } else {
         var7 = String.format(StatCollector.func_74838_a("book.pageIndicator"), new Object[]{Integer.valueOf(this.field_74178_r + 1), Integer.valueOf(this.field_74179_q)});
         var8 = "";
         if(this.field_74177_s != null && this.field_74178_r >= 0 && this.field_74178_r < this.field_74177_s.func_74745_c()) {
            NBTTagString var14 = (NBTTagString)this.field_74177_s.func_74743_b(this.field_74178_r);
            var8 = var14.toString();
         }

         if(this.field_74168_c) {
            if(this.field_73886_k.func_78260_a()) {
               var8 = var8 + "_";
            } else if(this.field_74170_n / 6 % 2 == 0) {
               var8 = var8 + "\u00a70_";
            } else {
               var8 = var8 + "\u00a77_";
            }
         }

         var9 = this.field_73886_k.func_78256_a(var7);
         this.field_73886_k.func_78276_b(var7, var5 - var9 + this.field_74171_o - 44, var6 + 16, 0);
         this.field_73886_k.func_78279_b(var8, var5 + 36, var6 + 16 + 16, 116, 0);
      }

      super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
   }
}
