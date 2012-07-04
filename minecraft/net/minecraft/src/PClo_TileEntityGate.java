package net.minecraft.src;


import net.minecraft.src.weasel.Calculator;

import org.nfunk.jep.JEP;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;


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
	/** JEP expression evaluator */
	public JEP evaluator;
	private Node jepNode = null;

	/**
	 * Check if program is valid and has no errors
	 * 
	 * @param pgm
	 * @return is valid
	 */
	public String checkProgram(String pgm) {
		if (evaluator == null) evaluator = JEP.createWeaselParser(false);

		pgm = pgm.trim();

		try {

			JEP jep = JEP.createWeaselParser(false);

			jep.addVariable("L", 0);
			jep.addVariable("R", 0);
			jep.addVariable("B", 0);

			Node node = jep.parse(pgm);

			if (jep.hasError()) return "Unknown syntax error!";

			evaluator.evaluate(node);

		} catch (Throwable t) {
			if (t instanceof ParseException) {
				if (((ParseException) t).getErrorInfo() == null) return "Unspecified parse error.\n" + t.getStackTrace();
				return ((ParseException) t).getErrorInfo().trim();
			} else {

				if (t.getMessage() == null) {
					t.printStackTrace();
					return "Unspecified parse error.\n" + t.toString();
				}

				return t.getMessage().trim();
			}
		}
		return null;
	}

	/**
	 * Set and init a program
	 * 
	 * @param program
	 */
	public void setProgram(String program) {
		if (evaluator == null) evaluator = JEP.createWeaselParser(false);

		if (checkProgram(program) == null) {
			this.program = program;
			try {
				if (evaluator == null) {
					evaluator = JEP.createWeaselParser(false);
				} else {
					evaluator.getSymbolTable().clearNonConstants();
				}
				evaluator.addVariable("L", 0);
				evaluator.addVariable("R", 0);
				evaluator.addVariable("B", 0);
				jepNode = evaluator.parse(this.program);
			} catch (ParseException e) {
				PC_Logger.warning("Error while setting program to programmable gate: " + e.getErrorInfo());
			}
		}
	}

	/**
	 * Calculate result of current program
	 * 
	 * @param L left input
	 * @param B back input
	 * @param R right input
	 * @return output
	 */
	public boolean evalProgram(boolean L, boolean B, boolean R) {
		if (evaluator == null) evaluator = JEP.createWeaselParser(false);

		if (jepNode == null) {
			return false;
		}
		evaluator.setVarValue("L", L ? 1 : 0);
		evaluator.setVarValue("B", B ? 1 : 0);
		evaluator.setVarValue("R", R ? 1 : 0);

		try {
			return Calculator.toBoolean(evaluator.evaluate(jepNode));
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * Helper boolean state for some flip flop gates
	 */
	public boolean prevClockState = false;

	private int updateIgnoreCounter = 10;
	private long lastUpdateTime = 0;

	/**
	 * Set to true if the entity is removed. Saves resources.
	 */
	public boolean zombie = false; // set true if this tile entity was already destroyed

	/** Programmable gate's program */
	public String program = "";

	@Override
	public void updateEntity() {
		if (zombie) {
			return;
		}

		// fix for double updates
		long t = System.currentTimeMillis();
		if (t - lastUpdateTime < 5) {
			return;
		}
		lastUpdateTime = t;

		// if regular ticks aren't needed, use only every sixth.
		if (gateType != PClo_GateType.FIFO_DELAYER && gateType != PClo_GateType.HOLD_DELAYER) {
			if (updateIgnoreCounter-- <= 0) {
				updateIgnoreCounter = 6;
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

		if (gateType == PClo_GateType.PROGRAMMABLE) {
			program = maintag.getString("programm");

			setProgram(program);
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

		if (gateType == PClo_GateType.PROGRAMMABLE) {
			maintag.setString("programm", program);
		}

		if (gateType == PClo_GateType.HOLD_DELAYER) {
			maintag.setInteger("RepeaterHoldTime", rHoldTime);
			maintag.setInteger("RepeaterTicksRem", rRemainingTicks);
		}

		if (gateType == PClo_GateType.CROSSING) {
			maintag.setInteger("CrossingX", crossing_X);
			maintag.setInteger("CrossingZ", crossing_Z);
		}
	}




	/**
	 * FULL CHEST DETECTOR: Check if nearby chest is full
	 * 
	 * @return true if chest is full.
	 */
	public boolean isChestFull() {
		int i1 = worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 3;

		int N0 = 0;
		int N1 = 1;
		int N2 = 2;
		int N3 = 3;

		if (i1 == N0) {
			return isFullChestAt(worldObj, getCoord().offset(0, 0, 1));
		}
		if (i1 == N1) {
			return isFullChestAt(worldObj, getCoord().offset(-1, 0, 0));
		}
		if (i1 == N2) {
			return isFullChestAt(worldObj, getCoord().offset(0, 0, -1));
		}
		if (i1 == N3) {
			return isFullChestAt(worldObj, getCoord().offset(1, 0, 0));
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

		int N0 = 0;
		int N1 = 1;
		int N2 = 2;
		int N3 = 3;

		if (i1 == N0) {
			return isEmptyChestAt(worldObj, getCoord().offset(0, 0, 1));
		}
		if (i1 == N1) {
			return isEmptyChestAt(worldObj, getCoord().offset(-1, 0, 0));
		}
		if (i1 == N2) {
			return isEmptyChestAt(worldObj, getCoord().offset(0, 0, -1));
		}
		if (i1 == N3) {
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
	private static boolean isEmptyChestAt(IBlockAccess blockaccess, PC_CoordI pos) {
		return PC_InvUtils.isInventoryEmpty(PC_InvUtils.getCompositeInventoryAt(blockaccess, pos));
	}

	/**
	 * Check if the chest at given coords is full
	 * 
	 * @param blockaccess block access
	 * @param pos chest pos
	 * @return is full
	 */
	private static boolean isFullChestAt(IBlockAccess blockaccess, PC_CoordI pos) {
		return PC_InvUtils.isInventoryFull(PC_InvUtils.getCompositeInventoryAt(blockaccess, pos));
	}


	// FIFO REPEATER

	/**
	 * FIFO DELAYER: State of the FIFO output
	 */
	private boolean dOutputState = false;

	/**
	 * FIFO DELAYER: Data buffer for FIFO delayer
	 */
	private boolean[] dBuffer = new boolean[20];

	/**
	 * FIFO DELAYER: Pointer in the buffer
	 */
	private int dPointer = 0;

	/**
	 * FIFO DELAYER: Status flag whether reset was already done. Used to reduce
	 * lag if "reset" is enabled.
	 */
	private boolean dResetDone = false;

	/**
	 * FIFO DELAYER: Clear and resize the fifo queue.
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
	 * FIFO DELAYER: Push given redstone state into buffer
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
	 * FIFO DELAYER: get output state (gate output)
	 * 
	 * @return true if powering
	 */
	public boolean getBufferOutput() {
		return dOutputState;
	}

	/**
	 * FIFO DELAYER: Get delay
	 * 
	 * @return delay in ticks
	 */
	public int getDelayBufferLength() {
		return dBuffer.length;
	}

	// HOLD REPEATER

	/**
	 * HOLD DELAYER: Ticks till output will turn off
	 */
	private int rRemainingTicks;
	/**
	 * HOLD DELAYER: Delay length
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

	// CROSSING GATE

	private static final int PLUS = 1, MINUS = -1;
	private int crossing_X = PLUS, crossing_Z = PLUS;

	/**
	 * State of inputs last time the CROSSING GATE was updated.
	 */
	public boolean[] powered = { false, false, false, false };
	private int inputVariant;

	/**
	 * CROSSING GATE: Get current crossing variant.
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

	/**
	 * Get corner side for corner repeater
	 * 
	 * @return true = left, false = right
	 */
	public int getInputsVariant() {
		return inputVariant;
	}

	/**
	 * change corner side.
	 */
	public void toggleInputVariant() {
		inputVariant++;
		if (inputVariant >= PClo_GateType.getMaxCornerSides(gateType)) {
			inputVariant = 0;
		}
	}
}
