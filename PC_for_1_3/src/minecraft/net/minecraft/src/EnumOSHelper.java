package net.minecraft.src;

public class EnumOSHelper
{
    public static final int[] field_74533_a = new int[EnumOS.values().length];

    static
    {
        try
        {
            field_74533_a[EnumOS.LINUX.ordinal()] = 1;
        }
        catch (NoSuchFieldError var4)
        {
            ;
        }

        try
        {
            field_74533_a[EnumOS.SOLARIS.ordinal()] = 2;
        }
        catch (NoSuchFieldError var3)
        {
            ;
        }

        try
        {
            field_74533_a[EnumOS.WINDOWS.ordinal()] = 3;
        }
        catch (NoSuchFieldError var2)
        {
            ;
        }

        try
        {
            field_74533_a[EnumOS.MACOS.ordinal()] = 4;
        }
        catch (NoSuchFieldError var1)
        {
            ;
        }
    }
}
