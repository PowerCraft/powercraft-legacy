package powercraft.transport;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_Direction;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_VecI;
import powercraft.management.annotation.PC_BlockInfo;
import powercraft.management.registry.PC_GresRegistry;
import powercraft.management.registry.PC_MSGRegistry;
import powercraft.management.tileentity.PC_TileEntity;

@PC_BlockInfo(itemBlock=PCtr_ItemBlockConveyor.class, tileEntity=PCtr_TileEntitySeparationBelt.class)
public class PCtr_BlockBeltSeparator extends PCtr_BlockBeltBase
{
    public PCtr_BlockBeltSeparator(int id)
    {
        super(id, 7);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        PC_VecI pos = new PC_VecI(i, j, k);

        if (PCtr_BeltHelper.isEntityIgnored(entity))
        {
            return;
        }

        PCtr_TileEntitySeparationBelt tes = (PCtr_TileEntitySeparationBelt) world.getBlockTileEntity(i, j, k);
        PC_Direction redir = tes.getDirection(entity);
        int rotation = PCtr_BeltHelper.getRotation(world.getBlockMetadata(i, j, k));
        for(;rotation>0; rotation--){
        	redir = redir.rotateLeft();
        }

        PC_VecI pos_leading_to = pos.offset(redir.getDir());

        rotation = PCtr_BeltHelper.getDir(redir);
        
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

            PC_GresRegistry.openGres("SeperationBelt", entityplayer, GameInfo.<PC_TileEntity>getTE(world, i, j, k));
            return true;
        }
    }

    @Override
    public TileEntity newTileEntity(World world, int metadata) {
        return new PCtr_TileEntitySeparationBelt();
    }
    
	@Override
	protected Object msg2(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch (msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:{
			return "separation belt";
		}
		}
		return null;
	}
}
