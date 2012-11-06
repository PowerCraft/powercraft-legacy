package powercraft.core;

import java.util.List;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public abstract class PC_Block extends BlockContainer {

	private boolean canSetTextureFile = true;
	
	protected PC_Block(int id, Material material){
		super(id, material);
	}
	
	protected PC_Block(int id, int textureIndex, Material material){
		super(id, textureIndex, material);
	}

	public PC_Block(int id, int textureIndex, Material material, boolean canSetTextureFile) {
		super(id, textureIndex, material);
		this.canSetTextureFile = canSetTextureFile;
	}

	public abstract String getDefaultName();
	
	public TileEntity createNewTileEntity(World world){
		return null;
	}
	
	@Override
	public int getRenderType() {
		return PC_Renderer.getRendererID(true);
	}
	
	@Override
	public void setTextureFile(String texture) {
		if(canSetTextureFile)
			super.setTextureFile(texture);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		if(side==1 && getRenderType()==PC_Renderer.getRendererID(true) && this instanceof PC_IRotatedBox)
			return false;
		return super.shouldSideBeRendered(world, x, y, z, side);
	}
	
}
