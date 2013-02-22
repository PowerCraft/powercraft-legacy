package powercraft.transport;

import java.util.List;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemStack;
import powercraft.management.PC_ItemArmor;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.registry.PC_LangRegistry.LangEntry;

public class PCtr_ItemArmorStickyBoots extends PC_ItemArmor{
    
	public PCtr_ItemArmorStickyBoots(int id){
        super(id, EnumArmorMaterial.IRON, FEET, false);
        setIconCoord(2, 3);
        setArmorTextureFile(ModuleInfo.getTextureDirectory(ModuleInfo.getModule("Transport"))+"slimeboots.png");
    }

    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        return 0x99ff99;
    }
    
    @Override
    public int getColor(ItemStack par1ItemStack)
    {
        return 0x99ff99;
    }

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<LangEntry> names = (List<LangEntry>)obj[0];
			names.add(new LangEntry(getItemName(), "Sticky Iron Boots"));
            return names;
		}
		return null;
	}
	
}
