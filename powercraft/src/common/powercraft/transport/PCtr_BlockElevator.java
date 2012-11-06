package powercraft.transport;

import net.minecraft.src.MapColor;
import net.minecraft.src.Material;
import powercraft.core.PC_Block;
import powercraft.core.PC_ISwapTerrain;

public class PCtr_BlockElevator extends PC_Block implements PC_ISwapTerrain {

	protected PCtr_BlockElevator(int id) {
		super(id, new PCtr_MaterialElevator(MapColor.airColor));
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getTerrainFile() {
		return mod_PowerCraftTransport.getInstance().getTerrainFile();
	}

	@Override
	public String getDefaultName() {
		return "Elevator";
	}
	
	

}
