package powercraft.transport;

import java.util.List;
import java.util.Random;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;
import powercraft.management.PC_Utils;
import powercraft.management.PC_VecI;

public class PCtr_BlockBeltDetector extends PCtr_BlockBeltBase
{
    public PCtr_BlockBeltDetector()
    {
        super(6);
    }

    @Override
    public String getDefaultName()
    {
        return "detection belt";
    }

    @Override
    public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        return isActive(iblockaccess, i, j, k);
    }

    @Override
    public boolean isIndirectlyPoweringTo(IBlockAccess world, int i, int j, int k, int l)
    {
        return isPoweringTo(world, i, j, k, l);
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

        if (isPressed != isAlreadyActive)
        {
            world.setBlockMetadataWithNotify(i, j, k, PCtr_BeltHelper.getMeta(meta, isPressed));
            PC_Utils.hugeUpdate(world, i, j, k, blockID);
            PC_Utils.playSound(i + 0.5D, j + 0.125D, k + 0.5D, "random.click", 0.15F, 0.5F);
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
        PC_CoordI pos = new PC_CoordI(i, j, k);

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

        int direction = getRotation(pos.getMeta(world));
        PC_CoordI pos_leading_to = pos.copy();

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
	public Object msg(World world, PC_VecI pos, int msg, Object... obj) {
		// TODO Auto-generated method stub
		return null;
	}
}
