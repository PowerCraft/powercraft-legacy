package net.minecraft.src;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.src.PClo_NetManager.NetworkMember;
import net.minecraft.src.PClo_NetManager.WeaselNetwork;
import net.minecraft.src.PClo_RadioBus.IRadioDevice;
import weasel.Calc;
import weasel.IWeaselHardware;
import weasel.WeaselEngine;
import weasel.exception.SyntaxError;
import weasel.exception.WeaselRuntimeException;
import weasel.lang.Instruction;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselObject;


/**
 * The weasel CPU
 * 
 * @author MightyPork
 */
public class PClo_WeaselPluginCore extends PClo_WeaselPlugin implements IWeaselHardware, IRadioDevice {

	private boolean connectedToRadioBus = false;
	private int sleepTimer = 0;

	private int initialSleep = 5;

	/**
	 * Set this flag to pause weasel execution.
	 */
	public boolean paused = true;

	/**
	 * Flag set if this device is halted.
	 */
	public boolean halted = false;

	private WeaselNetwork providedNetwork = null;

	/**
	 * Rename the network, and reconnect all members.
	 * 
	 * @param newName new network name.
	 */
	public void renameNetwork(String newName) {
		if (providedNetwork != null) {
			getNetManager().renameNetwork(getNetworkName(), newName);
		}
	}

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


	/** Weasel's program source code */
	public String program = default_program;

	/** The Weasel Engine */
	private WeaselEngine weasel = null;

	/** Error in the weasel execution */
	private String weaselError = null;



	/**
	 * CORE plugin
	 * 
	 * @param tew weasel tile entity
	 */
	public PClo_WeaselPluginCore(PClo_TileEntityWeasel tew) {
		super(tew);
		setMemberName("CORE");
	}

	@Override
	public int getType() {
		return PClo_WeaselType.CORE;
	}

	@Override
	public boolean onClick(EntityPlayer player) {
		if (player.isSneaking()) {
			PC_Utils.openGres(player, new PClo_GuiWeaselCoreProgramBig(this));
		} else {
			PC_Utils.openGres(player, new PClo_GuiWeaselCoreProgram(this));
		}
		return true;
	}

	@Override
	public boolean updateTick() {

		if (!connectedToRadioBus) {
			getRadioManager().connectToRedstoneBus(this);
		}

		if (providedNetwork == null) {

			// do not call setNetworkName, it doesn't exist yet and setNetworkName tries to connect
			String netname = Calc.generateUniqueName();

			providedNetwork = getNetManager().createNetwork(netname);
			setNetworkNameAndConnect(netname);

		}


		if (initialSleep > 0) {
			initialSleep--;
			return false;
		}


		if (weaselError == null) {
			initWeaselIfNull();
			if (!weasel.isProgramFinished && !paused && !halted) {

				if (sleepTimer > 0) {
					sleepTimer--;
					return false;
				}

				try {
					initWeaselIfNull();
					
					if(externalCallsWaiting.size()>0) {
						int state = weasel.callFunctionExternal(externalCallsWaiting.get(0).a, externalCallsWaiting.get(0).b);
						
						if(state != 0) {
							externalCallsWaiting.remove(0);
						}
					}
					
					weasel.run(400);
				} catch (WeaselRuntimeException wre) {
					setError(wre.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
					setError(e.getMessage());
				}
				return true;
			}
		}


		return false;
	}

	private void setError(String message) {
		this.weaselError = message;
	}
	
	private ArrayList<PC_Struct2<String, Object[]>> externalCallsWaiting = new ArrayList<PC_Struct2<String,Object[]>>();

	@Override
	public void onRedstoneSignalChanged() {
		callFunctionOnEngine("update", new Object[] {});
	}

	@Override
	public String getError() {
		return this.weaselError;
	}

	/**
	 * @return true if has error
	 */
	@Override
	public boolean hasError() {
		return weaselError != null;
	}


	@Override
	protected PClo_WeaselPlugin readPluginFromNBT(NBTTagCompound tag) {
		program = tag.getString("program");

		if (program.equals("")) program = default_program;
		initWeaselIfNull();
		weasel.readFromNBT(tag.getCompoundTag("Weasel"));

		weaselError = tag.getString("weaselError");
		if (weaselError.equals("")) weaselError = null;

		NBTTagList list = tag.getTagList("wradio");

		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound ct = (NBTTagCompound) list.tagAt(i);
			weaselRadioSignals.put(ct.getString("C"), ct.getBoolean("S"));
		}

		paused = tag.getBoolean("Paused");
		halted = tag.getBoolean("Halted");
		sleepTimer = tag.getInteger("Sleep") + 3;

		NBTTagCompound networkTag = tag.getCompoundTag("NetworkData");
		// network name was already read by superclass.
		getNetManager().registerNetwork(getNetworkName(), providedNetwork = new WeaselNetwork().readFromNBT(networkTag));
		registerToNetwork();

		return this;
	}

