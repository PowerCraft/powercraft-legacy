package net.minecraft.src;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.UUID;

public class PlayerUsageSnooper
{
    /** String map for report data */
    private Map dataMap = new HashMap();
    private final String field_76480_b = UUID.randomUUID().toString();

    /** URL of the server to send the report to */
    private final URL serverUrl;
    private final IPlayerUsage field_76478_d;
    private final Timer field_76479_e = new Timer("Snooper Timer", true);
    private final Object field_76476_f = new Object();
    private boolean field_76477_g = false;
    private int field_76483_h = 0;

    public PlayerUsageSnooper(String par1Str, IPlayerUsage par2IPlayerUsage)
    {
        try
        {
            this.serverUrl = new URL("http://snoop.minecraft.net/" + par1Str + "?version=" + 1);
        }
        catch (MalformedURLException var4)
        {
            throw new IllegalArgumentException();
        }

        this.field_76478_d = par2IPlayerUsage;
    }

    public void func_76463_a()
    {
        if (!this.field_76477_g)
        {
            this.field_76477_g = true;
            this.func_76464_f();
            this.field_76479_e.schedule(new PlayerUsageSnooperThread(this), 0L, 900000L);
        }
    }

    private void func_76464_f()
    {
        this.func_76467_g();
        this.addData("snooper_token", this.field_76480_b);
        this.addData("os_name", System.getProperty("os.name"));
        this.addData("os_version", System.getProperty("os.version"));
        this.addData("os_architecture", System.getProperty("os.arch"));
        this.addData("java_version", System.getProperty("java.version"));
        this.addData("version", "1.3.2");
        this.field_76478_d.func_70001_b(this);
    }

    private void func_76467_g()
    {
        RuntimeMXBean var1 = ManagementFactory.getRuntimeMXBean();
        List var2 = var1.getInputArguments();
        int var3 = 0;
        Iterator var4 = var2.iterator();

        while (var4.hasNext())
        {
            String var5 = (String)var4.next();

            if (var5.startsWith("-X"))
            {
                this.addData("jvm_arg[" + var3++ + "]", var5);
            }
        }

        this.addData("jvm_args", Integer.valueOf(var3));
    }

    public void func_76471_b()
    {
        this.addData("memory_total", Long.valueOf(Runtime.getRuntime().totalMemory()));
        this.addData("memory_max", Long.valueOf(Runtime.getRuntime().maxMemory()));
        this.addData("memory_free", Long.valueOf(Runtime.getRuntime().freeMemory()));
        this.addData("cpu_cores", Integer.valueOf(Runtime.getRuntime().availableProcessors()));
        this.field_76478_d.func_70000_a(this);
    }

    /**
     * Adds information to the report
     */
    public void addData(String par1Str, Object par2Obj)
    {
        Object var3 = this.field_76476_f;

        synchronized (this.field_76476_f)
        {
            this.dataMap.put(par1Str, par2Obj);
        }
    }

    public boolean func_76468_d()
    {
        return this.field_76477_g;
    }

    public void func_76470_e()
    {
        this.field_76479_e.cancel();
    }

    static IPlayerUsage func_76473_a(PlayerUsageSnooper par0PlayerUsageSnooper)
    {
        return par0PlayerUsageSnooper.field_76478_d;
    }

    static Object func_76474_b(PlayerUsageSnooper par0PlayerUsageSnooper)
    {
        return par0PlayerUsageSnooper.field_76476_f;
    }

    static Map func_76469_c(PlayerUsageSnooper par0PlayerUsageSnooper)
    {
        return par0PlayerUsageSnooper.dataMap;
    }

    static int func_76466_d(PlayerUsageSnooper par0PlayerUsageSnooper)
    {
        return par0PlayerUsageSnooper.field_76483_h++;
    }

    static URL func_76475_e(PlayerUsageSnooper par0PlayerUsageSnooper)
    {
        return par0PlayerUsageSnooper.serverUrl;
    }
}
