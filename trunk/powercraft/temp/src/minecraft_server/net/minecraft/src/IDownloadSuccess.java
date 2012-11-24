package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.File;

@SideOnly(Side.CLIENT)
public interface IDownloadSuccess {

   void func_76170_a(File var1);
}
