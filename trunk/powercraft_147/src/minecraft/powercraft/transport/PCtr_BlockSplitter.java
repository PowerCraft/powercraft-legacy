package powercraft.transport;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_Direction;
import powercraft.api.PC_Utils;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.PC_VecI;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.registry.PC_GresRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.tileentity.PC_TileEntity;

@PC_BlockInfo(tileEntity=PCtr_TileEntitySplitter.class)
public class PCtr_BlockSplitter extends PC_Block {

	public PCtr_BlockSplitter(int id) {
		super(id, 9, PCtr_MaterialElevator.getMaterial());
		setHardness(0.5F);
        setResistance(8.0F);
        setStepSound(Block.soundMetalFootstep);
        setCreativeTab(CreativeTabs.tabTransport);
	}
	
	@Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        PC_VecI pos = new PC_VecI(i, j, k);

        if (PCtr_BeltHelper.isEntityIgnored(entity))
        {
            return;
        }

        PCtr_TileEntitySplitter tes = (PCtr_TileEntitySplitter) world.getBlockTileEntity(i, j, k);
        PC_Direction redir = tes.getDirection(entity);

        PC_VecI pos_leading_to = pos.offset(redir.getDir());

        int rotation = PCtr_BeltHelper.getDir(redir);
        
        if (entity instanceof EntityItem && PCtr_BeltHelper.storeEntityItemAt(world, pos_leading_to, (EntityItem) entity))
        {
            return;
        }
        
        boolean leadsToNowhere = PCtr_BeltHelper.isBlocked(world, pos_leading_to);

        if (!leadsToNowhere)
        {
            PCtr_BeltHelper.entityPreventDespawning(world, pos, true, entity);
        }

        if(rotation<4){
        	entity.motionY=0;
        	entity.onGround=true;
        }
        
        leadsToNowhere = leadsToNowhere && PCtr_BeltHelper.isBeyondStorageBorder(world, rotation, pos, entity, PCtr_BeltHelper.STORAGE_BORDER_LONG);
        PCtr_BeltHelper.moveEntityOnBelt(world, pos, entity, true, !leadsToNowhere, rotation, PCtr_BeltHelper.MAX_HORIZONTAL_SPEED,
                PCtr_BeltHelper.HORIZONTAL_BOOST);
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        if (PCtr_BeltHelper.blockActivated(world, i, j, k, entityplayer))
        {
            return true;
        }
        else
        {
            ItemStack ihold = entityplayer.getCurrentEquippedItem();

            if (ihold != null)
            {
                if (ihold.getItem() instanceof ItemBlock)
                {
                    if (ihold.itemID == blockID)
                    {
                        return false;
                    }
                }
            }

            PC_GresRegistry.openGres("Splitter", entityplayer, GameInfo.<PC_TileEntity>getTE(world, i, j, k));
            return true;
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
        return null;
    }
    
    @Override
    public TileEntity newTileEntity(World world, int metadata) {
        return new PCtr_TileEntitySplitter();
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
    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l){
        return true;
    }
    
    @Override
	public int getBlockTextureFromSide(int side) {
		return blockIndexInTexture+side;
	}
    
	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch (msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			return "Splitter";
		case PC_MSGRegistry.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}case PC_MSGRegistry.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
	   		list.add(PC_Utils.NO_HARVEST);
	   		list.add(PC_Utils.NO_PICKUP);
	   		return list;
		}
		}
		return null;
	}

}
