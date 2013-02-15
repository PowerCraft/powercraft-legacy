package powercraft.transport;

import powercraft.management.PC_VecI;
import powercraft.management.entity.PC_EntityItem;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

public class PCtr_TileEntityVerticalSeparationBelt extends PCtr_TileEntitySeparationBelt {

	@Override
	public int calculateItemDirection(Entity entity) {
        boolean notItem = !(entity instanceof EntityItem);
        ItemStack itemstack = null;

        itemstack = PCtr_BeltHelper.getItemStackForEntity(entity);
        
        if (itemstack == null)
        {
            return 0;
        }
        
        int countUp = 0;
        int countDown = 0;

        for (int i = 0; i < getSizeInventory(); i++)
        {
            ItemStack stack = getStackInSlot(i);

            if (stack != null
                    && (stack.isItemEqual(itemstack) || (isGroupLogs() && stack.itemID == Block.wood.blockID && itemstack.itemID == Block.wood.blockID)
                            || (isGroupPlanks() && stack.itemID == Block.planks.blockID && itemstack.itemID == Block.planks.blockID) || (isGroupAll() && stack.itemID == itemstack.itemID)))
            {

                if (i <= 8)
                {
                    countUp += stack.stackSize;
                }

                if (i >= 9)
                {
                    countDown += stack.stackSize;
                }
            }
        }

        if (countDown == 0 && countUp == 0)
        {
            return 0;
        }

        if (countUp == 0 && countDown > 0)
        {
            return -1;
        }

        if (countUp > 0 && countDown == 0)
        {
            return 1;
        }
        
        if (countUp > 0 && countDown > 0)
        {
            if (notItem)
            {
                setItemDirection(entity, 0);
                return 0;
            }

            int[] translate = { 1, 0, -1 };
            int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
            int x = xCoord, z = zCoord;

            //  z
            //x   X
            //  Z
            
            switch (PCtr_BeltHelper.getRotation(meta))
            {
                case 0:
                    z++;
                    break;

                case 1:
                    x--;
                    break;

                case 2:
                    z--;
                    break;

                case 3:
                    x++;
                    break;
            }

            translate[2] = (PCtr_BeltHelper.isTransporterAt(worldObj, new PC_VecI(x, yCoord-1, z)) ? -1 : 0);
            translate[0] = (PCtr_BeltHelper.isTransporterAt(worldObj, new PC_VecI(x, yCoord+1, z)) ? 1 : 0);

            if (translate[0] == translate[2])
            {
                translate[0] = 1;
                translate[2] = -1;
            }

            if (itemstack.stackSize == 1)
            {
            	if(worldObj.isRemote){
                	setItemDirection(entity, 0);
                    return 0;
                }
                int newredir = (1 + rand.nextInt(countUp + countDown)) <= countUp ? 1 : -1;
                setItemDirection(entity,translate[1 - newredir]);
                return translate[1 - newredir];
            }

            float fractionUp = (float) countUp / (float)(countUp + countDown);
            int partUp = Math.round(itemstack.stackSize * fractionUp);
            int partDown = itemstack.stackSize - partUp;

            if (partUp > 0)
            {
                itemstack.stackSize = partUp;
            }
            else
            {
                setItemDirection(entity, translate[2]);
                return translate[2];
            }
            
            if (partDown > 0)
            {
            	if(!worldObj.isRemote){
	                ItemStack downStack = itemstack.copy();
	                downStack.stackSize = partDown;
	                EntityItem entityitem2 = new PC_EntityItem(worldObj, entity.posX, entity.posY, entity.posZ, downStack);
	                entityitem2.motionX = entity.motionX;
	                entityitem2.motionY = entity.motionY;
	                entityitem2.motionZ = entity.motionZ;
	                worldObj.spawnEntityInWorld(entityitem2);
	                setItemDirection(entityitem2, translate[2]);
	                EntityItem entityNew = new PC_EntityItem(worldObj, entity.posX, entity.posY, entity.posZ, itemstack);
	                entityNew.motionX = entity.motionX;
	                entityNew.motionY = entity.motionY;
	                entityNew.motionZ = entity.motionZ;
	                worldObj.spawnEntityInWorld(entityNew);
	                entity.setDead();
	                entity = entityNew;
	            }
            }
            
            setItemDirection(entity, translate[0]);
            return translate[0];
        }

        return 0;
	}

}
