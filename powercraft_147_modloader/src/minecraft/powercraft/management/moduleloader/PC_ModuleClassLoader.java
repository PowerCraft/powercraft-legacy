package powercraft.management.moduleloader;

import java.util.HashMap;

public class PC_ModuleClassLoader extends ClassLoader {

	private Class<?> c;
	private String className;
	private HashMap<String, Class> loaded = new HashMap<String, Class>();
	
	public PC_ModuleClassLoader(String name, byte[] data) {
		super(null);
		className = name;
		c = registerClass(name, data);
	}

	private Class<?> registerClass(String name, byte[] data) {
		Class<?> c = null;
		try {
			c = defineClass(name, data, 0, data.length);
			loaded.put(name, c);
			resolveClass(c);
		} catch (ClassFormatError e) {
			e.printStackTrace();
			throw e;
		}
		return c;
	}

	public Class<?> getCreateClass() {
		return c;
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if(loaded.containsKey(name)){
			return loaded.get(name);
		}
		return Class.forName(name);
	}
	
	@Override
	protected Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		return findClass(name);
	}
	
	
	
}
