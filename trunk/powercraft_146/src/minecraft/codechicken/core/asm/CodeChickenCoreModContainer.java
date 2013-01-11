package codechicken.core.asm;

import java.util.Arrays;

import codechicken.packager.Packager;
import codechicken.packager.SrcPackager;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@SrcPackager(getMappedDirectories = {"CodeChickenCore"}, getClasses = {""}, getName = "CodeChickenCore")
@Packager(getBaseDirectories = {"CodeChickenCore"}, getClasses = {""}, getName = "CodeChickenCore", getVersion = "0.7.1.0")
public class CodeChickenCoreModContainer extends DummyModContainer
{
	public CodeChickenCoreModContainer()
	{
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
        meta.modId       = "CodeChickenCore";
        meta.name        = "CodeChicken Core";
        meta.version     = getClass().getAnnotation(Packager.class).getVersion();
        meta.authorList  = Arrays.asList("ChickenBones");
        meta.description = "Base common code for all chickenbones mods.";
        meta.url         = "http://www.minecraftforum.net/topic/909223-";
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller)
	{
		bus.register(this);
		return true;		
	}
	
	@Subscribe
    public void init(FMLInitializationEvent event)
    {
    }
}
