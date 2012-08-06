package net.minecraft.src;

public class RConUtils
{
    public static char[] field_72666_a = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String func_72661_a(byte[] par0ArrayOfByte, int par1, int par2)
    {
        int var3 = par2 - 1;
        int var4;

        for (var4 = par1 > var3 ? var3 : par1; 0 != par0ArrayOfByte[var4] && var4 < var3; ++var4)
        {
            ;
        }

        return new String(par0ArrayOfByte, par1, var4 - par1);
    }

    public static int func_72662_b(byte[] par0ArrayOfByte, int par1)
    {
        return func_72665_b(par0ArrayOfByte, par1, par0ArrayOfByte.length);
    }

    public static int func_72665_b(byte[] par0ArrayOfByte, int par1, int par2)
    {
        return 0 > par2 - par1 - 4 ? 0 : par0ArrayOfByte[par1 + 3] << 24 | (par0ArrayOfByte[par1 + 2] & 255) << 16 | (par0ArrayOfByte[par1 + 1] & 255) << 8 | par0ArrayOfByte[par1] & 255;
    }

    public static int func_72664_c(byte[] par0ArrayOfByte, int par1, int par2)
    {
        return 0 > par2 - par1 - 4 ? 0 : par0ArrayOfByte[par1] << 24 | (par0ArrayOfByte[par1 + 1] & 255) << 16 | (par0ArrayOfByte[par1 + 2] & 255) << 8 | par0ArrayOfByte[par1 + 3] & 255;
    }

    public static String func_72663_a(byte par0)
    {
        return "" + field_72666_a[(par0 & 240) >>> 4] + field_72666_a[par0 & 15];
    }
}
