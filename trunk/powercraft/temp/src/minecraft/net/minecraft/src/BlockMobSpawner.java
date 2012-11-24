package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Random;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityMobSpawner;
import net.minecraft.src.World;

public class BlockMobSpawner extends BlockContainer {

   protected BlockMobSpawner(int p_i3970_1_, int p_i3970_2_) {
      super(p_i3970_1_, p_i3970_2_, Material.field_76246_e);
   }

   public TileEntity func_72274_a(World p_72274_1_) {
      return new TileEntityMobSpawner();
   }

   public int func_71885_a(int p_71885_1_, Random p_71885_2_, int p_71885_3_) {
      return 0;
   }

   public int func_71925_a(Random p_71925_1_) {
      return 0;
   }

   public void func_71914_a(World p_71914_1_, int p_71914_2_, int p_71914_3_, int p_71914_4_, int p_71914_5_, float p_71914_6_, int p_71914_7_) {
      super.func_71914_a(p_71914_1_, p_71914_2_, p_71914_3_, p_71914_4_, p_71914_5_, p_71914_6_, p_71914_7_);
      int var8 = 15 + p_71914_1_.field_73012_v.nextInt(15) + p_71914_1_.field_73012_v.nextInt(15);
      this.func_71923_g(p_71914_1_, p_71914_2_, p_71914_3_, p_71914_4_, var8);
   }

   public boolean func_71926_d() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public int func_71922_a(World p_71922_1_, int p_71922_2_, int p_71922_3_, int p_71922_4_) {
      return 0;
   }
}
