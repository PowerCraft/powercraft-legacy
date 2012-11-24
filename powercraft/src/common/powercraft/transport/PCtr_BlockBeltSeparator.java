package powercraft.transport;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityFX;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.core.PC_CoordI;
import powercraft.core.PC_InvUtils;
import powercraft.core.PC_Utils;

public class PCtr_BlockBeltSeparator extends PCtr_BlockBeltBase
{
    public PCtr_BlockBeltSeparator(int id)
    {
        super(id, 7);
    }

    @Override
    public String getDefaultName()
    {
        return "seperator belt";
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        PC_CoordI pos = new PC_CoordI(i, j, k);
        int redir = 0;

        if (PCtr_BeltHelper.isEntityIgnored(entity))
        {
            return;
        }
        
        if (PC_Utils.isEntityFX(entity))
        {
            return;
        }

        if (!entity.isEntityAlive())
        {
            return;
        }

        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isSneaking())
        {
            return;
        }

        PCtr_TileEntitySeparationBelt tes = (PCtr_TileEntitySeparationBelt) world.getBlockTileEntity(i, j, k);
        redir = tes.getDirection(entity);
        int rotation = getRotation(world.getBlockMetadata(i, j, k));
        rotation += redir;

        while (rotation >= 4)
        {
            rotation -= 4;
        }

        while (rotation < 0)
        {
            rotation += 4;
        }

        PC_CoordI pos_leading_to = pos.copy();

        switch (rotation)
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

            PC_Utils.openGres("SeperationBelt", entityplayer, i, j, k);
            return true;
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new PCtr_TileEntitySeparationBelt();
    }

    @Override
    public void breakBlock(World world, int i, int j, int k, int par5, int par6)
    {
        PCtr_TileEntitySeparationBelt te = (PCtr_TileEntitySeparationBelt) world.getBlockTileEntity(i, j, k);

        if (te != null)
        {
            PC_InvUtils.dropInventoryContents(te, world, te.getCoord());
        }

        super.breakBlock(world, i, j, k, par5, par6);
    }
}
