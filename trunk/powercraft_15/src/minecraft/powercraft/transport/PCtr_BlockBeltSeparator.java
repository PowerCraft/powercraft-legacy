package powercraft.transport;

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

@PC_BlockInfo(name="separation belt", tileEntity=PCtr_TileEntitySeparationBelt.class, canPlacedRotated=true)
public class PCtr_BlockBeltSeparator extends PCtr_BlockBeltBase
{
    public PCtr_BlockBeltSeparator(int id)
    {
        super(id, "belt_separator");
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
        PC_Direction rotation = getRotation(world.getBlockMetadata(i, j, k));
        rotation.rotate(redir);

        PC_VecI pos_leading_to = pos.offset(rotation.getOffset());
        
        if (entity instanceof EntityItem && PCtr_BeltHelper.storeEntityItemAt(world, pos_leading_to, (EntityItem) entity, redir))
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

            PC_GresRegistry.openGres("SeperationBelt", entityplayer, PC_Utils.<PC_TileEntity>getTE(world, i, j, k));
            return true;
        }
    }

}
