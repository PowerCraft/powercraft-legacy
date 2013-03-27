package net.minecraft.src;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
        String var1 = this.func_96377_a(Request.func_96358_a(field_96388_b + "worlds"));
        return ValueObjectList.func_98161_a(var1);
    }

    public McoServer func_98176_a(long par1) throws ExceptionMcoService, IOException
    {
        String var3 = this.func_96377_a(Request.func_96358_a(field_96388_b + "worlds" + "/$ID".replace("$ID", String.valueOf(par1))));
        return McoServer.func_98165_c(var3);
    }

    public McoServerAddress func_96374_a(long par1) throws ExceptionMcoService, IOException
    {
        String var3 = field_96388_b + "worlds" + "/$ID/join".replace("$ID", "" + par1);
        String var4 = this.func_96377_a(Request.func_96358_a(var3));
        return McoServerAddress.func_98162_a(var4);
    }

    public void func_96386_a(String par1Str, String par2Str, String par3Str) throws IOException, ExceptionMcoService, UnsupportedEncodingException
    {
        String var4 = field_96388_b + "worlds" + "/$NAME/$LOCATION_ID".replace("$NAME", this.func_96380_a(par1Str)).replace("$LOCATION_ID", par3Str);

        if (par2Str != null && !par2Str.trim().equals(""))
        {
            var4 = var4 + "?motd=" + this.func_96380_a(par2Str);
        }

        this.func_96377_a(Request.func_96361_b(var4, ""));
    }

    public Boolean func_96375_b() throws ExceptionMcoService, IOException
    {
        String var1 = field_96388_b + "mco" + "/available";
        String var2 = this.func_96377_a(Request.func_96358_a(var1));
        return Boolean.valueOf(var2);
    }

    public int func_96379_c() throws ExceptionMcoService
    {
        String var1 = field_96388_b + "payments" + "/unused";
        String var2 = this.func_96377_a(Request.func_96358_a(var1));
        return Integer.valueOf(var2).intValue();
    }

    public void func_96381_a(long par1, String par3Str) throws ExceptionMcoService
    {
        String var4 = field_96388_b + "worlds" + "/$WORLD_ID/invites/$USER_NAME".replace("$WORLD_ID", String.valueOf(par1)).replace("$USER_NAME", par3Str);
        this.func_96377_a(Request.func_96355_b(var4));
    }

    public McoServer func_96387_b(long par1, String par3Str) throws ExceptionMcoService, IOException
    {
        String var4 = field_96388_b + "worlds" + "/$WORLD_ID/invites/$USER_NAME".replace("$WORLD_ID", String.valueOf(par1)).replace("$USER_NAME", par3Str);
        String var5 = this.func_96377_a(Request.func_96361_b(var4, ""));
        return McoServer.func_98165_c(var5);
    }

    public void func_96384_a(long par1, String par3Str, String par4Str) throws ExceptionMcoService, UnsupportedEncodingException
    {
        String var5 = field_96388_b + "worlds" + "/$WORLD_ID/$NAME".replace("$WORLD_ID", String.valueOf(par1)).replace("$NAME", this.func_96380_a(par3Str));

        if (par4Str != null && !par4Str.trim().equals(""))
        {
            var5 = var5 + "?motd=" + this.func_96380_a(par4Str);
        }

        this.func_96377_a(Request.func_96363_c(var5, ""));
    }

    public Boolean func_96383_b(long par1) throws ExceptionMcoService, IOException
    {
        String var3 = field_96388_b + "worlds" + "/$WORLD_ID/open".replace("$WORLD_ID", String.valueOf(par1));
        String var4 = this.func_96377_a(Request.func_96363_c(var3, ""));
        return Boolean.valueOf(var4);
    }

    public Boolean func_96378_c(long par1) throws ExceptionMcoService, IOException
    {
        String var3 = field_96388_b + "worlds" + "/$WORLD_ID/close".replace("$WORLD_ID", String.valueOf(par1));
        String var4 = this.func_96377_a(Request.func_96363_c(var3, ""));
        return Boolean.valueOf(var4);
    }

    public Boolean func_96376_d(long par1) throws ExceptionMcoService, IOException
    {
        String var3 = field_96388_b + "worlds" + "/$WORLD_ID/reset".replace("$WORLD_ID", String.valueOf(par1));
        String var4 = this.func_96377_a(Request.func_96353_a(var3, "", 30000, 80000));
        return Boolean.valueOf(var4);
    }

    public Locations func_96385_d() throws ExceptionMcoService, IOException
    {
        String var1 = this.func_96377_a(Request.func_96358_a(field_96388_b + "worlds" + "/locations"));
        return Locations.func_98174_a(var1);
    }

    public ValueObjectSubscription func_98177_f(long par1) throws ExceptionMcoService, IOException
    {
        String var3 = this.func_96377_a(Request.func_96358_a(field_96388_b + "subscriptions" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(par1))));
        return ValueObjectSubscription.func_98169_a(var3);
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
            McoPair var2 = (McoPair)field_98178_a.func_98155_a();
            par1Request.func_100006_a((String)var2.func_100005_a(), (String)var2.func_100004_b());
        }

        try
        {
            int var5 = par1Request.func_96362_a();

            if (var5 == 503)
            {
                throw new ExceptionRetryCall(10);
            }
            else if (var5 >= 200 && var5 < 300)
            {
                McoOption var3 = par1Request.func_98175_b();

                if (var3 instanceof McoOptionSome)
                {
                    field_98178_a = var3;
                }

                return par1Request.func_96364_c();
            }
            else
            {
                throw new ExceptionMcoService(par1Request.func_96362_a(), par1Request.func_96364_c());
            }
        }
        catch (ExceptionMcoHttp var4)
        {
            throw new ExceptionMcoService(500, "Server not available!");
        }
    }
}
