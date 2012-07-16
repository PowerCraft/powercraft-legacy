package net.minecraft.src;



/**
 * elevator material
 * 
 * @author MightyPork
 */
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
