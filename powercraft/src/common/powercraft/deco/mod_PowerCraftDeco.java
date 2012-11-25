package powercraft.deco;

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
import powercraft.logic.PClo_CommonProxy;
import powercraft.logic.mod_PowerCraftLogic;

@Mod(modid = "PowerCraft-Deco", name = "PowerCraft-Deco", version = "3.5.0AlphaD", dependencies = "required-after:PowerCraft-Core")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class mod_PowerCraftDeco extends PC_Module {
	
	@SidedProxy(clientSide = "powercraft.deco.PCde_ClientProxy", serverSide = "powercraft.deco.PCde_CommonProxy")
	public static PCde_CommonProxy proxy;

	public static PC_Block redstoneStorage;
	public static PC_Block ironFrame;
	public static PC_Block chimney;

    public static mod_PowerCraftDeco getInstance()
    {
        return (mod_PowerCraftDeco)PC_Module.getModule("PowerCraft-Deco");
    }

    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        preInit(event, proxy);
    }

    @Init
    public void init(FMLInitializationEvent event)
    {
        init();
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
        postInit();
    }
	
	
	@Override
	protected void initProperties(Configuration config) {
		// TODO Auto-generated method stub

	}

	@Override
	protected List<String> loadTextureFiles(List<String> textures) {
		textures.add(getTerrainFile());
		textures.add(getTextureDirectory()+"block_deco.png");
		return textures;
	}

	@Override
	protected void initLanguage() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initBlocks() {
		redstoneStorage = PC_Utils.register(this, 516, PCde_BlockRedstoneStorage.class);
		ironFrame = PC_Utils.register(this, 517, PCde_BlockIronFrame.class, PCde_TileEntityIronFrame.class);
		chimney = PC_Utils.register(this, 518, PCde_BlockChimney.class, PCde_ItemBlockChimney.class, PCde_TileEntityChimney.class);
	}

	@Override
	protected void initItems() {
		// TODO Auto-generated method stub

	}

	@Override
	protected List<String> addSplashes(List<String> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initRecipes() {
		// TODO Auto-generated method stub

	}

}
