package powercraft.transport;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

import net.minecraft.entity.Entity;
import powercraft.management.PC_Struct2;
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

    private Hashtable<Integer, Integer> redirList = new Hashtable<Integer, Integer>();

    @Override
    public final void updateEntity()
    {
        Enumeration<Integer> enumer = redirList.keys();
        
        while (enumer.hasMoreElements())
        {
        	int id = enumer.nextElement();
            Entity thisItem = worldObj.getEntityByID(id);

            if(thisItem==null){
            	redirList.remove(id);
            }else{
	            if (thisItem.posX < xCoord - 0.2F || thisItem.posY < yCoord - 0.2F || thisItem.posZ < zCoord - 0.2F || thisItem.posX > xCoord + 1.2F
	                    || thisItem.posY > yCoord + 2.2F || thisItem.posZ > zCoord + 1.2F)
	            {
	                redirList.remove(id);
	            }
            }
        }
    }

    public final int getDirection(Entity entity)
    {
        if (redirList.containsKey(entity.entityId))
        {
            return redirList.get(entity.entityId);
        }
        else
        {
            return calculateItemDirection(entity);
        }
    }

    protected abstract int calculateItemDirection(Entity entity);

    protected final void setItemDirection(Entity entity, int direction)
    {
    	setItemDirection(entity.entityId, direction);
    }

    protected final void setItemDirection(int entityID, int direction)
    {
        redirList.put(entityID, direction);
        if(!worldObj.isRemote)
        	call("newID", new PC_Struct2<Integer, Integer>(entityID, direction));
    }
    
    @Override
	protected void onCall(String key, Object value) {
		if(key.equals("newID")){
			if(worldObj.isRemote){
				PC_Struct2<Integer, Integer> d= (PC_Struct2<Integer, Integer>)value;
				setItemDirection(d.a, d.b);
			}
		}
	}
    
}
