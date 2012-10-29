package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class LanServerList
{
    private ArrayList field_77555_b = new ArrayList();
    boolean field_77556_a;

    public synchronized boolean func_77553_a()
    {
        return this.field_77556_a;
    }

    public synchronized void func_77552_b()
    {
        this.field_77556_a = false;
    }

    public synchronized List func_77554_c()
    {
        return Collections.unmodifiableList(this.field_77555_b);
    }

    public synchronized void func_77551_a(String par1Str, InetAddress par2InetAddress)
    {
        String var3 = ThreadLanServerPing.func_77524_a(par1Str);
        String var4 = ThreadLanServerPing.func_77523_b(par1Str);

        if (var4 != null)
        {
            boolean var5 = false;
            Iterator var6 = this.field_77555_b.iterator();

            while (var6.hasNext())
            {
                LanServer var7 = (LanServer)var6.next();

                if (var7.func_77488_b().equals(var4))
                {
                    var7.updateLastSeen();
                    var5 = true;
                    break;
                }
            }

            if (!var5)
            {
                this.field_77555_b.add(new LanServer(var3, var4));
                this.field_77556_a = true;
            }
        }
    }
}
