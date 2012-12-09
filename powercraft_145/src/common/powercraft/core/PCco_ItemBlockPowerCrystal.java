package powercraft.core;

import java.util.List;

import net.minecraft.src.EnumRarity;
import net.minecraft.src.ItemStack;
import powercraft.management.PC_Color;
import powercraft.management.PC_ItemBlock;
import powercraft.management.PC_MathHelper;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Utils;

public class PCco_ItemBlockPowerCrystal extends PC_ItemBlock {

	public PCco_ItemBlockPowerCrystal(int id)
    {
        super(id);
        setMaxDamage(0);
        setHasSubtypes(true);
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
        return PCco_BlockPowerCrystal.crystal_colors[PC_MathHelper.clamp_int(itemStack.getItemDamage(), 0, 7)];
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

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<PC_Struct3<String, String, String[]>> names = (List<PC_Struct3<String, String, String[]>>)obj[0];
			names.add(new PC_Struct3<String, String, String[]>(getItemName() + ".color0", "Orange Power Crystal", null));
            names.add(new PC_Struct3<String, String, String[]>(getItemName() + ".color1", "Red Power Crystal", null));
            names.add(new PC_Struct3<String, String, String[]>(getItemName() + ".color2", "Green Power Crystal", null));
            names.add(new PC_Struct3<String, String, String[]>(getItemName() + ".color3", "Darkblue Power Crystal", null));
            names.add(new PC_Struct3<String, String, String[]>(getItemName() + ".color4", "Lightblue Power Crystal", null));
            names.add(new PC_Struct3<String, String, String[]>(getItemName() + ".color5", "Purple Power Crystal", null));
            names.add(new PC_Struct3<String, String, String[]>(getItemName() + ".color6", "Cyan Power Crystal", null));
            names.add(new PC_Struct3<String, String, String[]>(getItemName() + ".color7", "Yellow Power Crystal", null));
            return names;
		}
		return null;
	}

    
    
}
