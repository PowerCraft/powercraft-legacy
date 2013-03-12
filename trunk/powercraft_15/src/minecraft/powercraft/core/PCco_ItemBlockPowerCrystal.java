package powercraft.core;

import java.util.List;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import powercraft.api.PC_Color;
import powercraft.api.PC_MathHelper;
import powercraft.api.block.PC_ItemBlock;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.registry.PC_MSGRegistry;

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
	public String getUnlocalizedName(ItemStack itemstack) {
    	return super.getUnlocalizedName() + ".color" + Integer.toString(itemstack.getItemDamage());
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

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			List<LangEntry> names = (List<LangEntry>)obj[0];
			names.add(new LangEntry(getUnlocalizedName() + ".color0", "Orange Power Crystal"));
            names.add(new LangEntry(getUnlocalizedName() + ".color1", "Red Power Crystal"));
            names.add(new LangEntry(getUnlocalizedName() + ".color2", "Green Power Crystal"));
            names.add(new LangEntry(getUnlocalizedName() + ".color3", "Darkblue Power Crystal"));
            names.add(new LangEntry(getUnlocalizedName() + ".color4", "Lightblue Power Crystal"));
            names.add(new LangEntry(getUnlocalizedName() + ".color5", "Purple Power Crystal"));
            names.add(new LangEntry(getUnlocalizedName() + ".color6", "Cyan Power Crystal"));
            names.add(new LangEntry(getUnlocalizedName() + ".color7", "Yellow Power Crystal"));
            return names;
		}
		return null;
	}

    
    
}
