package net.minecraft.src;


/**
 * Structure of 2 objects.
 * 
 * @author MightyPork
 * @copy (c) 2012
 * @param <T1> 1st object class
 * @param <T2> 2nd object class
 */
public class PC_Struct2<T1, T2> {
	/**
	 * 1st object
	 */
	public T1 a;

	/**
	 * 2nd object
	 */
	public T2 b;

	/**
	 * Make structure of 2 objects
	 * 
	 * @param objA 1st object
	 * @param objB 2nd object
	 */
	public PC_Struct2(T1 objA, T2 objB) {
		a = objA;
		b = objB;
	}

	/**
	 * @return 1st object
	 */
	public T1 getA() {
		return a;
	}

	/**
	 * @return 2nd object
	 */
	public T2 getB() {
		return b;
	}

	/**
	 * @return 1st object
	 */
	public T1 get1() {
		return a;
	}

	/**
	 * @return 2nd object
	 */
	public T2 get2() {
		return b;
	}


	/**
	 * Set 1st object
	 * 
	 * @param obj 1st object
	 */
	public void setA(T1 obj) {
		a = obj;
	}

	/**
	 * Set 2nd object
	 * 
	 * @param obj 2nd object
	 */
	public void setB(T2 obj) {
		b = obj;
	}

	/**
	 * Set 1st object
	 * 
	 * @param obj 1st object
	 */
	public void set1(T1 obj) {
		a = obj;
	}

	/**
	 * Set 2nd object
	 * 
	 * @param obj 2nd object
	 */
	public void set2(T2 obj) {
		b = obj;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!this.getClass().equals(obj.getClass())) {
			return false;
		}

		PC_Struct2<?, ?> t = (PC_Struct2<?, ?>) obj;

		return PC_Utils.areObjectsEqual(a, t.a) && PC_Utils.areObjectsEqual(b, t.b);

	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (a == null ? 0 : a.hashCode());
		hash += (b == null ? 0 : b.hashCode());
		return hash;
	}

	@Override
	public String toString() {
		return "STRUCT {" + a + "," + b + "}";
	}

}
