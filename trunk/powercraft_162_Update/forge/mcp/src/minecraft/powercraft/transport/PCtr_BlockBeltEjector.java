package powercraft.transport;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.registry.PC_GresRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

@PC_BlockInfo(name="ejection belt", tileEntity=PCtr_TileEntityEjectionBelt.class, canPlacedRotated=true)
public class PCtr_BlockBeltEjector extends PCtr_BlockBeltBase
{
    public PCtr_BlockBeltEjector(int id)
    {
        super(id, "belt_ejector");
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

        PC_Direction direction = getRotation(world.getBlockMetadata(i, j, k));

        PC_VecI pos_leading_to = pos.offset(direction.getOffset());

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
    public boolean canProvidePower()
    {
        return true;
    }

    private boolean isPowered(World world, PC_VecI pos)
    {
    	return getRedstonePowereValueEx(world, pos.x, pos.y, pos.z)>0;
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        if (l > 0)
        {
            world.scheduleBlockUpdate(i, j, k, blockID, tickRate(world));
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
            PC_GresRegistry.openGres("EjectionBelt", entityplayer, PC_Utils.<PC_TileEntity>getTE(world, i, j, k));
            return true;
        }
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        PC_VecI pos = new PC_VecI(i, j, k);
        int meta = PC_Utils.getMD(world, pos);
        PCtr_TileEntityEjectionBelt te = PC_Utils.getTE(world, pos);
        
        if (isPowered(world, pos))
        {
            if (!te.isActive)
            {
                if (!PCtr_BeltHelper.dispenseStackFromNearbyMinecart(world, pos))
                {
                    PCtr_BeltHelper.tryToDispenseItem(world, pos);
                }

                te.isActive = true;
            }
        }
        else if (te.isActive)
        {
        	te.isActive = false;
        }
    }

}
