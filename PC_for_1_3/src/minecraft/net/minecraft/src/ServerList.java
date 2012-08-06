package net.minecraft.src;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;

public class ServerList
{
    /** The Minecraft instance. */
    private final Minecraft mc;
    private final List field_78858_b = new ArrayList();

    public ServerList(Minecraft par1Minecraft)
    {
        this.mc = par1Minecraft;
        this.func_78853_a();
    }

    public void func_78853_a()
    {
        try
        {
            NBTTagCompound var1 = CompressedStreamTools.read(new File(this.mc.mcDataDir, "servers.dat"));
            NBTTagList var2 = var1.getTagList("servers");
            this.field_78858_b.clear();

            for (int var3 = 0; var3 < var2.tagCount(); ++var3)
            {
                this.field_78858_b.add(ServerData.getServerDataFromNBTCompound((NBTTagCompound)var2.tagAt(var3)));
            }
        }
        catch (Exception var4)
        {
            var4.printStackTrace();
        }
    }

    public void func_78855_b()
    {
        try
        {
            NBTTagList var1 = new NBTTagList();
            Iterator var2 = this.field_78858_b.iterator();

            while (var2.hasNext())
            {
                ServerData var3 = (ServerData)var2.next();
                var1.appendTag(var3.func_78836_a());
            }

            NBTTagCompound var5 = new NBTTagCompound();
            var5.setTag("servers", var1);
            CompressedStreamTools.safeWrite(var5, new File(this.mc.mcDataDir, "servers.dat"));
        }
        catch (Exception var4)
        {
            var4.printStackTrace();
        }
    }

    public ServerData func_78850_a(int par1)
    {
        return (ServerData)this.field_78858_b.get(par1);
    }

    public void func_78851_b(int par1)
    {
        this.field_78858_b.remove(par1);
    }

    public void func_78849_a(ServerData par1ServerData)
    {
        this.field_78858_b.add(par1ServerData);
    }

    public int func_78856_c()
    {
        return this.field_78858_b.size();
    }

    public void func_78857_a(int par1, int par2)
    {
        ServerData var3 = this.func_78850_a(par1);
        this.field_78858_b.set(par1, this.func_78850_a(par2));
        this.field_78858_b.set(par2, var3);
    }

    public void func_78854_a(int par1, ServerData par2ServerData)
    {
        this.field_78858_b.set(par1, par2ServerData);
    }

    public static void func_78852_b(ServerData par0ServerData)
    {
        ServerList var1 = new ServerList(Minecraft.getMinecraft());
        var1.func_78853_a();

        for (int var2 = 0; var2 < var1.func_78856_c(); ++var2)
        {
            ServerData var3 = var1.func_78850_a(var2);

            if (var3.serverName.equals(par0ServerData.serverName) && var3.serverIP.equals(par0ServerData.serverIP))
            {
                var1.func_78854_a(var2, par0ServerData);
                break;
            }
        }

        var1.func_78855_b();
    }
}
