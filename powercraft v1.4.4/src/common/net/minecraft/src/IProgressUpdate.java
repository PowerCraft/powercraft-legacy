package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public interface IProgressUpdate
{
    void displayProgressMessage(String var1);

    @SideOnly(Side.CLIENT)

    void resetProgressAndMessage(String var1);

    void resetProgresAndWorkingMessage(String var1);

    void setLoadingProgress(int var1);

    @SideOnly(Side.CLIENT)

    void onNoMoreProgress();
}
