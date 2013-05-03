package powercraft.hologram;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemStack;
import powercraft.api.item.PC_ItemArmor;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.renderer.PC_Renderer;

public class PChg_ItemArmorHologramGlasses extends PC_ItemArmor {

	public PChg_ItemArmorHologramGlasses(int id) {
		super(id, EnumArmorMaterial.IRON, HEAD, "glasses");
		setArmorTextureFile("glasses.png");
	}

	@Override
	public String getArmorTextureFile(ItemStack itemstack) {
		PC_Renderer.glEnable(0xbe2);//GL_BLEND
		return super.getArmorTextureFile(itemstack);
	}

	@Override
	public List<LangEntry> getNames(ArrayList<LangEntry> names) {
		names.add(new LangEntry(getUnlocalizedName(), "Hologram Glasses"));
        return names;
	}
	
}
