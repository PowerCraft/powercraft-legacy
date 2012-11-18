package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet203AutoComplete extends Packet
{
    private String text;

    public Packet203AutoComplete() {}

    public Packet203AutoComplete(String par1Str)
    {
        this.text = par1Str;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.text = readString(par1DataInputStream, Packet3Chat.maxChatLength);
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        writeString(this.text, par1DataOutputStream);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleAutoComplete(this);
    }

    public int getPacketSize()
    {
        return 2 + this.text.length() * 2;
    }

    public String getText()
    {
        return this.text;
    }

    public boolean isRealPacket()
    {
        return true;
    }

    public boolean containsSameEntityIDAs(Packet par1Packet)
    {
        return true;
    }
}
