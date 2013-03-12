package powercraft.api.block;

import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import powercraft.api.PC_VecI;

public abstract class PC_BlockRotated extends PC_Block {

	protected PC_BlockRotated(int id, Material material, String textureName, String[] textureNames) {
		super(id, material, textureName, textureNames);
	}
	
	
	
}
