package mods.betterworld.CB.core;

import java.io.Serializable;



public class BWCB_Struct2<T1, T2> implements Serializable {
	
	public static final long serialVersionUID = 9182838373626L;
	
	public T1 a;
	public T2 b;

	public BWCB_Struct2(T1 objA, T2 objB) {
		a = objA;
		b = objB;
	}
	public T1 getA() {
		return a;
	}

	public T2 getB() {
		return b;
	}

	public T1 get1() {
		return a;
	}

	public T2 get2() {
		return b;
	}

	public void setA(T1 obj) {
		a = obj;
	}

	public void setB(T2 obj) {
		b = obj;
	}

	public void set1(T1 obj) {
		a = obj;
	}

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

		BWCB_Struct2<?, ?> t = (BWCB_Struct2<?, ?>) obj;
		return areObjectsEqual(a, t.a) && areObjectsEqual(b, t.b);
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
	
	public static boolean areObjectsEqual(Object a, Object b) {
		return a == null ? b == null : a.equals(b);
	}
	

}
