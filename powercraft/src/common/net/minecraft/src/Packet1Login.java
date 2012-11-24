package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cpw.mods.fml.common.network.FMLNetworkHandler;

public class Packet1Login extends Packet
{
    public int clientEntityId = 0;
    public WorldType terrainType;
    public boolean field_73560_c;
    public EnumGameType gameType;

    public int dimension;

    public byte difficultySetting;

    public byte worldHeight;

    public byte maxPlayers;

    private boolean vanillaCompatible;

    public Packet1Login()
    {
        this.vanillaCompatible = FMLNetworkHandler.vanillaLoginPacketCompatibility();
    }

    public Packet1Login(int par1, WorldType par2WorldType, EnumGameType par3EnumGameType, boolean par4, int par5, int par6, int par7, int par8)
    {
        this.clientEntityId = par1;
        this.terrainType = par2WorldType;
        this.dimension = par5;
        this.difficultySetting = (byte)par6;
        this.gameType = par3EnumGameType;
        this.worldHeight = (byte)par7;
        this.maxPlayers = (byte)par8;
        this.field_73560_c = par4;
        this.vanillaCompatible = false;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.clientEntityId = par1DataInputStream.readInt();
        String var2 = readString(par1DataInputStream, 16);
        this.terrainType = WorldType.parseWorldType(var2);

        if (this.terrainType == null)
        {
            this.terrainType = WorldType.DEFAULT;
        }

        byte var3 = par1DataInputStream.readByte();
        this.field_73560_c = (var3 & 8) == 8;
        int var4 = var3 & -9;
        this.gameType = EnumGameType.getByID(var4);

        if (vanillaCompatible)
        {
            this.dimension = par1DataInputStream.readByte();
        }
        else
        {
            this.dimension = par1DataInputStream.readInt();
        }

        this.difficultySetting = par1DataInputStream.readByte();
        this.worldHeight = par1DataInputStream.readByte();
        this.maxPlayers = par1DataInputStream.readByte();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.clientEntityId);
        writeString(this.terrainType == null ? "" : this.terrainType.getWorldTypeName(), par1DataOutputStream);
        int var2 = this.gameType.getID();

        if (this.field_73560_c)
        {
            var2 |= 8;
        }

        par1DataOutputStream.writeByte(var2);

        if (vanillaCompatible)
        {
            par1DataOutputStream.writeByte(this.dimension);
        }
        else
        {
            par1DataOutputStream.writeInt(this.dimension);
        }

        par1DataOutputStream.writeByte(this.difficultySetting);
        par1DataOutputStream.writeByte(this.worldHeight);
        par1DataOutputStream.writeByte(this.maxPlayers);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleLogin(this);
    }

    public int getPacketSize()
    {
        int var1 = 0;

        if (this.terrainType != null)
        {
            var1 = this.terrainType.getWorldTypeName().length();
        }

        return 6 + 2 * var1 + 4 + 4 + 1 + 1 + 1 + (vanillaCompatible ? 0 : 3);
    }
}
