package net.minecraft.src;

import java.util.Random;

import net.minecraft.src.forge.ITextureProvider;

public class PCma_BlockReplacer extends BlockContainer implements PC_ISwapTerrain, PC_IBlockType, ITextureProvider {
	private static final int TXDOWN = 109, TXTOP = 125, TXSIDE = 125, TXFRONT = 125, TXBACK = 125;
	
	private int blockOffset[] = {0, 0, 0};
	
	protected PCma_BlockReplacer(int par1, int par2, Material par3Material) {
		super(par1, par2, par3Material);
	}
	
	protected PCma_BlockReplacer(int par1, Material par2Material) {
		super(par1, par2Material);
	}
	
	protected PCma_BlockReplacer(int par1) {
		super(par1, Material.ground);
	}
	
	@Override
	public String getTerrainFile() {
		return mod_PCmachines.getImgDir() + "tiles.png";
	}

	@Override
	public String getTextureFile() {
		return getTerrainFile();
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return true;
	}
	
	@Override
	public int getRenderType() {
		return PC_Renderer.swapTerrainRenderer;
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int s, int m) {
		if (s == 1) {
			return TXTOP;
		}
		if (s == 0) {
			return TXDOWN;
		} else {
			if (m == s) return TXFRONT;
			if ((m == 2 && s == 3) || (m == 3 && s == 2) || (m == 4 && s == 5) || (m == 5 && s == 4)) return TXBACK;
			return TXSIDE;
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		super.onBlockPlacedBy(world, i, j, k, entityliving);

//		if(entityliving instanceof EntityPlayer){
//			EntityPlayer entityplayer = (EntityPlayer)entityliving;
//			PCma_TileEntityReplacer tileentity = (PCma_TileEntityReplacer) world.getBlockTileEntity(i, j, k);
//			if (tileentity != null) {
//				ModLoader.openGUI(entityplayer, new PC_GresGui(new PCma_GuiReplacer(entityplayer.inventory, tileentity)));
//			}
//		}
	}
	
	@Override
	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		
		ItemStack ihold = entityplayer.getCurrentEquippedItem();
		if (ihold != null) {
			if (ihold.getItem() instanceof ItemBlock && ihold.getItem().shiftedIndex != blockID) {

				Block bhold = Block.blocksList[ihold.getItem().shiftedIndex];
				if (bhold instanceof PC_IBlockType) { return false; }

			}
		}

		if (world.isRemote) { return true; }
		
		PCma_TileEntityReplacer tileentity = (PCma_TileEntityReplacer) world.getBlockTileEntity(i, j, k);
		if (tileentity != null) {
			PC_Utils.openGres(entityplayer, new PCma_GuiReplacer(tileentity, entityplayer.inventory));
		}
		
		return true;
	}
	
	@Override
	public int tickRate() {
		return 4;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (l > 0 && Block.blocksList[l].canProvidePower()) {
			world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
		}
	}
	
	private void swapBlocks(World world, int x, int y, int z, Random random, PCma_TileEntityReplacer tileentity){
		if(tileentity.buildBlock != null)
			if(!(tileentity.buildBlock.getItem() instanceof ItemBlock))
				return;
		int id = world.getBlockId(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		ItemStack newItem = null;
		if(id>0){
			int dropId = Block.blocksList[id].blockID;
			int dropMeta = Block.blocksList[id].damageDropped(meta);
			int dropQuant = Block.blocksList[id].quantityDropped(world.rand);
	
			/*// play breaking sound and animation
			if (mod_PCcore.soundsEnabled) {
				world.playAuxSFX(2001, coord.x, coord.y, coord.z, id + (meta << 12));
			}*/
			if (dropId <= 0) {
				dropId = id;
			}
			if (dropQuant <= 0) {
				dropQuant = 1;
			}
			newItem = new ItemStack(dropId, dropQuant, dropMeta);
		}
		if(tileentity.buildBlock != null)
			world.setBlockWithNotify(x, y, z, ((ItemBlock)(tileentity.buildBlock.getItem())).shiftedIndex);
		tileentity.buildBlock = newItem;
	}
	
	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		PCma_TileEntityReplacer tileentity = (PCma_TileEntityReplacer) world.getBlockTileEntity(i, j, k);
		if (tileentity != null && !world.isRemote ) {
			swapBlocks(world, i + tileentity.coordOffset[0], j + tileentity.coordOffset[1], k + tileentity.coordOffset[2], random, tileentity);
		}
	}
	
	private boolean isIndirectlyPowered(World world, int i, int j, int k) {
		if (world.isBlockGettingPowered(i, j, k)) {
			return true;
		}

		if (world.isBlockIndirectlyGettingPowered(i, j, k)) {
			return true;
		}

		if (world.isBlockGettingPowered(i, j - 1, k)) {
			return true;
		}

		if (world.isBlockIndirectlyGettingPowered(i, j - 1, k)) {
			return true;
		}
		return false;
	}
	
	@Override
	public TileEntity getBlockEntity() {
		return new PCma_TileEntityReplacer();
	}
	
	@Override
	public int getMobilityFlag() {
		return 0;
	}

	@Override
	public void onBlockRemoval(World world, int i, int j, int k) {
		PCma_TileEntityReplacer tileentity = (PCma_TileEntityReplacer) world.getBlockTileEntity(i, j, k);
		Random random = new Random();
		if (tileentity != null)
			if(tileentity.buildBlock != null) {
				float f = random.nextFloat() * 0.8F + 0.1F;
				float f1 = random.nextFloat() * 0.8F + 0.1F;
				float f2 = random.nextFloat() * 0.8F + 0.1F;
				EntityItem entityitem = new EntityItem(world, i + f, j + f1, k + f2, tileentity.buildBlock);
				float f3 = 0.05F;
				entityitem.motionX = (float) random.nextGaussian() * f3;
				entityitem.motionY = (float) random.nextGaussian() * f3 + 0.2F;
				entityitem.motionZ = (float) random.nextGaussian() * f3;
				world.spawnEntityInWorld(entityitem);
			}
		super.onBlockRemoval(world, i, j, k);
	}
	
	@Override
	public boolean isTranslucentForLaser(IBlockAccess world, PC_CoordI pos) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isHarvesterIgnored(IBlockAccess world, PC_CoordI pos) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isHarvesterDelimiter(IBlockAccess world, PC_CoordI pos) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBuilderIgnored() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isConveyor(IBlockAccess world, PC_CoordI pos) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isElevator(IBlockAccess world, PC_CoordI pos) {
		// TODO Auto-generated method stub
		return false;
	}
}
