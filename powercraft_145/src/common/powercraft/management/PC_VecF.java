package powercraft.management;

import java.io.Serializable;

import net.minecraft.src.NBTTagCompound;

public class PC_VecF implements Serializable, PC_Vec<Float, PC_VecF> {
	
	public float x;
	public float y;
	public float z;
	
	public PC_VecF(){
		this(0.0f, 0.0f, 0.0f);
	}
	
	public PC_VecF(float x){
		this(x, 0.0f, 0.0f);
	}
	
	public PC_VecF(float x, float y){
		this(x, y, 0.0f);
	}
	
	public PC_VecF(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public PC_VecF(PC_Vec vec){
		x = vec.getX().floatValue();
		y = vec.getY().floatValue();
		z = vec.getZ().floatValue();
	}

	@Override
	public Float getX() {
		return x;
	}

	@Override
	public Float getY() {
		return y;
	}

	@Override
	public Float getZ() {
		return z;
	}

	@Override
	public PC_VecF setX(Float x) {
		this.x = x;
		return this;
	}

	@Override
	public PC_VecF setY(Float y) {
		this.y = y;
		return this;
	}

	@Override
	public PC_VecF setZ(Float z) {
		this.z = z;
		return this;
	}

	@Override
	public PC_VecF setTo(PC_Vec vec) {
		x = vec.getX().floatValue();
		y = vec.getY().floatValue();
		z = vec.getZ().floatValue();
		return this;
	}

	@Override
	public PC_VecF setTo(Float x, Float y, Float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	@Override
	public PC_VecF add(PC_Vec vec) {
		return add(vec.getX().floatValue(), vec.getY().floatValue(), vec.getZ().floatValue());
	}

	@Override
	public PC_VecF add(Float n) {
		return add(n, n, n);
	}

	@Override
	public PC_VecF add(Float x, Float y, Float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	@Override
	public PC_VecF offset(PC_Vec vec) {
		return copy().add(vec);
	}
	
	@Override
	public PC_VecF offset(Float n) {
		return copy().add(n);
	}
	
	@Override
	public PC_VecF offset(Float x, Float y, Float z) {
		return copy().add(x, y, z);
	}
	
	@Override
	public PC_VecF sub(PC_Vec vec) {
		return sub(vec.getX().floatValue(), vec.getY().floatValue(), vec.getZ().floatValue());
	}

	@Override
	public PC_VecF sub(Float n) {
		return sub(n, n, n);
	}

	@Override
	public PC_VecF sub(Float x, Float y, Float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	@Override
	public PC_VecF mul(PC_Vec vec) {
		return mul(vec.getX().floatValue(), vec.getY().floatValue(), vec.getZ().floatValue());
	}

	@Override
	public PC_VecF mul(Float n) {
		return mul(n, n, n);
	}

	@Override
	public PC_VecF mul(Float x, Float y, Float z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
		return this;
	}

	@Override
	public PC_VecF div(PC_Vec vec) {
		return div(vec.getX().floatValue(), vec.getY().floatValue(), vec.getZ().floatValue());
	}

	@Override
	public PC_VecF div(Float n) {
		return div(n, n, n);
	}

	@Override
	public PC_VecF div(Float x, Float y, Float z) {
		this.x /= x;
		this.y /= y;
		this.z /= z;
		return this;
	}

	@Override
	public double length() {
		return Math.sqrt(x*x + y*y + z*z);
	}

	@Override
	public PC_VecF normalize() {
		double length = length();
		x /= length;
		y /= length;
		z /= length;
		return this;
	}

	@Override
	public PC_VecF clamp(PC_Vec min, PC_Vec max) {
		float minVal, maxVal;
		minVal = min.getX().floatValue();
		maxVal = max.getX().floatValue();
		if(minVal>maxVal){
			if(x<minVal)
				x = minVal;
			else if(x>maxVal)
				x = maxVal;
		}
		minVal = min.getY().floatValue();
		maxVal = max.getY().floatValue();
		if(minVal>maxVal){
			if(y<minVal)
				y = minVal;
			else if(y>maxVal)
				y = maxVal;
		}
		minVal = min.getZ().floatValue();
		maxVal = max.getZ().floatValue();
		if(minVal>maxVal){
			if(z<minVal)
				z = minVal;
			else if(z>maxVal)
				z = maxVal;
		}
		return this;
	}

	@Override
	public PC_VecF clamp(PC_Vec min, Float max) {
		return clamp(min, new PC_VecF(max, max, max));
	}

	@Override
	public PC_VecF clamp(Float min, PC_Vec max) {
		return clamp(new PC_VecF(min, min, min), max);
	}

	@Override
	public PC_VecF clamp(Float min, Float max) {
		return clamp(new PC_VecF(min, min, min), new PC_VecF(max, max, max));
	}

	@Override
	public PC_VecF max(PC_Vec max) {
		float maxVal;
		maxVal = max.getX().floatValue();
		if(x>maxVal)
			x = maxVal;
		maxVal = max.getY().floatValue();
		if(y>maxVal)
			y = maxVal;
		maxVal = max.getZ().floatValue();
		if(z>maxVal)
			z = maxVal;
		return this;
	}

	@Override
	public PC_VecF max(Float max) {
		if(x>max)
			x = max;
		if(y>max)
			y = max;
		if(z>max)
			z = max;
		return this;
	}

	@Override
	public PC_VecF min(PC_Vec min) {
		float minVal;
		minVal = min.getX().floatValue();
		if(x<minVal)
			x = minVal;
		minVal = min.getY().floatValue();
		if(y<minVal)
			y = minVal;
		minVal = min.getZ().floatValue();
		if(z<minVal)
			z = minVal;
		return this;
	}

	@Override
	public PC_VecF min(Float min) {
		if(x<min)
			x = min;
		if(y<min)
			y = min;
		if(z<min)
			z = min;
		return this;
	}

	@Override
	public PC_VecF copy() {
		return new PC_VecF(this);
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof PC_Vec)){
			return false;
		}
		PC_Vec vec = (PC_Vec)o;
		if(x != vec.getX().floatValue())
			return false;
		if(y != vec.getY().floatValue())
			return false;
		if(z != vec.getZ().floatValue())
			return false;
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttag) {
		x = nbttag.getFloat("x");
		y = nbttag.getFloat("y");
		z = nbttag.getFloat("z");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttag) {
		nbttag.setFloat("x", x);
		nbttag.setFloat("y", y);
		nbttag.setFloat("z", z);
	}
	
}
