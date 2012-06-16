package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

/**
 * Radio device block with tile entity.
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PClo_BlockRadio extends BlockContainer implements PC_IBlockType {

	/**
	 * @param id ID
	 */
	protected PClo_BlockRadio(int id) {
		super(id, Block.stone.blockIndexInTexture, Material.circuits);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.255F, 1.0F);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void getCollidingBoundingBoxes(World world, int x, int y, int z, AxisAlignedBB collidedbox, ArrayList list) {
		setBlockBoundsBasedOnState(world, x, y, z);
		super.getCollidingBoundingBoxes(world, x, y, z, collidedbox, list);

		setBlockBounds(0.65F, 0, 0.65F, 0.95F, 0.9F, 0.65F);
		super.getCollidingBoundingBoxes(world, x, y, z, collidedbox, list);
	}

	@Override
	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.255F, 1.0F);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.255F, 1.0F);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		return true;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		// stonebrick texture
		return 6;
	}

	@Override
	public int getRenderType() {
		// only for inventory.
		return PClo_Renderer.radioRenderer;
	}

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityplayer) {
		ItemStack ihold = entityplayer.getCurrentEquippedItem();
		if (ihold != null) {
			if (ihold.getItem() instanceof ItemBlock && ihold.getItem().shiftedIndex != blockID) {
				Block bhold = Block.blocksList[ihold.getItem().shiftedIndex];
				if (bhold instanceof PC_IBlockType) { return false; }
			}
		}

		ItemStack holditem = entityplayer.getCurrentEquippedItem();
		if (holditem != null) {
			if (holditem.itemID == mod_PClogic.portableTx.shiftedIndex) {
				String channel = mod_PClogic.default_radio_channel;

				channel = getTE(world, x, y, z).getChannel();

				PClo_ItemRadioRemote.setChannel(holditem, channel);
				world.playSoundAtEntity(entityplayer, "note.snare", (world.rand.nextFloat() + 0.7F) / 2.0F,
						1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.4F);
				return true;
			}
		}

		boolean istx = getTE(world, x, y, z).isTransmitter();
		String channel = getTE(world, x, y, z).getChannel();

		ModLoader.openGUI(entityplayer, new PClo_GuiRadioChannel(entityplayer.dimension, new PC_CoordI(x, y, z), channel, istx));

		return true;
	}

	/**
	 * Get radio tile entity at coordinates
	 * 
	 * @param iblockaccess block access
	 * @param i x
	 * @param j y
	 * @param k z
	 * @return the tile entity or null
	 */
	public static PClo_TileEntityRadio getTE(IBlockAccess iblockaccess, int i, int j, int k) {
		TileEntity te = iblockaccess.getBlockTileEntity(i, j, k);
		if (te == null) { return null; }
		PClo_TileEntityRadio ter = (PClo_TileEntityRadio) te;

		return ter;
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public int idDropped(int i, Random random, int j) {
		return -1;
	}

	@Override
	public void onBlockRemoval(World world, int i, int j, int k) {
		world.setBlockAndMetadataWithNotify(i, j, k, 0, 0);
		world.notifyBlocksOfNeighborChange(i, j, k, blockID);

		PClo_TileEntityRadio ter = getTE(world, i, j, k);

		if (ter.isTransmitter()) {
			PClo_RadioManager.unregisterTx(ter.dim, new PC_CoordI(i, j, k), ter.getChannel());
		} else {
			PClo_RadioManager.unregisterReceiver(new PC_CoordI(i, j, k));
		}



		if (!PC_Utils.isCreative()) {
			dropBlockAsItem_do(world, i, j, k, new ItemStack(mod_PClogic.radio, 1, getTE(world, i, j, k).isTransmitter() ? 0 : 1));
		}

		super.onBlockRemoval(world, i, j, k);
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		int meta = world.getBlockMetadata(i, j, k);

		PClo_TileEntityRadio ter = getTE(world, i, j, k);

		if (ter.isTransmitter()) {

			boolean on = (meta == 1);
			boolean power = isGettingPower(world, i, j, k);

			if (on && !power) {
				world.setBlockMetadataWithNotify(i, j, k, 0);
				ter.setStateWithNotify(false);

			} else if (!on && power) {
				world.setBlockMetadataWithNotify(i, j, k, 1);
				ter.setStateWithNotify(true);
			}

		} else {

			boolean on = (meta == 1);
			boolean power = PClo_RadioManager.getSignalStrength(ter.getChannel()) > 0;

			if (on && !power) {
				world.setBlockMetadataWithNotify(i, j, k, 0);
				ter.setStateWithNotify(false);

			} else if (!on && power) {
				world.setBlockMetadataWithNotify(i, j, k, 1);
				ter.setStateWithNotify(true);
			}
		}

		world.markBlockNeedsUpdate(i, j, k);
		world.notifyBlocksOfNeighborChange(i, j - 1, k, world.getBlockId(i, j - 1, k));
	}

	private boolean isGettingPower(World world, int i, int j, int k) {
		return ((world.isBlockGettingPowered(i, j, k) || world.isBlockGettingPowered(i, j - 1, k)
				|| world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j - 1, k)));
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {

		if (getTE(world, i, j, k).isReceiver()) { return; }


		if (!(l > 0 && Block.blocksList[l].canProvidePower())) { return; }

		boolean on = (world.getBlockMetadata(i, j, k) == 1);
		boolean power = isGettingPower(world, i, j, k);

		if (on && !power) {
			world.scheduleBlockUpdate(i, j, k, blockID, 1);
		} else if (!on && power) {
			world.scheduleBlockUpdate(i, j, k, blockID, 1);
		}

	}

	@Override
	public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		int meta = iblockaccess.getBlockMetadata(i, j, k);
		if (getTE(iblockaccess, i, j, k).isReceiver() && meta == 1) { return true; }
		return false;
	}

	@Override
	public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l) {
		return isPoweringTo(world, i, j, k, l);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random) {
		int i1 = world.getBlockMetadata(i, j, k);
		if (i1 != 1) { return; }

		double x = (i + 0.5F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;
		double y = (j + 0.9F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;
		double z = (k + 0.5F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;

		world.spawnParticle("reddust", x, y, z, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public int getMobilityFlag() {
		return 2;
	}

	//@formatter:off
	
	@Override
	public boolean isTranslucentForLaser(IBlockAccess world, PC_CoordI pos) { return true; }
	@Override
	public boolean isHarvesterIgnored(IBlockAccess world, PC_CoordI pos) { return true; }
	@Override
	public boolean isHarvesterDelimiter(IBlockAccess world, PC_CoordI pos) { return false; }
	@Override
	public boolean isBuilderIgnored() { return true; }
	@Override
	public boolean isConveyor(IBlockAccess world, PC_CoordI pos){ return false; }
	@Override
	public boolean isElevator(IBlockAccess world, PC_CoordI pos) { return false; }

	//@formatter:on

	@Override
	public TileEntity getBlockEntity() {
		return new PClo_TileEntityRadio();
	}
}
