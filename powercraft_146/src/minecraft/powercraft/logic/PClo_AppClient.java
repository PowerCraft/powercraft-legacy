package powercraft.logic;

import java.util.List;

import powercraft.management.PC_IClientModule;
import powercraft.management.PC_LangEntry;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.ModuleInfo;

public class PClo_AppClient extends PClo_App implements PC_IClientModule {

	@Override
    public List<String> loadTextureFiles(List<String> textures)
    {
        textures.add(ModuleInfo.getTerrainFile(this));
        return textures;
    }
	
	@Override
	public List<PC_LangEntry> initLanguage(List<PC_LangEntry> lang) {
		lang.add(new PC_LangEntry("pc.pulsar.clickMsg", "Period %s ticks (%s s)"));
		lang.add(new PC_LangEntry("pc.pulsar.clickMsgTime", "Period %s ticks (%s s), remains %s"));
		lang.add(new PC_LangEntry("pc.gate.not.desc", "negates input"));
		lang.add(new PC_LangEntry("pc.gate.and.desc", "all inputs on"));
		lang.add(new PC_LangEntry("pc.gate.nand.desc", "some inputs off"));
		lang.add(new PC_LangEntry("pc.gate.or.desc", "at least one input on"));
		lang.add(new PC_LangEntry("pc.gate.nor.desc", "all inputs off"));
		lang.add(new PC_LangEntry("pc.gate.xor.desc", "inputs different"));
		lang.add(new PC_LangEntry("pc.gate.xnor.desc", "inputs equal"));
		lang.add(new PC_LangEntry("pc.flipflop.D.desc", "latch memory"));
		lang.add(new PC_LangEntry("pc.flipflop.RS.desc", "set/reset memory"));
		lang.add(new PC_LangEntry("pc.flipflop.T.desc", "divides signal by 2"));
		lang.add(new PC_LangEntry("pc.flipflop.random.desc", "changes state randomly on pulse"));
		lang.add(new PC_LangEntry("pc.delayer.buffer.desc", "slows down signal"));
		lang.add(new PC_LangEntry("pc.delayer.slowRepeater.desc", "makes pulses longer"));
		lang.add(new PC_LangEntry("pc.special.day.desc", "on during day"));
		lang.add(new PC_LangEntry("pc.special.night.desc", "on during night"));
		lang.add(new PC_LangEntry("pc.special.rain.desc", "on during rain"));
		lang.add(new PC_LangEntry("pc.special.chestEmpty.desc", "on if nearby container is empty"));
		lang.add(new PC_LangEntry("pc.special.chestFull.desc", "on if nearby container is full"));
		lang.add(new PC_LangEntry("pc.special.special.desc", "spawner & pulsar control"));
		lang.add(new PC_LangEntry("pc.repeater.crossing.desc", "lets two wires intersect"));
		lang.add(new PC_LangEntry("pc.repeater.splitter.desc", "splits signal"));
		lang.add(new PC_LangEntry("pc.repeater.repeaterStraight.desc", "simple 1-tick repeater"));
		lang.add(new PC_LangEntry("pc.repeater.repeaterCorner.desc", "simple 1-tick corner repeater"));
		lang.add(new PC_LangEntry("pc.repeater.repeaterStraightInstant.desc", "instant repeater"));
		lang.add(new PC_LangEntry("pc.repeater.repeaterCornerInstant.desc", "instant corner repeater"));
		lang.add(new PC_LangEntry("pc.gui.pulsar.silent", "Silent"));
		lang.add(new PC_LangEntry("pc.gui.pulsar.paused", "Pause"));
		lang.add(new PC_LangEntry("pc.gui.pulsar.delay", "Delay (sec)"));
		lang.add(new PC_LangEntry("pc.gui.pulsar.hold", "Hold time (sec)"));
		lang.add(new PC_LangEntry("pc.gui.pulsar.ticks", "ticks"));
		lang.add(new PC_LangEntry("pc.gui.pulsar.errDelay", "Bad delay time!"));
		lang.add(new PC_LangEntry("pc.gui.pulsar.errHold", "Bad hold time!"));
		lang.add(new PC_LangEntry("pc.gui.delayer.delay", "Delay (sec)"));
		lang.add(new PC_LangEntry("pc.gui.pulsar.errintputzero", "Bad delay time!"));
		lang.add(new PC_LangEntry("pc.gui.delayer.errnoinput", "No Input!"));
		lang.add(new PC_LangEntry("pc.gui.special.chestEmpty.name", "Empty Chest"));
		lang.add(new PC_LangEntry("pc.gui.special.chestEmpty.inv", "No item of kind"));
		lang.add(new PC_LangEntry("pc.gui.special.chestFull.name", "Full Chest"));
		lang.add(new PC_LangEntry("pc.gui.special.chestFull.inv", "Space for"));
		return lang;
	}

	@Override
    public List<String> addSplashes(List<String> list)
    {
        list.add("Adjustable clock pulse!");
        return list;
    }

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(
			List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("Delayer", PClo_GuiDelayer.class));
		guis.add(new PC_Struct2<String, Class>("Pulsar", PClo_GuiPulsar.class));
		guis.add(new PC_Struct2<String, Class>("Special", PClo_GuiSpecial.class));
		return guis;
	}
	
	
}
