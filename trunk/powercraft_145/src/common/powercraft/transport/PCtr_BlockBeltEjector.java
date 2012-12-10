package powercraft.transport;

import java.util.Random;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.management.PC_Utils;
import powercraft.management.PC_VecI;

public class PCtr_BlockBeltEjector extends PCtr_BlockBeltBase
{
    public PCtr_BlockBeltEjector()
    {
        super(3);
    }

    @Override
    public String getDefaultName()
    {
        return "ejection belt";
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

        int direction = getRotation(pos.getMeta(world));
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
    public TileEntity createNewTileEntity(World world)
    {
        return new PCtr_TileEntityEjectionBelt();
    }

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    private boolean isPowered(World world, PC_VecI pos)
    {
        return pos.isPoweredIndirectly(world) || pos.offset(0, 1, 0).isPoweredIndirectly(world) || pos.offset(0, -1, 0).isPoweredIndirectly(world);
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        if (l > 0)
        {
            world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
        }
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

            PC_Utils.openGres("EjectionBelt", entityplayer, i, j, k);
            return true;
        }
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        PC_VecI pos = new PC_VecI(i, j, k);
        int meta = pos.getMeta(world);

        if (isPowered(world, pos))
        {
            if (!PCtr_BeltHelper.isActive(meta))
            {
                if (!PCtr_BeltHelper.dispenseStackFromNearbyMinecart(world, pos))
                {
                    PCtr_BeltHelper.tryToDispenseItem(world, pos);
                }

                pos.setMeta(world, PCtr_BeltHelper.getActiveMeta(meta));
            }
        }
        else if (PCtr_BeltHelper.isActive(meta))
        {
            pos.setMeta(world, PCtr_BeltHelper.getPassiveMeta(meta));
        }
    }

	@Override
	public Object msg(World world, PC_VecI pos, int msg, Object... obj) {
		// TODO Auto-generated method stub
		return null;
	}
}
