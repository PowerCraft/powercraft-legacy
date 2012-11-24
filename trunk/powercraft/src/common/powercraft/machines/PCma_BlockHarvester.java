package powercraft.machines;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.src.Block;
import net.minecraft.src.BlockRail;
import net.minecraft.src.BlockRedstoneRepeater;
import net.minecraft.src.BlockTorch;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityCow;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EntityMooshroom;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.Facing;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;
import powercraft.core.PC_BeamTracer;
import powercraft.core.PC_Block;
import powercraft.core.PC_Color;
import powercraft.core.PC_CoordI;
import powercraft.core.PC_IBeamHandler;
import powercraft.core.PC_ICraftingToolDisplayer;
import powercraft.core.PC_ISpecialInventoryTextures;
import powercraft.core.PC_InvUtils;
import powercraft.core.PC_Utils;

public class PCma_BlockHarvester extends PC_Block implements
		PC_ISpecialInventoryTextures, PC_IBeamHandler, PC_ICraftingToolDisplayer {

	private static final int TXDOWN = 109, TXTOP = 155, TXSIDE = 139, TXFRONT = 107, TXBACK = 123;
	/**
	 * Block which ends the harvesting. 98 = stone brick. Obsidian + bedrock
	 * stop too.
	 */
	public static final int ENDBLOCK = 98;
	
	public PCma_BlockHarvester(int id) {
		super(id, TXSIDE, Material.ground);
		setHardness(0.7F);
		setResistance(10.0F);
		setStepSound(Block.soundStoneFootstep);
		setCreativeTab(CreativeTabs.tabDecorations);
	}
	
	@Override
	public String getDefaultName() {
		return "Harvester";
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
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
	
	@Override
	public boolean isBlockSolid(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return true;
	}

	@Override
	public int tickRate() {
		return 1;
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		int l = MathHelper.floor_double(((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;

		if (entityliving instanceof EntityPlayer && PC_Utils.isPlacingReversed((EntityPlayer)entityliving)) {
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

		//if(!world.isRemote)
		//	PC_Utils.setBlock(null, this, x, y, z, deviceMeta);
		
		deviceMeta &= 0x7;

		int incZ = Facing.offsetsZForSide[deviceMeta];
		int incX = Facing.offsetsXForSide[deviceMeta];

		PC_CoordI move = new PC_CoordI(incX, 0, incZ);

		PC_CoordI cnt = new PC_CoordI(x, y, z);
		PC_BeamTracer beamTracer = new PC_BeamTracer(world, this);

		beamTracer.setStartCoord(cnt);
		beamTracer.setStartMove(move);
		beamTracer.setCanChangeColor(false);
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

		if(!world.isRemote)
			drops.clear();

		beamTracer.flash();

		if (drops != null && !world.isRemote) {
			PC_InvUtils.groupStacks(drops);


			for (ItemStack stack : drops) {
				dispenseItem(world, cnt, stack);
			}
		}

	}
	
	
	@Override
	public boolean onBlockHit(PC_BeamTracer beamTracer, Block block, PC_CoordI coord) {
		World world = beamTracer.getWorld();
		int id = coord.getId(world);
		int meta = coord.getMeta(world);

		if (id == 49 || id == 7 || id == ENDBLOCK) {
			return true;
		}


		// sapling on grass
		if (PCma_TreeHarvestingManager.isBlockTreeSapling(id, meta)) {
			int underId = coord.offset(0, -1, 0).getId(world);
			if (underId == Block.dirt.blockID || underId == Block.grass.blockID || underId == Block.mycelium.blockID) {
				return false;
			}
		}

		Block b = PC_Utils.getBlock(world, coord.x, coord.y, coord.z);
		
		if(b instanceof PC_Block && !((PC_Block)b).canBeHarvest()){
			return true;
		}

		// SKIP non-breaking
		if (id == 0 || id == Block.glass.blockID || id == Block.thinGlass.blockID || id == Block.redstoneLampActive.blockID
				|| id == Block.redstoneLampIdle.blockID || Block.blocksList[id] == null || id == 8 || id == 9 || id == 10 || id == 11
				|| id == Block.sapling.blockID || id == Block.pumpkinStem.blockID || id == Block.melonStem.blockID || id == Block.cake.blockID
				|| id == Block.fire.blockID || Block.blocksList[id] instanceof BlockTorch || id == Block.redstoneWire.blockID
				|| id == Block.lever.blockID || id == Block.woodenButton.blockID || id == Block.stoneButton.blockID || Block.blocksList[id] instanceof BlockRedstoneRepeater
				|| id == Block.pistonStickyBase.blockID || id == Block.pistonBase.blockID || id == Block.pistonExtension.blockID
				|| id == Block.pistonMoving.blockID || Block.blocksList[id] instanceof BlockRail) {

			return false;
		}

		if(id == Block.cocoaPlant.blockID){
			if(((meta & 12) >> 2)<2){
				return false;
			}else{
				if(!world.isRemote)
					addToDispenseList(new ItemStack(Item.dyePowder.shiftedIndex, 3, 3));
				world.setBlockWithNotify(coord.x, coord.y, coord.z, 0);
				return true;
			}
		}



		// tree - replace with sapling
		if (PCma_TreeHarvestingManager.isBlockTreeWood(id, meta)) {
			ItemStack[] output = PCma_TreeHarvestingManager.harvestTreeAt(world, coord);

			if (output != null) {
				if(!world.isRemote)
					for (ItemStack stack : output) {
						addToDispenseList(stack);
					}
			}

			return true;
			// return false;
		}

		// block registered using XML
		if (PCma_CropHarvestingManager.isBlockRegisteredCrop(id)) {

			if (PCma_CropHarvestingManager.canHarvestBlock(id, meta)) {

				ItemStack[] harvested = PCma_CropHarvestingManager.getHarvestedStacks(id, meta);

				if (harvested != null) {

					for (ItemStack stack : harvested) {

						// play breaking sound and animation
						if (PC_Utils.isSoundEnabled()) {
							world.playAuxSFX(2001, coord.x, coord.y, coord.z, id + (meta << 12));
						}

						if(!world.isRemote)
							addToDispenseList(stack);
					}

				}

				int newMeta = PCma_CropHarvestingManager.getReplantMeta(id);

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
		if (PC_Utils.isSoundEnabled()) {
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

		if(!world.isRemote)
			addToDispenseList(new ItemStack(dropId, dropQuant, dropMeta));

		return true;
		// return false;
	}


	@Override
	public boolean onEntityHit(PC_BeamTracer beamTracer, Entity entity, PC_CoordI coord) {

		World world = beamTracer.getWorld();
		
		if (entity == null) return true;

		if (entity instanceof EntityMinecart) {

			EntityMinecart cart = (EntityMinecart) entity;

			if (cart.isDead) {
				return true;
			}

			int l = PC_Utils.getMD(world, coord.x, coord.y, coord.z) & 7;

			int iPLUS1 = -Facing.offsetsXForSide[l];
			int kPLUS1 = -Facing.offsetsZForSide[l];

			PC_CoordI startCoord = beamTracer.getStartCoord();
			
			cart.posX = startCoord.x + iPLUS1 * 1.5D;
			cart.posY = startCoord.y;
			cart.posZ = startCoord.z + kPLUS1 * 1.5D;
			cart.attackEntityFrom(DamageSource.generic, 1000);

		} else if (entity instanceof EntitySheep) {

			EntitySheep sheep = (EntitySheep) entity;

			if (sheep.isDead) {
				return true;
			}

			if (!sheep.getSheared()) {
				sheep.setSheared(true);
				if(!world.isRemote)
					addToDispenseList(new ItemStack(Block.cloth.blockID, 1 + world.rand.nextInt(3), sheep.getFleeceColor()));
			}

		} else if (entity instanceof EntityMooshroom) {

			EntityMooshroom mooshroom = (EntityMooshroom) entity;

			if (mooshroom.isDead) {
				return true;
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

				if(!world.isRemote)
					addToDispenseList(new ItemStack(Block.mushroomRed.blockID, 1 + world.rand.nextInt(5), 0));

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

		/**@TOTO Set<String> blocktype = PC_BlockUtils.getBlockFlags(world, devPos.offset(dispIncX, 0, dispIncZ));

		if (blocktype.contains("BELT") || blocktype.contains("LIFT")) {
			entityitem.motionX = 0;
			entityitem.motionY = 0;
			entityitem.motionZ = 0;
		} else {*/
			entityitem.motionX = dispIncX * throwSpeed;
			entityitem.motionY = 0.20000000298023224D;
			entityitem.motionZ = dispIncZ * throwSpeed;
		//}

		entityitem.delayBeforeCanPickup = 5;
		world.spawnEntityInWorld(entityitem);
		if (PC_Utils.isSoundEnabled()) {
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
	public String getCraftingToolModule() {
		return mod_PowerCraftMachines.getInstance().getNameWithoutPowerCraft();
	}

	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this));
		return arrayList;
	}
	
}
