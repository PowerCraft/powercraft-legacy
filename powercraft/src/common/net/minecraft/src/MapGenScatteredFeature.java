package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

public class MapGenScatteredFeature extends MapGenStructure
{
    private static List biomelist = Arrays.asList(new BiomeGenBase[] {BiomeGenBase.desert, BiomeGenBase.desertHills, BiomeGenBase.jungle, BiomeGenBase.jungleHills, BiomeGenBase.swampland});
    private List field_82668_f;
    private int field_82669_g;
    private int field_82670_h;

    public MapGenScatteredFeature()
    {
        this.field_82668_f = new ArrayList();
        this.field_82669_g = 32;
        this.field_82670_h = 8;
        this.field_82668_f.add(new SpawnListEntry(EntityWitch.class, 1, 1, 1));
    }

    public MapGenScatteredFeature(Map par1Map)
    {
        this();
        Iterator var2 = par1Map.entrySet().iterator();

        while (var2.hasNext())
        {
            Entry var3 = (Entry)var2.next();

            if (((String)var3.getKey()).equals("distance"))
            {
                this.field_82669_g = MathHelper.func_82714_a((String)var3.getValue(), this.field_82669_g, this.field_82670_h + 1);
            }
        }
    }

    protected boolean canSpawnStructureAtCoords(int par1, int par2)
    {
        int var3 = par1;
        int var4 = par2;

        if (par1 < 0)
        {
            par1 -= this.field_82669_g - 1;
        }

        if (par2 < 0)
        {
            par2 -= this.field_82669_g - 1;
        }

        int var5 = par1 / this.field_82669_g;
        int var6 = par2 / this.field_82669_g;
        Random var7 = this.worldObj.setRandomSeed(var5, var6, 14357617);
        var5 *= this.field_82669_g;
        var6 *= this.field_82669_g;
        var5 += var7.nextInt(this.field_82669_g - this.field_82670_h);
        var6 += var7.nextInt(this.field_82669_g - this.field_82670_h);

        if (var3 == var5 && var4 == var6)
        {
            boolean var8 = this.worldObj.getWorldChunkManager().areBiomesViable(var3 * 16 + 8, var4 * 16 + 8, 0, biomelist);

            if (var8)
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

    public List func_82667_a()
    {
        return this.field_82668_f;
    }
}
