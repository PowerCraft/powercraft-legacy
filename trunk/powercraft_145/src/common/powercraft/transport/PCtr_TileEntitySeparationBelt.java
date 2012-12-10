package powercraft.transport;

import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityBlaze;
import net.minecraft.src.EntityChicken;
import net.minecraft.src.EntityCow;
import net.minecraft.src.EntityCreeper;
import net.minecraft.src.EntityEnderman;
import net.minecraft.src.EntityIronGolem;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityMagmaCube;
import net.minecraft.src.EntityMooshroom;
import net.minecraft.src.EntityOcelot;
import net.minecraft.src.EntityPig;
import net.minecraft.src.EntityPigZombie;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.EntitySkeleton;
import net.minecraft.src.EntitySlime;
import net.minecraft.src.EntitySnowman;
import net.minecraft.src.EntitySpider;
import net.minecraft.src.EntityWolf;
import net.minecraft.src.EntityXPOrb;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import powercraft.management.PC_ISpecialAccessInventory;
import powercraft.management.PC_InvUtils;
import powercraft.management.PC_VecI;

public class PCtr_TileEntitySeparationBelt extends PCtr_TileEntityRedirectionBeltBase implements IInventory, PC_ISpecialAccessInventory
{
    public boolean group_logs = true;

    public boolean group_planks = true;

    public boolean group_all = false;

    public PCtr_TileEntitySeparationBelt()
    {
        separatorContents = new ItemStack[18];
    }

    @Override
    public int getSizeInventory()
    {
        return 18;
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest() {}

    @Override
    public int calculateItemDirection(Entity entity)
    {
        boolean notItem = false;
        ItemStack itemstack = null;

        if (entity instanceof EntityItem)
        {
            itemstack = ((EntityItem) entity).item;
        }
        else
        {
            notItem = true;

            if (entity instanceof EntityPig)
            {
                itemstack = new ItemStack(Item.porkRaw, 1, 0);
            }

            if (entity instanceof EntitySheep)
            {
                itemstack = new ItemStack(Block.cloth, 1, 0);
            }

            if (entity instanceof EntityCow)
            {
                itemstack = new ItemStack(Item.beefRaw, 1, 0);
            }

            if (entity instanceof EntityCreeper)
            {
                itemstack = new ItemStack(Item.gunpowder, 1, 0);
            }

            if (entity instanceof EntityZombie)
            {
                itemstack = new ItemStack(Item.rottenFlesh, 1, 0);
            }

            if (entity instanceof EntitySkeleton)
            {
                itemstack = new ItemStack(Item.bone, 1, 0);
            }

            if (entity instanceof EntitySlime)
            {
                itemstack = new ItemStack(Item.slimeBall, 1, 0);
            }

            if (entity instanceof EntityEnderman)
            {
                itemstack = new ItemStack(Item.enderPearl, 1, 0);
            }

            if (entity instanceof EntitySnowman)
            {
                itemstack = new ItemStack(Item.snowball, 1, 0);
            }

            if (entity instanceof EntityChicken)
            {
                itemstack = new ItemStack(Item.chickenRaw, 1, 0);
            }

            if (entity instanceof EntityXPOrb)
            {
                itemstack = new ItemStack(Item.diamond, 1, 0);
            }

            if (entity instanceof EntitySpider)
            {
                itemstack = new ItemStack(Item.silk, 1, 0);
            }

            if (entity instanceof EntityOcelot)
            {
                itemstack = new ItemStack(Item.fishRaw, 1, 0);
            }

            if (entity instanceof EntityMooshroom)
            {
                itemstack = new ItemStack(Block.mushroomRed, 1, 0);
            }

            if (entity instanceof EntityWolf)
            {
                itemstack = new ItemStack(Item.cookie, 1, 0);
            }

            if (entity instanceof EntityBlaze)
            {
                itemstack = new ItemStack(Item.blazePowder, 1, 0);
            }

            if (entity instanceof EntityMagmaCube)
            {
                itemstack = new ItemStack(Item.magmaCream, 1, 0);
            }

            if (entity instanceof EntityPigZombie)
            {
                itemstack = new ItemStack(Item.goldNugget, 1, 0);
            }

            if (entity instanceof EntityIronGolem)
            {
                itemstack = new ItemStack(Item.ingotIron, 1, 0);
            }
        }

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
                    && (stack.isItemEqual(itemstack) || (group_logs && stack.itemID == Block.wood.blockID && itemstack.itemID == Block.wood.blockID)
                            || (group_planks && stack.itemID == Block.planks.blockID && itemstack.itemID == Block.planks.blockID) || (group_all && stack.itemID == itemstack.itemID)))
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
                setItemDirection(entity, Integer.valueOf(0));
                return 0;
            }

