package powercraft.core;

import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public abstract class PC_Block extends BlockContainer {

	protected PC_Block(int id, Material material){
		super(id, material);
	}
	
	protected PC_Block(int id, int textureIndex, Material material){
		super(id, textureIndex, material);
	}

	public abstract String getDefaultName();
	
	public TileEntity createNewTileEntity(World world){
		return null;
	}
	
}
