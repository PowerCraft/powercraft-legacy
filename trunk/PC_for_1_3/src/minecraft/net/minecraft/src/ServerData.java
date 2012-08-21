package net.minecraft.src;

public class ServerData
{
    public String serverName;
    public String serverIP;
    public String field_78846_c;
    public String serverMOTD;
    public long field_78844_e;
    public boolean field_78841_f = false;
    private boolean field_78842_g = true;
    private boolean acceptsTextures = false;

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

    /**
     * Takes an NBTTagCompound with 'name' and 'ip' keys, returns a ServerData instance.
     */
    public static ServerData getServerDataFromNBTCompound(NBTTagCompound par0NBTTagCompound)
    {
        ServerData var1 = new ServerData(par0NBTTagCompound.getString("name"), par0NBTTagCompound.getString("ip"));

        if (par0NBTTagCompound.hasKey("acceptTextures"))
        {
            var1.setAcceptsTextures(par0NBTTagCompound.getBoolean("acceptTextures"));
        }

        return var1;
    }
}
