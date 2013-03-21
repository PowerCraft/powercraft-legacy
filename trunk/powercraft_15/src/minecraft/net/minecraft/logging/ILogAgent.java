package net.minecraft.logging;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.logging.Logger;

public interface ILogAgent
{
    void logInfo(String s);

    @SideOnly(Side.SERVER)
    Logger func_98076_a();

    void logWarning(String s);

    void func_98231_b(String s, Object ... var2);

    void func_98235_b(String s, Throwable throwable);

    void func_98232_c(String s);

    void func_98234_c(String s, Throwable throwable);

    @SideOnly(Side.CLIENT)
    void func_98230_d(String s);
}
