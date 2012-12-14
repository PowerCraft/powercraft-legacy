package powercraft.transport;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_VecI;

public class PCtr_BlockBeltRedirector extends PCtr_BlockBeltBase
{
    public PCtr_BlockBeltRedirector(int id)
    {
        super(id, 8);
    }

    public boolean isPowered(World world, PC_VecI pos)
    {
    	return PC_Utils.isPoweredIndirectly(world, pos) || PC_Utils.isPoweredIndirectly(world, pos.offset(0, 1, 0)) || PC_Utils.isPoweredIndirectly(world, pos.offset(0, -1, 0));
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
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
        int redir = teRedir.getDirection(entity);
        int direction = PCtr_BeltHelper.getRotation(GameInfo.getMD(world, pos)) + redir;

        if (direction == -1)
        {
            direction = 3;
        }

        if (direction == 4)
        {
            direction = 0;
        }

        PC_VecI pos_leading_to = pos.copy();

        switch (direction)
        {
            case 0:
                pos_leading_to.z--;
                break;

            case 1:
                pos_leading_to.x++;
                break;

            case 2:
                pos_leading_to.z++;
                break;

            case 3:
                pos_leading_to.x--;
                break;
        }

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
	protected Object msg2(World world, PC_VecI pos, int msg, Object... obj) {
		switch (msg){
		case PC_Utils.MSG_DEFAULT_NAME:{
			return "redirector belt";
		}
		}
		return null;
	}
}
