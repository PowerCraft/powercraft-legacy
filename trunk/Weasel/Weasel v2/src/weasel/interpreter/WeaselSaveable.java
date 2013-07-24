package weasel.interpreter;

import java.io.DataOutputStream;
import java.io.IOException;

public interface WeaselSaveable {

	public void saveToDataStream(DataOutputStream dataOutputStream) throws IOException;
	
}
