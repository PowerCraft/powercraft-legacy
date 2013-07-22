package powercraft.api;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


public class PC_Reflection {

	public static Class<?> getCallerClass() {

		return getCallerClass(2);
	}


	public static Class<?> getCallerClass(int num) {

		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		if (stackTraceElements.length > 2 + num) {
			try {
				return Class.forName(stackTraceElements[2 + num].getClassName());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		PC_Logger.severe("Class %s has no %s callers, only %s", stackTraceElements[0], num, stackTraceElements.length - 2);
		return null;
	}


	public static Field findNearesBestField(Class<?> clazz, int index, Class<?> type) {

		Field fields[] = clazz.getDeclaredFields();
		Field f;
		if (index >= 0 && index < fields.length) {
			f = fields[index];
			if (type.isAssignableFrom(f.getType())) {
				return f;
			}
		} else {
			if (index < 0) index = 0;
			if (index >= fields.length) {
				index = fields.length - 1;
			}
		}
		int min = index - 1, max = index + 1;
		while (min >= 0 || max < fields.length) {
			if (max < fields.length) {
				f = fields[max];
				if (type.isAssignableFrom(f.getType())) {
					PC_Logger.warning("Field in %s which should be at index %s not found, now using index %s", clazz, index, max);
					return f;
				}
				max++;
			}
			if (min >= 0) {
				f = fields[min];
				if (type.isAssignableFrom(f.getType())) {
					PC_Logger.warning("Field in %s which should be at index %s not found, now using index %s", clazz, index, min);
					return f;
				}
				min--;
			}
		}
		PC_Logger.severe("Field in %s which should be at index %s not found", clazz, index);
		return null;
	}


	public static <T> T getValue(Class<?> clazz, Object object, int index, Class<?> type) {

		Field field = findNearesBestField(clazz, index, type);
		field.setAccessible(true);
		try {
			return (T) field.get(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public static void setValue(Class<?> clazz, Object object, int index, Class<?> type, Object value) {

		try {
			Field field = findNearesBestField(clazz, index, type);
			field.setAccessible(true);
			Field field_modifiers = Field.class.getDeclaredField("modifiers");
			field_modifiers.setAccessible(true);
			int modifier = field_modifiers.getInt(field);

			if ((modifier & Modifier.FINAL) != 0) {
				field_modifiers.setInt(field, modifier & ~Modifier.FINAL);
			}

			field.set(object, value);

			if ((modifier & Modifier.FINAL) != 0) {
				field_modifiers.setInt(field, modifier);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
