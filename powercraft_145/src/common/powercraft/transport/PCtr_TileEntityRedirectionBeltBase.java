package powercraft.transport;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

import net.minecraft.src.Entity;
import powercraft.management.PC_TileEntity;

public abstract class PCtr_TileEntityRedirectionBeltBase extends PC_TileEntity
{
    protected Random rand = new Random();

    public PCtr_TileEntityRedirectionBeltBase() {}

    @Override
    public final boolean canUpdate()
    {
        return true;
    }

    private Hashtable<Entity, Integer> redirList = new Hashtable<Entity, Integer>();

    @Override
    public final void updateEntity()
    {
        Enumeration<Entity> enumer = redirList.keys();

        while (enumer.hasMoreElements())
        {
            Entity thisItem = enumer.nextElement();

            if (thisItem.posX < xCoord - 0.2F || thisItem.posY < yCoord - 0.2F || thisItem.posZ < zCoord - 0.2F || thisItem.posX > xCoord + 1.2F
                    || thisItem.posY > yCoord + 2.2F || thisItem.posZ > zCoord + 1.2F)
            {
                redirList.remove(thisItem);
            }
        }
    }

    public final int getDirection(Entity entity)
    {
        if (redirList.containsKey(entity))
        {
            return redirList.get(entity);
        }
        else
        {
            return calculateItemDirection(entity);
        }
    }

    protected abstract int calculateItemDirection(Entity entity);

    protected final void setItemDirection(Entity entity, int direction)
    {
        redirList.put(entity, direction);
    }
}
