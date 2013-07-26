package powercraft.api;


public class PC_Vec3IWithRotation extends PC_Vec3I {

	public PC_Direction dir;


	public PC_Vec3IWithRotation(int x, int y, int z, PC_Direction dir) {

		super(x, y, z);
		this.dir = dir;
	}


	@Override
	public int hashCode() {

		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((dir == null) ? 0 : dir.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {

		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		PC_Vec3IWithRotation other = (PC_Vec3IWithRotation) obj;
		if (dir != other.dir) return false;
		return true;
	}


}
