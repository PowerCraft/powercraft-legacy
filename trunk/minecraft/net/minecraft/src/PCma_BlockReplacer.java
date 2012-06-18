package net.minecraft.src;

import java.util.Random;

import net.minecraft.src.forge.ITextureProvider;

public class PCma_BlockReplacer extends BlockContainer implements PC_ISwapTerrain, PC_IBlockType, ITextureProvider {
	private static final int TXDOWN = 109, TXTOP = 125, TXSIDE = 125, TXFRONT = 125, TXBACK = 125;

	private int blockOffset[] = { 0, 0, 0 };

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
		if (s == 1) { return TXTOP; }
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

		// if(entityliving instanceof EntityPlayer){
		// EntityPlayer entityplayer = (EntityPlayer)entityliving;
		// PCma_TileEntityReplacer tileentity = (PCma_TileEntityReplacer) world.getBlockTileEntity(i, j, k);
		// if (tileentity != null) {
		// ModLoader.openGUI(entityplayer, new PC_GresGui(new PCma_GuiReplacer(entityplayer.inventory, tileentity)));
		// }
		// }
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

	private boolean replacer_canHarvestBlockAt(World world, PC_CoordI pos) {

		int id = pos.getMeta(world);

		if (id == 0 || Block.blocksList[id] == null) return true;

		if (pos.getTileEntity(world) != null) return false;

		return true;

	}

	/**
	 * Check if block can be auto-placed (and is then harvestable again)
	 * 
	 * @param world
	 * @param itemstack
	 * @param pos
	 * @return can be placed
	 */
	private boolean replacer_canPlaceBlockAt(World world, ItemStack itemstack, PC_CoordI pos) {

		if (itemstack == null) return true;
		Item item = itemstack.getItem();

		if (item instanceof ItemBlock) {

			Block block = Block.blocksList[item.shiftedIndex];

			if (block == null) return false;

			if (PC_BlockUtils.isBuilderIgnored(block.blockID)) return false;

			if (block.isBlockContainer) return false;

			return true;

		} else {
			return false;
		}

	}


	/**
	 * Place itemblock from stack.
	 * 
	 * @param world
	 * @param itemstack
	 * @param pos
	 * @return
	 */
	private boolean replacer_placeBlockAt(World world, ItemStack itemstack, PC_CoordI pos) {

		if (itemstack == null) {
			pos.setBlock(world, 0, 0);
			return true;
		}

		if (!replacer_canPlaceBlockAt(world, itemstack, pos)) return false;

		ItemBlock iblock = (ItemBlock) itemstack.getItem();

		return iblock.onItemUse(itemstack, new PC_FakePlayer(world), world, pos.x, pos.y+1, pos.z, 0);

	}


	/**
	 * Get stack from a block at target coordinates.
	 * 
	 * @param world the world
	 * @param pos target position
	 * @return stack, or null if not harvested.
	 */
	private ItemStack replacer_harvestBlockAt(World world, PC_CoordI pos) {

		ItemStack loot = null;

		if (!replacer_canHarvestBlockAt(world, pos)){
			System.out.println("cant harvest "+pos);
			return null;
		}

		Block block = Block.blocksList[pos.getId(world)];

		if (block == null){
			System.out.println("block is null "+pos);
			return null;
		}

		if (block.canSilkHarvest()) {
			System.out.println("silk "+pos);
			loot = block.createStackedBlock(pos.getMeta(world));
		} else {
			System.out.println("nosilk "+pos);
			int dropId = block.blockID;
			int dropMeta = block.damageDropped(pos.getMeta(world));
			int dropQuant = block.quantityDropped(world.rand);
			if (dropId <= 0) {
				dropId = pos.getId(world);
			}
			if (dropQuant <= 0) {
				dropQuant = 1;
			}

			loot = new ItemStack(dropId, dropQuant, dropMeta);
		}

		pos.setBlock(world,0,0);
		return loot;
	}

	private void swapBlocks(PCma_TileEntityReplacer te) {
	
		PC_CoordI pos = new PC_CoordI(te.xCoord, te.yCoord, te.zCoord).offset(te.coordOffset);
		
		if(pos.equals(new PC_CoordI(te.xCoord, te.yCoord, te.zCoord))) return;
		
		if(!replacer_canHarvestBlockAt(te.worldObj, pos)){
			System.out.println("cant harvest  "+pos);
			return;
		}
		
		if(!replacer_canPlaceBlockAt(te.worldObj, te.buildBlock, pos)){
			System.out.println("cant place  "+te.buildBlock);
			return;
		}
		
		ItemStack harvested = replacer_harvestBlockAt(te.worldObj, pos);
		
		replacer_placeBlockAt(te.worldObj, te.buildBlock, pos);
		
		te.buildBlock = harvested;

	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		
		PCma_TileEntityReplacer ter = (PCma_TileEntityReplacer) world.getBlockTileEntity(i, j, k);
		
		if (ter != null && !world.isRemote) {
			swapBlocks(ter);
		}
	}

	private boolean isIndirectlyPowered(World world, int i, int j, int k) {
		if (world.isBlockGettingPowered(i, j, k)) { return true; }

		if (world.isBlockIndirectlyGettingPowered(i, j, k)) { return true; }

		if (world.isBlockGettingPowered(i, j - 1, k)) { return true; }

		if (world.isBlockIndirectlyGettingPowered(i, j - 1, k)) { return true; }
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
		if (tileentity != null) if (tileentity.buildBlock != null) {
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
