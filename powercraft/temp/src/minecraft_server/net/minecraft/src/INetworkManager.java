package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.net.SocketAddress;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

public interface INetworkManager {

   void func_74425_a(NetHandler var1);

   void func_74429_a(Packet var1);

   void func_74427_a();

   void func_74428_b();

   SocketAddress func_74430_c();

   void func_74423_d();

   int func_74426_e();

   void func_74424_a(String var1, Object ... var2);

   @SideOnly(Side.CLIENT)
   void func_74431_f();
}
