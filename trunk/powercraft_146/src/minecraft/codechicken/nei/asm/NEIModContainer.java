package codechicken.nei.asm;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import codechicken.core.ClientUtils;
import codechicken.nei.ClientHandler;
import codechicken.nei.ServerHandler;
import codechicken.nei.api.IConfigureNEI;
import codechicken.packager.Packager;
import codechicken.packager.SrcPackager;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;


import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.versioning.ArtifactVersion;

@SrcPackager(getName="NotEnoughItems", getClasses = {""}, getMappedDirectories = {"NEI"})
@Packager(getName = "NotEnoughItems", getClasses = {""}, getBaseDirectories = {"NEI"}, getVersion = "1.4.5.1")
public class NEIModContainer extends DummyModContainer
{    
    public static LinkedList<IConfigureNEI> plugins = new LinkedList<IConfigureNEI>();
    
	public NEIModContainer()
    {
        super(new ModMetadata());
        getMetadata();
    }
	
	@Override
	public List<ArtifactVersion> getDependencies()
	{
		return super.getDependencies();
	}
	
	@Override
	public ModMetadata getMetadata()
	{
		ModMetadata meta = super.getMetadata();

        meta.modId       = "NotEnoughItems";
        meta.name        = "Not Enough Items";
        meta.version     = NEIModContainer.class.getAnnotation(Packager.class).getVersion();
        meta.authorList  = Arrays.asList("ChickenBones");
        meta.description = "Recipe Viewer, Inventory Manager, Item Spawner, Cheats and more.\n\247f\n";
        meta.url         = "http://www.minecraftforum.net/topic/909223-";
        meta.credits     = "Alexandria";
        
        if(plugins.size() == 0)
        {
        	meta.description += "\247cNo installed plugins.";
        }
        else
        {
        	meta.description += "\247aInstalled plugins: ";
        	for(int i = 0; i < plugins.size(); i++)
        	{
        		if(i > 0)
            		meta.description += ", ";
        		IConfigureNEI plugin = plugins.get(i);
        		meta.description += plugin.getName()+" "+plugin.getVersion();
        	}
        	meta.description += ".";
        }
        
        return meta;
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
		if(ClientUtils.isClient())
        	ClientHandler.load();
	    
        ServerHandler.load();
    }
}
