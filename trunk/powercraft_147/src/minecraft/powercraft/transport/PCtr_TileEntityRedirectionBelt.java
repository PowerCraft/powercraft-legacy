package powercraft.transport;

import net.minecraft.entity.Entity;
import powercraft.management.PC_Direction;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_VecI;

public class PCtr_TileEntityRedirectionBelt extends PCtr_TileEntityRedirectionBeltBase
{
    public PCtr_TileEntityRedirectionBelt() {}

    @Override
    protected PC_Direction calculateItemDirection(Entity entity)
    {
        PCtr_BlockBeltRedirector block = ((PCtr_BlockBeltRedirector) PCtr_App.redirectionBelt);
        PC_VecI pos = getCoord();
        int meta = PCtr_BeltHelper.getRotation(GameInfo.getMD(worldObj, pos));
        PC_Direction redir = null;

        if (block.isPowered(worldObj, pos))
        {
            switch (meta)
            {
                case 0:
                    if (PCtr_BeltHelper.isTransporterAt(worldObj, pos.offset(1, 0, 0)))
                    {
                        redir = PC_Direction.LEFT;
                    }
                    else if (PCtr_BeltHelper.isTransporterAt(worldObj, pos.offset(-1, 0, 0)))
                    {
                        redir = PC_Direction.RIGHT;
                    }

                    break;

                case 1:
                    if (PCtr_BeltHelper.isTransporterAt(worldObj, pos.offset(0, 0, 1)))
                    {
                        redir = PC_Direction.LEFT;
                    }
                    else if (PCtr_BeltHelper.isTransporterAt(worldObj, pos.offset(0, 0, -1)))
                    {
                        redir = PC_Direction.RIGHT;
                    }

                    break;

                case 2:
                    if (PCtr_BeltHelper.isTransporterAt(worldObj, pos.offset(-1, 0, 0)))
                    {
                        redir = PC_Direction.LEFT;
                    }
                    else if (PCtr_BeltHelper.isTransporterAt(worldObj, pos.offset(1, 0, 0)))
                    {
                        redir = PC_Direction.RIGHT;
                    }

                    break;

                case 3:
                    if (PCtr_BeltHelper.isTransporterAt(worldObj, pos.offset(0, 0, -1)))
                    {
                        redir = PC_Direction.LEFT;
                    }
                    else if (PCtr_BeltHelper.isTransporterAt(worldObj, pos.offset(0, 0, 1)))
                    {
                        redir = PC_Direction.RIGHT;
                    }

                    break;
            }
        }

        if (redir == null)
        {
            switch (meta)
            {
                case 0:
                    if (PCtr_BeltHelper.isTransporterAt(worldObj, pos.offset(1, 0, 0)) && PCtr_BeltHelper.isTransporterAt(worldObj, pos.offset(-1, 0, 0))
                            && !PCtr_BeltHelper.isTransporterAt(worldObj, pos.offset(0, 0, -1)))
                    {
                        redir = PC_Direction.RIGHT;
                    }

                    break;

                case 1:
                    if (PCtr_BeltHelper.isTransporterAt(worldObj, pos.offset(0, 0, 1)) && PCtr_BeltHelper.isTransporterAt(worldObj, pos.offset(0, 0, -1))
                            && !PCtr_BeltHelper.isTransporterAt(worldObj, pos.offset(1, 0, 0)))
                    {
                        redir = PC_Direction.RIGHT;
                    }

                    break;

                case 2:
                    if (PCtr_BeltHelper.isTransporterAt(worldObj, pos.offset(-1, 0, 0)) && PCtr_BeltHelper.isTransporterAt(worldObj, pos.offset(1, 0, 0))
                            && !PCtr_BeltHelper.isTransporterAt(worldObj, pos.offset(0, 0, 1)))
                    {
                        redir = PC_Direction.RIGHT;
                    }

                    break;

                case 3:
                    if (PCtr_BeltHelper.isTransporterAt(worldObj, pos.offset(0, 0, -1)) && PCtr_BeltHelper.isTransporterAt(worldObj, pos.offset(0, 0, 1))
                            && !PCtr_BeltHelper.isTransporterAt(worldObj, pos.offset(-1, 0, 0)))
                    {
                        redir = PC_Direction.RIGHT;
                    }

                    break;
            }
        }

        if(redir==null){
        	redir = PC_Direction.FRONT;
        }
        
        setItemDirection(entity, redir);
        return redir;
    }
}
