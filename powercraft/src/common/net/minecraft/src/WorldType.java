package net.minecraft.src;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Sets;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class WorldType
{
    public static final BiomeGenBase[] base11Biomes = new BiomeGenBase[] {BiomeGenBase.desert, BiomeGenBase.forest, BiomeGenBase.extremeHills, BiomeGenBase.swampland, BiomeGenBase.plains, BiomeGenBase.taiga};
    public static final BiomeGenBase[] base12Biomes = ObjectArrays.concat(base11Biomes, BiomeGenBase.jungle);

    /** List of world types. */
    public static final WorldType[] worldTypes = new WorldType[16];

    /** Default world type. */
    public static final WorldType DEFAULT = (new WorldType(0, "default", 1)).setVersioned();

    /** Flat world type. */
    public static final WorldType FLAT = new WorldType(1, "flat");

    /** Large Biome world Type. */
    public static final WorldType LARGE_BIOMES = new WorldType(2, "largeBiomes");

    /** Default (1.1) world type. */
    public static final WorldType DEFAULT_1_1 = (new WorldType(8, "default_1_1", 0)).setCanBeCreated(false);
    private final int field_82748_f;

    /** 'default' or 'flat' */
    private final String worldType;

    /** The int version of the ChunkProvider that generated this world. */
    private final int generatorVersion;

    /**
     * Whether this world type can be generated. Normally true; set to false for out-of-date generator versions.
     */
    private boolean canBeCreated;

    /** Whether this WorldType has a version or not. */
    private boolean isWorldTypeVersioned;

    protected BiomeGenBase[] biomesForWorldType;

    public WorldType(int par1, String par2Str)
    {
        this(par1, par2Str, 0);
    }

    public WorldType(int par1, String par2Str, int par3)
    {
        this.worldType = par2Str;
        this.generatorVersion = par3;
        this.canBeCreated = true;
        this.field_82748_f = par1;
        worldTypes[par1] = this;
        switch (par1)
        {
        case 8:
            biomesForWorldType = base11Biomes;
            break;
        default:
            biomesForWorldType = base12Biomes;
        }
    }

    public String getWorldTypeName()
    {
        return this.worldType;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets the translation key for the name of this world type.
     */
    public String getTranslateName()
    {
        return "generator." + this.worldType;
    }

    /**
     * Returns generatorVersion.
     */
    public int getGeneratorVersion()
    {
        return this.generatorVersion;
    }

    public WorldType getWorldTypeForGeneratorVersion(int par1)
    {
        return this == DEFAULT && par1 == 0 ? DEFAULT_1_1 : this;
    }

    /**
     * Sets canBeCreated to the provided value, and returns this.
     */
    private WorldType setCanBeCreated(boolean par1)
    {
        this.canBeCreated = par1;
        return this;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets whether this WorldType can be used to generate a new world.
     */
    public boolean getCanBeCreated()
    {
        return this.canBeCreated;
    }

    /**
     * Flags this world type as having an associated version.
     */
    private WorldType setVersioned()
    {
        this.isWorldTypeVersioned = true;
        return this;
    }

    /**
     * Returns true if this world Type has a version associated with it.
     */
    public boolean isVersioned()
    {
        return this.isWorldTypeVersioned;
    }

    public static WorldType parseWorldType(String par0Str)
    {
        WorldType[] var1 = worldTypes;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            WorldType var4 = var1[var3];

            if (var4 != null && var4.worldType.equalsIgnoreCase(par0Str))
            {
                return var4;
            }
        }

        return null;
    }

    @SideOnly(Side.CLIENT)
    public int func_82747_f()
    {
        return this.field_82748_f;
    }

    public WorldChunkManager getChunkManager(World world)
    {
        if (this == FLAT)
        {
            FlatGeneratorInfo var1 = FlatGeneratorInfo.func_82651_a(world.getWorldInfo().func_82571_y());
            return new WorldChunkManagerHell(BiomeGenBase.biomeList[var1.func_82648_a()], 0.5F, 0.5F);
        }
        else
        {
            return new WorldChunkManager(world);
        }
    }

    public IChunkProvider getChunkGenerator(World world, String generatorOptions)
    { 
        return (this == FLAT ? new ChunkProviderFlat(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), generatorOptions) : new ChunkProviderGenerate(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled()));
    }

    public int getMinimumSpawnHeight(World world)
    {
        return this == FLAT ? 4 : 64;
    }

    public double getHorizon(World world)
    {
        return this == FLAT ? 0.0D : 63.0D;
    }

    public boolean hasVoidParticles(boolean var1)
    {
        return this != FLAT && !var1;
    }

    public double voidFadeMagnitude()
    {
        return this == FLAT ? 1.0D : 0.03125D;
    }

    public BiomeGenBase[] getBiomesForWorldType() {
        return biomesForWorldType;
    }

    public void addNewBiome(BiomeGenBase biome)
    {
        Set<BiomeGenBase> newBiomesForWorld = Sets.newLinkedHashSet(Arrays.asList(biomesForWorldType));
        newBiomesForWorld.add(biome);
        biomesForWorldType = newBiomesForWorld.toArray(new BiomeGenBase[0]);
    }

    public void removeBiome(BiomeGenBase biome)
    {
        Set<BiomeGenBase> newBiomesForWorld = Sets.newLinkedHashSet(Arrays.asList(biomesForWorldType));
        newBiomesForWorld.remove(biome);
        biomesForWorldType = newBiomesForWorld.toArray(new BiomeGenBase[0]);
    }

    public boolean handleSlimeSpawnReduction(Random random, World world)
    {
        return this == FLAT ? random.nextInt(4) != 1 : false;
    }

    /**
     * Called when 'Create New World' button is pressed before starting game
     */
    public void onGUICreateWorldPress() { }

    /**
     * Gets the spawn fuzz for players who join the world.
     * Useful for void world types.
     * @return Fuzz for entity initial spawn in blocks.
     */
    public int getSpawnFuzz()
    {
        return 20;
    }
}
