package net.minecraft.src;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.nfunk.jep.ParseException;

import net.minecraft.src.forge.ITextureProvider;
import net.minecraft.src.weasel.Calculator;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;
import net.minecraft.src.weasel.obj.WeaselBoolean;
import net.minecraft.src.weasel.obj.WeaselVariableMap;

/**
 * Logic gate and other flat sensors
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_BlockGate extends BlockContainer implements PC_IRotatedBox, PC_ISwapTerrain, PC_IBlockType, ITextureProvider {

	/** Flag that this gate is active (glowing) */
	public boolean active;

	private static boolean changingState;
	
    public int tickRate()
    {
        return 3;
    }

	/**
	 * gate block
	 * 
	 * @param id block ID
	 * @param flag is active (glowing)
	 */
	protected PClo_BlockGate(int id, boolean flag) {
		super(id, Material.circuits);
		active = flag;
		blockIndexInTexture = 6;
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
	}

	@Override
	public boolean renderItemHorizontal() {
		return false;
	}

	@Override
	public TileEntity getBlockEntity() {
		return new PClo_TileEntityGate();
	}

	@Override
	public String getTextureFile() {
		return getTerrainFile();
	}

	/**
	 * Get gate tile entity at position
	 * 
	 * @param iblockaccess block access
	 * @param x
	 * @param y
	 * @param z
	 * @return the tile entity or null
	 */
	public PClo_TileEntityGate getTE(IBlockAccess iblockaccess, int x, int y, int z) {
		TileEntity te = iblockaccess.getBlockTileEntity(x, y, z);
		if (te == null) { return null; }
		return (PClo_TileEntityGate) te;
	}

	/**
	 * Get gate type
	 * 
	 * @param iblockaccess world
	 * @param x x
	 * @param y y
	 * @param z z
	 * @return type index
	 */
	public int getType(IBlockAccess iblockaccess, int x, int y, int z) {
		return getTE(iblockaccess, x, y, z).gateType;
	}

	/**
	 * Set gate type
	 * 
	 * @param iblockaccess world
	 * @param x x
	 * @param y y
	 * @param z z
	 * @param type type index
	 */
	public void setType(IBlockAccess iblockaccess, int x, int y, int z, int type) {
		getTE(iblockaccess, x, y, z).gateType = type;
	}

	private boolean getOldClock(IBlockAccess iblockaccess, int x, int y, int z) {
		return getTE(iblockaccess, x, y, z).prevClockState;
	}

	private void setClock(IBlockAccess iblockaccess, int x, int y, int z, boolean clk) {
		getTE(iblockaccess, x, y, z).prevClockState = clk;
	}

	@Override
	public int getBlockTexture(IBlockAccess iblockaccess, int x, int y, int z, int side) {
		if (side == 1) {
			// top face!

			int index = getTE(iblockaccess, x, y, z).gateType;

			if (index == PClo_GateType.CROSSING) {
				int variant = getTE(iblockaccess, x, y, z).getCrossingVariant();
				switch (variant) {
					case 0:
						return 48;
					case 1:
						return 49;
					case 2:
						return 50;
					case 3:
						return 51;
				}

				return 48;
			}

			return getTopFaceFromEnum(index) + (active ? 16 : 0);
		}

		if (side == 0) { return 6; }
		return 5;
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		if (side == 0) { return 6; // stone slab particles
		}
		if (side == 1) {
			if (meta == PClo_GateType.CROSSING) { return 48; }
			return getTopFaceFromEnum(meta) + 16; // top face
		} else {
			return 5; // side
		}
	}

	private int getTopFaceFromEnum(int meta) {
		if (meta > PClo_GateType.CROSSING) {
			meta--;
		}
		if (meta <= 15) {
			return 16 + meta;
		} else {
			return 64 + meta;
		}
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int x, int y, int z, int side) {
		return side != 1;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return PC_Renderer.rotatedBoxRenderer;
	}

	@Override
	public int getRotation(int meta) {
		return getRotation_static(meta);
	}

	/**
	 * Get gate rotation, same as getRotation, but available statically
	 * 
	 * @param meta block meta
	 * @return rotation 0,1,2,3
	 */
	public static int getRotation_static(int meta) {
		return meta & 3;
	}

	@Override
	public String getTerrainFile() {
		return mod_PClogic.getTerrainFile();
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
	public void onBlockRemoval(World world, int x, int y, int z) {
		if (!changingState) {
			// drop the gate
			PClo_TileEntityGate teg = getTE(world, x, y, z);

			if (teg != null) {

				if (teg.gateType == PClo_GateType.SPECIAL) {
					unpausePausedBlocks(world, x, y, z);
				}

				teg.zombie = true;

				if (!PC_Utils.isCreative()) {
					dropBlockAsItem_do(world, x, y, z, new ItemStack(mod_PClogic.gateOn, 1, teg.gateType));
				}
			}
		}

		super.onBlockRemoval(world, x, y, z);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		int under = world.getBlockId(x, y - 1, z);
		if (Block.blocksList[under] != null && Block.blocksList[under] instanceof PClo_BlockGate) { return false; }

		// if (!world.isBlockNormalCube(x, y - 1, z)) {
		// return false;
		// } else {
		// return super.canPlaceBlockAt(world, x, y, z);
		// }
		return true;
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		// if (!world.isBlockNormalCube(x, y - 1, z)) {
		// return false;
		// } else {
		// return super.canBlockStay(world, x, y, z);
		// }
		return true;
	}

	// end of block setup

	// start of logic

	@Override
	public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int side) {
		return isPoweringTo(world, x, y, z, side);
	}

	private boolean checkEmptyChest(World world, int x, int y, int z) {
		return getTE(world, x, y, z).isChestEmpty();
	}

	private boolean checkFullChest(World world, int x, int y, int z) {
		return getTE(world, x, y, z).isChestFull();
	}

	private boolean isOutputActive(World world, int x, int y, int z) {

		if (getType(world, x, y, z) == PClo_GateType.DAY) { return world.isDaytime(); }
		if (getType(world, x, y, z) == PClo_GateType.RAIN) { return world.isRaining(); }
		if (getType(world, x, y, z) == PClo_GateType.CHEST_EMPTY) { return checkEmptyChest(world, x, y, z); }
		if (getType(world, x, y, z) == PClo_GateType.CHEST_FULL) { return checkFullChest(world, x, y, z); }

		if (getType(world, x, y, z) == PClo_GateType.SPECIAL) { return powered_from_input(world, x, y, z, 0); }
		if (getType(world, x, y, z) == PClo_GateType.FIFO_DELAYER) { return getTE(world, x, y, z).getBufferOutput(); }

		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		boolean q = false;
		if (tileEntity instanceof PClo_TileEntityGate) {
			q = getResult(getType(world, x, y, z), powered_from_input(world, x, y, z, 0), powered_from_input(world, x, y, z, 1),
					powered_from_input(world, x, y, z, 2), (PClo_TileEntityGate) tileEntity);
		}
		return q;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {

		int type = getType(world, x, y, z);
		boolean on, state;

		switch (type) {
			case PClo_GateType.CROSSING:

				boolean[] powered = { powered_from_input(world, x, y, z, 0), powered_from_input(world, x, y, z, 1), powered_from_input(
						world, x, y, z, 2), powered_from_input(world, x, y, z, 3) };

				if (!Arrays.equals(powered, getTE(world, x, y, z).powered)) {
					getTE(world, x, y, z).powered = powered;
					world.notifyBlockChange(x, y, z, blockID);
				}

				return;

			case PClo_GateType.SPECIAL:
				on = powered_from_input(world, x, y, z, 0);
				if (!on) {
					unpausePausedBlocks(world, x, y, z);
				}

				if (on && !active) {
					// do spawning
					spawnMobsFromSpawners(world, x, y, z);

					changeGateState(true, world, x, y, z);
				} else if (!on && active) {
					changeGateState(false, world, x, y, z);
				}

				break;

			case PClo_GateType.DAY:
				if (world.isDaytime() && !active) {
					changeGateState(true, world, x, y, z);
				} else if (!world.isDaytime() && active) {
					changeGateState(false, world, x, y, z);
				}
				break;

			case PClo_GateType.FIFO_DELAYER:

				state = getTE(world, x, y, z).getBufferOutput();

				if (state && !active) {
					changeGateState(true, world, x, y, z);
				} else if (!state && active) {
					changeGateState(false, world, x, y, z);
				}

				break;

			case PClo_GateType.HOLD_DELAYER:

				boolean inp = powered_from_input(world, x, y, z, 0);

				if (inp) {
					getTE(world, x, y, z).repeaterStartHolding();
				}

				state = getTE(world, x, y, z).isRepeaterOutputActive();

				if (state && !active) {
					changeGateState(true, world, x, y, z);
				} else if (!state && active) {
					changeGateState(false, world, x, y, z);
				}

				break;

			case PClo_GateType.RAIN:
				if (world.isRaining() && !active) {
					changeGateState(true, world, x, y, z);
				} else if (!world.isRaining() && active) {
					changeGateState(false, world, x, y, z);
				}

				break;

			case PClo_GateType.CHEST_EMPTY:
				boolean empty = checkEmptyChest(world, x, y, z);
				if (empty && !active) {
					changeGateState(true, world, x, y, z);
				} else if (!empty && active) {
					changeGateState(false, world, x, y, z);
				}

				break;

			case PClo_GateType.CHEST_FULL:
				boolean full = checkFullChest(world, x, y, z);
				if (full && !active) {
					changeGateState(true, world, x, y, z);
				} else if (!full && active) {
					changeGateState(false, world, x, y, z);
				}

				break;

			case PClo_GateType.D:
				int CLOCK = 2;
				int DATA = 0;
				int RST = 1;
				// D flip flop
				if (powered_from_input(world, x, y, z, RST)) {
					changeGateState(false, world, x, y, z);
					setClock(world, x, y, z, powered_from_input(world, x, y, z, CLOCK));
				} else if (powered_from_input(world, x, y, z, CLOCK)) { // clock
																		// on
					if (getOldClock(world, x, y, z) == false) {// clock was zero
																// before, thus
																// RISING EDGE
						if (powered_from_input(world, x, y, z, DATA)) {
							// D is on
							setClock(world, x, y, z, true);
							changeGateState(true, world, x, y, z);
						} else {
							// D is off
							setClock(world, x, y, z, true);
							changeGateState(false, world, x, y, z);
						}
					} else {
						// clock was on before, and is still on
						// do nothing
					}
				} else {
					// clock is off
					if (getOldClock(world, x, y, z) == true) {
						// clock was on before - set off
						setClock(world, x, y, z, false);
					}
				}

				break;

			case PClo_GateType.RS:
				int R = 2;
				int S = 1;
				on = isActive();
				// RS flip flop
				if (!on && powered_from_input(world, x, y, z, S) && !powered_from_input(world, x, y, z, R)) {
					// turn it on
					changeGateState(true, world, x, y, z);
				} else if (on && powered_from_input(world, x, y, z, R) && !powered_from_input(world, x, y, z, S)) {
					changeGateState(false, world, x, y, z);
				}

				break;

			case PClo_GateType.T:
				int T = 0;
				if (powered_from_input(world, x, y, z, 1) || powered_from_input(world, x, y, z, 2)) {
					changeGateState(false, world, x, y, z);
					setClock(world, x, y, z, powered_from_input(world, x, y, z, T));
				} else if (powered_from_input(world, x, y, z, T)) { // clock on
					if (getOldClock(world, x, y, z) == false) {// clock was zero
																// before, thus
																// RISING EDGE
						setClock(world, x, y, z, true);
						if (!isActive()) {// if off, turn on
							changeGateState(true, world, x, y, z);
						} else {
							changeGateState(false, world, x, y, z);
						}
					}
				} else {
					// clock is off
					if (getOldClock(world, x, y, z) == true) {
						// clock was on before - set off
						setClock(world, x, y, z, false);
					}
				}

				break;

			case PClo_GateType.RANDOM:
				int Ri = 0;
				if (powered_from_input(world, x, y, z, Ri)) { // clock on
					if (getOldClock(world, x, y, z) == false) {// clock was zero
																// before, thus
																// RISING EDGE
						setClock(world, x, y, z, true);

						changeGateState(random.nextBoolean(), world, x, y, z);
					}
				} else {
					// clock is off
					if (getOldClock(world, x, y, z) == true) {
						// clock was on before - set off
						setClock(world, x, y, z, false);
					}
				}

				break;

			// BASIC GATES
			default:

				on = isActive();

				boolean outputActive = isOutputActive(world, x, y, z);

				if (on && !outputActive) {
					// turn off
					changeGateState(false, world, x, y, z);
				} else if (!on && outputActive) {
					// turn on
					changeGateState(true, world, x, y, z);
				}
		}
	}

	// mob spawning procedure
	private void spawnMobsFromSpawners(World world, int x, int y, int z) {
		TileEntity te = world.getBlockTileEntity(x + 1, y, z);
		if (te != null && te instanceof TileEntityMobSpawner) {
			spawnMobs(world, x, y, z, ((TileEntityMobSpawner) te).getMobID());
		}

		te = world.getBlockTileEntity(x - 1, y, z);
		if (te != null && te instanceof TileEntityMobSpawner) {
			spawnMobs(world, x, y, z, ((TileEntityMobSpawner) te).getMobID());
		}

		te = world.getBlockTileEntity(x, y, z + 1);
		if (te != null && te instanceof TileEntityMobSpawner) {
			spawnMobs(world, x, y, z, ((TileEntityMobSpawner) te).getMobID());
		}

		te = world.getBlockTileEntity(x, y, z - 1);
		if (te != null && te instanceof TileEntityMobSpawner) {
			spawnMobs(world, x, y, z, ((TileEntityMobSpawner) te).getMobID());
		}

		te = world.getBlockTileEntity(x, y + 1, z);
		if (te != null && te instanceof TileEntityMobSpawner) {
			spawnMobs(world, x, y, z, ((TileEntityMobSpawner) te).getMobID());
		}
	}

	private boolean shouldSpawnParticles(World world, int x, int y, int z) {
		return world.getClosestPlayer(x + 0.5D, y + 0.5D, z + 0.5D, 16D) != null;
	}

	/**
	 * Spawn blocks near this Special Controller
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param type Mob "name" string
	 */
	private void spawnMobs(World world, int x, int y, int z, String type) {
		byte count = 5;

		boolean spawnParticles = shouldSpawnParticles(world, x, y, z);

		for (int q = 0; q < count; q++) {
			EntityLiving entityliving = (EntityLiving) EntityList.createEntityByName(type, world);
			if (entityliving == null) { return; }
			int c = world.getEntitiesWithinAABB(entityliving.getClass(),
					AxisAlignedBB.getBoundingBoxFromPool(x, y, z, x + 1, y + 1, z + 1).expand(8D, 4D, 8D)).size();
			if (c >= 6) {
				if (spawnParticles) {
					double d = world.rand.nextGaussian() * 0.02D;
					double d1 = world.rand.nextGaussian() * 0.02D;
					double d2 = world.rand.nextGaussian() * 0.02D;
					world.spawnParticle("smoke", x + 0.5D, y + 0.4D, z + 0.5D, d, d1, d2);
				}
				return;
			}

			double d3 = x + (world.rand.nextDouble() - world.rand.nextDouble()) * 3D;
			double d4 = (y + world.rand.nextInt(3)) - 1;
			double d5 = z + (world.rand.nextDouble() - world.rand.nextDouble()) * 3D;
			entityliving.setLocationAndAngles(d3, d4, d5, world.rand.nextFloat() * 360F, 0.0F);
			if (world.checkIfAABBIsClear(entityliving.boundingBox)
					&& world.getCollidingBoundingBoxes(entityliving, entityliving.boundingBox).size() == 0) {
				world.spawnEntityInWorld(entityliving);
				if (spawnParticles) {
					world.playAuxSFX(2004, x, y, z, 0);
					entityliving.spawnExplosionParticle();
				}
				return;
			}
		}
	}

	// end of mob spawning

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int x, int y, int z) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	/**
	 * Change the gate block from on (lighting) to off state, and preserve the tile entity.
	 * 
	 * @param state new state, on or off
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	private void changeGateState(boolean state, World world, int x, int y, int z) {
		int l = world.getBlockMetadata(x, y, z);
		TileEntity tileentity = world.getBlockTileEntity(x, y, z);

		changingState = true;
		if (state) {
			world.setBlockWithNotify(x, y, z, mod_PClogic.gateOn.blockID);
		} else {
			world.setBlockWithNotify(x, y, z, mod_PClogic.gateOff.blockID);
		}

		world.setBlockMetadataWithNotify(x, y, z, l);
		changingState = false;
		if (tileentity != null) {
			tileentity.validate();
			world.setBlockTileEntity(x, y, z, tileentity);
		}
		hugeUpdate(world, x, y, z, blockID);
	}

	/**
	 * Is this gate flipflop or other pulse triggered gate?
	 * 
	 * @param type gate type
	 * @return is special
	 */
	private boolean isSpecialDevice(int type) {
		return type == PClo_GateType.D || type == PClo_GateType.RS || type == PClo_GateType.T || type == PClo_GateType.RANDOM
				|| type == PClo_GateType.HOLD_DELAYER;
	}

	/**
	 * Check if this gate has two outputs.<br>
	 * Currently only RS gate.
	 * 
	 * @param type gate type
	 * @return has 2 outputs
	 */
	private boolean hasTwoOutputs(int type) {
		return type == PClo_GateType.RS;
	}

	/**
	 * Enable spawnign and run pulsars.<br>
	 * Called when special controller is destroyed.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	private void unpausePausedBlocks(World world, int x, int y, int z) {
		int type = getType(world, x, y, z);
		if (type == PClo_GateType.SPECIAL) {
			TileEntity te = world.getBlockTileEntity(x + 1, y, z);
			if (te != null && te instanceof PClo_TileEntityPulsar) {
				((PClo_TileEntityPulsar) te).paused = false;
			}

			te = world.getBlockTileEntity(x - 1, y, z);
			if (te != null && te instanceof PClo_TileEntityPulsar) {
				((PClo_TileEntityPulsar) te).paused = false;
			}

			te = world.getBlockTileEntity(x, y, z + 1);
			if (te != null && te instanceof PClo_TileEntityPulsar) {
				((PClo_TileEntityPulsar) te).paused = false;
			}

			te = world.getBlockTileEntity(x, y, z - 1);
			if (te != null && te instanceof PClo_TileEntityPulsar) {
				((PClo_TileEntityPulsar) te).paused = false;
			}

			te = world.getBlockTileEntity(x, y + 1, z);
			if (te != null && te instanceof PClo_TileEntityPulsar) {
				((PClo_TileEntityPulsar) te).paused = false;
			}

			te = world.getBlockTileEntity(x, y - 1, z);
			if (te != null && te instanceof PClo_TileEntityPulsar) {
				((PClo_TileEntityPulsar) te).paused = false;
			}
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int l) {
		/*
		 * if (!canBlockStay(world, i, j, k)) {
		 * unpausePausedBlocks(world, i, j, k);
		 * world.setBlockWithNotify(i, j, k, 0);
		 * return;
		 * }
		 */

		int type = getType(world, x, y, z);
		if (type == PClo_GateType.FIFO_DELAYER) { return; }

		if (type == PClo_GateType.CROSSING) {
			world.scheduleBlockUpdate(x, y, z, blockID, tickRate());
		}

		if (type == PClo_GateType.DAY || type == PClo_GateType.RAIN || type == PClo_GateType.CHEST_EMPTY
				|| type == PClo_GateType.CHEST_FULL) {
			world.scheduleBlockUpdate(x, y, z, blockID, tickRate());
			return;
		}

		if (isSpecialDevice(getType(world, x, y, z))) {
			world.scheduleBlockUpdate(x, y, z, blockID, tickRate());
			return;
		}

		boolean outputActive = isOutputActive(world, x, y, z);
		boolean on = isActive();

		if (on && !outputActive) {
			world.scheduleBlockUpdate(x, y, z, blockID, tickRate());
		} else if (!on && outputActive) {
			world.scheduleBlockUpdate(x, y, z, blockID, tickRate());
		}
	}

	@Override
	public boolean isPoweringTo(IBlockAccess iblockaccess, int x, int y, int z, int side) {
		int meta = iblockaccess.getBlockMetadata(x, y, z);
		int rotation = getRotation(meta);

		int type = getType(iblockaccess, x, y, z);

		if (type == PClo_GateType.CROSSING) {

			World world = ModLoader.getMinecraftInstance().theWorld;

			// check for rotation and variant.
			int variant = getTE(iblockaccess, x, y, z).getCrossingVariant();

			switch (variant) {
				case 0:
					if (side == 3) { return powered_from_input(world, x, y, z, 0); }
					if (side == 4) { return powered_from_input(world, x, y, z, 1); }
					break;

				case 1:
					if (side == 3) { return powered_from_input(world, x, y, z, 0); }
					if (side == 5) { return powered_from_input(world, x, y, z, 2); }
					break;

				case 2:
					if (side == 2) { return powered_from_input(world, x, y, z, 3); }
					if (side == 4) { return powered_from_input(world, x, y, z, 1); }
					break;

				case 3:
					if (side == 2) { return powered_from_input(world, x, y, z, 3); }
					if (side == 5) { return powered_from_input(world, x, y, z, 2); }
					break;

			}

			return false;
		}

		boolean on = isActive();
		if (!on) { return false; }

		if (type == PClo_GateType.SPECIAL) { return false; }

		if (type == PClo_GateType.DAY || type == PClo_GateType.RAIN) { return true; }

		// oriented gates
		if ((rotation == 0 && side == 3) || (rotation == 2 && side == 3 && hasTwoOutputs(getType(iblockaccess, x, y, z)))) { return true; }
		if ((rotation == 1 && side == 4) || (rotation == 3 && side == 4 && hasTwoOutputs(getType(iblockaccess, x, y, z)))) { return true; }
		if ((rotation == 2 && side == 2) || (rotation == 0 && side == 2 && hasTwoOutputs(getType(iblockaccess, x, y, z)))) { return true; }
		return ((rotation == 3 && side == 5) || (rotation == 1 && side == 5 && hasTwoOutputs(getType(iblockaccess, x, y, z))));
	}


	/**
	 * Calculate result of a programmable gate.
	 * @param pgm program (expression)
	 * @param B back input
	 * @param L left input
	 * @param R right input
	 * @return output
	 */
	boolean calcProgrammableGate(PClo_TileEntityGate teg, boolean B, boolean L, boolean R) {
		return teg.evalProgram(L, B, R);
	}

	/**
	 * Calculate logic result of given inputs. Often only A or A,B are needed.
	 * 
	 * @param gateType the gate type (index in PClo_GType)
	 * @param back input A
	 * @param left input B
	 * @param right input C
	 * @param tileEntety gate tile entity
	 * @return the result
	 */
	private boolean getResult(int gateType, boolean back, boolean left, boolean right, PClo_TileEntityGate tileEntety) {
		// A = bottom
		// B = left
		// C = right
		if (gateType == PClo_GateType.NOT) { return !back; }
		if (gateType == PClo_GateType.AND) { return left & right; }
		if (gateType == PClo_GateType.NAND) { return !(left & right); }
		if (gateType == PClo_GateType.OR) { return left | right; }
		if (gateType == PClo_GateType.NOR) { return !(left | right); }
		if (gateType == PClo_GateType.XOR) { return left != right; }
		if (gateType == PClo_GateType.XNOR) { return left == right; }
		if (gateType == PClo_GateType.AND3) { return back & left & right; }
		if (gateType == PClo_GateType.NAND3) { return !(back & left & right); }
		if (gateType == PClo_GateType.OR3) { return back | left | right; }
		if (gateType == PClo_GateType.NOR3) { return !(back | left | right); }
		if (gateType == PClo_GateType.XOR3) { return (back != left) | (left != right) | (right != back); }
		if (gateType == PClo_GateType.XNOR3) { return (back == left) && (left == right) && (right == back); }
		if (gateType == PClo_GateType.PROGRAMMABLE) { return calcProgrammableGate(tileEntety, back, left, right); }
		return false;

	}

	/**
	 * Is the gate powered from given input?
	 * This method take scare of rotation for you. 0 is input, 3 is output, 1
	 * and 2 sides.
	 * 
	 * @param world the World
	 * @param x
	 * @param y
	 * @param z
	 * @param inp the input number
	 * @return is powered
	 */
	public static boolean powered_from_input(World world, int x, int y, int z, int inp) {
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

		if (rotation == N0) { return (world.isBlockIndirectlyProvidingPowerTo(x, y, z + 1, 3) || world.getBlockId(x, y, z + 1) == Block.redstoneWire.blockID
				&& world.getBlockMetadata(x, y, z + 1) > 0); }
		if (rotation == N1) { return (world.isBlockIndirectlyProvidingPowerTo(x - 1, y, z, 4) || world.getBlockId(x - 1, y, z) == Block.redstoneWire.blockID
				&& world.getBlockMetadata(x - 1, y, z) > 0); }
		if (rotation == N2) { return (world.isBlockIndirectlyProvidingPowerTo(x, y, z - 1, 2) || world.getBlockId(x, y, z - 1) == Block.redstoneWire.blockID
				&& world.getBlockMetadata(x, y, z - 1) > 0); }
		if (rotation == N3) { return (world.isBlockIndirectlyProvidingPowerTo(x + 1, y, z, 5) || world.getBlockId(x + 1, y, z) == Block.redstoneWire.blockID
				&& world.getBlockMetadata(x + 1, y, z) > 0); }
		return false;
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving) {

		if (getType(world, x, y, z) == PClo_GateType.CROSSING) {
			world.scheduleBlockUpdate(x, y, z, blockID, tickRate());
			return;
		}

		int l = ((MathHelper.floor_double(((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3) + 2) % 4;

		if (PC_Utils.isPlacingReversed()) {
			l = PC_Utils.reverseSide(l);
		}

		world.setBlockMetadataWithNotify(x, y, z, l);
		boolean flag = isOutputActive(world, x, y, z);
		if (flag) {
			world.scheduleBlockUpdate(x, y, z, blockID, tickRate());
		}
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		hugeUpdate(world, x, y, z, blockID);
		super.onBlockAdded(world, x, y, z);
	}

	/**
	 * Perform hide redstone update around this gate.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param id
	 */
	public static void hugeUpdate(World world, int x, int y, int z, int id) {
		world.notifyBlocksOfNeighborChange(x, y, z, id);
		world.notifyBlocksOfNeighborChange(x + 1, y, z, id);
		world.notifyBlocksOfNeighborChange(x - 1, y, z, id);
		world.notifyBlocksOfNeighborChange(x, y, z + 1, id);
		world.notifyBlocksOfNeighborChange(x, y, z - 1, id);
		world.notifyBlocksOfNeighborChange(x, y - 1, z, id);
		world.notifyBlocksOfNeighborChange(x, y + 1, z, id);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if (!active) { return; }

		if (random.nextInt(3) != 0) { return; }

		double d = (x + 0.5F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;
		double d1 = (y + 0.2F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;
		double d2 = (z + 0.5F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;

		world.spawnParticle("reddust", d, d1, d2, 0.0D, 0.0D, 0.0D);
	}

	/**
	 * Is this gate active (output on)?
	 * 
	 * @return is active
	 */
	public boolean isActive() {
		return active;
	}

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {

		ItemStack ihold = player.getCurrentEquippedItem();
		if (ihold != null) {
			if (ihold.getItem() instanceof ItemBlock && ihold.getItem().shiftedIndex != mod_PClogic.gateOn.blockID
					&& ihold.getItem().shiftedIndex != mod_PClogic.gateOff.blockID) {

				Block bhold = Block.blocksList[ihold.getItem().shiftedIndex];
				if (bhold instanceof PC_IBlockType) {

				return false;

				}

			}
		}

		if (getType(world, x, y, z) == PClo_GateType.CROSSING) {
			int side = ((MathHelper.floor_double(((player.rotationYaw * 4F) / 360F) + 0.5D) & 3) + 2) % 4;
			if (side == 0 || side == 2) {
				getTE(world, x, y, z).toggleCrossingZ();
			}
			if (side == 1 || side == 3) {
				getTE(world, x, y, z).toggleCrossingX();
			}
		}

		if (getType(world, x, y, z) == PClo_GateType.PROGRAMMABLE) {

			PC_Utils.openGres(player, new PClo_GuiCustomGate(getTE(world, x, y, z)));
			boolean outputActive = isOutputActive(world, x, y, z);
			boolean on = isActive();

			if (on && !outputActive) {
				// turn off
				changeGateState(false, world, x, y, z);
			} else if (!on && outputActive) {
				// turn on
				changeGateState(true, world, x, y, z);
			}
			return true;

		}

		if (getType(world, x, y, z) == PClo_GateType.FIFO_DELAYER) {

			PC_Utils.openGres(player, new PClo_GuiDelayer(getTE(world, x, y, z), PClo_GuiDelayer.FIFO));
			return true;

		} else if (getType(world, x, y, z) == PClo_GateType.HOLD_DELAYER) {

			PC_Utils.openGres(player, new PClo_GuiDelayer(getTE(world, x, y, z), PClo_GuiDelayer.HOLD));
			return true;
		}

		return false;
	}

	@Override
	public Set<String> getBlockFlags(World world, PC_CoordI pos) {

		Set<String> set = new HashSet<String>();

		set.add("NO_HARVEST");
		set.add("TRANSLUCENT");
		set.add("REDSTONE");
		set.add("LOGIC");
		set.add("GATE");

		return set;
	}

	@Override
	public Set<String> getItemFlags(int damage) {
		Set<String> set = new HashSet<String>();
		set.add("NO_BUILD");
		return set;
	}

}
