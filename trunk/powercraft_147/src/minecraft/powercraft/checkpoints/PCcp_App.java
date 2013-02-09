package powercraft.checkpoints;

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
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.ModuleLoader;
import powercraft.management.recipes.PC_ShapedRecipes;

public class PCcp_App implements PC_IModule {

	public static PC_Block checkpoint;
	
	@Override
	public String getName() {
		return "Checkpoints";
	}

	@Override
	public String getVersion() {
		return "1.0.1";
	}

	@Override
	public void preInit() {}

	@Override
	public void init() {}

	@Override
	public void postInit() {}

	@Override
	public void initProperties(PC_Property config) {}

	@Override
	public void initBlocks() {
        checkpoint = (PC_Block)ModuleLoader.register(this, PCcp_BlockCheckpoint.class, PCcp_TileEntityCheckpoint.class);
	}

	@Override
	public void initItems() {}

	@Override
	public void initEntities() {}

	@Override
	public List<Object> initRecipes(List<Object> recipes) {
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(checkpoint), 
				"b",
				"e",
				'b', Block.stoneButton, Block.woodenButton, 'e', Item.bed));
		return recipes;
	}

	@Override
	public List<PC_Struct2<String, PC_IDataHandler>> initDataHandlers(
			List<PC_Struct2<String, PC_IDataHandler>> dataHandlers) {
		return null;
	}

	@Override
	public List<PC_IMSG> initMSGObjects(List<PC_IMSG> msgObjects) {
		return null;
	}

	@Override
	public List<PC_Struct2<String, PC_IPacketHandler>> initPacketHandlers(
			List<PC_Struct2<String, PC_IPacketHandler>> packetHandlers) {
		return null;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("Checkpoint", PCcp_ContainerCheckpoint.class));
		return guis;
	}

}
