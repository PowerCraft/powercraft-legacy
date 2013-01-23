package powercraft.transport;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;

public class PCtr_BlockBeltDetector extends PCtr_BlockBeltBase
{
    public PCtr_BlockBeltDetector(int id)
    {
        super(id, 6);
    }

    @Override
	public boolean isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int s) {
    	return isActive(world, x, y, z);
	}

	@Override
	public boolean isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int s) {
		return isProvidingWeakPower(world, x, y, z, s);
	}

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if (isActive(world, i, j, k))
        {
            setStateIfEntityInteractsWithDetector(world, i, j, k);
        }
    }

    private boolean isActive(IBlockAccess world, int i, int j, int k)
    {
        int meta = world.getBlockMetadata(i, j, k);
        return PCtr_BeltHelper.isActive(meta);
    }

    private void setStateIfEntityInteractsWithDetector(World world, int i, int j, int k)
    {
        int meta = world.getBlockMetadata(i, j, k);
        boolean isAlreadyActive = PCtr_BeltHelper.isActive(meta);
        boolean isPressed = false;
        List list = world.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBox(i, j, k, (i + 1), j + 1D, (k + 1)));
        isPressed = list.size() > 0;
        
        if (isPressed != isAlreadyActive && !world.isRemote)
        {
            world.setBlockMetadataWithNotify(i, j, k, PCtr_BeltHelper.getMeta(meta, isPressed));
            ValueWriting.hugeUpdate(world, i, j, k);
            ValueWriting.playSound(i + 0.5D, j + 0.125D, k + 0.5D, "random.click", 0.15F, 0.5F);
        }

        if (isPressed)
        {
            world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k,
            Entity entity)
    {
        PC_VecI pos = new PC_VecI(i, j, k);

        if (PCtr_BeltHelper.isEntityIgnored(entity))
        {
            return;
        }

        if (!isActive(world, i, j, k))
        {
            setStateIfEntityInteractsWithDetector(world, i, j, k);
        }

        if (entity instanceof EntityItem)
        {
            PCtr_BeltHelper.packItems(world, pos);
        }

        int direction = PCtr_BeltHelper.getRotation(GameInfo.getMD(world, pos));
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
	protected Object msg2(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch (msg){
		case PC_Utils.MSG_DEFAULT_NAME:{
			return "detection belt";
		}
		}
		return null;
	}
}
