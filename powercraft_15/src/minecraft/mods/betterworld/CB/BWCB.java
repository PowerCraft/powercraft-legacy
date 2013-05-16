package mods.betterworld.CB;

import java.io.IOException;
import java.util.logging.Logger;

import mods.betterworld.CB.block.BWCB_BlockGlass;
import mods.betterworld.CB.block.BWCB_BlockGlassR;
import mods.betterworld.CB.block.BWCB_BlockStairsGlass;
import mods.betterworld.CB.block.BWCB_BlockStairsGlassR;
import mods.betterworld.CB.block.BWCB_BlockStairsStone;
import mods.betterworld.CB.block.BWCB_BlockStairsStoneR;
import mods.betterworld.CB.block.BWCB_BlockStairsWood;
import mods.betterworld.CB.block.BWCB_BlockStairsWoodR;
import mods.betterworld.CB.block.BWCB_BlockStone;
import mods.betterworld.CB.block.BWCB_BlockStoneR;
import mods.betterworld.CB.block.BWCB_BlockWood;
import mods.betterworld.CB.block.BWCB_BlockWoodR;
import mods.betterworld.CB.core.BWCB_BlockList;
import mods.betterworld.CB.core.BWCB_Proxy;
import mods.betterworld.CB.core.BWCB_Config;
import mods.betterworld.CB.core.BWCB_NetworkHandler;
import mods.betterworld.CB.core.BW_BlockX;
import mods.betterworld.CB.core.BW_ItemX;
import mods.betterworld.CB.core.BW_TileEntityBlockX;
import mods.betterworld.CB.core.CreativeTab;
import mods.betterworld.CB.item.BWCB_ItemBlockGlass;
import mods.betterworld.CB.item.BWCB_ItemBlockGlassR;
import mods.betterworld.CB.item.BWCB_ItemBlockStairsGlass;
import mods.betterworld.CB.item.BWCB_ItemBlockStairsGlassR;
import mods.betterworld.CB.item.BWCB_ItemBlockStairsStone;
import mods.betterworld.CB.item.BWCB_ItemBlockStairsStoneR;
import mods.betterworld.CB.item.BWCB_ItemBlockStairsWood;
import mods.betterworld.CB.item.BWCB_ItemBlockStairsWoodR;
import mods.betterworld.CB.item.BWCB_ItemBlockStone;
import mods.betterworld.CB.item.BWCB_ItemBlockStoneR;
import mods.betterworld.CB.item.BWCB_ItemBlockWood;
import mods.betterworld.CB.item.BWCB_ItemBlockWoodR;
import mods.betterworld.CB.tileEntity.BWCB_TileEntityBlockGlass;
import mods.betterworld.CB.tileEntity.BWCB_TileEntityBlockGlassR;
import mods.betterworld.CB.tileEntity.BWCB_TileEntityBlockStairsGlass;
import mods.betterworld.CB.tileEntity.BWCB_TileEntityBlockStairsStone;
import mods.betterworld.CB.tileEntity.BWCB_TileEntityBlockStairsWood;
import mods.betterworld.CB.tileEntity.BWCB_TileEntityBlockStone;
import mods.betterworld.CB.tileEntity.BWCB_TileEntityBlockStoneR;
import mods.betterworld.CB.tileEntity.BWCB_TileEntityBlockWood;
import mods.betterworld.CB.tileEntity.BWCB_TileEntityBlockWoodR;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "BWCB", version = "4.0")
@NetworkMod(channels = {"BWCB"}, packetHandler = BWCB_NetworkHandler.class, clientSideRequired = true)
public class BWCB {

	public static final String VERSION = "4.0";
	public static int textureRes = 128;

	public static BWCB_BlockList bList = new BWCB_BlockList();
// Blocks
	public static  int blockStoneID, blockStoneRID, blockWoodID, blockWoodRID, blockGlassID, blockGlassRID;
	public static  Block blockStone, blockStoneR, blockWood, blockWoodR, blockGlass, blockGlassR;
// Stair Blocks
	public static int blockStairStoneID, blockStairStoneRID, blockStairWoodID, blockStairWoodRID, blockStairGlassID, blockStairGlassRID;
	public static Block blockStairStone, blockStairStoneR, blockStairWood, blockStairWoodR, blockStairGlass, blockStairGlassR;
// SlabBlocks
	public static int  blockSlabStoneID, blockDoubleSlabStoneID, blockSlabStoneRID, blockDoubleSlabStoneRID, blockSlabWoodID, blockDoubleSlabWoodID, blockSlabWoodRID, blockDoubleSlabWoodRID, blockSlabGlassID, blockDoubleSlabGlassID, blockSlabGlassRID, blockDoubleSlabGlassRID;
	public static Block  blockSlabStone, blockDoubleSlabStone, blockSlabStoneR, blockDoubleSlabStoneR, blockSlabWood, blockDoubleSlabWood, blockSlabWoodR, blockDoubleSlabWoodR, blockSlabGlass, blockDoubleSlabGlass, blockSlabGlassR, blockDoubleSlabGlassR;

