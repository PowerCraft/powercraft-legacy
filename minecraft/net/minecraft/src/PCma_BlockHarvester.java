package net.minecraft.src;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.src.forge.ITextureProvider;


/**
 * Block harvesting machine.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_BlockHarvester extends Block implements PC_ISwapTerrain, PC_IBlockType, PC_ISpecialInventoryTextures, ITextureProvider, PC_IBeamHandler {
	private static final int TXDOWN = 109, TXTOP = 155, TXSIDE = 139, TXFRONT = 107, TXBACK = 123;

	/**
	 * Block which ends the harvesting. 98 = stone brick. Obsidian + bedrock
	 * stop too.
	 */
	public static final int ENDBLOCK = 98;


	@Override
	public String getTerrainFile() {
		return mod_PCmachines.getTerrainFile();
	}

	@Override
	public String getTextureFile() {
		return getTerrainFile();
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
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
			if (m == s) {
				return TXFRONT;
			}
			if ((m == 2 && s == 3) || (m == 3 && s == 2) || (m == 4 && s == 5) || (m == 5 && s == 4)) {
				return TXBACK;
			}
			return TXSIDE;
		}
	}

	@Override
	public int getInvTexture(int i, int m) {
		if (i == 1) {
			return TXTOP;
		}
		if (i == 0) {
			return TXDOWN;
		}
		if (i == 3) {
			return TXFRONT;
		} else if (i == 4) {
			return TXBACK;
		} else {
			return TXSIDE;
		}
	}

	/**
	 * @param i ID
	 */
	protected PCma_BlockHarvester(int i) {
		super(i, Material.ground);
		setStepSound(Block.soundPowderFootstep);
	}

	@Override
	public boolean isBlockSolid(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return true;
	}

	@Override
	public int tickRate() {
		return 4;
	}

	@Override
	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		int l = MathHelper.floor_double(((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;

		if (PC_Utils.isPlacingReversed()) {
			l = PC_Utils.reverseSide(l);
		}

		if (l == 0) {
			l = 2;
		} else if (l == 1) {
			l = 5;
		} else if (l == 2) {
			l = 3;
		} else if (l == 3) {
			l = 4;
		}

		if (isIndirectlyPowered(world, i, j, k)) {
			world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
		}

		world.setBlockMetadataWithNotify(i, j, k, l);
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k) {
		int l = world.getBlockMetadata(i, j, k);
		if (isIndirectlyPowered(world, i, j, k)) {
			harvestBlocks(world, i, j, k, l);

			l |= 8;
		}
		world.setBlockMetadataWithNotify(i, j, k, l);
	}

	private boolean isIndirectlyPowered(World world, int i, int j, int k) {
		// if (world.isBlockGettingPowered(i, j, k)) { return true; }

		if (world.isBlockIndirectlyGettingPowered(i, j, k)) {
			return true;
		}

		// if (world.isBlockGettingPowered(i, j - 1, k)) { return true; }

		if (world.isBlockIndirectlyGettingPowered(i, j - 1, k)) {
			return true;
		}
		return false;
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (l > 0 && Block.blocksList[l].canProvidePower()) {
			boolean flag = isIndirectlyPowered(world, i, j, k);
			if (flag) {
				world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
			}
		}
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		if (isIndirectlyPowered(world, i, j, k)) {
			harvestBlocks(world, i, j, k, world.getBlockMetadata(i, j, k));
		}
	}

	/** Stacks harvested during this flash (including stacks from animals etc.) */
	private ArrayList<ItemStack> drops = new ArrayList<ItemStack>();


	/**
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param deviceMeta
	 */
	private void harvestBlocks(World world, int x, int y, int z, int deviceMeta) {

		deviceMeta &= 0x7;

		int incZ = Facing.offsetsZForSide[deviceMeta];
		int incX = Facing.offsetsXForSide[deviceMeta];

		PC_CoordI move = new PC_CoordI(incX, 0, incZ);

		PC_CoordI cnt = new PC_CoordI(x, y, z);
		PCma_BeamTracer beamTracer = new PCma_BeamTracer(world, this);

		beamTracer.setStartCoord(cnt);
		beamTracer.setStartMove(move);
		beamTracer.setCanChangeColor(false);
		beamTracer.setReflectedByMirror(true);
		beamTracer.setReflectedByPrism(true);
		beamTracer.setDetectEntities(true);
		beamTracer.setParticlesBidirectional(false);
		beamTracer.setTotalLengthLimit(8000);
		beamTracer.setMaxLengthAfterCrystal(2000);
		beamTracer.setStartLength(30);
		beamTracer.setCrystalAddedLength(100);

		PC_Color color = new PC_Color();

		color.setTo(0.001D, 1.0D, 0.001D);
		color.setMeta(2);

		beamTracer.setColor(color);

		if (world.getBlockId(x, y - 1, z) == ENDBLOCK) {
			beamTracer.setStartLength(1);
			beamTracer.setMaxLengthAfterCrystal(1);
		}

		drops.clear();

		beamTracer.flash();

		if (drops != null) {
			PC_InvUtils.mergeStacks(drops);


			for (ItemStack stack : drops) {
				dispenseItem(world, cnt, stack);
			}
		}

	}


	@Override
	public boolean onBlockHit(World world, PC_CoordI coord, PC_CoordI startCoord) {
		int id = coord.getId(world);
		int meta = coord.getMeta(world);

		if (id == 49 || id == 7 || id == ENDBLOCK) {
			return true;
		}


		// sapling on grass
		if (PC_TreeHarvestingManager.isBlockTreeSapling(id, meta)) {
			int underId = coord.offset(0, -1, 0).getId(world);
			if (underId == Block.dirt.blockID || underId == Block.grass.blockID || underId == Block.mycelium.blockID) {
				return false;
			}
		}

		if (PC_BlockUtils.hasFlag(world, coord, "HARVEST_STOP")) {
			return true;
		}

		// SKIP non-breaking
		if (id == 0 || id == Block.glass.blockID || id == Block.thinGlass.blockID || id == Block.redstoneLampActive.blockID || id == Block.redstoneLampIdle.blockID || Block.blocksList[id] == null || id == 8 || id == 9 || id == 10 || id == 11
				|| id == Block.sapling.blockID || id == Block.pumpkinStem.blockID || id == Block.melonStem.blockID || id == Block.cake.blockID || id == Block.fire.blockID || Block.blocksList[id] instanceof BlockTorch
				|| id == Block.redstoneWire.blockID || id == Block.lever.blockID || id == Block.button.blockID || Block.blocksList[id] instanceof BlockRedstoneRepeater || id == Block.pistonStickyBase.blockID || id == Block.pistonBase.blockID
				|| id == Block.pistonExtension.blockID || id == Block.pistonMoving.blockID || Block.blocksList[id] instanceof BlockRail || PC_BlockUtils.getBlockFlags(world, coord).contains("NO_HARVEST")) {

			return false;
		}

		if (PC_BlockUtils.hasFlag(world, coord, "POWERCRAFT")) {
			if (PC_BlockUtils.hasFlag(world, coord, "TRANSLUCENT")) {
				return false;
			}
		}



		// tree - replace with sapling
		if (PC_TreeHarvestingManager.isBlockTreeWood(id, meta)) {
			ItemStack[] output = PC_TreeHarvestingManager.harvestTreeAt(world, coord);

			if (output != null) {
				for (ItemStack stack : output) {
					addToDispenseList(stack);
				}
			}

			return true;
			// return false;
		}

		// crops

		// block implementing ICropBlock
		if (Block.blocksList[id] instanceof PC_ICropBlock) {
			if (!((PC_ICropBlock) Block.blocksList[id]).isMature(world, coord)) {
				// immature, continue
				return false;
			} else {

				// play breaking sound and animation
				if (mod_PCcore.soundsEnabled) {
					world.playAuxSFX(2001, coord.x, coord.y, coord.z, id + (meta << 12));
				}

				ItemStack[] harvested = ((PC_ICropBlock) Block.blocksList[id]).machineHarvest(world, coord);

				if (harvested != null) {

					for (ItemStack stack : harvested) {
						addToDispenseList(stack);
					}
				}
			}

		}

		// block registered using XML
		if (PC_CropHarvestingManager.isBlockRegisteredCrop(id)) {

			if (PC_CropHarvestingManager.canHarvestBlock(id, meta)) {

				ItemStack[] harvested = PC_CropHarvestingManager.getHarvestedStacks(id, meta);

				if (harvested != null) {

					for (ItemStack stack : harvested) {

						// play breaking sound and animation
						if (mod_PCcore.soundsEnabled) {
							world.playAuxSFX(2001, coord.x, coord.y, coord.z, id + (meta << 12));
						}

						addToDispenseList(stack);
					}

				}

				int newMeta = PC_CropHarvestingManager.getReplantMeta(id);

				if (newMeta == -1) {
					world.setBlockWithNotify(coord.x, coord.y, coord.z, 0);
				} else {
					world.setBlockMetadataWithNotify(coord.x, coord.y, coord.z, newMeta);
				}

				return true;

			}

			return false;

		}

		// ignore inventory blocks
		if (world.getBlockTileEntity(coord.x, coord.y, coord.z) != null && world.getBlockTileEntity(coord.x, coord.y, coord.z) instanceof IInventory) {
			return false;
		}

		// now regular block breaking
		int dropId;
		int dropMeta;
		int dropQuant;

		dropId = Block.blocksList[id].idDropped(id, world.rand, meta);
		dropMeta = Block.blocksList[id].damageDropped(meta);
		dropQuant = Block.blocksList[id].quantityDropped(world.rand);

		// play breaking sound and animation
		if (mod_PCcore.soundsEnabled) {
			world.playAuxSFX(2001, coord.x, coord.y, coord.z, id + (meta << 12));
		}

		// set air, or water in case of ice
		world.setBlockWithNotify(coord.x, coord.y, coord.z, id == Block.ice.blockID ? Block.waterMoving.blockID : 0);

		if (id == Block.tallGrass.blockID) {
			dropId = Item.seeds.shiftedIndex;
			if (world.rand.nextInt(5) != 0) {
				return true;
			} // dddd
		}

		if (dropId <= 0) {
			dropId = id;
		}

		if (dropQuant <= 0) {
			dropQuant = 1;
		}

		addToDispenseList(new ItemStack(dropId, dropQuant, dropMeta));

		return true;
		// return false;
	}


	@Override
	public boolean onEntityHit(World world, Entity[] array, PC_CoordI startCoord) {

		for (Entity entity : array) {

			if (entity == null) continue;

			if (entity instanceof EntityMinecart) {

				EntityMinecart cart = (EntityMinecart) entity;

				if (cart.isDead) {
					continue;
				}

				int l = world.getBlockMetadata(startCoord.x, startCoord.y, startCoord.z) & 7;

				int iPLUS1 = -Facing.offsetsXForSide[l];
				int kPLUS1 = -Facing.offsetsZForSide[l];

				cart.posX = startCoord.x + iPLUS1 * 1.5D;
				cart.posY = startCoord.y;
				cart.posZ = startCoord.z + kPLUS1 * 1.5D;
				cart.attackEntityFrom(DamageSource.generic, 1000);

			} else if (entity instanceof EntitySheep) {

				EntitySheep sheep = (EntitySheep) entity;

				if (sheep.isDead) {
					continue;
				}

				if (!sheep.getSheared()) {
					sheep.setSheared(true);
					addToDispenseList(new ItemStack(Block.cloth.blockID, 1 + world.rand.nextInt(3), sheep.getFleeceColor()));
				}

			} else if (entity instanceof EntityMooshroom) {

				EntityMooshroom mooshroom = (EntityMooshroom) entity;

				if (mooshroom.isDead) {
					continue;
				}

				if (mooshroom.getGrowingAge() >= 0) {

					EntityCow entitycow = new EntityCow(world);
					entitycow.setLocationAndAngles(mooshroom.posX, mooshroom.posY, mooshroom.posZ, mooshroom.rotationYaw, mooshroom.rotationPitch);
					entitycow.setEntityHealth(mooshroom.getHealth());
					entitycow.renderYawOffset = mooshroom.renderYawOffset;

					mooshroom.setDead();
					mooshroom.deathTime = 0;
					world.spawnParticle("largeexplode", mooshroom.posX, mooshroom.posY + (mooshroom.height / 2.0F), mooshroom.posZ, 0.0D, 0.0D, 0.0D);
					world.spawnEntityInWorld(entitycow);

					addToDispenseList(new ItemStack(Block.mushroomRed.blockID, 1 + world.rand.nextInt(5), 0));

				}

			}

		}
		return false;
	}

	private void addToDispenseList(ItemStack stack) {
		drops.add(stack);
	}


	private void dispenseItem(World world, PC_CoordI devPos, ItemStack itemstack) {

		if (itemstack == null || itemstack.stackSize <= 0) {
			return;
		}

		int l = world.getBlockMetadata(devPos.x, devPos.y, devPos.z) & 7;

		int dispIncX = -Facing.offsetsXForSide[l];
		int dispIncZ = -Facing.offsetsZForSide[l];

		double dx = devPos.x + dispIncX * 0.59999999999999998D + 0.5D;
		double dy = devPos.y + 0.5D;
		double dz = devPos.z + dispIncZ * 0.59999999999999998D + 0.5D;

		EntityItem entityitem = new EntityItem(world, dx, dy - 0.29999999999999999D, dz, itemstack);
		double throwSpeed = world.rand.nextDouble() * 0.10000000000000001D + 0.20000000000000001D;

		Set<String> blocktype = PC_BlockUtils.getBlockFlags(world, devPos.offset(dispIncX, 0, dispIncZ));

		if (blocktype.contains("BELT") || blocktype.contains("LIFT")) {
			entityitem.motionX = 0;
			entityitem.motionY = 0;
			entityitem.motionZ = 0;
		} else {
			entityitem.motionX = dispIncX * throwSpeed;
			entityitem.motionY = 0.20000000298023224D;
			entityitem.motionZ = dispIncZ * throwSpeed;
		}

		entityitem.delayBeforeCanPickup = 5;
		world.spawnEntityInWorld(entityitem);
		if (mod_PCcore.soundsEnabled) {
			world.playAuxSFX(1000, devPos.x, devPos.y, devPos.z, 0);
		}

		world.playAuxSFX(2000, devPos.x, devPos.y, devPos.z, dispIncX + 1 + (dispIncZ + 1) * 3);

	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	@Override
	public int getMobilityFlag() {
		return 0;
	}

	@Override
	public Set<String> getBlockFlags(World world, PC_CoordI pos) {

		Set<String> set = new HashSet<String>();

		set.add("NO_HARVEST");
		set.add("HARVEST_STOP");
		set.add("HARVESTER");
		set.add("REDSTONE");
		set.add("MACHINE");

		return set;
	}

	@Override
	public Set<String> getItemFlags(int damage) {
		Set<String> set = new HashSet<String>();
		set.add("NO_BUILD");
		return set;
	}
}
