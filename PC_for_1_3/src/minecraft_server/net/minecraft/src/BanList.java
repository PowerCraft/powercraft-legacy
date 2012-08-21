package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BanList
{
    private final LowerStringMap field_73715_a = new LowerStringMap();
    private final File field_73713_b;
    private boolean field_73714_c = true;

    public BanList(File par1File)
    {
        this.field_73713_b = par1File;
    }

    public boolean func_73710_b()
    {
        return this.field_73714_c;
    }

    public void setListActive(boolean par1)
    {
        this.field_73714_c = par1;
    }

    public Map func_73712_c()
    {
        this.func_73705_d();
        return this.field_73715_a;
    }

    public boolean func_73704_a(String par1Str)
    {
        if (!this.func_73710_b())
        {
            return false;
        }
        else
        {
            this.func_73705_d();
            return this.field_73715_a.containsKey(par1Str);
        }
    }

    public void func_73706_a(BanEntry par1BanEntry)
    {
        this.field_73715_a.func_76116_a(par1BanEntry.func_73684_a(), par1BanEntry);
        this.saveToFileWithHeader();
    }

    public void func_73709_b(String par1Str)
    {
        this.field_73715_a.remove(par1Str);
        this.saveToFileWithHeader();
    }

    public void func_73705_d()
    {
        Iterator var1 = this.field_73715_a.values().iterator();

        while (var1.hasNext())
        {
            BanEntry var2 = (BanEntry)var1.next();

            if (var2.func_73682_e())
            {
                var1.remove();
            }
        }
    }

    /**
     * Loads the ban list from the file (adds every entry, does not clear the current list).
     */
    public void loadBanList()
    {
        if (this.field_73713_b.isFile())
        {
            BufferedReader var1;

            try
            {
                var1 = new BufferedReader(new FileReader(this.field_73713_b));
            }
            catch (FileNotFoundException var4)
            {
                throw new Error();
            }

            String var2;

            try
            {
                while ((var2 = var1.readLine()) != null)
                {
                    if (!var2.startsWith("#"))
                    {
                        BanEntry var3 = BanEntry.func_73688_c(var2);

                        if (var3 != null)
                        {
                            this.field_73715_a.func_76116_a(var3.func_73684_a(), var3);
                        }
                    }
                }
            }
            catch (IOException var5)
            {
                Logger.getLogger("Minecraft").log(Level.SEVERE, "Could not load ban list", var5);
            }
        }
    }

    public void saveToFileWithHeader()
    {
        this.func_73703_b(true);
    }

    public void func_73703_b(boolean par1)
    {
        this.func_73705_d();

        try
        {
            PrintWriter var2 = new PrintWriter(new FileWriter(this.field_73713_b, false));

            if (par1)
            {
                var2.println("# Updated " + (new SimpleDateFormat()).format(new Date()) + " by Minecraft " + "1.3.2");
                var2.println("# victim name | ban date | banned by | banned until | reason");
                var2.println();
            }

            Iterator var3 = this.field_73715_a.values().iterator();

            while (var3.hasNext())
            {
                BanEntry var4 = (BanEntry)var3.next();
                var2.println(var4.func_73685_g());
            }

            var2.close();
        }
        catch (IOException var5)
        {
            Logger.getLogger("Minecraft").log(Level.SEVERE, "Could not save ban list", var5);
        }
    }
}
