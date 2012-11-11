package powercraft.light;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.Configuration;
import powercraft.core.PC_ItemStack;
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

@Mod(modid="PowerCraft-Light", name="PowerCraft-Light", version="3.5.0AlphaA", dependencies="required-after:PowerCraft-Core")
@NetworkMod(clientSideRequired=true, serverSideRequired=true)
public class mod_PowerCraftLight extends PC_Module {

	@SidedProxy(clientSide = "powercraft.light.PCli_ClientProxy", serverSide = "powercraft.light.PCli_CommonProxy")
	public static PCli_CommonProxy proxy;
	public static PCli_BlockLight light;
	
	public static mod_PowerCraftLight getInstance(){
		return (mod_PowerCraftLight)PC_Module.getModule("PowerCraft-Light");
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
		textures.add(getTextureDirectory()+"block_light.png");
		return textures;
	}

	@Override
	protected void initLanguage() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initBlocks() {
		light = PC_Utils.register(this, 484, PCli_BlockLight.class, PCli_TileEntityLight.class);
	}

	@Override
	protected void initItems() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initRecipes() {
		PC_Utils.addShapelessRecipe(
				new PC_ItemStack(light, 1, 0),
				new Object[] {
						Item.redstone, Block.glowStone});
	}

	@Override
	protected List<String> addSplashes(List<String> list) {
		// TODO Auto-generated method stub
		return null;
	}

}
