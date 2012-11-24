package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.EntityItem;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

public class Packet21PickupSpawn extends Packet {

   public int field_73485_a;
   public int field_73483_b;
   public int field_73484_c;
   public int field_73481_d;
   public byte field_73482_e;
   public byte field_73479_f;
   public byte field_73480_g;
   public ItemStack field_73487_h;


   public Packet21PickupSpawn() {}

   public Packet21PickupSpawn(EntityItem p_i3293_1_) {
      this.field_73485_a = p_i3293_1_.field_70157_k;
      this.field_73487_h = p_i3293_1_.field_70294_a.func_77946_l();
      this.field_73483_b = MathHelper.func_76128_c(p_i3293_1_.field_70165_t * 32.0D);
      this.field_73484_c = MathHelper.func_76128_c(p_i3293_1_.field_70163_u * 32.0D);
      this.field_73481_d = MathHelper.func_76128_c(p_i3293_1_.field_70161_v * 32.0D);
      this.field_73482_e = (byte)((int)(p_i3293_1_.field_70159_w * 128.0D));
      this.field_73479_f = (byte)((int)(p_i3293_1_.field_70181_x * 128.0D));
      this.field_73480_g = (byte)((int)(p_i3293_1_.field_70179_y * 128.0D));
   }

   public void func_73267_a(DataInputStream p_73267_1_) throws IOException {
      this.field_73485_a = p_73267_1_.readInt();
      this.field_73487_h = func_73276_c(p_73267_1_);
      this.field_73483_b = p_73267_1_.readInt();
      this.field_73484_c = p_73267_1_.readInt();
      this.field_73481_d = p_73267_1_.readInt();
      this.field_73482_e = p_73267_1_.readByte();
      this.field_73479_f = p_73267_1_.readByte();
      this.field_73480_g = p_73267_1_.readByte();
   }

   public void func_73273_a(DataOutputStream p_73273_1_) throws IOException {
      p_73273_1_.writeInt(this.field_73485_a);
      func_73270_a(this.field_73487_h, p_73273_1_);
      p_73273_1_.writeInt(this.field_73483_b);
      p_73273_1_.writeInt(this.field_73484_c);
      p_73273_1_.writeInt(this.field_73481_d);
      p_73273_1_.writeByte(this.field_73482_e);
      p_73273_1_.writeByte(this.field_73479_f);
      p_73273_1_.writeByte(this.field_73480_g);
   }

   public void func_73279_a(NetHandler p_73279_1_) {
      p_73279_1_.func_72459_a(this);
   }

   public int func_73284_a() {
      return 24;
   }
}
