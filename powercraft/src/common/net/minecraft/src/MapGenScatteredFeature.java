package net.minecraft.src;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MapGenScatteredFeature extends MapGenStructure
{
    private static List biomelist = Arrays.asList(new BiomeGenBase[] {BiomeGenBase.desert, BiomeGenBase.desertHills, BiomeGenBase.jungle});

    protected boolean canSpawnStructureAtCoords(int par1, int par2)
    {
        byte var3 = 32;
        byte var4 = 8;
        int var5 = par1;
        int var6 = par2;

        if (par1 < 0)
        {
            par1 -= var3 - 1;
        }

        if (par2 < 0)
        {
            par2 -= var3 - 1;
        }

        int var7 = par1 / var3;
        int var8 = par2 / var3;
        Random var9 = this.worldObj.setRandomSeed(var7, var8, 14357617);
        var7 *= var3;
        var8 *= var3;
        var7 += var9.nextInt(var3 - var4);
        var8 += var9.nextInt(var3 - var4);

        if (var5 == var7 && var6 == var8)
        {
            boolean var10 = this.worldObj.getWorldChunkManager().areBiomesViable(var5 * 16 + 8, var6 * 16 + 8, 0, biomelist);

            if (var10)
            {
                return true;
            }
        }

        return false;
    }

    protected StructureStart getStructureStart(int par1, int par2)
    {
        return new StructureScatteredFeatureStart(this.worldObj, this.rand, par1, par2);
    }
}
