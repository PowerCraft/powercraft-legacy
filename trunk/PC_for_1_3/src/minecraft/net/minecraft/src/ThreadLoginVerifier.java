package net.minecraft.src;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;

class ThreadLoginVerifier extends Thread
{
    final NetLoginHandler field_72590_a;

    ThreadLoginVerifier(NetLoginHandler par1NetLoginHandler)
    {
        this.field_72590_a = par1NetLoginHandler;
    }

    public void run()
    {
        try
        {
            String var1 = (new BigInteger(CryptManager.func_75895_a(NetLoginHandler.func_72526_a(this.field_72590_a), NetLoginHandler.func_72530_b(this.field_72590_a).getKeyPair().getPublic(), NetLoginHandler.func_72525_c(this.field_72590_a)))).toString(16);
            URL var2 = new URL("http://session.minecraft.net/game/checkserver.jsp?user=" + URLEncoder.encode(NetLoginHandler.func_72533_d(this.field_72590_a), "UTF-8") + "&serverId=" + URLEncoder.encode(var1, "UTF-8"));
            BufferedReader var3 = new BufferedReader(new InputStreamReader(var2.openStream()));
            String var4 = var3.readLine();
            var3.close();

            if (!"YES".equals(var4))
            {
                this.field_72590_a.raiseErrorAndDisconnect("Failed to verify username!");
                return;
            }

            NetLoginHandler.func_72531_a(this.field_72590_a, true);
        }
        catch (Exception var5)
        {
            this.field_72590_a.raiseErrorAndDisconnect("Failed to verify username! [internal error " + var5 + "]");
            var5.printStackTrace();
        }
    }
}
