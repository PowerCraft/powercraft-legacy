package powercraft.hologram;

import java.util.List;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemStack;
import powercraft.api.item.PC_ItemArmor;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.registry.PC_ModuleRegistry;
import powercraft.api.registry.PC_TextureRegistry;
import powercraft.api.renderer.PC_Renderer;

public class PChg_ItemArmorHologramGlasses extends PC_ItemArmor {

	public PChg_ItemArmorHologramGlasses(int id) {
		super(id, EnumArmorMaterial.IRON, HEAD, "glasses");
		setArmorTextureFile(PC_TextureRegistry.getTextureDirectory(PC_ModuleRegistry.getModule("Hologram"))+"glasses.png");
	}

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			List<LangEntry> names = (List<LangEntry>)obj[0];
			names.add(new LangEntry(getUnlocalizedName(), "Hologram Glasses"));
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
