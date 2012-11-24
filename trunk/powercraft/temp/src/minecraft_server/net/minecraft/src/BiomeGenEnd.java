package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.BiomeEndDecorator;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.EntityEnderman;
import net.minecraft.src.SpawnListEntry;

public class BiomeGenEnd extends BiomeGenBase {

   public BiomeGenEnd(int p_i3766_1_) {
      super(p_i3766_1_);
      this.field_76761_J.clear();
      this.field_76762_K.clear();
      this.field_76755_L.clear();
      this.field_82914_M.clear();
      this.field_76761_J.add(new SpawnListEntry(EntityEnderman.class, 10, 4, 4));
      this.field_76752_A = (byte)Block.field_71979_v.field_71990_ca;
      this.field_76753_B = (byte)Block.field_71979_v.field_71990_ca;
      this.field_76760_I = new BiomeEndDecorator(this);
   }

   @SideOnly(Side.CLIENT)
   public int func_76731_a(float p_76731_1_) {
      return 0;
   }
}
