package powercraft.management.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import powercraft.management.PC_OreDictionary;
import powercraft.management.PC_VecI;
import powercraft.management.item.PC_ItemStack;

public abstract class PC_BlockOre extends PC_Block{

	private int genOresInChunk;
	private int genOresDepositMaxCount;
	private int genOresMaxY;
	private int genOresMinY;
	
	protected PC_BlockOre(int id, String oreName, int genOresInChunk, int genOresDepositMaxCount, int genOresMinY, int genOresMaxY, Material material){
        this(id, oreName, genOresInChunk, genOresDepositMaxCount, genOresMinY, genOresMaxY, 0, material);
    }

    protected PC_BlockOre(int id, String oreName, int genOresInChunk, int genOresDepositMaxCount, int genOresMinY, int genOresMaxY, int textureIndex, Material material){
    	this(id, oreName, genOresInChunk, genOresDepositMaxCount, genOresMinY, genOresMaxY, textureIndex, material, true);
    }

    protected PC_BlockOre(int id, String oreName, int genOresInChunk, int genOresDepositMaxCount, int genOresMinY, int genOresMaxY, int textureIndex, Material material, boolean canSetTextureFile) {
        super(id, textureIndex, material);
        PC_OreDictionary.register(oreName, new PC_ItemStack(this));
        this.genOresInChunk = genOresInChunk;
        this.genOresDepositMaxCount = genOresDepositMaxCount;
        this.genOresMaxY = genOresMaxY;
        this.genOresMinY = genOresMinY;
    }

	public int getGenOresInChunk() {
		return genOresInChunk;
	}

	public void setGenOresInChunk(int genOresInChunk) {
		this.genOresInChunk = genOresInChunk;
	}

	public int getGenOresDepositMaxCount() {
		return genOresDepositMaxCount;
	}

	public void setGenOresDepositMaxCount(int genOresDepositMaxCount) {
		this.genOresDepositMaxCount = genOresDepositMaxCount;
	}

	public int getGenOresMaxY() {
		return genOresMaxY;
	}

	public void setGenOresMaxY(int genOresMaxY) {
		this.genOresMaxY = genOresMaxY;
	}

	public int getGenOresMinY() {
		return genOresMinY;
	}

	public void setGenOresMinY(int genOresMinY) {
		this.genOresMinY = genOresMinY;
	}
    
	public int getGenOresSpawnsInChunk(Random random, World world, int chunkX, int chunkZ){
		return genOresInChunk;
	}
	
	public int getGenOreblocksOnSpawnPoint(Random random, World world, int chunkX, int chunkZ){
		return random.nextInt(genOresDepositMaxCount+1);
	}
	
	public PC_VecI getGenOresSpawnPoint(Random random, World world, int chunkX, int chunkZ){
		return new PC_VecI(random.nextInt(16), genOresMinY + random.nextInt(genOresMaxY-genOresMinY+1), random.nextInt(16));
	}
	
	public int getGenOresSpawnMetadata(Random random, World world, int chunkX, int chunkZ){
		return 0;
	}
	
}
