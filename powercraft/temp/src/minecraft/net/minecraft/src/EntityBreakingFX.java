package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Block;
import net.minecraft.src.EntityFX;
import net.minecraft.src.Item;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

@SideOnly(Side.CLIENT)
public class EntityBreakingFX extends EntityFX {

   public EntityBreakingFX(World p_i3164_1_, double p_i3164_2_, double p_i3164_4_, double p_i3164_6_, Item p_i3164_8_) {
      super(p_i3164_1_, p_i3164_2_, p_i3164_4_, p_i3164_6_, 0.0D, 0.0D, 0.0D);
      this.func_70536_a(p_i3164_8_.func_77617_a(0));
      this.field_70552_h = this.field_70553_i = this.field_70551_j = 1.0F;
      this.field_70545_g = Block.field_72039_aU.field_72017_co;
      this.field_70544_f /= 2.0F;
   }

   public EntityBreakingFX(World p_i3165_1_, double p_i3165_2_, double p_i3165_4_, double p_i3165_6_, double p_i3165_8_, double p_i3165_10_, double p_i3165_12_, Item p_i3165_14_) {
      this(p_i3165_1_, p_i3165_2_, p_i3165_4_, p_i3165_6_, p_i3165_14_);
      this.field_70159_w *= 0.10000000149011612D;
      this.field_70181_x *= 0.10000000149011612D;
      this.field_70179_y *= 0.10000000149011612D;
      this.field_70159_w += p_i3165_8_;
      this.field_70181_x += p_i3165_10_;
      this.field_70179_y += p_i3165_12_;
   }

   public int func_70537_b() {
      return 2;
   }

   public void func_70539_a(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_) {
      float var8 = ((float)(this.func_70540_h() % 16) + this.field_70548_b / 4.0F) / 16.0F;
      float var9 = var8 + 0.015609375F;
      float var10 = ((float)(this.func_70540_h() / 16) + this.field_70549_c / 4.0F) / 16.0F;
      float var11 = var10 + 0.015609375F;
      float var12 = 0.1F * this.field_70544_f;
      float var13 = (float)(this.field_70169_q + (this.field_70165_t - this.field_70169_q) * (double)p_70539_2_ - field_70556_an);
      float var14 = (float)(this.field_70167_r + (this.field_70163_u - this.field_70167_r) * (double)p_70539_2_ - field_70554_ao);
      float var15 = (float)(this.field_70166_s + (this.field_70161_v - this.field_70166_s) * (double)p_70539_2_ - field_70555_ap);
      float var16 = 1.0F;
      p_70539_1_.func_78386_a(var16 * this.field_70552_h, var16 * this.field_70553_i, var16 * this.field_70551_j);
      p_70539_1_.func_78374_a((double)(var13 - p_70539_3_ * var12 - p_70539_6_ * var12), (double)(var14 - p_70539_4_ * var12), (double)(var15 - p_70539_5_ * var12 - p_70539_7_ * var12), (double)var8, (double)var11);
      p_70539_1_.func_78374_a((double)(var13 - p_70539_3_ * var12 + p_70539_6_ * var12), (double)(var14 + p_70539_4_ * var12), (double)(var15 - p_70539_5_ * var12 + p_70539_7_ * var12), (double)var8, (double)var10);
      p_70539_1_.func_78374_a((double)(var13 + p_70539_3_ * var12 + p_70539_6_ * var12), (double)(var14 + p_70539_4_ * var12), (double)(var15 + p_70539_5_ * var12 + p_70539_7_ * var12), (double)var9, (double)var10);
      p_70539_1_.func_78374_a((double)(var13 + p_70539_3_ * var12 - p_70539_6_ * var12), (double)(var14 - p_70539_4_ * var12), (double)(var15 + p_70539_5_ * var12 - p_70539_7_ * var12), (double)var9, (double)var11);
   }
}
