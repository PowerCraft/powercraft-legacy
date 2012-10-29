package powercraft.logic;

import java.util.Random;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.core.PC_Block;
import powercraft.core.PC_IRotatedBox;
import powercraft.core.PC_ISwapTerrain;
import powercraft.core.PC_Renderer;
import powercraft.core.PC_Utils;

public class PClo_BlockGate extends PC_Block implements PC_IRotatedBox, PC_ISwapTerrain {
	
	public PClo_BlockGate(int id){
		super(id, 6, Material.ground);
		setBlockName("PCloLogicGate");
		setHardness(0.35F);
		setStepSound(Block.soundWoodFootstep);
		disableStats();
		setRequiresSelfNotify();
		setResistance(30.0F);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
		setCreativeTab(CreativeTabs.tabRedstone);
	}
	
	@Override
	public String getDefaultName() {
		return null;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world){
		return new PClo_TileEntityGate();
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		boolean on = isActive(world, x, y, z);
		boolean outputActive = isOutputActive(world, x, y, z);
		if (on && !outputActive) {
			// turn off
			changeGateState(false, world, x, y, z);
		} else if (!on && outputActive) {
			// turn on
			changeGateState(true, world, x, y, z);
		}
	}

	@Override
	public int tickRate() {
		return 1;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int side) {
		boolean on = isActive(world, x, y, z);
		boolean outputActive = isOutputActive(world, x, y, z);
		if (on && !outputActive) {
			world.scheduleBlockUpdate(x, y, z, blockID, tickRate());
		} else if (!on && outputActive) {
			world.scheduleBlockUpdate(x, y, z, blockID, tickRate());
		}
		
	}

