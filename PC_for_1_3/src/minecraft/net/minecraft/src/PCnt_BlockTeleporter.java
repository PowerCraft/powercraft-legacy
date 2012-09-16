package net.minecraft.src;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;



/**
 * Teleporter block
 * 
 * @author MightyPork
 */
public class PCnt_BlockTeleporter extends BlockContainer implements PC_IBlockType {
	/**
	 * teleporter block
	 * 
	 * @param id ID
	 * @param tindex texture
	 * @param material material
	 */
	public PCnt_BlockTeleporter(int id, int tindex, Material material) {
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
		return PCnt_Renderer.teleporterRenderer;
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
		PC_Utils.openGres(entityplayer, PCnt_GuiTeleporter.class, i, j, k, entityplayer.dimension);
		
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		super.onBlockPlacedBy(world, i, j, k, entityliving);
		if(!world.isRemote){
			System.out.println("onBlockPlacedBy");
			PCnt_TeleporterData td;
			if(entityliving instanceof EntityPlayer)
				td = PCnt_TeleporterManager.getTeleporterDataAt(((EntityPlayer)entityliving).dimension, i, j, k);
			else
				td = PCnt_TeleporterManager.getTeleporterDataAt(entityliving.worldObj.worldInfo.getDimension(), i, j, k);
			if(td==null){
				td = new PCnt_TeleporterData();
				td.pos.setTo(i, j, k);
				td.setName("");
				td.defaultTarget="";
				if(entityliving instanceof EntityPlayer)
					td.dimension = ((EntityPlayer)entityliving).dimension;
				else
					td.dimension = entityliving.worldObj.worldInfo.getDimension();
				PCnt_TileEntityTeleporter tet = (PCnt_TileEntityTeleporter)world.getBlockTileEntity(i, j, k);
				tet.dimension = td.dimension;
				System.out.println("td.dimension:"+td.dimension);
				PCnt_TeleporterManager.add(td);
				PC_Utils.sendToPacketHandler(null, "TeleporterNetHandler", i, j, k, "", "", td.dimension);
			}//else{
			//	if(td.dimension!=worldObj.worldInfo.getDimension())
			//		PC_Utils.sendToPacketHandler(PC_Utils.mc().thePlayer, "TeleporterNetHandler", 0, xCoord, yCoord, zCoord, "", "", worldObj.worldInfo.getDimension());
			//}
			if (entityliving instanceof EntityPlayer) {
				PC_Utils.openGres((EntityPlayer)entityliving, PCnt_GuiTeleporter.class, i, j, k, td.dimension);
			}
		}
		//((PCnt_TileEntityTeleporter)world.getBlockTileEntity(i, j, k)).createData();
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int par5, int par6) {

		world.setBlockAndMetadataWithNotify(i, j, k, 0, 0);
		world.notifyBlocksOfNeighborChange(i, j, k, blockID);
		
		((PC_TileEntity)world.getBlockTileEntity(i, j, k)).onBlockPickup();
		
		super.breakBlock(world, i, j, k, par5, par6);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new PCnt_TileEntityTeleporter();
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
		
		PCnt_TeleporterData td = null;
		if(entity instanceof EntityPlayer)
			td = PCnt_TeleporterManager.getTeleporterDataAt(((EntityPlayer)entity).dimension, i, j, k);
		else
			td = PCnt_TeleporterManager.getTeleporterDataAt(world.worldInfo.getDimension(), i, j, k);
		
		if(td == null)
			return;
		
		if (entity == null) {
			return;
		}
		
		if(!(entity instanceof EntityLiving || entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow))
			return;
		
		
		if ((entity instanceof EntityAnimal || entity instanceof EntitySquid || entity instanceof EntitySlime) && !td.animals) {
			return;
		}

		if ((entity instanceof EntityMob || entity instanceof EntityGhast || entity instanceof EntityDragon || entity instanceof EntityGolem)
				&& !td.monsters) {
			return;
		}

		if ((entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow) && !td.items) {
			return;
		}

		if ((entity instanceof EntityPlayer) && !td.players) {
			return;
		}

		if ((entity instanceof EntityPlayer) && !entity.isSneaking() && td.sneakTrigger) {
			return;
		}
		
		System.out.println("onEntityCollidedWithBlock");
		PCnt_TeleporterManager.teleportEntityTo(entity, td.defaultTarget);

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
	public static PCnt_TileEntityTeleporter getTE(IBlockAccess iblockaccess, int x, int y, int z) {
		TileEntity te = iblockaccess.getBlockTileEntity(x, y, z);
		if (te == null) {
			return null;
		}
		PCnt_TileEntityTeleporter tet = (PCnt_TileEntityTeleporter) te;

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
