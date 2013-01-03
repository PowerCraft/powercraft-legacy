package powercraft.machines;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;

public class PCma_BlockFishingMachine extends PC_Block {

	public PCma_BlockFishingMachine(int id) {
		super(id, 4, Material.wood, false);
	}
	
	@Override
	public TileEntity newTileEntity(World world, int metadata) {
		return new PCma_TileEntityFishingMachine();
	}

	@Override
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
    }
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3) {
		return Block.planks.blockID;
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
	
	private static boolean isWaterBelow(World world, PC_VecI pos){
		int water = Block.waterStill.blockID;
		for(int x=-1; x<=1; x++){
			for(int y=-5; y<0; y++){
				for(int z=-1; z<=1; z++){
					int blockID = GameInfo.getBID(world, pos.x + x, pos.y + y, pos.z + z);
					if(blockID != Block.waterStill.blockID && blockID != Block.waterMoving.blockID){
						System.out.println(x+":"+y+":"+z+":"+blockID+":"+water);
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public static boolean isStructOK(World world, PC_VecI pos){
		int fence = Block.fence.blockID;
		int planks = Block.planks.blockID;
		int chest = Block.chest.blockID;
		int fishingMachine = PCma_App.fishingMachine.blockID;
		int[][][] array = {{
				{fence, planks, fence},
				{planks, fishingMachine, planks},
				{fence, planks, fence}},{
					
				{-1, -1, -1},
				{-1, chest, -1},
				{-1, -1, -1}}};
		
		for(int y=0; y<array.length; y++){
			for(int z=0; z<array[y].length; z++){
				for(int x=0; x<array[y][z].length; x++){
					int blockID = GameInfo.getBID(world, pos.x - 1 + x, pos.y + y, pos.z - 1 + z);
					if(blockID != array[y][z][x] && array[y][z][x] != -1){
						return false;
					}
				}
			}
		}
		return isWaterBelow(world, pos);
	}
	
	private static boolean isStructComplete(World world, PC_VecI pos){
		int fence = Block.fence.blockID;
		int planks = Block.planks.blockID;
		int chest = Block.chest.blockID;
		int fishingMachine = PCma_App.fishingMachine.blockID;
		int[][][] array = {{
				{fence, planks, fence},
				{planks, planks, planks},
				{fence, planks, fence}},{
					
				{-1, -1, -1},
				{-1, chest, -1},
				{-1, -1, -1}}};
		
		for(int y=0; y<array.length; y++){
			for(int z=0; z<array[y].length; z++){
				for(int x=0; x<array[y][z].length; x++){
					int blockID = GameInfo.getBID(world, pos.x - 1 + x, pos.y + y, pos.z - 1 + z);
					if(blockID != array[y][z][x] && array[y][z][x] != -1){
						return false;
					}
				}
			}
		}
		for(int z=-2; z<=2; z++){
			for(int x=-2; x<=2; x++){
				int blockID = GameInfo.getBID(world, pos.x + x, pos.y, pos.z - z);
				if(blockID==fishingMachine){
					return false;
				}
			}
		}
		return isWaterBelow(world, pos);
	}
	
	private boolean onActivatorUsedOnBlock(ItemStack itemstack, EntityPlayer entityplayer, World world, PC_VecI pos){
		for(int i=-1; i<=1; i++){
			for(int j=-1; j<=1; j++){
				PC_VecI p = new PC_VecI(pos.x+i, pos.y, pos.z+j);
				if(isStructComplete(world, p)){
					int meta = GameInfo.getMD(world, p);
					ValueWriting.setBID(world, p, blockID, meta);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
		if(GameInfo.<PCma_TileEntityFishingMachine>getTE(world, x, y, z).running){
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
		case PC_Utils.MSG_DONT_SHOW_IN_CRAFTING_TOOL:
			break;
		case PC_Utils.MSG_ON_ACTIVATOR_USED_ON_BLOCK:
			return onActivatorUsedOnBlock((ItemStack)obj[0], (EntityPlayer)obj[1], (World)obj[2], (PC_VecI)obj[3]);
		default:
			return null;
		}
		return true;
	}

}
