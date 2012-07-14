package net.minecraft.src;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import net.minecraft.src.PClo_NetManager.NetworkMember;
import net.minecraft.src.PClo_RadioBus.IRadioDevice;
import net.minecraft.src.PClo_NetManager.WeaselNetwork;

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
 * The weasel CPU
 * 
 * @author MightyPork
 */
public class PClo_WeaselPluginCore extends PClo_WeaselPlugin implements IWeaselHardware, IRadioDevice {

	private boolean connectedToRadioBus = false;
	private int sleepTimer = 0;
	/**
	 * Set this flag to pause weasel execution.
	 */
	public boolean paused = true;
	
	private WeaselNetwork coreProvidedNetwork = null;
	
	/**
	 * Rename the network, and reconnect all members.
	 * @param newName new network name.
	 */
	public void renameNetwork(String newName) {
		if(coreProvidedNetwork != null) {
			mod_PClogic.NETWORK.renameNetwork(getNetworkName(), newName);
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
	public void updateTick() {

		if (!connectedToRadioBus) {
			mod_PClogic.RADIO.connectToRedstoneBus(this);
		}

		if(coreProvidedNetwork == null) {
			
			// do not call setNetworkName, it doesn't exist yet and setNetworkName tries to connect
			String netname = Calc.generateUniqueName();
			
			coreProvidedNetwork = getNetManager().createNetwork(netname);
			setNetworkNameAndConnect(netname);
			
		}


		if (weaselError == null) {
			initWeaselIfNull();
			if (!weasel.isProgramFinished && !paused) {
				
				if (sleepTimer > 0) {
					sleepTimer--;
					return;
				}

				try {
					initWeaselIfNull();
					weasel.run(300);
				} catch (WeaselRuntimeException wre) {
					setError(wre.getMessage());
				}
			}
		}



	}

	private void setError(String message) {
		this.weaselError = message;
	}

	@Override
	public void onRedstoneSignalChanged() {
		if (hasError() || paused) return;

		try {
			initWeaselIfNull();
			if (weasel.callFunctionExternal("update", new Object[] {})) {
				weasel.run(400);
			} else {
				// update() is missing
			}

		} catch (WeaselRuntimeException wre) {
			weaselError = wre.getMessage();
		}
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
		sleepTimer = tag.getInteger("Sleep")+10;
		
		NBTTagCompound networkTag = tag.getCompoundTag("NetworkData");
		// network name was already read by superclass.
		getNetManager().registerNetwork(getNetworkName(), coreProvidedNetwork = new WeaselNetwork().readFromNBT(networkTag));
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
		tag.setInteger("Sleep", sleepTimer);
		
		
		
		// network name will be saved by superclass		
		if(coreProvidedNetwork != null) tag.setCompoundTag("NetworkData", coreProvidedNetwork.writeToNBT(new NBTTagCompound()));
		
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
		resetOutport();
		weaselRadioSignals.clear();
		paused = false;

//		try {
//			if (world() != null) weasel.run(500);
//		} catch (WeaselRuntimeException e) {
//			weaselError = e.getMessage();
//		}

	}
	
	/**
	 * Force restart program, but preserve source and instruction list.
	 */
	public void restartProgram() {
		initWeaselIfNull();
		weasel.restartProgramClearGlobals();
		weaselError = null;
		resetOutport();
		weaselRadioSignals.clear();
		paused = false;
		sleepTimer = 0;
	}
	
	/**
	 * Stop program, clear outputs, prepare for new program execution.
	 */
	public void stopProgram() {
		restartProgram();
		weasel.isProgramFinished = true;
		paused = false;
	}

	/**
	 * Check program for errors.
	 * 
	 * @param program the source code
	 * @throws SyntaxError
	 */
	public void checkProgramForErrors(String program) throws SyntaxError {
		List<Instruction> list = 
		WeaselEngine.compileProgram(program);
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
		if(getProvidedFunctionNames().contains(functionName)) return true;
		
		return false;
		
	}

	private class BadFunc extends Exception {}

	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
		try {
			return fnSound(engine, functionName, args);
		} catch (BadFunc e) {}

		try {
			return fnAmbient(engine, functionName, args);
		} catch (BadFunc e) {}

		try {
			return fnBus(engine, functionName, args);
		} catch (BadFunc e) {}
		
		
		WeaselNetwork net = getNetwork();
		if(net != null) {
			for(NetworkMember member: net.members.values()) {
				if(member == this) continue;
				if(member.doesProvideFunction(functionName)) {
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

		if (functionName.equals("set")) {

			mod_PClogic.NETWORK.setGlobalVariable((String) args[0].get(), args[1]);
			return null;

		} else if (functionName.equals("get")) {

			return mod_PClogic.NETWORK.getGlobalVariable((String) args[0].get());

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

		if (functionName.equals("time")) {

			return new WeaselInteger(world().worldInfo.getWorldTime());

		} else if (functionName.equals("empty")) {

			return this.chestEmptyTest(args);

		} else if (functionName.equals("full")) {

			return this.chestFullTest(args);

		} else if (functionName.equals("sleep")) {

			if(args.length == 0) {
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

	/**
	 * Try to execute a sound function
	 * 
	 * @param engine the engine
	 * @param functionName function name
	 * @param args arguments given
	 * @return return value
	 * @throws BadFunc not supported by this method
	 */
	private WeaselObject fnSound(WeaselEngine engine, String functionName, WeaselObject[] args) throws BadFunc {

		WeaselObject ret = null;
		
		do {
			float volume = 1.0F;
			
			if(args.length > 0) {
				if(args[args.length-1] instanceof WeaselInteger) {
					volume = ((Integer) args[args.length-1].get()) / 10F;
				}
			}
			
			if (volume > 5) volume = 5;
			if (volume < 0) volume = 0.001F;
			
			// sound(resource [, volume])
			if (functionName.equals("sound")) {
				world().playSoundEffect(coord().x + 0.5D, coord().y + 0.5D, coord().z + 0.5D, ((String) args[0].get()), volume, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
				break;
			}
			
			// note(resourceOrInstrument, tone [, volume])
			if (functionName.equals("note")) {	
				if(args.length != 3) volume = 3;
				return playNote(((String) args[0].get()), ((Integer) args[1].get()), volume);
			}
			
			String sound = "random.orb";
			boolean direct = false;
	
			// ???([volume]);
			if (functionName.equals("oink")) {
				sound = "mob.pig";
				direct = true;
				
			} else if (functionName.equals("bell")) {
				sound = "random.orb";
				direct = true;
				
			} else if (functionName.equals("click")) {
				sound = "random.click";
				direct = true;
				
			} else if (functionName.equals("moo")) {
				sound = "mob.cow";
				direct = true;
				
			} else if (functionName.equals("baa")) {
				sound = "mob.sheep";
				direct = true;
	
			} else if (functionName.equals("cluck")) {
				sound = "mob.chicken";
				direct = true;
	
			} else if (functionName.equals("woof")) {
				sound = "mob.wolf.bark";
				direct = true;
	
			}
			
			if(direct) {
				world().playSoundEffect(coord().x + 0.5D, coord().y + 0.5D, coord().z + 0.5D, sound, volume, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
				break;
			}			
			
			throw new BadFunc();
			
		}while(false);
		
		world().spawnParticle("note", coord().x + 0.5D, coord().y + 0.3D, coord().z + 0.5D, (functionName.length() * (3 + args.length)) / 24D, 0.0D, 0.0D);

		return ret;
	}

	private WeaselObject playNote(String type, int tone, float volume) {
		float f = (float) Math.pow(2D, (tone - 12) / 12D);
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

		world().playSoundEffect(coord().x + 0.5D, coord().y + 0.5D, coord().z + 0.5D, s, volume, f);
		world().spawnParticle("note", coord().x + 0.5D, coord().y + 0.3D, coord().z + 0.5D, tone / 24D, 0.0D, 0.0D);
		return null;
	}

	@Override
	public WeaselObject getVariable(String name) {

		WeaselObject obj = null;

		if (name.equals("B") || name.equals("back")) {
			obj = new WeaselBoolean(getInport("B"));
		}else
		if (name.equals("L") || name.equals("left")) {
			obj = new WeaselBoolean(getInport("L"));
		}else
		if (name.equals("R") || name.equals("right")) {
			obj = new WeaselBoolean(getInport("R"));
		}else
		if (name.equals("F") || name.equals("front")) {
			obj = new WeaselBoolean(getInport("F"));
		}else
		if (name.equals("U") || name.equals("up") || name.equals("top")) {
			obj = new WeaselBoolean(getInport("U"));
		}else
		if (name.equals("D") || name.equals("down") || name.equals("bottom")) {
			obj = new WeaselBoolean(getInport("D"));
		}else {
			WeaselNetwork net = getNetwork();
			if(net != null) {
				for(NetworkMember member: net.members.values()) {
					if(member == this) continue;
					obj = member.getVariable(name);
					if(obj != null) break;
				}
			}
		}


		return obj;
	}

	@Override
	public void setVariable(String name, Object object) {

		boolean change = false;
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
			if(net != null) {
				for(NetworkMember member: net.members.values()) {
					if(member == this) continue;
					if(member.getVariable(name) != null) {
						member.setVariable(name, object);
						break;
					}
				}
			}
		}

		if (change) {
			notifyBlockChange();
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
		list.add("bell");
		list.add("sound");
		list.add("note");

		list.add("isDay");
		list.add("isNight");
		list.add("time");
		list.add("isRaining");
		list.add("empty");
		list.add("full");
		list.add("sleep");

		list.add("get");
		list.add("set");

		list.add("rx");
		list.add("tx");
		
		
		WeaselNetwork net = getNetwork();
		if(net != null) {
			for(NetworkMember member: net.members.values()) {
				if(member == this) continue;
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
		if(net != null) {
			for(NetworkMember member: net.members.values()) {
				if(member == this) continue;
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
		return weasel;
	}

	@Override
	public Object callFunctionExternalDelegated(String function, Object... args) {
		try {
			initWeaselIfNull();
			if (weasel.callFunctionExternal(function, args)) {
				weasel.run(800);
				return weasel.getRetvalExternal();
			} else {
				// is missing
			}
		} catch (WeaselRuntimeException wre) {
			weaselError = wre.getMessage();
		}
		return null;
	}

}
