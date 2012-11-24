package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.AxisAlignedBB;

@SideOnly(Side.CLIENT)
public interface ICamera {

   boolean func_78546_a(AxisAlignedBB var1);

   void func_78547_a(double var1, double var3, double var5);
}
