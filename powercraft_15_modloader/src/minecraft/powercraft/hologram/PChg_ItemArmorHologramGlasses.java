package powercraft.hologram;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Entity;
import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.ItemStack;
import powercraft.api.item.PC_ItemArmor;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.renderer.PC_Renderer;

public class PChg_ItemArmorHologramGlasses extends PC_ItemArmor {

	public PChg_ItemArmorHologramGlasses(int id) {
		super(id, EnumArmorMaterial.IRON, HEAD, "glasses");
		setArmorTextureFile("glasses.png");
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer) {
		PC_Renderer.glEnable(0xbe2);//GL_BLEND
		return super.getArmorTexture(stack, entity, slot, layer);
	}

	@Override
	public List<LangEntry> getNames(ArrayList<LangEntry> names) {
		names.add(new LangEntry(getUnlocalizedName(), "Hologram Glasses"));
        return names;
	}
	
}