	/**
	 * Change the gate block from on (lighting) to off state, and preserve the
	 * tile entity.
	 * 
	 * @param state new state, on or off
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	private void changeGateState(boolean state, World world, int x, int y, int z) {
		
		((PClo_TileEntityGate)PC_Utils.getTE(world, x, y, z)).setState(state);

	}
	
	private boolean isOutputActive(World world, int x, int y, int z) {

		return PClo_GateType.getGateOutput(getType(world, x, y, z), powered_from_input(world, x, y, z, 2), powered_from_input(world, x, y, z, 0),
					powered_from_input(world, x, y, z, 1));
	}
	
	/**
	 * Is the gate powered from given input? This method takes care of rotation
	 * for you. 0 BACK, 1 LEFT, 2 RIGHT, 3 FRONT, 4 BOTTOM, 5 TOP
	 * 
	 * @param world the World
	 * @param x
	 * @param y
	 * @param z
	 * @param inp the input number
	 * @return is powered
	 */
	public static boolean powered_from_input(World world, int x, int y, int z, int inp) {

		if(world==null)
			return false;
		
		if (inp == 4) {
			boolean isProviding = (world.isBlockIndirectlyProvidingPowerTo(x, y - 1, z, 0) || (world.getBlockId(x, y - 1, z) == Block.redstoneWire.blockID && world
					.getBlockMetadata(x, y - 1, z) > 0));
			return isProviding;
		}
		if (inp == 5) {
			boolean isProviding = (world.isBlockIndirectlyProvidingPowerTo(x, y + 1, z, 1) || (world.getBlockId(x, y + 1, z) == Block.redstoneWire.blockID && world
					.getBlockMetadata(x, y + 1, z) > 0));
			return isProviding;
		}

		int rotation = getRotation_static(world.getBlockMetadata(x, y, z));
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
			return (world.isBlockIndirectlyProvidingPowerTo(x, y, z + 1, 3) || world.getBlockId(x, y, z + 1) == Block.redstoneWire.blockID
					&& world.getBlockMetadata(x, y, z + 1) > 0);
		}
		if (rotation == N1) {
			return (world.isBlockIndirectlyProvidingPowerTo(x - 1, y, z, 4) || world.getBlockId(x - 1, y, z) == Block.redstoneWire.blockID
					&& world.getBlockMetadata(x - 1, y, z) > 0);
		}
		if (rotation == N2) {
			return (world.isBlockIndirectlyProvidingPowerTo(x, y, z - 1, 2) || world.getBlockId(x, y, z - 1) == Block.redstoneWire.blockID
					&& world.getBlockMetadata(x, y, z - 1) > 0);
		}
		if (rotation == N3) {
			return (world.isBlockIndirectlyProvidingPowerTo(x + 1, y, z, 5) || world.getBlockId(x + 1, y, z) == Block.redstoneWire.blockID
					&& world.getBlockMetadata(x + 1, y, z) > 0);
		}
		return false;
	}
	
	@Override
	public boolean isIndirectlyPoweringTo(IBlockAccess world, int x, int y, int z, int side) {
		return isPoweringTo(world, x, y, z, side);
	}
	
	@Override
	public boolean isPoweringTo(IBlockAccess world, int x, int y, int z, int side) {
		int meta = PC_Utils.getMD(world, x, y, z);
		int rotation = getRotation(meta);
		
		if (!isActive(world, x, y, z)) return false;
		
		if((rotation==0 && side==3) || (rotation==1 && side==4) || (rotation==2 && side==2) || (rotation==3 && side==5))
			return true;
		
		return false;
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}
	
	@Override
	public boolean renderAsNormalBlock()
    {
        return false;
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
	
	public static PClo_TileEntityGate getTE(IBlockAccess world, int x, int y, int z){
		return (PClo_TileEntityGate)PC_Utils.getTE(world, x, y, z);
	}
	
	public static int getType(IBlockAccess world, int x, int y, int z){
		PClo_TileEntityGate te = getTE(world, x, y, z);
		if(te!=null)
			return te.getType();
		return 0;
	}
	
	public static boolean isActive(IBlockAccess world, int x, int y, int z){
		PClo_TileEntityGate te = getTE(world, x, y, z);
		if(te!=null)
			return te.getState();
		return false;
	}
	
	@Override
	public int getBlockTexture(IBlockAccess iblockaccess, int x, int y, int z, int side) {
		if (side == 1) {
			return getTopFaceFromEnum(getType(iblockaccess, x, y, z)) + (isActive(iblockaccess, x, y, z) ? 16 : 0);
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

	@Override
	public int getRotation(int meta) {
		return getRotation_static(meta);
	}

	public static int getRotation_static(int meta) {
		return meta & 0x3;
	}
	
	@Override
	public boolean renderItemHorizontal() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int x, int y, int z) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving) {

		int type = getType(world, x, y, z);

		int l = ((MathHelper.floor_double(((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3) + 2) % 4;

		if (PC_Utils.isPlacingReversed()) {
			l = PC_Utils.reverseSide(l);
		}
		
		world.setBlockMetadataWithNotify(x, y, z, l);
		
		onNeighborBlockChange(world, x, y, z, 0);
		
	}
	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z){
		return isActive(world, x, y, z) ? 15 : 0;
	}
	
	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if (!isActive(world, x, y, z)) {
			return;
		}

		if (random.nextInt(3) != 0) {
			return;
		}

		double d = (x + 0.5F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;
		double d1 = (y + 0.2F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;
		double d2 = (z + 0.5F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;

		world.spawnParticle("reddust", d, d1, d2, 0.0D, 0.0D, 0.0D);
	}	
	
	@Override
	public int idDropped(int i, Random random, int j) {
		return -1;
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}
	
	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {

		int type = getType(world, x, y, z);
		
		boolean remove = super.removeBlockByPlayer(world, player, x, y, z);
		
		if (remove && !PC_Utils.isCreative(player)) {
			dropBlockAsItem_do(world, x, y, z, new ItemStack(mod_PowerCraftLogic.gate, 1, type));
		}

		return remove;

	}
	
}
