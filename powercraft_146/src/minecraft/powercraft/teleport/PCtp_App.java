package powercraft.teleport;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import powercraft.management.PC_Block;
import powercraft.management.PC_IModule;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Property;
import powercraft.management.PC_ShapedRecipes;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.ModuleLoader;

public class PCtp_App implements PC_IModule {

	public static PC_Block teleporter;
	
	@Override
	public String getName() {
		return "Teleport";
	}

	@Override
	public String getVersion() {
		return "1.0.2";
	}

	@Override
	public void preInit() {}

	@Override
	public void init() {
		PCtp_TeleporterManager tm = new PCtp_TeleporterManager();
		ModuleLoader.regsterDataHandler("Teleporter", tm);
		PC_PacketHandler.registerPackethandler("Teleporter", tm);
	}

	@Override
	public void postInit() {}

	@Override
	public void initProperties(PC_Property config) {
		
	}

	@Override
	public void initBlocks() {
		teleporter = ModuleLoader.register(this, PCtp_BlockTeleporter.class, PCtp_TileEntityTeleporter.class);
	}

	@Override
	public void initItems() {
		
	}

	@Override
	public List<IRecipe> initRecipes(List<IRecipe> recipes) {
		PC_ItemStack prism;
		
		int prismId = ModuleInfo.getPCObjectIDByName("PCli_BlockPrism");
		
		if(prismId!=0){
			//safety check
			prism = new PC_ItemStack(Item.itemsList[prismId]);
		}else{
			prism = new PC_ItemStack(Block.glass);
		}
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(teleporter),
					" P ", 
					"PVP", 
					"SSS",
						'V', new PC_ItemStack(Item.dyePowder, 1, 5), 'P', prism, 'S', Item.ingotIron ));
		return recipes;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(
			List<PC_Struct2<String, Class>> guis) {
		// TODO Auto-generated method stub
		return null;
	}

}
