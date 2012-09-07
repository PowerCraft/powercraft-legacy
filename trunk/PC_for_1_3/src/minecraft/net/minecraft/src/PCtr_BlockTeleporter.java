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
		return AxisAlignedBB.getBoundingBox(i, j, k, (i + 1), (j + 0.03125), (k + 1));
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBox(i + 0.125D, j, k + 0.125D, (double) i + 1 - 0.125D, j + 1D, (double) k + 1 - 0.125D);
	}

	@Override
	public int getRenderType() {
		return PCtr_Renderer.teleporterRenderer;
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		ItemStack ihold = entityplayer.getCurrentEquippedItem();
		if (ihold != null) {
			if (ihold.getItem() instanceof ItemBlock && ihold.getItem().shiftedIndex != blockID) {
				Block bhold = Block.blocksList[ihold.getItem().shiftedIndex];
				if (bhold instanceof PC_IBlockType) {
					return false;
				}
			}
		}
		PC_Utils.openGres(entityplayer, PCtr_GuiTeleporter.class, world, i, j, k);
		
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		super.onBlockPlacedBy(world, i, j, k, entityliving);
		((PCtr_TileEntityTeleporter)world.getBlockTileEntity(i, j, k)).createData();
		if (entityliving instanceof EntityPlayer) {
			PC_Utils.openGres((EntityPlayer)entityliving, PCtr_GuiTeleporter.class, world, i, j, k);
		}
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int par5, int par6) {

		world.setBlockAndMetadataWithNotify(i, j, k, 0, 0);
		world.notifyBlocksOfNeighborChange(i, j, k, blockID);

		super.breakBlock(world, i, j, k, par5, par6);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
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
		PCtr_TeleporterData td = te.td;
		
		if (te.acceptsEntity(entity)) {
			PCtr_TeleporterHelper.teleportEntityTo(entity, td.defaultTarget);
		}

	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {

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