	@Override
	protected NBTTagCompound writePluginToNBT(NBTTagCompound tag) {
		tag.setString("program", program);
		tag.setString("weaselError", (weaselError == null ? "" : weaselError));
		initWeaselIfNull();
		tag.setCompoundTag("Weasel", weasel.writeToNBT(new NBTTagCompound()));

		//direct radio signals			
		NBTTagList list = new NBTTagList();
		for (Entry<String, Boolean> entry : weaselRadioSignals.entrySet()) {
			NBTTagCompound ct = new NBTTagCompound();
			ct.setString("C", entry.getKey());
			ct.setBoolean("S", entry.getValue());
			list.appendTag(ct);
		}

		tag.setTag("wradio", list);

		tag.setBoolean("Paused", paused);
		tag.setBoolean("Halted", halted);
		tag.setInteger("Sleep", sleepTimer);		

		// network name will be saved by superclass		
		if (providedNetwork != null) tag.setCompoundTag("NetworkData", providedNetwork.writeToNBT(new NBTTagCompound()));

		return tag;
	}

	@Override
	public boolean doesTransmitOnChannel(String channel) {
		return weaselRadioSignals.containsKey(channel) && weaselRadioSignals.get(channel) == true;
	}

	/**
	 * Save the programmable gate's source code in this tile entity.
	 * 
	 * @param program the source code
	 */
	public void setProgram(String program) {
		this.program = program;
	}

	/**
	 * if weasel is null, create it.
	 */
	protected void initWeaselIfNull() {
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

		restartAllNetworkDevices();
		restartDevice();
		List<Instruction> list = WeaselEngine.compileProgram(program);
		
		if (getNetwork() != null) {
			for (NetworkMember member : getNetwork().getMembers().values()) {
				if (member != null && member != this && member instanceof PClo_WeaselPluginDiskDrive) {
					System.out.println("Linked libraries from drive "+((PClo_WeaselPluginDiskDrive)member).getName()+" if any.");
					list.addAll(((PClo_WeaselPluginDiskDrive)member).getAllLibraryInstructions());
				}
			}
		}
		
		weasel.insertNewProgram(list);
		halted = false;
		paused = false;
		weaselError = null;


//		try {
//			if (world() != null) weasel.run(500);
//		} catch (WeaselRuntimeException e) {
//			weaselError = e.getMessage();
//		}

	}

	/**
	 * Force restart program, but preserve source and instruction list.
	 */
	public void restartAllNetworkDevices() {

		if (getNetwork() == null) {
			restartDevice();
			return;
		}

		for (NetworkMember member : getNetwork().getMembers().values()) {
			if (member != null && member != this && member instanceof PClo_WeaselPlugin) ((PClo_WeaselPlugin) member).restartDevice();
		}

	}

	/**
	 * Stop program, clear outputs, prepare for new program execution.
	 */
	public void stopProgram() {
		restartAllNetworkDevices();
		halted = true;
		paused = false;
	}

	/**
	 * Check program for errors.
	 * 
	 * @param program the source code
	 * @throws SyntaxError
	 */
	public void checkProgramForErrors(String program) throws SyntaxError {
		List<Instruction> list = WeaselEngine.compileProgram(program);
		System.out.println();
		for (Instruction i : list) {
			System.out.println(i);
		}
	}


