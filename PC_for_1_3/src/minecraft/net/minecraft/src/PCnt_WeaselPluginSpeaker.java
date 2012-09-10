package net.minecraft.src;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import weasel.WeaselEngine;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselInteger;
import weasel.obj.WeaselObject;


/**
 * @author MightyPork
 */
public class PCnt_WeaselPluginSpeaker extends PCnt_WeaselPlugin {

	/**
	 * @param tew tile entity weasel
	 */
	public PCnt_WeaselPluginSpeaker(PCnt_TileEntityWeasel tew) {
		super(tew);
	}

	@Override
	public int getType() {
		return PCnt_WeaselType.SPEAKER;
	}

	@Override
	public boolean onClick(EntityPlayer player) {
		PC_Utils.openGres(player, new PCnt_GuiWeaselSpeaker(this));
		return true;
	}

	@Override
	public boolean updateTick() {
		return false;
	}

	@Override
	public void onRedstoneSignalChanged() {}

	@Override
	public String getError() {
		return null;
	}

	@Override
	public boolean hasError() {
		return false;
	}

	@Override
	protected PCnt_WeaselPlugin readPluginFromNBT(NBTTagCompound tag) {
		return this;
	}

	@Override
	protected NBTTagCompound writePluginToNBT(NBTTagCompound tag) {
		return tag;
	}

	@Override
	public void onDeviceDestroyed() {}

	@Override
	public boolean doesProvideFunction(String functionName) {
		return getProvidedFunctionNames().contains(functionName);
	}

	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
		//engine.requestPause();

		if (!functionName.startsWith(getName() + ".")) return null;

		functionName = functionName.substring((getName() + ".").length());

