package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public class LanServer
{
    private String field_77492_a;
    private String field_77490_b;

    /** Last time this LanServer was seen. */
    private long timeLastSeen;

    public LanServer(String par1Str, String par2Str)
    {
        this.field_77492_a = par1Str;
        this.field_77490_b = par2Str;
        this.timeLastSeen = Minecraft.getSystemTime();
    }

    public String func_77487_a()
    {
        return this.field_77492_a;
    }

    public String func_77488_b()
    {
        return this.field_77490_b;
    }

    /**
     * Updates the time this LanServer was last seen.
     */
    public void updateLastSeen()
    {
        this.timeLastSeen = Minecraft.getSystemTime();
    }
}
