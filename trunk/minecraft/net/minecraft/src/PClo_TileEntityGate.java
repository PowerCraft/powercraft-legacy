package net.minecraft.src;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.src.PClo_WirelessBus.IRadioDevice;

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
public class PClo_TileEntityGate extends PC_TileEntity implements IWeaselHardware, IRadioDevice {

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
	
	private boolean connectedToRadioBus = false;

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
		
		if(!connectedToRadioBus) {
			mod_PClogic.DATA_BUS.connectToRedstoneBus(this);
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
				updateIgnoreCounter = (gateType == PClo_GateType.PROG) ? 1 : PRESCALLER;
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

			case PClo_GateType.PROG:
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

		if (gateType == PClo_GateType.PROG) {
			program = maintag.getString("program");
			if (program.equals("")) program = default_program;
			initWeaselIfNull();
			weasel.readFromNBT(maintag.getCompoundTag("Weasel"));
			
			for(int i=0; i<weaselOutport.length; i++)
				weaselOutport[i] = maintag.getBoolean("wo"+i);
			

			weaselError = maintag.getString("weaselError");
			if (weaselError.equals("")) weaselError = null;
			
			NBTTagList list = maintag.getTagList("wradio");
			
			for(int i=0; i< list.tagCount(); i++) {
				NBTTagCompound ct = (NBTTagCompound) list.tagAt(i);
				weaselRadioSignals.put(ct.getString("C"), ct.getBoolean("S"));
			}

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

		if (gateType == PClo_GateType.PROG) {
			maintag.setString("program", program);
			maintag.setString("weaselError", (weaselError == null ? "" : weaselError));
			initWeaselIfNull();
			maintag.setCompoundTag("Weasel", weasel.writeToNBT(new NBTTagCompound()));
			
			
			for(int i=0; i<weaselOutport.length; i++)
				maintag.setBoolean("wo"+i, weaselOutport[i]);
			
			
			NBTTagList list = new NBTTagList();
			for(Entry<String,Boolean> entry : weaselRadioSignals.entrySet()) {
				NBTTagCompound ct = new NBTTagCompound();
				ct.setString("C", entry.getKey());
				ct.setBoolean("S", entry.getValue());
				list.appendTag(ct);
			}
			
			maintag.setTag("wradio", list);
			
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
		weaselRadioSignals.clear();

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
		//List<Instruction> list = 
		WeaselEngine.compileProgram(program);
//		System.out.println();
//		for (Instruction i : list) {
//			System.out.println(i);
//		}
	}

	private boolean[] weaselOutport = { false, false, false, false, false, false };
	private boolean[] weaselInport = { false, false, false, false, false, false };
	
	private Map<String,Boolean> weaselRadioSignals = new HashMap<String, Boolean>(0);

	private static Random rand = new Random();


	@Override
	public boolean doesProvideFunction(String functionName) {
		return getProvidedFunctionNames().contains(functionName);
	}
	
	private class BadFunc extends Exception{}

	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
		try{
			return fnSound(engine, functionName, args);
		}catch(BadFunc e) {}
		
		try{
			return fnAmbient(engine, functionName, args);
		}catch(BadFunc e) {}
		
		try{
			return fnBus(engine, functionName, args);
		}catch(BadFunc e) {}
		
		throw new WeaselRuntimeException("Invalid call of function "+functionName);
	}
	
	/**
	 * try to execute a BUS function
	 * @param engine the engine
	 * @param functionName function name
	 * @param args arguments given
	 * @return return value
	 * @throws BadFunc not supported by this method
	 */
	private WeaselObject fnBus(WeaselEngine engine, String functionName, WeaselObject[] args) throws BadFunc {
		
		if (functionName.equals("bus.set") || functionName.equals("bs")) {

			mod_PClogic.DATA_BUS.setWeaselVariable((String) args[0].get(), args[1]);
			return null;

		} else if (functionName.equals("bus.get") || functionName.equals("bg")) {

			return mod_PClogic.DATA_BUS.getWeaselVariable((String) args[0].get());

		} else if (functionName.equals("radio")) {

			if(args.length == 1) {
				//receive
				return new WeaselBoolean(mod_PClogic.DATA_BUS.getChannelState((String) args[0].get()));
				
			}else if(args.length == 2) {
				//send
				weaselRadioSignals.put((String) args[0].get(), Calc.toBoolean(args[1].get()));
				return null;
			}
			
		} else if (functionName.equals("rx")) {
			//receive
			return new WeaselBoolean(mod_PClogic.DATA_BUS.getChannelState((String) args[0].get()));
			
		} else if (functionName.equals("tx")) {
			//send
			weaselRadioSignals.put((String) args[0].get(), Calc.toBoolean(args[1].get()));
			return null;
			
		}
		
		throw new BadFunc();
		
	}
	
	/**
	 * Try to execute a function which works with the environment or surrounding blocks.
	 * @param engine the engine
	 * @param functionName function name
	 * @param args arguments given
	 * @return return value
	 * @throws BadFunc not supported by this method
	 */
	private WeaselObject fnAmbient(WeaselEngine engine, String functionName, WeaselObject[] args) throws BadFunc {
		
		if (functionName.equals("time")) {

			return new WeaselInteger(worldObj.worldInfo.getWorldTime());

		} else if (functionName.equals("emptyChest") || functionName.equals("empty")) {

			int rotation = worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 3;

			String side = (String) args[0].get();

			if (side.equals("B")) rotation = rotation + 0;
			if (side.equals("F")) rotation = rotation + 2;
			if (side.equals("L")) rotation = rotation + 1;
			if (side.equals("R")) rotation = rotation + 3;
			if (rotation > 3) rotation = rotation % 4;

			return new WeaselBoolean(isChestEmpty(rotation));

		} else if (functionName.equals("fullChest") || functionName.equals("full")) {

			int rotation = worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 3;

			String side = (String) args[0].get();
			boolean strict = false;

			if (args.length == 2) strict = (Boolean) args[1].get();

			if (side.equals("B")) rotation = rotation + 0;
			if (side.equals("F")) rotation = rotation + 2;
			if (side.equals("L")) rotation = rotation + 1;
			if (side.equals("R")) rotation = rotation + 3;

			if (rotation > 3) rotation = rotation % 4;

			return new WeaselBoolean(isChestFull(rotation, strict));

		} else if (functionName.equals("sleep")) {

			updateIgnoreCounter = (Integer) args[0].get();
			if (updateIgnoreCounter < 0) updateIgnoreCounter = 0;
			engine.requestPause();
			return null;

		} else if (functionName.equals("isDay")) {

			return new WeaselBoolean(worldObj.isDaytime());

		} else if (functionName.equals("idNight")) {

			return new WeaselBoolean(!worldObj.isDaytime());

		} else if (functionName.equals("isRaining")) {

			return new WeaselBoolean(worldObj.isRaining());

		}else {
			throw new BadFunc();
		}
		
	}
	
	/**
	 * Try to execute a sound function
	 * @param engine the engine
	 * @param functionName function name
	 * @param args arguments given
	 * @return return value
	 * @throws BadFunc not supported by this method
	 */
	private WeaselObject fnSound(WeaselEngine engine, String functionName, WeaselObject[] args) throws BadFunc {

		float volume = 1.0F;
		if (args.length >= 2 && args[1].getType() == WeaselObjectType.INTEGER) {
			if (args.length == 2) volume = ((Integer) args[1].get()) / 10F;
			if (volume > 5) volume = 5;
			if (volume < 0) volume = 0.001F;
		}

		if (functionName.equals("oink")) {
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
		}else {
			throw new BadFunc();
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
		} else if (type.equalsIgnoreCase("dirt") || type.equalsIgnoreCase("harp") || type.equalsIgnoreCase("piano") || type.equalsIgnoreCase("pi")) {
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
			PClo_BlockGate.hugeUpdate(worldObj, xCoord, yCoord, zCoord, mod_PClogic.gateOn.blockID);
//			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, mod_PClogic.gateOff.blockID);

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

		list.add("isDay");
		list.add("isNight");
		list.add("time");
		list.add("isRaining");
		list.add("emptyChest");
		list.add("empty");
		list.add("fullChest");
		list.add("full");
		list.add("sleep");
		
		list.add("bus.set");
		list.add("bus.get");
		
		list.add("bg");
		list.add("bs");
		
		list.add("radio");
		list.add("rx");
		list.add("tx");
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

	/**
	 * @param message set the error message
	 */
	public void setWeaselError(String message) {
		this.weaselError = message;
	}

	/**
	 * @return message describing last weasel error
	 */
	public String getWeaselError() {
		return this.weaselError;
	}

	@Override
	public boolean doesTransmitOnChannel(String channel) {
		return weaselRadioSignals.containsKey(channel) && weaselRadioSignals.get(channel) == true;
	}

}
