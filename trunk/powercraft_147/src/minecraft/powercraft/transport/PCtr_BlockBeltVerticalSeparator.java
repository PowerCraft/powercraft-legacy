package powercraft.transport;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils;
import powercraft.management.PC_VecI;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Gres;

public class PCtr_BlockBeltVerticalSeparator extends PCtr_BlockBeltBase {

	public PCtr_BlockBeltVerticalSeparator(int id) {
		super(id, 8);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k,
			Entity entity) {
		PC_VecI pos = new PC_VecI(i, j, k);
        int redir = 0;

        if (PCtr_BeltHelper.isEntityIgnored(entity))
        {
            return;
        }

        PCtr_TileEntityVerticalSeparationBelt tes = (PCtr_TileEntityVerticalSeparationBelt) world.getBlockTileEntity(i, j, k);
        redir =1; //= tes.getDirection(entity);
        int rotation=0;
        
        PC_VecI pos_leading_to = pos.copy();

        switch (redir)
        {
            case -1:
                pos_leading_to.y--;
                rotation=5;
                break;

            case 0:
            	rotation=PCtr_BeltHelper.getRotation(world.getBlockMetadata(i, j, k));
            	break;
            	
            case 1:
                pos_leading_to.y++;
                rotation=4;
                break;
        }

        if (entity instanceof EntityItem && PCtr_BeltHelper.storeEntityItemAt(world, pos_leading_to, (EntityItem) entity))
        {
        	return;
        }
        
        boolean leadsToNowhere = PCtr_BeltHelper.isBlocked(world, pos_leading_to);

        if (!leadsToNowhere)
        {
            PCtr_BeltHelper.entityPreventDespawning(world, pos, true, entity);
        }
        
        leadsToNowhere = leadsToNowhere && PCtr_BeltHelper.isBeyondStorageBorder(world, rotation, pos, entity, PCtr_BeltHelper.STORAGE_BORDER_LONG);
        
        if (entity.onGround){
            entity.moveEntity(0, 0.01D, 0);
        }
        PCtr_BeltHelper.moveEntityOnBelt(world, pos, entity, true, !leadsToNowhere, rotation, PCtr_BeltHelper.MAX_HORIZONTAL_SPEED,
                PCtr_BeltHelper.HORIZONTAL_BOOST);
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k,
			EntityPlayer entityplayer, int par6, float par7, float par8,
			float par9) {
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

            Gres.openGres("seperationBelt", entityplayer, GameInfo.<PC_TileEntity>getTE(world, i, j, k));
            return true;
        }
	}
	
    @Override
    public TileEntity newTileEntity(World world, int metadata) {
        return new PCtr_TileEntityVerticalSeparationBelt();
    }

	@Override
	protected Object msg2(IBlockAccess world, PC_VecI pos, int msg,
			Object... obj) {
		switch (msg){
		case PC_Utils.MSG_DEFAULT_NAME:{
			return "vertical separation belt";
		}
		}
		return null;
	}

}
