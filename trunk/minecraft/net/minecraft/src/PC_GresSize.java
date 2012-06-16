package net.minecraft.src;

/**
 * Width-height int pair used in guis.
 * 
 * @author MightyPork
 * @copy (c) 2012
 *
 */
public class PC_GresSize extends PC_Struct2<Integer, Integer> {

	/**
	 * Gui size int pair
	 * @param width width
	 * @param height height
	 */
	public PC_GresSize(Integer width, Integer height) {
		super(width, height);
	}
	
	/**
	 * @return width
	 */
	public int getWidth(){
		return a;
	}
	
	/**
	 * @return height
	 */
	public int getHeight(){
		return b;
	}
	
	/**
	 * Set width
	 * @param width new width
	 * @return this
	 */
	public PC_GresSize setWidth(int width){
		a = width;
		return this;
	}
	
	/**
	 * Set height
	 * @param height new height
	 * @return this
	 */
	public PC_GresSize setHeight(int height){
		b = height;
		return this;
	}

}
