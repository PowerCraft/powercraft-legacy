package net.minecraft.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class DedicatedServerCommandThread extends Thread
{
    final DedicatedServer field_72428_a;

    DedicatedServerCommandThread(DedicatedServer par1DedicatedServer)
    {
        this.field_72428_a = par1DedicatedServer;
    }

    public void run()
    {
        BufferedReader var1 = new BufferedReader(new InputStreamReader(System.in));
        String var2;

        try
        {
            while (!this.field_72428_a.func_71241_aa() && this.field_72428_a.func_71278_l() && (var2 = var1.readLine()) != null)
            {
                this.field_72428_a.func_71331_a(var2, this.field_72428_a);
            }
        }
        catch (IOException var4)
        {
            var4.printStackTrace();
        }
    }
}
