package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.server.MinecraftServer;

public class TileEntityCommandBlock extends TileEntity implements ICommandSender
{
    private String field_82354_a = "";

    public void func_82352_b(String par1Str)
    {
        this.field_82354_a = par1Str;
        this.onInventoryChanged();
    }

    @SideOnly(Side.CLIENT)

    public String getCommand()
    {
        return this.field_82354_a;
    }

    public void executeCommandOnPowered(World par1World)
    {
        if (!par1World.isRemote)
        {
            MinecraftServer var2 = MinecraftServer.getServer();

            if (var2 != null && var2.isCommandBlockEnabled())
            {
                ICommandManager var3 = var2.getCommandManager();
                var3.executeCommand(this, this.field_82354_a);
            }
        }
    }

    public String getCommandSenderName()
    {
        return "@";
    }

    public void sendChatToPlayer(String par1Str) {}

    public boolean canCommandSenderUseCommand(int par1, String par2Str)
    {
        return par1 <= 2;
    }

    public String translateString(String par1Str, Object ... par2ArrayOfObj)
    {
        return par1Str;
    }

    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setString("Command", this.field_82354_a);
    }

    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.field_82354_a = par1NBTTagCompound.getString("Command");
    }

    public ChunkCoordinates getPlayerCoordinates()
    {
        return new ChunkCoordinates(this.xCoord, this.yCoord, this.zCoord);
    }

    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 2, var1);
    }
}
