package powercraft.management;

import java.io.InputStream;
import java.security.AccessControlException;

public class PC_ModuleClassLoader extends ClassLoader {

	private Class<?> c;

	public PC_ModuleClassLoader(String name, byte[] data) {
		c = registerClass(name, data);
	}

	private Class<?> registerClass(String name, byte[] data) {
		Class<?> c = null;
		try {
			c = defineClass(name, data, 0, data.length, null);
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
	protected synchronized Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		
		Class<?> c = findLoadedClass(name);
		if (c != null) {
			if (resolve) {
				resolveClass(c);
			}
			return c;
		}
		c = Class.forName(name);
		if (c != null) {
			if (resolve) {
				resolveClass(c);
			}
			return c;
		}
		try {
			byte[] data = null;
			try {
				// lesen per InputStream
				String fileName = "";
				fileName = name.replace('.', '/');
				fileName = fileName + ".class";
				InputStream is = this.getClass().getClassLoader()
						.getResourceAsStream(fileName);
				if (is != null) {
					data = new byte[is.available()];
					is.read(data);
					is.close();
					is = null;
				}
				if (data == null) {
					throw new ClassNotFoundException(name);
				}
			} catch (AccessControlException e) {
				e.printStackTrace();
			}
			c = registerClass(name, data);
			data = null;
			if (c == null) {
				throw new ClassNotFoundException(name);
			}
			return c;
		} catch (ClassNotFoundException e) {
			System.err.println("ERROR RESOLVING CLASS: " + name);
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ClassNotFoundException("Class unknown: " + name);
		}
	}
}
