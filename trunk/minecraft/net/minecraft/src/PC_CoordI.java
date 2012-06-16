package net.minecraft.src;

/**
 * Integer coordinate class, can be used to hold information about position or a
 * simple movement vector.
 * 
 * @author MightyPork
 */
public class PC_CoordI implements PC_ICoord, PC_INBT {

	@Override
	public String toString() {
		return "[" + x + ";" + y + ";" + z + "]";
	}

	/** X coordinate */
	public int x;
	/** Y coordinate */
	public int y;
	/** Z coordinate */
	public int z;

	/**
	 * Create coordinate [0,0,0]
	 * 
	 */
	public PC_CoordI() {
		x = 0;
		y = 0;
		z = 0;
	}

	/**
	 * Create coordinate [x,y,z]
	 * 
	 * @param a x
	 * @param b y
	 * @param c z
	 */
	public PC_CoordI(int a, int b, int c) {
		x = a;
		y = b;
		z = c;
	}

	/**
	 * Create coordinate [x,y,z]
	 * 
	 * @param a x
	 * @param b y
	 * @param c z
	 */
	public PC_CoordI(long a, long b, long c) {
		x = (int) a;
		y = (int) b;
		z = (int) c;
	}

	/**
	 * Copy the give coordinate
	 * 
	 * @param c source coordinate
	 */
	public PC_CoordI(PC_CoordI c) {
		x = c.x;
		y = c.y;
		z = c.z;
	}


	/**
	 * Return a copy of this coordinate
	 * 
	 * @return the copy
	 */
	public PC_CoordI copy() {
		return new PC_CoordI(x, y, z);
	}

	/**
	 * Tests if the coordinates have equal numbers.
	 * If both are null, they are also equal.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		if (!this.getClass().equals(obj.getClass())) { return false; }

		return ((PC_CoordI) obj).x == x && ((PC_CoordI) obj).y == y && ((PC_CoordI) obj).z == z;
	}

	@Override
	public int hashCode() {
		return (x + 17) ^ (y - 156) ^ z;
	};

	/**
	 * Set coordinates
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setTo(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Use coords from given coord
	 * 
	 * @param src source coord
	 */
	public void setTo(PC_CoordI src) {
		this.x = src.x;
		this.y = src.y;
		this.z = src.z;
	}

	/**
	 * Set coordinates
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setTo(long x, long y, long z) {
		this.x = (int) x;
		this.y = (int) y;
		this.z = (int) z;
	}


	@Override
	public PC_CoordI getInverted() {
		return new PC_CoordI(-x, -y, -z);
	}

	/**
	 * Check if this coord is at the same X/Z coordinates like the given one
	 * 
	 * @param compared coord to compare
	 * @return is equal in X,Z
	 */
	public boolean equalsXZ(PC_CoordI compared) {
		return x == compared.x && z == compared.z;
	}

	/**
	 * Add a coord to this one
	 * 
	 * @param added added coord
	 */
	public void add(PC_CoordI added) {
		x += added.x;
		y += added.y;
		z += added.z;
	}


	@Override
	public PC_CoordI offset(PC_CoordI added) {
		return new PC_CoordI(x + added.x, y + added.y, z + added.z);
	}

	@Override
	public PC_CoordF offset(PC_CoordF added) {
		return new PC_CoordF(x + added.x, y + added.y, z + added.z);
	}

	@Override
	public PC_CoordD offset(PC_CoordD added) {
		return new PC_CoordD(x + added.x, y + added.y, z + added.z);
	}


	@Override
	public PC_CoordI offset(int xm, int ym, int zm) {
		return new PC_CoordI(x + xm, y + ym, z + zm);
	}

	@Override
	public PC_CoordF offset(float xm, float ym, float zm) {
		return new PC_CoordF(x + xm, y + ym, z + zm);
	}

	@Override
	public PC_CoordD offset(double xm, double ym, double zm) {
		return new PC_CoordD(x + xm, y + ym, z + zm);
	}

	@Override
	public PC_CoordI offset(long xm, long ym, long zm) {
		return new PC_CoordI(x + xm, y + ym, z + zm);
	}

	/**
	 * Get copy with changed X
	 * 
	 * @param xx new x
	 * @return copy with replaced X
	 */
	public PC_CoordI setX(int xx) {
		return new PC_CoordI(xx, y, z);
	}

	/**
	 * Get copy with changed Y
	 * 
	 * @param yy new y
	 * @return copy with replaced Y
	 */
	public PC_CoordI setY(int yy) {
		return new PC_CoordI(x, yy, z);
	}

	/**
	 * Get copy with changed Z
	 * 
	 * @param zz new z
	 * @return copy with replaced Z
	 */
	public PC_CoordI setZ(int zz) {
		return new PC_CoordI(x, y, zz);
	}

	/**
	 * Get block ID at this coord
	 * 
	 * @param iba IBlockAccess
	 * @return id
	 */
	public int getId(IBlockAccess iba) {
		return iba.getBlockId(x, y, z);
	}

	/**
	 * Get block metadata at this coord
	 * 
	 * @param iba IBlockAccess
	 * @return meta
	 */
	public int getMeta(IBlockAccess iba) {
		return iba.getBlockMetadata(x, y, z);
	}

	/**
	 * Set world's block
	 * 
	 * @param w world
	 * @param id
	 * @param meta
	 * @return success
	 */
	public boolean setBlock(World w, int id, int meta) {
		return w.setBlockAndMetadataWithNotify(x, y, z, id, meta);
	}

	/**
	 * Set world's block without notification
	 * 
	 * @param w world
	 * @param id
	 * @param meta
	 * @return success
	 */
	public boolean setBlockNoNotify(World w, int id, int meta) {
		return w.setBlockAndMetadata(x, y, z, id, meta);
	}

	/**
	 * Set world's block meta
	 * 
	 * @param w world
	 * @param meta block meta
	 */
	public void setMeta(World w, int meta) {
		w.setBlockMetadataWithNotify(x, y, z, meta);
	}

	/**
	 * Set world's block id
	 * 
	 * @param w world
	 * @param id block id
	 */
	public void setId(World w, int id) {
		w.setBlockWithNotify(x, y, z, id);
	}

	/**
	 * Get tile entity in world
	 * 
	 * @param world the world
	 * @return tile entity or null
	 */
	public TileEntity getTileEntity(World world) {
		return world.getBlockTileEntity(x, y, z);
	}

	@Override
	public PC_CoordI round() {
		return copy();
	}

	@Override
	public PC_CoordI floor() {
		return copy();
	}

	@Override
	public PC_CoordI ceil() {
		return copy();
	}

	/**
	 * Calculate distance to another coordinate
	 * 
	 * @param pos
	 * @return distance
	 */
	public double distanceTo(PC_CoordI pos) {
		return Math.sqrt((x - pos.x) * (x - pos.x) + (y - pos.y) * (y - pos.y) + (z - pos.z) * (z - pos.z));
	}

	/**
	 * Calculate horizontal distance to another coordinate (= ignore y difference)
	 * 
	 * @param pos
	 * @return distance
	 */
	public double distanceHorizontalTo(PC_CoordI pos) {
		return Math.sqrt((x - pos.x) * (x - pos.x) + (z - pos.z) * (z - pos.z));
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		tag.setInteger("ix", x);
		tag.setInteger("iy", y);
		tag.setInteger("iz", z);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		x = tag.getInteger("ix");
		y = tag.getInteger("iy");
		z = tag.getInteger("iz");
	}
}
