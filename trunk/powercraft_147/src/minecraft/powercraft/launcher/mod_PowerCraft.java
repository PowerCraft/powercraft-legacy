package powercraft.launcher;

import java.util.List;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;

@Mod(modid = "PowerCraft", name = "PowerCraft", version = "3.5.0AlphaJ", dependencies = "after:*")
@NetworkMod(clientSideRequired = true, serverSideRequired = true, clientPacketHandlerSpec = @SidedPacketHandler(channels = { "PowerCraft" }, packetHandler = PC_PacketHandler.class), serverPacketHandlerSpec = @SidedPacketHandler(channels = { "PowerCraft" }, packetHandler = PC_PacketHandler.class))
public class mod_PowerCraft{
	
	@SidedProxy(clientSide = "powercraft.launcher.PC_LauncherClientUtils", serverSide = "powercraft.launcher.PC_LauncherUtils")
	public static PC_LauncherUtils proxy;

	private static mod_PowerCraft instance;
	
	public static mod_PowerCraft getInstance(){
		return instance;
	}
	
	public mod_PowerCraft(){
		instance = this;
	}
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		PC_Launcher.preInit();
	}

	@Init
	public void init(FMLInitializationEvent event) {
		PC_Launcher.init();
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
		PC_Launcher.postInit();
	}

 	public ModContainer getModContainer()
    {
        List<ModContainer> modContainers = Loader.instance().getModList();

        for (ModContainer modContainer: modContainers)
        {
            if (modContainer.matches(this))
            {
                return modContainer;
            }
        }

        return null;
    }

    public ModMetadata getModMetadata()
    {
        return getModContainer().getMetadata();
    }

	public String getVersion() {
		return getModMetadata().version;
	}
	
}
