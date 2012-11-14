package powercraft.core;

/**
 * Structure of 3 objects.
 * 
 * @author MightyPork
 * @copy (c) 2012
 * @param <T1> 1st object class
 * @param <T2> 2nd object class
 * @param <T3> 3rd object class
 */
public class PC_Struct3<T1, T2, T3> {
	/**
	 * 1st object
	 */
	public T1 a;

	/**
	 * 2nd object
	 */
	public T2 b;

	/**
	 * 3rd object
	 */
	public T3 c;

	/**
	 * Make structure of 3 objects
	 * 
	 * @param objA 1st object
	 * @param objB 2nd object
	 * @param objC 3rd object
	 */
	public PC_Struct3(T1 objA, T2 objB, T3 objC) {
		a = objA;
		b = objB;
		c = objC;
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
	 * @return 3rd object
	 */
	public T3 getC() {
		return c;
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
	 * @return 3rd object
	 */
	public T3 get3() {
		return c;
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
	 * Set 3rd object
	 * 
	 * @param obj 3rd object
	 */
	public void setC(T3 obj) {
		c = obj;
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

	/**
	 * Set 3rd object
	 * 
	 * @param obj 3rd object
	 */
	public void set3(T3 obj) {
		c = obj;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!this.getClass().equals(obj.getClass())) {
			return false;
		}

		PC_Struct3<?, ?, ?> t = (PC_Struct3<?, ?, ?>) obj;

		return PC_Utils.areObjectsEqual(a, t.a) && PC_Utils.areObjectsEqual(b, t.b) && PC_Utils.areObjectsEqual(c, t.c);

	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (a == null ? 0 : a.hashCode());
		hash += (b == null ? 0 : b.hashCode());
		hash += (c == null ? 0 : c.hashCode());
		return hash;
	}

	@Override
	public String toString() {
		return "STRUCT {" + a + "," + b + "," + c + "}";
	}

}