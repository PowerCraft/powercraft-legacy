package net.minecraft.src;


/**
 * Optical device tile entity (mirror, prism)
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_TileEntityOptical extends PC_TileEntity {
	/** device type ("MIRROR", "PRISM") */
	public String type = "MIRROR";

	/**
	 * List of prism's sides, flags whether there are attached glass panels.
	 * starts with up and down, but the order does not really matter here.
	 */
	private boolean[] prismSides = { false, false, false, false, false, false, false, false, false, false };

	private int mirrorColor = -1;

	/**
	 * set type to prism
	 */
	public void setPrism() {
		type = "PRISM";
	}

	/**
	 * set type to mirror
	 */
	public void setMirror() {
		type = "MIRROR";
	}

	/**
	 * @return is device prism
	 */
	public boolean isPrism() {
		return type.equals("PRISM");
	}

	/**
	 * @return is device mirror
	 */
	public boolean isMirror() {
		return type.equals("MIRROR");
	}

	/**
	 * @param i side number
	 * @return has glass panel
	 */
	public boolean getPrismSide(int i) {
		if (i < 0 || i > 9) {
			return false;
		}
		return prismSides[i];
	}

	/**
	 * Set prism's side flag (glass panel set/missing)
	 * 
	 * @param i side number
	 * @param state has glass panel
	 */
	public void setPrismSide(int i, boolean state) {
		if (i < 0 || i > 9) {
			return;
		}
		prismSides[i] = state;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setString("opttype", type);
		tag.setInteger("optMirrorColor", 1 + mirrorColor);

		tag.setBoolean("prismSide0", prismSides[0]);
		tag.setBoolean("prismSide1", prismSides[1]);
		tag.setBoolean("prismSide2", prismSides[2]);
		tag.setBoolean("prismSide3", prismSides[3]);
		tag.setBoolean("prismSide4", prismSides[4]);
		tag.setBoolean("prismSide5", prismSides[5]);
		tag.setBoolean("prismSide6", prismSides[6]);
		tag.setBoolean("prismSide7", prismSides[7]);
		tag.setBoolean("prismSide8", prismSides[8]);
		tag.setBoolean("prismSide9", prismSides[9]);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		type = tag.getString("opttype");
		prismSides[0] = tag.getBoolean("prismSide0");
		prismSides[1] = tag.getBoolean("prismSide1");
		prismSides[2] = tag.getBoolean("prismSide2");
		prismSides[3] = tag.getBoolean("prismSide3");
		prismSides[4] = tag.getBoolean("prismSide4");
		prismSides[5] = tag.getBoolean("prismSide5");
		prismSides[6] = tag.getBoolean("prismSide6");
		prismSides[7] = tag.getBoolean("prismSide7");
		prismSides[8] = tag.getBoolean("prismSide8");
		prismSides[9] = tag.getBoolean("prismSide9");
		mirrorColor = tag.getInteger("optMirrorColor") - 1;
	}

	/**
	 * @return forge method can update; false;
	 */
	@Override
	public boolean canUpdate() {
		return false;
	}

	/**
	 * Set mirror's color
	 * 
	 * @param crystalMeta damage of clicked crystal.
	 */
	public void setMirrorColor(int crystalMeta) {

		mirrorColor = crystalMeta;

	}

	/**
	 * @return filtering color of mirror.
	 */
	public int getMirrorColor() {

		return mirrorColor;

	}

}
