package powercraft.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;

import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.tube.PC_ItemTube;
import powercraft.api.tube.PC_TubeType;

public class PCco_ItemTubeCarbonNanotube extends PC_ItemTube {

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
	public PC_TubeType getTubeType() {
		return PC_TubeType.ENERGY;
	}

}
