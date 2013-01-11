package codechicken.nei;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import codechicken.core.ServerUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerSave
{
    public ItemStack[] creativeInv;
    private File saveFile;
    private NBTTagCompound saveCompound;
    public String username;
    private boolean isDirty;
    private boolean creativeInvDirty;
    public boolean wasOp;

    public PlayerSave(String playername, File saveLocation)
    {
        username = playername;
        wasOp = ServerUtils.isPlayerOP(playername);

        saveFile = new File(saveLocation, username+".dat");
        if(!saveFile.getParentFile().exists())
            saveFile.getParentFile().mkdirs();
        load();
    }

    private void load()
    {
        saveCompound = new NBTTagCompound();
        try
        {
            if(!saveFile.exists())
                saveFile.createNewFile();
            if(saveFile.length() > 0)
            {
                DataInputStream din = new DataInputStream(new FileInputStream(saveFile));
                saveCompound = (NBTTagCompound) NBTBase.readNamedTag(din);
                din.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        loadCreativeInv();
    }

    private void loadCreativeInv()
    {
        creativeInv = new ItemStack[54];
        NBTTagList itemList = saveCompound.getTagList("creativeitems");
        if(itemList != null)
        {
            for(int tagPos = 0; tagPos < itemList.tagCount(); tagPos++)
            {
                NBTTagCompound stacksave = (NBTTagCompound)itemList.tagAt(tagPos);

                creativeInv[stacksave.getByte("Slot") & 0xff] = ItemStack.loadItemStackFromNBT(stacksave);
            }
        }
    }

    public boolean getMagnetMode()
    {
        return saveCompound.getBoolean("magnetmode");
    }

    public boolean getCreativeInv()
    {
        return saveCompound.getBoolean("creativeinv");
    }

    public void setMagnetMode(boolean b)
    {
        saveCompound.setBoolean("magnetmode", b);
        setDirty();
    }

    public void setCreativeInv(boolean b)
    {
        saveCompound.setBoolean("creativeinv", b);
        setDirty();
    }

    public void save()
    {
        if(!isDirty)
            return;

        if(creativeInvDirty)
            saveCreativeInv();

        try
        {
            DataOutputStream dout = new DataOutputStream(new FileOutputStream(saveFile));
            NBTBase.writeNamedTag(saveCompound, dout);
            dout.close();
            isDirty = false;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void saveCreativeInv()
    {
        NBTTagList invsave = new NBTTagList();
        for(int i = 0; i < creativeInv.length; i++)
        {
            if(creativeInv[i] != null)
            {
                NBTTagCompound stacksave = new NBTTagCompound();
                stacksave.setByte("Slot", (byte)i);
                creativeInv[i].writeToNBT(stacksave);
                invsave.appendTag(stacksave);
            }
        }
        saveCompound.setTag("creativeitems", invsave);

        creativeInvDirty = false;
    }

    public void setCreativeDirty()
    {
        creativeInvDirty = isDirty = true;
    }

    public void setDirty()
    {
        isDirty = true;
    }
}
