package net.minecraft.src;

import java.util.HashMap;
import java.util.TimerTask;

class PlayerUsageSnooperThread extends TimerTask
{
    /** The PlayerUsageSnooper object. */
    final PlayerUsageSnooper snooper;

    PlayerUsageSnooperThread(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        this.snooper = par1PlayerUsageSnooper;
    }

    public void run()
    {
        if (PlayerUsageSnooper.func_76473_a(this.snooper).isSnooperEnabled())
        {
            HashMap var1;

            synchronized (PlayerUsageSnooper.func_76474_b(this.snooper))
            {
                var1 = new HashMap(PlayerUsageSnooper.func_76469_c(this.snooper));
            }

            var1.put("snooper_count", Integer.valueOf(PlayerUsageSnooper.func_76466_d(this.snooper)));
            HttpUtil.sendPost(PlayerUsageSnooper.func_76475_e(this.snooper), var1, true);
        }
    }
}
