package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.src.ComponentStrongholdStairs2;
import net.minecraft.src.StructureComponent;
import net.minecraft.src.StructureStart;
import net.minecraft.src.StructureStrongholdPieces;
import net.minecraft.src.World;

class StructureStrongholdStart extends StructureStart {

   public StructureStrongholdStart(World p_i3837_1_, Random p_i3837_2_, int p_i3837_3_, int p_i3837_4_) {
      StructureStrongholdPieces.func_75198_a();
      ComponentStrongholdStairs2 var5 = new ComponentStrongholdStairs2(0, p_i3837_2_, (p_i3837_3_ << 4) + 2, (p_i3837_4_ << 4) + 2);
      this.field_75075_a.add(var5);
      var5.func_74861_a(var5, this.field_75075_a, p_i3837_2_);
      ArrayList var6 = var5.field_75026_c;

      while(!var6.isEmpty()) {
         int var7 = p_i3837_2_.nextInt(var6.size());
         StructureComponent var8 = (StructureComponent)var6.remove(var7);
         var8.func_74861_a(var5, this.field_75075_a, p_i3837_2_);
      }

      this.func_75072_c();
      this.func_75067_a(p_i3837_1_, p_i3837_2_, 10);
   }
}
