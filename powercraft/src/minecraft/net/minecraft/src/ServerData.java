package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class ServerData
{
    public String serverName;
    public String serverIP;

    /**
     * the string indicating number of players on and capacity of the server that is shown on the server browser (i.e.
     * "5/20" meaning 5 slots used out of 20 slots total)
     */
    public String populationInfo;

    /**
     * (better variable name would be 'hostname') server name as displayed in the server browser's second line (grey
     * text)
     */
    public String serverMOTD;

    /** last server ping that showed up in the server browser */
    public long pingToServer;
    public int field_82821_f = 47;
    public String field_82822_g = "1.4.2";
    public boolean field_78841_f = false;
    private boolean field_78842_g = true;
    private boolean acceptsTextures = false;
    private boolean field_82823_k = false;

    public ServerData(String par1Str, String par2Str)
    {
        this.serverName = par1Str;
        this.serverIP = par2Str;
    }

    /**
     * Returns an NBTTagCompound with the server's name, IP and maybe acceptTextures.
     */
    public NBTTagCompound getNBTCompound()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        var1.setString("name", this.serverName);
        var1.setString("ip", this.serverIP);
        var1.setBoolean("hideAddress", this.field_82823_k);

        if (!this.field_78842_g)
        {
            var1.setBoolean("acceptTextures", this.acceptsTextures);
        }

        return var1;
    }

    public boolean getAcceptsTextures()
    {
        return this.acceptsTextures;
    }

    public boolean func_78840_c()
    {
        return this.field_78842_g;
    }

    public void setAcceptsTextures(boolean par1)
    {
        this.acceptsTextures = par1;
        this.field_78842_g = false;
    }

    public boolean func_82820_d()
    {
        return this.field_82823_k;
    }

    public void func_82819_b(boolean par1)
    {
        this.field_82823_k = par1;
    }

    /**
     * Takes an NBTTagCompound with 'name' and 'ip' keys, returns a ServerData instance.
     */
    public static ServerData getServerDataFromNBTCompound(NBTTagCompound par0NBTTagCompound)
    {
        ServerData var1 = new ServerData(par0NBTTagCompound.getString("name"), par0NBTTagCompound.getString("ip"));
        var1.field_82823_k = par0NBTTagCompound.getBoolean("hideAddress");

        if (par0NBTTagCompound.hasKey("acceptTextures"))
        {
            var1.setAcceptsTextures(par0NBTTagCompound.getBoolean("acceptTextures"));
        }

        return var1;
    }
}