	public static int blockMachineBrickID, blockMachineWoodID, blockMachineObsidianID;
	public static Block blockMachineBrick, blockMachineWood, blockMachineObsidian;
	
	public static final int blockDoubleSlabsId = 1050;
	public static final Block blockDoubleSlabs = new BWCB_BlockSlabs(blockDoubleSlabsId,true,1051);
	
	public static final int blockSingleSlabsId = 1051;
	public static final Block blockSingleSlabs = new BWCB_BlockSlabs(blockSingleSlabsId,false,1050);

	public static CreativeTab tab;
	
	@Instance("BWCB")
	public static BWCB instance;

	@SidedProxy(clientSide = "mods.betterworld.CB.client.BWCB_ClientProxy", serverSide = "mods.betterworld.CB.core.BWCB_Proxy")
	public static BWCB_Proxy proxy;



	@PreInit
	public void preInit(FMLPreInitializationEvent event) {

		NetworkRegistry.instance().registerGuiHandler(this, new BWCB_GuiHandler());
		BWCB_Config.init(event.getSuggestedConfigurationFile());

		try {
			bList.readConfFile();

		} catch (IOException e) {
			System.out.println("BW_CB IOEXCEPTION");
			e.printStackTrace();
		}
		bList.sortArrays();
	}

	@Init
	public void init(FMLInitializationEvent event) {
		tab = new CreativeTab();
		
		//Block Instances
		blockStone = new mods.betterworld.CB.block.BWCB_BlockStone(blockStoneID);
		blockStoneR = new mods.betterworld.CB.block.BWCB_BlockStoneR(blockStoneRID);
		blockWood = new mods.betterworld.CB.block.BWCB_BlockWood(blockWoodID);
		blockWoodR = new mods.betterworld.CB.block.BWCB_BlockWoodR(blockWoodRID);
		blockGlass = new mods.betterworld.CB.block.BWCB_BlockGlass(blockGlassID);
		blockGlassR = new mods.betterworld.CB.block.BWCB_BlockGlassR(blockGlassRID);
		
		blockStairStone = new mods.betterworld.CB.block.BWCB_BlockStairsStone(blockStairStoneID, blockStone, 0);
		blockStairStoneR = new mods.betterworld.CB.block.BWCB_BlockStairsStoneR(blockStairStoneRID, blockStoneR, 0);
		blockStairWood = new mods.betterworld.CB.block.BWCB_BlockStairsWood(blockStairWoodID, blockWood, 0);
		blockStairWoodR = new mods.betterworld.CB.block.BWCB_BlockStairsWoodR(blockStairWoodRID, blockWoodR, 0);
		blockStairGlass = new mods.betterworld.CB.block.BWCB_BlockStairsGlass(blockStairGlassID, blockGlass, 0);
		blockStairGlassR = new mods.betterworld.CB.block.BWCB_BlockStairsGlassR(blockStairGlassRID, blockGlassR, 0);	
		//Slabs
		
		//Machines
		blockMachineBrick = new mods.betterworld.CB.BWCB_MachineBrick(blockMachineBrickID, Material.rock);
		blockMachineObsidian = new mods.betterworld.CB.BWCB_MachineObsidian(blockMachineObsidianID, Material.rock);
		
		// Block Register
		proxy.registerBlocks();
		proxy.registerTileEntity();
		proxy.registerLanguage();
		

		//wenn renderer Fertig umbenennen und kopieren

		GameRegistry.registerBlock(blockDoubleSlabs, BWCB_ItemBlockSlabs.class, "BlockDSlabs-Name");
		GameRegistry.registerTileEntity(BWCB_TileEntityBlockSlabs.class, "tile_BlockDSlabs");
		
		GameRegistry.registerBlock(blockSingleSlabs, BWCB_ItemBlockSlabs.class, "BlockSlabs-Name");
		GameRegistry.registerTileEntity(BWCB_TileEntityBlockSlabs.class, "tile_BlockSlabs");
		for (int i = 0; i < BWCB_BlockList.blockStoneNormalName.size(); i++) {
			ItemStack blockSlabsStack = new ItemStack(blockSingleSlabs, 1, i);
			LanguageRegistry.addName(blockSlabsStack,"Slab "+ BWCB_BlockList.blockStoneNormalName.get(blockSlabsStack.getItemDamage()).toString());
		}

		proxy.registerRendering();

	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event) {

	}
}
