package powercraft.core;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.IBlockAccess;
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
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		if(side==1 && getRenderType()==PC_Renderer.getRendererID(true) && this instanceof PC_IRotatedBox)
			return false;
		return super.shouldSideBeRendered(world, x, y, z, side);
	}
	
}
