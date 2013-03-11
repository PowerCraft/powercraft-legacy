package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import net.minecraft.client.mco.McoServer;

@SideOnly(Side.CLIENT)
class ThreadConnectToOnlineServer extends Thread
{
    final McoServer field_96597_a;

    final GuiSlotOnlineServerList field_96596_b;

    ThreadConnectToOnlineServer(GuiSlotOnlineServerList par1GuiSlotOnlineServerList, McoServer par2McoServer)
    {
        this.field_96596_b = par1GuiSlotOnlineServerList;
        this.field_96597_a = par2McoServer;
    }

    public void run()
    {
        boolean flag = false;
        label183:
        {
            label184:
            {
                label185:
                {
                    label186:
                    {
                        label187:
                        {
                            try
                            {
                                flag = true;
                                long i = System.nanoTime();
                                GuiScreenOnlineServers.func_96179_a(this.field_96596_b.field_96294_a, this.field_96597_a);
                                long j = System.nanoTime();
                                this.field_96597_a.field_96412_m = (j - i) / 1000000L;
                                flag = false;
                                break label183;
                            }
                            catch (UnknownHostException unknownhostexception)
                            {
                                this.field_96597_a.field_96412_m = -1L;
                                flag = false;
                            }
                            catch (SocketTimeoutException sockettimeoutexception)
                            {
                                this.field_96597_a.field_96412_m = -1L;
                                flag = false;
                                break label187;
                            }
                            catch (ConnectException connectexception)
                            {
                                this.field_96597_a.field_96412_m = -1L;
                                flag = false;
                                break label186;
                            }
                            catch (IOException ioexception)
                            {
                                this.field_96597_a.field_96412_m = -1L;
                                flag = false;
                                break label185;
                            }
                            catch (Exception exception)
                            {
                                this.field_96597_a.field_96412_m = -1L;
                                flag = false;
                                break label184;
                            }
                            finally
                            {
                                if (flag)
                                {
                                    synchronized (GuiScreenOnlineServers.func_96170_h())
                                    {
                                        GuiScreenOnlineServers.func_96181_k();
                                    }
                                }
                            }

                            synchronized (GuiScreenOnlineServers.func_96170_h())
                            {
                                GuiScreenOnlineServers.func_96181_k();
                                return;
                            }
                        }

                        synchronized (GuiScreenOnlineServers.func_96170_h())
                        {
                            GuiScreenOnlineServers.func_96181_k();
                            return;
                        }
                    }

                    synchronized (GuiScreenOnlineServers.func_96170_h())
                    {
                        GuiScreenOnlineServers.func_96181_k();
                        return;
                    }
                }

                synchronized (GuiScreenOnlineServers.func_96170_h())
                {
                    GuiScreenOnlineServers.func_96181_k();
                    return;
                }
            }

            synchronized (GuiScreenOnlineServers.func_96170_h())
            {
                GuiScreenOnlineServers.func_96181_k();
                return;
            }
        }

        synchronized (GuiScreenOnlineServers.func_96170_h())
        {
            GuiScreenOnlineServers.func_96181_k();
        }
    }
}
