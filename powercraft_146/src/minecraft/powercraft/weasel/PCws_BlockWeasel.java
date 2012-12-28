package powercraft.weasel;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_VecI;

public class PCws_BlockWeasel extends PC_Block {

	public PCws_BlockWeasel(int id) {
		super(id, 6, Material.ground);
		setHardness(0.5F);
		setLightValue(0);
		setStepSound(Block.soundWoodFootstep);
		disableStats();
		setRequiresSelfNotify();
		setResistance(60.0F);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
		setCreativeTab(CreativeTabs.tabRedstone);
	}
	
	@Override
	public TileEntity newTileEntity(World world, int metadata) {
		return new PCws_TileEntityWeasel();
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		PCws_TileEntityWeasel te = GameInfo.getTE(world, x, y, z);
		if(te!=null){
			float[] bounds = te.getPluginInfo().getBounds();
			setBlockBounds(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		if(!world.isRemote){
			PCws_WeaselPlugin weaselPlugin = getPlugin(world, x, y, z);
			if(weaselPlugin!=null)
				PCws_WeaselManager.removePlugin(weaselPlugin);
		}
		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_RENDER_INVENTORY_BLOCK:
			PCws_WeaselManager.getPluginInfo((Integer)obj[1]).renderInventoryBlock((Block)obj[0], obj[3]);
			break;
		case PC_Utils.MSG_RENDER_WORLD_BLOCK:
			break;
		default:
			return null;
		}
		return true;
	}

	public static PCws_WeaselPlugin getPlugin(World world, int x, int y, int z){
		PCws_WeaselPlugin wp = null;
		if(world.blockExists(x, y, z))
			wp = GameInfo.<PCws_TileEntityWeasel>getTE(world, x, y, z).getPlugin();
		if(wp==null)
			wp = PCws_WeaselManager.getPlugin(world, x, y, z);
		return wp;
	}
	
	public static PCws_WeaselPlugin getPlugin(World world, PC_VecI pos){
		return getPlugin(world, pos.x, pos.y, pos.z);
	}
	
	/**
	 * Get redstone states on direct inputs. 0 BACK, 1 LEFT, 2 RIGHT, 3 FRONT, 5
	 * TOP, 4 BOTTOM
	 * 
	 * @param world
	 * @param pos the block position
	 * @return array of booleans
	 */
	public static boolean[] getWeaselInputStates(World world, PC_VecI pos) {

		if(!world.blockExists(pos.x, pos.y, pos.z))
			return null;
		
		return new boolean[]{
				powered_from_input(world, pos, 0),
				powered_from_input(world, pos, 1),
				powered_from_input(world, pos, 2),
				powered_from_input(world, pos, 3),
				powered_from_input(world, pos, 5),
				powered_from_input(world, pos, 4)
			};
		
	}

	public static boolean gettingPowerFrom(World world, int x, int y, int z, int rot){
		return world.isBlockIndirectlyProvidingPowerTo(x, y, z, rot) || 
				(GameInfo.getBID(world, x, y, z) == Block.redstoneWire.blockID && 
				GameInfo.getMD(world, x, y, z)>0);
	}
	
	/**
	 * Is the gate powered from given input? This method takes care of rotation
	 * for you. 0 BACK, 1 LEFT, 2 RIGHT, 3 FRONT, 4 BOTTOM, 5 TOP
	 * 
	 * @param world the World
	 * @param pos the block position
	 * @param inp the input number
	 * @return is powered
	 */
	public static boolean powered_from_input(World world, PC_VecI pos, int inp) {
		if (world == null) return false;
		int x = pos.x, y = pos.y, z = pos.z;
		
		if (inp == 4) {
			return gettingPowerFrom(world, x, y - 1, z, 0);
		}
		if (inp == 5) {
			return gettingPowerFrom(world, x, y + 1, z, 1);
		}

		int rotation = getRotation(world.getBlockMetadata(x, y, z));
		int N0 = 0, N1 = 1, N2 = 2, N3 = 3;
		if (inp == 0) {
			N0 = 0;
			N1 = 1;
			N2 = 2;
			N3 = 3;
		}
		if (inp == 1) {
			N0 = 3;
			N1 = 0;
			N2 = 1;
			N3 = 2;
		} else if (inp == 2) {
			N0 = 1;
			N1 = 2;
			N2 = 3;
			N3 = 0;
		} else if (inp == 3) {
			N0 = 2;
			N1 = 3;
			N2 = 0;
			N3 = 1;
		}

		if (rotation == N0) {
			return gettingPowerFrom(world, x, y, z + 1, 3);
		}
		if (rotation == N1) {
			return gettingPowerFrom(world, x - 1, y, z, 4);
		}
		if (rotation == N2) {
			return gettingPowerFrom(world, x, y, z - 1, 2);
		}
		if (rotation == N3) {
			return gettingPowerFrom(world, x + 1, y, z, 5);
		}
		return false;
	}
	
	/**
	 * Get gate rotation, same as getRotation, but available statically
	 * 
	 * @param meta block meta
	 * @return rotation 0,1,2,3
	 */
	public static int getRotation(int meta) {
		return meta & 3;
	}
	
}
