package powercraft.machines;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.annotation.PC_BlockInfo;
import powercraft.management.recipes.PC_3DRecipe;
import powercraft.management.recipes.PC_I3DRecipeHandler;
import powercraft.management.PC_VecI;

@PC_BlockInfo(tileEntity=PCma_TileEntityFishingMachine.class)
public class PCma_BlockFishingMachine extends PC_Block implements PC_I3DRecipeHandler {

	private static PC_3DRecipe struct;
	
	public PCma_BlockFishingMachine(int id) {
		super(id, 22, Material.iron, false);
		struct = new PC_3DRecipe(null, 
				new String[]{
				"www",
				"www",
				"www"},
				new String[]{
				"www",
				"www",
				"www"},
				new String[]{
				"www",
				"www",
				"www"},
				new String[]{
				"www",
				"www",
				"www"}, 
				new String[]{
				"www",
				"www",
				"www"},
				new String[]{
				"fpf",
				"pmp",
				"fpf"},
				new String[]{
				" !c ",
				"!cc!c",
				" !c "},
				'w', Block.waterMoving, Block.waterStill, 'f', Block.fence, 'p', Block.planks, 'm', this, 'c', Block.chest);
		
	}
	
	@Override
	public TileEntity newTileEntity(World world, int metadata) {
		return new PCma_TileEntityFishingMachine();
	}

	/*@Override
	public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        switch (par2)
        {
            case 1:
                return 198;
            case 2:
                return 214;
            case 3:
                return 199;
            default:
                return 4;
        }
    }*/
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3) {
		return Block.blockSteel.blockID;
	}

	@Override
	public int damageDropped(int par1)
    {
        return par1;
    }
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	public static boolean isStructOK(World world, PC_VecI pos){
		return struct.getStructRotation(world, pos.offset(-1, -5, -1))!=-1;
	}
	
	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
		if(GameInfo.<PCma_TileEntityFishingMachine>getTE(world, x, y, z).isRunning()){
			// bubbles
			for (int i = 0; i < 2; i++) {
				double buX = x + 0.2D + rand.nextFloat() * 0.6F;
				double buY = y - 4.8D + rand.nextFloat() * 0.6F;
				double buZ = z + 0.2D + rand.nextFloat() * 0.6F;
				world.spawnParticle("bubble", buX, buY, buZ, 0, 0.01F, 0);
			}
	
			// splash sound
			if (rand.nextInt(20) == 0 && GameInfo.isSoundEnabled()) {
				ValueWriting.playSound(x, y, z, "random.splash", 0.08F, 0.5F + rand.nextFloat() * 0.3F);
			}
	
			// smoke from chimney
			if (rand.nextInt(2) == 0) {
				double chimX = x + 0.5;
				double chimY = y + 2.4F;
				double chimZ = z + 0.5;
				int rota = GameInfo.getMD(world, x, y+1, z);
				if (rota == 2){
					chimX += 0.6;
		        }else if (rota == 3){
		        	chimX -= 0.6;
		        }else if (rota == 4){
		        	chimZ -= 0.6;
		        }else if (rota == 5){
		        	chimZ += 0.6;
		        }
				world.spawnParticle("largesmoke", chimX, chimY, chimZ, 0, 0, 0);
			}
		}
	}

	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}case PC_Utils.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
	   		list.add(PC_Utils.NO_HARVEST);
	   		list.add(PC_Utils.NO_PICKUP);
	   		return list;
		}case PC_Utils.MSG_DONT_SHOW_IN_CRAFTING_TOOL:
			break;
		default:
			return null;
		}
		return true;
	}

	@Override
	public boolean foundStructAt(EntityPlayer entityplayer, World world, PC_Struct2<PC_VecI, Integer> structStart) {
		PC_VecI pos = structStart.a.offset(1, 5, 1);
		int fishingMachine = PCma_App.fishingMachine.blockID;
		for(int z=-2; z<=2; z++){
			for(int x=-2; x<=2; x++){
				int blockID = GameInfo.getBID(world, pos.x + x, pos.y, pos.z - z);
				if(blockID==fishingMachine){
					return false;
				}
			}
		}
		int meta = GameInfo.getMD(world, pos);
		ValueWriting.setBID(world, pos, blockID, meta);
		return true;
	}

}
