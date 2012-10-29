package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)

public class EnumOSHelper
{
    public static final int[] enumOSMappingHelperArray = new int[EnumOS.values().length];

    static
    {
        try
        {
            enumOSMappingHelperArray[EnumOS.LINUX.ordinal()] = 1;
        }
        catch (NoSuchFieldError var4)
        {
            ;
        }

        try
        {
            enumOSMappingHelperArray[EnumOS.SOLARIS.ordinal()] = 2;
        }
        catch (NoSuchFieldError var3)
        {
            ;
        }

        try
        {
            enumOSMappingHelperArray[EnumOS.WINDOWS.ordinal()] = 3;
        }
        catch (NoSuchFieldError var2)
        {
            ;
        }

        try
        {
            enumOSMappingHelperArray[EnumOS.MACOS.ordinal()] = 4;
        }
        catch (NoSuchFieldError var1)
        {
            ;
        }
    }
}
