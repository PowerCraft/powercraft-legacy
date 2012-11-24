package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.src.Block;
import net.minecraft.src.BlockRail;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.World;

public class RailLogic {

   private World field_73657_b;
   private int field_73658_c;
   private int field_73655_d;
   private int field_73656_e;
   private final boolean field_73653_f;
   private List field_73654_g;
   // $FF: synthetic field
   final BlockRail field_73659_a;


   public RailLogic(BlockRail p_i3983_1_, World p_i3983_2_, int p_i3983_3_, int p_i3983_4_, int p_i3983_5_) {
      this.field_73659_a = p_i3983_1_;
      this.field_73654_g = new ArrayList();
      this.field_73657_b = p_i3983_2_;
      this.field_73658_c = p_i3983_3_;
      this.field_73655_d = p_i3983_4_;
      this.field_73656_e = p_i3983_5_;
      int var6 = p_i3983_2_.func_72798_a(p_i3983_3_, p_i3983_4_, p_i3983_5_);
      int var7 = p_i3983_2_.func_72805_g(p_i3983_3_, p_i3983_4_, p_i3983_5_);
      if(BlockRail.func_72179_a((BlockRail)Block.field_71973_m[var6])) {
         this.field_73653_f = true;
         var7 &= -9;
      } else {
         this.field_73653_f = false;
      }

      this.func_73645_a(var7);
   }

   private void func_73645_a(int p_73645_1_) {
      this.field_73654_g.clear();
      if(p_73645_1_ == 0) {
         this.field_73654_g.add(new ChunkPosition(this.field_73658_c, this.field_73655_d, this.field_73656_e - 1));
         this.field_73654_g.add(new ChunkPosition(this.field_73658_c, this.field_73655_d, this.field_73656_e + 1));
      } else if(p_73645_1_ == 1) {
         this.field_73654_g.add(new ChunkPosition(this.field_73658_c - 1, this.field_73655_d, this.field_73656_e));
         this.field_73654_g.add(new ChunkPosition(this.field_73658_c + 1, this.field_73655_d, this.field_73656_e));
      } else if(p_73645_1_ == 2) {
         this.field_73654_g.add(new ChunkPosition(this.field_73658_c - 1, this.field_73655_d, this.field_73656_e));
         this.field_73654_g.add(new ChunkPosition(this.field_73658_c + 1, this.field_73655_d + 1, this.field_73656_e));
      } else if(p_73645_1_ == 3) {
         this.field_73654_g.add(new ChunkPosition(this.field_73658_c - 1, this.field_73655_d + 1, this.field_73656_e));
         this.field_73654_g.add(new ChunkPosition(this.field_73658_c + 1, this.field_73655_d, this.field_73656_e));
      } else if(p_73645_1_ == 4) {
         this.field_73654_g.add(new ChunkPosition(this.field_73658_c, this.field_73655_d + 1, this.field_73656_e - 1));
         this.field_73654_g.add(new ChunkPosition(this.field_73658_c, this.field_73655_d, this.field_73656_e + 1));
      } else if(p_73645_1_ == 5) {
         this.field_73654_g.add(new ChunkPosition(this.field_73658_c, this.field_73655_d, this.field_73656_e - 1));
         this.field_73654_g.add(new ChunkPosition(this.field_73658_c, this.field_73655_d + 1, this.field_73656_e + 1));
      } else if(p_73645_1_ == 6) {
         this.field_73654_g.add(new ChunkPosition(this.field_73658_c + 1, this.field_73655_d, this.field_73656_e));
         this.field_73654_g.add(new ChunkPosition(this.field_73658_c, this.field_73655_d, this.field_73656_e + 1));
      } else if(p_73645_1_ == 7) {
         this.field_73654_g.add(new ChunkPosition(this.field_73658_c - 1, this.field_73655_d, this.field_73656_e));
         this.field_73654_g.add(new ChunkPosition(this.field_73658_c, this.field_73655_d, this.field_73656_e + 1));
      } else if(p_73645_1_ == 8) {
         this.field_73654_g.add(new ChunkPosition(this.field_73658_c - 1, this.field_73655_d, this.field_73656_e));
         this.field_73654_g.add(new ChunkPosition(this.field_73658_c, this.field_73655_d, this.field_73656_e - 1));
      } else if(p_73645_1_ == 9) {
         this.field_73654_g.add(new ChunkPosition(this.field_73658_c + 1, this.field_73655_d, this.field_73656_e));
         this.field_73654_g.add(new ChunkPosition(this.field_73658_c, this.field_73655_d, this.field_73656_e - 1));
      }

   }

   private void func_73644_a() {
      for(int var1 = 0; var1 < this.field_73654_g.size(); ++var1) {
         RailLogic var2 = this.func_73648_a((ChunkPosition)this.field_73654_g.get(var1));
         if(var2 != null && var2.func_73647_b(this)) {
            this.field_73654_g.set(var1, new ChunkPosition(var2.field_73658_c, var2.field_73655_d, var2.field_73656_e));
         } else {
            this.field_73654_g.remove(var1--);
         }
      }

   }

