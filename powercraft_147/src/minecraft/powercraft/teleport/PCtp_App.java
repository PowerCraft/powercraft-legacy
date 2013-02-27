package powercraft.teleport;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_IModule;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_Property;
import powercraft.management.PC_Struct2;
import powercraft.management.annotation.PC_FieldObject;
import powercraft.management.block.PC_Block;
import powercraft.management.item.PC_ItemStack;
import powercraft.management.recipes.PC_IRecipe;
import powercraft.management.recipes.PC_ShapedRecipes;
import powercraft.management.registry.PC_BlockRegistry;

public class PCtp_App implements PC_IModule {

	public static PCtp_TeleporterManager teleporterManager = new PCtp_TeleporterManager();
	
	@PC_FieldObject(clazz=PCtp_BlockTeleporter.class)
	public static PC_Block teleporter;
	
	@Override
	public String getName() {
		return "Teleport";
	}

	@Override
	public String getVersion() {
		return "1.0.3";
	}

	@Override
	public void preInit() {}

	@Override
	public void init() {}

	@Override
	public void postInit() {}

	@Override
	public void initProperties(PC_Property config) {
		
	}

	@Override
	public List<PC_Struct2<Class<? extends Entity>, Integer>> initEntities(List<PC_Struct2<Class<? extends Entity>, Integer>> entities) {
		return null;
	}

	@Override
	public List<PC_IRecipe> initRecipes(List<PC_IRecipe> recipes) {
		PC_ItemStack prism;
		
		Block bprism = PC_BlockRegistry.getPCBlockByName("PCli_BlockPrism");
		
		if(bprism==null){
			prism = new PC_ItemStack(bprism);
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
	public List<PC_Struct2<String, PC_IDataHandler>> initDataHandlers(
			List<PC_Struct2<String, PC_IDataHandler>> dataHandlers) {
		dataHandlers.add(new PC_Struct2<String, PC_IDataHandler>("Teleporter", teleporterManager));
		return dataHandlers;
	}

	@Override
	public List<PC_Struct2<String, PC_IPacketHandler>> initPacketHandlers(
			List<PC_Struct2<String, PC_IPacketHandler>> packetHandlers) {
		packetHandlers.add(new PC_Struct2<String, PC_IPacketHandler>("Teleporter", teleporterManager));
		return packetHandlers;
	}
	
	@Override
	public List<PC_Struct2<String, Class>> registerGuis(
			List<PC_Struct2<String, Class>> guis) {
		return null;
	}

}
