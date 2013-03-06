package powercraft.teleport;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import powercraft.launcher.PC_Module;
import powercraft.launcher.PC_Module.PC_InitDataHandlers;
import powercraft.launcher.PC_Module.PC_InitPacketHandlers;
import powercraft.launcher.PC_Module.PC_InitRecipes;
import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_Struct2;
import powercraft.management.annotation.PC_FieldObject;
import powercraft.management.block.PC_Block;
import powercraft.management.item.PC_ItemStack;
import powercraft.management.recipes.PC_IRecipe;
import powercraft.management.recipes.PC_ShapedRecipes;
import powercraft.management.registry.PC_BlockRegistry;

@PC_Module(name="Teleport", version="1.1.0")
public class PCtp_App {

	public static PCtp_TeleporterManager teleporterManager = new PCtp_TeleporterManager();
	
	@PC_FieldObject(clazz=PCtp_BlockTeleporter.class)
	public static PC_Block teleporter;

	@PC_InitRecipes
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

	@PC_InitDataHandlers
	public List<PC_Struct2<String, PC_IDataHandler>> initDataHandlers(
			List<PC_Struct2<String, PC_IDataHandler>> dataHandlers) {
		dataHandlers.add(new PC_Struct2<String, PC_IDataHandler>("Teleporter", teleporterManager));
		return dataHandlers;
	}

	@PC_InitPacketHandlers
	public List<PC_Struct2<String, PC_IPacketHandler>> initPacketHandlers(
			List<PC_Struct2<String, PC_IPacketHandler>> packetHandlers) {
		packetHandlers.add(new PC_Struct2<String, PC_IPacketHandler>("Teleporter", teleporterManager));
		return packetHandlers;
	}

}
