package mods.betterworld.DeCB;

import java.io.IOException;
import java.util.logging.Logger;

import mods.betterworld.CB.BWCB;
import mods.betterworld.CB.core.BWCB_BlockList;
import mods.betterworld.CB.core.BWCB_Config;
import mods.betterworld.DeCB.blocks.BWDeCB_BlockColumnRound;
import mods.betterworld.DeCB.blocks.BWDeCB_BlockColumnSquare;
import mods.betterworld.DeCB.blocks.BWDeCB_BlockFenceWood;
import mods.betterworld.DeCB.blocks.BWDeCB_BlockFenceWoodGate;
import mods.betterworld.DeCB.blocks.BWDeCB_BlockWallStone;
import mods.betterworld.DeCB.core.BWDeCB_Config;
import mods.betterworld.DeCB.core.BWDeCB_NetworkHandler;
import mods.betterworld.DeCB.core.BWDeCB_Proxy;
import mods.betterworld.DeCB.core.CreativeTab;
import mods.betterworld.DeCB.items.BWDeCB_ItemBlockFenceWood;
import mods.betterworld.DeCB.items.BWDeCB_ItemBlockFenceWoodGate;
import mods.betterworld.DeCB.items.BWDeCB_ItemBlockWallStone;
import mods.betterworld.DeCB.tileEntity.BWDeCB_TileEntityBlockFenceWood;
import mods.betterworld.DeCB.tileEntity.BWDeCB_TileEntityBlockWallStone;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
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
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "BWDeCB", version = "4.0", dependencies = "required-after:BWCB")
@NetworkMod(channels = {"BWDeCB"}, packetHandler = BWDeCB_NetworkHandler.class, clientSideRequired = true)
public class BWDeCB {

	public static int textureRes = 128;

	public static BWCB_BlockList bList = new BWCB_BlockList();

	public static int blockFenceWoodID, blockFenceWoodGateID, blockWallStoneID;
	public static int blockColumnSquareID, blockColumnRoundID;
	public static Block	blockFenceWood, blockFenceWoodGate, blockWallStone;
	public static Block blockColumnSquare, blockColumnRound;
	
	public static int renderColumnSquareID, renderColumnRoundID;
	public static CreativeTab tabDeco;

	@Instance("BWDeCB")
	public static BWDeCB instance;

	@SidedProxy(clientSide = "mods.betterworld.DeCB.client.BWDeCB_ClientProxy", serverSide = "mods.betterworld.DeCB.core.BWDeCB_Proxy")
	public static BWDeCB_Proxy proxy;

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {

		BWDeCB_Config.init(event.getSuggestedConfigurationFile());
	}

	@Init
	public void init(FMLInitializationEvent event) {
		tabDeco = new CreativeTab();
		// item = new Item(Configs.itemId);

		// block = new Block(blockId);
		// GameRegistry.registerBlock(block);

		blockFenceWood = new BWDeCB_BlockFenceWood(blockFenceWoodID, null, null);
		blockFenceWoodGate = new BWDeCB_BlockFenceWoodGate(blockFenceWoodGateID);
		blockWallStone = new BWDeCB_BlockWallStone(blockWallStoneID, BWCB.blockStone);
		
		blockColumnSquare = new BWDeCB_BlockColumnSquare(blockColumnSquareID);
		blockColumnRound = new BWDeCB_BlockColumnRound(blockColumnRoundID);
		
		proxy.registerBlocks();
		proxy.registerTileEntity();
		proxy.registerLanguageRecipe();

		proxy.registerRendering();

	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event) {

	}
}
