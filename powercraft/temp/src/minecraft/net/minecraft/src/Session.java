package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.src.Block;

@SideOnly(Side.CLIENT)
public class Session {

   public static List field_74288_a = new ArrayList();
   public String field_74286_b;
   public String field_74287_c;


   public Session(String p_i3014_1_, String p_i3014_2_) {
      this.field_74286_b = p_i3014_1_;
      this.field_74287_c = p_i3014_2_;
   }

   static {
      field_74288_a.add(Block.field_71981_t);
      field_74288_a.add(Block.field_71978_w);
      field_74288_a.add(Block.field_72081_al);
      field_74288_a.add(Block.field_71979_v);
      field_74288_a.add(Block.field_71988_x);
      field_74288_a.add(Block.field_71951_J);
      field_74288_a.add(Block.field_71952_K);
      field_74288_a.add(Block.field_72069_aq);
      field_74288_a.add(Block.field_72079_ak);
      field_74288_a.add(Block.field_71946_M);
      field_74288_a.add(Block.field_72087_ao);
      field_74288_a.add(Block.field_71987_y);
      field_74288_a.add(Block.field_72097_ad);
      field_74288_a.add(Block.field_72107_ae);
      field_74288_a.add(Block.field_72109_af);
      field_74288_a.add(Block.field_72103_ag);
      field_74288_a.add(Block.field_71939_E);
      field_74288_a.add(Block.field_71940_F);
      field_74288_a.add(Block.field_71945_L);
      field_74288_a.add(Block.field_72101_ab);
      field_74288_a.add(Block.field_71950_I);
      field_74288_a.add(Block.field_71949_H);
      field_74288_a.add(Block.field_71941_G);
      field_74288_a.add(Block.field_72083_ai);
      field_74288_a.add(Block.field_72105_ah);
      field_74288_a.add(Block.field_72093_an);
      field_74288_a.add(Block.field_72091_am);
      field_74288_a.add(Block.field_72089_ap);
   }
}
