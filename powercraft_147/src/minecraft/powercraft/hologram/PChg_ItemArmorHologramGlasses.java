package powercraft.hologram;

import java.util.List;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemStack;
import powercraft.management.PC_ItemArmor;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.registry.PC_LangRegistry.LangEntry;

public class PChg_ItemArmorHologramGlasses extends PC_ItemArmor {

	public PChg_ItemArmorHologramGlasses(int id) {
		super(id, EnumArmorMaterial.IRON, HEAD, 1);
		setArmorTextureFile(ModuleInfo.getTextureDirectory(ModuleInfo.getModule("Hologram"))+"glasses.png");
	}

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<LangEntry> names = (List<LangEntry>)obj[0];
			names.add(new LangEntry(getItemName(), "Hologram Glasses"));
            return names;
		}
		return null;
	}

	@Override
	public String getArmorTextureFile(ItemStack itemstack) {
		PC_Renderer.glEnable(0xbe2);//GL_BLEND
		return super.getArmorTextureFile(itemstack);
	}

}
