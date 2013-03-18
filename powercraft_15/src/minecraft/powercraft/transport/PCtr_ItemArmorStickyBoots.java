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
        super(id, EnumArmorMaterial.IRON, FEET, "stickyboots");
        setArmorTextureFile(PC_TextureRegistry.getPowerCraftImageDir()+"slimeboots.png");
    }

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			List<LangEntry> names = (List<LangEntry>)obj[0];
			names.add(new LangEntry(getUnlocalizedName(), "Sticky Iron Boots"));
            return names;
		}
		return null;
	}
	
}
