package net.minecraft.src;


/**
 * conveyer material
 * 
 * @author MightyPork
 */
public class PCtr_MaterialConveyor extends Material {

	/**
	 * the material for conveyor belts.
	 */
	public PCtr_MaterialConveyor() {
		super(MapColor.airColor);
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
