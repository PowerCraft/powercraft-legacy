package weasel.interpreter.io;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class WeaselVersionClassFileSaver {

	protected abstract void save(DataOutputStream dataOutputStream, WeaselClassFile weaselClassFile) throws IOException;

}
