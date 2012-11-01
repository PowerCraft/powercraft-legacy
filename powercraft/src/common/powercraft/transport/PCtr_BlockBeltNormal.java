package powercraft.transport;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import powercraft.core.PC_Block;
import powercraft.core.PC_IRotatedBox;
import powercraft.core.PC_ISwapTerrain;

public class PCtr_BlockBeltNormal extends PC_Block implements PC_IRotatedBox, PC_ISwapTerrain {
	
	protected PCtr_BlockBeltNormal(int id) {
		super(id, 0, PCtr_MaterialConveyor.getMaterial());
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, PCtr_BeltHelper.HEIGHT, 1.0F);
		setStepSound(Block.soundPowderFootstep);
		
	}

	@Override
	public String getDefaultName() {
		return "Belt";
	}
	
	@Override
	public String getTerrainFile() {
		return mod_PowerCraftTransport.getInstance().getTerrainFile();
	}

	@Override
	public int getRotation(int meta) {
		return PCtr_BeltHelper.getRotation(meta);
	}

	@Override
	public boolean renderItemHorizontal() {
		return false;
	}

	@Override
	public String getTextureFile() {
		return getTerrainFile();
	}
	
	@Override
	public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return getBlockTextureFromSideAndMetadata(l, iblockaccess.getBlockMetadata(i, j, k));
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		if (i == 0) {
			return 1;
		}
		if (i == 1) {
			return 0;
		} else {
			return 2;
		}
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
}
