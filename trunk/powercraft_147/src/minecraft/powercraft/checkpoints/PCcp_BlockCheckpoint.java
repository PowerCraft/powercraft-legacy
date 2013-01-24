package powercraft.checkpoints;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_InvUtils;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.Communication;
import powercraft.management.PC_Utils.Lang;
import powercraft.management.PC_VecI;

public class PCcp_BlockCheckpoint extends PC_Block {
	
	public PCcp_BlockCheckpoint(int id) {
		super(id, 1, Material.air);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F/16.0F, 1.0F);
        setHardness(1.0F);
        setResistance(8.0F);
        setStepSound(Block.soundMetalFootstep);
        setCreativeTab(CreativeTabs.tabTools);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if(!world.isRemote){
			if(entity instanceof EntityPlayer){
				EntityPlayer player = (EntityPlayer) entity;
				if(player.ticksExisted<=2 && PC_InvUtils.isInventoryEmpty(player.inventory)){
					PC_InvUtils.addItemStackToInventory(player.inventory, new ItemStack(Block.grass));
				}
			}
		}
	}

	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9){
        if(world.isRemote){
        	Communication.chatMsg(Lang.tr("pc.checkpoint.setSpawn", new PC_VecI(i, j, k).toString()), true);
        }else{
        	entityplayer.setSpawnChunk(new ChunkCoordinates(i, j, k), true);
        }
        return true;
    }
	
	@Override
	public int getBlockTextureFromSide(int par1) {
		if(par1==0)
			return 0;
		if(par1==1)
			return 1;
		return 2;
	}

	@Override
    public boolean renderAsNormalBlock(){
        return false;
    }

    @Override
    public boolean isOpaqueCube(){
        return false;
    }
    
	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			return "Checkpoint";
		case PC_Utils.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
			list.add(PC_Utils.NO_HARVEST);
			list.add(PC_Utils.NO_PICKUP);
	   		return list;
		}case PC_Utils.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}default:
			return null;
		}
	}

}
