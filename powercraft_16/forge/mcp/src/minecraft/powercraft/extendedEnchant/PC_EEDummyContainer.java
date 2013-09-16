package powercraft.extendedEnchant;

import java.util.Arrays;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class PC_EEDummyContainer extends DummyModContainer {

	
	public PC_EEDummyContainer() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "extendedEnchant";
		meta.name = "Extended Enchanting";
		meta.version = "@VERSION@"; //String.format("%d.%d.%d.%d", majorVersion, minorVersion, revisionVersion, buildVersion);
		meta.credits = "Special thanks to culegooner for the tutorials!";
		meta.authorList = Arrays.asList("Buggi");
		meta.description = "";
		meta.url = "";
		meta.updateUrl = "";
		meta.screenshots = new String[0];
		meta.logoFile = "";
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}

	@SuppressWarnings("unused")
	@Subscribe
	public void preInit(FMLPreInitializationEvent evt) {

	}

	@SuppressWarnings("unused")
	@Subscribe
	public void postInit(FMLPostInitializationEvent evt) {

	}

}