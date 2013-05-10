package net.minecraft.client.mco;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.util.Session;

@SideOnly(Side.CLIENT)
public class McoClient
{
    private static McoOption field_98178_a = McoOption.func_98154_b();
    private final String field_96390_a;
    private final String field_100007_c;
    private static String field_96388_b = "https://mcoapi.minecraft.net/";

    public McoClient(Session par1Session)
    {
        this.field_96390_a = par1Session.sessionId;
        this.field_100007_c = par1Session.username;
    }

    public ValueObjectList func_96382_a() throws ExceptionMcoService
    {
        String s = this.func_96377_a(Request.func_96358_a(field_96388_b + "worlds"));
        return ValueObjectList.func_98161_a(s);
    }

    public McoServer func_98176_a(long par1) throws ExceptionMcoService, IOException
    {
        String s = this.func_96377_a(Request.func_96358_a(field_96388_b + "worlds" + "/$ID".replace("$ID", String.valueOf(par1))));
        return McoServer.func_98165_c(s);
    }

    public McoServerAddress func_96374_a(long par1) throws ExceptionMcoService, IOException
    {
        String s = field_96388_b + "worlds" + "/$ID/join".replace("$ID", "" + par1);
        String s1 = this.func_96377_a(Request.func_96358_a(s));
        return McoServerAddress.func_98162_a(s1);
    }

    public void func_96386_a(String par1Str, String par2Str, String par3Str, String par4Str) throws ExceptionMcoService, UnsupportedEncodingException
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(field_96388_b).append("worlds").append("/$NAME/$LOCATION_ID".replace("$NAME", this.func_96380_a(par1Str)).replace("$LOCATION_ID", par3Str));
        HashMap hashmap = new HashMap();

        if (par2Str != null && !par2Str.trim().equals(""))
        {
            hashmap.put("motd", par2Str);
        }

        if (par4Str != null && !par4Str.equals(""))
        {
            hashmap.put("seed", par4Str);
        }

        if (!hashmap.isEmpty())
        {
            boolean flag = true;
            Entry entry;

            for (Iterator iterator = hashmap.entrySet().iterator(); iterator.hasNext(); stringbuilder.append((String)entry.getKey()).append("=").append(this.func_96380_a((String)entry.getValue())))
            {
                entry = (Entry)iterator.next();

                if (flag)
                {
                    stringbuilder.append("?");
                    flag = false;
                }
                else
                {
                    stringbuilder.append("&");
                }
            }
        }

        this.func_96377_a(Request.func_104064_a(stringbuilder.toString(), "", 5000, 30000));
    }

    public Boolean func_96375_b() throws ExceptionMcoService, IOException
    {
        String s = field_96388_b + "mco" + "/available";
        String s1 = this.func_96377_a(Request.func_96358_a(s));
        return Boolean.valueOf(s1);
    }

    public int func_96379_c() throws ExceptionMcoService
    {
        String s = field_96388_b + "payments" + "/unused";
        String s1 = this.func_96377_a(Request.func_96358_a(s));
        return Integer.valueOf(s1).intValue();
    }

    public void func_96381_a(long par1, String par3Str) throws ExceptionMcoService
    {
        String s1 = field_96388_b + "worlds" + "/$WORLD_ID/invites/$USER_NAME".replace("$WORLD_ID", String.valueOf(par1)).replace("$USER_NAME", par3Str);
        this.func_96377_a(Request.func_96355_b(s1));
    }

    public McoServer func_96387_b(long par1, String par3Str) throws ExceptionMcoService, IOException
    {
        String s1 = field_96388_b + "worlds" + "/$WORLD_ID/invites/$USER_NAME".replace("$WORLD_ID", String.valueOf(par1)).replace("$USER_NAME", par3Str);
        String s2 = this.func_96377_a(Request.func_96361_b(s1, ""));
        return McoServer.func_98165_c(s2);
    }

    public void func_96384_a(long par1, String par3Str, String par4Str, int par5, int par6) throws ExceptionMcoService, UnsupportedEncodingException
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(field_96388_b).append("worlds").append("/$WORLD_ID/$NAME".replace("$WORLD_ID", String.valueOf(par1)).replace("$NAME", this.func_96380_a(par3Str)));

        if (par4Str != null && !par4Str.trim().equals(""))
        {
            stringbuilder.append("?motd=").append(this.func_96380_a(par4Str));
        }

        stringbuilder.append("&difficulty=").append(par5).append("&gameMode=").append(par6);
        this.func_96377_a(Request.func_96363_c(stringbuilder.toString(), ""));
    }

    public Boolean func_96383_b(long par1) throws ExceptionMcoService, IOException
    {
        String s = field_96388_b + "worlds" + "/$WORLD_ID/open".replace("$WORLD_ID", String.valueOf(par1));
        String s1 = this.func_96377_a(Request.func_96363_c(s, ""));
        return Boolean.valueOf(s1);
    }

    public Boolean func_96378_c(long par1) throws ExceptionMcoService, IOException
    {
        String s = field_96388_b + "worlds" + "/$WORLD_ID/close".replace("$WORLD_ID", String.valueOf(par1));
        String s1 = this.func_96377_a(Request.func_96363_c(s, ""));
        return Boolean.valueOf(s1);
    }

    public Boolean func_96376_d(long par1, String par3Str) throws ExceptionMcoService, UnsupportedEncodingException
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(field_96388_b).append("worlds").append("/$WORLD_ID/reset".replace("$WORLD_ID", String.valueOf(par1)));

        if (par3Str != null && par3Str.length() > 0)
        {
            stringbuilder.append("?seed=").append(this.func_96380_a(par3Str));
        }

        String s1 = this.func_96377_a(Request.func_96353_a(stringbuilder.toString(), "", 30000, 80000));
        return Boolean.valueOf(s1);
    }

    public ValueObjectSubscription func_98177_f(long par1) throws ExceptionMcoService, IOException
    {
        String s = this.func_96377_a(Request.func_96358_a(field_96388_b + "subscriptions" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(par1))));
        return ValueObjectSubscription.func_98169_a(s);
    }

    private String func_96380_a(String par1Str) throws UnsupportedEncodingException
    {
        return URLEncoder.encode(par1Str, "UTF-8");
    }

    private String func_96377_a(Request par1Request) throws ExceptionMcoService
    {
        par1Request.func_100006_a("sid", this.field_96390_a);
        par1Request.func_100006_a("user", this.field_100007_c);

        if (field_98178_a instanceof McoOptionSome)
        {
            McoPair mcopair = (McoPair)field_98178_a.func_98155_a();
            par1Request.func_100006_a((String)mcopair.func_100005_a(), (String)mcopair.func_100004_b());
        }

        try
        {
            int i = par1Request.func_96362_a();

            if (i == 503)
            {
                throw new ExceptionRetryCall(10);
            }
            else if (i >= 200 && i < 300)
            {
                McoOption mcooption = par1Request.func_98175_b();

                if (mcooption instanceof McoOptionSome)
                {
                    field_98178_a = mcooption;
                }

                return par1Request.func_96364_c();
            }
            else
            {
                throw new ExceptionMcoService(par1Request.func_96362_a(), par1Request.func_96364_c());
            }
        }
        catch (ExceptionMcoHttp exceptionmcohttp)
        {
            throw new ExceptionMcoService(500, "Server not available!");
        }
    }
}
