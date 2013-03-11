package net.minecraft.network;

import java.util.concurrent.Callable;
import net.minecraft.network.packet.Packet;

class CallablePacketID implements Callable
{
    final Packet field_98245_a;

    final NetServerHandler field_98244_b;

    CallablePacketID(NetServerHandler par1NetServerHandler, Packet par2Packet)
    {
        this.field_98244_b = par1NetServerHandler;
        this.field_98245_a = par2Packet;
    }

    public String func_98243_a()
    {
        return String.valueOf(this.field_98245_a.getPacketId());
    }

    public Object call()
    {
        return this.func_98243_a();
    }
}
