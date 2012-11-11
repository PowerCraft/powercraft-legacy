package powercraft.machines;

import java.util.List;

import net.minecraftforge.common.Configuration;
import powercraft.core.PC_Block;
import powercraft.core.PC_Module;
import powercraft.core.PC_Utils;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid="PowerCraft-Machines", name="PowerCraft-Machines", version="3.5.0AlphaA", dependencies="required-after:PowerCraft-Core")
@NetworkMod(clientSideRequired=true, serverSideRequired=true)
public class mod_PowerCraftMachines extends PC_Module {

	@SidedProxy(clientSide = "powercraft.machines.PCma_ClientProxy", serverSide = "powercraft.machines.PCma_CommonProxy")
	public static PCma_CommonProxy proxy;
	
	public static PC_Block automaticWorkbench;
	public static PC_Block roaster;
	public static PC_Block replacer;
	
	public static List<Integer >roasterIgnoreBlockIDs;
	
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
		roasterIgnoreBlockIDs = PC_Utils.parseIntList(PC_Utils.getConfigString(config, "roaster_ignored_blocks_list", Configuration.CATEGORY_GENERAL, "1"));
	}

	@Override
	protected List<String> loadTextureFiles(List<String> textures) {
		textures.add(getTerrainFile());
		return textures;
	}

	@Override
	protected void initLanguage() {
		PC_Utils.registerLanguage(this, 
				"pc.gui.automaticWorkbench.redstoneActivated", "Redstone triggered",
				"pc.roaster.insertFuel", "fuel"
				);
	}

	@Override
	protected void initBlocks() {
		automaticWorkbench = PC_Utils.register(this, 482, PCma_BlockAutomaticWorkbench.class, PCma_TileEntityAutomaticWorkbench.class);
		roaster = PC_Utils.register(this, 483, PCma_BlockRoaster.class, PCma_TileEntityRoaster.class);
		replacer = PC_Utils.register(this, 484, PCma_BlockReplacer.class, PCma_TileEntityReplacer.class);
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
