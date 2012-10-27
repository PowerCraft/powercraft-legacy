package net.minecraft.src;

import java.util.Random;

public class StructureScatteredFeatureStart extends StructureStart
{
    public StructureScatteredFeatureStart(World par1World, Random par2Random, int par3, int par4)
    {
        if (par1World.getBiomeGenForCoords(par3 * 16 + 8, par4 * 16 + 8) == BiomeGenBase.jungle)
        {
            ComponentScatteredFeatureJunglePyramid var5 = new ComponentScatteredFeatureJunglePyramid(par2Random, par3 * 16, par4 * 16);
            this.components.add(var5);
        }
        else
        {
            ComponentScatteredFeatureDesertPyramid var6 = new ComponentScatteredFeatureDesertPyramid(par2Random, par3 * 16, par4 * 16);
            this.components.add(var6);
        }

        this.updateBoundingBox();
    }
}
