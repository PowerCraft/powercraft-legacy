package net.minecraft.src;

import java.util.Random;

public class RandomPositionGenerator
{
    private static Vec3 field_75465_a = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);

    public static Vec3 func_75463_a(EntityCreature par0EntityCreature, int par1, int par2)
    {
        return func_75462_c(par0EntityCreature, par1, par2, (Vec3)null);
    }

    public static Vec3 func_75464_a(EntityCreature par0EntityCreature, int par1, int par2, Vec3 par3Vec3)
    {
        field_75465_a.xCoord = par3Vec3.xCoord - par0EntityCreature.posX;
        field_75465_a.yCoord = par3Vec3.yCoord - par0EntityCreature.posY;
        field_75465_a.zCoord = par3Vec3.zCoord - par0EntityCreature.posZ;
        return func_75462_c(par0EntityCreature, par1, par2, field_75465_a);
    }

    public static Vec3 func_75461_b(EntityCreature par0EntityCreature, int par1, int par2, Vec3 par3Vec3)
    {
        field_75465_a.xCoord = par0EntityCreature.posX - par3Vec3.xCoord;
        field_75465_a.yCoord = par0EntityCreature.posY - par3Vec3.yCoord;
        field_75465_a.zCoord = par0EntityCreature.posZ - par3Vec3.zCoord;
        return func_75462_c(par0EntityCreature, par1, par2, field_75465_a);
    }

    private static Vec3 func_75462_c(EntityCreature par0EntityCreature, int par1, int par2, Vec3 par3Vec3)
    {
        Random var4 = par0EntityCreature.getRNG();
        boolean var5 = false;
        int var6 = 0;
        int var7 = 0;
        int var8 = 0;
        float var9 = -99999.0F;
        boolean var10;

        if (par0EntityCreature.hasHome())
        {
            double var11 = (double)(par0EntityCreature.getHomePosition().getDistanceSquared(MathHelper.floor_double(par0EntityCreature.posX), MathHelper.floor_double(par0EntityCreature.posY), MathHelper.floor_double(par0EntityCreature.posZ)) + 4.0F);
            double var13 = (double)(par0EntityCreature.getMaximumHomeDistance() + (float)par1);
            var10 = var11 < var13 * var13;
        }
        else
        {
            var10 = false;
        }

        for (int var16 = 0; var16 < 10; ++var16)
        {
            int var12 = var4.nextInt(2 * par1) - par1;
            int var17 = var4.nextInt(2 * par2) - par2;
            int var14 = var4.nextInt(2 * par1) - par1;

            if (par3Vec3 == null || (double)var12 * par3Vec3.xCoord + (double)var14 * par3Vec3.zCoord >= 0.0D)
            {
                var12 += MathHelper.floor_double(par0EntityCreature.posX);
                var17 += MathHelper.floor_double(par0EntityCreature.posY);
                var14 += MathHelper.floor_double(par0EntityCreature.posZ);

                if (!var10 || par0EntityCreature.isWithinHomeDistance(var12, var17, var14))
                {
                    float var15 = par0EntityCreature.getBlockPathWeight(var12, var17, var14);

                    if (var15 > var9)
                    {
                        var9 = var15;
                        var6 = var12;
                        var7 = var17;
                        var8 = var14;
                        var5 = true;
                    }
                }
            }
        }

        if (var5)
        {
            return Vec3.func_72437_a().func_72345_a((double)var6, (double)var7, (double)var8);
        }
        else
        {
            return null;
        }
    }
}
