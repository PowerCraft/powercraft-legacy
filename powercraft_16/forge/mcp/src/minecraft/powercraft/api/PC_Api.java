package powercraft.api;


import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;


@Mod(modid = "PowerCraft-Api", name = "PowerCraft-Api", version = "1.0.0")
@NetworkMod(clientSideRequired = true, serverPacketHandlerSpec = @SidedPacketHandler(channels = { "PowerCraft" }, packetHandler = PC_PacketHandler.class), clientPacketHandlerSpec = @SidedPacketHandler(channels = { "PowerCraft" }, packetHandler = PC_PacketHandlerClient.class))
public class PC_Api extends PC_Module {

	@Instance(value = "PowerCraft-Api")
	public static PC_Api instance;

	@SidedProxy(clientSide = "powercraft.api.PC_ClientUtils", serverSide = "powercraft.api.PC_Utils")
	public static PC_Utils utils;


	@Override
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		PC_Logger.init(PC_Utils.getPowerCraftFile(null, "PowerCraft.log"));
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
