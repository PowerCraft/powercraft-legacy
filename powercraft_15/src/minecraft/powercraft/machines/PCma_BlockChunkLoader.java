package powercraft.machines;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_ChunkUpdateForcer;
import powercraft.api.PC_Struct2;
import powercraft.api.PC_Utils;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.PC_Utils.ValueWriting;
import powercraft.api.PC_VecI;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.recipes.PC_I3DRecipeHandler;
import powercraft.api.registry.PC_MSGRegistry;

@PC_BlockInfo(tileEntity=PCma_TileEntityChunkLoader.class)
public class PCma_BlockChunkLoader extends PC_Block implements PC_I3DRecipeHandler {

	public PCma_BlockChunkLoader(int id) {
		super(id, Material.glass, null);
	}
	
	@Override
	public boolean showInCraftingTool() {
		return false;
	}
	
	@Override
	public TileEntity newTileEntity(World world, int metadata) {
		return new PCma_TileEntityChunkLoader();
	}

	@Override
	public int quantityDropped(Random par1Random){
        return 0;
    }
	
	@Override
	public boolean isOpaqueCube(){
        return false;
    }
	
	@Override
	public boolean renderAsNormalBlock(){
        return false;
    }
	
	@Override
	public int idPicked(World par1World, int par2, int par3, int par4){
		return Block.glass.blockID;
	}
	
	@Override
	public boolean foundStructAt(EntityPlayer entityplayer, World world, PC_Struct2<PC_VecI, Integer> structStart) {
		PC_VecI mid = structStart.a.offset(1, 0, 1);
		for(int i=-1; i<=1; i++){
			for(int j=-1; j<=1; j++){
				int h=0;
				if(i!=j && -i!=j){
					h=1;
				}
				if(!world.canBlockSeeTheSky(mid.x+i, mid.y+h, mid.z+j))
					return false;
			}
		}
		if(!GameInfo.isPlayerOPOrOwner(entityplayer)){
			return false;
		}
		ValueWriting.setBID(world, mid.offset(0, 1, 1), 0, 0);
		ValueWriting.setBID(world, mid.offset(0, 1, -1), 0, 0);
		ValueWriting.setBID(world, mid.offset(1, 1, 0), 0, 0);
		ValueWriting.setBID(world, mid.offset(-1, 1, 0), 0, 0);
		ValueWriting.setBID(world, mid, blockID, 0);
		PC_ChunkUpdateForcer.forceChunkUpdate(world, mid, 1);
		return true;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		PC_ChunkUpdateForcer.stopForceChunkUpdate(world, new PC_VecI(x, y, z));
		super.breakBlock(world, x, y, z, par5, par6);
	}
	
	@Override
	public Icon getBlockTextureFromSideAndMetadata(int par1, int par2) {
		return Block.glass.getBlockTextureFromSideAndMetadata(par1, par2);
	}

	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch (msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			return "Chunk Loader";
		case PC_MSGRegistry.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
	   		list.add(PC_Utils.NO_HARVEST);
	   		list.add(PC_Utils.NO_PICKUP);
	   		list.add(PC_Utils.HARVEST_STOP);
	   		return list;
		}case PC_MSGRegistry.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}}
		return null;
	}

	@Override
	public boolean canBeCrafted() {
		return blockID!=-1;
	}

}
