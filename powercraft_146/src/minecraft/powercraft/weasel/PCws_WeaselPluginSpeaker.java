package powercraft.weasel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Struct4;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.Gres;
import weasel.Calc;
import weasel.WeaselEngine;
import weasel.WeaselFunctionManager;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselDouble;
import weasel.obj.WeaselNull;
import weasel.obj.WeaselObject;

public class PCws_WeaselPluginSpeaker extends PCws_WeaselPlugin {
	
	private static Random rand = new Random();

	@Override
	public WeaselFunctionManager makePluginProvider() {
		WeaselFunctionManager fp = new WeaselFunctionManager();
		fp.registerMethod("play", this);
		fp.registerMethod("sound", "play", this);
		fp.registerMethod("note", this);
		fp.registerMethod("piano", this);
		fp.registerMethod("p", "piano", this);
		fp.registerMethod("pn", "piano", this);
		fp.registerMethod("harp", "piano", this);
		fp.registerMethod("sticks", this);
		fp.registerMethod("st", "sticks", this);
		fp.registerMethod("stick", "sticks", this);
		fp.registerMethod("cl", "sticks", this);
		fp.registerMethod("clicks", "sticks", this);
		fp.registerMethod("click", "sticks", this);
		fp.registerMethod("snare", this);
		fp.registerMethod("snaredrum", "snare", this);
		fp.registerMethod("sd", "snare", this);
		fp.registerMethod("bass", this);
		fp.registerMethod("drum", "bass", this);
		fp.registerMethod("bassdrum", "bass", this);
		fp.registerMethod("bd", "bass", this);
		fp.registerMethod("guitar", this);
		fp.registerMethod("bassguitar", "guitar", this);
		fp.registerMethod("bg", "guitar", this);
		fp.registerMethod("bell", this);
		fp.registerMethod("orb", "bell", this);
		return fp;
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
		
		play(s, volume, f, f/24f);
	}
	
	private void play(String type, float volume, float pitch, float val) {
		PCws_TileEntityWeasel te = getTE();
		if(te!=null){
			PC_PacketHandler.setTileEntity(te, "msg", "play", new PC_Struct4<String, Float, Float, Float>(type, volume, pitch, val));
		}
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

	public void play(String sound){
		play(sound, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F, rand.nextFloat());
	}
	
	public void play(String sound, float volume){
		play(sound, volume, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F, rand.nextFloat());
	}
	
	public void note(String sound, int tone){
		playNote(sound, tone, 1.0f);
	}
	
	public void note(String sound, int tone, float volume){
		playNote(sound, tone, volume);
	}
	
	public void snare(int tone){
		playNote("snaredrum", tone, 1.0f);
	}
	
	public void snare(int tone, float volume){
		playNote("snaredrum", tone, volume);
	}
	
	public void bass(int tone){
		playNote("bassdrum", tone, 1.0f);
	}
	
	public void bass(int tone, float volume){
		playNote("bassdrum", tone, volume);
	}
	
	public void guitar(int tone){
		playNote("bassguitar", tone, 1.0f);
	}
	
	public void guitar(int tone, float volume){
		playNote("bassguitar", tone, volume);
	}
	
	public void sticks(int tone){
		playNote("sticks", tone, 1.0f);
	}
	
	public void sticks(int tone, float volume){
		playNote("sticks", tone, volume);
	}
	
	public void piano(int tone){
		playNote("piano", tone, 1.0f);
	}
	
	public void piano(int tone, float volume){
		playNote("piano", tone, volume);
	}
	
	public void bell(int tone){
		playNote("bell", tone, 1.0f);
	}
	
	public void bell(int tone, float volume){
		playNote("bell", tone, volume);
	}
	
}
