package powercraft.light;

import java.util.List;

import net.minecraft.src.EnumRarity;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import powercraft.core.PC_Color;
import powercraft.core.PC_Item;
import powercraft.core.PC_MathHelper;
import powercraft.core.PC_Utils;

public class PCli_ItemLaserComposition extends PC_Item
{
    public PCli_ItemLaserComposition(int id)
    {
        super(id);
        setMaxStackSize(1);
        setHasSubtypes(false);
    }

    @Override
    public String[] getDefaultNames()
    {
        return new String[]
                {
                    getItemName(), "Laser Composition",
                    getItemName() + ".kill", "Kill Level %s",
                    getItemName() + ".distance", "Distance Level %s"
                };
    }

    @Override
    public int getMetadata(int i)
    {
        return i;
    }

    @Override
    public String getLocalItemName(ItemStack itemstack)
    {
        NBTTagCompound nbtTagCompound = itemstack.getTagCompound();
        int levelKill = nbtTagCompound.getInteger("level.kill");
        int levelDistance = nbtTagCompound.getInteger("level.distance");
        String name = PC_Utils.tr(getItemName() + ".name");

        if (levelKill > 0)
        {
            name += ", " + PC_Utils.tr(getItemName() + ".kill.name", "" + levelKill);
        }

        if (levelDistance > 0)
        {
            name += ", " + PC_Utils.tr(getItemName() + ".distance.name", "" + levelDistance);
        }

        return name;
    }

    public PC_Color getColorForItemStack(ItemStack itemstack)
    {
        NBTTagCompound nbtTagCompound = itemstack.getTagCompound();
        int levelKill = nbtTagCompound.getInteger("level.kill");
        int levelDistance = nbtTagCompound.getInteger("level.distance");
        PC_Color c = new PC_Color();
        c.r = levelKill / 10.0;
        c.g = levelDistance / 10.0;
        return c;
    }

    @Override
    public int getColorFromItemStack(ItemStack itemStack, int pass)
    {
        return getColorForItemStack(itemStack).getHex();
    }
}
