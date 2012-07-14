package net.minecraft.src;



import java.util.List;


/**
 * Logic gate tile entity
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_TileEntityGate extends PC_TileEntity {

	/**
	 * TEG
	 */
	public PClo_TileEntityGate() {}

	/**
	 * For forge: does the entity need update ticks
	 * 
	 * @return true if it needs updates
	 */
	@Override
	public boolean canUpdate() {
		return true;
	}

	/**
	 * The gate type index, from PClo_GType
	 */
	public int gateType = -1;

	/**
	 * Helper boolean state for some flip flop gates
	 */
	public boolean prevClockState = false;

	private int updateIgnoreCounter = 10;
	private long lastUpdateAbsoluteTime = 0;
	/**
	 * Set to true if the entity is removed. Saves resources.
	 */
	public boolean zombie = false; // set true if this tile entity was already destroyed

	/** number of updates to skip before check. */
	public int PRESCALLER = 6;

	@Override
	public void updateEntity() {
		if (zombie || worldObj == null) {
			return;
		}



		// fix for double updates
		long t = System.currentTimeMillis();
		if (t - lastUpdateAbsoluteTime < 5) {
			return;
		}
		lastUpdateAbsoluteTime = t;

		// if regular ticks aren't needed, use only every sixth.
		if (gateType != PClo_GateType.FIFO_DELAYER && gateType != PClo_GateType.HOLD_DELAYER) {
			if (updateIgnoreCounter-- <= 0) {
				updateIgnoreCounter = PRESCALLER;
			} else {
				return;
			}
		}

		// refresh cached information
		updateContainingBlockInfo();

		Block block = getBlockType();
		if (block == null || (block.blockID != mod_PClogic.gateOn.blockID && block.blockID != mod_PClogic.gateOff.blockID)) {
			return;
		}

		int blockID = block.blockID;

		// is the gate active
		boolean active = ((PClo_BlockGate) Block.blocksList[blockID]).active;

		switch (gateType) {
			case PClo_GateType.FIFO_DELAYER:

				// "reset" input powered
				if (PClo_BlockGate.powered_from_input(worldObj, xCoord, yCoord, zCoord, 1)) {
					if (!dResetDone) {
						for (int i = 0; i < dBuffer.length; i++) {
							dBuffer[i] = false;
						}
						dPointer = 0;

						if (dOutputState) {
							dOutputState = false;
							updateBlock();
						}

						dResetDone = true;
					}

					// "hold" input not powered
				} else {

					dResetDone = false;
					if (!PClo_BlockGate.powered_from_input(worldObj, xCoord, yCoord, zCoord, 2)) {

						bufferPush(PClo_BlockGate.powered_from_input(worldObj, xCoord, yCoord, zCoord, 0));

						if (dOutputState != active) {
							updateBlock();
						}

					}

				}

				break;

			case PClo_GateType.HOLD_DELAYER:

				if (PClo_BlockGate.powered_from_input(worldObj, xCoord, yCoord, zCoord, 0)) {
					repeaterStartHolding();
				}

				if (rRemainingTicks > 0) {
					rRemainingTicks--;
				}

				if (rRemainingTicks == 0 && active) {
					updateBlock();
				} else if (rRemainingTicks > 0 && !active) {
					updateBlock();
				}

				break;

			case PClo_GateType.DAY:

				if (worldObj.isDaytime() != active) {
					updateBlock();
				}

				break;

			case PClo_GateType.NIGHT:

				if (!worldObj.isDaytime() != active) {
					updateBlock();
				}

				break;

			case PClo_GateType.RAIN:

				if (worldObj.isRaining() != active) {
					updateBlock();
				}

				break;

			case PClo_GateType.CHEST_EMPTY:

				if (isChestEmpty() != active) {
					updateBlock();
				}

				break;

			case PClo_GateType.CHEST_FULL:

				if (isChestFull() != active) {
					updateBlock();
				}

				break;

			case PClo_GateType.SPECIAL:

				stopSpawning_stopPulsar(worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord), active);
				stopSpawning_stopPulsar(worldObj.getBlockTileEntity(xCoord - 1, yCoord, zCoord), active);
				stopSpawning_stopPulsar(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord + 1), active);
				stopSpawning_stopPulsar(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - 1), active);
				stopSpawning_stopPulsar(worldObj.getBlockTileEntity(xCoord, yCoord + 1, zCoord), active);
				stopSpawning_stopPulsar(worldObj.getBlockTileEntity(xCoord, yCoord - 1, zCoord), active);
				break;

			case PClo_GateType.OBSOLETE_UNUSED:

		}

	}


	/**
	 * Notify world block change
	 */
	private void updateBlock() {
		worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, getBlockType().blockID, 1);
	}



	/**
	 * Reset nearby spawner timer and pause pulsars.
	 * 
	 * @param te
	 * @param active
	 */
	private void stopSpawning_stopPulsar(TileEntity te, boolean active) {
		if (te != null) {
			if (te instanceof TileEntityMobSpawner) {
				((TileEntityMobSpawner) te).delay = 500;
			} else if (te instanceof PClo_TileEntityPulsar && active) {

				((PClo_TileEntityPulsar) te).paused = true;
				((PClo_TileEntityPulsar) te).active = false;
				((PClo_TileEntityPulsar) te).delayTimer = 0;
				((PClo_TileEntityPulsar) te).updateBlock();

			}
		}
	}


	@Override
	public void readFromNBT(NBTTagCompound maintag) {
		super.readFromNBT(maintag);
		gateType = maintag.getInteger("type");
		prevClockState = maintag.getBoolean("clock");

		if (gateType == PClo_GateType.FIFO_DELAYER) {

			NBTTagList taglist = maintag.getTagList("DelayBuffer");

			int length = maintag.getInteger("DelayLength");

			dBuffer = new boolean[length];

			dOutputState = maintag.getBoolean("DelayState");

			for (int i = 0; i < length; i++) {
				NBTTagCompound booltag = (NBTTagCompound) taglist.tagAt(i);
				dBuffer[booltag.getInteger("Index")] = booltag.getBoolean("state");
			}

		}

		if (gateType == PClo_GateType.OBSOLETE_UNUSED) {

		}

		if (gateType == PClo_GateType.HOLD_DELAYER) {
			rHoldTime = maintag.getInteger("RepeaterHoldTime");
			rRemainingTicks = maintag.getInteger("RepeaterTicksRem");
		}

		inputVariant = maintag.getInteger("inputVariant");

		if (gateType == PClo_GateType.CROSSING) {
			crossing_X = maintag.getInteger("CrossingX");
			crossing_Z = maintag.getInteger("CrossingZ");
		}

		if (gateType == PClo_GateType.CHEST_FULL) {
			fullChestNeedsAllSlotsFull = maintag.getBoolean("FullChestStrict");
		}

	}

	@Override
	public void writeToNBT(NBTTagCompound maintag) {
		super.writeToNBT(maintag);
		maintag.setInteger("type", gateType);
		maintag.setBoolean("clock", prevClockState);

		if (gateType == PClo_GateType.FIFO_DELAYER) {

			NBTTagList listtag = new NBTTagList();
			for (int i = 0; i < dBuffer.length; i++) {
				NBTTagCompound booltag = new NBTTagCompound();
				booltag.setInteger("Index", i);
				booltag.setBoolean("state", dBuffer[i]);
				listtag.appendTag(booltag);
			}

			maintag.setTag("DelayBuffer", listtag);
			maintag.setInteger("DelayLength", dBuffer.length);
			maintag.setBoolean("DelayState", dOutputState);

		}

		maintag.setInteger("inputVariant", inputVariant);

		if (gateType == PClo_GateType.OBSOLETE_UNUSED) {
			
		}

		if (gateType == PClo_GateType.HOLD_DELAYER) {
			maintag.setInteger("RepeaterHoldTime", rHoldTime);
			maintag.setInteger("RepeaterTicksRem", rRemainingTicks);
		}

		if (gateType == PClo_GateType.CROSSING) {
			maintag.setInteger("CrossingX", crossing_X);
			maintag.setInteger("CrossingZ", crossing_Z);
		}

		if (gateType == PClo_GateType.CHEST_FULL) {
			maintag.setBoolean("FullChestStrict", fullChestNeedsAllSlotsFull);
		}
	}


	/** Flag for full chest detector how to handle the chests. */
	public boolean fullChestNeedsAllSlotsFull;

	/*
	 * 
	 * CHEST STATE SENSORS
	 * 
	 */

	/**
	 * FULL CHEST DETECTOR: Check if nearby chest is full
	 * 
	 * @return true if chest is full.
	 */
	public boolean isChestFull() {
		int i1 = worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 3;

		return isChestFull(i1, fullChestNeedsAllSlotsFull);
	}

	/**
	 * FULL CHEST DETECTOR: Check if nearby chest is full
	 * 
	 * @param side meta of the gate, rotation
	 * @param allSlotsFull require that all slots are full. otherwise also
	 *            partially used slots are treated as used.
	 * @return true if chest is full.
	 */
	private boolean isChestFull(int side, boolean allSlotsFull) {

		if (side == 0) {
			return isFullChestAt(worldObj, getCoord().offset(0, 0, 1), allSlotsFull);
		}
		if (side == 1) {
			return isFullChestAt(worldObj, getCoord().offset(-1, 0, 0), allSlotsFull);
		}
		if (side == 2) {
			return isFullChestAt(worldObj, getCoord().offset(0, 0, -1), allSlotsFull);
		}
		if (side == 3) {
			return isFullChestAt(worldObj, getCoord().offset(1, 0, 0), allSlotsFull);
		}
		return false;
	}

	/**
	 * EMPTY CHEST DETECTOR: Check if nearby chest is empty
	 * 
	 * @return true if chest is empty.
	 */
	public boolean isChestEmpty() {
		int i1 = worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 3;

		return isChestEmpty(i1);
	}

	/**
	 * Check if nearby chest is empty
	 * 
	 * @param side metadata of the gate
	 * @return true if chest is empty.
	 */
	private boolean isChestEmpty(int side) {
		if (side == 0) {
			return isEmptyChestAt(getCoord().offset(0, 0, 1));
		}
		if (side == 1) {
			return isEmptyChestAt(getCoord().offset(-1, 0, 0));
		}
		if (side == 2) {
			return isEmptyChestAt(getCoord().offset(0, 0, -1));
		}
		if (side == 3) {
			return isEmptyChestAt(getCoord().offset(1, 0, 0));
		}
		return true;
	}

	/**
	 * Check if the chest at given coords is empty
	 * 
	 * @param pos chest pos
	 * @return is full
	 */
	private boolean isEmptyChestAt(PC_CoordI pos) {

		IInventory invAt = PC_InvUtils.getCompositeInventoryAt(worldObj, pos);
		if (invAt != null) return PC_InvUtils.isInventoryEmpty(invAt);

		List<IInventory> list = worldObj.getEntitiesWithinAABB(IInventory.class, AxisAlignedBB.getBoundingBox(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 1, pos.z + 1).expand(0.6D, 0.6D, 0.6D));

		if (list.size() >= 1) {
			return PC_InvUtils.isInventoryEmpty(list.get(0));
		}

		return false;

	}

	/**
	 * Check if the chest at given coords is full
	 * 
	 * @param blockaccess block access
	 * @param pos chest pos
	 * @param allSlotsFull strict check for full slots
	 * @return is full
	 */
	private boolean isFullChestAt(IBlockAccess blockaccess, PC_CoordI pos, boolean allSlotsFull) {

		IInventory invAt = PC_InvUtils.getCompositeInventoryAt(blockaccess, pos);
		if (invAt != null) {
			if (allSlotsFull) {
				return PC_InvUtils.isInventoryFull(invAt);
			} else {
				return PC_InvUtils.hasInventoryNoFreeSlots(invAt);
			}
		}

		List<IInventory> list = worldObj.getEntitiesWithinAABB(IInventory.class, AxisAlignedBB.getBoundingBox(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 1, pos.z + 1).expand(0.6D, 0.6D, 0.6D));

		if (list.size() >= 1) {
			if (allSlotsFull) {
				return PC_InvUtils.isInventoryFull(list.get(0));
			} else {
				return PC_InvUtils.hasInventoryNoFreeSlots(list.get(0));
			}
		}

		return false;
	}




	/*
	 * 
	 * FIFO REPEATER
	 * 
	 */

	/**
	 * FIFO REPEATER: State of the FIFO output
	 */
	private boolean dOutputState = false;

	/**
	 * FIFO REPEATER: Data buffer for FIFO REPEATER
	 */
	private boolean[] dBuffer = new boolean[20];

	/**
	 * FIFO REPEATER: Pointer in the buffer
	 */
	private int dPointer = 0;

	/**
	 * FIFO REPEATER: Status flag whether reset was already done. Used to reduce
	 * lag if "reset" is enabled.
	 */
	private boolean dResetDone = false;

	/**
	 * FIFO REPEATER: Clear and resize the fifo queue.
	 * 
	 * @param length delay in ticks
	 */
	public void bufferResize(int length) {

		dBuffer = new boolean[length];
		System.gc();
		dPointer = 0;
		dOutputState = false;

	}

	/**
	 * FIFO REPEATER: Push given redstone state into buffer
	 * 
	 * @param state redstone on/off
	 */
	private void bufferPush(boolean state) {
		dOutputState = dBuffer[dPointer];
		dBuffer[dPointer] = state;
		dPointer++;
		if (dPointer >= dBuffer.length) {
			dPointer = 0;
		}
	}

	/**
	 * FIFO REPEATER: get output state (gate output)
	 * 
	 * @return true if powering
	 */
	public boolean getBufferOutput() {
		return dOutputState;
	}

	/**
	 * FIFO REPEATER: Get delay
	 * 
	 * @return delay in ticks
	 */
	public int getDelayBufferLength() {
		return dBuffer.length;
	}





	/*
	 * 
	 * HOLD REPEATER
	 * 
	 */

	/**
	 * HOLD REPEATER: Ticks till output will turn off
	 */
	private int rRemainingTicks;
	/**
	 * HOLD REPEATER: Delay length
	 */
	private int rHoldTime = 20;

	/**
	 * HOLD REPEATER: Set the delay (hold) time
	 * 
	 * @param ticks time in ticks
	 */
	public void setRepeaterHoldTime(int ticks) {
		rHoldTime = ticks;
		rRemainingTicks = Math.min(rHoldTime, rRemainingTicks);
	}

	/**
	 * HOLD REPEATER: Set output to TRUE and set countdown timer = hold time.
	 */
	public void repeaterStartHolding() {
		rRemainingTicks = rHoldTime;
	}

	/**
	 * HOLD REPEATER: Is output active?
	 * 
	 * @return true if output active.
	 */
	public boolean isRepeaterOutputActive() {
		return rRemainingTicks > 0;
	}

	/**
	 * HOLD REPEATER: Get the delay time
	 * 
	 * @return time in ticks
	 */
	public int repeaterGetHoldTime() {
		return rHoldTime;
	}



	/*
	 * 
	 * CROSSING GATE
	 * 
	 */

	private static final int PLUS = 1, MINUS = -1;
	private int crossing_X = PLUS, crossing_Z = PLUS;

	/**
	 * State of inputs last time the CROSSING GATE was updated.
	 */
	public boolean[] crossingGateInputStates = { false, false, false, false };


	/**
	 * CROSSING GATE: Get current crossing variant, for renderer.
	 * 
	 * @return the variant, as follows: 0=X+Z+, 1=X-Z+, 2=X+Z-, 3=X-Z-
	 */
	public int getCrossingVariant() {
		if (crossing_X == PLUS && crossing_Z == PLUS) {
			return 0;
		}
		if (crossing_X == MINUS && crossing_Z == PLUS) {
			return 1;
		}
		if (crossing_X == PLUS && crossing_Z == MINUS) {
			return 2;
		}
		if (crossing_X == MINUS && crossing_Z == MINUS) {
			return 3;
		}
		return -1;
	}

	/**
	 * CROSSING GATE: Change X-arrow's direction.
	 */
	public void toggleCrossingX() {
		crossing_X *= -1;
		PClo_BlockGate.hugeUpdate(worldObj, xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
		worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
		worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
	}

	/**
	 * CROSSING GATE: Change Z-arrow's direction.
	 */
	public void toggleCrossingZ() {
		crossing_Z *= -1;
		PClo_BlockGate.hugeUpdate(worldObj, xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
		worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
		worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
	}



	/*
	 * Input variant changing
	 * Some gates may have multiple input layouts changed by right-clicking.
	 * Number of available variants is set in GateType.
	 * 
	 */

	private int inputVariant;

	/**
	 * Get index of currently set input variant
	 * 
	 * @return input variant index
	 */
	public int getInputsVariant() {
		return inputVariant;
	}

	/**
	 * Change input variant.
	 */
	public void toggleInputVariant() {
		inputVariant++;
		if (inputVariant >= PClo_GateType.getMaxCornerSides(gateType)) {
			inputVariant = 0;
		}
	}
}
