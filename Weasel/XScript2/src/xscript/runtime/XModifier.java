package xscript.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XModifier {

	public static final String PUBLIC_NAME = "public"; //$NON-NLS-1$
	public static final String PRIVATE_NAME = "private"; //$NON-NLS-1$
	public static final String PROTECTED_NAME = "protected"; //$NON-NLS-1$
	public static final String FINAL_NAME = "final"; //$NON-NLS-1$
	public static final String ABSTRACT_NAME = "abstract"; //$NON-NLS-1$
	public static final String STATIC_NAME = "static"; //$NON-NLS-1$
	public static final String NATIVE_NAME = "native"; //$NON-NLS-1$
	public static final String SYNCHRONIZED_NAME = "synchronized"; //$NON-NLS-1$
	
	public static final int PUBLIC = 1;
	public static final int PRIVATE = 2;
	public static final int PROTECTED = 4;
	public static final int FINAL = 8;
	public static final int ABSTRACT = 16;
	public static final int STATIC = 32;
	public static final int NATIVE = 64;
	public static final int SYNCHRONIZED = 128;
	
	private static final HashMap<String, Integer> map = new HashMap<String, Integer>();

	static {
		map.put(PUBLIC_NAME, PUBLIC);
		map.put(PRIVATE_NAME, PRIVATE);
		map.put(PROTECTED_NAME, PROTECTED);
		map.put(FINAL_NAME, FINAL);
		map.put(ABSTRACT_NAME, ABSTRACT);
		map.put(STATIC_NAME, STATIC);
		map.put(NATIVE_NAME, NATIVE);
		map.put(SYNCHRONIZED_NAME, SYNCHRONIZED);
	}

	public static boolean isPublic(int modifier) {
		return (modifier & PUBLIC) != 0;
	}

	public static boolean isPrivate(int modifier) {
		return (modifier & PRIVATE) != 0;
	}

	public static boolean isProtected(int modifier) {
		return (modifier & PROTECTED) != 0;
	}

	public static boolean isFinal(int modifier) {
		return (modifier & FINAL) != 0;
	}

	public static boolean isAbstract(int modifier) {
		return (modifier & ABSTRACT) != 0;
	}

	public static boolean isStatic(int modifier) {
		return (modifier & STATIC) != 0;
	}

	public static boolean isNative(int modifier) {
		return (modifier & NATIVE) != 0;
	}
	
	public static boolean isSynchronized(int modifier) {
		return (modifier & SYNCHRONIZED) != 0;
	}

	public static String[] getModifiers(int modifier) {
		List<String> list = new ArrayList<String>();
		if (isPublic(modifier)) {
			list.add(PUBLIC_NAME);
		}
		if (isPrivate(modifier)) {
			list.add(PRIVATE_NAME);
		}
		if (isProtected(modifier)) {
			list.add(PROTECTED_NAME);
		}
		if (isFinal(modifier)) {
			list.add(FINAL_NAME);
		}
		if (isAbstract(modifier)) {
			list.add(ABSTRACT_NAME);
		}
		if (isStatic(modifier)) {
			list.add(STATIC_NAME);
		}
		if (isNative(modifier)) {
			list.add(NATIVE_NAME);
		}
		if (isSynchronized(modifier)) {
			list.add(SYNCHRONIZED_NAME);
		}
		return list.toArray(new String[list.size()]);
	}

	public static String toString(int modifier) {
		String[] output = getModifiers(modifier);
		if (output.length == 0)
			return "";//$NON-NLS-1$
		if (output.length == 1)
			return output[0];
		String sOutput = "";//$NON-NLS-1$
		for (int i = 0; i < output.length - 2; i++) {
			sOutput += output[i] + ", ";//$NON-NLS-1$
		}
		sOutput += output[output.length - 2] + " & "; //$NON-NLS-1$
		sOutput += output[output.length - 1];
		return sOutput;
	}

	public static int getModifier(String name) {
		Integer i = map.get(name);
		if (i == null)
			return 0;
		return i;
	}

	private XModifier() {
		throw new InstantiationError();
	}

	public static String getSource(int modifier) {
		String[] s = getModifiers(modifier);
		String out = ""; //$NON-NLS-1$
		for (int i = 0; i < s.length; i++) {
			out += s[i] + " "; //$NON-NLS-1$
		}
		return out;
	}

}
