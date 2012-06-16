package net.minecraft.src;

/**
 * x-y int pair used in guis.
 * 
 * @author MightyPork
 * @copy (c) 2012
 *
 */
public class PC_GresCoord extends PC_Struct2<Integer, Integer> {

	/**
	 * Gui position int pair
	 * @param x x position
	 * @param y y position
	 */
	public PC_GresCoord(Integer x, Integer y) {
		super(x, y);
	}
	
	/**
	 * @return x
	 */
	public int getX(){
		return a;
	}
	
	/**
	 * @return y
	 */
	public int getY(){
		return b;
	}
	
	/**
	 * Set x position
	 * @param x new x position
	 * @return this
	 */
	public PC_GresCoord setX(int x){
		a = x;
		return this;
	}
	
	/**
	 * Set y position
	 * @param y new y position
	 * @return this
	 */
	public PC_GresCoord setY(int y){
		b = y;
		return this;
	}

}
