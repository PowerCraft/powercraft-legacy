package powercraft.core;

import java.util.List;

import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;

public abstract class PC_ItemArmor extends ItemArmor implements PC_ICraftingToolDisplayer
{
    public static final int HEAD = 0, TORSO = 1, LEGS = 2, FEET = 3;

    private String craftingToolModule;

    protected PC_ItemArmor(int id, EnumArmorMaterial material, int textureID, int type)
    {
        super(id, material, textureID, type);
    }

    public abstract String getDefaultName();

    public String getCraftingToolModule()
    {
        return craftingToolModule;
    }

    public void setCraftingToolModule(String module)
    {
        craftingToolModule = module;
    }

    public List<ItemStack> getItemStacks(List<ItemStack> arrayList)
    {
        arrayList.add(new ItemStack(this));
        return arrayList;
    }
}
