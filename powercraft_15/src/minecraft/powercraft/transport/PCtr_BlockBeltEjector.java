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
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.PC_VecI;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.registry.PC_GresRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.tileentity.PC_TileEntity;

@PC_BlockInfo(itemBlock=PCtr_ItemBlockConveyor.class, tileEntity=PCtr_TileEntityEjectionBelt.class)
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
    public TileEntity newTileEntity(World world, int metadata) {
        return new PCtr_TileEntityEjectionBelt();
    }

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    private boolean isPowered(World world, PC_VecI pos)
    {
    	return GameInfo.isPoweredIndirectly(world, pos) || GameInfo.isPoweredIndirectly(world, pos.offset(0, 1, 0)) || GameInfo.isPoweredIndirectly(world, pos.offset(0, -1, 0));
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

            PC_GresRegistry.openGres("EjectionBelt", entityplayer, GameInfo.<PC_TileEntity>getTE(world, i, j, k));
            return true;
        }
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        PC_VecI pos = new PC_VecI(i, j, k);
        int meta = GameInfo.getMD(world, pos);
        PCtr_TileEntityEjectionBelt te = GameInfo.getTE(world, pos);
        
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

	@Override
	protected Object msg2(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch (msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:{
			return "ejection belt";
		}
		}
		return null;
	}
}
