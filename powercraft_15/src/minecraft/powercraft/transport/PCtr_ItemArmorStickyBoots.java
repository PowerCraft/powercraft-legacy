package powercraft.transport;

import java.util.List;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemStack;
import powercraft.api.item.PC_ItemArmor;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.registry.PC_ModuleRegistry;
import powercraft.api.registry.PC_TextureRegistry;

public class PCtr_ItemArmorStickyBoots extends PC_ItemArmor{
    
	public PCtr_ItemArmorStickyBoots(int id){
        super(id, EnumArmorMaterial.IRON, FEET, false);
        setIconCoord(2, 3);
        setArmorTextureFile(PC_TextureRegistry.getTextureDirectory(PC_ModuleRegistry.getModule("Transport"))+"slimeboots.png");
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
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			List<LangEntry> names = (List<LangEntry>)obj[0];
			names.add(new LangEntry(getItemName(), "Sticky Iron Boots"));
            return names;
		}
		return null;
	}
	
}
