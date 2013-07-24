package weasel.interpreter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class WeaselUserDataSaver {

	private static HashMap<String, WeaselUserDataSaver> userDataSavers = new HashMap<String, WeaselUserDataSaver>();
	
	public static void saveUserDataToDataStream(WeaselInterpreter interpreter, DataOutputStream dataOutputStream, Object userData) throws IOException{
		for(Entry<String, WeaselUserDataSaver> e:userDataSavers.entrySet()){
			if(e.getValue().canSave(userData.getClass())){
				dataOutputStream.writeUTF(e.getKey());
				e.getValue().save(interpreter, dataOutputStream, userData);
				return;
			}
		}
		dataOutputStream.writeUTF("None");
	}

	public static Object loadUserDataFromDataStream(WeaselInterpreter interpreter, DataInputStream dataInputStream) throws IOException{
		WeaselUserDataSaver userDataSaver = userDataSavers.get(dataInputStream.readUTF());
		if(userDataSaver==null)
			return null;
		return userDataSaver.load(interpreter, dataInputStream);
	}
	
	protected abstract boolean canSave(Class<?> c);

	protected abstract void save(WeaselInterpreter interpreter, DataOutputStream dataOutputStream, Object userData) throws IOException;
	
	protected abstract Object load(WeaselInterpreter interpreter, DataInputStream dataInputStream) throws IOException;
	
}
