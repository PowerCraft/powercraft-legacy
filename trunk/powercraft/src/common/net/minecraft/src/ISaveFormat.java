package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;

public interface ISaveFormat
{
    ISaveHandler getSaveLoader(String var1, boolean var2);

    @SideOnly(Side.CLIENT)
    List getSaveList();

    void flushCache();

    @SideOnly(Side.CLIENT)

    WorldInfo getWorldInfo(String var1);

    boolean deleteWorldDirectory(String var1);

    @SideOnly(Side.CLIENT)

    void renameWorld(String var1, String var2);

    boolean isOldMapFormat(String var1);

    boolean convertMapFormat(String var1, IProgressUpdate var2);

    @SideOnly(Side.CLIENT)
    boolean func_90033_f(String var1);
}
