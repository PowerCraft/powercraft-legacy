package powercraft.weasel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Struct4;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.Gres;
import weasel.WeaselEngine;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselDouble;
import weasel.obj.WeaselNull;
import weasel.obj.WeaselObject;

public class PCws_WeaselPluginSpeaker extends PCws_WeaselPlugin {
	
	private static Random rand = new Random();
	
	@Override
	protected List<String> getProvidedPluginFunctionNames() {
		List<String> list = new ArrayList<String>(0);

		list.add("sound");
		list.add("note");
		list.add("play");

		list.add("piano");
		list.add("p");
		list.add("pn");
		list.add("harp");
		list.add("sticks");
		list.add("st");
		list.add("stick");
		list.add("cl");
		list.add("clicks");
		list.add("click");
		list.add("snare");
		list.add("snaredrum");
		list.add("sd");
		list.add("drum");
		list.add("bassdrum");
		list.add("bd");
		list.add("guitar");
		list.add("bassguitar");
		list.add("bg");
		list.add("orb");
		list.add("bell");

		return list;
	}

	@Override
	protected WeaselObject callProvidedPluginFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
		float volume = 1.0F;

		if (args.length > 0) {
			if (args[args.length - 1] instanceof WeaselDouble) {
				volume = ((Integer) args[args.length - 1].get()) / 10F;
			}
		}

		if (volume > 5) volume = 5;
		if (volume < 0) volume = 0.001F;

		if (functionName.equals("sound") || functionName.equals("play")) {
			// sound(resource [, volume])
			play(((String) args[0].get()), volume, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F, (functionName.length() * (3 + args.length)) / 24f);
		}else if (functionName.equals("note")) {
			// note(resourceOrInstrument, tone [, volume])
			if (args.length != 3) volume = 3;
			playNote(((String) args[0].get()), ((Integer) args[1].get()), volume);
		}else if (functionName.equals("snare") || functionName.equals("snaredrum") || functionName.equals("sd")) {
			// instr(tone [, volume])
			if (args.length != 2) volume = 3;
			playNote("snaredrum", ((Integer) args[0].get()), volume);
		}else if (functionName.equals("bass") || functionName.equals("bassdrum") || functionName.equals("drum") || functionName.equals("bs")) {
			// instr(tone [, volume])
			if (args.length != 2) volume = 3;
			playNote("bassdrum", ((Integer) args[0].get()), volume);
		}else if (functionName.equals("guitar") || functionName.equals("bassguitar") || functionName.equals("bg")) {
			// instr(tone [, volume])
			if (args.length != 2) volume = 3;
			playNote("bassguitar", ((Integer) args[0].get()), volume);
		}else if (functionName.equals("sticks") || functionName.equals("stick") || functionName.equals("clicks") || functionName.equals("click")
				|| functionName.equals("st") || functionName.equals("cl")) {
			// instr(tone [, volume])
			if (args.length != 2) volume = 3;
			playNote("sticks", ((Integer) args[0].get()), volume);
		}else if (functionName.equals("piano") || functionName.equals("harp") || functionName.equals("p") || functionName.equals("pn")) {
			// instr(tone [, volume])
			if (args.length != 2) volume = 3;
			playNote("harp", ((Integer) args[0].get()), volume);
		}else if ((functionName.equals("bell") || functionName.equals("orb"))) {
			// instr(tone [, volume])
			if (args.length != 2) volume = 3;
			playNote("random.orb", ((Integer) args[0].get()), volume);
			
		} else {
			throw new WeaselRuntimeException("Invalid function called on speaker " + getName());
		}

		return new WeaselNull();
	}

	private void playNote(String type, int tone, float volume) {
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
		
		play( s, volume, f, f/24f);
	}
	
	private void play(String type, float volume, float pitch, float val) {
		PCws_TileEntityWeasel te = getTE();
		if(te!=null){
			PC_PacketHandler.setTileEntity(te, "msg", "play", new PC_Struct4<String, Float, Float, Float>(type, volume, pitch, val));
		}
	}
	
	@Override
	protected List<String> getProvidedPluginVariableNames() {
		return new ArrayList<String>();
	}

	@Override
	protected void setPluginVariable(String name, Object value) {}

	@Override
	protected WeaselObject getPluginVariable(String name) {
		return null;
	}

	@Override
	public void update() {}

	@Override
	public void refreshInport(){}
	
	@Override
	public void syncWithClient(PCws_TileEntityWeasel tileEntityWeasel) {}

	@Override
	protected void openPluginGui(EntityPlayer player) {
		Gres.openGres("WeaselOnlyNet", player, getPos().x, getPos().y, getPos().z);
	}

	@Override
	public void restart() {}

}
