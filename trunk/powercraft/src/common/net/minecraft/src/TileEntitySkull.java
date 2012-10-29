package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class TileEntitySkull extends TileEntity
{
    private int field_82123_a;
    private int field_82121_b;
    private String field_82122_c = "";

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setByte("SkullType", (byte)(this.field_82123_a & 255));
        par1NBTTagCompound.setByte("Rot", (byte)(this.field_82121_b & 255));
        par1NBTTagCompound.setString("ExtraType", this.field_82122_c);
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.field_82123_a = par1NBTTagCompound.getByte("SkullType");
        this.field_82121_b = par1NBTTagCompound.getByte("Rot");

        if (par1NBTTagCompound.hasKey("ExtraType"))
        {
            this.field_82122_c = par1NBTTagCompound.getString("ExtraType");
        }
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 4, var1);
    }

    public void func_82118_a(int par1, String par2Str)
    {
        this.field_82123_a = par1;
        this.field_82122_c = par2Str;
    }

    public int func_82117_a()
    {
        return this.field_82123_a;
    }

    public void func_82116_a(int par1)
    {
        this.field_82121_b = par1;
    }

    @SideOnly(Side.CLIENT)
    public int func_82119_b()
    {
        return this.field_82121_b;
    }

    @SideOnly(Side.CLIENT)
    public String func_82120_c()
    {
        return this.field_82122_c;
    }
}
