package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet3Chat extends Packet
{
    public static int maxChatLength = 119;

    public String message;
    private boolean field_73477_c;

    public Packet3Chat()
    {
        this.field_73477_c = true;
    }

    public Packet3Chat(String par1Str)
    {
        this(par1Str, true);
    }

    public Packet3Chat(String par1Str, boolean par2)
    {
        this.field_73477_c = true;

        if (par1Str.length() > maxChatLength)
        {
            par1Str = par1Str.substring(0, maxChatLength);
        }

        this.message = par1Str;
        this.field_73477_c = par2;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.message = readString(par1DataInputStream, maxChatLength);
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        writeString(this.message, par1DataOutputStream);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleChat(this);
    }

    public int getPacketSize()
    {
        return 2 + this.message.length() * 2;
    }

    public boolean func_73475_d()
    {
        return this.field_73477_c;
    }

    public boolean isWritePacket()
    {
        return !this.message.startsWith("/");
    }
}
