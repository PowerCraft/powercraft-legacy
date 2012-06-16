package net.minecraft.src;

import java.io.*;

public class Packet202PlayerAbilities extends Packet
{
    /** Disables player damage. */
    public boolean disableDamage;

    /** Indicates whether the player is flying or not. */
    public boolean isFlying;

    /** Whether or not to allow the player to fly when they double jump. */
    public boolean allowFlying;

    /**
     * Used to determine if creative mode is enabled, and therefore if items should be depleted on usage
     */
    public boolean isCreativeMode;

    public Packet202PlayerAbilities()
    {
        disableDamage = false;
        isFlying = false;
        allowFlying = false;
        isCreativeMode = false;
    }

    public Packet202PlayerAbilities(PlayerCapabilities par1PlayerCapabilities)
    {
        disableDamage = false;
        isFlying = false;
        allowFlying = false;
        isCreativeMode = false;
        disableDamage = par1PlayerCapabilities.disableDamage;
        isFlying = par1PlayerCapabilities.isFlying;
        allowFlying = par1PlayerCapabilities.allowFlying;
        isCreativeMode = par1PlayerCapabilities.isCreativeMode;
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        disableDamage = par1DataInputStream.readBoolean();
        isFlying = par1DataInputStream.readBoolean();
        allowFlying = par1DataInputStream.readBoolean();
        isCreativeMode = par1DataInputStream.readBoolean();
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeBoolean(disableDamage);
        par1DataOutputStream.writeBoolean(isFlying);
        par1DataOutputStream.writeBoolean(allowFlying);
        par1DataOutputStream.writeBoolean(isCreativeMode);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handlePlayerAbilities(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 1;
    }
}