		do {
			float volume = 1.0F;

			if (args.length > 0) {
				if (args[args.length - 1] instanceof WeaselInteger) {
					volume = ((Integer) args[args.length - 1].get()) / 10F;
				}
			}

			if (volume > 5) volume = 5;
			if (volume < 0) volume = 0.001F;

			// sound(resource [, volume])
			if (functionName.equals("sound") || functionName.equals("play")) {
				world().playSoundEffect(coord().x + 0.5D, coord().y + 0.5D, coord().z + 0.5D, ((String) args[0].get()), volume,
						(rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
				break;
			}

			// note(resourceOrInstrument, tone [, volume])
			if (functionName.equals("note")) {
				if (args.length != 3) volume = 3;
				return playNote(((String) args[0].get()), ((Integer) args[1].get()), volume);
			}

			// instr(tone [, volume])
			if (functionName.equals("snare") || functionName.equals("snaredrum") || functionName.equals("sd")) {
				if (args.length != 2) volume = 3;
				return playNote("snaredrum", ((Integer) args[0].get()), volume);
			}

			// instr(tone [, volume])
			if (functionName.equals("bass") || functionName.equals("bassdrum") || functionName.equals("drum") || functionName.equals("bs")) {
				if (args.length != 2) volume = 3;
				return playNote("bassdrum", ((Integer) args[0].get()), volume);
			}

			// instr(tone [, volume])
			if (functionName.equals("guitar") || functionName.equals("bassguitar") || functionName.equals("bg")) {
				if (args.length != 2) volume = 3;
				return playNote("bassguitar", ((Integer) args[0].get()), volume);
			}

			// instr(tone [, volume])
			if (functionName.equals("sticks") || functionName.equals("stick") || functionName.equals("clicks") || functionName.equals("click")
					|| functionName.equals("st") || functionName.equals("cl")) {
				if (args.length != 2) volume = 3;
				return playNote("sticks", ((Integer) args[0].get()), volume);
			}

			// instr(tone [, volume])
			if (functionName.equals("piano") || functionName.equals("harp") || functionName.equals("p") || functionName.equals("pn")) {
				if (args.length != 2) volume = 3;
				return playNote("harp", ((Integer) args[0].get()), volume);
			}

			// instr(tone [, volume])
			if ((functionName.equals("bell") || functionName.equals("orb"))) {
				if (args.length != 2) volume = 3;
				return playNote("random.orb", ((Integer) args[0].get()), volume);
			}

			String sound = "";
			boolean direct = false;

			// ???([volume]);
			if (functionName.equals("oink")) {
				sound = "mob.pig";
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

			if (direct) {
				world().playSoundEffect(coord().x + 0.5D, coord().y + 0.5D, coord().z + 0.5D, sound, volume,
						(rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
				break;
			}

			throw new WeaselRuntimeException("Invalid function called on speaker " + getName());

		} while (false);

		world().spawnParticle("note", coord().x + 0.5D, coord().y + 1.2D, coord().z + 0.5D, (functionName.length() * (3 + args.length)) / 24D, 0.0D,
				0.0D);

		return null;
	}

	private WeaselObject playNote(String type, int tone, float volume) {
		float f = (float) Math.pow(2D, (tone - 12) / 12D);
		String s = type;

		if (type.equalsIgnoreCase("stone") || type.equalsIgnoreCase("bass drum") || type.equalsIgnoreCase("bassdrum") || type.equalsIgnoreCase("bd")
				|| type.equalsIgnoreCase("drum")) {
			s = "note.bd";
		} else if (type.equalsIgnoreCase("sand") || type.equalsIgnoreCase("snare drum") || type.equalsIgnoreCase("snaredrum")
				|| type.equalsIgnoreCase("sd") || type.equalsIgnoreCase("snare")) {
			s = "note.snare";
		} else if (type.equalsIgnoreCase("glass") || type.equalsIgnoreCase("stick") || type.equalsIgnoreCase("sticks") || type.equalsIgnoreCase("cl")
				|| type.equalsIgnoreCase("clicks") || type.equalsIgnoreCase("click")) {
			s = "note.hat";
		} else if (type.equalsIgnoreCase("wood") || type.equalsIgnoreCase("bass guitar") || type.equalsIgnoreCase("bassguitar")
				|| type.equalsIgnoreCase("bg") || type.equalsIgnoreCase("guitar")) {
			s = "note.bassattack";
		} else if (type.equalsIgnoreCase("dirt") || type.equalsIgnoreCase("harp") || type.equalsIgnoreCase("piano") || type.equalsIgnoreCase("pi")) {
			s = "note.harp";
		}

		world().playSoundEffect(coord().x + 0.5D, coord().y + 0.5D, coord().z + 0.5D, s, volume, f);
		world().spawnParticle("note", coord().x + 0.5D, coord().y + 1.2D, coord().z + 0.5D, tone / 24D, 0.0D, 0.0D);
		return null;
	}

	@Override
	public WeaselObject getVariable(String name) {
		return null;
	}

	@Override
	public void setVariable(String name, Object object) {}

	@Override
	public List<String> getProvidedFunctionNames() {
		List<String> list = new ArrayList<String>(0);

		list.add(getName() + ".oink");
		list.add(getName() + ".moo");
		list.add(getName() + ".baa");
		list.add(getName() + ".cluck");
		list.add(getName() + ".woof");

		list.add(getName() + ".sound");
		list.add(getName() + ".note");
		list.add(getName() + ".play");

		list.add(getName() + ".piano");
		list.add(getName() + ".p");
		list.add(getName() + ".pn");
		list.add(getName() + ".harp");
		list.add(getName() + ".sticks");
		list.add(getName() + ".st");
		list.add(getName() + ".stick");
		list.add(getName() + ".cl");
		list.add(getName() + ".clicks");
		list.add(getName() + ".click");
		list.add(getName() + ".snare");
		list.add(getName() + ".snaredrum");
		list.add(getName() + ".sd");
		list.add(getName() + ".drum");
		list.add(getName() + ".bassdrum");
		list.add(getName() + ".bd");
		list.add(getName() + ".guitar");
		list.add(getName() + ".bassguitar");
		list.add(getName() + ".bg");
		list.add(getName() + ".orb");
		list.add(getName() + ".bell");

		return list;
	}

	@Override
	public List<String> getProvidedVariableNames() {
		List<String> list = new ArrayList<String>(0);
		return list;
	}

	@Override
	public boolean isMaster() {
		return false;
	}

	@Override
	protected void onNetworkChanged() {}

	@Override
	public WeaselEngine getWeaselEngine() {
		return null;
	}

	@Override
	public void callFunctionOnEngine(String function, Object... args) {}

	@Override
	public void restartDevice() {}

	@Override
	public void onBlockPlaced(EntityLiving entityliving) {}

	@Override
	public void onRandomDisplayTick(Random random) {}

	@Override
	public float[] getBounds() {
		return new float[] { 0, 0, 0, 1, 1, 1 };
	}

}
