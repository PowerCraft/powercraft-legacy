public final class Arrays{

	private Arrays(){}
	
	public native static <T> T[] newArray(Class<T> c, int size);
	
	public static <T> T[] arrayCopy(T[] array){
		return arrayCopy(array, 0, array.length);
	}
	
	public static <T> T[] arrayCopy(T[] array, int start, int end){
		int length = end-start;
		T[] newArray = newArray(array.getClass().getArrayClass(), length);
		for(int i=0; i<length; i++){
			newArray[i] = array[i+start];
		}
		return newArray;
	}

	public static char[] arrayCopy(char[] array){
		return arrayCopy(array, 0, array.length);
	}

	public static char[] arrayCopy(char[] array, int start, int end){
		int length = end-start;
		char[] newArray = new char[length];
		for(int i=0; i<length; i++){
			newArray[i] = array[i+start];
		}
		return newArray;
	}

	public static byte[] arrayCopy(byte[] array){
		return arrayCopy(array, 0, array.length);
	}
	
	public static byte[] arrayCopy(byte[] array, int start, int end){
		int length = end-start;
		byte[] newArray = new byte[length];
		for(int i=0; i<length; i++){
			newArray[i] = array[i+start];
		}
		return newArray;
	}
	
	public static short[] arrayCopy(short[] array){
		return arrayCopy(array, 0, array.length);
	}
	
	public static short[] arrayCopy(short[] array, int start, int end){
		int length = end-start;
		short[] newArray = new short[length];
		for(int i=0; i<length; i++){
			newArray[i] = array[i+start];
		}
		return newArray;
	}

	public static int[] arrayCopy(int[] array){
		return arrayCopy(array, 0, array.length);
	}

	public static int[] arrayCopy(int[] array, int start, int end){
		int length = end-start;
		int[] newArray = new int[length];
		for(int i=0; i<length; i++){
			newArray[i] = array[i+start];
		}
		return newArray;
	}

	public static long[] arrayCopy(long[] array){
		return arrayCopy(array, 0, array.length);
	}

	public static long[] arrayCopy(long[] array, int start, int end){
		int length = end-start;
		long[] newArray = new long[length];
		for(int i=0; i<length; i++){
			newArray[i] = array[i+start];
		}
		return newArray;
	}
	
	public static float[] arrayCopy(float[] array){
		return arrayCopy(array, 0, array.length);
	}
	
	public static float[] arrayCopy(float[] array, int start, int end){
		int length = end-start;
		float[] newArray = new float[length];
		for(int i=0; i<length; i++){
			newArray[i] = array[i+start];
		}
		return newArray;
	}
	
	public static double[] arrayCopy(double[] array){
		return arrayCopy(array, 0, array.length);
	}
	
	public static double[] arrayCopy(double[] array, int start, int end){
		int length = end-start;
		double[] newArray = new double[length];
		for(int i=0; i<length; i++){
			newArray[i] = array[i+start];
		}
		return newArray;
	}

}