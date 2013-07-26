package powercraft.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.structure.PC_ItemStructure;
import powercraft.api.structure.PC_StructureType;

public class PCco_ItemTubeCarbonNanotube extends PC_ItemStructure {

	public PCco_ItemTubeCarbonNanotube(int id) {
		super(id, "CarbonNanotube", "CarbonNanotube");
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public List<LangEntry> getNames(ArrayList<LangEntry> names) {
		names.add(new LangEntry(getUnlocalizedName(), "Carbon nanotube"));
		return names;
	}

	@Override
	public PC_StructureType getStructureType() {
		return PC_StructureType.ENERGY;
	}

}
