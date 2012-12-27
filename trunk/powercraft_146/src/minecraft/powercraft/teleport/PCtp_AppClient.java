package powercraft.teleport;

import java.util.List;

import powercraft.management.PC_GresCheckBox;
import powercraft.management.PC_GresLabel;
import powercraft.management.PC_GresRadioButton;
import powercraft.management.PC_IClientModule;
import powercraft.management.PC_LangEntry;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_GresRadioButton.PC_GresRadioGroup;
import powercraft.management.PC_Utils.Lang;
import powercraft.management.PC_Utils.ModuleInfo;

public class PCtp_AppClient extends PCtp_App implements PC_IClientModule {

	@Override
	public List<PC_LangEntry> initLanguage(List<PC_LangEntry> lang) {
		lang.add(new PC_LangEntry("pc.gui.teleporter.name", "Name:"));
		lang.add(new PC_LangEntry("pc.gui.teleporter.target", "Target:"));
		lang.add(new PC_LangEntry("pc.gui.teleporter.nothing", "<Nothing>"));
		lang.add(new PC_LangEntry("pc.gui.teleporter.page1", "Teleporter"));
		lang.add(new PC_LangEntry("pc.gui.teleporter.animals", "animals"));
		lang.add(new PC_LangEntry("pc.gui.teleporter.monsters", "monsters"));
		lang.add(new PC_LangEntry("pc.gui.teleporter.items", "items"));
		lang.add(new PC_LangEntry("pc.gui.teleporter.players", "players"));
		lang.add(new PC_LangEntry("pc.gui.teleporter.sneakTrigger", "sneakTrigger"));
		lang.add(new PC_LangEntry("pc.gui.teleporter.playerChoose", "playerChoose"));
		lang.add(new PC_LangEntry("pc.gui.teleporter.north", "north"));
		lang.add(new PC_LangEntry("pc.gui.teleporter.east", "east"));
		lang.add(new PC_LangEntry("pc.gui.teleporter.south", "south"));
		lang.add(new PC_LangEntry("pc.gui.teleporter.west", "west"));
		lang.add(new PC_LangEntry("pc.gui.teleporter.page2", "Property"));
		lang.add(new PC_LangEntry("pc.gui.teleportTo.title", "Teleport To"));
		return lang;
	}

	@Override
	public List<String> loadTextureFiles(List<String> textures) {
		return null;
	}

	@Override
	public List<String> addSplashes(List<String> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(
			List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("Teleporter", PCtp_GuiTeleporter.class));
		guis.add(new PC_Struct2<String, Class>("PlayerTeleport", PCtp_GuiPlayerTeleport.class));
		return guis;
	}
	
}
