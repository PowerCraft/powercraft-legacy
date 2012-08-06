package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class ComponentVillageStartPiece extends ComponentVillageWell
{
    public final WorldChunkManager worldChunkMngr;
    public final boolean field_74927_b;

    /** World terrain type, 0 for normal, 1 for flap map */
    public final int terrainType;
    public StructureVillagePieceWeight structVillagePieceWeight;

    /**
     * Contains List of all spawnable Structure Piece Weights. If no more Pieces of a type can be spawned, they are
     * removed from this list
     */
    public ArrayList structureVillageWeightedPieceList;
    public ArrayList field_74932_i = new ArrayList();
    public ArrayList field_74930_j = new ArrayList();

    public ComponentVillageStartPiece(WorldChunkManager par1WorldChunkManager, int par2, Random par3Random, int par4, int par5, ArrayList par6ArrayList, int par7)
    {
        super((ComponentVillageStartPiece)null, 0, par3Random, par4, par5);
        this.worldChunkMngr = par1WorldChunkManager;
        this.structureVillageWeightedPieceList = par6ArrayList;
        this.terrainType = par7;
        BiomeGenBase var8 = par1WorldChunkManager.getBiomeGenAt(par4, par5);
        this.field_74927_b = var8 == BiomeGenBase.desert || var8 == BiomeGenBase.desertHills;
        this.field_74897_k = this;
    }

    public WorldChunkManager getWorldChunkManager()
    {
        return this.worldChunkMngr;
    }
}
