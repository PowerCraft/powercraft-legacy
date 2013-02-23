package powercraft.transport;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_Direction;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_VecI;
import powercraft.management.annotation.PC_BlockInfo;
import powercraft.management.registry.PC_MSGRegistry;

@PC_BlockInfo(itemBlock=PCtr_ItemBlockConveyor.class, tileEntity=PCtr_TileEntityRedirectionBelt.class)
public class PCtr_BlockBeltRedirector extends PCtr_BlockBeltBase
{
    public PCtr_BlockBeltRedirector(int id)
    {
        super(id, 8);
    }

    public boolean isPowered(World world, PC_VecI pos)
    {
    	return GameInfo.isPoweredIndirectly(world, pos) || GameInfo.isPoweredIndirectly(world, pos.offset(0, 1, 0)) || GameInfo.isPoweredIndirectly(world, pos.offset(0, -1, 0));
    }

    @Override
    public TileEntity newTileEntity(World world, int metadata) {
        return new PCtr_TileEntityRedirectionBelt();
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        PC_VecI pos = new PC_VecI(i, j, k);

        if (PCtr_BeltHelper.isEntityIgnored(entity))
        {
            return;
        }

        if (entity instanceof EntityItem)
        {
            PCtr_BeltHelper.doSpecialItemAction(world, pos, (EntityItem) entity);

            if (PCtr_BeltHelper.storeNearby(world, pos, (EntityItem) entity, false))
            {
                return;
            }
        }

        PCtr_TileEntityRedirectionBeltBase teRedir = (PCtr_TileEntityRedirectionBeltBase) world.getBlockTileEntity(i, j, k);
        PC_Direction redir = teRedir.getDirection(entity);
        int direction = PCtr_BeltHelper.getRotation(GameInfo.getMD(world, pos));
        for(;direction>0; direction--){
        	redir = redir.rotateLeft();
        }

        PC_VecI pos_leading_to = pos.offset(redir.getDir());

        direction = PCtr_BeltHelper.getDir(redir);
        
        boolean leadsToNowhere = PCtr_BeltHelper.isBlocked(world, pos_leading_to);
        leadsToNowhere = leadsToNowhere && PCtr_BeltHelper.isBeyondStorageBorder(world, direction, pos, entity, PCtr_BeltHelper.STORAGE_BORDER_LONG);

        if (!leadsToNowhere)
        {
            PCtr_BeltHelper.entityPreventDespawning(world, pos, true, entity);
        }

        double speed_max = PCtr_BeltHelper.MAX_HORIZONTAL_SPEED;
        double boost = PCtr_BeltHelper.HORIZONTAL_BOOST;
        PCtr_BeltHelper.moveEntityOnBelt(world, pos, entity, true, !leadsToNowhere, direction, speed_max, boost);
    }

	@Override
	protected Object msg2(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch (msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:{
			return "redirector belt";
		}
		}
		return null;
	}
}
