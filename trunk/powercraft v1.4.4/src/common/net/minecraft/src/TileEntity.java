package net.minecraft.src;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class TileEntity
{
    private static Map nameToClassMap = new HashMap();

    private static Map classToNameMap = new HashMap();

    public World worldObj;

    public int xCoord;

    public int yCoord;

    public int zCoord;
    protected boolean tileEntityInvalid;
    public int blockMetadata = -1;

    public Block blockType;

    public static void addMapping(Class par0Class, String par1Str)
    {
        if (nameToClassMap.containsKey(par1Str))
        {
            throw new IllegalArgumentException("Duplicate id: " + par1Str);
        }
        else
        {
            nameToClassMap.put(par1Str, par0Class);
            classToNameMap.put(par0Class, par1Str);
        }
    }

    @SideOnly(Side.CLIENT)

    public World getWorldObj()
    {
        return this.worldObj;
    }

    public void setWorldObj(World par1World)
    {
        this.worldObj = par1World;
    }

    public boolean func_70309_m()
    {
        return this.worldObj != null;
    }

    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        this.xCoord = par1NBTTagCompound.getInteger("x");
        this.yCoord = par1NBTTagCompound.getInteger("y");
        this.zCoord = par1NBTTagCompound.getInteger("z");
    }

    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        String var2 = (String)classToNameMap.get(this.getClass());

        if (var2 == null)
        {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        }
        else
        {
            par1NBTTagCompound.setString("id", var2);
            par1NBTTagCompound.setInteger("x", this.xCoord);
            par1NBTTagCompound.setInteger("y", this.yCoord);
            par1NBTTagCompound.setInteger("z", this.zCoord);
        }
    }

    public void updateEntity() {}

    public static TileEntity createAndLoadEntity(NBTTagCompound par0NBTTagCompound)
    {
        TileEntity var1 = null;
        Class var2 = null;

        try
        {
            var2 = (Class)nameToClassMap.get(par0NBTTagCompound.getString("id"));

            if (var2 != null)
            {
                var1 = (TileEntity)var2.newInstance();
            }
        }
        catch (Exception var3)
        {
            var3.printStackTrace();
        }

        if (var1 != null)
        {
            try
            {
                var1.readFromNBT(par0NBTTagCompound);
            }
            catch (Exception e)
            {
                FMLLog.log(Level.SEVERE, e,
                        "A TileEntity %s(%s) has thrown an exception during loading, its state cannot be restored. Report this to the mod author",
                        par0NBTTagCompound.getString("id"), var2.getName());
                var1 = null;
            }
        }
        else
        {
            System.out.println("Skipping TileEntity with id " + par0NBTTagCompound.getString("id"));
        }

        return var1;
    }

    public int getBlockMetadata()
    {
        if (this.blockMetadata == -1)
        {
            this.blockMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
        }

        return this.blockMetadata;
    }

    public void onInventoryChanged()
    {
        if (this.worldObj != null)
        {
            this.blockMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
            this.worldObj.updateTileEntityChunkAndDoNothing(this.xCoord, this.yCoord, this.zCoord, this);
        }
    }

    @SideOnly(Side.CLIENT)

    public double getDistanceFrom(double par1, double par3, double par5)
    {
        double var7 = (double)this.xCoord + 0.5D - par1;
        double var9 = (double)this.yCoord + 0.5D - par3;
        double var11 = (double)this.zCoord + 0.5D - par5;
        return var7 * var7 + var9 * var9 + var11 * var11;
    }

    @SideOnly(Side.CLIENT)
    public double func_82115_m()
    {
        return 4096.0D;
    }

    public Block getBlockType()
    {
        if (this.blockType == null)
        {
            this.blockType = Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
        }

        return this.blockType;
    }

    public Packet getDescriptionPacket()
    {
        return null;
    }

    public boolean isInvalid()
    {
        return this.tileEntityInvalid;
    }

    public void invalidate()
    {
        this.tileEntityInvalid = true;
    }

    public void validate()
    {
        this.tileEntityInvalid = false;
    }

    public void receiveClientEvent(int par1, int par2) {}

    public void updateContainingBlockInfo()
    {
        this.blockType = null;
        this.blockMetadata = -1;
    }

    public void func_85027_a(CrashReportCategory par1CrashReportCategory)
    {
        par1CrashReportCategory.addCrashSectionCallable("Name", new CallableTileEntityName(this));
        CrashReportCategory.func_85068_a(par1CrashReportCategory, this.xCoord, this.yCoord, this.zCoord, this.blockType.blockID, this.blockMetadata);
    }

    static Map func_85028_t()
    {
        return classToNameMap;
    }

    static
    {
        addMapping(TileEntityFurnace.class, "Furnace");
        addMapping(TileEntityChest.class, "Chest");
        addMapping(TileEntityEnderChest.class, "EnderChest");
        addMapping(TileEntityRecordPlayer.class, "RecordPlayer");
        addMapping(TileEntityDispenser.class, "Trap");
        addMapping(TileEntitySign.class, "Sign");
        addMapping(TileEntityMobSpawner.class, "MobSpawner");
        addMapping(TileEntityNote.class, "Music");
        addMapping(TileEntityPiston.class, "Piston");
        addMapping(TileEntityBrewingStand.class, "Cauldron");
        addMapping(TileEntityEnchantmentTable.class, "EnchantTable");
        addMapping(TileEntityEndPortal.class, "Airportal");
        addMapping(TileEntityCommandBlock.class, "Control");
        addMapping(TileEntityBeacon.class, "Beacon");
        addMapping(TileEntitySkull.class, "Skull");
    }

    public boolean canUpdate()
    {
        return true;
    }

    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
    }

    public void onChunkUnload()
    {
    }
}
