package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.File;

@SideOnly(Side.CLIENT)
public interface IDownloadSuccess
{
    void onSuccess(File var1);
}