   private boolean func_73642_a(int p_73642_1_, int p_73642_2_, int p_73642_3_) {
      return BlockRail.func_72180_d_(this.field_73657_b, p_73642_1_, p_73642_2_, p_73642_3_)?true:(BlockRail.func_72180_d_(this.field_73657_b, p_73642_1_, p_73642_2_ + 1, p_73642_3_)?true:BlockRail.func_72180_d_(this.field_73657_b, p_73642_1_, p_73642_2_ - 1, p_73642_3_));
   }

   private RailLogic func_73648_a(ChunkPosition p_73648_1_) {
      return BlockRail.func_72180_d_(this.field_73657_b, p_73648_1_.field_76930_a, p_73648_1_.field_76928_b, p_73648_1_.field_76929_c)?new RailLogic(this.field_73659_a, this.field_73657_b, p_73648_1_.field_76930_a, p_73648_1_.field_76928_b, p_73648_1_.field_76929_c):(BlockRail.func_72180_d_(this.field_73657_b, p_73648_1_.field_76930_a, p_73648_1_.field_76928_b + 1, p_73648_1_.field_76929_c)?new RailLogic(this.field_73659_a, this.field_73657_b, p_73648_1_.field_76930_a, p_73648_1_.field_76928_b + 1, p_73648_1_.field_76929_c):(BlockRail.func_72180_d_(this.field_73657_b, p_73648_1_.field_76930_a, p_73648_1_.field_76928_b - 1, p_73648_1_.field_76929_c)?new RailLogic(this.field_73659_a, this.field_73657_b, p_73648_1_.field_76930_a, p_73648_1_.field_76928_b - 1, p_73648_1_.field_76929_c):null));
   }

   private boolean func_73647_b(RailLogic p_73647_1_) {
      for(int var2 = 0; var2 < this.field_73654_g.size(); ++var2) {
         ChunkPosition var3 = (ChunkPosition)this.field_73654_g.get(var2);
         if(var3.field_76930_a == p_73647_1_.field_73658_c && var3.field_76929_c == p_73647_1_.field_73656_e) {
            return true;
         }
      }

      return false;
   }

   private boolean func_73649_b(int p_73649_1_, int p_73649_2_, int p_73649_3_) {
      for(int var4 = 0; var4 < this.field_73654_g.size(); ++var4) {
         ChunkPosition var5 = (ChunkPosition)this.field_73654_g.get(var4);
         if(var5.field_76930_a == p_73649_1_ && var5.field_76929_c == p_73649_3_) {
            return true;
         }
      }

      return false;
   }

   private int func_73651_b() {
      int var1 = 0;
      if(this.func_73642_a(this.field_73658_c, this.field_73655_d, this.field_73656_e - 1)) {
         ++var1;
      }

      if(this.func_73642_a(this.field_73658_c, this.field_73655_d, this.field_73656_e + 1)) {
         ++var1;
      }

      if(this.func_73642_a(this.field_73658_c - 1, this.field_73655_d, this.field_73656_e)) {
         ++var1;
      }

      if(this.func_73642_a(this.field_73658_c + 1, this.field_73655_d, this.field_73656_e)) {
         ++var1;
      }

      return var1;
   }

   private boolean func_73646_c(RailLogic p_73646_1_) {
      if(this.func_73647_b(p_73646_1_)) {
         return true;
      } else if(this.field_73654_g.size() == 2) {
         return false;
      } else if(this.field_73654_g.isEmpty()) {
         return true;
      } else {
         ChunkPosition var2 = (ChunkPosition)this.field_73654_g.get(0);
         return true;
      }
   }

   private void func_73641_d(RailLogic p_73641_1_) {
      this.field_73654_g.add(new ChunkPosition(p_73641_1_.field_73658_c, p_73641_1_.field_73655_d, p_73641_1_.field_73656_e));
      boolean var2 = this.func_73649_b(this.field_73658_c, this.field_73655_d, this.field_73656_e - 1);
      boolean var3 = this.func_73649_b(this.field_73658_c, this.field_73655_d, this.field_73656_e + 1);
      boolean var4 = this.func_73649_b(this.field_73658_c - 1, this.field_73655_d, this.field_73656_e);
      boolean var5 = this.func_73649_b(this.field_73658_c + 1, this.field_73655_d, this.field_73656_e);
      byte var6 = -1;
      if(var2 || var3) {
         var6 = 0;
      }

      if(var4 || var5) {
         var6 = 1;
      }

      if(!this.field_73653_f) {
         if(var3 && var5 && !var2 && !var4) {
            var6 = 6;
         }

         if(var3 && var4 && !var2 && !var5) {
            var6 = 7;
         }

         if(var2 && var4 && !var3 && !var5) {
            var6 = 8;
         }

         if(var2 && var5 && !var3 && !var4) {
            var6 = 9;
         }
      }

      if(var6 == 0) {
         if(BlockRail.func_72180_d_(this.field_73657_b, this.field_73658_c, this.field_73655_d + 1, this.field_73656_e - 1)) {
            var6 = 4;
         }

         if(BlockRail.func_72180_d_(this.field_73657_b, this.field_73658_c, this.field_73655_d + 1, this.field_73656_e + 1)) {
            var6 = 5;
         }
      }

      if(var6 == 1) {
         if(BlockRail.func_72180_d_(this.field_73657_b, this.field_73658_c + 1, this.field_73655_d + 1, this.field_73656_e)) {
            var6 = 2;
         }

         if(BlockRail.func_72180_d_(this.field_73657_b, this.field_73658_c - 1, this.field_73655_d + 1, this.field_73656_e)) {
            var6 = 3;
         }
      }

      if(var6 < 0) {
         var6 = 0;
      }

      int var7 = var6;
      if(this.field_73653_f) {
         var7 = this.field_73657_b.func_72805_g(this.field_73658_c, this.field_73655_d, this.field_73656_e) & 8 | var6;
      }

      this.field_73657_b.func_72921_c(this.field_73658_c, this.field_73655_d, this.field_73656_e, var7);
   }

