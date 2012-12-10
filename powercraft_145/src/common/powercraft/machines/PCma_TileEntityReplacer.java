package powercraft.machines;

import java.util.Random;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import powercraft.core.PC_Color;
import powercraft.core.PC_CoordD;
import powercraft.core.PC_CoordI;
import powercraft.core.PC_EntityLaserParticleFX;
import powercraft.core.PC_ISpecialAccessInventory;
import powercraft.core.PC_InvUtils;
import powercraft.core.PC_TileEntity;
import powercraft.core.PC_Utils;

public class PCma_TileEntityReplacer extends PC_TileEntity implements IInventory, PC_ISpecialAccessInventory
{
    public ItemStack buildBlock;

    private static final int MAXSTACK = 1;
    private static final int SIZE = 1;

    public PC_CoordI coordOffset = new PC_CoordI(0, 1, 0);

    public boolean aidEnabled = false;
    private PC_Color aidcolor;

    public boolean state = false;
    private Random rand;
    private boolean init = false;

    public int extraMeta = -1;

    @Override

    public void updateEntity()
    {
        if (!init)
        {
            init = true;
            Random rnd = new Random((145896555 + xCoord) ^ yCoord ^ (zCoord ^ 132));
            double used = 2D;
            double r = rnd.nextDouble() * 1D;
            used -= r;
            double g = rnd.nextDouble() * 1D;
            used -= g;
            double b = used;

            if (rnd.nextBoolean())
            {
                double f = r;
                r = g;
                g = f;
            }

            if (rnd.nextBoolean())
            {
                double f = g;
                g = b;
                b = f;
            }

            if (rnd.nextBoolean())
            {
                double f = b;
                b = r;
                r = f;
            }

            aidcolor = new PC_Color(r, g, b);
            rand = new Random();
        }

        if (aidEnabled && worldObj.isRemote)
        {
            double d = xCoord + rand.nextFloat();
            double d1 = yCoord + 1.1D;
            double d2 = zCoord + rand.nextFloat();
            int a = rand.nextInt(3);
            int b = rand.nextInt(3);
            PC_Utils.spawnParticle("PC_EntityLaserParticleFX", worldObj, new PC_CoordD(d, d1, d2), aidcolor, new PC_CoordD(), 0);

            for (int q = 0; q < 8; q++)
            {
                d = xCoord + coordOffset.x + rand.nextFloat();
                d1 = yCoord + coordOffset.y + rand.nextFloat();
                d2 = zCoord + coordOffset.z + rand.nextFloat();
                a = rand.nextInt(3);
                b = rand.nextInt(3);

                while (a == b)
                {
                    b = rand.nextInt(3);
                }

                boolean aa = rand.nextBoolean();
                boolean bb = rand.nextBoolean();

                switch (a)
                {
                    case 0:
                        d = aa ? Math.floor(d) : Math.ceil(d);
                        break;

                    case 1:
                        d1 = aa ? Math.floor(d1) : Math.ceil(d1);
                        break;

                    case 2:
                        d2 = aa ? Math.floor(d2) : Math.ceil(d2);
                        break;
                }

                switch (b)
                {
                    case 0:
                        d = bb ? Math.floor(d) : Math.ceil(d);
                        break;

                    case 1:
                        d1 = bb ? Math.floor(d1) : Math.ceil(d1);
                        break;

                    case 2:
                        d2 = bb ? Math.floor(d2) : Math.ceil(d2);
                        break;
                }

                PC_Utils.spawnParticle("PC_EntityLaserParticleFX", worldObj, new PC_CoordD(d, d1, d2), aidcolor, new PC_CoordI(), 0);
            }
        }

        super.updateEntity();
    }

    @Override
    public boolean canPlayerInsertStackTo(int slot, ItemStack stack)
    {
        if (stack.getItem() instanceof ItemBlock)
        {
            return true;
        }

        return false;
    }

    @Override
    public int getSizeInventory()
    {
        return SIZE;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return buildBlock;
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        if (j > 0)
        {
            ItemStack itemStack = buildBlock;
            buildBlock = null;
            return itemStack;
        }

        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (buildBlock != null)
        {
            ItemStack itemstack = buildBlock;
            buildBlock = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        buildBlock = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }

        onInventoryChanged();
    }

    @Override
    public String getInvName()
    {
        return "Block Replacer";
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        System.out.println("readFromNBT");
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");

        if (nbttaglist.tagCount() > 0)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(0);
            buildBlock = ItemStack.loadItemStackFromNBT(nbttagcompound1);
        }

        PC_Utils.loadFromNBT(nbttagcompound, "targetPos", coordOffset);
        state = nbttagcompound.getBoolean("state");
        aidEnabled = nbttagcompound.getBoolean("aid");
        extraMeta = nbttagcompound.getInteger("extraMeta");

        if (coordOffset.equals(new PC_CoordI(0, 0, 0)))
        {
            coordOffset.setTo(0, 1, 0);
        }

        init = false;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();

        if (buildBlock != null)
        {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            buildBlock.writeToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);
        }

        nbttagcompound.setTag("Items", nbttaglist);
        nbttagcompound.setBoolean("state", state);
        nbttagcompound.setBoolean("aid", aidEnabled);
        nbttagcompound.setInteger("extraMeta", extraMeta);
        PC_Utils.saveToNBT(nbttagcompound, "targetPos", coordOffset);
    }

    @Override
    public int getInventoryStackLimit()
    {
        return MAXSTACK;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return false;
    }

    @Override
    public void openChest()
    {
    }

    @Override
    public void closeChest()
    {
    }

    @Override
    public boolean insertStackIntoInventory(ItemStack stack)
    {
        return PC_InvUtils.addWholeItemStackToInventory(this, stack);
    }

    @Override
    public boolean canDispenseStackFrom(int slot)
    {
        return true;
    }

    @Override
    public boolean needsSpecialInserter()
    {
        return false;
    }

    @Override
    public boolean canUpdate()
    {
        return true;
    }

    @Override
    public boolean canMachineInsertStackTo(int slot, ItemStack stack)
    {
        return canPlayerInsertStackTo(slot, stack);
    }

    @Override
    public void setData(Object o[])
    {
        int p = 0;

        while (p < o.length)
        {
            String var = (String)o[p++];

            if (var.equals("extraMeta"))
            {
                extraMeta = (Integer)o[p++];
            }
            else if (var.equals("coordOffset"))
            {
                coordOffset.setTo((Integer)o[p++], (Integer)o[p++], (Integer)o[p++]);
            }
            else if (var.equals("aidEnabled"))
            {
                aidEnabled = (Boolean)o[p++];
            }
        }
    }

    @Override
    public Object[] getData()
    {
        Object[] o = new Object[8];
        o[0] = "extraMeta";
        o[1] = extraMeta;
        o[2] = "coordOffset";
        o[3] = coordOffset.x;
        o[4] = coordOffset.y;
        o[5] = coordOffset.z;
        o[6] = "aidEnabled";
        o[7] = aidEnabled;
        return o;
    }
}
