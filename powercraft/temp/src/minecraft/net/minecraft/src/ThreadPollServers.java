package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import net.minecraft.src.GuiMultiplayer;
import net.minecraft.src.GuiSlotServer;
import net.minecraft.src.ServerData;

@SideOnly(Side.CLIENT)
class ThreadPollServers extends Thread {

   // $FF: synthetic field
   final ServerData field_78318_a;
   // $FF: synthetic field
   final GuiSlotServer field_78317_b;


   ThreadPollServers(GuiSlotServer p_i3040_1_, ServerData p_i3040_2_) {
      this.field_78317_b = p_i3040_1_;
      this.field_78318_a = p_i3040_2_;
   }

   public void run() {
      boolean var27 = false;

      label183: {
         label184: {
            label185: {
               label186: {
                  label187: {
                     try {
                        var27 = true;
                        this.field_78318_a.field_78843_d = "\u00a78Polling..";
                        long var1 = System.nanoTime();
                        GuiMultiplayer.func_82291_a(this.field_78318_a);
                        long var3 = System.nanoTime();
                        this.field_78318_a.field_78844_e = (var3 - var1) / 1000000L;
                        var27 = false;
                        break label183;
                     } catch (UnknownHostException var35) {
                        this.field_78318_a.field_78844_e = -1L;
                        this.field_78318_a.field_78843_d = "\u00a74Can\'t resolve hostname";
                        var27 = false;
                     } catch (SocketTimeoutException var36) {
                        this.field_78318_a.field_78844_e = -1L;
                        this.field_78318_a.field_78843_d = "\u00a74Can\'t reach server";
                        var27 = false;
                        break label187;
                     } catch (ConnectException var37) {
                        this.field_78318_a.field_78844_e = -1L;
                        this.field_78318_a.field_78843_d = "\u00a74Can\'t reach server";
                        var27 = false;
                        break label186;
                     } catch (IOException var38) {
                        this.field_78318_a.field_78844_e = -1L;
                        this.field_78318_a.field_78843_d = "\u00a74Communication error";
                        var27 = false;
                        break label185;
                     } catch (Exception var39) {
                        this.field_78318_a.field_78844_e = -1L;
                        this.field_78318_a.field_78843_d = "ERROR: " + var39.getClass();
                        var27 = false;
                        break label184;
                     } finally {
                        if(var27) {
                           synchronized(GuiMultiplayer.func_74011_h()) {
                              GuiMultiplayer.func_74018_k();
                           }
                        }
                     }

                     synchronized(GuiMultiplayer.func_74011_h()) {
                        GuiMultiplayer.func_74018_k();
                        return;
                     }
                  }

                  synchronized(GuiMultiplayer.func_74011_h()) {
                     GuiMultiplayer.func_74018_k();
                     return;
                  }
               }

               synchronized(GuiMultiplayer.func_74011_h()) {
                  GuiMultiplayer.func_74018_k();
                  return;
               }
            }

            synchronized(GuiMultiplayer.func_74011_h()) {
               GuiMultiplayer.func_74018_k();
               return;
            }
         }

         synchronized(GuiMultiplayer.func_74011_h()) {
            GuiMultiplayer.func_74018_k();
            return;
         }
      }

      synchronized(GuiMultiplayer.func_74011_h()) {
         GuiMultiplayer.func_74018_k();
      }

   }
}
