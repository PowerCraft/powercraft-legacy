package powercraft.transport;

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
import powercraft.core.PC_Block;
import powercraft.core.PC_Module;
import powercraft.core.PC_Utils;
import powercraft.logic.PClo_BlockPulsar;
import powercraft.logic.PClo_CommonProxy;
import powercraft.logic.PClo_TileEntityPulsar;
import powercraft.logic.mod_PowerCraftLogic;

@Mod(modid="PowerCraft-Transport", name="PowerCraft-Transport", version="0.0.1Alpha", dependencies="required-after:PowerCraft-Core")
@NetworkMod(clientSideRequired=true, serverSideRequired=true)
public class mod_PowerCraftTransport extends PC_Module {

	@SidedProxy(clientSide = "powercraft.transport.PCtr_ClientProxy", serverSide = "powercraft.transport.PCtr_CommonProxy")
	public static PCtr_CommonProxy proxy;
	public static PC_Block belt;
	public static PC_Block speedyBelt;
	public static PC_Block detectorBelt;
	public static PC_Block breakBelt;
	
	public static mod_PowerCraftTransport getInstance() {
		return (mod_PowerCraftTransport)PC_Module.getModule("PowerCraft-Transport");
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
		textures.add(getTerrainFile());
		return textures;
	}

	@Override
	protected void initLanguage() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initBlocks() {
		belt = (PC_Block)PC_Utils.register(this, 467, PCtr_BlockBeltNormal.class, PCtr_ItemBlockConveyor.class);
		speedyBelt = (PC_Block)PC_Utils.register(this, 468, PCtr_BlockBeltSpeedy.class, PCtr_ItemBlockConveyor.class);
		detectorBelt = (PC_Block)PC_Utils.register(this, 469, PCtr_BlockBeltDetector.class, PCtr_ItemBlockConveyor.class);
		breakBelt = (PC_Block)PC_Utils.register(this, 470, PCtr_BlockBeltBreak.class, PCtr_ItemBlockConveyor.class);
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