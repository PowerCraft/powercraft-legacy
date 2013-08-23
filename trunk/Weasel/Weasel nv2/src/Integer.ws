public final class Integer extends Number{

	private final int value;

	private final static char[] digits = {
        '0' , '1' , '2' , '3' , '4' , '5' ,
        '6' , '7' , '8' , '9' , 'a' , 'b' ,
        'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
        'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
        'o' , 'p' , 'q' , 'r' , 's' , 't' ,
        'u' , 'v' , 'w' , 'x' , 'y' , 'z'
    };

	public Integer(int value){
		this.value = value;
	}

	public int intValue(){
		return value;
	}

	public long longValue(){
		return value;
	}
	
	public float floatValue(){
		return value;
	}
	
	public double doubleValue(){
		return value;
	}

	public boolean equals(Object o){
		if(o instanceof Integer){
			return ((Integer)o).intValue()==value;
		}
		return false;
	}

	public static String toHexString(int value){
		String hex = "";
		do{
			int i = value % 16;
			value /= 16;
			hex += i;
		}while(value!=0);
		return hex;
	}

	private static String toUnsignedString(int i, int shift) {
        char[] buf = new char[32];
        int charPos = 32;
        int radix = 1 << shift;
        int mask = radix - 1;
        do {
            buf[--charPos] = digits[i & mask];
            i >>= shift;
        } while (i != 0);

        return new String(buf, charPos, (32 - charPos));
    }

}