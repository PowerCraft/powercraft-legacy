package powercraft.transport;

import net.minecraft.src.MapColor;
import net.minecraft.src.Material;

public class PCtr_MaterialElevator extends Material {
	/**
	* @param mapcolor color on map
	*/
	public PCtr_MaterialElevator(MapColor mapcolor) {
	super(mapcolor);
	}

	@Override
	public boolean isSolid() {
	return true;
	}

	@Override
	public boolean getCanBlockGrass() {
	return false;
	}

	@Override
	public boolean blocksMovement() {
	return true;
	}
}
