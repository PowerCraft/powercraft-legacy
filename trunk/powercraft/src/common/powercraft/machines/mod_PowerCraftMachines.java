package powercraft.machines;

import java.util.List;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

import net.minecraftforge.common.Configuration;
import powercraft.core.PC_Module;
import powercraft.transport.PCtr_CommonProxy;
import powercraft.transport.mod_PowerCraftTransport;

@Mod(modid="PowerCraft-Machines", name="PowerCraft-Machines", version="3.5.0AlphaA", dependencies="required-after:PowerCraft-Core")
@NetworkMod(clientSideRequired=true, serverSideRequired=true)
public class mod_PowerCraftMachines extends PC_Module {

	@SidedProxy(clientSide = "powercraft.machines.PCma_ClientProxy", serverSide = "powercraft.machines.PCma_CommonProxy")
	public static PCma_CommonProxy proxy;
	
	public static mod_PowerCraftMachines getInstance() {
		return (mod_PowerCraftMachines)PC_Module.getModule("PowerCraft-Machines");
	}
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event){
		
		preInit(event, proxy);
		
	}
	
	@Init
	public void init(FMLInitializationEvent event){
		
		init();
		
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event){
		
		postInit();
		
	}
	
	@Override
	protected void initProperties(Configuration config) {
		// TODO Auto-generated method stub

	}

	@Override
	protected List<String> loadTextureFiles(List<String> textures) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initLanguage() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initBlocks() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initItems() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initRecipes() {
		// TODO Auto-generated method stub

	}

	@Override
	protected List<String> addSplashes(List<String> list) {
		// TODO Auto-generated method stub
		return null;
	}

}
