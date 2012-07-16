package net.minecraft.src;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * Teleporter block
 * 
 * @author MightyPork
 */
public class PCtr_BlockTeleporter extends BlockContainer implements PC_IBlockType {
	/**
	 * teleporter block
	 * 
	 * @param id ID
	 * @param tindex texture
	 * @param material material
	 */
	public PCtr_BlockTeleporter(int id, int tindex, Material material) {
		super(id, tindex, material);
		setBlockBounds(0.125F, 0.0F, 0.125F, 1.0F - 0.125F, 1.0F - 0.125F, 1.0F - 0.125F);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBoxFromPool(i, j, k, (i + 1), (j + 0.03125), (k + 1));
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBoxFromPool(i + 0.125D, j, k + 0.125D, (double) i + 1 - 0.125D, j + 1D, (double) k + 1 - 0.125D);
	}

	@Override
	public int getRenderType() {
		return PCtr_Renderer.teleporterRenderer;
	}

	@Override
	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		ItemStack ihold = entityplayer.getCurrentEquippedItem();
		if (ihold != null) {
			if (ihold.getItem() instanceof ItemBlock && ihold.getItem().shiftedIndex != blockID) {
				Block bhold = Block.blocksList[ihold.getItem().shiftedIndex];
				if (bhold instanceof PC_IBlockType) {
					return false;
				}
			}
		}

		PCtr_TileEntityTeleporter te = (PCtr_TileEntityTeleporter) world.getBlockTileEntity(i, j, k);
		PC_Utils.openGres(entityplayer, new PCtr_GuiTeleporter(te));

		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		super.onBlockPlacedBy(world, i, j, k, entityliving);

		if (entityliving instanceof EntityPlayer) {
			PCtr_TileEntityTeleporter te = (PCtr_TileEntityTeleporter) world.getBlockTileEntity(i, j, k);

			if (te == null) {
				return;
			}

			PC_Utils.openGres((EntityPlayer) entityliving, new PCtr_GuiTeleporterDecide(te));
		}
	}

	@Override
	public void onBlockRemoval(World world, int i, int j, int k) {

		PCtr_TeleporterHelper.unregisterDevice(getTE(world, i, j, k).identifierName);

		world.setBlockAndMetadataWithNotify(i, j, k, 0, 0);
		world.notifyBlocksOfNeighborChange(i, j, k, blockID);

		super.onBlockRemoval(world, i, j, k);
	}

	@Override
	public TileEntity getBlockEntity() {
		return new PCtr_TileEntityTeleporter();
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int quantityDropped(Random par1Random) {
		return 1;
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		if ((entity instanceof EntityFishHook) || (entity instanceof EntityPainting) || (entity instanceof EntityDiggingFX)) {
			return;
		}

		PCtr_TileEntityTeleporter te = getTE(world, i, j, k);

		if (te.isSender() && te.isActive()) {
			if (te.acceptsEntity(entity)) {
				PCtr_TeleporterHelper.teleportEntityTo(entity, te.targetName);
			}
		} else {
			// receiver, do nothing.
		}

	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if (!isActive(world, x, y, z)) {
			return;
		}

		if (random.nextInt(60) == 0) {
			if (mod_PCcore.soundsEnabled) {
				world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "portal.portal", 0.1F, random.nextFloat() * 0.4F + 0.8F);
			}
		}

		for (int i = 0; i < 8; i++) {
			// target pos
			double d = x + random.nextFloat();
			double d1 = y + random.nextFloat();
			double d2 = z + random.nextFloat();

			// initial position
			double d3 = 0.0D;
			double d4 = 0.0D;
			double d5 = 0.0D;

			d3 = -0.75F + random.nextFloat() * 1.5F;
			d4 = -0.25F + random.nextFloat() * 1F;
			d5 = -0.75F + random.nextFloat() * 1.5F;

			world.spawnParticle("portal", d, d1, d2, d3, d4, d5);
		}
	}

	/**
	 * Get Tile Entity at position.
	 * 
	 * @param iblockaccess
	 * @param x
	 * @param y
	 * @param z
	 * @return te
	 */
	public static PCtr_TileEntityTeleporter getTE(IBlockAccess iblockaccess, int x, int y, int z) {
		TileEntity te = iblockaccess.getBlockTileEntity(x, y, z);
		if (te == null) {
			return null;
		}
		PCtr_TileEntityTeleporter tet = (PCtr_TileEntityTeleporter) te;

		return tet;
	}

	/**
	 * check if the teleporter is running
	 * 
	 * @param iblockaccess
	 * @param i
	 * @param j
	 * @param k
	 * @return flag
	 */
	public static boolean isActive(IBlockAccess iblockaccess, int i, int j, int k) {
		return getTE(iblockaccess, i, j, k).isActive();
	}

	/**
	 * check if this is the target device
	 * 
	 * @param iblockaccess
	 * @param i
	 * @param j
	 * @param k
	 * @return flag
	 */
	public static boolean isTarget(IBlockAccess iblockaccess, int i, int j, int k) {
		return getTE(iblockaccess, i, j, k).isReceiver();
	}

	@Override
	public Set<String> getBlockFlags(World world, PC_CoordI pos) {

		Set<String> set = new HashSet<String>();

		set.add("NO_HARVEST");
		set.add("NO_PICKUP");
		set.add("TRANSLUCENT");
		set.add("BELT");
		set.add("TELEPORTER");

		return set;
	}

	@Override
	public Set<String> getItemFlags(ItemStack stack) {
		Set<String> set = new HashSet<String>();
		set.add("NO_BUILD");
		set.add("TELEPORTER");
		return set;
	}
}
