package net.minecraft.src;


public class PCtr_MaterialConveyor extends Material {

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
