package powercraft.deco;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.IRecipe;
import net.minecraft.src.Item;
import powercraft.management.PC_Block;
import powercraft.management.PC_Configuration;
import powercraft.management.PC_IModule;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils;

public class PCde_App implements PC_IModule {

	public static PC_Block redstoneStorage;
	public static PC_Block ironFrame;
	public static PC_Block chimney;
	public static PC_Block platform;
	public static PC_Block stairs;

	@Override
	public String getName() {
		return "Deco";
	}

	@Override
	public String getVersion() {
		return "1.0AlphaA";
	}
	
    public void preInit(){}

    public void init(){}

    public void postInit(){}
	
	
	@Override
	public void initProperties(PC_Configuration config) {}

	@Override
	public void initBlocks() {
		/*redstoneStorage = PC_Utils.register(this, PCde_BlockRedstoneStorage.class);
		ironFrame = PC_Utils.register(this, PCde_BlockIronFrame.class, PCde_TileEntityIronFrame.class);
		chimney = PC_Utils.register(this, PCde_BlockChimney.class, PCde_ItemBlockChimney.class, PCde_TileEntityChimney.class);
		platform = PC_Utils.register(this, PCde_BlockPlatform.class, PCde_ItemBlockPlatform.class, PCde_TileEntityPlatform.class);
		stairs = PC_Utils.register(this, PCde_BlockStairs.class, PCde_ItemBlockStairs.class, PCde_TileEntityStairs.class);*/
	}

	@Override
	public void initItems() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<IRecipe> initRecipes(List<IRecipe> recipes) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*@Override
	protected void initRecipes() {
		
		PC_Utils.addRecipe(
				new PC_ItemStack(ironFrame, 32, 0),
				new Object[] { "XXX", "X X", "XXX",
					'X', Item.ingotIron });	
		
		PC_Utils.addRecipe(
				new PC_ItemStack(redstoneStorage, 1, 1),
				new Object[] { "XXX", "XXX", "XXX",
					'X', Item.redstone });
		
		PC_Utils.addShapelessRecipe(
				new PC_ItemStack(Item.redstone, 9, 0),
				new Object[] { new PC_ItemStack(redstoneStorage) });
		
		
		PC_Utils.addRecipe(
				new PC_ItemStack(platform, 15),
				new Object[] { "X  ", "X  ", "XXX",
					'X', Item.ingotIron });	
		
		PC_Utils.addRecipe(
				new PC_ItemStack(stairs, 15),
				new Object[] { "X  ", "XX ", " XX",
					'X', Item.ingotIron });			

		PC_Utils.addShapelessRecipe(
				new PC_ItemStack(Item.ingotIron),
				new Object[] {new PC_ItemStack(platform, 1),new PC_ItemStack(platform, 1),new PC_ItemStack(platform, 1)});
		
		PC_Utils.addShapelessRecipe(
				new PC_ItemStack(Item.ingotIron),
				new Object[] {new PC_ItemStack(stairs, 1),new PC_ItemStack(stairs, 1),new PC_ItemStack(stairs, 1)});
		
		// chimneys
		PC_Utils.addRecipe(new PC_ItemStack(chimney,6,0),new Object[] {"X X", "X X", "X X", Character.valueOf('X'), Block.cobblestone});
		PC_Utils.addRecipe(new PC_ItemStack(chimney,6,1),new Object[] {"X X", "X X", "X X", Character.valueOf('X'), Block.brick});
		PC_Utils.addRecipe(new PC_ItemStack(chimney,6,2),new Object[] {"X X", "X X", "X X", Character.valueOf('X'), Block.stoneBrick});
		
	}*/

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		// TODO Auto-generated method stub
		return null;
	}

}
