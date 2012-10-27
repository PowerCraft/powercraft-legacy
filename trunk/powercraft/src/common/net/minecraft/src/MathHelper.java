package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Random;

public class MathHelper
{
    /**
     * A table of sin values computed from 0 (inclusive) to 2*pi (exclusive), with steps of 2*PI / 65536.
     */
    private static float[] SIN_TABLE = new float[65536];

    /**
     * sin looked up in a table
     */
    public static final float sin(float par0)
    {
        return SIN_TABLE[(int)(par0 * 10430.378F) & 65535];
    }

    /**
     * cos looked up in the sin table with the appropriate offset
     */
    public static final float cos(float par0)
    {
        return SIN_TABLE[(int)(par0 * 10430.378F + 16384.0F) & 65535];
    }

    public static final float sqrt_float(float par0)
    {
        return (float)Math.sqrt((double)par0);
    }

    public static final float sqrt_double(double par0)
    {
        return (float)Math.sqrt(par0);
    }

    /**
     * Returns the greatest integer less than or equal to the float argument
     */
    public static int floor_float(float par0)
    {
        int var1 = (int)par0;
        return par0 < (float)var1 ? var1 - 1 : var1;
    }

    @SideOnly(Side.CLIENT)

    /**
     * returns par0 cast as an int, and no greater than Integer.MAX_VALUE-1024
     */
    public static int truncateDoubleToInt(double par0)
    {
        return (int)(par0 + 1024.0D) - 1024;
    }

    /**
     * Returns the greatest integer less than or equal to the double argument
     */
    public static int floor_double(double par0)
    {
        int var2 = (int)par0;
        return par0 < (double)var2 ? var2 - 1 : var2;
    }

    /**
     * Long version of floor_double
     */
    public static long floor_double_long(double par0)
    {
        long var2 = (long)par0;
        return par0 < (double)var2 ? var2 - 1L : var2;
    }

    public static float abs(float par0)
    {
        return par0 >= 0.0F ? par0 : -par0;
    }

    public static int abs_int(int par0)
    {
        return par0 >= 0 ? par0 : -par0;
    }

    public static int ceiling_float_int(float par0)
    {
        int var1 = (int)par0;
        return par0 > (float)var1 ? var1 + 1 : var1;
    }

    public static int ceiling_double_int(double par0)
    {
        int var2 = (int)par0;
        return par0 > (double)var2 ? var2 + 1 : var2;
    }

    /**
     * Returns the value of the first parameter, clamped to be within the lower and upper limits given by the second and
     * third parameters.
     */
    public static int clamp_int(int par0, int par1, int par2)
    {
        return par0 < par1 ? par1 : (par0 > par2 ? par2 : par0);
    }

    @SideOnly(Side.CLIENT)

    /**
     * Returns the value of the first parameter, clamped to be within the lower and upper limits given by the second and
     * third parameters
     */
    public static float clamp_float(float par0, float par1, float par2)
    {
        return par0 < par1 ? par1 : (par0 > par2 ? par2 : par0);
    }

    /**
     * Maximum of the absolute value of two numbers.
     */
    public static double abs_max(double par0, double par2)
    {
        if (par0 < 0.0D)
        {
            par0 = -par0;
        }

        if (par2 < 0.0D)
        {
            par2 = -par2;
        }

        return par0 > par2 ? par0 : par2;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Buckets an integer with specifed bucket sizes.  Args: i, bucketSize
     */
    public static int bucketInt(int par0, int par1)
    {
        return par0 < 0 ? -((-par0 - 1) / par1) - 1 : par0 / par1;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Tests if a string is null or of length zero
     */
    public static boolean stringNullOrLengthZero(String par0Str)
    {
        return par0Str == null || par0Str.length() == 0;
    }

    public static int getRandomIntegerInRange(Random par0Random, int par1, int par2)
    {
        return par1 >= par2 ? par1 : par0Random.nextInt(par2 - par1 + 1) + par1;
    }

    public static double average(long[] par0ArrayOfLong)
    {
        long var1 = 0L;
        long[] var3 = par0ArrayOfLong;
        int var4 = par0ArrayOfLong.length;

        for (int var5 = 0; var5 < var4; ++var5)
        {
            long var6 = var3[var5];
            var1 += var6;
        }

        return (double)var1 / (double)par0ArrayOfLong.length;
    }

    /**
     * the angle is reduced to an angle between -180 and +180 by mod, and a 360 check
     */
    public static float wrapAngleTo180_float(float par0)
    {
        par0 %= 360.0F;

        if (par0 >= 180.0F)
        {
            par0 -= 360.0F;
        }

        if (par0 < -180.0F)
        {
            par0 += 360.0F;
        }

        return par0;
    }

    /**
     * the angle is reduced to an angle between -180 and +180 by mod, and a 360 check
     */
    public static double wrapAngleTo180_double(double par0)
    {
        par0 %= 360.0D;

        if (par0 >= 180.0D)
        {
            par0 -= 360.0D;
        }

        if (par0 < -180.0D)
        {
            par0 += 360.0D;
        }

        return par0;
    }

    static
    {
        for (int var0 = 0; var0 < 65536; ++var0)
        {
            SIN_TABLE[var0] = (float)Math.sin((double)var0 * Math.PI * 2.0D / 65536.0D);
        }
    }
}