	private Map<String, Boolean> weaselRadioSignals = new HashMap<String, Boolean>(0);

	private static Random rand = new Random();


	@Override
	public boolean doesProvideFunction(String functionName) {

		// functions from the network are already added by getProvidedFunctionNames
		if (getProvidedFunctionNames().contains(functionName)) return true;

		return false;

	}

	private class BadFunc extends Exception {}

	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {

		if (halted || paused) return null;

		if (functionName.equals("bell")) {
			world().playSoundEffect(coord().x + 0.5D, coord().y + 0.5D, coord().z + 0.5D, "random.orb", 0.8F,
					(rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
			world().spawnParticle("note", coord().x + 0.5D, coord().y + 0.3D, coord().z + 0.5D, (functionName.length() * (3 + args.length)) / 24D,
					0.0D, 0.0D);
			return null;
		}


		try {
			return fnAmbient(engine, functionName, args);
		} catch (BadFunc e) {}

		try {
			return fnBus(engine, functionName, args);
		} catch (BadFunc e) {}


		WeaselNetwork net = getNetwork();
		if (net != null) {
			for (NetworkMember member : net.getMembers().values()) {
				if (member == this) continue;
				if (member.doesProvideFunction(functionName)) {
					return member.callProvidedFunction(engine, functionName, args);
				}
			}
		}


		throw new WeaselRuntimeException("Invalid call of function " + functionName);
	}

	/**
	 * try to execute a BUS function
	 * 
	 * @param engine the engine
	 * @param functionName function name
	 * @param args arguments given
	 * @return return value
	 * @throws BadFunc not supported by this method
	 */
	private WeaselObject fnBus(WeaselEngine engine, String functionName, WeaselObject[] args) throws BadFunc {

		if (functionName.equals("nset")) {

			getNetManager().setGlobalVariable((String) args[0].get(), args[1]);
			return null;

		} else if (functionName.equals("nget")) {

			return getNetManager().getGlobalVariable((String) args[0].get());

		} else if (functionName.equals("rx")) {
			//receive
			return new WeaselBoolean(mod_PClogic.RADIO.getChannelState((String) args[0].get()));

		} else if (functionName.equals("tx")) {
			//send
			weaselRadioSignals.put((String) args[0].get(), Calc.toBoolean(args[1].get()));
			return null;

		}

		throw new BadFunc();

	}



	/**
	 * Try to execute a function which works with the environment or surrounding
	 * blocks.
	 * 
	 * @param engine the engine
	 * @param functionName function name
	 * @param args arguments given
	 * @return return value
	 * @throws BadFunc not supported by this method
	 */
	private WeaselObject fnAmbient(WeaselEngine engine, String functionName, WeaselObject[] args) throws BadFunc {

		if (functionName.equals("empty")) {
			return this.chestEmptyTest(args);
		} else if (functionName.equals("full")) {
			return this.chestFullTest(args);

		} else if (functionName.equals("sleep")) {
			if (args.length == 0) {
				sleepTimer = 1;
				engine.requestPause();
				return null;
			}

			sleepTimer = (Integer) args[0].get();
			if (sleepTimer < 0) sleepTimer = 0;
			engine.requestPause();
			return null;

		} else if (functionName.equals("isDay")) {
			return new WeaselBoolean(world().isDaytime());
		} else if (functionName.equals("idNight")) {
			return new WeaselBoolean(!world().isDaytime());
		} else if (functionName.equals("isRaining")) {
			return new WeaselBoolean(world().isRaining());

		} else {
			throw new BadFunc();
		}

	}



	@Override
	public WeaselObject getVariable(String name) {


		if (halted || paused) return null;

		WeaselObject obj = null;

		if (name.equals("B") || name.equals("back")) {
			obj = new WeaselBoolean(getInport("B"));
		} else if (name.equals("L") || name.equals("left")) {
			obj = new WeaselBoolean(getInport("L"));
		} else if (name.equals("R") || name.equals("right")) {
			obj = new WeaselBoolean(getInport("R"));
		} else if (name.equals("F") || name.equals("front")) {
			obj = new WeaselBoolean(getInport("F"));
		} else if (name.equals("U") || name.equals("up") || name.equals("top")) {
			obj = new WeaselBoolean(getInport("U"));
		} else if (name.equals("D") || name.equals("down") || name.equals("bottom")) {
			obj = new WeaselBoolean(getInport("D"));
		} else {
			WeaselNetwork net = getNetwork();
			if (net != null) {
				for (NetworkMember member : net.getMembers().values()) {
					if (member == this) continue;
					obj = member.getVariable(name);
					if (obj != null) break;
				}
			}
		}


		return obj;
	}

	@Override
	public void setVariable(String name, Object object) {

		if (halted || paused) return;

		boolean setval = Calc.toBoolean(object);

		if (name.equals("B") || name.equals("back")) {
			setOutport("B", setval);
		} else if (name.equals("L") || name.equals("left")) {
			setOutport("L", setval);
		} else if (name.equals("R") || name.equals("right")) {
			setOutport("R", setval);
		} else if (name.equals("F") || name.equals("front")) {
			setOutport("F", setval);
		} else if (name.equals("U") || name.equals("up") || name.equals("top")) {
			setOutport("U", setval);
		} else if (name.equals("D") || name.equals("down") || name.equals("bottom")) {
			setOutport("D", setval);
		} else {
			WeaselNetwork net = getNetwork();
			if (net != null) {
				for (NetworkMember member : net.getMembers().values()) {
					if (member == this) continue;
					if (member.getVariable(name) != null) {
						member.setVariable(name, object);
						break;
					}
				}
			}
		}

	}

	@Override
	public List<String> getProvidedFunctionNames() {
		List<String> list = new ArrayList<String>();

		list.add("bell");

		list.add("isDay");
		list.add("isNight");

		list.add("isRaining");
		list.add("empty");
		list.add("full");
		list.add("sleep");

		list.add("nget");
		list.add("nset");

		list.add("rx");
		list.add("tx");


		WeaselNetwork net = getNetwork();
		if (net != null) {
			for (NetworkMember member : net.getMembers().values()) {
				if (member == this) continue;
				list.addAll(member.getProvidedFunctionNames());
			}
		}
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

		WeaselNetwork net = getNetwork();
		if (net != null) {
			for (NetworkMember member : net.getMembers().values()) {
				if (member == this) continue;
				list.addAll(member.getProvidedVariableNames());
			}
		}

		return list;

	}


	@Override
	public void onDeviceDestroyed() {
		getRadioManager().disconnectFromRedstoneBus(this);
		getNetManager().destroyNetwork(getNetworkName());
	}

	@Override
	public boolean isMaster() {
		return true;
	}

	@Override
	protected void onNetworkChanged() {
		// impossible, this is the master
	}

	@Override
	public WeaselEngine getWeaselEngine() {
		initWeaselIfNull();
		return weasel;
	}

	@Override
	public void callFunctionOnEngine(String function, Object... args) {
		if (hasError() || paused || halted) {
			return;
		}
		if(initialSleep > 0) {
			externalCallsWaiting.add(new PC_Struct2<String, Object[]>(function, args));
			return;
		}
		try {
			initWeaselIfNull();
			int state = weasel.callFunctionExternal(function, args);
			if(state == -1 || state == 1) return;	
			externalCallsWaiting.add(new PC_Struct2<String, Object[]>(function, args));
			
		} catch (WeaselRuntimeException wre) {
			weaselError = wre.getMessage();
		}
	}

	@Override
	public void restartDevice() {
		initWeaselIfNull();
		weasel.restartProgramClearGlobals();
		weaselError = null;
		weaselRadioSignals.clear();
		paused = false;
		halted = false;
		sleepTimer = 0;
		externalCallsWaiting.clear();
		resetOutport();
	}

	@Override
	public void onBlockPlaced(EntityLiving entityliving) {}

	@Override
	public void onRandomDisplayTick(Random random) {}

}