   private boolean func_73643_c(int p_73643_1_, int p_73643_2_, int p_73643_3_) {
      RailLogic var4 = this.func_73648_a(new ChunkPosition(p_73643_1_, p_73643_2_, p_73643_3_));
      if(var4 == null) {
         return false;
      } else {
         var4.func_73644_a();
         return var4.func_73646_c(this);
      }
   }

   public void func_73652_a(boolean p_73652_1_, boolean p_73652_2_) {
      boolean var3 = this.func_73643_c(this.field_73658_c, this.field_73655_d, this.field_73656_e - 1);
      boolean var4 = this.func_73643_c(this.field_73658_c, this.field_73655_d, this.field_73656_e + 1);
      boolean var5 = this.func_73643_c(this.field_73658_c - 1, this.field_73655_d, this.field_73656_e);
      boolean var6 = this.func_73643_c(this.field_73658_c + 1, this.field_73655_d, this.field_73656_e);
      byte var7 = -1;
      if((var3 || var4) && !var5 && !var6) {
         var7 = 0;
      }

      if((var5 || var6) && !var3 && !var4) {
         var7 = 1;
      }

      if(!this.field_73653_f) {
         if(var4 && var6 && !var3 && !var5) {
            var7 = 6;
         }

         if(var4 && var5 && !var3 && !var6) {
            var7 = 7;
         }

         if(var3 && var5 && !var4 && !var6) {
            var7 = 8;
         }

         if(var3 && var6 && !var4 && !var5) {
            var7 = 9;
         }
      }

      if(var7 == -1) {
         if(var3 || var4) {
            var7 = 0;
         }

         if(var5 || var6) {
            var7 = 1;
         }

         if(!this.field_73653_f) {
            if(p_73652_1_) {
               if(var4 && var6) {
                  var7 = 6;
               }

               if(var5 && var4) {
                  var7 = 7;
               }

               if(var6 && var3) {
                  var7 = 9;
               }

               if(var3 && var5) {
                  var7 = 8;
               }
            } else {
               if(var3 && var5) {
                  var7 = 8;
               }

               if(var6 && var3) {
                  var7 = 9;
               }

               if(var5 && var4) {
                  var7 = 7;
               }

               if(var4 && var6) {
                  var7 = 6;
               }
            }
         }
      }

      if(var7 == 0) {
         if(BlockRail.func_72180_d_(this.field_73657_b, this.field_73658_c, this.field_73655_d + 1, this.field_73656_e - 1)) {
            var7 = 4;
         }

         if(BlockRail.func_72180_d_(this.field_73657_b, this.field_73658_c, this.field_73655_d + 1, this.field_73656_e + 1)) {
            var7 = 5;
         }
      }

      if(var7 == 1) {
         if(BlockRail.func_72180_d_(this.field_73657_b, this.field_73658_c + 1, this.field_73655_d + 1, this.field_73656_e)) {
            var7 = 2;
         }

         if(BlockRail.func_72180_d_(this.field_73657_b, this.field_73658_c - 1, this.field_73655_d + 1, this.field_73656_e)) {
            var7 = 3;
         }
      }

      if(var7 < 0) {
         var7 = 0;
      }

      this.func_73645_a(var7);
      int var8 = var7;
      if(this.field_73653_f) {
         var8 = this.field_73657_b.func_72805_g(this.field_73658_c, this.field_73655_d, this.field_73656_e) & 8 | var7;
      }

      if(p_73652_2_ || this.field_73657_b.func_72805_g(this.field_73658_c, this.field_73655_d, this.field_73656_e) != var8) {
         this.field_73657_b.func_72921_c(this.field_73658_c, this.field_73655_d, this.field_73656_e, var8);

         for(int var9 = 0; var9 < this.field_73654_g.size(); ++var9) {
            RailLogic var10 = this.func_73648_a((ChunkPosition)this.field_73654_g.get(var9));
            if(var10 != null) {
               var10.func_73644_a();
               if(var10.func_73646_c(this)) {
                  var10.func_73641_d(this);
               }
            }
         }
      }

   }

   // $FF: synthetic method
   public static int func_73650_a(RailLogic p_73650_0_) {
      return p_73650_0_.func_73651_b();
   }
}
