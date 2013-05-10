package mods.betterworld.CB.core;

import net.minecraft.nbt.NBTTagCompound;

public class BWCB_VecI {

	public static final long serialVersionUID = 33947857564642L;

	public int x;
	public int y;
	public int z;

	public BWCB_VecI() {
		this(0, 0, 0);
	}

	public BWCB_VecI(int x) {
		this(x, 0, 0);
	}

	public BWCB_VecI(int x, int y) {
		this(x, y, 0);
	}

	public BWCB_VecI(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public BWCB_VecI(BWCB_VecI vec) {
		x = vec.getX().intValue();
		y = vec.getY().intValue();
		z = vec.getZ().intValue();
	}


	public Integer getX() {
		return x;
	}


	public Integer getY() {
		return y;
	}


	public Integer getZ() {
		return z;
	}


	public BWCB_VecI setX(Number x) {
		this.x = x.intValue();
		return this;
	}


	public BWCB_VecI setY(Number y) {
		this.y = y.intValue();
		return this;
	}


	public BWCB_VecI setZ(Number z) {
		this.z = z.intValue();
		return this;
	}

	public BWCB_VecI setTo(BWCB_VecI vec) {
		return setTo(vec.getX(), vec.getY(), vec.getZ());
	}

	public BWCB_VecI setTo(Number x, Number y, Number z) {
		this.x = x.intValue();
		this.y = y.intValue();
		this.z = z.intValue();
		return this;
	}

	public BWCB_VecI add(BWCB_VecI vec) {
		return add(vec.getX(), vec.getY(), vec.getZ());
	}

	public BWCB_VecI add(Number n) {
		return add(n, n, n);
	}

	public BWCB_VecI add(Number x, Number y, Number z) {
		this.x += x.doubleValue();
		this.y += y.doubleValue();
		this.z += z.doubleValue();
		return this;
	}

	public BWCB_VecI offset(BWCB_VecI vec) {
		return copy().add(vec);
	}

	public BWCB_VecI offset(Number n) {
		return copy().add(n);
	}

	public BWCB_VecI offset(Number x, Number y, Number z) {
		return copy().add(x, y, z);
	}

	public BWCB_VecI sub(BWCB_VecI vec) {
		return sub(vec.getX(), vec.getY(), vec.getZ());
	}

	public BWCB_VecI sub(Number n) {
		return sub(n, n, n);
	}

	public BWCB_VecI sub(Number x, Number y, Number z) {
		this.x -= x.doubleValue();
		this.y -= y.doubleValue();
		this.z -= z.doubleValue();
		return this;
	}

	public BWCB_VecI mul(BWCB_VecI vec) {
		return mul(vec.getX(), vec.getY(), vec.getZ());
	}

	public BWCB_VecI mul(Number n) {
		return mul(n, n, n);
	}

	public BWCB_VecI mul(Number x, Number y, Number z) {
		this.x = (int) (this.x * x.doubleValue());
		this.y = (int) (this.y * y.doubleValue());
		this.z = (int) (this.z * z.doubleValue());
		return this;
	}

	public BWCB_VecI div(BWCB_VecI vec) {
		return div(vec.getX(), vec.getY(), vec.getZ());
	}

	public BWCB_VecI div(Number n) {
		return div(n, n, n);
	}

	public BWCB_VecI div(Number x, Number y, Number z) {
		this.x /= x.doubleValue();
		this.y /= y.doubleValue();
		this.z /= z.doubleValue();
		return this;
	}

	public double length() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public double distanceTo(BWCB_VecI vec) {
		return copy().sub(vec).length();
	}

	public double distanceTo(Number x, Number y, Number z) {
		return copy().sub(x, y, z).length();
	}

	public BWCB_VecI normalize() {
		double length = length();
		x /= length;
		y /= length;
		z /= length;
		return this;
	}

	public BWCB_VecI clamp(BWCB_VecI min, BWCB_VecI max) {
		int minVal, maxVal;
		minVal = min.getX().intValue();
		maxVal = max.getX().intValue();
		if (minVal > maxVal) {
			if (x < minVal)
				x = minVal;
			else if (x > maxVal)
				x = maxVal;
		}
		minVal = min.getY().intValue();
		maxVal = max.getY().intValue();
		if (minVal > maxVal) {
			if (y < minVal)
				y = minVal;
			else if (y > maxVal)
				y = maxVal;
		}
		minVal = min.getZ().intValue();
		maxVal = max.getZ().intValue();
		if (minVal > maxVal) {
			if (z < minVal)
				z = minVal;
			else if (z > maxVal)
				z = maxVal;
		}
		return this;
	}

	public BWCB_VecI clamp(BWCB_VecI min, Integer max) {
		return clamp(min, new BWCB_VecI(max, max, max));
	}

	public BWCB_VecI clamp(Integer min, BWCB_VecI max) {
		return clamp(new BWCB_VecI(min, min, min), max);
	}

	public BWCB_VecI clamp(Integer min, Integer max) {
		return clamp(new BWCB_VecI(min, min, min), new BWCB_VecI(max, max, max));
	}

	public BWCB_VecI max(BWCB_VecI max) {
		int maxVal;
		maxVal = max.getX().intValue();
		if (x > maxVal)
			x = maxVal;
		maxVal = max.getY().intValue();
		if (y > maxVal)
			y = maxVal;
		maxVal = max.getZ().intValue();
		if (z > maxVal)
			z = maxVal;
		return this;
	}

	public BWCB_VecI max(Integer max) {
		if (x > max)
			x = max;
		if (y > max)
			y = max;
		if (z > max)
			z = max;
		return this;
	}

	public BWCB_VecI min(BWCB_VecI min) {
		int minVal;
		minVal = min.getX().intValue();
		if (x < minVal)
			x = minVal;
		minVal = min.getY().intValue();
		if (y < minVal)
			y = minVal;
		minVal = min.getZ().intValue();
		if (z < minVal)
			z = minVal;
		return this;
	}

	public BWCB_VecI min(Integer min) {
		if (x < min)
			x = min;
		if (y < min)
			y = min;
		if (z < min)
			z = min;
		return this;
	}

	public BWCB_VecI copy() {
		return new BWCB_VecI(this);
	}

	public boolean equals(Object o) {
		if (!(o instanceof BWCB_VecI)) {
			return false;
		}
		BWCB_VecI vec = (BWCB_VecI) o;
		if (x != vec.getX().intValue())
			return false;
		if (y != vec.getY().intValue())
			return false;
		if (z != vec.getZ().intValue())
			return false;
		return true;
	}

	public BWCB_VecI readFromNBT(NBTTagCompound nbttag) {
		x = nbttag.getInteger("x");
		y = nbttag.getInteger("y");
		z = nbttag.getInteger("z");
		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbttag) {
		nbttag.setInteger("x", x);
		nbttag.setInteger("y", y);
		nbttag.setInteger("z", z);
		return nbttag;
	}

	public String toString() {
		return "Vec[" + x + ", " + y + ", " + z + "]";
	}

}
