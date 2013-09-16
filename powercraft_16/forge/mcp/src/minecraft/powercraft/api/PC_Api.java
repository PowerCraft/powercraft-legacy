package powercraft.api;


import powercraft.core.PCco_ModuleCore;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;


@Mod(modid = PC_Api.NAME, name = PC_Api.NAME, version = PC_Api.VERSION, dependencies=PC_Api.DEPENDENCIES)
@NetworkMod(clientSideRequired = true, serverPacketHandlerSpec = @SidedPacketHandler(channels = { PC_Module.POWERCRAFT }, packetHandler = PC_PacketHandler.class), clientPacketHandlerSpec = @SidedPacketHandler(channels = { PC_Module.POWERCRAFT }, packetHandler = PC_PacketHandlerClient.class))
public class PC_Api extends PC_Module {

	public static final String NAME = POWERCRAFT+"-Api";
	public static final String VERSION = "1.0.0";
	public static final String DEPENDENCIES = "required-before:"+PCco_ModuleCore.NAME+"@"+PCco_ModuleCore.VERSION;
	
	@Instance(NAME)
	public static PC_Api instance;

	@SidedProxy(clientSide = "powercraft.api.PC_ClientUtils", serverSide = "powercraft.api.PC_Utils")
	public static PC_Utils utils;
	
	@Override
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		PC_Logger.init(PC_Utils.getPowerCraftFile(null, POWERCRAFT+".log"));
		defaultPreInit(event);
	}


	@Override
	@EventHandler
	public void init(FMLInitializationEvent event) {

		defaultInit(event);
	}


	@Override
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

}
