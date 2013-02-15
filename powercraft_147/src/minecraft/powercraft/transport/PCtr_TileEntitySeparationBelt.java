package powercraft.transport;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_VecI;
import powercraft.management.entity.PC_EntityItem;
import powercraft.management.inventory.PC_ISpecialAccessInventory;

public class PCtr_TileEntitySeparationBelt extends PCtr_TileEntitySeparationBeltBase
{
	public PCtr_TileEntitySeparationBelt()
    {
        separatorContents = new ItemStack[18];
        setData(GROUP_LOGS, false);
        setData(GROUP_PLANKS, false);
        setData(GROUP_ALL, false);
    }
    
    @Override
    public int calculateItemDirection(Entity entity)
    {
        boolean notItem = !(entity instanceof EntityItem);
        ItemStack itemstack = null;

        itemstack = PCtr_BeltHelper.getItemStackForEntity(entity);
        
        if (itemstack == null)
        {
            return 0;
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
            return 0;
        }

        if (countLeft == 0 && countRight > 0)
        {
            return -1;
        }

        if (countLeft > 0 && countRight == 0)
        {
            return 1;
        }
        
        if (countLeft > 0 && countRight > 0)
        {
            if (notItem)
            {
                setItemDirection(entity, 0);
                return 0;
            }

            int[] translate = { 1, 0, -1 };
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

            translate[2] = (PCtr_BeltHelper.isTransporterAt(worldObj, new PC_VecI(leftX, yCoord, leftZ)) ? -1 : 0);
            translate[0] = (PCtr_BeltHelper.isTransporterAt(worldObj, new PC_VecI(rightX, yCoord, rightZ)) ? 1 : 0);

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

        return 0;
    }

    @Override
    public String getInvName()
    {
        return "Item Separator";
    }
}
