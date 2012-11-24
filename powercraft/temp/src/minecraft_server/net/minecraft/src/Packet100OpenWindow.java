package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

public class Packet100OpenWindow extends Packet {

   public int field_73431_a;
   public int field_73429_b;
   public String field_73430_c;
   public int field_73428_d;


   public Packet100OpenWindow() {}

   public Packet100OpenWindow(int p_i3311_1_, int p_i3311_2_, String p_i3311_3_, int p_i3311_4_) {
      this.field_73431_a = p_i3311_1_;
      this.field_73429_b = p_i3311_2_;
      this.field_73430_c = p_i3311_3_;
      this.field_73428_d = p_i3311_4_;
   }

   public void func_73279_a(NetHandler p_73279_1_) {
      p_73279_1_.func_72516_a(this);
   }

   public void func_73267_a(DataInputStream p_73267_1_) throws IOException {
      this.field_73431_a = p_73267_1_.readByte() & 255;
      this.field_73429_b = p_73267_1_.readByte() & 255;
      this.field_73430_c = func_73282_a(p_73267_1_, 32);
      this.field_73428_d = p_73267_1_.readByte() & 255;
   }

   public void func_73273_a(DataOutputStream p_73273_1_) throws IOException {
      p_73273_1_.writeByte(this.field_73431_a & 255);
      p_73273_1_.writeByte(this.field_73429_b & 255);
      func_73271_a(this.field_73430_c, p_73273_1_);
      p_73273_1_.writeByte(this.field_73428_d & 255);
   }

   public int func_73284_a() {
      return 3 + this.field_73430_c.length();
   }
}
