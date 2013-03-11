package powercraft.transport;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.PC_VecI;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.registry.PC_MSGRegistry;

@PC_BlockInfo(itemBlock=PCtr_ItemBlockConveyor.class)
public class PCtr_BlockBeltBreak extends PCtr_BlockBeltBase
{
    public PCtr_BlockBeltBreak(int id)
    {
        super(id, 5);
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
            PCtr_BeltHelper.packItems(world, pos);
        }

        if (entity instanceof EntityItem)
        {
            PCtr_BeltHelper.doSpecialItemAction(world, pos, (EntityItem) entity);

            if (PCtr_BeltHelper.storeNearby(world, pos, (EntityItem) entity, isPowered(world, pos)))
            {
                return;
            }
        }

        boolean halted = isPowered(world, pos);

        if (halted)
        {
            if (entity instanceof EntityMinecart && halted)
            {
                entity.motionX *= 0.2D;
                entity.motionZ *= 0.2D;
            }
            else
            {
                entity.motionX *= 0.6D;
                entity.motionZ *= 0.6D;
            }
        }

        int meta = PCtr_BeltHelper.getRotation(world.getBlockMetadata(i, j, k));
        int direction = meta;

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
            PCtr_BeltHelper.entityPreventDespawning(world, pos, !halted, entity);
        }

        double speed_max = PCtr_BeltHelper.MAX_HORIZONTAL_SPEED * 0.6D;
        double boost = PCtr_BeltHelper.HORIZONTAL_BOOST * 0.6D;
        PCtr_BeltHelper.moveEntityOnBelt(world, pos, entity, true, !halted && !leadsToNowhere, direction, speed_max, boost);
    }

    private boolean isPowered(World world, PC_VecI pos)
    {
    	return GameInfo.isPoweredIndirectly(world, pos) || GameInfo.isPoweredIndirectly(world, pos.offset(0, 1, 0)) || GameInfo.isPoweredIndirectly(world, pos.offset(0, -1, 0));
    }

	@Override
	protected Object msg2(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch (msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:{
			return "break belt";
		}
		}
		return null;
	}
}
