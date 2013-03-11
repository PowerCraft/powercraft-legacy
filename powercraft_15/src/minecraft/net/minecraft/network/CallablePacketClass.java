package net.minecraft.network;

import java.util.concurrent.Callable;
import net.minecraft.network.packet.Packet;

class CallablePacketClass implements Callable
{
    final Packet field_98227_a;

    final NetServerHandler field_98226_b;

    CallablePacketClass(NetServerHandler par1NetServerHandler, Packet par2Packet)
    {
        this.field_98226_b = par1NetServerHandler;
        this.field_98227_a = par2Packet;
    }

    public String func_98225_a()
    {
        return this.field_98227_a.getClass().getCanonicalName();
    }

    public Object call()
    {
        return this.func_98225_a();
    }
}
