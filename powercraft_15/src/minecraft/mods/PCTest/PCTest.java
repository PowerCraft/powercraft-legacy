package mods.PCTest;

import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.spi.RegisterableService;



import mods.betterworld.DeCB.BWDeCB;
import mods.betterworld.DeCB.client.BWDeCB_RendererColumnRound;
import mods.betterworld.DeCB.client.BWDeCB_RendererColumnSquare;
import mods.betterworld.DeCB.tileEntity.BWDeCB_TileEntityBlockColumnRound;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
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
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "PCTest", version = "4.0")
@NetworkMod(channels = {"PCTest"}, packetHandler = PCNetworkHandler.class, clientSideRequired = true)
public class PCTest {

	public static int textureRes = 128;



	public static int blockWireID, itemBlockWireID;
	public static Block blockWire;
	public static Item itemBlockWire;
	public static int renderWireID;


	@Instance("PCTest")
	public static PCTest instance;

	@SidedProxy(clientSide = "mods.PCTest.PCClientProxy", serverSide = "mods.PCTest.PCProxy")
	public static PCProxy proxy;

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {

		PCConfig.init(event.getSuggestedConfigurationFile());
	}

	@Init
	public void init(FMLInitializationEvent event) {
		// item = new Item(Configs.itemId);

		// block = new Block(blockId);
		// GameRegistry.registerBlock(block);


		blockWire = new PCBlockWire(blockWireID);

		
		GameRegistry.registerBlock(blockWire, PCItemBlockWire.class, "name_Wire");
		GameRegistry.registerTileEntity(PCTileEntityBlockWire.class, "tile_wire");
        
		
		

		ClientRegistry.bindTileEntitySpecialRenderer(PCTileEntityBlockWire.class, new PCRenderBlockWire());
		PCTest.renderWireID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new PCRenderBlockWire());

		

		
	/*	
		proxy.registerBlocks();
		proxy.registerTileEntity();
		proxy.registerLanguageRecipe();

		proxy.registerRendering();
*/

	}
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
    }
	@PostInit
	public void postInit(FMLPostInitializationEvent event) {

	}
}
