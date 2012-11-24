package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.awt.image.BufferedImage;

@SideOnly(Side.CLIENT)
public interface IImageBuffer {

   BufferedImage func_78432_a(BufferedImage var1);
}
