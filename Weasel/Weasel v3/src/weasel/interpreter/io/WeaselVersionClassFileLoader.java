package weasel.interpreter.io;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class WeaselVersionClassFileLoader {

	protected abstract WeaselClassFile load(DataInputStream dataInputStream) throws IOException;

}
