package powercraft.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public abstract class PC_TileEntity extends TileEntity
{
    private boolean isInvalidLocked = false;

    @Override
    public Packet getDescriptionPacket()
    {
        Object[] o = getData();
        
        if (o == null)
        {
            return null;
        }

        ByteArrayOutputStream data = new ByteArrayOutputStream();
        ObjectOutputStream sendData;

        try
        {
            sendData = new ObjectOutputStream(data);
            sendData.writeInt(PC_PacketHandler.PACKETTILEENTITY);
            sendData.writeInt(xCoord);
            sendData.writeInt(yCoord);
            sendData.writeInt(zCoord);
           	sendData.writeObject(o);
            sendData.writeInt(PC_PacketHandler.PACKETTILEENTITY);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return new Packet250CustomPayload("PowerCraft", data.toByteArray());
    }

    public PC_CoordI getCoord()
    {
        return new PC_CoordI(xCoord, yCoord, zCoord);
    }
    
    public void setData(Object[] o)
    {
    }

    public Object[] getData()
    {
        return null;
    }

    public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
    }

    public void lockInvalid(boolean validateLocked)
    {
        isInvalidLocked = validateLocked;
    }

    @Override
    public final void invalidate()
    {
        if (!isInvalidLocked)
        {
            super.invalidate();
            setValidate(true);
        }
    }

    @Override
    public final void validate()
    {
        if (!isInvalidLocked)
        {
            super.validate();
            setValidate(false);
        }
    }

    public void setValidate(boolean invalid)
    {
    }
}
