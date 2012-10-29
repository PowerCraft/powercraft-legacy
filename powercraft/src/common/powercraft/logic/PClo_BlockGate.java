package powercraft.logic;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import powercraft.core.PC_Block;
import powercraft.core.PC_IBlockRenderer;
import powercraft.core.PC_IRotatedBox;
import powercraft.core.PC_ISwapTerrain;
import powercraft.core.PC_Renderer;
import powercraft.core.PC_Shining;
import powercraft.core.PC_Shining.OFF;
import powercraft.core.PC_Shining.ON;
import powercraft.core.PC_Utils;

@PC_Shining
public class PClo_BlockGate extends PC_Block implements PC_IRotatedBox, PC_ISwapTerrain {

	@ON(lightValue=15)
	public static PClo_BlockGate on;
	@OFF(lightValue=0)
	public static PClo_BlockGate off;
	
	public PClo_BlockGate(int id, boolean on){
		super(id, Material.ground);
		setBlockName("PCloLogicGate");
		setHardness(0.35F);
		setStepSound(Block.soundWoodFootstep);
		disableStats();
		setRequiresSelfNotify();
		setResistance(30.0F);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
		if(on)
			setCreativeTab(CreativeTabs.tabRedstone);
	}
	
	@Override
	public String getDefaultName() {
		return null;
	}

	@Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		// TODO Auto-generated method stub
		super.updateTick(par1World, par2, par3, par4, par5Random);
	}

	@Override
	public boolean isPoweringTo(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5) {
		// TODO Auto-generated method stub
		return super.isPoweringTo(par1iBlockAccess, par2, par3, par4, par5);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}
	
	@Override
	public String getTextureFile() {
		return getTerrainFile();
	}

	@Override
	public String getTerrainFile() {
		return mod_PowerCraftLogic.getInstance().getTerrainFile();
	}
	
	@Override
	public int getRenderType() {
		return PC_Renderer.getRendererID(true);
	}
	
	@Override
	public int getBlockTexture(IBlockAccess iblockaccess, int x, int y, int z, int side) {
		if (side == 1) {
			// top face!

			/*int index = getTE(iblockaccess, x, y, z).gateType;

			if (index == PClo_GateType.OR) {
				int variant = getTE(iblockaccess, x, y, z).getLayoutVariant();
				if (variant == 0) {
					return getTopFaceFromEnum(index) + (active ? 16 : 0);
				} else if (variant == 1) {
					return active ? 71 : 55;
				} else if (variant == 2) {
					return active ? 72 : 56;
				}
			}

			if (index == PClo_GateType.AND) {
				int variant = getTE(iblockaccess, x, y, z).getLayoutVariant();
				if (variant == 0) {
					return getTopFaceFromEnum(index) + (active ? 16 : 0);
				} else if (variant == 1) {
					return active ? 73 : 57;
				} else if (variant == 2) {
					return active ? 74 : 58;
				}
			}*/

			return getTopFaceFromEnum(PC_Utils.getMD(iblockaccess, x, y, z)) + (isActive() ? 16 : 0);
		}

		if (side == 0) {
			return 6;
		}
		return 5;
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		if (side == 0) {
			return 6; // stone slab particles
		}
		if (side == 1) {
			return getTopFaceFromEnum(meta) + 16; // top face
		} else {
			return 5; // side
		}
	}
	
	private int getTopFaceFromEnum(int meta) {
		if (meta <= 15) {
			return 16 + meta;
		} else {
			return 64 + meta;
		}
	}
	
	public boolean isActive(){
		return this==on;
	}

	@Override
	public int getRotation(int meta) {
		return meta & 3;
	}

	@Override
	public boolean renderItemHorizontal() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	
	
}