package powercraft.machines;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import powercraft.api.PC_Color;
import powercraft.api.PC_Utils.ValueWriting;
import powercraft.api.PC_VecF;
import powercraft.api.PC_VecI;
import powercraft.api.annotation.PC_ClientServerSync;
import powercraft.api.inventory.PC_ISpecialAccessInventory;
import powercraft.api.inventory.PC_InventoryUtils;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.tileentity.PC_TileEntityWithInventory;

public class PCma_TileEntityReplacer extends PC_TileEntityWithInventory{
	
	private static Random rand = new Random();

    @PC_ClientServerSync
    private PC_VecI coordOffset = new PC_VecI(0, 1, 0);
    @PC_ClientServerSync
    private boolean aidEnabled = false;
    @PC_ClientServerSync
    private PC_Color aidcolor;
    @PC_ClientServerSync(clientChangeAble=false)
    private boolean state = false;

    public int extraMeta = -1;
    
    public PCma_TileEntityReplacer(){
    	super("Block Replacer", 1);
    	float used = 2.0f;
        float r = rand.nextFloat();
        used -= r;
        float g = rand.nextFloat();
        used -= g;
        float b = used;

        if (rand.nextBoolean())
        {
            float f = r;
            r = g;
            g = f;
        }

        if (rand.nextBoolean())
        {
        	float f = g;
            g = b;
            b = f;
        }

        if (rand.nextBoolean())
        {
        	float f = b;
            b = r;
            r = f;
        }
        aidcolor = new PC_Color(r, g, b);
    }

	public PC_VecI getCoordOffset() {
		return coordOffset;
	}

	public void setCoordOffset(PC_VecI coordOffset) {
		if(!this.coordOffset.equals(coordOffset)){
			this.coordOffset = coordOffset.copy();
			notifyChanges("coordOffset");
		}
	}

	public boolean isAidEnabled() {
		return aidEnabled;
	}

	public void setAidEnabled(boolean aidEnabled) {
		if(this.aidEnabled != aidEnabled){
			this.aidEnabled = aidEnabled;
			notifyChanges("aidEnabled");
		}
	}

	public PC_Color getAidcolor() {
		return aidcolor.copy();
	}

	public void setAidcolor(PC_Color aidcolor) {
		if(!this.aidcolor.equals(aidcolor)){
			this.aidcolor = aidcolor.copy();
			notifyChanges("aidcolor");
		}
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		if(this.state != state){
			this.state = state;
			notifyChanges("state");
		}
	}

	@Override
    public void updateEntity()
    {
        if (isAidEnabled() && worldObj.isRemote)
        {
            double d = xCoord + rand.nextFloat();
            double d1 = yCoord + 1.1f;
            double d2 = zCoord + rand.nextFloat();
            int a = rand.nextInt(3);
            int b = rand.nextInt(3);
            ValueWriting.spawnParticle("PC_EntityLaserParticleFX", worldObj, new PC_VecF((float)d, (float)d1, (float)d2), getAidcolor(), new PC_VecF(), 0);

            for (int q = 0; q < 8; q++)
            {
            	PC_VecI coordOffset = getCoordOffset();
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

                ValueWriting.spawnParticle("PC_EntityLaserParticleFX", worldObj, new PC_VecF((float)d, (float)d1, (float)d2), getAidcolor(), new PC_VecF(), 0);
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
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
    	extraMeta = -1;
        super.setInventorySlotContents(i, itemstack);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        extraMeta = nbttagcompound.getInteger("extraMeta");

    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("extraMeta", extraMeta);
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }
    
    @Override
    public boolean canUpdate()
    {
        return true;
    }
    
}
