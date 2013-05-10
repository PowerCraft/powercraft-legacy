package mods.betterworld.DeCB.core;


import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import mods.betterworld.CB.BWCB;
import mods.betterworld.CB.core.BWCB_BlockList;
import mods.betterworld.DeCB.BWDeCB;
import mods.betterworld.DeCB.items.BWDeCB_ItemBlockColumnSquare;
import mods.betterworld.DeCB.tileEntity.BWDeCB_TileEntityBlockColumnSquare;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class BWDeCB_Proxy {

	public void registerRendering() {
		// TODO Auto-generated method stub
		
	}

	public void registerBlocks() {
		
		GameRegistry.registerBlock(BWDeCB.blockFenceWood, mods.betterworld.DeCB.items.BWDeCB_ItemBlockFenceWood.class,"name_BlockFenceWood");
		GameRegistry.registerBlock(BWDeCB.blockFenceWoodGate, mods.betterworld.DeCB.items.BWDeCB_ItemBlockFenceWoodGate.class,"name_BlockFenceWoodGate");
		GameRegistry.registerBlock(BWDeCB.blockWallStone, mods.betterworld.DeCB.items.BWDeCB_ItemBlockWallStone.class,"name_BlockWallStone");
		
		GameRegistry.registerBlock(BWDeCB.blockColumnSquare, mods.betterworld.DeCB.items.BWDeCB_ItemBlockColumnSquare.class,"name_BlockColumnSquare");
		GameRegistry.registerBlock(BWDeCB.blockColumnRound, mods.betterworld.DeCB.items.BWDeCB_ItemBlockColumnRound.class,"name_BlockColumnRound");
		
	}

	public void registerTileEntity() {
		
		GameRegistry.registerTileEntity(mods.betterworld.DeCB.tileEntity.BWDeCB_TileEntityBlockFenceWood.class,"tile_BlockFenceWood");
		GameRegistry.registerTileEntity(mods.betterworld.DeCB.tileEntity.BWDeCB_TileEntityBlockFenceWood.class,"tile_BlockFenceWoodGate");
		GameRegistry.registerTileEntity(mods.betterworld.DeCB.tileEntity.BWDeCB_TileEntityBlockWallStone.class,"tile_BlockWallStone");
	
		GameRegistry.registerTileEntity(mods.betterworld.DeCB.tileEntity.BWDeCB_TileEntityBlockColumnSquare.class,"tile_BlockColumnSquare");
		GameRegistry.registerTileEntity(mods.betterworld.DeCB.tileEntity.BWDeCB_TileEntityBlockColumnRound.class,"tile_BlockColumnRound");
		
	}

	public void registerLanguageRecipe() {
		LanguageRegistry.instance().addStringLocalization(
				"itemGroup.BW-Deco", "en_US", "BW-Deco");
		//Fence Wood
		for (int i = 0; i < BWCB_BlockList.blockPlanksNormalName.size(); i++) {
			ItemStack blockFenceWoodStack = new ItemStack(BWDeCB.blockFenceWood, 4, i);
			ItemStack blockWoodStack = new ItemStack(BWCB.blockWood, 1, i);
			LanguageRegistry.addName(blockFenceWoodStack, BWCB_BlockList.blockPlanksNormalName.get(blockFenceWoodStack.getItemDamage()).toString()+" Fence");
			GameRegistry.addRecipe(blockFenceWoodStack, new Object[] {"   ", "yxy", "yxy",Character.valueOf('x'),Item.stick, Character.valueOf('y'),blockWoodStack});
		}

		//FenceGate wood
		for (int i = 0; i < BWCB_BlockList.blockPlanksNormalName.size(); i++) {
			ItemStack blockFenceWoodGateStack = new ItemStack(BWDeCB.blockFenceWoodGate, 1, i);
			ItemStack blockWoodStack = new ItemStack(BWCB.blockWood, 1, i);
			LanguageRegistry.addName(blockFenceWoodGateStack,BWCB_BlockList.blockPlanksNormalName.get(blockFenceWoodGateStack.getItemDamage()).toString()+" Fence Gate");
			GameRegistry.addRecipe(blockFenceWoodGateStack, new Object[] {"   ", "xyx", "xyx",Character.valueOf('x'),Item.stick, Character.valueOf('y'),blockWoodStack});
		}

		//Wall Stone
		for (int i = 0; i < BWCB_BlockList.blockStoneNormalName.size(); i++) {
			ItemStack blockWallStoneStack = new ItemStack(BWDeCB.blockWallStone, 6, i);
			ItemStack blockStoneStack = new ItemStack(BWCB.blockStone, 1, i);
			LanguageRegistry.addName(blockWallStoneStack,BWCB_BlockList.blockStoneNormalName.get(blockWallStoneStack.getItemDamage()).toString()+" Wall");
			GameRegistry.addRecipe(blockWallStoneStack, new Object[] {"xxx", "xxx", "   ", Character.valueOf('x'),blockStoneStack});
		}	
		//Column Square
		for (int i = 0; i < BWCB_BlockList.blockStoneNormalName.size(); i++) {
			ItemStack blockColumnStack = new ItemStack(BWDeCB.blockColumnSquare, 2, i);
			ItemStack blockStoneStack = new ItemStack(BWCB.blockStone, 1, i);
			LanguageRegistry.addName(blockColumnStack,BWCB_BlockList.blockStoneNormalName.get(blockColumnStack.getItemDamage()).toString()+" Column Square");
			GameRegistry.addRecipe(blockColumnStack, new Object[] {"xx ", "xx ", "xx ", Character.valueOf('x'),blockStoneStack});
		}	
		//Column Round
		for (int i = 0; i < BWCB_BlockList.blockStoneNormalName.size(); i++) {
			ItemStack blockColumnStack = new ItemStack(BWDeCB.blockColumnRound, 2, i);
			ItemStack blockStoneStack = new ItemStack(BWCB.blockStone, 1, i);
			LanguageRegistry.addName(blockColumnStack,BWCB_BlockList.blockStoneNormalName.get(blockColumnStack.getItemDamage()).toString()+" Column Round");
			GameRegistry.addRecipe(blockColumnStack, new Object[] {"xx ", "xx ", "xx ", Character.valueOf('x'),blockStoneStack});
		}	
	
	}

}
