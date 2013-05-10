package mods.betterworld.CB.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import mods.betterworld.CB.BWCB;
import mods.betterworld.CB.BWCB_TileEntityBlockMachineBrick;
import mods.betterworld.CB.BWCB_TileEntityBlockMachineObsidian;
import mods.betterworld.CB.item.BWCB_ItemBlockStairsStone;
import mods.betterworld.CB.item.BWCB_ItemBlockStairsStoneR;
import mods.betterworld.CB.item.BWCB_ItemBlockStairsWood;
import mods.betterworld.CB.item.BWCB_ItemBlockStairsWoodR;
import mods.betterworld.CB.item.BWCB_ItemBlockStairsGlass;
import mods.betterworld.CB.item.BWCB_ItemBlockStairsGlassR;

import mods.betterworld.CB.item.BWCB_ItemBlockStone;
import mods.betterworld.CB.item.BWCB_ItemBlockStoneR;
import mods.betterworld.CB.item.BWCB_ItemBlockWood;
import mods.betterworld.CB.item.BWCB_ItemBlockWoodR;
import mods.betterworld.CB.item.BWCB_ItemBlockGlass;
import mods.betterworld.CB.item.BWCB_ItemBlockGlassR;
import mods.betterworld.CB.tileEntity.BWCB_TileEntityBlockGlass;
import mods.betterworld.CB.tileEntity.BWCB_TileEntityBlockGlassR;
import mods.betterworld.CB.tileEntity.BWCB_TileEntityBlockStairsGlass;
import mods.betterworld.CB.tileEntity.BWCB_TileEntityBlockStairsStone;
import mods.betterworld.CB.tileEntity.BWCB_TileEntityBlockStairsWood;
import mods.betterworld.CB.tileEntity.BWCB_TileEntityBlockStone;
import mods.betterworld.CB.tileEntity.BWCB_TileEntityBlockStoneR;
import mods.betterworld.CB.tileEntity.BWCB_TileEntityBlockWood;
import mods.betterworld.CB.tileEntity.BWCB_TileEntityBlockWoodR;
import cpw.mods.fml.common.registry.GameRegistry;

public class BWCB_Proxy{

	public void registerRendering() {

	}
	public void registerTileEntity()
	{
		// Blocks
		GameRegistry.registerTileEntity(BWCB_TileEntityBlockStone.class,"tile_BlockStone");
		GameRegistry.registerTileEntity(BWCB_TileEntityBlockStoneR.class,"tile_BlockStoneR");
		GameRegistry.registerTileEntity(BWCB_TileEntityBlockWood.class,"tile_BlockWood");
		GameRegistry.registerTileEntity(BWCB_TileEntityBlockWoodR.class,"tile_BlockWoodR");
		GameRegistry.registerTileEntity(BWCB_TileEntityBlockGlass.class,"tile_BlockGlass");
		GameRegistry.registerTileEntity(BWCB_TileEntityBlockGlassR.class,"tile_BlockGlassR");
		// Stairs
		GameRegistry.registerTileEntity(BWCB_TileEntityBlockStairsStone.class,"tile_BlockStairsStone");
		GameRegistry.registerTileEntity(BWCB_TileEntityBlockStairsStone.class,"tile_BlockStairsStoneR");
		GameRegistry.registerTileEntity(BWCB_TileEntityBlockStairsWood.class,"tile_BlockStairsWood");
		GameRegistry.registerTileEntity(BWCB_TileEntityBlockStairsWood.class,"tile_BlockStairsWoodR");
		GameRegistry.registerTileEntity(BWCB_TileEntityBlockStairsGlass.class,"tile_BlockStairsGlass");
		GameRegistry.registerTileEntity(BWCB_TileEntityBlockStairsGlass.class,"tile_BlockStairsGlassR");
		//Slabs
		
		//Machines
		GameRegistry.registerTileEntity(BWCB_TileEntityBlockMachineBrick.class, "tileBlockMachineBrick");
		GameRegistry.registerTileEntity(BWCB_TileEntityBlockMachineObsidian.class, "tileBlockMachineObsidian");
		
	}

	public void registerBlocks() 
	{
		// Blocks
		GameRegistry.registerBlock(BWCB.blockStone, BWCB_ItemBlockStone.class,"name_BlockStone");
		GameRegistry.registerBlock(BWCB.blockStoneR, BWCB_ItemBlockStoneR.class,"name_BlockStoneR");
		GameRegistry.registerBlock(BWCB.blockWood, BWCB_ItemBlockWood.class,"name_BlockWood");
		GameRegistry.registerBlock(BWCB.blockWoodR, BWCB_ItemBlockWoodR.class,"name_BlockWoodR");
		GameRegistry.registerBlock(BWCB.blockGlass, BWCB_ItemBlockGlass.class,"name_BlockGlass");
		GameRegistry.registerBlock(BWCB.blockGlassR, BWCB_ItemBlockGlassR.class,"name_BlockGlassR");
		// Stairs
		GameRegistry.registerBlock(BWCB.blockStairStone, BWCB_ItemBlockStairsStone.class,"name_BlockStairsStone");
		GameRegistry.registerBlock(BWCB.blockStairStoneR, BWCB_ItemBlockStairsStoneR.class,"name_BlockStairsStoneR");
		GameRegistry.registerBlock(BWCB.blockStairWood, BWCB_ItemBlockStairsWood.class,"name_BlockStairsWood");
		GameRegistry.registerBlock(BWCB.blockStairWoodR, BWCB_ItemBlockStairsWoodR.class,"name_BlockStairsWoodR");
		GameRegistry.registerBlock(BWCB.blockStairGlass, BWCB_ItemBlockStairsGlass.class,"name_BlockStairsGlass");
		GameRegistry.registerBlock(BWCB.blockStairGlassR, BWCB_ItemBlockStairsGlassR.class,"name_BlockStairsGlassR");
		//Slabs
		
		// Machines
		GameRegistry.registerBlock(BWCB.blockMachineBrick, "name_MachineBrick");
		GameRegistry.registerBlock(BWCB.blockMachineObsidian, "name_MachineObsidian");

	}
	public void registerLanguage() {}
	
	public World getClientWorld() {
		return null;
	}

}