            int[] translate = { 1, 0, -1 };
            int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
            int leftX = xCoord, leftZ = zCoord;
            int rightX = xCoord, rightZ = zCoord;

            switch (((PCtr_BlockBeltSeparator) PCtr_App.separationBelt).getRotation(meta))
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
                int newredir = (1 + rand.nextInt(countLeft + countRight)) <= countLeft ? 1 : -1;
                setItemDirection(entity, Integer.valueOf(translate[1 - newredir]));
                return translate[1 - newredir];
            }

            float fractionLeft = (float) countLeft / (float)(countLeft + countRight);
            int partLeft = Math.round(itemstack.stackSize * fractionLeft);
            int partRight = itemstack.stackSize - partLeft;

            if (partLeft > 0)
            {
                itemstack.stackSize = partLeft;
                setItemDirection(entity, Integer.valueOf(translate[0]));
            }
            else
            {
                setItemDirection(entity, Integer.valueOf(translate[2]));
                return translate[2];
            }

            if (partRight > 0)
            {
                ItemStack rightStack = itemstack.copy();
                rightStack.stackSize = partRight;
                EntityItem entityitem2 = new EntityItem(worldObj, entity.posX, entity.posY, entity.posZ, rightStack);
                entityitem2.motionX = entity.motionX;
                entityitem2.motionY = entity.motionY;
                entityitem2.motionZ = entity.motionZ;
                worldObj.spawnEntityInWorld(entityitem2);
                setItemDirection(entityitem2, Integer.valueOf(translate[2]));
            }
            else
            {
                setItemDirection(entity, Integer.valueOf(translate[0]));
                return translate[0];
            }

            return translate[0];
        }

        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return separatorContents[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        if (separatorContents[i] != null)
        {
            if (separatorContents[i].stackSize <= j)
            {
                ItemStack itemstack = separatorContents[i];
                separatorContents[i] = null;
                onInventoryChanged();
                return itemstack;
            }

            ItemStack itemstack1 = separatorContents[i].splitStack(j);

            if (separatorContents[i].stackSize == 0)
            {
                separatorContents[i] = null;
            }

            onInventoryChanged();
            return itemstack1;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        separatorContents[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }

        onInventoryChanged();
    }

    @Override
    public String getInvName()
    {
        return "Item Separator";
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        PC_InvUtils.loadInventoryFromNBT(nbttagcompound, "Items", this);
        group_all = nbttagcompound.getBoolean("GroupAll");
        group_logs = nbttagcompound.getBoolean("GroupLogs");
        group_planks = nbttagcompound.getBoolean("GroupPlanks");

        if (!nbttagcompound.getBoolean("mark342"))
        {
            group_all = false;
            group_logs = true;
            group_planks = true;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        PC_InvUtils.saveInventoryToNBT(nbttagcompound, "Items", this);
        nbttagcompound.setBoolean("GroupAll", group_all);
        nbttagcompound.setBoolean("GroupLogs", group_logs);
        nbttagcompound.setBoolean("GroupPlanks", group_planks);
        nbttagcompound.setBoolean("mark342", true);
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    private ItemStack separatorContents[];

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (separatorContents[par1] != null)
        {
            ItemStack itemstack = separatorContents[par1];
            separatorContents[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public boolean insertStackIntoInventory(ItemStack stack)
    {
        return false;
    }

    @Override
    public boolean needsSpecialInserter()
    {
        return true;
    }

    @Override
    public boolean canPlayerInsertStackTo(int slot, ItemStack stack)
    {
        return true;
    }

    @Override
    public boolean canMachineInsertStackTo(int slot, ItemStack stack)
    {
        return false;
    }

    @Override
    public boolean canDispenseStackFrom(int slot)
    {
        return false;
    }

    @Override
    public void setData(Object[] o)
    {
        int p = 0;

        while (p < o.length)
        {
            String var = (String)o[p++];

            if (var.equals("logsPlanksAll"))
            {
                group_logs = (boolean)(Boolean)o[p++];
                group_planks = (boolean)(Boolean)o[p++];
                group_all = (boolean)(Boolean)o[p++];
            }
        }
    }

    @Override
    public Object[] getData()
    {
        Object[] o = new Object[4];
        o[0] = "logsPlanksAll";
        o[1] = group_logs;
        o[2] = group_planks;
        o[3] = group_all;
        return o;
    }
}
