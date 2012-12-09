package powercraft.management;

public class PC_Color extends PC_VecF {

	public PC_Color(){
		this(0.0f, 0.0f, 0.0f);
	}
	
	public PC_Color(float x){
		this(x, 0.0f, 0.0f);
	}
	
	public PC_Color(float x, float y){
		this(x, y, 0.0f);
	}
	
	public PC_Color(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public PC_Color(PC_Vec vec){
		x = vec.getX().floatValue();
		y = vec.getY().floatValue();
		z = vec.getZ().floatValue();
	}
	
	public float getRed(){
		return x;
	}
	
	public float getGreen(){
		return y;
	}
	
	public float getBlue(){
		return z;
	}
	
	public PC_Color syncGL(){
		PC_Renderer.glColor3f(x, y, z);
		return this;
	}
	
	public PC_Color syncGL(float alpha){
		PC_Renderer.glColor4f(x, y, z, alpha);
		return this;
	}

	public int getHex() {
		int r255 = (int) Math.round(x * 255) & 0xff;
		int g255 = (int) Math.round(y * 255) & 0xff;
	    int b255 = (int) Math.round(z * 255) & 0xff;
	    return r255 << 16 | g255 << 8 | b255;
	}

	 public PC_Color setTo(int hex){
        x = red(hex);
        y = green(hex);
        z = blue(hex);
        return this;
	}
	
	public static float red(int hex){
        return ((hex & 0xff0000) >> 16) / 255.0f;
    }

    public static float green(int hex){
        return ((hex & 0x00ff00) >> 8) / 255.0f;
    }

    public static float blue(int hex){
        return ((hex & 0x0000ff)) / 255.0f;
    }
	 
	public static PC_Color fromHex(int hex) {
		return new PC_Color().setTo(hex);
	}
	
}
