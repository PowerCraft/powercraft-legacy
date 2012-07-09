package net.minecraft.src;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weasel.Calc;
import weasel.IWeaselHardware;
import weasel.WeaselEngine;
import weasel.exception.SyntaxError;
import weasel.exception.WeaselRuntimeException;
import weasel.lang.Instruction;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselInteger;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselObject.WeaselObjectType;


/**
 * Logic gate tile entity
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_TileEntityGate extends PC_TileEntity implements IWeaselHardware {

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

	private long lightningCharge = 0;

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

		updateFlashCharge();

		// fix for double updates
		long t = System.currentTimeMillis();
		if (t - lastUpdateAbsoluteTime < 5) {
			return;
		}
		lastUpdateAbsoluteTime = t;

		// if regular ticks aren't needed, use only every sixth.
		if (gateType != PClo_GateType.FIFO_DELAYER && gateType != PClo_GateType.HOLD_DELAYER) {
			if (updateIgnoreCounter-- <= 0) {
				updateIgnoreCounter = (gateType == PClo_GateType.CPU) ? 1 : PRESCALLER;
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

			case PClo_GateType.CPU:
				if (weaselError == null) {
					if (!weasel.isProgramFinished) {
						weaselInport = PClo_BlockGate.getWeaselInputStates(worldObj, xCoord, yCoord, zCoord);

						try {
							initWeaselIfNull();
							weasel.run(400);
						} catch (WeaselRuntimeException wre) {
							setWeaselError(wre.getMessage());
						}
					}
				}
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

		if (gateType == PClo_GateType.CPU) {
			program = maintag.getString("program");
			if (program.equals("")) program = default_program;
			initWeaselIfNull();
			weasel.readFromNBT(maintag.getCompoundTag("Weasel"));

			weaselOutport[0] = maintag.getBoolean("wo0");
			weaselOutport[1] = maintag.getBoolean("wo1");
			weaselOutport[2] = maintag.getBoolean("wo2");
			weaselOutport[3] = maintag.getBoolean("wo3");
			weaselOutport[4] = maintag.getBoolean("wo4");
			weaselOutport[5] = maintag.getBoolean("wo5");

			weaselError = maintag.getString("weaselError");
			lightningCharge = maintag.getLong("LightningCharge");
			if (weaselError.equals("")) weaselError = null;

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

		if (gateType == PClo_GateType.CPU) {
			maintag.setString("program", program);
			maintag.setString("weaselError", (weaselError == null ? "" : weaselError));
			initWeaselIfNull();
			maintag.setCompoundTag("Weasel", weasel.writeToNBT(new NBTTagCompound()));
			maintag.setBoolean("wo0", weaselOutport[0]);
			maintag.setBoolean("wo1", weaselOutport[1]);
			maintag.setBoolean("wo2", weaselOutport[2]);
			maintag.setBoolean("wo3", weaselOutport[3]);
			maintag.setBoolean("wo4", weaselOutport[4]);
			maintag.setBoolean("wo5", weaselOutport[5]);

			maintag.setLong("LightningCharge", lightningCharge);
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
	 * @param meta metadata of the gate
	 * @return true if chest is empty.
	 */
	private boolean isChestEmpty(int meta) {
		if (meta == 0) {
			return isEmptyChestAt(worldObj, getCoord().offset(0, 0, 1));
		}
		if (meta == 1) {
			return isEmptyChestAt(worldObj, getCoord().offset(-1, 0, 0));
		}
		if (meta == 2) {
			return isEmptyChestAt(worldObj, getCoord().offset(0, 0, -1));
		}
		if (meta == 3) {
			return isEmptyChestAt(worldObj, getCoord().offset(1, 0, 0));
		}
		return true;
	}

	/**
	 * Check if the chest at given coords is empty
	 * 
	 * @param blockaccess block access
	 * @param pos chest pos
	 * @return is full
	 */
	private boolean isEmptyChestAt(IBlockAccess blockaccess, PC_CoordI pos) {

		IInventory invAt = PC_InvUtils.getCompositeInventoryAt(blockaccess, pos);
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




	/*
	 * 
	 * Weasel for the Programmable gates.
	 * 
	 */

	//@formatter:off
	
	private static final String default_program = 
			"# *** Weasel powered Microcontroller ***\n"+
			"# update() is called when neighbor block changes.\n" +
			"# Use variables F,L,R,B,U,D to access sides.\n" +
			"\n\n"+
			"function update(){\n"+
			"  \n"+
			"}\n";
	
	//@formatter:on

	/** Programmable gate's program */
	public String program = default_program;


	private WeaselEngine weasel = null;

	private String weaselError = null;

	/**
	 * Save the programmable gate's source code in this tile entity.
	 * 
	 * @param program the source code
	 */
	public void setProgram(String program) {
		this.program = program;
	}

	private void initWeaselIfNull() {
		if (weasel == null) {

			weasel = new WeaselEngine(this);
			try {

				if (program.equals("")) {
					program = default_program;
				}

				setAndStartNewProgram(program);

			} catch (SyntaxError e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Set program to the engine and start it.
	 * 
	 * @param program the source code
	 * @throws SyntaxError
	 */
	public void setAndStartNewProgram(String program) throws SyntaxError {
		setProgram(program);

		initWeaselIfNull();
		List<Instruction> list = WeaselEngine.compileProgram(program);
		weasel.insertNewProgram(list);

		weaselError = null;
		weaselOutport = new boolean[] { false, false, false, false, false, false };

		try {
			if (worldObj != null) weasel.run(500);
		} catch (WeaselRuntimeException e) {
			weaselError = e.getMessage();
		}

	}

	/**
	 * Check program for errors.
	 * 
	 * @param program the source code
	 * @throws SyntaxError
	 */
	public void checkProgramForErrors(String program) throws SyntaxError {
		List<Instruction> list = WeaselEngine.compileProgram(program);
//		System.out.println();
//		for (Instruction i : list) {
//			System.out.println(i);
//		}
	}

	boolean[] weaselOutport = { false, false, false, false, false, false };
	boolean[] weaselInport = { false, false, false, false, false, false };

	private static Random rand = new Random();

	private static final int FLASH_CHARGE_NEEDED = 2000;
	private static final int FLASH_MIN_HEIGHT = 79;

	private void updateFlashCharge() {
		
		//charging not started.
		if(lightningCharge == 0) return;

		if (lightningCharge <= FLASH_CHARGE_NEEDED * 5) {
			
			int increment = rand.nextInt(3);
			
			if(worldObj.isThundering()) {
				increment = 2+rand.nextInt(5);
			}else			
			if(worldObj.isRaining()) {
				increment = 1+rand.nextInt(3);
			}else				
			if(worldObj.isBlockHighHumidity(xCoord, yCoord, zCoord)) {
				increment = rand.nextInt(2);
			}
			
			//System.out.println("\nUpdating lighting charge by: "+lightningCharge);
			lightningCharge += increment;
			//System.out.println("Lighting charge: "+lightningCharge);
			
		}else {
			//System.out.println("Lightning fully charged to 5 bolts.");
		}

	}

	private boolean isFlashReadyToStrike() {
		return lightningCharge >= FLASH_CHARGE_NEEDED;
	}

	private void summonLightning(int count) {
		boolean ok = true;

		int steel = Block.blockSteel.blockID;

		//check integrity of the underlying iron pillar
		for (int i = -1; i >= -6; i--) {
			if (getCoord().offset(0, i, 0).getId(worldObj) != steel) ok = false;
		}
		
		ok &= worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord);
		ok &= yCoord >= FLASH_MIN_HEIGHT;

		// if okay and is high enough
		if (ok) {
			
			for (int i = 0; i < count; i++) {
				
				if (isFlashReadyToStrike()) {
					worldObj.addWeatherEffect(new EntityLightningBolt(worldObj, xCoord, yCoord + 0.2D, zCoord));
					lightningCharge -= FLASH_CHARGE_NEEDED;
				} else {
					for (int j = 0; j < 40; j++) {
						// blue sparks in the air
						ModLoader.getMinecraftInstance().effectRenderer.addEffect(new PC_EntityLaserParticleFX(worldObj, new PC_CoordD(getCoord()).offset(rand.nextDouble(), rand.nextDouble() * 4, rand.nextDouble()), new PC_Color(0.6, 0.6, 1),
								new PC_CoordI(), 0));
					}
					// explosion sound
					worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "random.explode", 3.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
				}
				
			}
			
		} else {
			// blue sparks
			for (int i = 0; i < 20; i++) {
				ModLoader.getMinecraftInstance().effectRenderer.addEffect(new PC_EntityLaserParticleFX(worldObj, new PC_CoordD(getCoord()).offset(rand.nextDouble(), rand.nextDouble() * 0.5, rand.nextDouble()), new PC_Color(0.6, 0.6, 1),
						new PC_CoordI(), 0));
			}
			worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "random.explode", 0.6F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);

		}
	}

	@Override
	public boolean doesProvideFunction(String functionName) {
		return getProvidedFunctionNames().contains(functionName);
	}

	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {


		float volume = 1.0F;
		if (args.length == 2) volume = ((Integer) args[1].get()) / 10F;
		if (volume > 5) volume = 5;
		if (volume < 0) volume = 0.001F;


		if (functionName.equals("flash.strike")) {

			int count = 1;
			if(args.length == 1) {
				count = (Integer) args[0].get();
			}

			summonLightning(count);
			
			return null;

		} else if (functionName.equals("flash.ready")) {

			return new WeaselBoolean(isFlashReadyToStrike());

		} else if (functionName.equals("flash.level")) {

			return new WeaselInteger((int)lightningCharge/20);

		}else if (functionName.equals("flash.charge") || functionName.equals("flash.begin") || functionName.equals("flash.startCharging")) {

			if(lightningCharge == 0) lightningCharge = 1;
			
			return null;

		} else if (functionName.equals("time")) {

			return new WeaselInteger(worldObj.worldInfo.getWorldTime());

		} else if (functionName.equals("sleep")) {

			updateIgnoreCounter = (Integer) args[0].get();
			if (updateIgnoreCounter < 0) updateIgnoreCounter = 0;
			engine.requestPause();
			return null;

		} else if (functionName.equals("day")) {

			return new WeaselBoolean(worldObj.isDaytime());

		} else if (functionName.equals("night")) {

			return new WeaselBoolean(!worldObj.isDaytime());

		} else if (functionName.equals("rain")) {

			return new WeaselBoolean(worldObj.isRaining());

		} else if (functionName.equals("oink")) {
			worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "mob.pig", volume, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
		} else if (functionName.equals("moo")) {
			worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "mob.cow", volume, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
		} else if (functionName.equals("baa")) {
			worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "mob.sheep", volume, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);

		} else if (functionName.equals("cluck")) {
			worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "mob.chicken", volume, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);

		} else if (functionName.equals("woof")) {
			worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "mob.wolf.bark", volume, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);

		} else if (functionName.equals("sound")) {
			worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, ((String) args[0].get()), volume, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
		} else if (functionName.equals("note")) {
			if (args.length == 3) {
				volume = ((Integer) args[2].get()) / 10F;
			} else {
				volume = 3F;
			}

			if (volume > 5) volume = 5;
			if (volume < 0) volume = 0.001F;

			playNote(((String) args[0].get()), ((Integer) args[1].get()), volume);
			return null;
		}

		worldObj.spawnParticle("note", xCoord + 0.5D, yCoord + 0.3D, zCoord + 0.5D, (functionName.length() * (3 + args.length)) / 24D, 0.0D, 0.0D);

		return null;
	}

	private void playNote(String type, int height, float volume) {
		float f = (float) Math.pow(2D, (height - 12) / 12D);
		String s = type;

		if (type.equalsIgnoreCase("stone") || type.equalsIgnoreCase("bass drum") || type.equalsIgnoreCase("bassdrum") || type.equalsIgnoreCase("bd") || type.equalsIgnoreCase("drum")) {
			s = "note.bd";
		} else if (type.equalsIgnoreCase("sand") || type.equalsIgnoreCase("snare drum") || type.equalsIgnoreCase("snaredrum") || type.equalsIgnoreCase("sd") || type.equalsIgnoreCase("snare")) {
			s = "note.snare";
		} else if (type.equalsIgnoreCase("glass") || type.equalsIgnoreCase("stick") || type.equalsIgnoreCase("sticks") || type.equalsIgnoreCase("cl") || type.equalsIgnoreCase("clicks") || type.equalsIgnoreCase("click")) {
			s = "note.hat";
		} else if (type.equalsIgnoreCase("wood") || type.equalsIgnoreCase("bass guitar") || type.equalsIgnoreCase("bassguitar") || type.equalsIgnoreCase("bg") || type.equalsIgnoreCase("guitar")) {
			s = "note.bassattack";
		} else if (type.equalsIgnoreCase("firt") || type.equalsIgnoreCase("harp") || type.equalsIgnoreCase("piano") || type.equalsIgnoreCase("pi")) {
			s = "note.harp";
		}

		worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, s, volume, f);
		worldObj.spawnParticle("note", xCoord + 0.5D, yCoord + 0.3D, zCoord + 0.5D, height / 24D, 0.0D, 0.0D);
	}

	@Override
	public WeaselObject getVariable(String name) {

		//System.out.print("Get variable "+name+" = ");
		WeaselObject obj = null;

		if (name.equals("B") || name.equals("back")) obj = new WeaselBoolean(weaselInport[0]);
		if (name.equals("L") || name.equals("left")) obj = new WeaselBoolean(weaselInport[1]);
		if (name.equals("R") || name.equals("right")) obj = new WeaselBoolean(weaselInport[2]);
		if (name.equals("F") || name.equals("front")) obj = new WeaselBoolean(weaselInport[3]);
		if (name.equals("U") || name.equals("up") || name.equals("top")) obj = new WeaselBoolean(weaselInport[4]);
		if (name.equals("D") || name.equals("down") || name.equals("bottom")) obj = new WeaselBoolean(weaselInport[5]);

		if (name.equals("flash.charge") || name.equals("flash.level")) obj = new WeaselInteger((int) lightningCharge/20);
		if (name.equals("flash.ready")) obj = new WeaselBoolean(isFlashReadyToStrike());

		//System.out.println(" ← get "+name+" = "+obj);

		return obj;
	}

	@Override
	public void setVariable(String name, Object object) {

		//System.out.println("Set variable "+name+" = "+object);

		boolean change = false;
		boolean setval = Calc.toBoolean(object);

		if (name.equals("B") || name.equals("back")) {
			change = (weaselOutport[0] != setval);
			weaselOutport[0] = setval;
		} else if (name.equals("L") || name.equals("left")) {
			change = (weaselOutport[1] != setval);
			weaselOutport[1] = setval;
		} else if (name.equals("R") || name.equals("right")) {
			change = (weaselOutport[2] != setval);
			weaselOutport[2] = setval;
		} else if (name.equals("F") || name.equals("front")) {
			change = (weaselOutport[3] != setval);
			weaselOutport[3] = setval;
		} else if (name.equals("U") || name.equals("up") || name.equals("top")) {
			change = (weaselOutport[4] != setval);
			weaselOutport[4] = setval;
		} else if (name.equals("D") || name.equals("down") || name.equals("bottom")) {
			change = (weaselOutport[5] != setval);
			weaselOutport[5] = setval;
		}


//		System.out.println(" set → "+name+" = "+object);

		if (change) {
//			System.out.println("Sending notification.");
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, mod_PClogic.gateOff.blockID);

		}

	}

	@Override
	public List<String> getProvidedFunctionNames() {
		List<String> list = new ArrayList<String>();
		list.add("oink");
		list.add("moo");
		list.add("baa");
		list.add("cluck");
		list.add("woof");
		list.add("sound");
		list.add("note");

		list.add("flash.startCharging");
		list.add("flash.begin");
		list.add("flash.charge");
		
		list.add("flash.ready");
		
		list.add("flash.level");

		list.add("flash.strike");

		list.add("day");
		list.add("night");
		list.add("time");
		list.add("rain");
		list.add("sleep");
		return list;
	}

	@Override
	public List<String> getProvidedVariableNames() {

		List<String> list = new ArrayList<String>();
		list.add("B");
		list.add("L");
		list.add("R");
		list.add("F");
		list.add("U");
		list.add("D");
		list.add("back");
		list.add("left");
		list.add("right");
		list.add("front");
		list.add("up");
		list.add("down");
		list.add("top");
		list.add("bottom");

		list.add("flash.charge");
		list.add("flash.level");
		list.add("flash.ready");

		return list;

	}

	/**
	 * Get outport
	 * 
	 * @return array of booleans: Back, left, right, front
	 */
	public boolean[] getWeaselOutputStates() {
		return new boolean[] { weaselOutport[1], weaselOutport[0], weaselOutport[2], weaselOutport[3], weaselOutport[4], weaselOutport[5] };

	}


	/**
	 * Call weasel's function update
	 */
	public void weaselOnPinChanged() {

		if (weaselError != null) return;

//		System.out.println("Getting INPORT for weasel at "+yCoord);
		weaselInport = PClo_BlockGate.getWeaselInputStates(worldObj, xCoord, yCoord, zCoord);
//		System.out.println("Weasel at " + yCoord + " - pin changed update");
//		System.out.println("BOTTOM = " + weaselInport[5]);
//		System.out.println("TOP = " + weaselInport[4]);

		try {
			initWeaselIfNull();
			weasel.callFunctionExternal("update", new Object[] {});
			weasel.run(400);

		} catch (WeaselRuntimeException wre) {
			setWeaselError(wre.getMessage());
		}

	}

	public void setWeaselError(String message) {
		this.weaselError = message;
	}

	public String getWeaselError() {
		return this.weaselError;
	}






}
