package powercraft.transport;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import powercraft.api.entity.PC_EntityItem;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_VecI;

public class PCtr_TileEntitySeparationBelt extends PCtr_TileEntitySeparationBeltBase
{
	public PCtr_TileEntitySeparationBelt()
    {
        separatorContents = new ItemStack[18];
    }
    
    @Override
    public PC_Direction calculateItemDirection(Entity entity)
    {
        boolean notItem = !(entity instanceof EntityItem);

        ItemStack itemstack = PCtr_BeltHelper.getItemStackForEntity(entity);
        
        if (itemstack == null)
        {
            return PC_Direction.FRONT;
        }

        int countLeft = 0;
        int countRight = 0;

        for (int i = 0; i < getSizeInventory(); i++)
        {
            ItemStack stack = getStackInSlot(i);

            if (stack != null
                    && (stack.isItemEqual(itemstack) || (isGroupLogs() && stack.itemID == Block.wood.blockID && itemstack.itemID == Block.wood.blockID)
                            || (isGroupPlanks() && stack.itemID == Block.planks.blockID && itemstack.itemID == Block.planks.blockID) || (isGroupAll() && stack.itemID == itemstack.itemID)))
            {
                int tmpi = i % 6;

                if (tmpi >= 3)
                {
                    countRight += stack.stackSize;
                }

                if (tmpi <= 2)
                {
                    countLeft += stack.stackSize;
                }
            }
        }

        if (countLeft == 0 && countRight == 0)
        {
            return PC_Direction.FRONT;
        }

        if (countLeft == 0 && countRight > 0)
        {
            return PC_Direction.RIGHT;
        }

        if (countLeft > 0 && countRight == 0)
        {
            return PC_Direction.LEFT;
        }
        
        if (countLeft > 0 && countRight > 0)
        {
            if (notItem)
            {
                setItemDirection(entity, PC_Direction.FRONT);
                return PC_Direction.FRONT;
            }

            PC_Direction[] translate = { PC_Direction.LEFT, PC_Direction.FRONT, PC_Direction.RIGHT };
            int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
            int leftX = xCoord, leftZ = zCoord;
            int rightX = xCoord, rightZ = zCoord;

            switch (PCtr_BeltHelper.getRotation(meta))
            {
                case 0:
                    leftX++;
                    rightX--;
                    break;

                case 1:
                    leftZ++;
                    rightZ--;
                    break;

                case 2:
                    leftX--;
                    rightX++;
                    break;

                case 3:
                    leftZ--;
                    rightZ++;
                    break;
            }

            translate[2] = (PCtr_BeltHelper.isTransporterAt(worldObj, new PC_VecI(leftX, yCoord, leftZ)) ? PC_Direction.RIGHT : PC_Direction.FRONT);
            translate[0] = (PCtr_BeltHelper.isTransporterAt(worldObj, new PC_VecI(rightX, yCoord, rightZ)) ? PC_Direction.LEFT : PC_Direction.FRONT);

            if (translate[0] == translate[2])
            {
                translate[0] = PC_Direction.LEFT;
                translate[2] = PC_Direction.RIGHT;
            }

            if (itemstack.stackSize == 1)
            {
            	if(worldObj.isRemote){
                	setItemDirection(entity, PC_Direction.FRONT);
                    return PC_Direction.FRONT;
                }
                int newredir = (1 + rand.nextInt(countLeft + countRight)) <= countLeft ? 1 : -1;
                setItemDirection(entity,translate[1 - newredir]);
                return translate[1 - newredir];
            }

            float fractionLeft = (float) countLeft / (float)(countLeft + countRight);
            int partLeft = Math.round(itemstack.stackSize * fractionLeft);
            int partRight = itemstack.stackSize - partLeft;

            if (partLeft > 0)
            {
                itemstack.stackSize = partLeft;
            }
            else
            {
                setItemDirection(entity, translate[2]);
                return translate[2];
            }
            
            if (partRight > 0)
            {
            	if(!worldObj.isRemote){
	                ItemStack rightStack = itemstack.copy();
	                rightStack.stackSize = partRight;
	                EntityItem entityitem2 = new PC_EntityItem(worldObj, entity.posX, entity.posY, entity.posZ, rightStack);
	                entityitem2.motionX = entity.motionX;
	                entityitem2.motionY = entity.motionY;
	                entityitem2.motionZ = entity.motionZ;
	                worldObj.spawnEntityInWorld(entityitem2);
	                setItemDirection(entityitem2, translate[2]);
	                entity.setDead();
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

        return PC_Direction.FRONT;
    }

}
