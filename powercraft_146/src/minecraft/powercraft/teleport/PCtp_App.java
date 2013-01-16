package powercraft.teleport;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import powercraft.management.PC_Block;
import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_IModule;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_Property;
import powercraft.management.PC_ShapedRecipes;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.ModuleLoader;

public class PCtp_App implements PC_IModule {

	public static PCtp_TeleporterManager teleporterManager = new PCtp_TeleporterManager();
	
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
	public void init() {}

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
	public void initItems() {}

	@Override
	public void initEntities() {}

	@Override
	public List<Object> initRecipes(List<Object> recipes) {
		PC_ItemStack prism;
		
		int prismId = ModuleInfo.getPCObjectIDByName("PCli_BlockPrism");
		
		if(prismId!=0){
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
	public List<PC_Struct2<String, PC_IDataHandler>> initDataHandlers(
			List<PC_Struct2<String, PC_IDataHandler>> dataHandlers) {
		dataHandlers.add(new PC_Struct2<String, PC_IDataHandler>("Teleporter", teleporterManager));
		return dataHandlers;
	}

	@Override
	public List<PC_IMSG> initMSGObjects(List<PC_IMSG> msgObjects) {
		return null;
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
