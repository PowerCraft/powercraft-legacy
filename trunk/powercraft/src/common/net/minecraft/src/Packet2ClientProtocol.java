package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet2ClientProtocol extends Packet
{
    private int protocolVersion;
    private String username;
    private String serverHost;
    private int serverPort;

    public Packet2ClientProtocol() {}

    @SideOnly(Side.CLIENT)
    public Packet2ClientProtocol(int par1, String par2Str, String par3Str, int par4)
    {
        this.protocolVersion = par1;
        this.username = par2Str;
        this.serverHost = par3Str;
        this.serverPort = par4;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.protocolVersion = par1DataInputStream.readByte();
        this.username = readString(par1DataInputStream, 16);
        this.serverHost = readString(par1DataInputStream, 255);
        this.serverPort = par1DataInputStream.readInt();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeByte(this.protocolVersion);
        writeString(this.username, par1DataOutputStream);
        writeString(this.serverHost, par1DataOutputStream);
        par1DataOutputStream.writeInt(this.serverPort);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleClientProtocol(this);
    }

    public int getPacketSize()
    {
        return 3 + 2 * this.username.length();
    }

    public int getProtocolVersion()
    {
        return this.protocolVersion;
    }

    public String getUsername()
    {
        return this.username;
    }
}
