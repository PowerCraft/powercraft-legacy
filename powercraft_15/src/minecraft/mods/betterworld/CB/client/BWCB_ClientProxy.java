package mods.betterworld.CB.client;

import net.minecraft.item.ItemStack;
import mods.betterworld.CB.BWCB;
import mods.betterworld.CB.core.BWCB_BlockList;
import mods.betterworld.CB.core.BWCB_Proxy;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class BWCB_ClientProxy extends BWCB_Proxy {

	@Override
	public void registerRendering() {
		RenderingRegistry.registerBlockHandler(BWCB_Render.getRender());
	}
	@Override
	public void registerLanguage() {
		
		LanguageRegistry.instance().addStringLocalization(
				"itemGroup.BW-Blocks", "en_US", "BW-Blocks");
		//stoneBlock
				for (int i = 0; i < BWCB_BlockList.blockStoneNormalName.size(); i++) 
				{
					ItemStack blockStoneStack = new ItemStack(BWCB.blockStone, 1, i);
					LanguageRegistry.addName(blockStoneStack, BWCB_BlockList.blockStoneNormalName.get(blockStoneStack.getItemDamage()).toString());
				}
				//stoneBlockR
				for (int i = 0; i < BWCB_BlockList.blockStoneResistentName.size(); i++) {
					ItemStack blockStoneRStack = new ItemStack(BWCB.blockStoneR, 1, i);
					LanguageRegistry.addName(blockStoneRStack,BWCB_BlockList.blockStoneResistentName.get(blockStoneRStack.getItemDamage()).toString());
				}
				// Wood
				for (int i = 0; i < BWCB_BlockList.blockPlanksNormalName.size(); i++) {
					ItemStack blockWoodStack = new ItemStack(BWCB.blockWood, 1, i);
					LanguageRegistry.addName(blockWoodStack,BWCB_BlockList.blockPlanksNormalName.get(blockWoodStack.getItemDamage()).toString());
				}
				//woodR
				for (int i = 0; i < BWCB_BlockList.blockPlanksResistentName.size(); i++) {
					ItemStack blockWoodRStack = new ItemStack(BWCB.blockWoodR, 1, i);
					LanguageRegistry.addName(blockWoodRStack,BWCB_BlockList.blockPlanksResistentName.get(blockWoodRStack.getItemDamage()).toString());
				}
				//glass
				for (int i = 0; i < BWCB_BlockList.blockGlassNormalName.size(); i++) {
					ItemStack blockGlassStack = new ItemStack(BWCB.blockGlass, 1, i);
					LanguageRegistry.addName(blockGlassStack,BWCB_BlockList.blockGlassNormalName.get(blockGlassStack.getItemDamage()).toString());
				}
				//glassR
				for (int i = 0; i < BWCB_BlockList.blockGlassResistentName.size(); i++) {
					ItemStack blockGlassRStack = new ItemStack(BWCB.blockGlassR, 1, i);
					LanguageRegistry.addName(blockGlassRStack,BWCB_BlockList.blockGlassResistentName.get(blockGlassRStack.getItemDamage()).toString());
				}
				//Stairs Stone
				for (int i = 0; i < BWCB_BlockList.blockStoneNormalName.size(); i++) {
					ItemStack blockStairsStoneStack = new ItemStack(BWCB.blockStairStone, 1, i);
					LanguageRegistry.addName(blockStairsStoneStack,BWCB_BlockList.blockStoneNormalName.get(blockStairsStoneStack.getItemDamage()).toString()+" Stair");
				}
				//StairsStoneR
				for (int i = 0; i < BWCB_BlockList.blockStoneResistentName.size(); i++) {
					ItemStack blockStairsStoneStack = new ItemStack(BWCB.blockStairStoneR, 1, i);
					LanguageRegistry.addName(blockStairsStoneStack,BWCB_BlockList.blockStoneResistentName.get(blockStairsStoneStack.getItemDamage()).toString()+" Stair");
				}
				// Wood
				for (int i = 0; i < BWCB_BlockList.blockPlanksNormalName.size(); i++) {
					ItemStack blockStairsWoodStack = new ItemStack(BWCB.blockStairWood, 1, i);LanguageRegistry.addName(blockStairsWoodStack,BWCB_BlockList.blockPlanksNormalName.get(blockStairsWoodStack.getItemDamage()).toString()+" Stair");
				}
				//WoodR
				for (int i = 0; i < BWCB_BlockList.blockPlanksResistentName.size(); i++) {
					ItemStack blockStairsWoodRStack = new ItemStack(BWCB.blockStairWoodR, 1, i);
					LanguageRegistry.addName(blockStairsWoodRStack,BWCB_BlockList.blockPlanksResistentName.get(blockStairsWoodRStack.getItemDamage()).toString()+" Stair");
				}
				// GLass
				for (int i = 0; i < BWCB_BlockList.blockGlassNormalName.size(); i++) {
					ItemStack blockStairsGlassStack = new ItemStack(BWCB.blockStairGlass, 1, i);
					LanguageRegistry.addName(blockStairsGlassStack,BWCB_BlockList.blockGlassNormalName.get(blockStairsGlassStack.getItemDamage()).toString()+" Stair");
				}
				//GlassR
				for (int i = 0; i < BWCB_BlockList.blockGlassResistentName.size(); i++) {
					ItemStack blockStairsGlassRStack = new ItemStack(BWCB.blockStairGlassR, 1, i);
					LanguageRegistry.addName(blockStairsGlassRStack,BWCB_BlockList.blockGlassResistentName.get(blockStairsGlassRStack.getItemDamage()).toString()+" Stair");
				}	
				
				// Slabs
				
				
				// Machnines
				LanguageRegistry.addName(BWCB.blockMachineBrick, "Base Bricks Machine");
				LanguageRegistry.addName(BWCB.blockMachineBrick, "Base Obsidian Machine");
				
				
		
	}

}
