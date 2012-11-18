package powercraft.core;

import java.util.List;

import net.minecraft.src.EnumRarity;
import net.minecraft.src.ItemStack;

public class PCco_ItemBlockPowerCrystal extends PC_ItemBlock
{
    public PCco_ItemBlockPowerCrystal(int id)
    {
        super(id);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public String[] getDefaultNames()
    {
        return new String[]
                {
                    getItemName() + ".color0", "Orange Power Crystal",
                    getItemName() + ".color1", "Red Power Crystal",
                    getItemName() + ".color2", "Green Power Crystal",
                    getItemName() + ".color3", "Darkblue Power Crystal",
                    getItemName() + ".color4", "Lightblue Power Crystal",
                    getItemName() + ".color5", "Purple Power Crystal",
                    getItemName() + ".color6", "Cyan Power Crystal",
                    getItemName() + ".color7", "Yellow Power Crystal",
                    getItemName(), "Power Crystal"
                };
    }

    @Override
    public int getMetadata(int i)
    {
        return i;
    }

    @Override
    public String getItemNameIS(ItemStack itemstack)
    {
        return super.getItemName() + ".color" + Integer.toString(itemstack.getItemDamage());
    }

    @Override
    public int getColorFromItemStack(ItemStack itemStack, int pass)
    {
        return PC_Color.crystal_colors[PC_MathHelper.clamp_int(itemStack.getItemDamage(), 0, 7)];
    }

    @Override
    public boolean hasEffect(ItemStack itemstack)
    {
        return true;
    }

    @Override
    public EnumRarity getRarity(ItemStack itemstack)
    {
        return EnumRarity.rare;
    }

    @Override
    public List<ItemStack> getItemStacks(List<ItemStack> arrayList)
    {
        for (int i = 0; i < 8; i++)
        {
            arrayList.add(new ItemStack(this, 1, i));
        }

        return arrayList;
    }
}
